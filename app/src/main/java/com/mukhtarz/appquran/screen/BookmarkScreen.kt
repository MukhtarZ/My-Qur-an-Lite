@file:OptIn(ExperimentalMaterial3Api::class)

package com.mukhtarz.appquran.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mukhtarz.appquran.R
import com.mukhtarz.appquran.data.GlobalState
import com.mukhtarz.appquran.data.kotpref.SetPref
import com.mukhtarz.appquran.database.BookmarkDatabase
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun BookmarkScreen(
    goToHome: ()-> Unit,
    modifier: Modifier = Modifier,
    goToRead: (surahNumber: Int?, juzNumber: Int?, page: Int?, index : Int?) -> Unit
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        val context = LocalContext.current
        val dao = BookmarkDatabase.getInstance(context).bookmarksDao()
        val bookmarkss = dao.getAllBookmarks()
        val scope = rememberCoroutineScope()
        val stateSnack = remember{
            SnackbarHostState()
        }
        var iconHapus by remember { mutableStateOf(false) }



        var isDialogOpen by remember { mutableStateOf(false) }

        bookmarkss.collectAsState(initial = emptyList()).let {state ->
        Column(modifier = Modifier.fillMaxSize()) {
            Scaffold(
                snackbarHost = {
                    SnackbarHost(hostState = stateSnack)
                },
                topBar = {
                    TopAppBar(
                        title = {
                            Text(modifier = Modifier.padding(start = 6.dp), text = "Bookmarks")
                        },
                        actions = {
                                IconButton(onClick = {
                                    isDialogOpen = state.value.isNotEmpty()
                                }) {
                                    Icon(imageVector = Icons.Default.Delete, contentDescription = null)

                                }


                            if (isDialogOpen){
                                AlertDialog(
                                    onDismissRequest = { isDialogOpen = false },
                                    title = {
                                        Text(text = "Hapus Bookmark")
                                    },
                                    text = {
                                        Text(text = "Anda yakin ingin menghapus semua bookmark ini ?")
                                    },
                                    confirmButton = {
                                        TextButton(onClick = {
                                            scope.launch{
                                                dao.deleteAllFromBookmark()
                                            }
                                            isDialogOpen = false
                                        }) {
                                            Text(text = "Ya")
                                        }
                                    },
                                    dismissButton = {
                                        TextButton(onClick = { isDialogOpen = false }) {
                                            Text(text = "Batal")
                                        }
                                    }
                                )
                            }
                        }
                    )
                }
            ) { innerPadding ->
                    if (state.value.isEmpty()){
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Center){
                            Text(text = if (GlobalState.Language == SetPref.INDONESIAN){
                                "Bookmark Kosong"
                            }else{
                                "Empty Bookmark"
                            })
                        }
                    }else{
                        LazyColumn(modifier = Modifier.padding(innerPadding)) {
                            itemsIndexed(state.value) { index,bookmarks ->
                                val clock = SimpleDateFormat("dd-MMMM-yyyy", Locale.getDefault())
                                    .format(Date(bookmarks.createdAt))
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(
                                            start = 16.dp,
                                            end = 16.dp,
                                            bottom = 16.dp,
                                            top = 26.dp
                                        ),
                                    border = BorderStroke(width = 1.dp, color = Color.Black),
                                    colors = CardDefaults.cardColors(containerColor =MaterialTheme.colorScheme.primaryContainer),
                                    onClick = {goToRead.invoke(bookmarks.surahNumber,null,null,bookmarks.ayahNumber)}
                                ) {
                                    Row() {
                                        Box(
                                            modifier = Modifier
                                                .align(CenterVertically)
                                                .size(70.dp)
                                        ) {
                                            Icon(
                                                modifier = Modifier
                                                    .padding(12.dp)
                                                    .align(Center)
                                                    .size(60.dp),
                                                painter = painterResource(id = R.drawable.baseline_circle_24),
                                                contentDescription = null,
                                                tint = MaterialTheme.colorScheme.primary
                                            )
                                            Text(
                                                modifier = Modifier.align(Center),
                                                text = "${index+1}",
                                                color = MaterialTheme.colorScheme.onPrimary,
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                        Column(modifier = Modifier.align(CenterVertically)) {
                                            Spacer(modifier = Modifier.height(6.dp))
                                            Text(text = "${bookmarks.surahName!!} : ${bookmarks.ayahNumber}")
                                            Spacer(modifier = Modifier.height(10.dp))
                                            Text(text = clock)
                                            Spacer(modifier = Modifier.height(6.dp))
                                        }
                                        Box(modifier = Modifier.weight(0.35f))
                                        IconButton(
                                            modifier = Modifier
                                                .align(CenterVertically)
                                                .size(40.dp),
                                            onClick = { scope.launch {
                                                dao.deleteBookmark(bookmark = bookmarks)
                                                val result = stateSnack
                                                    .showSnackbar(
                                                        message = "Remove bookmark",
                                                        actionLabel = "Undo",

                                                        // Defaults to SnackbarDuration.Short
                                                        duration = SnackbarDuration.Short
                                                    )
                                                when (result) {
                                                    SnackbarResult.ActionPerformed -> {
                                                        /* Handle snackbar action performed */
                                                        dao.insertBookmark(
                                                            bookmark = bookmarks
                                                        )
                                                    }

                                                    SnackbarResult.Dismissed -> {
                                                        /* Handle snackbar dismissed */
                                                        dao.deleteBookmark(
                                                            bookmark = bookmarks
                                                        )

                                                    }
                                                }
                                            }
                                            }
                                        ) {
                                            Icon(
                                                modifier = Modifier
                                                    .size(35.dp)
                                                    .padding(end = 6.dp),
                                                imageVector = Icons.Default.Delete,
                                                contentDescription = null,
                                                tint = Color.Red
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                }


            }
        }
    }
}




