package dev.bogwalk.ui.components.forms

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import dev.bogwalk.common.generated.resources.Res
import dev.bogwalk.common.generated.resources.planet_distance
import dev.bogwalk.common.generated.resources.planet_name
import dev.bogwalk.common.generated.resources.planet_region
import dev.bogwalk.models.PlanetInfo
import dev.bogwalk.models.Region
import dev.bogwalk.ui.components.FormButton
import dev.bogwalk.ui.style.smallDp
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun PlanetForm(
    onConfirmPlanetData: (PlanetInfo) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var regionsExpanded by remember { mutableStateOf(false) }
    var region by remember { mutableStateOf<Region?>(null) }
    var distance by remember { mutableStateOf("0.0") }
    var priceLower by remember { mutableStateOf("0") }
    var priceUpper by remember { mutableStateOf("0") }

    BaseForm(
        content = {
            BaseTextField(name, stringResource(Res.string.planet_name)) { name = it }
            DropDownReadOnlyField(
                Modifier.fillMaxWidth().padding(smallDp),
                region?.output ?: "",
                stringResource(Res.string.planet_region),
                regionsExpanded,
                { regionsExpanded = !regionsExpanded },
                { regionsExpanded = false }
            ) {
                Region.entries.forEach { r ->
                    DropdownMenuItem(
                        text = { Text(r.output) },
                        onClick = {
                            region = r
                            regionsExpanded = !regionsExpanded
                        },
                    )
                }
            }
            BaseTextField(distance, stringResource(Res.string.planet_distance)) { distance = it }
            PriceRangeRow(priceLower, priceUpper, { priceLower = it }) { priceUpper = it }
        },
        button = {
            FormButton(
                modifier = it,
                isEnabled = name.isNotEmpty() && region != null && distance.ifEmpty { "0.0" }.toDouble() > 0.0,
                isSearch = false,
                onConfirmData = {
                    val range = if (priceLower == "0" && priceUpper == "0") 0..1000 else priceLower.toInt()..priceUpper.toInt()
                    onConfirmPlanetData(
                        PlanetInfo(-1, name, region!!, distance.toDouble(), range)
                    )
                }
            )
        }
    )
}