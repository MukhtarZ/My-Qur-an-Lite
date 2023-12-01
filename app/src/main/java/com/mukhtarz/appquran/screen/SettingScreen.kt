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
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.FilterCenterFocus
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mukhtarz.appquran.data.GlobalState
import com.mukhtarz.appquran.data.kotpref.Qaries
import com.mukhtarz.appquran.data.kotpref.SetPref
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(
    goHome: () -> Unit
) {
    var openDialog by remember {
        mutableStateOf(false)
    }
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var isSheetOpen by rememberSaveable {
        mutableStateOf(false)
    }
    var isSheetQariOpen by rememberSaveable {
        mutableStateOf(false)
    }
    

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Setting")
                }

            )
        }
    ) {
        val innerPadding = it
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                border = BorderStroke(
                    0.6.dp,
                    MaterialTheme.colorScheme.onBackground
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 7.dp)
                ) {
                    Icon(
                        modifier = Modifier.size(40.dp),
                        imageVector = Icons.Default.DarkMode,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(modifier = Modifier.align(CenterVertically), text = "Ganti ke dark mode")
                    Box(modifier = Modifier.weight(0.10f))
                    Switch(checked = GlobalState.DarkMode, onCheckedChange = { switchs ->
                        GlobalState.DarkMode = switchs
                        SetPref.selectedTheme = switchs
                    })
                }
            }

            Box(modifier = Modifier.height(5.dp))


            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                border = BorderStroke(
                    0.6.dp,
                    MaterialTheme.colorScheme.onBackground
                ),
                onClick = {
                    scope.launch {
                        sheetState.expand()
                        isSheetOpen = true
                    }

                }

            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 7.dp)
                ) {
                    Icon(
                        modifier = Modifier
                            .size(40.dp)
                            .align(CenterVertically),
                        imageVector = Icons.Default.Language,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        val bahasanya = if (GlobalState.Language == 0){
                            "Bahasa Indonesia"
                        }else{
                           "Bahasa Inggris"
                        }
                        Text(text = "Ganti bahasa")
                        Box(modifier = Modifier.height(7.dp))
                        Text(text = "Saat ini sedang menggunakan\n$bahasanya")
                    }

                }
            }
            Box(modifier = Modifier.size(6.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                border = BorderStroke(
                    0.6.dp,
                    MaterialTheme.colorScheme.onBackground
                ),
                onClick = {
                    scope.launch {
                        sheetState.expand()
                        isSheetQariOpen = true
                    }
                }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 7.dp),
                    verticalAlignment = CenterVertically
                ) {
                    Icon(
                        modifier = Modifier.size(40.dp),
                        imageVector = Icons.Default.Mic,
                        contentDescription = null
                    )
                    Box(modifier = Modifier.width(16.dp))
                    Column {
                        Text(text = "Ganti Qari")
                        Box(modifier = Modifier.height(7.dp))
                        Text(text = "Saat ini sedang menggunakan\nqari ${GlobalState.SelectedQari.qariName}")
                    }

                }


            }
            Box(modifier = Modifier.size(6.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                border = BorderStroke(
                    0.6.dp,
                    MaterialTheme.colorScheme.onBackground
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 7.dp)
                ) {
                    Icon(
                        modifier = Modifier.size(40.dp),
                        imageVector = Icons.Default.FilterCenterFocus,
                        contentDescription = null
                    )
                    Box(modifier = Modifier.width(16.dp))
                    Text(modifier = Modifier.align(CenterVertically),text = "Mode Fokus Baca")
                    Box(modifier = Modifier.weight(0.10f))
                    Switch(checked = GlobalState.FocusMode, onCheckedChange = {focus ->
                        GlobalState.FocusMode = focus
                        SetPref.isFocusRead = focus

                    })
                }
            }

                

            if (isSheetQariOpen) {
                ModalBottomSheet(
                    sheetState = sheetState,
                    onDismissRequest = {
                        scope.launch {
                            sheetState.hide()
                            isSheetQariOpen = false
                        }
                    }
                ) {
                    Text(modifier = Modifier.padding(horizontal = 16.dp), text = "Ganti Qari", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Medium)
                    Box(modifier = Modifier.height(19.dp))
                    Qaries.values().forEach { qaries ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = CenterVertically,
                        ) {
                            RadioButton(
                                selected = qaries == GlobalState.SelectedQari,
                                onClick = { 
                                    SetPref.changeQaries = qaries
                                    GlobalState.SelectedQari = qaries
                                    scope.launch {
                                        sheetState.hide()
                                        isSheetQariOpen = false
                                    }
                                }
                            )
                            Box(modifier = Modifier.width(10.dp))
                            Text(text = qaries.qariName)
                        }
                        Box(modifier = Modifier.height(3.dp))
                    }
//                    Spacer(modifier = Modifier.height(16.dp))
//                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = BottomEnd){
//                        Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.End) {
//                            Button(onClick = {
//                                scope.launch {
//                                    sheetState.hide()
//                                    isSheetQariOpen = false
//                                }
//                                GlobalState.SelectedQari = SetPref.changeQaries
//                            }) {
//                                Text(text = "Selesai")
//                            }
//                        }
//                    }
                    
                    
                    
                }
            }

            if (isSheetOpen) {
                ModalBottomSheet(
                    sheetState = sheetState,
                    onDismissRequest = {
                        scope.launch {
                            sheetState.hide()
                            isSheetOpen = false
                        }
                    }
                ) {
                    var radioButton by remember {
                        mutableIntStateOf(SetPref.selectedLanguage)
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 3.dp),
                        verticalAlignment = CenterVertically
                    ) {
                        RadioButton(selected = radioButton == SetPref.INDONESIAN, onClick = {
                            radioButton = SetPref.INDONESIAN
                            SetPref.selectedLanguage = SetPref.INDONESIAN
                            GlobalState.Language = SetPref.selectedLanguage
                            scope.launch {
                                sheetState.hide()
                                isSheetOpen = false
                            }
                        })
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(text = "Bahasa Indonesia")
                    }
                    Box(modifier = Modifier.height(7.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 3.dp),
                        verticalAlignment = CenterVertically
                    ) {
                        RadioButton(selected = radioButton == SetPref.ENGLISH, onClick = {
                            radioButton = SetPref.ENGLISH
                            SetPref.selectedLanguage = SetPref.ENGLISH
                            GlobalState.Language = SetPref.selectedLanguage
                            scope.launch {
                                sheetState.hide()
                                isSheetOpen = false
                            }
                        })
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(text = "Bahasa Inggris")
                    }
//                    Box(modifier = Modifier.height(17.dp))
//                    Text(
//                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
//                        text = if (radioButton == 0) {
//                            "Kamu memilih Bahasa"
//                        } else {
//                            "Kamu memilih Language"
//                        }
//                    )
                }
            }

        }
//        if (openDialog){
//            AlertDialog(onDismissRequest = { /*TODO*/ }) {
//
//                var radioButton by remember {
//                    mutableIntStateOf(0)
//                }
//
//                Column(modifier = Modifier) {
//                    Row(modifier = Modifier.fillMaxWidth()) {
//                        RadioButton(selected = radioButton == 0 , onClick = { radioButton = 0 })
//                        Spacer(modifier = Modifier.width(16.dp))
//                        Text(text = "Bahasa")
//                    }
//                    Box(modifier = Modifier.height(7.dp))
//                    Row(modifier = Modifier.fillMaxWidth()) {
//                        RadioButton(selected = radioButton == 1, onClick = { radioButton = 1 })
//                        Spacer(modifier = Modifier.width(16.dp))
//                        Text(text = "Language")
//                    }
//                    Box(modifier = Modifier.height(17.dp))
//                    Text(text = if (radioButton == 0){
//                        "Kamu memilih Bahasa"
//                    } else {
//                        "Kamu memilih Language"
//                    }
//                    )
//                }
//            }
//        }else{
//            Text(text = "Kosong")
//        }

    }
}


@Preview
@Composable
fun Tes() {
    SettingScreen {

    }
}




    
