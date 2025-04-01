package com.example.skysync.alerts.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.skysync.R
import com.example.skysync.alerts.viewmodel.AlertViewModelImp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

@Composable
fun AlertsScreen(viewModel: AlertViewModelImp) {
    var isAlarmBoardVisible by remember { mutableStateOf(false) }
    var selectedTimeInMillis by remember { mutableStateOf<Long?>(null) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        verticalArrangement = Arrangement.Bottom
    ) {
        if (isAlarmBoardVisible) {
            AlarmBoard(onTimeSelected = { timeInMillis ->
                selectedTimeInMillis = timeInMillis
            },selectedTimeInMillis = selectedTimeInMillis)
            ElevatedButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                colors = ButtonDefaults.elevatedButtonColors(MaterialTheme.colorScheme.primary.copy(.6f)),
                onClick = {
                    isAlarmBoardVisible = false
                    selectedTimeInMillis?.let {
                        viewModel.addAlert(it)
                    }
                }) { Text("Save") }
        }
        if (!isAlarmBoardVisible) {
            ElevatedButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                colors = ButtonDefaults.elevatedButtonColors(MaterialTheme.colorScheme.primary.copy(.3f)),
                onClick = {
                    isAlarmBoardVisible = true
                    viewModel.requestNotificationPermission()
                }) { Text("Add New Alarm") }

        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AlarmBoard(onTimeSelected: (Long) -> Unit,
                       selectedTimeInMillis: Long? ) {
    var notificationChecked by remember { mutableStateOf(true) }
    var alarmChecked by remember { mutableStateOf(false) }
    ///////
    var isPickerVisible by remember { mutableStateOf(false) }
    val currentTime = Calendar.getInstance()

    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = true,
    )
    if (isPickerVisible)
    {
        TimePickerDialog(
            onCancel = {isPickerVisible =false},
            onConfirm = {val calendar = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, timePickerState.hour)
                set(Calendar.MINUTE, timePickerState.minute)
            }

                val timeInMillis = calendar.timeInMillis
               /// Log.i(TAG ,"Selected: $timeInMillis $selectedHour :$selectedMinute")
                onTimeSelected(timeInMillis)
                isPickerVisible = false },
            toggle = {},
            content = {TimePicker(state = timePickerState)}
        )
    }
    ///////////
    Column(
        Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(10.dp)
            .background(
                color = MaterialTheme.colorScheme.secondary,
                shape = RoundedCornerShape(20.dp)
            ), horizontalAlignment = Alignment.CenterHorizontally
    ) {


        Row (Modifier.padding(top = 10.dp)){
            Text("Set Alert", fontSize = 20.sp)
        }
        Row (verticalAlignment = Alignment.CenterVertically){

                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp, horizontal = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painterResource(id = R.drawable.ic_alarm_clock),
                        contentDescription = null,
                        tint = Color.Unspecified
                    )
                    Text(" Time : ")
                    TextButton( onClick = { isPickerVisible= true }) {
                        Text(
                            selectedTimeInMillis?.let {
                                SimpleDateFormat("HH:mm a").format(Date(it))
                            } ?: "Select Time",
                            fontSize = 20.sp
                        )
                    }

              /*  Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp, horizontal = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painterResource(id = R.drawable.ic_alarm_clock),
                        contentDescription = null,
                        tint = Color.Unspecified
                    )
                    Text(" To : ", Modifier.weight(.6f))
                    TextButton(modifier = Modifier.weight(2f), onClick = {}) {
                        Text("Start Date", fontSize = 20.sp)
                    }
                }*/
            }
        }
        Row(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
                .weight(.5f), horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    onClick = {
                        notificationChecked = true
                        alarmChecked = false
                    },
                    selected =  notificationChecked

                )
                Text("Notification")
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    onClick = {
                        alarmChecked = true
                        notificationChecked = false
                    },
                    selected =  alarmChecked

                )
                Text("Alarm")
            }
        }

    }
}

@Composable
fun TimePickerDialog(
    title: String = "Select Time",
    onCancel: () -> Unit,
    onConfirm: () -> Unit,
    toggle: @Composable () -> Unit = {},
    content: @Composable () -> Unit,
) {
    Dialog(
        onDismissRequest = onCancel,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        ),
    ) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            tonalElevation = 6.dp,
            modifier = Modifier
                .width(IntrinsicSize.Min)
                .height(IntrinsicSize.Min)
                .background(
                    shape = MaterialTheme.shapes.extraLarge,
                    color = MaterialTheme.colorScheme.surface
                ),
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    text = title,
                    style = MaterialTheme.typography.labelMedium
                )
                content()
                Row(
                    modifier = Modifier
                        .height(40.dp)
                        .fillMaxWidth()
                ) {
                    toggle()
                    Spacer(modifier = Modifier.weight(1f))
                    TextButton(
                        onClick = onCancel
                    ) { Text("Cancel") }
                    TextButton(
                        onClick = onConfirm
                    ) { Text("OK") }
                }
            }
        }
    }
}

