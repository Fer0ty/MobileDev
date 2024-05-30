package com.example.newapp.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.newapp.db.DeviceDatabaseHelper
import com.example.newapp.db.RoomDatabaseHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoomDevicesScreen(
    navController: NavController,
    roomId: Long,
    roomDatabaseHelper: RoomDatabaseHelper,
    deviceDatabaseHelper: DeviceDatabaseHelper
) {
    val room = roomDatabaseHelper.getRoomById(roomId)
    val devices = room?.let { deviceDatabaseHelper.getDevicesByRoom(it.name) } ?: emptyList()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = room?.name ?: "Комната",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Назад",
                            tint = Color.White
                        )
                    }
                }, colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF6200EE)
                ),
                actions = {
                    room?.let {
                        IconButton(onClick = { navController.navigate("edit_room/${room.id}") }) {
                            Icon(
                                imageVector = Icons.Default.Create,
                                contentDescription = "Изменить",
                                tint = Color.White
                            )
                        }
                    }
                },
                modifier = Modifier.background(Color(0xFF6200EE))
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                Text(
                    text = "Устройства в комнате",
                    modifier = Modifier.padding(16.dp),
                )
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                ) {
                    itemsIndexed(devices) { index, device ->
                        Text(
                            text = device.name,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .clickable { navController.navigate("edit_device/${device.id}") }
                        )
                    }
                }
            }
        }
    )
}