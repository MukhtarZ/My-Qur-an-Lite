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

@Composable
fun ScreenAwals(
    goToScreenKeempats: () -> Unit,
    goToScreenKeduas: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface {
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.animation_man_read_quran))
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
                text = "Selamat datang di My Qur'an Lite, Aplikasi Qur'an digital indonesia yang dilengkapi fitur untuk memudahkan anda dalam membaca & memahami Al-Quran.",
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
                        goToScreenKeempats()
                    }
                ) {
                    Text(
                        text = "Skip",
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
                        tint = MaterialTheme.colorScheme.primary,
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
                        tint = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.size(16.dp)
                    )
                }
                TextButton(
                    onClick = {
                        goToScreenKeduas()
                    }
                ) {
                    Text(
                        text = "Next",
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

