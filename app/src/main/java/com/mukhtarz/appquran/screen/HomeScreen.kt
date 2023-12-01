@file:OptIn(
    ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class
)

package com.mukhtarz.appquran.screen

import android.Manifest
import android.location.Geocoder
import android.location.Location
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CompassCalibration
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Mosque
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionsRequired
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.LocationServices
import com.mukhtarz.appquran.R
import com.mukhtarz.appquran.data.GlobalState
import com.mukhtarz.appquran.data.kotpref.LastReadPref
import com.mukhtarz.appquran.data.kotpref.SetPref
import com.mukhtarz.appquran.data.remote.APIInterface
import com.mukhtarz.appquran.data.remote.model.list.Time
import com.mukhtarz.appquran.database.location.LocationService
import com.mukhtarz.appquran.database.location.LocationServiceCondition
import com.mukhtarz.appquran.ui.Kosong
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    goToKiblat: () -> Unit,
//    modifier: Modifier = Modifier
    goToRead: () -> Unit
) {

    val api = APIInterface.createApi()
    val jadwalSholatList = remember {
        mutableStateListOf<Time?>()
    }
    val context = LocalContext.current
    val client = LocationServices.getFusedLocationProviderClient(context)
    val locationTracker = LocationService(
        client,
        context
    )
    val locationState = remember {
        MutableStateFlow<LocationServiceCondition<Location?>?>(null)
    }
    val modalBottomSheet = rememberModalBottomSheetState()
    var isModalBottomOpen by remember { mutableStateOf(false) }

//    LaunchedEffect(key1 = true ){
//        if (LastReadPref.lastAyaNo != -1){
//            delay(2000)
//            isModalBottomOpen = true
//        }
////        delay(10000)
////        isModalBottomOpen = false
//
//    }


    val geoCoder = Geocoder(context)

    val locationPermissions = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    )

    if (!locationPermissions.allPermissionsGranted) {
        LaunchedEffect(key1 = true) {
            locationPermissions.launchMultiplePermissionRequest()
        }
    }

    LaunchedEffect(locationPermissions.allPermissionsGranted) {
        locationState.emit(locationTracker.getCurrentLocation())
    }
    val navController: NavHostController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        modifier = Modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(modifier = Modifier.padding(start = 6.dp), text = "My Qur'an Lite")
                },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null
                        )
                    }
                }
            )

        }) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            PermissionsRequired(
                multiplePermissionsState = locationPermissions,
                permissionsNotGrantedContent = {
                    Kosong(textMassage = "ngelag")
                },
                permissionsNotAvailableContent = {
                    Kosong(textMassage = "entahlah")
                }) {
                locationState.asStateFlow().collectAsState().let { state ->
                    when (val locationCondition = state.value) {
                        is LocationServiceCondition.Error -> {
                            Text(
                                text = "-",
                                fontSize = 18.sp,
                                color = MaterialTheme.colorScheme.onError
                            )
                        }

                        is LocationServiceCondition.MissingPermission -> {
                            Text(
                                text = "-",
                                fontSize = 18.sp,
                                color = MaterialTheme.colorScheme.onError
                            )
                        }

                        is LocationServiceCondition.NoGps -> {
                            Text(
                                text = "nyalakan GPS !!",
                                fontSize = 18.sp,
                                color = MaterialTheme.colorScheme.onSecondary
                            )
                        }

                        is LocationServiceCondition.Success -> {
                            val location = locationCondition.location
                            val latitude = location?.latitude
                            val longtitude = location?.longitude
                            if (latitude != null && longtitude != null) {
                                val locationName = geoCoder.getFromLocation(
                                    latitude, longtitude, 1
                                )?.get(0)

                                val provinsi = locationName?.subLocality
                                val negara = locationName?.countryName


                                if (jadwalSholatList.isNotEmpty()){
                                    val dateFlow = getTime().collectAsState(initial = "")
                                    Column(modifier = Modifier.fillMaxWidth()) {
                                        Text(modifier = Modifier.padding(horizontal = 16.dp, vertical = 7.dp), text = "Jadwal Sholat di $provinsi")
                                        Card(modifier = Modifier.padding(horizontal = 64.dp, vertical = 15.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
                                            Text(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(vertical = 7.dp),
                                                text = dateFlow.value,
                                                fontSize = 40.sp,
                                                fontWeight = FontWeight.Bold,
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                        Row(modifier = Modifier.fillMaxWidth()) {
                                            Card(
                                                modifier = Modifier
                                                    .padding(horizontal = 16.dp, vertical = 10.dp)
                                                    .weight(0.30f),
                                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                                            ) {
                                                Text(
                                                    modifier = Modifier
                                                        .padding(
                                                            horizontal = 16.dp,
                                                            vertical = 8.dp
                                                        )
                                                        .align(CenterHorizontally), text = "Shubuh"
                                                )
                                                Spacer(modifier = Modifier.height(2.dp))
                                                Text(modifier = Modifier
                                                    .align(CenterHorizontally)
                                                    .padding(bottom = 8.dp), text = "${jadwalSholatList[0]?.subuh}")
                                            }
                                            Card(
                                                modifier = Modifier
                                                    .padding(horizontal = 16.dp, vertical = 10.dp)
                                                    .weight(0.30f),
                                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                                            ) {
                                                Text(
                                                    modifier = Modifier
                                                        .padding(
                                                            horizontal = 16.dp,
                                                            vertical = 8.dp
                                                        )
                                                        .align(CenterHorizontally), text = "Dzuhur"
                                                )
                                                Spacer(modifier = Modifier.height(2.dp))
                                                Text(modifier = Modifier
                                                    .align(CenterHorizontally)
                                                    .padding(bottom = 8.dp), text = "${jadwalSholatList[0]?.dhuhur}")
                                            }
                                        }
                                        Row(modifier = Modifier.fillMaxWidth()) {
                                            Card(
                                                modifier = Modifier
                                                    .padding(horizontal = 16.dp, vertical = 10.dp)
                                                    .weight(0.30f),
                                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                                            ) {
                                                Text(
                                                    modifier = Modifier
                                                        .padding(
                                                            horizontal = 16.dp,
                                                            vertical = 8.dp
                                                        )
                                                        .align(CenterHorizontally), text = "Ashar"
                                                )
                                                Spacer(modifier = Modifier.height(2.dp))
                                                Text(modifier = Modifier
                                                    .align(CenterHorizontally)
                                                    .padding(bottom = 8.dp), text = "${jadwalSholatList[0]?.ashar}")
                                            }
                                            Card(
                                                modifier = Modifier
                                                    .padding(horizontal = 16.dp, vertical = 10.dp)
                                                    .weight(0.30f),
                                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                                            ) {
                                                Text(
                                                    modifier = Modifier
                                                        .padding(
                                                            horizontal = 16.dp,
                                                            vertical = 8.dp
                                                        )
                                                        .align(CenterHorizontally), text = "Maghrib"
                                                )
                                                Spacer(modifier = Modifier.height(2.dp))
                                                Text(modifier = Modifier
                                                    .align(CenterHorizontally)
                                                    .padding(bottom = 8.dp), text = "${jadwalSholatList[0]?.maghrib}")
                                            }

                                        }
                                        Row {
                                            Card(
                                                modifier = Modifier
                                                    .padding(horizontal = 16.dp, vertical = 10.dp)
                                                    .weight(0.30f),
                                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                                            ) {
                                                Text(
                                                    modifier = Modifier
                                                        .padding(
                                                            horizontal = 16.dp,
                                                            vertical = 8.dp
                                                        )
                                                        .align(CenterHorizontally), text = "Isya"
                                                )
                                                Spacer(modifier = Modifier.height(2.dp))
                                                Text(
                                                    modifier = Modifier
                                                        .align(CenterHorizontally)
                                                        .padding(bottom = 8.dp),
                                                    text = "${jadwalSholatList[0]?.isya}"
                                                )
                                            }
                                        }
                                        Spacer(modifier = Modifier.height(12.dp))
                                        Divider(modifier = Modifier.height(1.dp), color = MaterialTheme.colorScheme.outline)
                                        Spacer(modifier = Modifier.height(12.dp))
                                    }

                                }
                            }
                            LaunchedEffect(true) {
                                val result = api.getJadwal(
                                    latitude = latitude.toString(), longitude = longtitude.toString()
                                )
                                jadwalSholatList.clear()
                                jadwalSholatList.addAll(result.times!!)
                            }
                        }

                        else -> {
                            Box(
                                modifier = Modifier
                                    .height(42.dp)
                                    .padding(8.dp),
                                contentAlignment = Center
                            ) {
                                Row(modifier = Modifier.fillMaxWidth()) {
                                    Spacer(modifier = Modifier.width(16.dp))
                                    Text(text = "Mencari Lokasi", modifier = Modifier.align(CenterVertically))
                                    Spacer(modifier = Modifier.width(7.dp))
                                    CircularProgressIndicator()
                                }


                            }
                        }
                    }
                }
            }

//            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                
                Card (modifier = Modifier
                    .weight(0.30f)
                    .padding(16.dp)
                    .align(CenterVertically)
                    .fillMaxWidth()
                    .height(145.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    onClick = {
                        goToKiblat()
                    }){
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Center
                    ) {
                        Column {
                            Icon(
                                modifier = Modifier
                                    .size(45.dp)
                                    .align(CenterHorizontally)
                                    .padding(top = 16.dp),
                                imageVector = Icons.Default.CompassCalibration,
                                contentDescription = null
                            )
                            Text(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .align(CenterHorizontally), text = "Cari Kiblat"
                            )
                        }
                    }



                }

                Card (modifier = Modifier
                    .weight(0.30f)
                    .padding(16.dp)
                    .align(CenterVertically)
                    .fillMaxWidth()
                    .height(145.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    onClick = {
                        goToRead.invoke()
                    }){
                    val waktu = SimpleDateFormat("dd-MMMM-yyy", Locale.getDefault()).format(Date(LastReadPref.lastReadTime))
                    Text(modifier = Modifier.padding(16.dp), text = if (GlobalState.Language == SetPref.INDONESIAN){
                        "Terakhir Dibaca"
                    }else{
                        "Last Read"
                    })
                    Box(modifier = Modifier.padding(start = 16.dp, end = 10.dp, bottom = 10.dp)) {
                        Column {
                            Text(text = "Surat ${LastReadPref.lastSurahName}:\nAyat ${LastReadPref.lastAyaNo}")
                            Spacer(modifier = Modifier.height(7.dp))
                            Text(text = waktu)
                        }
                    }

                }
            }
//        Row(modifier = Modifier
//            .fillMaxWidth()
//            .padding(horizontal = 16.dp)
//        ) {
//            Card(
//                modifier = Modifier
//                    .size(60.dp),
//                shape = RoundedCornerShape(50.dp),
//                colors = CardDefaults.cardColors(Color.Gray)
//            ) {
//                Box(modifier = Modifier.fillMaxSize(),
//                    contentAlignment = Center
//                ) {
//                    Image(
//                        modifier = Modifier.size(30.dp),
//                        painter = painterResource(id = R.drawable.baseline_mosque_24),
//                        contentDescription = null
//                    )
//                }
//            }
//        }


        }

//        if (isModalBottomOpen){
//            ModalBottomSheet(
//                sheetState = modalBottomSheet,
//                onDismissRequest = {
//                    isModalBottomOpen = false
//                    LastReadPref.index = 0
//                    LastReadPref.lastSurahNumber = 0
//                    LastReadPref.lastAyaNo = 0
//                    LastReadPref.lastReadTime = 0L
//                    LastReadPref.lastSurahName = ""
//                }
//            ) {
//                val waktu = SimpleDateFormat("dd-MMMM-yyy", Locale.getDefault()).format(Date(LastReadPref.lastReadTime))
//                Text(modifier = Modifier
//                    .align(CenterHorizontally)
//                    .padding(
//                        top = 10.dp, bottom = 7.dp
//                    ), text = if (SetPref.selectedLanguage == SetPref.INDONESIAN) "Surat Terakhir Dibaca" else "Last Read Surah", fontSize = 20.sp)
//
//                Box (modifier = Modifier
//                    .fillMaxWidth()
//                    .clickable {
//                        goToRead.invoke()
//                    }){
//                    Row(
//                        modifier = Modifier.padding(
//                            bottom = 23.dp,
//                            top = 16.dp,
//                            start = 16.dp,
//                            end = 19.dp
//                        )
//                    ) {
//                        Box(
//                            modifier = Modifier
//                                .align(CenterVertically)
//                                .size(53.dp)
//                                .padding(end = 12.dp)
//
//                        ) {
//                            Icon(
//                                modifier = Modifier
//                                    .align(Center)
//                                    .size(43.dp),
//                                painter = painterResource(id = R.drawable.baseline_circle_24),
//                                contentDescription = null,
//                                tint = Color.Gray
//                            )
//                            Text(
//                                modifier = Modifier
//                                    .align(Center),
//                                text = "${LastReadPref.lastSurahNumber}",
//                                color = Color.White,
//                                textAlign = TextAlign.Center
//                            )
//                        }
//                        Spacer(modifier = Modifier.width(10.dp))
//                        Column {
//                            Text(text = "Surat ${LastReadPref.lastSurahName}: Ayat  ${LastReadPref.lastAyaNo}")
//                            Spacer(modifier = Modifier.height(7.dp))
//                            Text(text = waktu)
//                        }
//                    }
//                }
//            }
//        }



    }
    

}


private fun getTime() = flow { 
    while (true){
        val date = SimpleDateFormat(
            "HH:mm:ss",
            Locale.getDefault()
        ).format(System.currentTimeMillis())
        emit(date)
        delay(1000)
    }
}
