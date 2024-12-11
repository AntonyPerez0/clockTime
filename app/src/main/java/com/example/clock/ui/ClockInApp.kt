package com.example.clock

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.time.format.DateTimeFormatter
import java.util.Locale
import android.util.Log

@Composable
fun ClockInApp(viewModel: ClockInViewModel) {
    val clockInTime by viewModel.clockInTime.collectAsState()
    val clockOutTime by viewModel.clockOutTime.collectAsState()
    val customClockInTime by viewModel.customClockInTime.collectAsState()
    val customClockInInput by viewModel.customClockInInput.collectAsState()
    val lunchClockInTime by viewModel.lunchClockInTime.collectAsState()
    val lunchClockOutTime by viewModel.lunchClockOutTime.collectAsState()
    val expectedLunchBackTime by viewModel.expectedLunchBackTime.collectAsState()
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
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                // Custom Clock-In Time Input
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

                // Row containing Clock In, Clock Out, and Clear Clock-In Time Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Clock In Button
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

                    // Clock Out Button
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

                    // Clear Clock-In Time Button
                    Button(
                        onClick = { viewModel.resetClockInTime() },
                        enabled = effectiveClockInTime != null || clockOutTime != null || lunchClockInTime != null || lunchClockOutTime != null,
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

                // Display Times
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    // Display Custom Clock-In Time
                    customClockInTime?.let {
                        Text(
                            "Your custom clock-in time: ${it.format(displayFormatter)}",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }

                    // Display Clock In Time
                    clockInTime?.let {
                        Text(
                            "You clocked in at: ${it.format(displayFormatter)}",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }

                    // Display Expected Clock Out Time
                    expectedClockOutTime?.let {
                        Text(
                            "Your expected clock-out time is: ${it.format(displayFormatter)}",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }

                    // Display Clock Out Time
                    clockOutTime?.let {
                        Text(
                            "You clocked out at: ${it.format(displayFormatter)}",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            }

            Divider(
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f),
                thickness = 1.dp,
                modifier = Modifier.fillMaxWidth()
            )

            // Lunch Section at the Bottom
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Lunch Break",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Lunch Clock In Button
                    Button(
                        onClick = {
                            Log.d("ClockInApp", "Lunch Clock In button clicked")
                            viewModel.lunchClockIn()
                        },
                        enabled = lunchClockInTime == null || lunchClockOutTime != null,
                        modifier = Modifier
                            .width(140.dp)
                            .height(40.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text("Lunch Clock In")
                    }

                    // Lunch Clock Out Button
                    Button(
                        onClick = {
                            Log.d("ClockInApp", "Lunch Clock Out button clicked")
                            viewModel.lunchClockOut()
                        },
                        enabled = lunchClockInTime != null && lunchClockOutTime == null,
                        modifier = Modifier
                            .width(140.dp)
                            .height(40.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary
                        )
                    ) {
                        Text("Lunch Clock Out")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Display Lunch Times and Expected Back Time
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    // Display Lunch Clock-In Time
                    lunchClockInTime?.let {
                        Text(
                            "You took lunch at: ${it.format(displayFormatter)}",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }

                    // Display Expected Lunch Back Time
                    if (expectedLunchBackTime != null && lunchClockOutTime == null) {
                        Text(
                            "You should clock back in by: ${expectedLunchBackTime?.format(displayFormatter)}",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }

                    // Display Lunch Clock-Out Time
                    lunchClockOutTime?.let {
                        Text(
                            "You returned from lunch at: ${it.format(displayFormatter)}",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            }
        }
    }
}