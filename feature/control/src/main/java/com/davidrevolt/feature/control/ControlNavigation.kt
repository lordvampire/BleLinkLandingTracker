package com.davidrevolt.feature.control

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

const val DEVICE_NAME = "deviceName"
const val DEVICE_ADDRESS = "deviceAddress"
const val CONTROL_ROUTE = "control_route/deviceName={deviceName}/deviceAddress={deviceAddress}"
const val DATA_SCREEN_ROUTE = "data_screen/{deviceAddress}/{characteristicUuid}"


fun NavController.navigateToControl(
    deviceName: String,
    deviceAddress: String,
    navOptions: NavOptions? = null
) {
    this.navigate(
        "control_route/deviceName=${deviceName}/deviceAddress=${deviceAddress}",
        navOptions
    )
}

fun NavController.navigateToDataScreen(
    deviceAddress: String,
    characteristicUuid: String,
    navOptions: NavOptions? = null
) {
    this.navigate(
        "data_screen/${deviceAddress}/${characteristicUuid}", navOptions
    )
}

@RequiresApi(Build.VERSION_CODES.S)
fun NavGraphBuilder.controlScreen(onBackClick: () -> Unit) {
    composable(route = CONTROL_ROUTE, arguments = listOf(
        navArgument(DEVICE_NAME) {
            type = NavType.StringType
            defaultValue = ""
            //nullable = true  // if no args -> set query to null [not needed because defaultValue is set]
        },
        navArgument(DEVICE_ADDRESS) {
            type = NavType.StringType
            defaultValue = ""
            //nullable = true  // if no args -> set query to null [not needed because defaultValue is set]
        }
    )) {
        ControlScreen(onBackClick = onBackClick)
    }

    composable(
        route = DATA_SCREEN_ROUTE, arguments = listOf(
            navArgument("deviceAddress") { type = NavType.StringType },
            navArgument("characteristicUuid") { type = NavType.StringType }
        )
    ) { backStackEntry ->
        val deviceAddress = backStackEntry.arguments?.getString("deviceAddress") ?: ""
        val characteristicUuid = backStackEntry.arguments?.getString("characteristicUuid") ?: ""
    }
}