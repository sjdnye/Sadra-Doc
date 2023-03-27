package com.example.consignmentProject.presentation.consignments.component


import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.consignmentProject.data.local.Consignment
import com.example.consignmentProject.presentation.consignments.ConsignmentEvent
import com.example.consignmentProject.presentation.consignments.ConsignmentViewModel
import com.example.consignmentProject.presentation.consignments.component.navigationDrawer.DrawerBody
import com.example.consignmentProject.presentation.consignments.component.navigationDrawer.MenuItem
import com.example.consignmentProject.presentation.destinations.AddEditConsignmentScreenDestination
import com.example.consignmentProject.presentation.destinations.AdminExportConsignmentsToExcelDestination
import com.example.consignmentProject.presentation.destinations.ExportConsignmentToExcelDestination
import com.example.consignmentProject.utils.SimpleAlertDialog
import com.example.utils.Admin_Uid
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.google.firebase.auth.FirebaseAuth
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import kotlin.math.roundToInt

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Destination(route = "/ConsignmentScreen")
@Composable
fun ConsignmentScreen(
    navigator: DestinationsNavigator,
    viewModel: ConsignmentViewModel = hiltViewModel()
) {
    val context = LocalContext.current

//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && !Environment.isExternalStorageManager()) {
//        val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
//        val uri = Uri.fromParts(
//            "package",
//            context.packageName, null
//        )
//        intent.data = uri
//        context.startActivity(intent)
//    }


    var deleteConsignment by remember {
        mutableStateOf<Consignment?>(null)
    }

    val drawerItems = mutableListOf<MenuItem>()
    createMenuDrawer(drawerItems)

    val state = viewModel.state
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val fabHeight = 72.dp
    val fabHeightPx = with(LocalDensity.current) {
        fabHeight.roundToPx().toFloat()
    }
    val fabOffsetHeightPx = remember {
        mutableStateOf(0f)
    }
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                val newOffset = fabOffsetHeightPx.value + delta
                fabOffsetHeightPx.value = newOffset.coerceIn(-fabHeightPx, 0f)
                return Offset.Zero
            }
        }
    }
    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is ConsignmentViewModel.ConsignmentScreenUi.DeleteConsignmentCompleted -> {
                    scope.launch {
                        val result = scaffoldState.snackbarHostState.showSnackbar(
                            message = "Consignment was deleted!",
                            actionLabel = "Undo"
                        )
                        if (result == SnackbarResult.ActionPerformed) {
                            viewModel.onEvent(ConsignmentEvent.RestoreConsignment)
                        }
                    }
                }
                is ConsignmentViewModel.ConsignmentScreenUi.ShowMessage -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }
            }
        }
    }
    Scaffold(
        modifier = Modifier
            .nestedScroll(nestedScrollConnection),

        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier
                    .offset { IntOffset(x = 0, y = -fabOffsetHeightPx.value.roundToInt()) },
                onClick = {
                    navigator.navigate(AddEditConsignmentScreenDestination())
                },
                backgroundColor = MaterialTheme.colors.secondary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add consignment",
                )
            }
        },
        scaffoldState = scaffoldState,
        snackbarHost = {
            SnackbarHost(hostState = it) { data ->
                Snackbar(
                    actionColor = MaterialTheme.colors.onPrimary,
                    backgroundColor = MaterialTheme.colors.primary,
                    snackbarData = data
                )
            }
        },
