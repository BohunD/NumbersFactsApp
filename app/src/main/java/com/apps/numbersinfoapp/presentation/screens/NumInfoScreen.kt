package com.apps.numbersinfoapp.presentation.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apps.numbersinfoapp.R
import com.apps.numbersinfoapp.presentation.viewmodel.NumbersContract
import com.apps.numbersinfoapp.util.mvi.collectInLaunchedEffect
import kotlinx.coroutines.flow.Flow


@Composable
fun NumInfoScreen(
    state: NumbersContract.State,
    event: (NumbersContract.Event) -> Unit,
    effect: Flow<NumbersContract.Effect>,
    onBackClicked: () -> Unit,
) {
    val context = LocalContext.current
    effect.collectInLaunchedEffect {
        when (it) {
            NumbersContract.Effect.ShowToast -> {
                Toast.makeText(
                    context,
                    context.getString(R.string.something_went_wrong),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
    BackHandler() {
        onBackClicked()
        event(NumbersContract.Event.ClearData)
        event(NumbersContract.Event.UpdateDataFromDB)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (state.isLoading) {
            CircularProgressIndicator()
        } else {
            state.currentNumberInfo.let {
                if (it.isNotEmpty()) {
                    Text(
                        text = it.substring(0, it.indexOf(' ')),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(text = it)
            }

            Button(modifier = Modifier.padding(top = 30.dp),
                onClick = {
                    onBackClicked()
                    event(NumbersContract.Event.ClearData)
                    event(NumbersContract.Event.UpdateDataFromDB)
                }) {
                Text(text = stringResource(R.string.to_home_screen))
            }
        }
    }
}