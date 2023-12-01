package com.mukhtarz.appquran.screen

import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mukhtarz.appquran.R
import com.mukhtarz.appquran.database.QoranDatabase

@Composable
fun SurahScreen (
    modifier: Modifier = Modifier,
    goToRead: (surahNumber: Int?, juzNumber: Int?, page: Int?, index: Int?) -> Unit
) {
    val context = LocalContext.current
    val dao = QoranDatabase.getInstance(context).dao()
    val list = dao.listSurahBySurah()




    Surface(modifier = Modifier.fillMaxSize()) {

        list.collectAsState(initial = emptyList()).let {state ->
            Column(modifier = Modifier.fillMaxSize()) {
                LazyColumn(){
                    items(state.value){index ->
                       val surahPlace = if (index.soraDescendPlace == "Meccan"){
                            "Mekkah"
                        }else{
                            "Madinah"
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp)
                                .clickable {
                                    goToRead.invoke(index.surahNumber,null,null,null)
                                }
                        ) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.CenterVertically)
                                    .size(60.dp)
                                    .padding(end = 12.dp)


                            ) {
                                Icon(
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                        .size(46.dp),
                                    painter = painterResource(id = R.drawable.baseline_circle_24),
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primaryContainer
                                )
                                Text(
                                    modifier = Modifier.align(Alignment.Center),
                                    text = "${index.surahNumber}",
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    textAlign = TextAlign.Center
                                )
                            }
                            Column {
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(text = "${index.soraEn}", color = MaterialTheme.colorScheme.secondary)
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(text = "$surahPlace | ${index.ayahTotal}", color = MaterialTheme.colorScheme.secondary)
                                Spacer(modifier = Modifier.height(6.dp))
                            }
                            Box(modifier = Modifier.weight(0.35f))
                            Text(modifier = Modifier.align(Alignment.CenterVertically), text = "${index.soraAr}", fontSize = 20.sp)
                        }
                        Divider()
                    }
                }
            }
        }

    }
}

