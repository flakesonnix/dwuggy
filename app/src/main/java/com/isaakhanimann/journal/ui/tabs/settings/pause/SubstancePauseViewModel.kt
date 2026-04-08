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

package com.isaakhanimann.journal.ui.tabs.settings.pause

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isaakhanimann.journal.data.room.experiences.ExperienceRepository
import com.isaakhanimann.journal.data.room.experiences.entities.SubstanceCompanion
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class SubstancePauseViewModel @Inject constructor(
    private val experienceRepository: ExperienceRepository
) : ViewModel() {

    private val _substanceCompanionsFlow = MutableStateFlow<List<SubstanceCompanion>>(emptyList())
    val substanceCompanionsFlow: StateFlow<List<SubstanceCompanion>> = _substanceCompanionsFlow

    init {
        viewModelScope.launch {
            experienceRepository.getAllSubstanceCompanionsFlow().collect {
                _substanceCompanionsFlow.value = it
            }
        }
    }

    fun toggleBlacklist(substanceName: String) {
        viewModelScope.launch {
            val companion = _substanceCompanionsFlow.value.find { it.substanceName == substanceName }
            companion?.let {
                val updated = it.copy(isBlacklisted = !it.isBlacklisted)
                experienceRepository.update(updated)
                updateLocalList(updated)
            }
        }
    }

    fun togglePause(substanceName: String) {
        viewModelScope.launch {
            val companion = _substanceCompanionsFlow.value.find { it.substanceName == substanceName }
            companion?.let {
                val updated = if (it.isPaused()) {
                    it.copy(pauseEndDate = null)
                } else {
                    it.copy(pauseEndDate = Instant.now().plusSeconds(30 * 24 * 60 * 60))
                }
                experienceRepository.update(updated)
                updateLocalList(updated)
            }
        }
    }

    fun setPauseEndDate(substanceName: String, endDate: Instant?) {
        viewModelScope.launch {
            val companion = _substanceCompanionsFlow.value.find { it.substanceName == substanceName }
            companion?.let {
                val updated = it.copy(pauseEndDate = endDate)
                experienceRepository.update(updated)
                updateLocalList(updated)
            }
        }
    }

    fun removePause(substanceName: String) {
        viewModelScope.launch {
            val companion = _substanceCompanionsFlow.value.find { it.substanceName == substanceName }
            companion?.let {
                val updated = it.copy(pauseEndDate = null)
                experienceRepository.update(updated)
                updateLocalList(updated)
            }
        }
    }

    private fun updateLocalList(updated: SubstanceCompanion) {
        _substanceCompanionsFlow.value = _substanceCompanionsFlow.value.map {
            if (it.substanceName == updated.substanceName) updated else it
        }
    }
}