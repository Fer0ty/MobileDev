package com.example.newapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.newapp.db.DeviceDatabaseHelper
import com.example.newapp.db.Room
import com.example.newapp.db.RoomDatabaseHelper


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDeviceScreen(
    navController: NavController,
    deviceDatabaseHelper: DeviceDatabaseHelper,
    roomDatabaseHelper: RoomDatabaseHelper
) {
    var deviceName by remember { mutableStateOf("") }
    var deviceType by remember { mutableStateOf("") }
    var selectedRoom by remember { mutableStateOf<Room?>(null) }
    val roomList = roomDatabaseHelper.getAllRooms()
    val deviceTypes = listOf("датчик протечки", "лампочка", "датчик температуры", "чайник")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Мои устройства",
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
                },
                actions = {
                    IconButton(onClick = { navController.navigate("add_device") }) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Добавить",
                            tint = Color.White
                        )
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
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                OutlinedTextField(
                    value = deviceName,
                    onValueChange = { deviceName = it },
                    label = { Text("Название устройства") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text)
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Device Type Dropdown
                var expandedDeviceType by remember { mutableStateOf(false) }
                Column {
                    OutlinedTextField(
                        value = deviceType,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Тип устройства") },
                        trailingIcon = {
                            Icon(
                                imageVector = if (expandedDeviceType) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                contentDescription = null,
                                modifier = Modifier.clickable {
                                    expandedDeviceType = !expandedDeviceType
                                }
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { expandedDeviceType = !expandedDeviceType }
                    )
                    if (expandedDeviceType) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            deviceTypes.forEach { type ->
                                Text(
                                    text = type,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            deviceType = type
                                            expandedDeviceType = false
                                        }
                                        .padding(8.dp)
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                // Room Dropdown
                var expandedRoom by remember { mutableStateOf(false) }
                Column {
                    OutlinedTextField(
                        value = selectedRoom?.name ?: "",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Комната") },
                        trailingIcon = {
                            Icon(
                                imageVector = if (expandedRoom) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                contentDescription = null,
                                modifier = Modifier.clickable { expandedRoom = !expandedRoom }
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { expandedRoom = !expandedRoom }
                    )
                    if (expandedRoom) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            roomList.forEach { room ->
                                Text(
                                    text = room.name,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            selectedRoom = room
                                            expandedRoom = false
                                        }
                                        .padding(8.dp)
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (deviceName.isNotBlank() && deviceType.isNotBlank() && selectedRoom != null) {
                            deviceDatabaseHelper.addDevice(
                                deviceName,
                                deviceType,
                                selectedRoom!!.name
                            )
                            navController.popBackStack() // Возврат на предыдущий экран
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Добавить")
                }
            }
        }
    )
}