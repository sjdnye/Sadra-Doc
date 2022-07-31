package com.example.summerproject.presentation.article_add_edit.component

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.summerproject.data.local.Article
import com.example.summerproject.presentation.article_add_edit.AddEditArticleEvent
import com.example.summerproject.presentation.article_add_edit.AddEditArticleViewModel
import com.example.summerproject.ui.theme.BlueGray300
import com.example.summerproject.ui.theme.BlueGray700
import com.example.summerproject.ui.theme.LightBlue700
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.collectLatest

@Destination
@Composable
fun AddEditArticleScreen(
    navigator: DestinationsNavigator,
    viewModel: AddEditArticleViewModel = hiltViewModel(),
    article: Article? = null
) {
    val state = viewModel.state
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is AddEditArticleViewModel.UiEvent.SaveNote -> {
                    navigator.popBackStack()
                }
                is AddEditArticleViewModel.UiEvent.ShowSnackBar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message
                    )

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
                    Text(text = "Add/Edit Articles", color = MaterialTheme.colors.primary)
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
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.onEvent(AddEditArticleEvent.SaveArticle)
                },
                backgroundColor = MaterialTheme.colors.secondary,
            ) {
                Icon(
                    imageVector = Icons.Default.Save,
                    contentDescription = "Save Article",
                    tint = MaterialTheme.colors.onPrimary
                )
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
                .padding(16.dp)
                .verticalScroll(scrollState)
        ) {

            CustomTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(5.dp)),
                value = state.englishTitle,
                hint = "Enter english title",
                label = "English Title",
                maxLine = 4
                ,
                valueChange = {
                    viewModel.onEvent(AddEditArticleEvent.ChangeEnglishTitle(it))
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            CustomTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(5.dp)),
                value = state.persianTitle,
                hint = "Enter persian title",
                label = "Persian Title",
                maxLine = 4,
                valueChange = {
                    viewModel.onEvent(AddEditArticleEvent.ChangePersianTitle(it))
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            CustomTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(5.dp)),
                value = state.institute,
                hint = "Enter institute's name",
                label = "Institute",
                maxLine = 4,
                valueChange = {
                    viewModel.onEvent(AddEditArticleEvent.ChangeInstitute(it))
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CustomTextField(
                    modifier = Modifier
                        .clip(RoundedCornerShape(5.dp))
                        .weight(1f),
                    value = state.author_name_1,
                    hint = "1'st author's name",
                    label = "1'st Name",
                    valueChange = {
                        viewModel.onEvent(AddEditArticleEvent.ChangeAuthorName1(it))
                    }
                )
                Spacer(modifier = Modifier.width(5.dp))
                CustomTextField(
                    modifier = Modifier
                        .clip(RoundedCornerShape(5.dp))
                        .weight(1f),
                    value = state.author_family_1,
                    hint = "1'st author's family",
                    label = "1'st Family",
                    valueChange = {
                        viewModel.onEvent(AddEditArticleEvent.ChangeAuthorFamily1(it))
                    }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            CustomTextField(
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.state.author_affiliation_1,
                hint = "Enter 1's author's affiliation",
                maxLine = 2,
                label = "1'st affiliation",
                valueChange = {
                    viewModel.onEvent(AddEditArticleEvent.ChangeAuthorAffiliation1(it))
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CustomTextField(
                    modifier = Modifier
                        .clip(RoundedCornerShape(5.dp))
                        .weight(1f),
                    value = state.author_name_2,
                    hint = "2'nd author's name",
                    label = "2'nd Name",
                    valueChange = {
                        viewModel.onEvent(AddEditArticleEvent.ChangeAuthorName2(it))
                    }
                )
                Spacer(modifier = Modifier.width(5.dp))
                CustomTextField(
                    modifier = Modifier
                        .clip(RoundedCornerShape(5.dp))
                        .weight(1f),
                    value = state.author_family_2,
                    hint = "2'nd author's family",
                    label = "2'nd Family",
                    valueChange = {
                        viewModel.onEvent(AddEditArticleEvent.ChangeAuthorFamily2(it))
                    }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            CustomTextField(
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.state.author_affiliation_2,
                hint = "Enter 2'nd author's affiliation",
                maxLine = 2,
                label = "2'nd affiliation",
                valueChange = {
                    viewModel.onEvent(AddEditArticleEvent.ChangeAuthorAffiliation2(it))
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CustomTextField(
                    modifier = Modifier
                        .clip(RoundedCornerShape(5.dp))
                        .weight(1f),
                    value = state.author_name_3,
                    hint = "3'rd author's name",
                    label = "3'rd Name",
                    valueChange = {
                        viewModel.onEvent(AddEditArticleEvent.ChangeAuthorName3(it))
                    }
                )
                Spacer(modifier = Modifier.width(5.dp))
                CustomTextField(
                    modifier = Modifier
                        .clip(RoundedCornerShape(5.dp))
                        .weight(1f),
                    value = state.author_family_3,
                    hint = "3'rd author's family",
                    label = "3'rd Family",
                    valueChange = {
                        viewModel.onEvent(AddEditArticleEvent.ChangeAuthorFamily3(it))
                    }
                )

            }
            Spacer(modifier = Modifier.height(16.dp))
            CustomTextField(
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.state.author_affiliation_3,
                hint = "Enter 3'rd author's affiliation",
                label = "3'rd affiliation",
                maxLine = 2,
                valueChange = {
                    viewModel.onEvent(AddEditArticleEvent.ChangeAuthorAffiliation3(it))
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            CustomTextField(
                modifier = Modifier
                    .fillMaxWidth()
                ,
                value = viewModel.state.articleTitle,
                hint = "Magazine's name",
                label = "Magazine's name",
                maxLine = 3,
                valueChange = {
                    viewModel.onEvent(AddEditArticleEvent.ChangeArticleTitle(it))
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CustomTextField(
                    modifier = Modifier
                        .clip(RoundedCornerShape(5.dp))
                        .fillMaxWidth(0.5f),
                    value = state.placeOfPrinting,
                    hint = "Enter Country",
                    label = "Country",
                    valueChange = {
                        viewModel.onEvent(AddEditArticleEvent.ChangePlaceOfPrinting(it))
                    }
                )
                Spacer(modifier = Modifier.width(5.dp))
                DropDownMenuMagazineType(
                    value = viewModel.state.articleType,
                    changeArticleType = {
                        viewModel.onEvent(AddEditArticleEvent.ChangeArticleType(it))
                    }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                CustomNumberTextField(
                    modifier = Modifier
                        .clip(RoundedCornerShape(5.dp))
                        .weight(1f),
                    value = state.year,
                    hint = "Enter year",
                    label = "Year",
                    valueChange = {
                        viewModel.onEvent(AddEditArticleEvent.ChangeYear(it))
                    }
                )
                Spacer(modifier = Modifier.width(5.dp))
                CustomNumberTextField(
                    modifier = Modifier
                        .clip(RoundedCornerShape(5.dp))
                        .weight(1f),
                    value = state.vol,
                    hint = "Enter Vol",
                    label = "VOL",
                    valueChange = {
                        viewModel.onEvent(AddEditArticleEvent.ChangeVOL(it))
                    }
                )
                Spacer(modifier = Modifier.width(5.dp))
                CustomNumberTextField(
                    modifier = Modifier
                        .clip(RoundedCornerShape(5.dp))
                        .weight(1f),
                    value = state.No,
                    hint = "Enter No.",
                    label = "No.",
                    valueChange = {
                        viewModel.onEvent(AddEditArticleEvent.ChangeNO(it))
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            CustomTextField(
                modifier = Modifier
                    .fillMaxSize(),
                value = state.content,
                hint = "Enter brief summary of BOOK/ARTICLE",
                label = "Abstract",
                maxLine = 30,
                valueChange = {
                    viewModel.onEvent(AddEditArticleEvent.ChangeContent(it))
                }
            )
        }
    }
}


@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    value: String,
    hint: String,
    label: String,
    maxLine: Int = 1,
    valueChange: (text: String) -> Unit
) {
    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = {
            valueChange(it)
        },
        placeholder = { Text(text = hint) },
        label = { Text(text = label) },
        maxLines = maxLine
    )
}

@Composable
fun CustomNumberTextField(
    modifier: Modifier = Modifier,
    value: String,
    hint: String,
    label: String,
    maxLine: Int = 1,
    valueChange: (text: String) -> Unit
) {
    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = {
            valueChange(it)
        },
        maxLines = maxLine,
        placeholder = { Text(text = hint) },
        label = { Text(text = label) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
    )

}

@Composable
fun DropDownMenuMagazineType(value: String, changeArticleType: (selectedText: String) -> Unit) {

    var expanded by remember { mutableStateOf(false) }
    val suggestions = mutableListOf<String>()
    suggestions.add("ISI")
    suggestions.add("Scientific")
    suggestions.add("Other")

    var selectedText by remember { mutableStateOf(value) }

    var textFieldSize by remember { mutableStateOf(Size.Zero) }

    val icon = if (expanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown


    Column(modifier = Modifier.fillMaxSize()) {
        OutlinedTextField(
            value = selectedText,
            onValueChange = {
                selectedText = it
                changeArticleType(selectedText)
            },
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    //This value is used to assign to the DropDown the same width
                    textFieldSize = coordinates.size.toSize()
                },
            label = { Text("Magazine's type") },
            trailingIcon = {
                Icon(icon, "contentDescription",
                    Modifier.clickable { expanded = !expanded })
            },
            readOnly = true,
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
                    changeArticleType(selectedText)
                }) {
                    Text(text = label)
                }
            }
        }
    }
}