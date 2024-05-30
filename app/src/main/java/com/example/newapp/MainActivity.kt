package com.example.newapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.newapp.db.DeviceDatabaseHelper
import com.example.newapp.db.RoomDatabaseHelper
import com.example.newapp.screens.AddDeviceScreen
import com.example.newapp.screens.AddRoomScreen
import com.example.newapp.screens.DevicesScreen
import com.example.newapp.screens.EditDeviceScreen
import com.example.newapp.screens.EditRoomScreen
import com.example.newapp.screens.HomeScreen
import com.example.newapp.screens.RoomDevicesScreen
import com.example.newapp.screens.RoomsScreen
import com.example.newapp.ui.theme.NewAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NewAppTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val roomDatabaseHelper = remember { RoomDatabaseHelper(context) }
    val deviceDatabaseHelper = remember { DeviceDatabaseHelper(context) }

    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomeScreen(navController, roomDatabaseHelper) }
        composable("my_rooms") { RoomsScreen(navController, roomDatabaseHelper) }
        composable("add_room") { AddRoomScreen(navController, roomDatabaseHelper) }
        composable(
            "edit_room/{roomId}",
            arguments = listOf(navArgument("roomId") { type = NavType.LongType })
        ) { backStackEntry ->
            val roomId = backStackEntry.arguments?.getLong("roomId") ?: -1L
            EditRoomScreen(navController, roomId, roomDatabaseHelper)
        }
        composable("add_device") {
            AddDeviceScreen(navController, deviceDatabaseHelper, roomDatabaseHelper)
        }
        composable("my_devices") { DevicesScreen(navController, deviceDatabaseHelper) }
        composable(
            "edit_device/{deviceId}",
            arguments = listOf(navArgument("deviceId") { type = NavType.LongType })
        ) { backStackEntry ->
            val deviceId = backStackEntry.arguments?.getLong("deviceId") ?: -1L
            EditDeviceScreen(navController, deviceDatabaseHelper, roomDatabaseHelper, deviceId)
        }
        composable(
            "room_devices/{roomId}",
            arguments = listOf(navArgument("roomId") { type = NavType.LongType })
        ) { backStackEntry ->
            val roomId = backStackEntry.arguments?.getLong("roomId") ?: -1L
            RoomDevicesScreen(navController, roomId, roomDatabaseHelper, deviceDatabaseHelper)
        }
    }
}






