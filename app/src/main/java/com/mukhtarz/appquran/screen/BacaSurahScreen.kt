package com.mukhtarz.appquran.screen

import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.RepeatOne
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mukhtarz.appquran.R
import com.mukhtarz.appquran.TajweedHelper
import com.mukhtarz.appquran.data.GlobalState
import com.mukhtarz.appquran.data.kotpref.LastReadPref
import com.mukhtarz.appquran.data.kotpref.SetPref
import com.mukhtarz.appquran.database.AyahSearch
import com.mukhtarz.appquran.database.Bookmark
import com.mukhtarz.appquran.database.BookmarkDatabase
import com.mukhtarz.appquran.database.QoranDatabase
import com.mukhtarz.appquran.service.player.MyPlayerService
import com.mukhtarz.appquran.ui.read.component.FootNotesBottomSheet
import com.mukhtarz.appquran.ui.read.component.SpannableText
import com.mukhtarz.appquran.data.toAnnotatedString
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import snow.player.PlayMode
import snow.player.PlayerClient
import snow.player.audio.MusicItem
import snow.player.playlist.Playlist

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BacaSurahScreen(
    goBack: () -> Unit,
    surahNumber: Int?,
    juzNumber: Int?,
    page: Int?,
    index : Int?
) {
    val context = LocalContext.current
    val dao = QoranDatabase.getInstance(context).dao()
    val scope = rememberCoroutineScope()
    val bookmarkDao = BookmarkDatabase.getInstance(context).bookmarksDao()

    var isOpenBottomBarAll by remember { mutableStateOf(false) }


    val searchResult = remember { mutableStateListOf<AyahSearch>() }
    var search by remember { mutableStateOf("") }
    var isOpenBottomBar by remember { mutableStateOf(false) }
    val modalBottomState = rememberModalBottomSheetState()
    var isOpenSearch by remember { mutableStateOf(false) }
    val footNoteState = remember {
        mutableStateOf("")
    }
    val playerClient = remember {
        PlayerClient.newInstance(context, MyPlayerService::class.java)
    }
    var isBottomSheetShow by remember {
        mutableStateOf(false)
    }
    val clipboardManager = LocalClipboardManager.current

    val lazyColumnState = rememberLazyListState()


    val readSurah = when {
        juzNumber != -1 -> {
            dao.getSurahByJozz(juzNumber!!)

        }

        surahNumber != -1 -> {
            dao.getSurahBySora(surahNumber!!)
        }

        page != -1 -> {
            dao.getSurahByPage(page!!)
        }

        else -> throw Exception("Error")
    }

    var currentPlayedAyah by remember {
        mutableStateOf(0)
    }
    var currentPlayedAyats by remember {
        mutableStateOf(0)
    }
    var currentPlayedSurah by remember {
        mutableStateOf("")
    }

    var titleSurah by remember {
        mutableStateOf("")
    }
    var surahNameEn by remember {
        mutableStateOf("")
    }
    var surahNameIn by remember {
        mutableStateOf("")
    }
    var surahPlace by remember {
        mutableStateOf("")
    }
    var totalAyat by remember {
        mutableStateOf(0)
    }

    var titleSurahArab by remember {
        mutableStateOf("")
    }

    var isPlayerPlaying by remember {
        mutableStateOf(false)
    }

    var playerClientState by remember {
        mutableStateOf(true)
    }


    val qariPlaylist = mutableListOf<MusicItem>()

    LaunchedEffect(key1 = true) {
        readSurah.collectLatest {
            val nameSurah = it[0].soraEn
            titleSurah = "$nameSurah"
        }
    }

    LaunchedEffect(key1 = true) {
        readSurah.collectLatest {
            val judulSurah = it[0].soraEn
            surahNameEn = "$judulSurah"
        }
    }

    LaunchedEffect(key1 = true) {
        readSurah.collectLatest {
            val surahIn = it[0].soraNameId
            surahNameIn = "$surahIn"
        }
    }
    LaunchedEffect(key1 = true) {
        readSurah.collectLatest {
            val surahDescandPlace = it[0].soraDescendPlace
            surahPlace = "$surahDescandPlace"
        }
    }

    LaunchedEffect(key1 = true) {
        readSurah.collectLatest {
            val totalAyah = it[0].ayaNo
            totalAyat = totalAyah!!
        }
    }

    LaunchedEffect(key1 = true) {
        readSurah.collectLatest {
            val titleSurahArabs = it[0].soraAr
            titleSurahArab = titleSurahArabs!!
        }
    }

    LaunchedEffect(Unit){
        delay(1400)
        if (index == -1) {
            return@LaunchedEffect
        }
        lazyColumnState.scrollToItem(
            index!!
        )

    }





    Box {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(modifier = Modifier.padding(start = 6.dp), text = titleSurah)
                    },
                    navigationIcon = {
                        IconButton(onClick = { goBack() }) {
                            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                        }


                    },
                    actions = {
                        IconButton(onClick = { isOpenSearch = true }) {
                            Icon(
                                modifier = Modifier.padding(end = 16.dp),
                                imageVector = Icons.Default.Search,
                                contentDescription = null
                            )
                        }

                    }
                )
            },
            bottomBar = {
                if (isOpenBottomBar) {
                    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 7.dp)) {
                        Text(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 3.5.dp),
                            text = "${titleSurah}: $currentPlayedAyah",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 3.5.dp),
                            text = "${GlobalState.SelectedQari}"
                        )
                        Row(modifier = Modifier.fillMaxWidth()) {
                            IconButton(onClick = {
                                playerClient.playPause(); isPlayerPlaying = playerClient.isPlaying
                            }) {
                                Icon(
                                    imageVector = if (!isPlayerPlaying) {
                                        Icons.Default.Pause
                                    } else {
                                        Icons.Default.PlayArrow
                                    }, contentDescription = null
                                )
                            }
                            Spacer(modifier = Modifier.width(3.dp))
                            IconButton(onClick = {
                                playerClient.stop();isPlayerPlaying =
                                playerClient.isPlaying; isOpenBottomBar = false

                            }) {
                                Icon(imageVector = Icons.Default.Stop, contentDescription = null)
                            }
                            Spacer(modifier = Modifier.size(3.dp))
                            IconButton(
                                onClick = {
                                    when (playerClient.playMode) {
                                        PlayMode.PLAYLIST_LOOP -> {
                                            playerClientState = true
                                            playerClient.playMode = PlayMode.SINGLE_ONCE
                                        }
                                        else -> {
                                            playerClientState = false
                                            playerClient.playMode = PlayMode.PLAYLIST_LOOP
                                        }
                                    }
                                }) {
                                Icon(
                                    imageVector = if (!playerClientState) Icons.Default.Repeat else Icons.Default.RepeatOne,
                                    contentDescription = null
                                )
                            }
                        }
                    }
                }else if (isOpenBottomBarAll){

                    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 7.dp)) {
                        Text(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 3.5.dp),
                            text = "$titleSurah: $currentPlayedAyats",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 3.5.dp),
                            text = "${GlobalState.SelectedQari}"
                        )
                        Row(modifier = Modifier.fillMaxWidth()) {
                            IconButton(onClick = {
                                playerClient.playPause(); isPlayerPlaying = playerClient.isPlaying
                            }) {
                                Icon(
                                    imageVector = if (!isPlayerPlaying) {
                                        Icons.Default.Pause
                                    } else {
                                        Icons.Default.PlayArrow
                                    }, contentDescription = null
                                )
                            }
                            Spacer(modifier = Modifier.width(3.dp))
                            IconButton(onClick = {
                                playerClient.stop();isPlayerPlaying =
                                playerClient.isPlaying; isOpenBottomBar = false;isOpenBottomBarAll = false

                            }) {
                                Icon(imageVector = Icons.Default.Stop, contentDescription = null)
                            }
                            Spacer(modifier = Modifier.size(3.dp))
                            IconButton(
                                onClick = {
                                    when (playerClient.playMode) {
                                        PlayMode.PLAYLIST_LOOP -> {
                                            playerClientState = true
                                            playerClient.playMode = PlayMode.SINGLE_ONCE
                                        }
                                        else -> {
                                            playerClientState = false
                                            playerClient.playMode = PlayMode.PLAYLIST_LOOP
                                        }
                                    }
                                }) {
                                Icon(
                                    imageVector = if (!playerClientState) Icons.Default.Repeat else Icons.Default.RepeatOne,
                                    contentDescription = null
                                )
                            }
                        }

                    }
            }

            }
        ) {
            val innerPadding = it
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                readSurah.collectAsState(initial = emptyList()).let { state ->
                    LazyColumn(state = lazyColumnState) {
                        itemsIndexed(state.value) { index,surah ->
                            LastReadPref.apply {
                                lastAyaNo = surah.ayaNo!!
                                lastSurahName = surah.soraNameEmlaey!!
                                lastSurahNumber = surah.surahNumber!!
                                lastReadTime = System.currentTimeMillis()
                                this.index = index
                            }
                            if (surah.ayaNo == 1) {
                                val surahPlaces = if (surah.soraDescendPlace == "Meccan") {
                                    "Mekkah"
                                } else {
                                    "Madinah"
                                }
                                val headerText =
                                    if (surah.surahNumber == 1 || surah.surahNumber == 9) {
                                        "أَعُوذُ بِاللَّهِ مِنَ الشَّيْطَانِ الرَّجِيمِ"
                                    } else {
                                        "بِسْمِ اللهِ الرَّحْمَنِ الرَّحِيْمِ"
                                    }
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                                ) {
                                    Column(horizontalAlignment = CenterHorizontally) {
                                        Text(
                                            modifier = Modifier
                                                .padding(16.dp)
                                                .fillMaxWidth(),
                                            text = "${surah.soraEn} | ${surah.soraAr}",
                                            textAlign = TextAlign.Center,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer
                                        )
                                        Text(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(bottom = 10.dp, top = 6.dp),
                                            text = surah.soraNameId!!,
                                            textAlign = TextAlign.Center,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer
                                        )
                                        Divider(
                                            modifier = Modifier
                                                .width(180.dp)
                                                .padding(bottom = 6.dp), color = Color.Black
                                        )
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.Center
                                        ) {
                                            Text(
                                                modifier = Modifier.padding(
                                                    end = 35.dp,
                                                    bottom = 39.dp
                                                ),
                                                text = surahPlaces,
                                                color = MaterialTheme.colorScheme.onPrimaryContainer
                                            )
                                            Text(
                                                modifier = Modifier.padding(start = 16.dp),
                                                text = "$totalAyat ayat",
                                                color = MaterialTheme.colorScheme.onPrimaryContainer
                                            )

                                        }
                                        Text(
                                            modifier = Modifier.padding(bottom = 10.dp),
                                            text = headerText,
                                            fontSize = 20.sp,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(16.dp))
                                    IconButton(modifier = Modifier.align(CenterHorizontally),
                                        onClick = {
                                            playerClient.stop()
                                            state.value.forEach { surah ->
                                                val formatSurahNumber =
                                                    convertNumberToThreeDigits(surah.surahNumber!!)
                                                val formatAyahNumber =
                                                    convertNumberToThreeDigits(surah.ayaNo!!)
                                                val musicItem = createMusicItem(
                                                    title = "${surah.soraEn} : ${surah.ayaNo}",
                                                    surahNumber = formatSurahNumber,
                                                    ayahNumber = formatAyahNumber
                                                )
                                                qariPlaylist.add(musicItem)
                                            }

                                            playerClient.connect {
                                                val qoriPlaylist =
                                                    createSurahPlaylist(playQari = qariPlaylist)
                                                playerClient.setPlaylist(qoriPlaylist!!, true)
                                                playerClient.playMode = PlayMode.PLAYLIST_LOOP
                                                isOpenBottomBarAll = true
                                                isOpenBottomBar = false
                                                currentPlayedAyats = surah.ayaNo
                                                playerClient.addOnPlayingMusicItemChangeListener { _, position, _ ->
                                                    val surahName = surah.soraEn
                                                }
                                            }
                                        }) {
                                        Icon(
                                            imageVector = Icons.Default.PlayCircle,
                                            contentDescription = null
                                        )
                                    }
                                }
                            }
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                Row(modifier = Modifier) {
                                    Box(
                                        modifier = Modifier
                                            .align(CenterVertically)
                                            .size(60.dp)
                                            .padding(end = 12.dp)
                                    ) {
                                        Icon(
                                            modifier = Modifier
                                                .align(Alignment.Center)
                                                .size(46.dp),
                                            painter = painterResource(id = R.drawable.baseline_circle_24),
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.secondary
                                        )
                                        Text(
                                            modifier = Modifier
                                                .align(Alignment.Center),
                                            text = "${surah.ayaNo}",
                                            color = MaterialTheme.colorScheme.onSecondary,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                    Column() {
                                        if (GlobalState.FocusMode) {   //jika FocusMode bernilai true
                                            Text(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(
                                                        top = 12.dp,
                                                        end = 20.dp,
                                                        bottom = 7.dp
                                                    ),
                                                text = TajweedHelper.getTajweed(
                                                    context = context,
                                                    s = Regex("\\d+\$").replace(
                                                        surah.ayaText!!,
                                                        ""
                                                    ),
                                                )
                                                    .toAnnotatedString(MaterialTheme.colorScheme.primary),
                                                fontSize = 23.sp,
                                                textAlign = TextAlign.End
                                            )
                                        } else {                         //jika FocusMode bernilai false
                                            Text(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(
                                                        top = 12.dp,
                                                        end = 20.dp,
                                                        bottom = 7.dp
                                                    ),
                                                text = TajweedHelper.getTajweed(
                                                    context = context,
                                                    s = Regex("\\d+\$").replace(
                                                        surah.ayaText!!,
                                                        ""
                                                    ),
                                                )
                                                    .toAnnotatedString(MaterialTheme.colorScheme.primary),
                                                textAlign = TextAlign.End
                                            )
                                        }


                                        if (!GlobalState.FocusMode) {       //jika Focusmode bernilai false
                                            SpannableText(
                                                text = if (SetPref.selectedLanguage == SetPref.INDONESIAN) {
                                                    "${surah.translationId}"
                                                } else {
                                                    "${surah.translationEn}"
                                                },
                                                onClick = { footnotenumber ->
                                                    if (SetPref.selectedLanguage == SetPref.INDONESIAN) {
                                                        footNoteState.value =
                                                            surah.footnotesId ?: ""
                                                    } else {
                                                        footNoteState.value =
                                                            surah.footnotesEn ?: ""
                                                    }
                                                    scope.launch {
                                                        modalBottomState.show()
                                                        isBottomSheetShow = true
                                                        isOpenBottomBar = false
                                                    }
                                                }
                                            )
                                        }


                                        Divider(modifier = Modifier.size(3.dp), thickness = 0.dp)
                                        Row(modifier = Modifier.fillMaxWidth()) {
                                            Divider(
                                                modifier = Modifier.weight(0.25f),
                                                thickness = 0.dp
                                            )
                                            IconButton(onClick = {
                                                val textShare =
                                                    "${surah.ayaText}\n${surah.translationId}"
                                                val sendIntent: Intent = Intent().apply {
                                                    action = Intent.ACTION_SEND
                                                    putExtra(Intent.EXTRA_TEXT, textShare)
                                                    type = "text/plain"
                                                }
                                                val shareIntent =
                                                    Intent.createChooser(sendIntent, null)

                                                context.startActivity(shareIntent)
                                            }) {
                                                Icon(
                                                    imageVector = Icons.Default.Share,
                                                    contentDescription = null
                                                )
                                            }
                                            Divider(modifier = Modifier.width(5.dp))
                                            IconButton(onClick = {
                                                Toast.makeText(
                                                    context,
                                                    "Sudah di bookmark",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                scope.launch {
                                                    bookmarkDao.insertBookmark(
                                                        Bookmark(
                                                            surahName = surah.soraEn,
                                                            surahNumber = surah.surahNumber,
                                                            ayahNumber = surah.ayaNo,

                                                            )

                                                    )
                                                }
                                            }) {
                                                Icon(
                                                    painter = painterResource(id = R.drawable.baseline_bookmarks_24),
                                                    contentDescription = null
                                                )


                                            }
                                            Divider(modifier = Modifier.width(5.dp))
                                            IconButton(
                                                onClick = {
                                                    Toast.makeText(
                                                        context,
                                                        "Copy ayat dan arti",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    clipboardManager.setText(
                                                        AnnotatedString("${surah.ayaText}\n${surah.translationId}")
                                                    )
                                                },
                                            ) {
                                                Icon(
                                                    painter = painterResource(id = R.drawable.baseline_content_copy_24),
                                                    contentDescription = null
                                                )
                                            }
                                            Divider(modifier = Modifier.width(5.dp))
                                            IconButton(
                                                onClick = {
                                                    val formatSurah =
                                                        convertNumberToThreeDigits(surah.surahNumber!!)
                                                    val formatAyah =
                                                        convertNumberToThreeDigits(surah.ayaNo!!)
                                                    val musicItem = createMusicItem(
                                                        title = "${surah.soraEn}: ${surah.ayaNo}",
                                                        surahNumber = formatSurah,
                                                        ayahNumber = formatAyah
                                                    )
                                                    val playlist =
                                                        Playlist.Builder().append(musicItem).build()
                                                    playerClient.connect { bolean ->
                                                        Toast.makeText(
                                                            context,
                                                            "$bolean",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                        playerClient.setPlaylist(playlist, true)
                                                        playerClient.playMode = PlayMode.SINGLE_ONCE
                                                        currentPlayedAyah = surah.ayaNo
                                                        isOpenBottomBar = true
                                                        isOpenBottomBarAll = false
                                                    }
                                                }
                                            ) {
                                                Icon(
                                                    painter = painterResource(id = R.drawable.baseline_play_arrow_24),
                                                    contentDescription = null
                                                )
                                            }


                                        }
                                    }
                                }
                            }
                        }

                    }
                }
                if (isBottomSheetShow) {
                    ModalBottomSheet(
                        sheetState = modalBottomState, onDismissRequest = {
                            isBottomSheetShow = false

                        }) {
                        FootNotesBottomSheet(
                            footNotesContent = footNoteState.value,
                            hideBottomSheet = {
                                scope.launch {
                                    modalBottomState.show()
                                    isBottomSheetShow = true
                                }
                            }
                        )
                    }
                }
            }
        }
        if (isOpenSearch) {
            SearchBar(
                query = search,
                onQueryChange = {
                    search = it
                },
                onSearch = {
                    val daos = dao.getAyahBySearch(it)
                    searchResult.clear()
                    scope.launch {
                        daos.collect {
                            searchResult.addAll(it)
                        }
                    }
                },
                active = isOpenSearch,
                onActiveChange = {
                    isOpenSearch = it
                },
                placeholder = {
                    Text(
                        text = if (SetPref.selectedLanguage == SetPref.INDONESIAN) {
                            "Cari Ayat"
                        } else {
                            "Search Ayah"
                        }
                    )
                },
                leadingIcon = {
                    IconButton(onClick = { isOpenSearch = false }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                    }
                },

                ) {
                LazyColumn() {
                    items(searchResult) { ayat ->
                        var expandShareAndCopy by remember { mutableStateOf(false) }

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp, end = 16.dp, top = 10.dp, bottom = 5.dp)
                        ) {

                            Card(modifier = Modifier.fillMaxWidth()) {
                                Row (){
                                    AnimatedVisibility(visible = !expandShareAndCopy) {
                                        ExpandSurah(surat =ayat.soraNameEmlaey!! , ayat =ayat.ayaNo!! )
                                    }
                                    AnimatedVisibility(visible = expandShareAndCopy) {
                                        ExpandIconCopyAndShare(ayatText = ayat.ayaText, translationId = ayat.translationId)
                                    }
                                    Box(modifier = Modifier.weight(0.5f))
                                    IconButton(
                                        onClick = {
                                            scope.launch {
                                                if (expandShareAndCopy){
                                                    scope.launch {
                                                        expandShareAndCopy = false
                                                    }
                                                }else{
                                                    scope.launch {
                                                        expandShareAndCopy = true
                                                    }
                                                }
                                            }
                                        },
                                        modifier = Modifier.align(CenterVertically)
                                        ) {
                                        Icon(imageVector = if (expandShareAndCopy) Icons.Default.KeyboardArrowRight else Icons.Default.KeyboardArrowLeft, contentDescription = null)
                                    }
                                }

                            }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(text ="${ayat.ayaText}")
                            Spacer(modifier = Modifier.height(4.6.dp))
                            Text(
                                text = if (SetPref.selectedLanguage == SetPref.INDONESIAN){
                                    "${ayat.translationId}"
                                } else {
                                    "${ayat.translationEn}"
                                }
                            )
                            Divider(
                                modifier = Modifier
                                    .height(15.dp)
                                    .padding(vertical = 7.dp),
                                thickness = 1.7.dp
                            )
                        }
                    }


                }
            }

        }
    }
}






