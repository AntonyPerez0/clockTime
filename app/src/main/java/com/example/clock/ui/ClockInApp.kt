package com.example.clock

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun ClockInApp(viewModel: ClockInViewModel) {
    val clockInTime by viewModel.clockInTime.collectAsState()
    val clockOutTime by viewModel.clockOutTime.collectAsState()
    val customClockInTime by viewModel.customClockInTime.collectAsState()
    val customClockInInput by viewModel.customClockInInput.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    val effectiveClockInTime = customClockInTime ?: clockInTime
    val expectedClockOutTime = effectiveClockInTime?.plusHours(8)?.plusMinutes(30)
    val displayFormatter = DateTimeFormatter.ofPattern("h:mm a", Locale.US)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = customClockInInput,
                onValueChange = { viewModel.updateCustomClockInInput(it) },
                label = { Text("Custom Clock-In Time (h:mm AM/PM)") },
                placeholder = { Text("e.g., 9:00 AM") },
                isError = errorMessage != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            if (errorMessage != null) {
                Text(
                    text = errorMessage ?: "",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(start = 16.dp)
                )
            }

            Button(
                onClick = { viewModel.saveCustomClockInTime() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text("Save Custom Clock-In Time")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { viewModel.clockIn() },
                    modifier = Modifier
                        .width(120.dp)
                        .height(40.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text("Clock In")
                }

                Button(
                    onClick = { viewModel.clockOut() },
                    enabled = effectiveClockInTime != null && clockOutTime == null,
                    modifier = Modifier
                        .width(120.dp)
                        .height(40.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary
                    )
                ) {
                    Text("Clock Out")
                }

                Button(
                    onClick = { viewModel.resetClockInTime() },
                    enabled = effectiveClockInTime != null || clockOutTime != null,
                    modifier = Modifier
                        .width(160.dp)
                        .height(40.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Clear Clock-In Time")
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            customClockInTime?.let {
                Text(
                    "Your custom clock-in time: ${it.format(displayFormatter)}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            clockInTime?.let {
                Text(
                    "You clocked in at: ${it.format(displayFormatter)}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            expectedClockOutTime?.let {
                Text(
                    "Your expected clock-out time is: ${it.format(displayFormatter)}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            clockOutTime?.let {
                Text(
                    "You clocked out at: ${it.format(displayFormatter)}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}