package com.mukhtarz.appquran.database.location

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine


class LocationService(
    private val client: FusedLocationProviderClient,
    private val context: Context
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun getCurrentLocation (): LocationServiceCondition<Location?> {
        val isFineLocationPermitted = ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )== PackageManager.PERMISSION_GRANTED

        val isCoarseLocationPermitted = ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        )== PackageManager.PERMISSION_GRANTED

        val locatioManager = context.getSystemService(
            Context.LOCATION_SERVICE
        ) as LocationManager

        val isGpsEnabled =
            locatioManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)||
                    locatioManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

        if (!isGpsEnabled){
            return LocationServiceCondition.NoGps()
        }

        if (!(isFineLocationPermitted || isCoarseLocationPermitted)){
            return LocationServiceCondition.MissingPermission()
        }

        return suspendCancellableCoroutine {coroutine ->
            client.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                CancellationTokenSource().token
            ).apply {
                if (isComplete){
                    if (isSuccessful){
                        coroutine.resume(LocationServiceCondition.Success(result)){}
                    }else{
                        coroutine.resume(LocationServiceCondition.Error()){}
                    }
                    return@suspendCancellableCoroutine
                }
                addOnSuccessListener {
                    coroutine.resume(LocationServiceCondition.Success(result)){}
                }
                addOnCanceledListener {
                    coroutine.cancel()
                }
                addOnFailureListener {
                    coroutine.resume(LocationServiceCondition.Error()){}
                }
            }

        }
    }
}