//        drawerGesturesEnabled = scaffoldState.drawerState.isOpen,

        drawerContent = {
            DrawerBody(
                items = drawerItems,
                onItemClick = {
                    when (it.id) {
                        "excel" -> {
                            scope.launch {
                                scaffoldState.drawerState.close()
                            }
                            navigator.navigate(ExportConsignmentToExcelDestination())
                        }
                        "admin" -> {
                            scope.launch {
                                scaffoldState.drawerState.close()
                            }
                            navigator.navigate(AdminExportConsignmentsToExcelDestination())
                        }
                        "logout" -> {
                            scope.launch {
                                scaffoldState.drawerState.close()
                            }
                            CoroutineScope(Dispatchers.IO).launch {
                                try {
                                    FirebaseAuth.getInstance().signOut()
                                    delay(500L)
                                    withContext(Dispatchers.Main) {
                                        navigator.navigateUp()
                                    }
                                } catch (e: Exception) {
                                    scaffoldState.snackbarHostState.showSnackbar(
                                        message = e.message.toString()
                                    )
                                }
                            }
                        }
                    }
                }
            )
        }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {

            if (viewModel.showAlertDialog) {
                SimpleAlertDialog(
                    modifier = Modifier.align(Alignment.Center),
                    title = "Warning!! Delete this consignment",
                    description = "Are you sure?",
                    onDismissRequest = {
                        viewModel.showAlertDialog = false
                        deleteConsignment = null
                    },
                    confirmButton = {
                        viewModel.showAlertDialog = false
                        deleteConsignment?.let {
                            viewModel.onEvent(ConsignmentEvent.DeleteConsignment(it))
                        } ?: Toast.makeText(context, "please  try again!", Toast.LENGTH_LONG).show()
                        deleteConsignment = null
                    },
                    dismissButton = {
                        viewModel.showAlertDialog = false
                        deleteConsignment = null
                    }
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(CircleShape)
                        .background(MaterialTheme.colors.primary),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(
                        modifier = Modifier.weight(0.5f),
                        onClick = {
                            scope.launch {
                                scaffoldState.drawerState.open()
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Navigation drawer",
                            tint = Color.White
                        )
                    }
                    OutlinedTextField(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(MaterialTheme.colors.background)
                            .weight(1.5f)
                            .border(
                                color = MaterialTheme.colors.primary,
                                width = 2.dp,
                                shape = CircleShape
                            ),
                        value = state.searchQuery,
                        onValueChange = {
                            viewModel.onEvent(
                                ConsignmentEvent.SearchConsignment(it)
                            )
                        },
                        placeholder = {
                            Text(
                                text = "Search...",
                                modifier = Modifier,
                                color = MaterialTheme.colors.onBackground
                            )
                        },
                        maxLines = 1,
                        singleLine = true,
                        textStyle = TextStyle(
                            color = MaterialTheme.colors.onBackground
                        ),
                    )

                    IconButton(
                        modifier = Modifier.weight(0.5f),
                        onClick = {
                            viewModel.onEvent(ConsignmentEvent.ToggleOrderSection)
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Default.Sort,
                            contentDescription = "Sort",
                            tint = Color.White
                        )
                    }
                }
                AnimatedVisibility(
                    visible = state.isOrderSectionVisible,
                    enter = fadeIn() + slideInVertically(),
                    exit = fadeOut() + slideOutVertically()
                ) {
                    OrderSection(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        consignmentOrder = state.consignmentOrder,
                        onOrderChange = {
                            viewModel.onEvent(ConsignmentEvent.Order(it))
                        }
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                SwipeRefresh(
                    state = rememberSwipeRefreshState(isRefreshing = viewModel.refreshState),
                    onRefresh = {
                        viewModel.onEvent(ConsignmentEvent.Refresh)
                    }
                ) {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(state.consignments) { consignment ->
                            ConsignmentItem(
                                consignment = consignment,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        navigator.navigate(
                                            AddEditConsignmentScreenDestination(
                                                consignment = consignment
                                            )
                                        )
                                    },
                                onDeleteClick = {
                                    viewModel.showAlertDialog = true
                                    deleteConsignment = consignment
                                }
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }

                }
            }
            if (viewModel.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}

fun createMenuDrawer(drawerItems: MutableList<MenuItem>) {
    drawerItems.add(
        MenuItem(
            id = "excel",
            title = "Export data to Excel",
            icon = Icons.Default.ImportExport,
            contentDescription = "Go to export data screen",
        )
    )
    if (FirebaseAuth.getInstance().currentUser!!.uid == Admin_Uid) {
        drawerItems.add(
            MenuItem(
                id = "admin",
                title = "Admin",
                icon = Icons.Default.AdminPanelSettings,
                contentDescription = "Admin access this feature"
            )
        )
    }
    drawerItems.add(
        MenuItem(
            id = "logout",
            title = "Log out",
            icon = Icons.Default.Logout,
            contentDescription = "Log out from account",
        )
    )
}



