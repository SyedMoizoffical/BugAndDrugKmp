package com.medical.buganddrug

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.medical.buganddrug.ui.AppScreens

class MainActivity : ComponentActivity() {
    private val imagePicker =
        registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(5)) { uris ->
            val bytes = uris.mapNotNull { uri ->
                contentResolver.openInputStream(uri)?.use { it.readBytes() }
            }
            AndroidImagePicker.callback?.invoke(bytes)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        AndroidImagePicker.register(imagePicker)

        setContent {
AppScreens.App()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    AppScreens.App()
}