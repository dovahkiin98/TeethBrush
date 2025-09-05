package net.inferno.teethbrush.ui.main

import androidx.compose.runtime.Composable
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import net.inferno.teethbrush.ui.setting.SettingUI
import net.inferno.teethbrush.ui.timer.TimerUI

@Composable
fun MainUI(

) {
    val navController = rememberSwipeDismissableNavController()

    SwipeDismissableNavHost(
        navController,
        startDestination = "timer",
    ) {
        composable("timer") {
            TimerUI(
                onNavigateToSettings = {
                    navController.navigate("settings")
                }
            )
        }

        composable("settings") {
            SettingUI(
                onSubmit = {
                    navController.popBackStack()
                },
            )
        }
    }
}