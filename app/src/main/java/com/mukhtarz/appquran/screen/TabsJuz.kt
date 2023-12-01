package com.mukhtarz.appquran.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.mukhtarz.appquran.data.kotpref.LastReadPref
import com.mukhtarz.appquran.data.kotpref.SetPref
import com.mukhtarz.appquran.database.QoranDatabase
import com.mukhtarz.appquran.database.SurahSearch
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun TabsJuz (
modifier: Modifier = Modifier,
goToRead : (surahNumber: Int?,juzNumber: Int?, page : Int?,index : Int?) -> Unit
) {
    val context = LocalContext.current
    val tablist = listOf("Juz", "Page", "Surah")
    val scope = rememberCoroutineScope()
    val pageState = rememberPagerState(
        initialPage = 0,
        pageCount = {
            tablist.size
        }
    )
    var active by remember { mutableStateOf(false) }
    var search by remember { mutableStateOf("") }
    val searchResult = remember { mutableStateListOf<SurahSearch>() }
    val dao = QoranDatabase.getInstance(context = context).dao()

    var openSearchSurah by remember { mutableStateOf(false) }

        Scaffold(
            modifier = Modifier,
            topBar = {
                if (!openSearchSurah){
                    TopAppBar(
                        title = {
                            Text(modifier = Modifier.padding(start = 6.dp), text = "Baca Surah")
                        },
                        actions = {
                            IconButton(
                                onClick = {
                                    openSearchSurah = true
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = null
                                )
                            }
                        }
                    )
                }else{
                    Row (modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)){
                        DockedSearchBar(
                            query = search,
                            onQueryChange = {
                                search = it
                            },
                            onSearch = {
                                search = it
                                val daos = dao.getSurahBySearch(soraNameEmlaey = search)
                                searchResult.clear()
                                scope.launch {
                                    daos.collect {
                                        searchResult.addAll(it)
                                    }
                                }
                            },
                            active = active,
                            onActiveChange = {
                                active = it
                            },
                            modifier = Modifier
                                .padding(vertical = 7.dp),
                            trailingIcon = {
                                Icon(imageVector = Icons.Default.Search, contentDescription = null)
                            },
                            leadingIcon = {
                                IconButton(onClick = { openSearchSurah = false  }) {
                                    Icon(imageVector = Icons.Default.Close, contentDescription = null)
                                }

                            },
                            placeholder = {
                                Text(
                                    text = if (SetPref.selectedLanguage == SetPref.INDONESIAN) {
                                        "Cari Surah"
                                    } else {
                                        "Search Surah"
                                    }
                                )

                            }
                        ) {
                            LazyColumn() {
                                items(searchResult) {
                                    Card(modifier = Modifier
                                        .fillMaxWidth()
                                        .align(CenterHorizontally)
                                        .padding(
                                            top = 7.dp,
                                            end = 16.dp,
                                            start = 16.dp,
                                            bottom = 10.dp
                                        ),
                                        onClick = {
                                            goToRead.invoke(it.surahNumber!!, null, null,LastReadPref.index)
                                        }) {
                                        Row {
                                            Text(
                                                modifier = Modifier
                                                    .padding(horizontal = 16.dp)
                                                    .align(CenterVertically), text = "${it.surahNumber}"
                                            )
                                            Column {
                                                Text(
                                                    modifier = Modifier.padding(
                                                        vertical = 5.dp,
                                                        horizontal = 16.dp
                                                    ), text = it.soraNameEmlaey!!
                                                )
                                                Text(
                                                    modifier = Modifier.padding(
                                                        vertical = 5.dp,
                                                        horizontal = 16.dp
                                                    ), text = "total ayat ${it.ayahTotal}"
                                                )
                                            }

                                        }


                                    }
                                }
                            }
                        }
                    }
                }


            }) { innerPadding ->
            Surface(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    //                SearchBar(
                    //                    modifier = Modifier.align(CenterHorizontally),
                    //                    query = search,
                    //                    onQueryChange = {
                    //                       search = it
                    //                    } ,
                    //                    onSearch = {
                    //                       active = false
                    //                    } ,
                    //                    active = active,
                    //                    onActiveChange = {
                    //                       active = it
                    //                    } ,
                    //                    trailingIcon = {
                    //                        Icon(imageVector = Icons.Default.Search, contentDescription = null)
                    //                    },
                    //                    placeholder = {
                    //                        Text(text =  if (SetPref.selectedLanguage == SetPref.INDONESIAN) {
                    //                            "Cari Surah"
                    //                        } else {
                    //                            "Search Surah"
                    //                        })
                    //
                    //                    }
                    //                ) {
                    //
                    //                }


                    // A surface container using the 'background' color from the theme
                    TabRow(
                        selectedTabIndex = pageState.currentPage,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        tablist.forEachIndexed { index: Int, text: String ->
                            Tab(
                                modifier = Modifier.height(56.dp),
                                selected = index == pageState.currentPage,
                                onClick = {
                                    scope.launch {
                                        pageState.animateScrollToPage(index)
                                    }
                                }) {
                                Text(text = text)
                            }
                        }
                    }
                    HorizontalPager(
                        modifier = Modifier.fillMaxSize(),
                        state = pageState,

                        ) { page ->
                        when (page) {
                            0 -> JuzScreen(
                                modifier = Modifier.fillMaxSize(), goToRead = goToRead
                            )

                            1 -> PageScreen(
                                modifier = Modifier.fillMaxSize(), goToRead = goToRead
                            )

                            2 -> SurahScreen(
                                modifier = Modifier.fillMaxSize(), goToRead = goToRead
                            )

                        }

                    }

                }
            }

        }

    }





