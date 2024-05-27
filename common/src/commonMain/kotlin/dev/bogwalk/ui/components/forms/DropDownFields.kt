package dev.bogwalk.ui.components.forms

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DropDownField(
    modifier: Modifier,
    fieldValue: String,
    fieldLabel: String,
    expanded: Boolean,
    onExpandedChange: () -> Unit,
    onValueChange: (String) -> Unit,
    onDismissRequest: () -> Unit,
    menuContent: @Composable (ColumnScope.() -> Unit)
) {
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { onExpandedChange() },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = fieldValue,
            onValueChange = { onValueChange(it) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth().menuAnchor(),
            label = { Text(fieldLabel) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) }
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { onDismissRequest() }
        ) {
            menuContent()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DropDownReadOnlyField(
    modifier: Modifier,
    fieldValue: String,
    fieldLabel: String,
    expanded: Boolean,
    onExpandedChange: () -> Unit,
    onDismissRequest: () -> Unit,
    menuContent: @Composable (ColumnScope.() -> Unit)
) {
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { onExpandedChange() },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = fieldValue,
            onValueChange = {},
            readOnly = true,
            singleLine = true,
            modifier = Modifier.fillMaxWidth().menuAnchor(),
            label = { Text(fieldLabel) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) }
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { onDismissRequest() }
        ) {
            menuContent()
        }
    }
}