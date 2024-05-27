package dev.bogwalk.ui.components.forms

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bogwalk.ui.style.mediumDp
import dev.bogwalk.ui.style.smallDp

@Composable
internal fun BaseForm(
    content: @Composable (ColumnScope.() -> Unit),
    button: @Composable (BoxScope.(Modifier) -> Unit)
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = smallDp, end = smallDp, bottom = smallDp)
            .border(2.dp, MaterialTheme.colorScheme.primary, MaterialTheme.shapes.medium),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(mediumDp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            content()
        }
        button(Modifier.padding(smallDp).align(Alignment.BottomEnd))
    }
}

@Composable
internal fun BaseTextField(
    value: String,
    label: String,
    enabled: Boolean = true,
    modifier: Modifier? = null,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = { onValueChange(it) },
        modifier = modifier ?: Modifier.fillMaxWidth().padding(smallDp),
        singleLine = true,
        label = { Text(text = label) },
        enabled = enabled
    )
}