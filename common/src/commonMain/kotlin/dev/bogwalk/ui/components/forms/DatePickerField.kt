package dev.bogwalk.ui.components.forms

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.bogwalk.common.generated.resources.*
import dev.bogwalk.ui.components.DialogButton
import dev.bogwalk.ui.style.smallDp
import kotlinx.datetime.*
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun DatePickerField(
    receivedDate: LocalDate,
    modifier: Modifier? = null,
    onDateChange: (LocalDate) -> Unit
) {
    var date by remember { mutableStateOf(receivedDate) }
    var isOpen by remember { mutableStateOf(false) }

    Row(
        modifier = modifier ?: Modifier.padding(smallDp).fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = date.format(LocalDate.Formats.ISO),
            readOnly = true,
            label = { Text(if (modifier == null) stringResource(Res.string.ship_arrival_date) else "") },
            onValueChange = {},
            trailingIcon = {
                IconButton(onClick = { isOpen = true }) {
                    Icon(imageVector = Icons.Rounded.CalendarToday, contentDescription = stringResource(Res.string.calendar_cd))
                }
            }
        )
    }

    if (isOpen) {
        DatePickerDialog(
            onConfirmRequest = {
                isOpen = false
                if (it != null) {
                    date = Instant.fromEpochMilliseconds(it).toLocalDateTime(TimeZone.UTC).date
                    onDateChange(date)
                }
            },
            onCloseRequest = { isOpen = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DatePickerDialog(
    onConfirmRequest: (Long?) -> Unit,
    onCloseRequest: () -> Unit
) {
    val state = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onCloseRequest,
        confirmButton = {
            DialogButton(stringResource(Res.string.confirm_button)) { onConfirmRequest(state.selectedDateMillis) }
        },
        dismissButton = {
            DialogButton(stringResource(Res.string.cancel_button), onCloseRequest)
        }
    ) {
        DatePicker(state = state)
    }
}
