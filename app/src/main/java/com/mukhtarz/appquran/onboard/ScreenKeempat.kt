package com.mukhtarz.appquran.onboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.mukhtarz.appquran.R
import com.mukhtarz.appquran.data.GlobalState
import com.mukhtarz.appquran.data.kotpref.SetPref

@Composable
fun ScreenKeempat(
    goToScreenKetigas : () -> Unit,
    goToHome : () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface {
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.animation_compass))
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            LottieAnimation(modifier = Modifier.size(250.dp),composition =composition , iterations = LottieConstants.IterateForever)
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 56.dp),
                text = "Terdapat juga fitur yang memungkinkan anda untuk mencari arah kiblat",
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.weight(1f))
            Row (
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ){
                TextButton(
                    onClick = {
                        goToScreenKetigas()
                    }
                ) {
                    Text(
                        text = "Back",
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
                Row (
                    modifier = Modifier
                        .weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                ){
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_circle_24),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.size(16.dp)
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_circle_24),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.size(16.dp)
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_circle_24),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.size(16.dp)
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_circle_24),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )
                }
                TextButton(
                    onClick = {
                        GlobalState.onBoarding = false
                        SetPref.isOnBoarding = false
                        goToHome()
                    }
                ) {
                    Text(
                        text = "Open",
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}