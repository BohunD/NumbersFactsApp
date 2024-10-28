package com.apps.numbersinfoapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.apps.numbersinfoapp.presentation.screens.HomeScreen
import com.apps.numbersinfoapp.presentation.screens.NumInfoScreen
import com.apps.numbersinfoapp.presentation.viewmodel.NumbersViewModel
import com.apps.numbersinfoapp.ui.theme.NumbersInfoAppTheme
import com.apps.numbersinfoapp.util.mvi.use
import dagger.hilt.android.AndroidEntryPoint

sealed class Destination(val route: String) {
    data object FirstScreen : Destination("first_screen")
    data object SecondScreen : Destination("second_screen{numId}")
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel = hiltViewModel<NumbersViewModel>()
            val (state, event, effect) = use(viewModel)
            NumbersInfoAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Destination.FirstScreen.route
                    ) {
                        composable(Destination.FirstScreen.route) {
                            HomeScreen(state, event) {
                                navController.navigate(
                                    Destination.SecondScreen.route
                                )
                            }
                        }
                        composable(Destination.SecondScreen.route) {
                            NumInfoScreen(
                                state,
                                event,
                                effect
                            ) { navController.popBackStack() }
                        }

                    }
                }
            }
        }
    }
}