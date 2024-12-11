package com.example.clock

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.time.format.DateTimeFormatter

@Composable
fun ClockInApp(viewModel: ClockInViewModel) {
    val clockInTime by viewModel.clockInTime.collectAsState()
    val clockOutTime by viewModel.clockOutTime.collectAsState()

    val expectedClockOutTime = clockInTime?.plusHours(8)?.plusMinutes(30)
    val timeFormatter = DateTimeFormatter.ofPattern("hh:mm a")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background) // Set background color
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Row containing Clock In and Clock Out Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Clock In Button
            Button(
                onClick = { viewModel.clockIn() },
                modifier = Modifier
                    .width(150.dp)
                    .height(50.dp),
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text("Clock In")
            }

            // Clock Out Button
            Button(
                onClick = { viewModel.clockOut() },
                enabled = clockInTime != null && clockOutTime == null,
                modifier = Modifier
                    .width(150.dp)
                    .height(50.dp),
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Text("Clock Out")
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        // Display Clock In Time
        clockInTime?.let {
            Text(
                "You clocked in at: ${it.format(timeFormatter)}",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        // Display Expected Clock Out Time
        expectedClockOutTime?.let {
            Text(
                "Your expected clock-out time is: ${it.format(timeFormatter)}",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        // Display Clock Out Time
        clockOutTime?.let {
            Text(
                "You clocked out at: ${it.format(timeFormatter)}",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}