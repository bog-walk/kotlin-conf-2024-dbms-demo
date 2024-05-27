package dev.bogwalk.ui.components.forms

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.bogwalk.common.generated.resources.Res
import dev.bogwalk.common.generated.resources.add_planet_cd
import dev.bogwalk.common.generated.resources.ship_origin_planet
import dev.bogwalk.common.generated.resources.ship_type
import dev.bogwalk.models.PlanetInfo
import dev.bogwalk.models.ShipInfo
import dev.bogwalk.models.TemporaryShipInfo
import dev.bogwalk.models.currentDate
import dev.bogwalk.ui.components.FormButton
import dev.bogwalk.ui.style.smallDp
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun ShipForm(
    dbId: Int,
    ship: ShipInfo?,
    tempShip: TemporaryShipInfo?,
    allPlanets: List<PlanetInfo>,
    onAddPlanetRequest: ((TemporaryShipInfo?) -> Unit)?,
    onConfirmShipData: (ShipInfo) -> Unit
) {
    var license by remember { mutableStateOf((tempShip?.license ?: ship?.license)?.formatFromLicense() ?: "") }
    var licenseInvalid by remember { mutableStateOf(false) }
    var type by remember { mutableStateOf(tempShip?.type ?: ship?.type ?: "") }
    var planet by remember { mutableStateOf(tempShip?.originPlanet ?: ship?.originPlanet) }
    var planetsExpanded by remember { mutableStateOf(false) }
    var arrived by remember { mutableStateOf(tempShip?.arrivalDate ?: ship?.arrivalDate ?: currentDate()) }

    BaseForm(
        content = {
            ShipLicenseField(license, licenseInvalid) {
                license = it.uppercase()
                licenseInvalid = validateLicense(license)
            }
            BaseTextField(type, stringResource(Res.string.ship_type)) { type = it }
            DatePickerField(arrived) { arrived = it }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                DropDownReadOnlyField(
                    Modifier.weight(0.1f).padding(smallDp),
                    planet?.name ?: "",
                    stringResource(Res.string.ship_origin_planet),
                    planetsExpanded,
                    { planetsExpanded = !planetsExpanded },
                    { planetsExpanded = false }
                ) {
                    allPlanets.forEach { p ->
                        DropdownMenuItem(
                            text = { Text(p.name) },
                            onClick = {
                                planet = p
                                planetsExpanded = !planetsExpanded
                            },
                        )
                    }
                }
                onAddPlanetRequest?.let {
                    IconButton(
                        onClick = { it(TemporaryShipInfo(dbId, license.ifEmpty { null }, type.ifEmpty { null }, arrived, planet)) }
                    ) {
                        Icon(imageVector = Icons.Rounded.Add, contentDescription = stringResource(Res.string.add_planet_cd))
                    }
                }
            }
        },
        button = {
            FormButton(
                modifier = it,
                isEnabled = license.isNotEmpty() && planet != null,
                isSearch = false,
                onConfirmData = { onConfirmShipData(
                    ShipInfo(dbId, license.formatToLicense(), type.ifEmpty { null }, arrived, planet!!)
                ) }
            )
        }
    )
}