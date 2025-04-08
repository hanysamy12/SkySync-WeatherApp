package com.example.skysync.alerts.view

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.skysync.R
import com.example.skysync.alerts.viewmodel.AlertViewModelImp
import com.example.skysync.helper.Constants
import com.example.skysync.helper.Response
import com.example.skysync.home.view.MessageShow
import com.example.skysync.home.view.ProgressShow
import com.example.skysync.models.Alert
import com.example.skysync.ui.navigation.ScreenRoute
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.UUID

private const val TAG = "AlertsScreen"
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AlertsScreen(viewModel: AlertViewModelImp, navController: NavController) {
    var isAlarmBoardVisible by remember { mutableStateOf(false) }
    var selectedTimeInMillis by remember { mutableStateOf<Long?>(null) }
    var alarmChecked by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()
    var alertToDelete by remember { mutableStateOf<UUID?>(null) }
    LaunchedEffect(Unit) {
        viewModel.getAllAlerts()
    }
    val uiAlertState by viewModel.alertList.collectAsStateWithLifecycle()
    when (uiAlertState) {
        is Response.Loading -> ProgressShow()
        is Response.Failure -> {
            val msg = (uiAlertState as Response.Failure).toString()
            MessageShow(msg)
        }

        is Response.Success<*> -> {
            val alertList = (uiAlertState as Response.Success).data
            Log.i(TAG, "AlertsScreen: AlertsList = $alertList")
            AlertsListShow (alertsList = alertList, onDeleteClicked = {alert ->
                coroutineScope.launch {
                }
            })
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        verticalArrangement = Arrangement.Bottom
    ) {
        if (isAlarmBoardVisible) {
            AlarmBoard(
                onTimeSelected = { timeInMillis ->
                    selectedTimeInMillis = timeInMillis
                },
                onMapClicked = {
                    navController.navigate(
                        ScreenRoute.GoogleMap(Constants.ALERTS_SCREEN)
                    )
                },
                selectedTimeInMillis = selectedTimeInMillis,
                alarmChecked = alarmChecked,
                onAlarmChecked = { alarmChecked = it },

                )
            ElevatedButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                colors = ButtonDefaults.elevatedButtonColors(
                    MaterialTheme.colorScheme.primary.copy(
                        .6f
                    )
                ),
                onClick = {
                    isAlarmBoardVisible = false
                    selectedTimeInMillis?.let { time ->
                        viewModel.addAlert(time, alarmChecked)
                    }
                }) { Text(stringResource(R.string.save)) }
        }
        if (!isAlarmBoardVisible) {
            ElevatedButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                colors = ButtonDefaults.elevatedButtonColors(
                    MaterialTheme.colorScheme.primary.copy(
                        .6f
                    )
                ),
                onClick = {
                    isAlarmBoardVisible = true
                    viewModel.requestNotificationPermission()
                }) { Text(stringResource(R.string.add_new_alarm)) }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AlarmBoard(
    onTimeSelected: (Long) -> Unit,
    onMapClicked: () -> Unit,
    selectedTimeInMillis: Long?,
    alarmChecked: Boolean,
    onAlarmChecked: (Boolean) -> Unit
) {

    var isPickerVisible by remember { mutableStateOf(false) }
    val currentTime = Calendar.getInstance()

    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = true,
    )
    if (isPickerVisible) {
        TimePickerDialog(
            onCancel = { isPickerVisible = false },
            onConfirm = {
                val calendar = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, timePickerState.hour)
                    set(Calendar.MINUTE, timePickerState.minute)
                }
                val timeInMillis = calendar.timeInMillis
                onTimeSelected(timeInMillis)
                isPickerVisible = false
            },
            toggle = {},
            content = { TimePicker(state = timePickerState) }
        )
    }
    ///////////
    Column(
        Modifier
            .fillMaxWidth()
            .height(250.dp)
            .padding(10.dp)
            .background(
                color = MaterialTheme.colorScheme.secondary,
                shape = RoundedCornerShape(20.dp)
            ), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(Modifier.padding(top = 10.dp)) {
            Text(stringResource(R.string.set_alert), fontSize = 20.sp)
        }

        Row(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp, horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                painterResource(id = R.drawable.ic_alarm_clock),
                contentDescription = "alert icon",
                tint = Color.Unspecified
            )
            Text(" Time : ")
            TextButton(
                onClick = { isPickerVisible = true }) {
                Text(
                    selectedTimeInMillis?.let {
                        SimpleDateFormat("HH:mm a", Locale.getDefault()).format(Date(it))
                    } ?: stringResource(R.string.select_time),
                    fontSize = 20.sp, textDecoration = TextDecoration.Underline
                )
            }


        }

        Row(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp, horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                painterResource(id = R.drawable.ic_location),
                contentDescription = null,
                tint = Color.Unspecified
            )
            Text(stringResource(R.string.pick_location))
            TextButton(
                 onClick = { onMapClicked() }) {
                Text(stringResource(R.string.map), fontSize = 20.sp, textDecoration = TextDecoration.Underline)
            }
        }
        Row(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp), horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    onClick = {
                        onAlarmChecked(false)
                    },
                    selected = !alarmChecked
                )
                Text(stringResource(R.string.notification))
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    onClick = {
                        onAlarmChecked(true)
                    },
                    selected = alarmChecked

                )
                Text(stringResource(R.string.alarm))
            }
        }

    }
}

@Composable
private fun AlertsListShow(
    alertsList: List<Alert>,
    onDeleteClicked: (UUID) -> Unit
) {

    LazyColumn(
        Modifier
            .fillMaxSize()
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(
                stringResource(R.string.alerts),
                fontSize = 22.sp,
                color = MaterialTheme.colorScheme.primary
            )
        }
        items(alertsList.size) {
            val currentAlert = alertsList[it]
            AlertItem(
                currentAlert,
                onFavClicked = {
                }, onDeleteClicked = { onDeleteClicked(currentAlert.id) })
        }
    }
}

@Composable
fun AlertItem(alert: Alert, onFavClicked: () -> Unit, onDeleteClicked: () -> Unit) {
    Column(
        Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.primary.copy(alpha = .5f),
                shape = RoundedCornerShape(20.dp)
            )
            .clickable {
                onFavClicked()
            },
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.SpaceAround,
    ) {
        Text(
            alert.name.toString(),
            fontSize = 22.sp,
            maxLines = 2,
            modifier = Modifier
                .padding(start = 12.dp, top = 12.dp)
        )
        Spacer(Modifier.height(8.dp))
        Text(
            alert.time.toString(),
            fontSize = 18.sp,
            fontStyle = FontStyle.Italic,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier
                .padding(start = 12.dp, bottom = 12.dp)
        )

    }
}


@Composable
fun TimePickerDialog(
    title: String = stringResource(R.string.select_time),
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
                    ) { Text(stringResource(R.string.cancel)) }
                    TextButton(
                        onClick = onConfirm
                    ) { Text(stringResource(R.string.ok)) }
                }
            }
        }
    }
}

