package com.example.consignmentProject.presentation.consignments.component

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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.consignmentProject.data.local.Consignment
import com.example.consignmentProject.ui.theme.*

@Composable
fun ConsignmentItem(
    consignment: Consignment,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 10.dp,
    cutCornerSize: Dp = 30.dp,
    onDeleteClick: () -> Unit
) {
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
                text = if (!expand) "Title: ${consignment.title}" else "Title:\n ${consignment.title}",
                style = MaterialTheme.typography.h6,
                maxLines = if (!expand) 1 else 5,
                overflow = if (!expand) TextOverflow.Ellipsis else TextOverflow.Visible,
            )
            Spacer(modifier = Modifier.height(16.dp))

            if (consignment.weight.toString().isNotEmpty()) {
                Text(
                    text = "Weight: ${consignment.weight.toString()} Kg",
                    style = MaterialTheme.typography.body1,
                    maxLines = if (!expand) 1 else 5,
                    overflow = if (!expand) TextOverflow.Ellipsis else TextOverflow.Visible
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
            if (consignment.cost.toString().isNotEmpty()) {
                Text(
                    text = "Cost: ${consignment.cost.toString()} Rial",
                    style = MaterialTheme.typography.body1,
                    maxLines = if (!expand) 1 else 5,
                    overflow = if (!expand) TextOverflow.Ellipsis else TextOverflow.Visible
                )

                Spacer(modifier = Modifier.height(16.dp))
            }

            Text(
                text = "Document Number: ${consignment.documentNumber}",
                maxLines = 2,
                style = MaterialTheme.typography.body2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Data : ${consignment.year}\\${consignment.month}\\${consignment.day}",
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
                contentDescription = "Delete consignment",
                tint = MaterialTheme.colors.onBackground
            )

        }

    }
}