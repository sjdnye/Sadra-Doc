package com.example.summerproject.presentation.article_add_edit.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.summerproject.data.local.Article
import com.example.summerproject.presentation.article_add_edit.AddEditArticleEvent
import com.example.summerproject.presentation.article_add_edit.AddEditArticleViewModel
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
                    Text(text = "Add/Edit Articles")
                },
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = MaterialTheme.colors.onPrimary,
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
                    tint = MaterialTheme.colors.surface
                )
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(16.dp)
                .padding(top = 10.dp)
                .verticalScroll(scrollState)
        ) {

            CustomTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(5.dp)),
                value = state.englishTitle,
                hint = "Enter english title",
                label = "English Title",
                maxLine = 2,
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
                maxLine = 2,
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
                    value = state.placeOfPrinting,
                    hint = "Enter Country",
                    label = "Country",
                    valueChange = {
                        viewModel.onEvent(AddEditArticleEvent.ChangePlaceOfPrinting(it))
                    }
                )
                Spacer(modifier = Modifier.width(5.dp))
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
                    .fillMaxSize()
                    .fillMaxHeight(),
                value = state.content,
                hint = "Enter brief summary of BOOK/ARTICLE",
                label = "Content",
                maxLine = 10,
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
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )

}