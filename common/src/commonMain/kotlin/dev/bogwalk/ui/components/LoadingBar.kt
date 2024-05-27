package dev.bogwalk.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import dev.bogwalk.common.generated.resources.Res
import dev.bogwalk.common.generated.resources.loading_message
import dev.bogwalk.ui.style.*
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun LoadingBar() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(windowHeight * 0.2f))
        Text(
            text = stringResource(Res.string.loading_message),
            modifier = Modifier.padding(mediumDp),
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.SemiBold
        )
        LinearProgressIndicator(
            modifier = Modifier
                .padding(mediumDp)
                .size(width = dialogSize, height = smallDp),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.secondary
        )
    }
}