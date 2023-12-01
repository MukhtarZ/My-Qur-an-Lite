package com.mukhtarz.appquran.ui.read.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mukhtarz.appquran.data.kotpref.SetPref

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun FootNotesBottomSheet(
    footNotesContent: String,
    hideBottomSheet: () -> Unit,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Box(modifier = Modifier.fillMaxWidth()) {
            IconButton(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                ,
                onClick = {
                    hideBottomSheet()
                }
            ) {
                Icon(
                    modifier = Modifier.size(32.dp),
                    imageVector = Icons.Default.Close,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        if (SetPref.selectedLanguage == SetPref.INDONESIAN){
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = "Tafsir ayat :",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Medium
            )
        }else{
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = "Verse interpretation :",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            modifier = Modifier.padding(16.dp),
            text = footNotesContent,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}