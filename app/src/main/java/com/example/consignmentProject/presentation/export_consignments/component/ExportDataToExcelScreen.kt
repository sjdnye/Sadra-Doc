package com.example.consignmentProject.presentation.export_consignments.component

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Environment
import android.provider.Settings
import android.util.Log
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
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.core.content.ContextCompat.startActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.example.consignmentProject.presentation.export_consignments.ExportConsignmentsViewModel
import com.example.consignmentProject.presentation.export_consignments.UiEvent
import com.example.consignmentProject.ui.theme.*
import com.example.consignmentProject.utils.isPermanentlyDenied
import com.example.utils.*
import com.google.accompanist.permissions.*
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalPermissionsApi::class, ExperimentalComposeUiApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Destination
@Composable
fun ExportConsignmentToExcel(
    navigator: DestinationsNavigator,
    viewModel: ExportConsignmentsViewModel = hiltViewModel()
) {
    val scaffoldState = rememberScaffoldState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current

//    val storageWritePermission =
//        rememberPermissionState(permission = android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
//    val storageReadPermission =
//        rememberPermissionState(permission = android.Manifest.permission.READ_EXTERNAL_STORAGE)
//    val storageManagePermission =
//        rememberPermissionState(permission = android.Manifest.permission.MANAGE_EXTERNAL_STORAGE)

//    val permissionsState = rememberMultiplePermissionsState(
//        permissions = if (SDK_INT >= Build.VERSION_CODES.R) {
//            listOf(
//                Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                Manifest.permission.READ_EXTERNAL_STORAGE,
//                Manifest.permission.MANAGE_EXTERNAL_STORAGE
//            )
//        } else {
//            listOf(
//                Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                Manifest.permission.READ_EXTERNAL_STORAGE
//            )
//        }
//    )
//    val showStoragePermissionRationale = remember { mutableStateOf(ShowRationale()) }


    var selectedYear by remember {
        mutableStateOf<String>("")
    }
    var selectedMonth by remember {
        mutableStateOf("")
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = {
        }
    )


//    val lifecycleOwner = LocalLifecycleOwner.current
//    DisposableEffect(
//        key1 = lifecycleOwner,
//        effect = {
//            val observer = LifecycleEventObserver { _, event ->
//                if (event == Lifecycle.Event.ON_START) {
//                    permissionsState.launchMultiplePermissionRequest()
//                }
//            }
//            lifecycleOwner.lifecycle.addObserver(observer)
//
//            onDispose {
//                lifecycleOwner.lifecycle.removeObserver(observer)
//            }
//        }
//    )

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
                    Text(text = "Export consignments", color = MaterialTheme.colors.primary)
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
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 30.dp, end = 30.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Row(
                        verticalAlignment = CenterVertically
                    ) {
                        Switch(
                            checked = viewModel.isYearSwitch,
                            onCheckedChange = { viewModel.isYearSwitch = it })
                        Text(text = "  Year")
                    }
                    Row(
                        verticalAlignment = CenterVertically
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
                            selectedYear = it
                            viewModel.getConsignments(year = selectedYear, month = selectedMonth)
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
                            selectedMonth = it
                            viewModel.getConsignments(
                                year = selectedYear,
                                month = selectedMonth
                            )
                        }
                    )
                }
                Spacer(modifier = Modifier.height(50.dp))

                Text(
                    modifier = Modifier
                        .align(CenterHorizontally),
                    text = "${viewModel.numberOfConsignments} consignment(s) was found",
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
                        .align(CenterHorizontally),
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
                            if (SDK_INT >= Build.VERSION_CODES.R) {
                                if (Environment.isExternalStorageManager()) {
                                    null
                                } else {
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
                                }

                            } else {
                                permissionLauncher.launch(
                                    arrayOf(
                                        Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    )
                                )
                            }
                        }
