package com.example.consignmentProject.presentation.export_consignments.admin_export.component

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Storage
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.Start
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.consignmentProject.presentation.export_consignments.admin_export.AdminExportDataViewModel
import com.example.consignmentProject.presentation.export_consignments.component.DropDownMenuMonth
import com.example.consignmentProject.presentation.export_consignments.component.DropDownMenuYear
import com.example.consignmentProject.ui.theme.*
import com.example.utils.*
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@OptIn(ExperimentalPermissionsApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Destination
@Composable
fun AdminExportConsignmentsToExcel(
    navigator: DestinationsNavigator,
    viewModel: AdminExportDataViewModel = hiltViewModel()
) {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

//    val storageWritePermission =
//        rememberPermissionState(permission = Manifest.permission.WRITE_EXTERNAL_STORAGE)
//    val storageReadPermission =
//        rememberPermissionState(permission = Manifest.permission.READ_EXTERNAL_STORAGE)
//    val storageManagePermission =
//        rememberPermissionState(permission = Manifest.permission.MANAGE_EXTERNAL_STORAGE)


    val showStoragePermissionRationale = remember { mutableStateOf(ShowRationale()) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = {

        }
    )


    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is AdminExportDataViewModel.AdminUiEvent.ShowSnackBar -> {
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
                    Text(
                        text = "Export consignments by admin",
                        color = MaterialTheme.colors.primary
                    )
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

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Center) {
            if (viewModel.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 30.dp, end = 30.dp)
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Switch(
                            checked = viewModel.isYearSwitch,
                            onCheckedChange = { viewModel.isYearSwitch = it })
                        Text(text = "  Year")
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Switch(
                            checked = viewModel.isMonthSwitch,
                            onCheckedChange = { viewModel.isMonthSwitch = it }
                        )
                        Text(text = "  Month")
                    }
                }
                Spacer(modifier = Modifier.height(5.dp))

                if (viewModel.isYearSwitch) {
                    Text(
                        text = "Select a specific year:",
                        style = MaterialTheme.typography.body2
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    DropDownMenuYear(
                        exportData = {
                            viewModel.selectedYear = it
                            viewModel.getConsignments()
                        }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
                if (viewModel.isMonthSwitch) {
                    Text(
                        text = "Select a specific month:",
                        style = MaterialTheme.typography.body2
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    DropDownMenuMonth(
                        exportData = {
                            viewModel.selectedMonth = it
                            viewModel.getConsignments()
                        }
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))

                ButtonGradiant(
                    modifier = Modifier.align(Start),
                    text = "Or get all consignments",
                    textColor = MaterialTheme.colors.onPrimary,
                    gradiant = Brush.horizontalGradient(
                        colors = listOf(
                            LightBlue800,
                            LightBlue300
                        )
                    ),
                    onCLick = {
                        viewModel.selectedYear = ""
                        viewModel.getAllConsignments()
                    }
                )

                Spacer(modifier = Modifier.height(50.dp))

                Text(
                    modifier = Modifier
                        .align(CenterHorizontally),
                    text = "${viewModel.numberOfConsignments} consignment(s) was found",
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (!viewModel.isLoading) LightBlue700 else BlueGray500
                    )
                )

                Spacer(modifier = Modifier.height(30.dp))

                ButtonGradiant(
                    modifier = Modifier.align(CenterHorizontally),
                    text = "Export to excel file",
                    textColor = MaterialTheme.colors.onPrimary,
                    gradiant = Brush.horizontalGradient(
                        colors = listOf(
                            LightBlue800,
                            LightBlue300
                        )
                    ),
                    onCLick = {
                        if (viewModel.isAccessGranted()) {
                            viewModel.exportConsignmentsToExcel()
                        } else {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                                permissionLauncher.launch(
                                    arrayOf(
                                        Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                        Manifest.permission.MANAGE_EXTERNAL_STORAGE
                                    )
                                )
                                val intent =
                                    Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                                val uri = Uri.fromParts(
                                    "package",
                                    context.packageName, null
                                )
                                intent.data = uri
                                context.startActivity(intent)
                            } else {
                                permissionLauncher.launch(
                                    arrayOf(
                                        Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    )
                                )
                            }
                        }
                    }
                )
            }
//            if (showStoragePermissionRationale.value.showDialog) {
//                PermissionRationaleDialog(
//                    message = showStoragePermissionRationale.value.message,
//                    imageVector = showStoragePermissionRationale.value.imageVector!!,
//                    onRequestPermission = {
//                        showStoragePermissionRationale.value =
//                            showStoragePermissionRationale.value.copy(showDialog = false)
//                        when (showStoragePermissionRationale.value.permission) {
//                            WRITE_STORAGE_PERMISSION -> {
//                                storageWritePermission.launchPermissionRequest()
//                            }
//                            READ_STORAGE_PERMISSION -> {
//                                storageReadPermission.launchPermissionRequest()
//                            }
//                            MANAGE_STORAGE_PERMISSION -> {
//                                storageManagePermission.launchPermissionRequest()
//                            }
//                        }
//                    }
//                ) {
//                    showStoragePermissionRationale.value =
//                        showStoragePermissionRationale.value.copy(showDialog = false)
//                }
//            }
        }
    }
}

@Composable
fun DropDownMenuYear(selectedText: String, exportData: (selectedText: String) -> Unit) {

    var expanded by remember { mutableStateOf(false) }
    val suggestions = mutableListOf<String>()
    for (i in 1450 downTo 1350) {
        suggestions.add(i.toString())
    }
    var textFieldSize by remember { mutableStateOf(Size.Zero) }

    val icon = if (expanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown


    Column() {
        OutlinedTextField(
            value = selectedText,
            onValueChange = {
                exportData(it)
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
                DropdownMenuItem(
                    onClick = {
                        expanded = false
                        exportData(label)
                    }
                ) {
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
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.Transparent
        ),
        contentPadding = PaddingValues(),
        onClick = { onCLick() },
        elevation = null,
        shape = CircleShape
    ) {
        Box(
            modifier = Modifier
                .background(gradiant)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(text = text, color = textColor)
        }
    }
}