package com.example.consignmentProject.presentation.consignment_add_edit.component

import android.annotation.SuppressLint
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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.consignmentProject.data.local.Consignment
import com.example.consignmentProject.presentation.consignment_add_edit.AddEditConsignmentEvent
import com.example.consignmentProject.presentation.consignment_add_edit.AddEditConsignmentViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalComposeUiApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Destination
@Composable
fun AddEditConsignmentScreen(
    navigator: DestinationsNavigator,
    viewModel: AddEditConsignmentViewModel = hiltViewModel(),
    consignment: Consignment? = null
) {
    val state = viewModel.state
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is AddEditConsignmentViewModel.UiEvent.SaveConsignment -> {
                    navigator.popBackStack()
                }
                is AddEditConsignmentViewModel.UiEvent.ShowSnackBar -> {
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
                    Text(text = "Add/Edit Consignments", color = MaterialTheme.colors.primary)
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
                    keyboardController?.hide()
                    viewModel.onEvent(AddEditConsignmentEvent.SaveConsignment)
                },
                backgroundColor = MaterialTheme.colors.secondary,
            ) {
                Icon(
                    imageVector = Icons.Default.Save,
                    contentDescription = "Save Consignment",
                    tint = MaterialTheme.colors.onPrimary
                )
            }
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)

        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(scrollState)
            ) {
                CustomTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(5.dp)),
                    value = state.title,
                    hint = "Enter consignment title",
                    label = "Title",
                    maxLine = 4,
                    valueChange = {
                        viewModel.onEvent(AddEditConsignmentEvent.ChangeTitle(it))
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
                CustomNumberTextField(
                    modifier = Modifier
                        .clip(RoundedCornerShape(5.dp))
                        .fillMaxWidth(),
                    value = state.documentNumber,
                    hint = "Enter document number",
                    label = "Doc No.",
                    valueChange = {
                        viewModel.onEvent(AddEditConsignmentEvent.ChangeDocumentNumber(it))
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))

                CustomNumberTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(5.dp)),
                    value = state.weight,
                    hint = "Enter consignment's weight",
                    label = "Weight",
                    valueChange = {
                        viewModel.onEvent(AddEditConsignmentEvent.ChangeWeight(it))
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
                CustomNumberTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(5.dp)),
                    value = state.cost,
                    hint = "Enter consignment's cost",
                    label = "Cost",
                    valueChange = {
                        viewModel.onEvent(AddEditConsignmentEvent.ChangeCost(it))
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
                CustomNumberTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(5.dp)),
                    value = state.year,
                    hint = "Enter year",
                    label = "Year",
                    valueChange = {
                        viewModel.onEvent(AddEditConsignmentEvent.ChangeYear(it))
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
                CustomNumberTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(5.dp)),
                    value = state.month,
                    hint = "Enter month",
                    label = "Month",
                    valueChange = {
                        viewModel.onEvent(AddEditConsignmentEvent.ChangeMonth(it))
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
                CustomNumberTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(5.dp)),
                    value = state.day,
                    hint = "Enter day",
                    label = "Day",
                    valueChange = {
                        viewModel.onEvent(AddEditConsignmentEvent.ChangeDay(it))
                    }
                )

//                    DropDownMenuMagazineType(
//                        value = viewModel.state.articleType,
//                        changeArticleType = {
//                            viewModel.onEvent(AddEditConsignmentEvent.ChangeConsignmentType(it))
//                        }
//                    )
//                Spacer(modifier = Modifier.height(16.dp))

            }
            if (viewModel.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
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