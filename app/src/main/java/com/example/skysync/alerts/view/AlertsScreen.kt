package com.example.skysync.alerts.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.skysync.R
import com.example.skysync.alerts.viewmodel.AlertViewModelImp

@Composable
fun AlertsScreen(viewModel: AlertViewModelImp) {
    var isAlarmBoardVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Bottom
        //contentAlignment = Alignment.Center
    ) {
        if (isAlarmBoardVisible) {
            AlarmBoard()
            ElevatedButton(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                colors = ButtonDefaults.elevatedButtonColors(MaterialTheme.colorScheme.primary.copy(.6f)),
                onClick = {
                    isAlarmBoardVisible = false
                    viewModel.addAlert()
                }) { Text("Save") }

        }
        if (!isAlarmBoardVisible) {
            ElevatedButton(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                colors = ButtonDefaults.elevatedButtonColors(MaterialTheme.colorScheme.primary.copy(.3f)),
                onClick = {
                    isAlarmBoardVisible = true
                    viewModel.requestNotificationPermission()
                }) { Text("Add New Alarm") }

        }
    }
}
@Preview
@Composable
private fun AlarmBoard() {
    var notificationChecked by remember { mutableStateOf(true) }
    var alarmChecked by remember { mutableStateOf(false) }
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
        Text("Set Alert", fontSize = 20.sp)
        Row (Modifier.fillMaxHeight(.8f)){
            Column(
                Modifier
                    .weight(1f)
                .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Row(
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
                    Text(" From : ",Modifier.weight(.6f))
                    TextButton(modifier = Modifier.weight(2f), onClick = {}) {
                        Text("Start Date", fontSize = 20.sp)
                    }
                }


                Row(
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
                }
            }

        }
        Row(
            Modifier
                .fillMaxWidth()
                .weight(.5f), horizontalArrangement = Arrangement.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = notificationChecked, onCheckedChange = {
                    notificationChecked = it
                    alarmChecked = !it
                })
                Text("Notification")
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = alarmChecked, onCheckedChange = {
                    notificationChecked = !it
                    alarmChecked = it
                })
                Text("Alarm")
            }
        }

    }
}