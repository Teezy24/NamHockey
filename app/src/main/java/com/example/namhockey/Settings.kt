package com.example.namhockey

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    darkMode: Boolean,
    onDarkModeChanged: (Boolean) -> Unit,
    notificationsEnabled: Boolean,
    onNotificationsEnabledChanged: (Boolean) -> Unit,
    onLogout: (() -> Unit)? = null
) {
    var language by remember { mutableStateOf("English") }
    var dataSyncEnabled by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Settings",
            fontSize = 24.sp,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Dark Mode Toggle
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Dark Mode")
            Switch(
                checked = darkMode,
                onCheckedChange = { onDarkModeChanged(it) }
            )
        }

        Divider()

        // Notifications Toggle
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Enable Notifications")
            Switch(
                checked = notificationsEnabled,
                onCheckedChange = { onNotificationsEnabledChanged(it) }
            )
        }

        Divider()

        // Language Selection
        var expanded by remember { mutableStateOf(false) }
        val languages = listOf("English", "Afrikaans", "Oshiwambo")

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text("Language")
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = it }
            ) {
                TextField(
                    value = language,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    languages.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                language = option
                                expanded = false
                            }
                        )
                    }
                }
            }
        }

        Divider()




        // About Section
        Text(
            text = "About",
            fontSize = 20.sp,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        Text(
            text = "NamHockey v1.0.2",
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = "© 2025 NamHockey. All rights reserved by Us.",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(top = 4.dp)
        )

        // Logout button
        if (onLogout != null) {
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = { onLogout() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp)
            ) {
                Text("Logout")
            }
        }
    }
}

