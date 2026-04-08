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

package com.isaakhanimann.journal.data.substances.classes.roa

import com.isaakhanimann.journal.data.substances.AdministrationRoute

data class CustomRoa(
    val route: AdministrationRoute,
    val doseUnits: String = "",
    val thresholdMin: Double? = null,
    val lightMin: Double? = null,
    val commonMin: Double? = null,
    val strongMin: Double? = null,
    val heavyMin: Double? = null,
    val onsetMin: Double? = null,
    val onsetMax: Double? = null,
    val comeupMin: Double? = null,
    val comeupMax: Double? = null,
    val peakMin: Double? = null,
    val peakMax: Double? = null,
    val offsetMin: Double? = null,
    val offsetMax: Double? = null,
    val totalMin: Double? = null,
    val totalMax: Double? = null,
    val bioavailabilityMin: Double? = null,
    val bioavailabilityMax: Double? = null,
)

object CustomRoaParser {
    fun toJson(roas: List<CustomRoa>): String {
        return buildString {
            append("[")
            roas.forEachIndexed { index, roa ->
                if (index > 0) append(",")
                append("{")
                append("\"route\":\"${roa.route.name}\",")
                append("\"doseUnits\":\"${roa.doseUnits}\",")
                append("\"thresholdMin\":${roa.thresholdMin},")
                append("\"lightMin\":${roa.lightMin},")
                append("\"commonMin\":${roa.commonMin},")
                append("\"strongMin\":${roa.strongMin},")
                append("\"heavyMin\":${roa.heavyMin},")
                append("\"onsetMin\":${roa.onsetMin},")
                append("\"onsetMax\":${roa.onsetMax},")
                append("\"comeupMin\":${roa.comeupMin},")
                append("\"comeupMax\":${roa.comeupMax},")
                append("\"peakMin\":${roa.peakMin},")
                append("\"peakMax\":${roa.peakMax},")
                append("\"offsetMin\":${roa.offsetMin},")
                append("\"offsetMax\":${roa.offsetMax},")
                append("\"totalMin\":${roa.totalMin},")
                append("\"totalMax\":${roa.totalMax},")
                append("\"bioavailabilityMin\":${roa.bioavailabilityMin},")
                append("\"bioavailabilityMax\":${roa.bioavailabilityMax}")
                append("}")
            }
            append("]")
        }
    }

    fun fromJson(json: String): List<CustomRoa> {
        if (json.isBlank() || json == "[]") return emptyList()
        val roas = mutableListOf<CustomRoa>()
        try {
            val routes = json.removePrefix("[").removeSuffix("]").split("},{")
            for (routeStr in routes) {
                val clean = routeStr.removePrefix("{").removeSuffix("}")
                val props = clean.split(",").associate {
                    val keyValue = it.split(":")
                    val key = keyValue[0].removeSurrounding("\"")
                    val value = keyValue.getOrElse(1) { "null" }.removeSurrounding("\"")
                    key to value
                }
                val routeName = props["route"] ?: continue
                val route = try {
                    AdministrationRoute.valueOf(routeName)
                } catch (e: Exception) {
                    continue
                }
                roas.add(
                    CustomRoa(
                        route = route,
                        doseUnits = props["doseUnits"] ?: "",
                        thresholdMin = props["thresholdMin"]?.toDoubleOrNull(),
                        lightMin = props["lightMin"]?.toDoubleOrNull(),
                        commonMin = props["commonMin"]?.toDoubleOrNull(),
                        strongMin = props["strongMin"]?.toDoubleOrNull(),
                        heavyMin = props["heavyMin"]?.toDoubleOrNull(),
                        onsetMin = props["onsetMin"]?.toDoubleOrNull(),
                        onsetMax = props["onsetMax"]?.toDoubleOrNull(),
                        comeupMin = props["comeupMin"]?.toDoubleOrNull(),
                        comeupMax = props["comeupMax"]?.toDoubleOrNull(),
                        peakMin = props["peakMin"]?.toDoubleOrNull(),
                        peakMax = props["peakMax"]?.toDoubleOrNull(),
                        offsetMin = props["offsetMin"]?.toDoubleOrNull(),
                        offsetMax = props["offsetMax"]?.toDoubleOrNull(),
                        totalMin = props["totalMin"]?.toDoubleOrNull(),
                        totalMax = props["totalMax"]?.toDoubleOrNull(),
                        bioavailabilityMin = props["bioavailabilityMin"]?.toDoubleOrNull(),
                        bioavailabilityMax = props["bioavailabilityMax"]?.toDoubleOrNull(),
                    )
                )
            }
        } catch (e: Exception) {
            // parsing failed, return empty
        }
        return roas
    }

    fun toRoaDose(customRoa: CustomRoa): RoaDose? {
        if (customRoa.doseUnits.isBlank()) return null
        return RoaDose(
            units = customRoa.doseUnits,
            lightMin = customRoa.lightMin,
            commonMin = customRoa.commonMin,
            strongMin = customRoa.strongMin,
            heavyMin = customRoa.heavyMin,
        )
    }

    fun toRoaDuration(customRoa: CustomRoa): RoaDuration? {
        val hasAnyDuration = customRoa.onsetMin != null || customRoa.comeupMin != null || 
            customRoa.peakMin != null || customRoa.offsetMin != null || customRoa.totalMin != null
        if (!hasAnyDuration) return null
        return RoaDuration(
            onset = if (customRoa.onsetMin != null || customRoa.onsetMax != null) 
                DurationRange(customRoa.onsetMin, customRoa.onsetMax) else null,
            comeup = if (customRoa.comeupMin != null || customRoa.comeupMax != null) 
                DurationRange(customRoa.comeupMin, customRoa.comeupMax) else null,
            peak = if (customRoa.peakMin != null || customRoa.peakMax != null) 
                DurationRange(customRoa.peakMin, customRoa.peakMax) else null,
            offset = if (customRoa.offsetMin != null || customRoa.offsetMax != null) 
                DurationRange(customRoa.offsetMin, customRoa.offsetMax) else null,
            total = if (customRoa.totalMin != null || customRoa.totalMax != null) 
                DurationRange(customRoa.totalMin, customRoa.totalMax) else null,
            afterglow = null,
        )
    }

    fun toBioavailability(customRoa: CustomRoa): Bioavailability? {
        if (customRoa.bioavailabilityMin == null && customRoa.bioavailabilityMax == null) return null
        return Bioavailability(
            min = customRoa.bioavailabilityMin,
            max = customRoa.bioavailabilityMax
        )
    }
}