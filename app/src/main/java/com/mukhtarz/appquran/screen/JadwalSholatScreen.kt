@file:OptIn(ExperimentalMaterial3Api::class)

package com.mukhtarz.appquran.screen

import android.Manifest
import android.location.Geocoder
import android.location.Location
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.LocationServices
import com.mukhtarz.appquran.JadwalSholat
import com.mukhtarz.appquran.R
import com.mukhtarz.appquran.database.location.LocationService
import com.mukhtarz.appquran.database.location.LocationServiceCondition
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun JadwalSholatScreen(
    modifier: Modifier = Modifier,
    goToHome: () -> Unit
) {

    val context = LocalContext.current
    val client = LocationServices.getFusedLocationProviderClient(context)
    val locationTracker = LocationService(
        client,
        context
    )
    val locationState =
        MutableStateFlow<LocationServiceCondition<Location?>?>(null)

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


    val listShalat = listOf(
        "Subuh",
        "Dzuhur",
        "Ashar",
        "Maghrib",
        "Isya"
    )
    val listWaktu = listOf(
        "04.38",
        "11.55",
        "16.13",
        "17.53",
        "19.03"
    )

    val listShalatDanWaktu = mutableListOf<JadwalSholat>()

    for (i in listShalat.indices) {
        val data = JadwalSholat(
            listShalat[i],
            listWaktu[i]
        )
        listShalatDanWaktu.add(data)
    }

    Scaffold(
        modifier = Modifier,
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { goToHome() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null
                        )
                    }

                },
                title = {
                    Text(modifier = Modifier.padding(start = 6.dp), text = "Jadwal Sholat")
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


        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LazyColumn(contentPadding = PaddingValues(12.dp)) {
                item {
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
                                    val provinsi = locationName?.adminArea
                                    val namaNegara = locationName?.countryName
                                    Text(
                                        text = "$provinsi| $namaNegara",
                                        fontSize = 18.sp,
                                        color = MaterialTheme.colorScheme.onBackground
                                    )
                                }
                            }
                            null -> {
//                                Box(modifier = Modifier.height(24.dp), contentAlignment = Center) {
//                                    Text(text = "Error", fontSize = 18.sp)
//                                }
                                Box(modifier = Modifier.height(24.dp), contentAlignment = Center){
                                    CircularProgressIndicator()
                                }
                                Spacer(modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                }
                items(listShalatDanWaktu) { item ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.Cyan)
                    ) {
                        Row {
                            Text(
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                                text = item.shalat
                            )
                            Box(modifier = Modifier.weight(0.20f))
                            Text(
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                                text = item.waktu
                            )
                            Icon(
                                modifier = Modifier
                                    .padding(horizontal = 16.dp)
                                    .align(CenterVertically),
                                painter = painterResource(id = R.drawable.baseline_access_time_24),
                                contentDescription = null
                            )
                        }
                    }
                    Spacer(modifier = Modifier.size(12.dp))
                }
            }
        }
    }
}