//                        if (permissionsState.allPermissionsGranted) {
//                            viewModel.exportConsignmentsToExcel()
//                        } else {
//                            handlePermissions(permissionsState) { permission: String, shouldRationale: Boolean ->
//                                when (permission) {
//                                    WRITE_STORAGE_PERMISSION -> {
//                                        if (shouldRationale) {
//                                            showStoragePermissionRationale.value =
//                                                showStoragePermissionRationale.value.copy(
//                                                    showDialog = true,
//                                                    permission = WRITE_STORAGE_PERMISSION,
//                                                    imageVector = Icons.Filled.Storage,
//                                                    message = "Sadra Doc needs write permission to create excel file"
//                                                )
//
//                                        } else {
//                                            permissionsState.permissions[0].launchPermissionRequest()
//                                        }
//
//                                    }
//                                    READ_STORAGE_PERMISSION -> {
//                                        if (shouldRationale) {
//                                            showStoragePermissionRationale.value =
//                                                showStoragePermissionRationale.value.copy(
//                                                    showDialog = true,
//                                                    permission = READ_STORAGE_PERMISSION,
//                                                    imageVector = Icons.Filled.Storage,
//                                                    message = "Sadra Doc needs read permission to create excel file"
//                                                )
//
//                                        } else {
//                                            permissionsState.permissions[1].launchPermissionRequest()
//                                        }
//
//                                    }
//                                    MANAGE_STORAGE_PERMISSION -> {
//                                        if (shouldRationale) {
//                                            showStoragePermissionRationale.value =
//                                                showStoragePermissionRationale.value.copy(
//                                                    showDialog = true,
//                                                    permission = MANAGE_STORAGE_PERMISSION,
//                                                    imageVector = Icons.Filled.Storage,
//                                                    message = "Sadra Doc needs manage storage permission to create excel file"
//                                                )
//
//                                        } else {
//                                            permissionsState.launchMultiplePermissionRequest()
//                                        }
//                                    }
//                                }
//                            }
//                        }
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
//                                permissionsState.permissions[0].launchPermissionRequest()
//                            }
//                            READ_STORAGE_PERMISSION -> {
//                                permissionsState.permissions[1].launchPermissionRequest()
//                            }
//                            MANAGE_STORAGE_PERMISSION -> {
//                                val intent =
//                                    Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
//                                val uri = Uri.fromParts(
//                                    "package",
//                                    context.packageName, null
//                                )
//                                intent.data = uri
//                                context.startActivity(intent)
//                                permissionsState.launchMultiplePermissionRequest()
//                            }
//                        }
//                    },
//                    onDismissRequest = {
//                        showStoragePermissionRationale.value =
//                            showStoragePermissionRationale.value.copy(showDialog = false)
//                    }
//                )
//            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
private fun handlePermissions(
    permissionsState: MultiplePermissionsState,
    onCallBackRequest: (permission: String, shouldRationale: Boolean) -> Unit
) {
    permissionsState.permissions.forEach { perm ->
        when (perm.permission) {
            Manifest.permission.WRITE_EXTERNAL_STORAGE -> {
                when {
                    perm.shouldShowRationale -> {
                        onCallBackRequest(WRITE_STORAGE_PERMISSION, true)
                    }
                    perm.isPermanentlyDenied() -> {
                        onCallBackRequest(WRITE_STORAGE_PERMISSION, false)
                    }
                }
            }
            Manifest.permission.READ_EXTERNAL_STORAGE -> {
                when {
                    perm.shouldShowRationale -> {
                        onCallBackRequest(READ_STORAGE_PERMISSION, true)
                    }
                    perm.isPermanentlyDenied() -> {
                        onCallBackRequest(READ_STORAGE_PERMISSION, false)
                    }
                }
            }
            Manifest.permission.MANAGE_EXTERNAL_STORAGE -> {
                when {
                    perm.shouldShowRationale -> {
                        onCallBackRequest(MANAGE_STORAGE_PERMISSION, true)
                    }
                    perm.isPermanentlyDenied() -> {
                        onCallBackRequest(MANAGE_STORAGE_PERMISSION, true)
                    }
                }
            }
        }
    }
}

@Composable
fun DropDownMenuYear(
    exportData: (selectedText: String) -> Unit
) {

    var expanded by remember { mutableStateOf(false) }
    val suggestions = mutableListOf<String>()
    for (i in 1450 downTo 1350) {
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
fun DropDownMenuMonth(
    exportData: (selectedText: String) -> Unit
) {

    var expanded by remember { mutableStateOf(false) }
    val suggestions = listOf<String>(
        "فروردین",
        "اردیبهشت",
        "خرداد",
        "تیر",
        "مرداد",
        "شهریور",
        "مهر",
        "آبان",
        "آذر",
        "دی",
        "بهمن",
        "اسفند"
    )
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
            label = { Text("Month") },
            trailingIcon = {
                Icon(icon, "contentDescription",
                    Modifier.clickable { expanded = !expanded })
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            enabled = false
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
                        selectedText = label
                        expanded = false
                        exportData(selectedText)
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

