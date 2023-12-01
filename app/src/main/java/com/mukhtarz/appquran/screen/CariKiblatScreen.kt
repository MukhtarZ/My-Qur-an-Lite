package com.mukhtarz.appquran.screen

import android.app.Activity
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.just.agentweb.AgentWeb

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CariKiblatScreen(
    goToHome:()-> Unit
) {
    val activity = LocalContext.current as Activity
    val url = "https://qiblafinder.withgoogle.com/intl/id/"


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Cari Kiblat")
                },
                navigationIcon = {
                    IconButton(onClick = { goToHome() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                    }
                }
            ) 
        }
    ) {val innerPadding = it
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            AndroidView(
                factory = { context ->
                    LinearLayout(context).apply {
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                        AgentWeb.with(activity)
                            .setAgentWebParent(this,this.layoutParams)
                            .useDefaultIndicator()
                            .createAgentWeb()
                            .ready()
                            .go(url)
                    }

                }
            )
        }
    }

}