package com.example.summerproject.presentation.articles.component

import android.widget.Space
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.summerproject.data.local.Article
import com.example.summerproject.ui.theme.*
import com.ramcosta.composedestinations.annotation.Destination

@Composable
fun ArticleItem(
    article: Article,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 10.dp,
    cutCornerSize: Dp = 30.dp,
    onDeleteClick: () -> Unit
) {
    var writers by remember {
        mutableStateOf("")
    }
    var expand by remember {
        mutableStateOf(false)
    }
    val icon = if (expand) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(cornerRadius))
            .background(if (isSystemInDarkTheme()) BlueGray800 else LightBlue50)
    ) {
        IconButton(
            onClick = { expand = !expand },
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            Icon(
                imageVector = icon, contentDescription = "expand box",
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colors.onBackground,

                )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(end = 32.dp)
        ) {
                Text(
                    text = "Title: ${article.englishTitle}",
                    style = MaterialTheme.typography.h6,
                    maxLines = if (!expand) 1 else 5,
                    overflow = if (!expand) TextOverflow.Ellipsis else TextOverflow.Visible,
                )
            Spacer(modifier = Modifier.height(8.dp))
            if (article.institute.isNotBlank()) {
                Text(
                    text = "Institute: ${article.institute}",
                    style = MaterialTheme.typography.body1,
                    maxLines = if (!expand) 1 else 5,
                    overflow = if (!expand) TextOverflow.Ellipsis else TextOverflow.Visible
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            if (article.authorName_3.isNotBlank()) {
                writers =
                    "By: ${article.authorName_1} ${article.authorFamily_1}, ${article.authorName_2} ${article.authorFamily_2}, ${article.authorName_3} ${article.authorFamily_3}"
            } else if (article.authorName_2.isNotBlank()) {
                writers =
                    "By: ${article.authorName_1} ${article.authorFamily_1}, ${article.authorName_2} ${article.authorFamily_2}"
            } else {
                writers = "By: ${article.authorName_1} ${article.authorFamily_1}"
            }
            Text(
                text = writers,
                maxLines = 2,
                style = MaterialTheme.typography.body2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Year : ${article.year}",
                style = MaterialTheme.typography.body1,
                color = LightBlue600,
            )
        }
        IconButton(
            onClick = onDeleteClick,
            modifier = Modifier.align(Alignment.BottomEnd)
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete article",
                tint = MaterialTheme.colors.onBackground
            )

        }

    }
}