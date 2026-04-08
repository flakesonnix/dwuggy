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

package com.isaakhanimann.journal.ui.tabs.journal.addingestion.time

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.isaakhanimann.journal.data.room.experiences.entities.AdaptiveColor

@Preview
@Composable
fun ColorPickerPreview() {
    val alreadyUsedColors = listOf(AdaptiveColor.BLUE, AdaptiveColor.PINK)
    val otherColors = AdaptiveColor.entries.filter { color ->
        !alreadyUsedColors.contains(color)
    }
    ColorPicker(
        selectedColor = AdaptiveColor.BLUE,
        onChangeOfColor = {},
        alreadyUsedColors,
        otherColors
    )
}

@Composable
fun ColorPicker(
    selectedColor: AdaptiveColor,
    onChangeOfColor: (AdaptiveColor) -> Unit,
    alreadyUsedColors: List<AdaptiveColor>,
    otherColors: List<AdaptiveColor>,
    customRed: Int? = null,
    customGreen: Int? = null,
    customBlue: Int? = null,
    onCustomColorPicked: ((red: Int, green: Int, blue: Int) -> Unit)? = null
) {
    val isDarkTheme = isSystemInDarkTheme()
    var isColorPickerVisible by remember {
        mutableStateOf(false)
    }
    val displayColor = if (selectedColor == AdaptiveColor.CUSTOM && customRed != null && customGreen != null && customBlue != null) {
        Color(red = customRed, green = customGreen, blue = customBlue)
    } else {
        selectedColor.getComposeColor(isDarkTheme)
    }
    val shape = RoundedCornerShape(8.dp)
    Surface(
        shape = shape,
        color = displayColor,
        modifier = Modifier
            .size(50.dp)
            .clip(shape)
            .clickable(onClick = { isColorPickerVisible = true })
    ) {
        Icon(
            imageVector = Icons.Default.Edit,
            contentDescription = "Edit color",
            modifier = Modifier.padding(15.dp),
        )
    }
    if (isColorPickerVisible) {
        ColorDialog(
            dismiss = { isColorPickerVisible = false },
            onChangeOfColor = onChangeOfColor,
            alreadyUsedColors = alreadyUsedColors,
            otherColors = otherColors,
            customRed = customRed,
            customGreen = customGreen,
            customBlue = customBlue,
            onCustomColorPicked = onCustomColorPicked
        )
    }
}

@Preview
@Composable
fun ColorDialogPreview() {
    val alreadyUsedColors = listOf(AdaptiveColor.BLUE, AdaptiveColor.PINK, AdaptiveColor.AUBURN)
    val otherColors = AdaptiveColor.entries.filter { color ->
        !alreadyUsedColors.contains(color)
    }
    ColorDialog(
        dismiss = {},
        onChangeOfColor = {},
        alreadyUsedColors = alreadyUsedColors,
        otherColors = otherColors
    )
}

@Composable
fun ColorDialog(
    dismiss: () -> Unit,
    onChangeOfColor: (AdaptiveColor) -> Unit,
    alreadyUsedColors: List<AdaptiveColor>,
    otherColors: List<AdaptiveColor>,
    customRed: Int? = null,
    customGreen: Int? = null,
    customBlue: Int? = null,
    onCustomColorPicked: ((red: Int, green: Int, blue: Int) -> Unit)? = null
) {
    var showCustomColorPicker by remember { mutableStateOf(false) }
    val initialColor = if (customRed != null && customGreen != null && customBlue != null) {
        Color(red = customRed, green = customGreen, blue = customBlue)
    } else {
        Color.Red
    }

    if (showCustomColorPicker && onCustomColorPicked != null) {
        CustomColorPickerDialog(
            dismiss = { showCustomColorPicker = false },
            initialColor = initialColor,
            onColorPicked = { color ->
                val red = (color.red * 255).toInt()
                val green = (color.green * 255).toInt()
                val blue = (color.blue * 255).toInt()
                onCustomColorPicked(red, green, blue)
                onChangeOfColor(AdaptiveColor.CUSTOM)
                dismiss()
            }
        )
    } else {
        AlertDialog(
            onDismissRequest = dismiss,
            title = {
                Text(text = "Pick a color", style = MaterialTheme.typography.titleLarge)
            },
            text = {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState())
                ) {
                    if (otherColors.isEmpty()) {
                        Text(text = "No unused colors")
                    } else {
                        Text(text = "Not yet used")
                        Spacer(modifier = Modifier.height(2.dp))
                        CircleColorButtons(colors = otherColors) {
                            onChangeOfColor(it)
                            dismiss()
                        }
                    }
                    if (alreadyUsedColors.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(text = "Already used")
                        Spacer(modifier = Modifier.height(2.dp))
                        CircleColorButtons(colors = alreadyUsedColors) {
                            onChangeOfColor(it)
                            dismiss()
                        }
                    }
                    if (onCustomColorPicked != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = { showCustomColorPicker = true },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Custom Color")
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = dismiss
                ) {
                    Text("Cancel", textAlign = TextAlign.Center)
                }
            },
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CircleColorButtons(
    colors: List<AdaptiveColor>,
    onTapOnColor: (AdaptiveColor) -> Unit
) {
    val isDarkTheme = isSystemInDarkTheme()
    val spacing = 5.dp
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(spacing),
        verticalArrangement = Arrangement.spacedBy(spacing)
    ) {
        val shape = RoundedCornerShape(8.dp)
        colors.forEach { color ->
            Surface(
                shape = shape,
                color = color.getComposeColor(isDarkTheme),
                modifier = Modifier
                    .size(48.dp)
                    .clip(shape)
                    .clickable(
                        onClick = { onTapOnColor(color) }
                    )
                    .semantics { contentDescription = color.name }
            ) {}
        }
    }
}

@Composable
fun CustomColorPickerDialog(
    dismiss: () -> Unit,
    initialColor: Color,
    onColorPicked: (Color) -> Unit
) {
    AlertDialog(
        onDismissRequest = dismiss,
        title = { Text("Custom Color") },
        text = {
            CustomColorPicker(
                initialColor = initialColor,
                onColorChanged = onColorPicked
            )
        },
        confirmButton = {
            TextButton(onClick = dismiss) {
                Text("Cancel")
            }
        }
    )
}