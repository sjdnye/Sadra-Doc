package com.example.summerproject.presentation.articles.component

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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.summerproject.presentation.articles.ArticleEvent
import com.example.summerproject.presentation.articles.ArticlesViewModel
import com.example.summerproject.presentation.articles.component.navigationDrawer.DrawerBody
import com.example.summerproject.presentation.articles.component.navigationDrawer.MenuItem
import com.example.summerproject.presentation.destinations.*
import com.example.summerproject.ui.theme.LightBlue800
import com.example.utils.Admin_Uid
import com.google.firebase.auth.FirebaseAuth
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavController
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dagger.internal.InjectedFieldSignature
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject
import kotlin.math.roundToInt

@Destination(route = "/ArticleScreen")
@Composable
fun ArticlesScreen(
    navigator: DestinationsNavigator,
    viewModel: ArticlesViewModel = hiltViewModel()
) {

    var drawerItems = mutableListOf<MenuItem>(
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
                is ArticlesViewModel.ArticleScreenUi.DeleteArticleCompleted -> {
                    scope.launch {
                        val result = scaffoldState.snackbarHostState.showSnackbar(
                            message = "Article was deleted",
                            actionLabel = "Undo"
                        )
                        if (result == SnackbarResult.ActionPerformed) {
                            viewModel.onEvent(ArticleEvent.RestoreNote)
                        }
                    }
                }
                is ArticlesViewModel.ArticleScreenUi.ShowMessage -> {
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
                    navigator.navigate(AddEditArticleScreenDestination()) {
//                        popUpTo("/ArticleScreen"){ inclusive = true}
                    }
                },
                backgroundColor = MaterialTheme.colors.secondary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add article",
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
                            navigator.navigate(ExportArticleToExcelDestination())
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

                                }
                            }
                        }
                    }
                }
            )
        }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (viewModel.isLoading){
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
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
                        }) {
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
                                ArticleEvent.SearchArticle(it)
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
                            viewModel.onEvent(ArticleEvent.ToggleOrderSection)
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
                        articleOrder = state.articleOrder,
                        onOrderChange = {
                            viewModel.onEvent(ArticleEvent.Order(it))
                        }
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(state.articles) { article ->
                        ArticleItem(
                            article = article,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    navigator.navigate(AddEditArticleScreenDestination(article = article))
                                },
                            onDeleteClick = {
                                viewModel.onEvent(ArticleEvent.DeleteNote(article))
                            }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}