private fun createMusicItem(
    title:String, ayahNumber:String, surahNumber: String
): MusicItem {
    return MusicItem.Builder()
        .setMusicId(ayahNumber)
        .setMusicId("$ayahNumber$surahNumber")
        .autoDuration()
        .setTitle(title)
        .setIconUri(SetPref.changeQaries.image)
        .setUri("https://everyayah.com/data/${SetPref.changeQaries.id}/$surahNumber$ayahNumber.mp3")
        .setArtist(SetPref.changeQaries.qariName)
        .build()
}

private fun createSurahPlaylist(playQari : List<MusicItem>): Playlist? {
    return Playlist.Builder()
        .appendAll(playQari).build()
}

private fun convertNumberToThreeDigits(
    number: Int
): String {
    return String.format("%03d", number)
}

//@Composable
//fun SearchAyah (context : Context) {
//    var search by remember { mutableStateOf("") }
//    val searchResult = remember { mutableStateListOf<AyahSearch>() }
//    val dao = QoranDatabase.getInstance(context).dao()
//    val scope = rememberCoroutineScope()
//    Column (modifier = Modifier.fillMaxSize()){
//        OutlinedTextField(
//            value = search,
//            onValueChange = {
//                search = it
//                val daos = dao.getAyahBySearch(search)
//                searchResult.clear()
//                scope.launch{
//                    daos.collect{
//                        searchResult.addAll(it)
//                    }
//                }
//
//            }
////            ,
////            keyboardOptions = KeyboardOptions.Default.copy(
////                imeAction = ImeAction.Search
////            )
//        )
//    }
//
//}


@Composable
fun ExpandSurah(surat: String, ayat: Int) {
        Row (modifier = Modifier.padding(horizontal = 16.dp, vertical = 13.dp), verticalAlignment = Alignment.CenterVertically){
            Text(text = "surat ${surat} ayat ${ayat}")

        }
}

@Composable
fun ExpandIconCopyAndShare(ayatText: String?, translationId: String?) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    Row (modifier = Modifier.padding(horizontal = 10.dp)){
        IconButton(onClick = {
            val textShare =
                "${ayatText}\n${translationId}"
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, textShare)
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, null)

            context.startActivity(shareIntent)
        }) {
            Icon(
                imageVector = Icons.Default.Share,
                contentDescription = null
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        IconButton(
            onClick = {
                Toast.makeText(
                    context,
                    "Copy ayat dan arti",
                    Toast.LENGTH_SHORT
                ).show()
                clipboardManager.setText(
                    AnnotatedString("${ayatText}\n${translationId}")
                )
            },
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_content_copy_24),
                contentDescription = null
            )
        }
    }
}

