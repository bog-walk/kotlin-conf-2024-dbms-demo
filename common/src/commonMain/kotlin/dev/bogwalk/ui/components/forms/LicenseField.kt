package dev.bogwalk.ui.components.forms

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import dev.bogwalk.common.generated.resources.*
import dev.bogwalk.ui.style.smallDp
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun ShipLicenseField(
    value: String,
    invalidInput: Boolean,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = { onValueChange(it.take(14)) },
        modifier = Modifier.fillMaxWidth().padding(smallDp),
        singleLine = true,
        label = { Text(stringResource(Res.string.ship_license)) },
        placeholder = { Text(stringResource(Res.string.ship_license_format)) },
        trailingIcon = {
            if (invalidInput) {
                Icon(imageVector = Icons.Rounded.Error, contentDescription = stringResource(Res.string.error_cd), tint = MaterialTheme.colorScheme.error)
            }
        },
        supportingText = {
            if (invalidInput) { Text("${stringResource(Res.string.invalid_license_message)} ${stringResource(Res.string.ship_license_format)}") }
        },
        isError = invalidInput,
        visualTransformation = LicenseVisualTransformation()
    )
}

internal fun String.formatFromLicense(): String = replace("-", "")

internal fun String.formatToLicense(): String = buildString {
    this@formatToLicense.forEachIndexed { index, ch ->
        append(ch)
        if (index == 2 || index == 7) append('-')
    }
}

internal fun validateLicense(input: String): Boolean = Regex(pattern).matches(input).not()

private const val pattern = """^[A-Z]{3}[0-9]{5}[A-Z]{2}[0-9]{2}[A-Z]{2}$"""

private class LicenseVisualTransformation : VisualTransformation {
    private val maskLength = 14

    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = if (text.length > maskLength) text.take(maskLength) else text

        val annotatedString = buildAnnotatedString {
            if (trimmed.isEmpty()) return@buildAnnotatedString

            var maskIndex = 0
            var textIndex = 0
            while (textIndex < trimmed.length && maskIndex < 16) {
                if (maskIndex == 3 || maskIndex == 9) {
                    append('-')
                    maskIndex += 1
                }
                append(trimmed[textIndex++])
                maskIndex++
            }
        }

        return TransformedText(annotatedString, LicenseOffsetMapper())
    }
}

private class LicenseOffsetMapper : OffsetMapping {
    private val mask = "XXX-XXXXX-XXXXXX"
    private val maskChar = 'X'

    override fun originalToTransformed(offset: Int): Int {
        var hyphenCount = 0
        var i = 0
        while (i < offset + hyphenCount) {
            if (mask[i++] != maskChar) hyphenCount++
        }
        return offset + hyphenCount
    }

    override fun transformedToOriginal(offset: Int) = offset - mask.take(offset).count { it != maskChar }
}