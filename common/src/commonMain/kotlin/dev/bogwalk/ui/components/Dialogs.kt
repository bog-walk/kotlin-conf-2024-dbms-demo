package dev.bogwalk.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.*
import dev.bogwalk.common.generated.resources.Res
import dev.bogwalk.common.generated.resources.confirm_button
import dev.bogwalk.ui.style.dialogSize
import dev.bogwalk.ui.style.largeDp
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun CustomDialog(
    text: String,
    onConfirmRequest: () -> Unit,
    onCloseRequest: () -> Unit
) {
    DialogWindow(
        onCloseRequest = { onCloseRequest() },
        state = DialogState(WindowPosition(Alignment.Center), dialogSize, dialogSize),
        title = "",
        resizable = false
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text, color = MaterialTheme.colorScheme.onBackground, textAlign = TextAlign.Center)
            Spacer(Modifier.height(largeDp))
            DialogButton(stringResource(Res.string.confirm_button), onConfirmRequest)
        }
    }
}
