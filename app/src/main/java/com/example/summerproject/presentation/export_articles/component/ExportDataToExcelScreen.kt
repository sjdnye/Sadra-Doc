package com.example.summerproject.presentation.export_articles.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.summerproject.presentation.export_articles.ExportArticlesViewModel
import com.example.summerproject.presentation.export_articles.UiEvent
import com.example.summerproject.ui.theme.*
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Destination
@Composable
fun ExportArticleToExcel(
    navigator: DestinationsNavigator,
    viewModel: ExportArticlesViewModel = hiltViewModel()
) {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    var selectedYear by remember {
        mutableStateOf<String>("")
    }
    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is UiEvent.ShowSnackBar -> {
                    scope.launch {
                        scaffoldState.snackbarHostState.showSnackbar(
                            message = event.message,
                        )
                    }

                }
            }
        }
    }
    Scaffold(
        scaffoldState = scaffoldState,
        snackbarHost = {
            // reuse default SnackbarHost to have default animation and timing handling
            SnackbarHost(it) { data ->
                // custom snackbar with the custom colors
                Snackbar(
                    //contentColor = ...,
                    snackbarData = data,
                    backgroundColor = MaterialTheme.colors.primary,

                    )
            }
        },
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Import articles", color = MaterialTheme.colors.primary)
                },
                backgroundColor = MaterialTheme.colors.background,
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back to main panel",
                        Modifier
                            .clickable {
                                navigator.popBackStack()
                            }
                    )
                },
                contentColor = MaterialTheme.colors.primary,
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(30.dp)
        ) {
            Text(text = "Select a specific year:",
                style = MaterialTheme.typography.body2
          )
            Spacer(modifier = Modifier.height(5.dp))
            DropDownMenuYear(exportData = {
                selectedYear = it
                viewModel.getArticlesByYear(selectedYear)
            })

            Spacer(modifier = Modifier.height(50.dp))

            Text(
                modifier = Modifier
                    .align(CenterHorizontally),
                text = "${viewModel.numberOfArticles} article(s) was found",
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = LightBlue700
                )
            )

            Spacer(modifier = Modifier.height(30.dp))

            ButtonGradiant(
                modifier = Modifier
                    .align(CenterHorizontally)
                    .clip(CircleShape),
                text = "Import to excel file",
                textColor = MaterialTheme.colors.onPrimary,
                gradiant = Brush.horizontalGradient(
                    colors = listOf(
                        LightBlue800,
                        LightBlue300
                    )
                ),
                onCLick = {
                    viewModel.exportArticlesToExcel()
                }
            )
        }
    }
}

@Composable
fun DropDownMenuYear(exportData: (selectedText: String) -> Unit) {

    var expanded by remember { mutableStateOf(false) }
    val suggestions = mutableListOf<String>()
    for (i in 2022 downTo 1990) {
        suggestions.add(i.toString())
    }
    var selectedText by remember { mutableStateOf("") }

    var textFieldSize by remember { mutableStateOf(Size.Zero) }

    val icon = if (expanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown


    Column() {
        OutlinedTextField(
            value = selectedText,
            onValueChange = {
                selectedText = it
                exportData(selectedText)
            },
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    //This value is used to assign to the DropDown the same width
                    textFieldSize = coordinates.size.toSize()
                },
            label = { Text("Year") },
            trailingIcon = {
                Icon(icon, "contentDescription",
                    Modifier.clickable { expanded = !expanded })
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(with(LocalDensity.current) { textFieldSize.width.toDp() })
        ) {
            suggestions.forEach { label ->
                DropdownMenuItem(onClick = {
                    selectedText = label
                    expanded = false
                    exportData(selectedText)
                }) {
                    Text(text = label)
                }
            }
        }
    }
}

@Composable
fun ButtonGradiant(
    text: String,
    textColor: Color,
    gradiant: Brush,
    modifier: Modifier = Modifier,
    onCLick: () -> Unit
) {

    Button(
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.Transparent
        ),
        contentPadding = PaddingValues(),
        onClick = { onCLick() },
        modifier = modifier,
        elevation = null
    ) {
        Box(
            modifier = Modifier
                .background(gradiant)
                .clip(CircleShape)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(text = text, color = textColor)
        }
    }
}