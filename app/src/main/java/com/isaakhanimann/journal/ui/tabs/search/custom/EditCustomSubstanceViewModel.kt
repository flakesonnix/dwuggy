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

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.isaakhanimann.journal.data.room.experiences.ExperienceRepository
import com.isaakhanimann.journal.data.room.experiences.entities.CustomSubstance
import com.isaakhanimann.journal.data.substances.classes.roa.CustomRoa
import com.isaakhanimann.journal.data.substances.classes.roa.CustomRoaParser
import com.isaakhanimann.journal.ui.main.navigation.graphs.EditCustomSubstanceRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditCustomSubstanceViewModel @Inject constructor(
    val experienceRepo: ExperienceRepository,
    state: SavedStateHandle
) : ViewModel() {

    var id = 0
    var name by mutableStateOf("")
    var units by mutableStateOf("")
    var description by mutableStateOf("")
    var commonNames by mutableStateOf("")
    var categories by mutableStateOf("")
    var tolerance by mutableStateOf("")
    var addictionPotential by mutableStateOf("")
    var summary by mutableStateOf("")
    var effectsSummary by mutableStateOf("")
    var dosageRemark by mutableStateOf("")
    var generalRisks by mutableStateOf("")
    var longtermRisks by mutableStateOf("")
    var saferUse by mutableStateOf("")
    var customRoas by mutableStateOf<List<CustomRoa>>(emptyList())

    val isValid get() = name.isNotBlank() && units.isNotBlank()

    init {
        val editCustomSubstanceRoute = state.toRoute<EditCustomSubstanceRoute>()
        val customSubstanceId = editCustomSubstanceRoute.customSubstanceId
        viewModelScope.launch {
            val customSubstance =
                experienceRepo.getCustomSubstanceFlow(customSubstanceId).firstOrNull() ?: return@launch
            id = customSubstanceId
            name = customSubstance.name
            units = customSubstance.units
            description = customSubstance.description
            commonNames = customSubstance.commonNames
            categories = customSubstance.categories
            tolerance = customSubstance.tolerance ?: ""
            addictionPotential = customSubstance.addictionPotential ?: ""
            summary = customSubstance.summary ?: ""
            effectsSummary = customSubstance.effectsSummary ?: ""
            dosageRemark = customSubstance.dosageRemark ?: ""
            generalRisks = customSubstance.generalRisks ?: ""
            longtermRisks = customSubstance.longtermRisks ?: ""
            saferUse = customSubstance.saferUse
            customRoas = CustomRoaParser.fromJson(customSubstance.roasJson)
        }
    }

    fun onDoneTap() {
        viewModelScope.launch {
            val customSubstance = CustomSubstance(
                id = id,
                name = name,
                units = units,
                description = description,
                commonNames = commonNames,
                categories = categories,
                tolerance = tolerance.ifBlank { null },
                addictionPotential = addictionPotential.ifBlank { null },
                summary = summary.ifBlank { null },
                effectsSummary = effectsSummary.ifBlank { null },
                dosageRemark = dosageRemark.ifBlank { null },
                generalRisks = generalRisks.ifBlank { null },
                longtermRisks = longtermRisks.ifBlank { null },
                saferUse = saferUse,
                roasJson = CustomRoaParser.toJson(customRoas)
            )
            experienceRepo.insert(customSubstance)
        }
    }

    fun deleteCustomSubstance() {
        viewModelScope.launch {
            experienceRepo.delete(CustomSubstance(id, name, units, description))
        }
    }

    fun addRoa(roa: CustomRoa) {
        customRoas = customRoas + roa
    }

    fun updateRoa(index: Int, roa: CustomRoa) {
        customRoas = customRoas.toMutableList().also { it[index] = roa }
    }

    fun removeRoa(index: Int) {
        customRoas = customRoas.toMutableList().also { it.removeAt(index) }
    }
}