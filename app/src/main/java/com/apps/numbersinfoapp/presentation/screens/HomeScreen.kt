package com.apps.numbersinfoapp.presentation.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.apps.numbersinfoapp.R
import com.apps.numbersinfoapp.domain.model.NumberModel
import com.apps.numbersinfoapp.presentation.viewmodel.NumbersContract
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    state: NumbersContract.State,
    event: (NumbersContract.Event) -> Unit,
    onNextScreen: ()->Unit

) {

    LaunchedEffect(Unit) {
        event(NumbersContract.Event.UpdateDataFromDB)
    }
    val scope = rememberCoroutineScope()
    Column(modifier = Modifier.fillMaxSize()) {

        NumberInputSection(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            state = state,
            event =  event,
            ){
            onNextScreen()
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f), verticalArrangement = Arrangement.Center
        ) {
            val list = remember {
                mutableStateOf(listOf<NumberModel?>(null))
            }
            SideEffect {
                scope.launch {
                    state.numbersFromDB.collect {
                        list.value = it.reversed()
                    }
                }
            }
            LazyVerticalGrid(columns = GridCells.Fixed(2), modifier = Modifier.padding(4.dp)) {
                items(list.value) {
                    it?.info?.let { it1 ->
                        ListItem(info = it1, onClick = { info ->
                            event(NumbersContract.Event.SetCurrentNumberInfo(info))
                            onNextScreen()
                        })
                    }
                }
            }
        }
    }
}

@Composable
private fun NumberInputSection(
    modifier: Modifier,
    state: NumbersContract.State,
    event: (NumbersContract.Event) -> Unit,
    onNextScreen: () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        OutlinedTextField(
            value = state.currentNumber,
            onValueChange = {
                event(NumbersContract.Event.SetCurrentNumber(it))
            },
            isError = state.isNumberCorrect?.let { !it } ?: false,
            label = { Text(text = stringResource(R.string.enter_number)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Button(onClick = {
            event(NumbersContract.Event.ValidateNumber)
            if (state.isNumberCorrect == true) {
                event(NumbersContract.Event.GetNumberInfo)
                onNextScreen()
            }
        }) {
            Text(text = stringResource(R.string.get_fact))
        }
        Text(text = stringResource(R.string.or))
        Button(onClick = {
            event(NumbersContract.Event.GetRandomNumberInfo)
            onNextScreen()
        }) {
            Text(text = stringResource(R.string.get_fact_about_random_num), textAlign = TextAlign.Center)
        }

    }
}

@Composable
fun ListItem(info: String, onClick: (String) -> Unit) {

    Column(
        modifier = Modifier
            .height(100.dp)
            .fillMaxWidth(0.9f)
            .padding(2.dp)
            .clip(RoundedCornerShape(8.dp))
            .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
            .padding(2.dp)
            .clickable {
                onClick(info)
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val number = info.substring(0,info.indexOf(' '))
        val numberInfo = info.substring(info.indexOf(' '), info.length)
        Text(text = number, fontWeight = FontWeight.Bold)
        Text(text = numberInfo, maxLines = 1,
            overflow = TextOverflow.Ellipsis )
    }
}