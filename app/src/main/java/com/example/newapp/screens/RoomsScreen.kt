package com.example.newapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.newapp.db.RoomDatabaseHelper
import com.example.newapp.ui.theme.NewAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoomsScreen(navController: NavController, roomDatabaseHelper: RoomDatabaseHelper) {
    val rooms = roomDatabaseHelper.getAllRooms()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Мои комнаты",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF6200EE)
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Назад", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate("add_room") }) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "Добавить", tint = Color.White)
                    }
                },
                modifier = Modifier.background(Color(0xFF6200EE))
            )
        },
        content = { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                itemsIndexed(rooms) { index, room ->
                    Text(
                        text = room.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .clickable { navController.navigate("room_devices/${room.id}") }
                    )
                }
            }
        }
    )
}
@Preview(showBackground = true)
@Composable
fun RoomsScreenPreview() {
    NewAppTheme {
        RoomsScreen(rememberNavController(), RoomDatabaseHelper(LocalContext.current))
    }
}