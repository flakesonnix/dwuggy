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

package com.isaakhanimann.journal.ui.tabs.journal.addingestion.time

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp

data class ColorPickerState(
    val hue: Float = 0f,
    val saturation: Float = 1f,
    val value: Float = 1f
) {
    fun toColor(): Color {
        return Color.hsv(hue, saturation, value)
    }

    companion object {
        fun fromColor(color: Color): ColorPickerState {
            val hsv = FloatArray(3)
            android.graphics.Color.colorToHSV(
                android.graphics.Color.rgb(
                    (color.red * 255).toInt(),
                    (color.green * 255).toInt(),
                    (color.blue * 255).toInt()
                ),
                hsv
            )
            return ColorPickerState(hsv[0], hsv[1], hsv[2])
        }
    }
}

@Composable
fun CustomColorPicker(
    modifier: Modifier = Modifier,
    initialColor: Color = Color.Red,
    onColorChanged: (Color) -> Unit
) {
    val colorPickerState = remember(initialColor) {
        ColorPickerState.fromColor(initialColor)
    }
    var hue by remember { mutableFloatStateOf(colorPickerState.hue) }
    var saturation by remember { mutableFloatStateOf(colorPickerState.saturation) }
    var value by remember { mutableFloatStateOf(colorPickerState.value) }

    Column(modifier = modifier.fillMaxWidth()) {
        SaturationValueSelector(
            hue = hue,
            saturation = saturation,
            value = value,
            onSaturationChange = { newSaturation ->
                saturation = newSaturation
                onColorChanged(Color.hsv(hue, newSaturation, value))
            },
            onValueChange = { newValue ->
                value = newValue
                onColorChanged(Color.hsv(hue, saturation, newValue))
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        HueSelector(
            hue = hue,
            onHueChange = { newHue ->
                hue = newHue
                onColorChanged(Color.hsv(newHue, saturation, value))
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        ColorPreview(color = Color.hsv(hue, saturation, value))
    }
}

@Composable
private fun SaturationValueSelector(
    hue: Float,
    saturation: Float,
    value: Float,
    onSaturationChange: (Float) -> Unit,
    onValueChange: (Float) -> Unit
) {
    val backgroundColor = Color.hsv(hue, 1f, 1f)

    Column {
        Text("Saturation", style = MaterialTheme.typography.labelMedium)
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp)
                .pointerInput(Unit) {
                    detectDragGestures { change, _ ->
                        change.consume()
                        val newSaturation = (change.position.x / size.width).coerceIn(0f, 1f)
                        onSaturationChange(newSaturation)
                    }
                }
        ) {
            drawRect(backgroundColor)
            drawRect(
                color = Color.White,
                topLeft = Offset(0f, 0f),
                size = androidx.compose.ui.geometry.Size(size.width * (1 - saturation), size.height)
            )
            drawRect(
                color = Color.Black,
                topLeft = Offset(size.width * saturation, 0f),
                size = androidx.compose.ui.geometry.Size(size.width * (1 - saturation), size.height)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text("Brightness", style = MaterialTheme.typography.labelMedium)
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp)
                .pointerInput(Unit) {
                    detectDragGestures { change, _ ->
                        change.consume()
                        val newValue = (change.position.x / size.width).coerceIn(0f, 1f)
                        onValueChange(newValue)
                    }
                }
        ) {
            val baseColor = Color.hsv(hue, saturation, 1f)
            drawRect(baseColor)
            drawRect(
                color = Color.Black.copy(alpha = 1f - value),
                topLeft = Offset(0f, 0f),
                size = androidx.compose.ui.geometry.Size(size.width, size.height)
            )
        }
    }
}

@Composable
private fun HueSelector(
    hue: Float,
    onHueChange: (Float) -> Unit
) {
    Column {
        Text("Hue", style = MaterialTheme.typography.labelMedium)
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp)
                .pointerInput(Unit) {
                    detectDragGestures { change, _ ->
                        change.consume()
                        val newHue = (change.position.x / size.width) * 360f
                        onHueChange(newHue.coerceIn(0f, 360f))
                    }
                }
        ) {
            drawHueGradient()
        }
        Slider(
            value = hue,
            onValueChange = onHueChange,
            valueRange = 0f..360f,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

private fun DrawScope.drawHueGradient() {
    val width = size.width
    for (i in 0 until width.toInt()) {
        val hue = (i / width) * 360f
        drawLine(
            color = Color.hsv(hue, 1f, 1f),
            start = Offset(i.toFloat(), 0f),
            end = Offset(i.toFloat(), size.height),
            strokeWidth = 1f
        )
    }
}

@Composable
private fun ColorPreview(color: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Preview:", style = MaterialTheme.typography.labelMedium)
        Spacer(modifier = Modifier.width(8.dp))
        Canvas(
            modifier = Modifier
                .width(60.dp)
                .height(30.dp)
        ) {
            drawRect(color)
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "RGB: (${(color.red * 255).toInt()}, ${(color.green * 255).toInt()}, ${(color.blue * 255).toInt()})",
            style = MaterialTheme.typography.bodySmall
        )
    }
}