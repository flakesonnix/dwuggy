/*
 * Copyright (c) 2024. Isaak Hanimann.
 * This file is part of PsychonautWiki Journal.
 *
 * PsychonautWiki Journal is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at
 * your option) any later version.
 *
 * PsychonautWiki Journal is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with PsychonautWiki Journal.  If not, see https://www.gnu.org/licenses/gpl-3.0.en.html.
 */

package com.isaakhanimann.journal.ui.tabs.search.custom

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.ExpandedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.isaakhanimann.journal.data.substances.AdministrationRoute
import com.isaakhanimann.journal.data.substances.classes.roa.CustomRoa
import com.isaakhanimann.journal.ui.theme.horizontalPadding

@Composable
fun AddOrEditCustomSubstanceAdvancedContent(
    padding: PaddingValues,
    name: String,
    onNameChange: (String) -> Unit,
    units: String,
    onUnitsChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
    commonNames: String,
    onCommonNamesChange: (String) -> Unit,
    categories: String,
    onCategoriesChange: (String) -> Unit,
    tolerance: String,
    onToleranceChange: (String) -> Unit,
    addictionPotential: String,
    onAddictionPotentialChange: (String) -> Unit,
    summary: String,
    onSummaryChange: (String) -> Unit,
    effectsSummary: String,
    onEffectsSummaryChange: (String) -> Unit,
    dosageRemark: String,
    onDosageRemarkChange: (String) -> Unit,
    generalRisks: String,
    onGeneralRisksChange: (String) -> Unit,
    longtermRisks: String,
    onLongtermRisksChange: (String) -> Unit,
    saferUse: String,
    onSaferUseChange: (String) -> Unit,
    customRoas: List<CustomRoa>,
    onAddRoa: (CustomRoa) -> Unit,
    onUpdateRoa: (Int, CustomRoa) -> Unit,
    onRemoveRoa: (Int) -> Unit,
    isAdvancedMode: Boolean,
    onToggleAdvanced: () -> Unit,
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .padding(padding)
            .padding(horizontal = horizontalPadding)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(5.dp))

        OutlinedTextField(
            value = name,
            onValueChange = onNameChange,
            label = { Text("Name *") },
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Words
            ),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = units,
            onValueChange = onUnitsChange,
            label = { Text("Default Units *") },
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedButton(onClick = { onUnitsChange("µg") }) { Text("µg") }
            OutlinedButton(onClick = { onUnitsChange("mg") }) { Text("mg") }
            OutlinedButton(onClick = { onUnitsChange("g") }) { Text("g") }
            OutlinedButton(onClick = { onUnitsChange("mL") }) { Text("mL") }
        }

        OutlinedTextField(
            value = description,
            onValueChange = onDescriptionChange,
            label = { Text("Short Description") },
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Sentences
            ),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedButton(
            onClick = onToggleAdvanced,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isAdvancedMode) "Hide Advanced Editor" else "Show Advanced Editor")
        }

        if (isAdvancedMode) {
            Spacer(modifier = Modifier.height(8.dp))
            Text("Advanced Information", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))

            OutlinedTextField(
                value = commonNames,
                onValueChange = onCommonNamesChange,
                label = { Text("Common Names (comma separated)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = categories,
                onValueChange = onCategoriesChange,
                label = { Text("Categories (comma separated)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = tolerance,
                onValueChange = onToleranceChange,
                label = { Text("Tolerance") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = addictionPotential,
                onValueChange = onAddictionPotentialChange,
                label = { Text("Addiction Potential") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = summary,
                onValueChange = onSummaryChange,
                label = { Text("Summary") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2
            )

            OutlinedTextField(
                value = effectsSummary,
                onValueChange = onEffectsSummaryChange,
                label = { Text("Effects Summary") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2
            )

            OutlinedTextField(
                value = dosageRemark,
                onValueChange = onDosageRemarkChange,
                label = { Text("Dosage Remark") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2
            )

            OutlinedTextField(
                value = generalRisks,
                onValueChange = onGeneralRisksChange,
                label = { Text("General Risks") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2
            )

            OutlinedTextField(
                value = longtermRisks,
                onValueChange = onLongtermRisksChange,
                label = { Text("Longterm Risks") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2
            )

            OutlinedTextField(
                value = saferUse,
                onValueChange = onSaferUseChange,
                label = { Text("Safer Use Tips (one per line)") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            Spacer(modifier = Modifier.height(8.dp))
            Text("Routes of Administration", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))

            customRoas.forEachIndexed { index, roa ->
                CustomRoaCard(
                    customRoa = roa,
                    onUpdate = { updated -> onUpdateRoa(index, updated) },
                    onRemove = { onRemoveRoa(index) }
                )
                Spacer(modifier = Modifier.height(4.dp))
            }

            OutlinedButton(
                onClick = {
                    onAddRoa(
                        CustomRoa(
                            route = AdministrationRoute.ORAL,
                            doseUnits = units
                        )
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Text("Add Route of Administration")
            }
        }

        Spacer(modifier = Modifier.height(5.dp))
    }
}

@Composable
private fun CustomRoaCard(
    customRoa: CustomRoa,
    onUpdate: (CustomRoa) -> Unit,
    onRemove: () -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    ExpandedCard(
        onClick = { expanded = !expanded },
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(customRoa.route.displayText, style = MaterialTheme.typography.titleSmall)
                IconButton(onClick = onRemove) {
                    Icon(Icons.Default.Delete, contentDescription = "Remove")
                }
            }

            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = customRoa.doseUnits,
                    onValueChange = { onUpdate(customRoa.copy(doseUnits = it)) },
                    label = { Text("Dose Units") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Text("Dose Ranges (${customRoa.doseUnits})", style = MaterialTheme.typography.labelMedium)
                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = customRoa.thresholdMin?.toString() ?: "",
                        onValueChange = { onUpdate(customRoa.copy(thresholdMin = it.toDoubleOrNull())) },
                        label = { Text("Threshold") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = customRoa.lightMin?.toString() ?: "",
                        onValueChange = { onUpdate(customRoa.copy(lightMin = it.toDoubleOrNull())) },
                        label = { Text("Light") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = customRoa.commonMin?.toString() ?: "",
                        onValueChange = { onUpdate(customRoa.copy(commonMin = it.toDoubleOrNull())) },
                        label = { Text("Common") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = customRoa.strongMin?.toString() ?: "",
                        onValueChange = { onUpdate(customRoa.copy(strongMin = it.toDoubleOrNull())) },
                        label = { Text("Strong") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.weight(1f)
                    )
                }
                OutlinedTextField(
                    value = customRoa.heavyMin?.toString() ?: "",
                    onValueChange = { onUpdate(customRoa.copy(heavyMin = it.toDoubleOrNull())) },
                    label = { Text("Heavy") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )

                Text("Duration (minutes)", style = MaterialTheme.typography.labelMedium)
                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = customRoa.onsetMin?.toString() ?: "",
                        onValueChange = { onUpdate(customRoa.copy(onsetMin = it.toDoubleOrNull())) },
                        label = { Text("Onset Min") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = customRoa.onsetMax?.toString() ?: "",
                        onValueChange = { onUpdate(customRoa.copy(onsetMax = it.toDoubleOrNull())) },
                        label = { Text("Onset Max") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = customRoa.totalMin?.toString() ?: "",
                        onValueChange = { onUpdate(customRoa.copy(totalMin = it.toDoubleOrNull())) },
                        label = { Text("Total Min") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = customRoa.totalMax?.toString() ?: "",
                        onValueChange = { onUpdate(customRoa.copy(totalMax = it.toDoubleOrNull())) },
                        label = { Text("Total Max") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                }

                Text("Bioavailability (%)", style = MaterialTheme.typography.labelMedium)
                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = customRoa.bioavailabilityMin?.toString() ?: "",
                        onValueChange = { onUpdate(customRoa.copy(bioavailabilityMin = it.toDoubleOrNull())) },
                        label = { Text("Min") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = customRoa.bioavailabilityMax?.toString() ?: "",
                        onValueChange = { onUpdate(customRoa.copy(bioavailabilityMax = it.toDoubleOrNull())) },
                        label = { Text("Max") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}