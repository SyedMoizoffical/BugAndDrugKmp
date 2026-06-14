//package com.medical.buganddrug.util
//
//import android.content.Context
//import android.net.Uri
//import androidx.compose.foundation.background
//import androidx.compose.foundation.border
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.unit.dp
//import com.farimarwat.pagepilot.PdfView
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.withContext
//import okhttp3.OkHttpClient
//import okhttp3.Request
//import java.io.File
//import java.io.FileOutputStream
//
//@Composable
//fun PdfFromUrlViewer(
//    context: Context,
//    pdfUrl: String
//) {
//    var pdfUri by remember { mutableStateOf<Uri?>(null) }
//    var isLoading by remember { mutableStateOf(true) }
//
//    // Download PDF when composable loads
//    LaunchedEffect(pdfUrl) {
//        pdfUri = downloadPdf(context, pdfUrl)
//        isLoading = false
//    }
//
//    Box(
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(500.dp)
//            .clip(RoundedCornerShape(8.dp))
//            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp))
//    ) {
//        pdfUri?.let {
//            PdfView(fileUri = it)
//        }
//
//        if (isLoading) {
//            Box(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.3f)),
//                contentAlignment = Alignment.Center
//            ) {
//                CircularProgressIndicator()
//            }
//        }
//    }
//}
//
//suspend fun downloadPdf(context: Context, pdfUrl: String): Uri? = withContext(Dispatchers.IO) {
//    try {
//        val client = OkHttpClient()
//        val request = Request.Builder().url(pdfUrl).build()
//        val response = client.newCall(request).execute()
//
//        if (!response.isSuccessful) return@withContext null
//
//        val file = File(context.cacheDir, "temp_download.pdf")
//        val sink = FileOutputStream(file)
//        response.body?.byteStream()?.copyTo(sink)
//        sink.close()
//
//        return@withContext Uri.fromFile(file)
//    } catch (e: Exception) {
//        e.printStackTrace()
//        return@withContext null
//    }
//}
