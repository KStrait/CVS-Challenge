package com.kls.cvschallenge.ui.details

import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import coil.compose.rememberImagePainter
import com.kls.cvschallenge.data.FlickrImage
import com.kls.cvschallenge.extensions.formatDate

@Composable
fun DetailsScreen(
    image: FlickrImage
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                FlickrImage(image.media.m)
            }
        }
        item {
            Text(
                text = "Title: ${image.title}",
                fontSize = 24.sp,
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            HtmlText(
                html = image.description,
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            Text(
                text = "Author: ${image.author}",
                fontSize = 20.sp,
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            Text(
                text = "Published Date: ${image.date_taken.formatDate()}",
                fontSize = 20.sp,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun FlickrImage(url: String) {
    Image(
        modifier = Modifier.wrapContentSize()
            .fillMaxWidth(0.75F)
            .defaultMinSize(minHeight = 100.dp),
        painter = rememberImagePainter(url),
        contentDescription = null,
        contentScale = ContentScale.FillWidth
    )
}

@Composable
fun HtmlText(
    modifier: Modifier,
    html: String
) {
    AndroidView(
        factory = { context ->
            TextView(context).apply {
                text = HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_LEGACY)
                movementMethod = LinkMovementMethod.getInstance()
                textSize = 20f
            }
        },
        modifier = Modifier.fillMaxWidth()
    )
}