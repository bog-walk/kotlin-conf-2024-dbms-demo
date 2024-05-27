package dev.bogwalk.ui.components.forms

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.bogwalk.common.generated.resources.Res
import dev.bogwalk.common.generated.resources.planet_prices
import dev.bogwalk.ui.style.smallDp
import dev.bogwalk.ui.style.windowWidth
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun PriceRangeRow(
    lowerValue: String,
    upperValue: String,
    onLowerValueChange: (String) -> Unit,
    onUpperValueChange: (String) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BaseTextField(
            lowerValue,
            stringResource(Res.string.planet_prices),
            modifier = Modifier.requiredWidth(windowWidth / 4).padding(smallDp),
            onValueChange = { onLowerValueChange(it) }
        )
        Text(text = "to", modifier = Modifier.padding(smallDp))
        BaseTextField(
            upperValue,
            stringResource(Res.string.planet_prices),
            modifier = Modifier.requiredWidth(windowWidth / 4).padding(smallDp),
            onValueChange = { onUpperValueChange(it) }
        )
    }
}