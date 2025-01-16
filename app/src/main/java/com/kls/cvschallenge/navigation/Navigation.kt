package com.kls.cvschallenge.navigation

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.toRoute
import com.kls.cvschallenge.data.FlickrImage
import com.kls.cvschallenge.ui.details.DetailsScreen
import com.kls.cvschallenge.ui.search.SearchScreen
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.reflect.typeOf


sealed class NavigationItem {
    @Serializable
    data object SearchScreen : NavigationItem()
}

@Composable
fun Navigation(navController: NavHostController) {
    NavHost(navController, startDestination = NavigationItem.SearchScreen) {
        composable<NavigationItem.SearchScreen> {
            SearchScreen(navController)
        }
        composable(
            route = "detailsScreen/{imageArg}"
        ) { backStackEntry ->
            val encodedImage = backStackEntry.arguments?.getString("imageArg")
            encodedImage?.let {
                Log.d("Navigation", "IMAGE")
                val decodedImage = Json.decodeFromString<FlickrImage>(it)
                DetailsScreen(image = decodedImage)
            }
        }
    }
}