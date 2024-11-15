package dev.bogwalk.ui.components.forms

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Flight
import androidx.compose.material.icons.rounded.Language
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bogwalk.common.generated.resources.*
import dev.bogwalk.models.*
import dev.bogwalk.ui.components.FormButton
import dev.bogwalk.ui.components.LabelRadioButton
import dev.bogwalk.ui.style.dateFieldSize
import dev.bogwalk.ui.style.mediumDp
import dev.bogwalk.ui.style.smallDp
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterForm(
    allPlanets: List<PlanetInfo>,
    maxPlanetDistance: Float,
    onFilterRequest: (FilterConditions) -> Unit
) {
    var filterIndex by remember { mutableStateOf(0) }

    var planetName by remember { mutableStateOf<String?>(null) }
    var planetsExpanded by remember { mutableStateOf(false) }
    var region by remember { mutableStateOf<Region?>(null) }
    var regionsExpanded by remember { mutableStateOf(false) }
    val distanceBound = remember {
        RangeSliderState(0f, maxPlanetDistance, 10000, {}, 0f..maxPlanetDistance)
    }
    var priceLower by remember { mutableStateOf("0") }
    var priceUpper by remember { mutableStateOf("0") }

    var type by remember { mutableStateOf<String?>(null) }
    var nullTypeOnly by remember { mutableStateOf(false) }
    var license by remember { mutableStateOf<String?>(null) }
    var licenseInvalid by remember { mutableStateOf(false) }
    var dateOption by remember { mutableStateOf<DateOption?>(null) }
    var dateOptionsExpanded by remember { mutableStateOf(false) }
    var comparisonDate by remember { mutableStateOf(currentDate()) }

    BaseForm(
        content = {
            SingleChoiceSegmentedButtonRow(Modifier.fillMaxWidth().padding(smallDp)) {
                DBMSSegmentedButton(0, filterIndex == 1, { filterIndex = 1 }, stringResource(Res.string.planets)) {
                    Icon(imageVector = Icons.Rounded.Language, contentDescription = stringResource(Res.string.planet_cd))
                }
                DBMSSegmentedButton(1, filterIndex == 2, { filterIndex = 2 }, stringResource(Res.string.ships)) {
                    Icon(imageVector = Icons.Rounded.Flight, contentDescription = stringResource(Res.string.ship_cd))
                }
            }
            when (filterIndex) {
                1 -> {
                    DropDownField(
                        Modifier.fillMaxWidth().padding(smallDp),
                        planetName ?: "",
                        stringResource(Res.string.ship_origin_planet),
                        planetsExpanded,
                        { planetsExpanded = !planetsExpanded },
                        { planetName = it },
                        { planetsExpanded = false }
                    ) {
                        allPlanets.forEach { p ->
                            DropdownMenuItem(
                                text = { Text(p.name) },
                                onClick = {
                                    planetName = p.name
                                    planetsExpanded = !planetsExpanded
                                }
                            )
                        }
                    }
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
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(smallDp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(text = stringResource(Res.string.planet_distance), modifier = Modifier.weight(0.1f).padding(top = smallDp), style = MaterialTheme.typography.titleSmall)
                        Spacer(Modifier.width(20.dp))
                        Column(
                            modifier = Modifier.weight(0.5f),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            RangeSlider(state = distanceBound)
                            Text(
                                text = "${distanceBound.activeRangeStart.toInt()} to ${distanceBound.activeRangeEnd.toInt()}"
                            )
                        }
                    }
                    PriceRangeRow(priceLower, priceUpper, { priceLower = it }) { priceUpper = it }
                }
                2 -> {
                    ShipLicenseField(license ?: "", licenseInvalid) {
                        license = it.uppercase()
                        license?.let { l ->
                            licenseInvalid = validateLicense(l)
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(smallDp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        BaseTextField(type ?: "", stringResource(Res.string.ship_type), !nullTypeOnly, Modifier.weight(0.1f)) { type = it }
                        LabelRadioButton(nullTypeOnly, stringResource(Res.string.ship_unclassified)) {
                            type = null
                            nullTypeOnly = !nullTypeOnly
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(smallDp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(stringResource(Res.string.ship_arrival_date), style = MaterialTheme.typography.titleSmall)
                        DropDownReadOnlyField(
                            Modifier.padding(horizontal = mediumDp).weight(0.1f),
                            dateOption?.text ?: "",
                            "",
                            dateOptionsExpanded,
                            { dateOptionsExpanded = !dateOptionsExpanded},
                            { dateOptionsExpanded = false }
                        ) {
                            DateOption.entries.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option.text) },
                                    onClick = {
                                        dateOption = option
                                        dateOptionsExpanded = !dateOptionsExpanded
                                    }
                                )
                            }
                        }
                        DatePickerField(comparisonDate, Modifier.requiredWidth(dateFieldSize)) { comparisonDate = it }
                    }
                }
            }
        },
        button = {
            FormButton(
                modifier = it,
                isEnabled = filterIndex != 0,
                isSearch = true,
                onConfirmData = {
                    val lowerDistance = if (distanceBound.activeRangeStart == 0f) null else distanceBound.activeRangeStart.toDouble()
                    val upperDistance = if (distanceBound.activeRangeEnd == MAX_PLANET_DISTANCE) null else distanceBound.activeRangeEnd.toDouble()
                    val range = if (priceLower == "0" && priceUpper == "0") null else priceLower.toInt()..priceUpper.toInt()
                    val exactDate = if (dateOption == DateOption.EQUALS) comparisonDate else null
                    val lowerDate = if (dateOption == DateOption.GREATER) comparisonDate else null
                    val upperDate = if (dateOption == DateOption.LESSER) comparisonDate else null
                    onFilterRequest(
                        FilterConditions(planetName, region, lowerDistance, upperDistance, range, license?.formatToLicense(), type, nullTypeOnly, exactDate, lowerDate, upperDate)
                    )
                }
            )
        }
    )
}

@Composable
private fun SingleChoiceSegmentedButtonRowScope.DBMSSegmentedButton(
    index: Int,
    selected: Boolean,
    onClick: () -> Unit,
    label: String,
    icon: @Composable () -> Unit
) {
    SegmentedButton(
        selected = selected,
        onClick = { onClick() },
        colors = SegmentedButtonDefaults.colors(
            activeBorderColor = MaterialTheme.colorScheme.primary,
            activeContentColor = MaterialTheme.colorScheme.primary,
            activeContainerColor = MaterialTheme.colorScheme.surface
        ),
        shape = SegmentedButtonDefaults.itemShape(index = index, count = 2),
        icon = { icon() },
        label = { Text(text = label) }
    )
}