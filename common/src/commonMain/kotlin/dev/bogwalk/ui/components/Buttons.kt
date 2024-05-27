package dev.bogwalk.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import dev.bogwalk.common.generated.resources.*
import dev.bogwalk.common.generated.resources.Res
import dev.bogwalk.common.generated.resources.back_cd
import dev.bogwalk.common.generated.resources.save_cd
import dev.bogwalk.common.generated.resources.search_cd
import dev.bogwalk.ui.style.*
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun BackButton(
    modifier: Modifier,
    onReturnRequest: () -> Unit
) {
    IconButton(
        onClick = onReturnRequest,
        modifier = modifier.padding(largeDp)
    ) {
        Icon(imageVector = Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = stringResource(Res.string.back_cd))
    }
}

@Composable
internal fun OptionButton(
    image: ImageVector,
    description: String,
    isEnabled: Boolean,
    onRequest: () -> Unit
) {
    IconButton(
        onClick = onRequest,
        enabled = isEnabled
    ) {
        Icon(imageVector = image, contentDescription = description)
    }
}

@Composable
internal fun DialogButton(
    text: String,
    onConfirmRequest: () -> Unit
) {
    OutlinedButton(
        onClick = { onConfirmRequest() },
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = MaterialTheme.colorScheme.primary,
            containerColor = MaterialTheme.colorScheme.surface
        ),
    ) {
        Text(text = text)
    }
}

@Composable
internal fun FormButton(
    modifier: Modifier,
    isEnabled: Boolean,
    isSearch: Boolean,
    onConfirmData: () -> Unit
) {
    ElevatedButton(
        onClick = { onConfirmData() },
        modifier = modifier,
        enabled = isEnabled,
        shape = MaterialTheme.shapes.large
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (isSearch) {
                Icon(imageVector = Icons.Rounded.Search, contentDescription = stringResource(Res.string.search_cd))
            } else {
                Icon(imageVector = Icons.Rounded.Check, contentDescription = stringResource(Res.string.save_cd))
            }
            Spacer(Modifier.width(mediumDp))
            Text(if (isSearch) stringResource(Res.string.search_button) else stringResource(Res.string.save_button))
        }
    }
}

@Composable
internal fun LabelRadioButton(
    selected: Boolean,
    label: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier.selectable(
            selected = selected,
            onClick = { onClick() },
            role = Role.RadioButton
        )
            .padding(start = smallDp, top = smallDp, bottom = smallDp)
            .clip(MaterialTheme.shapes.large),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = null,
            modifier = Modifier.padding(end = smallDp)
        )
        Text(
            text = label,
            modifier = Modifier.wrapContentHeight(Alignment.CenterVertically),
            style = MaterialTheme.typography.titleSmall
        )
    }
}