/*
 * Copyright (c) 2022. Isaak Hanimann.
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

import androidx.compose.foundation.layout.imePadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCustomSubstanceScreen(
    navigateBack: () -> Unit,
    initialName: String = "",
    viewModel: AddCustomSubstanceViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.name = initialName
    }

    var isAdvancedMode by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Add custom substance") })
        },
        floatingActionButton = {
            if (viewModel.isValid) {
                ExtendedFloatingActionButton(
                    modifier = Modifier.imePadding(),
                    onClick = {
                        viewModel.addCustomSubstance {
                            navigateBack()
                        }
                    },
                    icon = {
                        Icon(
                            Icons.Filled.Done,
                            contentDescription = "Done"
                        )
                    },
                    text = { Text("Done") },
                )
            }
        }
    ) { padding ->
        AddOrEditCustomSubstanceAdvancedContent(
            padding = padding,
            name = viewModel.name,
            onNameChange = { viewModel.name = it },
            units = viewModel.units,
            onUnitsChange = { viewModel.units = it },
            description = viewModel.description,
            onDescriptionChange = { viewModel.description = it },
            commonNames = viewModel.commonNames,
            onCommonNamesChange = { viewModel.commonNames = it },
            categories = viewModel.categories,
            onCategoriesChange = { viewModel.categories = it },
            tolerance = viewModel.tolerance,
            onToleranceChange = { viewModel.tolerance = it },
            addictionPotential = viewModel.addictionPotential,
            onAddictionPotentialChange = { viewModel.addictionPotential = it },
            summary = viewModel.summary,
            onSummaryChange = { viewModel.summary = it },
            effectsSummary = viewModel.effectsSummary,
            onEffectsSummaryChange = { viewModel.effectsSummary = it },
            dosageRemark = viewModel.dosageRemark,
            onDosageRemarkChange = { viewModel.dosageRemark = it },
            generalRisks = viewModel.generalRisks,
            onGeneralRisksChange = { viewModel.generalRisks = it },
            longtermRisks = viewModel.longtermRisks,
            onLongtermRisksChange = { viewModel.longtermRisks = it },
            saferUse = viewModel.saferUse,
            onSaferUseChange = { viewModel.saferUse = it },
            customRoas = emptyList(),
            onAddRoa = {},
            onUpdateRoa = { _, _ -> },
            onRemoveRoa = {},
            isAdvancedMode = isAdvancedMode,
            onToggleAdvanced = { isAdvancedMode = !isAdvancedMode }
        )
    }
}