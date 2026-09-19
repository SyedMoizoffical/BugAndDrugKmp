package com.medical.buganddrug.ui.onboarding

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CopyrightFooter(
    modifier: Modifier = Modifier,
    textColor: Color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
    linkColor: Color = Color(0xFF800080),
    isCompact: Boolean = true
) {
    val uriHandler = LocalUriHandler.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = if (isCompact) 12.dp else 20.dp, vertical = if (isCompact) 6.dp else 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Copyright@2026 Sindh Infectious Diseases Hospital & Research Centre/Dow University of Health Sciences",
            style = MaterialTheme.typography.bodySmall.copy(
                fontSize = if (isCompact) 10.sp else 11.sp,
                lineHeight = if (isCompact) 14.sp else 16.sp,
                fontWeight = FontWeight.Normal
            ),
            color = textColor,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(if (isCompact) 2.dp else 4.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Powered by",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 10.sp,
                ),
                color = textColor
            )
            Text(
                text = " Autobar Pvt. Ltd.",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 10.sp ,
                ),
                color = textColor,
            )
//            Text(
//                text = " • ",
//                style = MaterialTheme.typography.labelSmall.copy(
//                    fontSize = 10.sp
//                ),
//                color = textColor
//            )
//            Text(
//                text = "www.autobar.com.pk",
//                style = MaterialTheme.typography.labelSmall.copy(
//                    fontSize = 10.sp ,
//                ),
//                color = textColor,
////                modifier = Modifier.clickable(
////                    interactionSource = remember { MutableInteractionSource() },
////                    indication = null
////                ) {
////                    try {
////                        uriHandler.openUri("https://www.autobar.com.pk")
////                    } catch (e: Exception) {
////                        // ignore URI open error if no browser handler available
////                    }
////                }
//            )
        }
    }
}
