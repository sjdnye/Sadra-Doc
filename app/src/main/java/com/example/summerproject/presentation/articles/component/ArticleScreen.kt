package com.example.summerproject.presentation.articles.component

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.summerproject.presentation.articles.ArticleEvent
import com.example.summerproject.presentation.articles.ArticlesViewModel
import com.example.summerproject.presentation.articles.component.navigationDrawer.DrawerBody
import com.example.summerproject.presentation.articles.component.navigationDrawer.MenuItem
import com.example.summerproject.presentation.destinations.AddEditArticleScreenDestination
import com.example.summerproject.presentation.destinations.ExportArticleToExcelDestination
import com.example.summerproject.ui.theme.LightBlue800
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch

@Destination(start = true)
@Composable
fun ArticlesScreen(
    navigator: DestinationsNavigator,
    viewModel: ArticlesViewModel = hiltViewModel()
) {
    val state = viewModel.state
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navigator.navigate(AddEditArticleScreenDestination())
                },
                backgroundColor = MaterialTheme.colors.secondary
            ) {
                Icon(imageVector = Icons.Default.Add,
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
        drawerGesturesEnabled = scaffoldState.drawerState.isOpen,
        drawerContent = {
            DrawerBody(
                items = listOf(
                    MenuItem(
                        id = "excel",
                        title = "Import data to Excel",
                        icon = Icons.Default.ImportExport,
                        contentDescription = "Go to import data screen",
                    ), MenuItem(
                        id = "settings",
                        title = "Settings",
                        icon = Icons.Default.Settings,
                        contentDescription = "Go to settings screen",
                    ), MenuItem(
                        id = "help",
                        title = "Help",
                        icon = Icons.Default.Info,
                        contentDescription = "Get help",
                    )
                ),
                onItemClick = {
                    when (it.id) {
                        "excel" -> {
                            navigator.navigate(ExportArticleToExcelDestination())
                        }
                    }
                    println("Clicked on ${it.title}")
                }
            )

        }
    ) {
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
                        .background(Color.White)
                        ,
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
                    )
                )

                IconButton(
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
                            scope.launch {
                                val result = scaffoldState.snackbarHostState.showSnackbar(
                                    message = "Article deleted",
                                    actionLabel = "Undo"
                                )
                                if (result == SnackbarResult.ActionPerformed) {
                                    viewModel.onEvent(ArticleEvent.RestoreNote)
                                }
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}