package com.carfax.assignment.presentation.ui.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.carfax.assignment.presentation.details.VehicleDetailRoute
import com.carfax.assignment.presentation.listings.ListingsRoute

@Composable
fun AppNavGraph() {
    val nav = rememberNavController()
    NavHost(navController = nav, startDestination = "list") {
        composable("list") { ListingsRoute(onOpen = { id -> nav.navigate("detail/$id") }) }
        composable(
            "detail/{id}",
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) {
            VehicleDetailRoute(
                id = it.arguments!!.getString("id")!!,
                onBack = { nav.popBackStack() })
        }
    }
}