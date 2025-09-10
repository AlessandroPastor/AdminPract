package com.example.adminmovile.presentation.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

@Composable
fun AdminSchoolBackground(
    modifier: Modifier = Modifier,
) {
    val colorScheme = MaterialTheme.colorScheme
    Canvas(modifier = modifier.fillMaxSize()) {
        // Fondo degradado profesional usando AppTheme
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(
                    colorScheme.primary.copy(alpha = 0.1f),
                    colorScheme.primaryContainer.copy(alpha = 0.2f),
                    colorScheme.secondary.copy(alpha = 0.15f),
                    colorScheme.background
                ),
                startY = 0f,
                endY = size.height
            )
        )
    }
}

private data class FloatingElement(
    val x: Float,
    val y: Float,
    val size: Float,
    val speed: Float,
    val alpha: Float,
    val type: ElementType,
    val rotationSpeed: Float
)

private enum class ElementType {
    DOCUMENT, GRAPH, GEAR, LIGHTBULB, BRIEFCASE
}

private fun generateFloatingElements(count: Int): List<FloatingElement> {
    return List(count) {
        FloatingElement(
            x = Random.nextFloat(),
            y = Random.nextFloat(),
            size = 8f + Random.nextFloat() * 20f,
            speed = 0.05f + Random.nextFloat() * 0.15f,
            alpha = 0.1f + Random.nextFloat() * 0.3f,
            type = ElementType.values()[Random.nextInt(ElementType.values().size)],
            rotationSpeed = 0.5f + Random.nextFloat() * 1.5f
        )
    }
}

private fun DrawScope.drawModernCampus(
    colorScheme: androidx.compose.material3.ColorScheme,
    pulse: Float,
    canvasSize: Size
) {
    // Edificio principal de la escuela
    val mainBuildingWidth = canvasSize.width * 0.25f
    val mainBuildingHeight = canvasSize.height * 0.4f
    val mainBuildingX = canvasSize.width * 0.15f
    val mainBuildingY = canvasSize.height * 0.7f - mainBuildingHeight

    drawRoundRect(
        brush = Brush.verticalGradient(
            colors = listOf(
                colorScheme.primaryContainer.copy(alpha = 0.8f),
                colorScheme.primary.copy(alpha = 0.6f)
            ),
            startY = mainBuildingY,
            endY = mainBuildingY + mainBuildingHeight
        ),
        topLeft = Offset(mainBuildingX, mainBuildingY),
        size = Size(mainBuildingWidth * pulse, mainBuildingHeight),
        cornerRadius = CornerRadius(12f, 12f)
    )

    // Ventanas del edificio principal
    repeat(4) { floor ->
        repeat(3) { window ->
            val windowX = mainBuildingX + (window + 1) * mainBuildingWidth * pulse / 4f - 15f
            val windowY = mainBuildingY + (floor + 1) * mainBuildingHeight / 5f - 10f

            drawRoundRect(
                color = colorScheme.onPrimary.copy(alpha = 0.9f),
                topLeft = Offset(windowX, windowY),
                size = Size(30f, 20f),
                cornerRadius = CornerRadius(4f, 4f)
            )

            // Luz en algunas ventanas
            if (Random.nextFloat() > 0.6f) {
                drawRoundRect(
                    color = colorScheme.tertiary.copy(alpha = 0.7f),
                    topLeft = Offset(windowX, windowY),
                    size = Size(30f, 20f),
                    cornerRadius = CornerRadius(4f, 4f)
                )
            }
        }
    }

    // Edificio secundario
    val secBuildingWidth = canvasSize.width * 0.2f
    val secBuildingHeight = canvasSize.height * 0.3f
    val secBuildingX = canvasSize.width * 0.7f
    val secBuildingY = canvasSize.height * 0.7f - secBuildingHeight

    drawRoundRect(
        brush = Brush.verticalGradient(
            colors = listOf(
                colorScheme.secondaryContainer.copy(alpha = 0.7f),
                colorScheme.secondary.copy(alpha = 0.5f)
            ),
            startY = secBuildingY,
            endY = secBuildingY + secBuildingHeight
        ),
        topLeft = Offset(secBuildingX, secBuildingY),
        size = Size(secBuildingWidth, secBuildingHeight * pulse),
        cornerRadius = CornerRadius(8f, 8f)
    )

    // Entrada principal con columnas
    val entranceWidth = mainBuildingWidth * 0.6f
    val entranceHeight = 40f
    val entranceX = mainBuildingX + (mainBuildingWidth * pulse - entranceWidth) / 2f
    val entranceY = canvasSize.height * 0.7f - entranceHeight

    drawRoundRect(
        color = colorScheme.surface.copy(alpha = 0.9f),
        topLeft = Offset(entranceX, entranceY),
        size = Size(entranceWidth, entranceHeight),
        cornerRadius = CornerRadius(6f, 6f)
    )

    // Columnas de la entrada
    repeat(3) { i ->
        val columnX = entranceX + (i + 0.5f) * entranceWidth / 3f - 3f
        drawRoundRect(
            color = colorScheme.onSurface.copy(alpha = 0.8f),
            topLeft = Offset(columnX, entranceY - 20f),
            size = Size(6f, 60f),
            cornerRadius = CornerRadius(3f, 3f)
        )
    }
}

private fun DrawScope.drawFloatingCharts(
    colorScheme: androidx.compose.material3.ColorScheme,
    animation: Float,
    canvasSize: Size
) {
    // Gráfico de barras flotante
    val chartX = canvasSize.width * 0.6f
    val chartY = canvasSize.height * 0.3f
    val barWidth = 12f
    val maxBarHeight = 80f * animation

    repeat(5) { i ->
        val barHeight = maxBarHeight * (0.3f + Random.nextFloat() * 0.7f)
        val barX = chartX + i * (barWidth + 8f)

        drawRoundRect(
            brush = Brush.verticalGradient(
                colors = listOf(
                    colorScheme.tertiary.copy(alpha = 0.8f),
                    colorScheme.tertiaryContainer.copy(alpha = 0.6f)
                ),
                startY = chartY - barHeight,
                endY = chartY
            ),
            topLeft = Offset(barX, chartY - barHeight),
            size = Size(barWidth, barHeight),
            cornerRadius = CornerRadius(4f, 4f)
        )
    }

    // Gráfico circular
    val pieChartCenter = Offset(canvasSize.width * 0.8f, canvasSize.height * 0.5f)
    val pieRadius = 40f

    val segments = listOf(0.35f, 0.25f, 0.4f)
    var currentAngle = 0f

    segments.forEachIndexed { index, segment ->
        val sweepAngle = 360f * segment * animation
        val colors = listOf(
            colorScheme.primary.copy(alpha = 0.7f),
            colorScheme.secondary.copy(alpha = 0.7f),
            colorScheme.tertiary.copy(alpha = 0.7f)
        )

        drawArc(
            color = colors[index],
            startAngle = currentAngle,
            sweepAngle = sweepAngle,
            useCenter = true,
            topLeft = Offset(pieChartCenter.x - pieRadius, pieChartCenter.y - pieRadius),
            size = Size(pieRadius * 2, pieRadius * 2)
        )
        currentAngle += sweepAngle
    }
}

private fun DrawScope.drawAcademicElements(
    colorScheme: androidx.compose.material3.ColorScheme,
    canvasSize: Size
) {
    // Diploma/Certificado
    val diplomaX = canvasSize.width * 0.05f
    val diplomaY = canvasSize.height * 0.2f

    drawRoundRect(
        color = colorScheme.surfaceVariant.copy(alpha = 0.8f),
        topLeft = Offset(diplomaX, diplomaY),
        size = Size(120f, 80f),
        cornerRadius = CornerRadius(8f, 8f)
    )

    // Marco del diploma
    drawRoundRect(
        color = colorScheme.outline.copy(alpha = 0.6f),
        topLeft = Offset(diplomaX + 5f, diplomaY + 5f),
        size = Size(110f, 70f),
        cornerRadius = CornerRadius(6f, 6f),
        style = Stroke(width = 2f)
    )

    // Libro abierto
    val bookX = canvasSize.width * 0.1f
    val bookY = canvasSize.height * 0.5f

    // Páginas del libro
    drawRoundRect(
        color = colorScheme.surface.copy(alpha = 0.9f),
        topLeft = Offset(bookX, bookY),
        size = Size(80f, 60f),
        cornerRadius = CornerRadius(4f, 4f)
    )

    // Líneas del texto
    repeat(4) { line ->
        drawLine(
            color = colorScheme.onSurface.copy(alpha = 0.3f),
            start = Offset(bookX + 10f, bookY + 15f + line * 10f),
            end = Offset(bookX + 70f, bookY + 15f + line * 10f),
            strokeWidth = 1f
        )
    }
}

private fun DrawScope.drawDataWaves(
    colorScheme: androidx.compose.material3.ColorScheme,
    offset: Float,
    canvasSize: Size
) {
    repeat(3) { waveIndex ->
        val waveY = canvasSize.height * (0.8f + waveIndex * 0.05f)
        val amplitude = 15f - waveIndex * 3f
        val alpha = 0.3f - waveIndex * 0.08f

        val path = Path()
        path.moveTo(-offset + waveIndex * 30f, waveY)

        for (x in 0..canvasSize.width.toInt() step 20) {
            val actualX = x - offset + waveIndex * 30f
            val waveY2 = waveY + amplitude * sin((actualX / 50f) * PI).toFloat()
            path.lineTo(actualX, waveY2)
        }

        drawPath(
            path = path,
            color = colorScheme.primary.copy(alpha = alpha),
            style = Stroke(width = 2f, cap = StrokeCap.Round)
        )
    }
}

private fun DrawScope.drawKnowledgeParticles(
    elements: List<FloatingElement>,
    animation: Float,
    colorScheme: androidx.compose.material3.ColorScheme,
    canvasSize: Size
) {
    elements.forEach { element ->
        val currentY = (element.y - animation * element.speed) % 1.1f
        if (currentY > 0f && currentY < 1f) {
            val xPos = canvasSize.width * element.x +
                    sin(animation * PI.toFloat() * element.rotationSpeed) * 20f
            val yPos = canvasSize.height * currentY
            val rotation = animation * element.rotationSpeed * 360f

            withTransform({
                rotate(rotation, Offset(xPos, yPos))
            }) {
                when (element.type) {
                    ElementType.DOCUMENT -> drawDocument(
                        center = Offset(xPos, yPos),
                        size = element.size,
                        color = colorScheme.primary.copy(alpha = element.alpha)
                    )
                    ElementType.GRAPH -> drawMiniGraph(
                        center = Offset(xPos, yPos),
                        size = element.size,
                        color = colorScheme.secondary.copy(alpha = element.alpha)
                    )
                    ElementType.GEAR -> drawGear(
                        center = Offset(xPos, yPos),
                        size = element.size,
                        color = colorScheme.tertiary.copy(alpha = element.alpha)
                    )
                    ElementType.LIGHTBULB -> drawLightbulb(
                        center = Offset(xPos, yPos),
                        size = element.size,
                        color = colorScheme.secondary.copy(alpha = element.alpha)
                    )
                    ElementType.BRIEFCASE -> drawBriefcase(
                        center = Offset(xPos, yPos),
                        size = element.size,
                        color = colorScheme.primary.copy(alpha = element.alpha)
                    )
                }
            }
        }
    }
}

private fun DrawScope.drawDocument(center: Offset, size: Float, color: Color) {
    drawRoundRect(
        color = color,
        topLeft = Offset(center.x - size/2, center.y - size/2),
        size = Size(size * 0.7f, size),
        cornerRadius = CornerRadius(2f, 2f)
    )

    // Líneas del documento
    repeat(3) { line ->
        drawLine(
            color = color.copy(alpha = color.alpha * 0.7f),
            start = Offset(center.x - size/3, center.y - size/3 + line * size/6),
            end = Offset(center.x + size/4, center.y - size/3 + line * size/6),
            strokeWidth = 1f
        )
    }
}

private fun DrawScope.drawMiniGraph(center: Offset, size: Float, color: Color) {
    // Base del gráfico
    drawRect(
        color = color.copy(alpha = 0.3f),
        topLeft = Offset(center.x - size/2, center.y - size/2),
        size = Size(size, size)
    )

    // Barras pequeñas
    repeat(3) { bar ->
        val barHeight = size * (0.2f + bar * 0.2f)
        drawRect(
            color = color,
            topLeft = Offset(center.x - size/2 + bar * size/3 + size/6, center.y + size/2 - barHeight),
            size = Size(size/6, barHeight)
        )
    }
}

private fun DrawScope.drawGear(center: Offset, size: Float, color: Color) {
    drawCircle(
        color = color,
        radius = size/2,
        center = center,
        style = Stroke(width = size/8)
    )

    // Dientes del engranaje
    repeat(8) { tooth ->
        val angle = tooth * 45f
        val startRadius = size/2
        val endRadius = size/1.5f

        drawLine(
            color = color,
            start = Offset(
                center.x + startRadius * cos(angle * PI / 180).toFloat(),
                center.y + startRadius * sin(angle * PI / 180).toFloat()
            ),
            end = Offset(
                center.x + endRadius * cos(angle * PI / 180).toFloat(),
                center.y + endRadius * sin(angle * PI / 180).toFloat()
            ),
            strokeWidth = size/10
        )
    }

    // Centro del engranaje
    drawCircle(
        color = color,
        radius = size/6,
        center = center
    )
}

private fun DrawScope.drawLightbulb(center: Offset, size: Float, color: Color) {
    // Bulbo
    drawCircle(
        color = color,
        radius = size/2.5f,
        center = Offset(center.x, center.y - size/6)
    )

    // Base
    drawRect(
        color = color.copy(alpha = 0.8f),
        topLeft = Offset(center.x - size/4, center.y + size/6),
        size = Size(size/2, size/4)
    )

    // Rayos de luz
    repeat(4) { ray ->
        val angle = ray * 90f
        drawLine(
            color = color.copy(alpha = 0.6f),
            start = Offset(
                center.x + (size/2) * cos(angle * PI / 180).toFloat(),
                center.y - size/6 + (size/2) * sin(angle * PI / 180).toFloat()
            ),
            end = Offset(
                center.x + (size/1.3f) * cos(angle * PI / 180).toFloat(),
                center.y - size/6 + (size/1.3f) * sin(angle * PI / 180).toFloat()
            ),
            strokeWidth = 2f
        )
    }
}

private fun DrawScope.drawBriefcase(center: Offset, size: Float, color: Color) {
    // Cuerpo del maletín
    drawRoundRect(
        color = color,
        topLeft = Offset(center.x - size/2, center.y - size/3),
        size = Size(size, size * 2/3),
        cornerRadius = CornerRadius(size/10, size/10)
    )

    // Asa
    drawArc(
        color = color,
        startAngle = 180f,
        sweepAngle = 180f,
        useCenter = false,
        topLeft = Offset(center.x - size/4, center.y - size/2),
        size = Size(size/2, size/3),
        style = Stroke(width = size/12)
    )

    // Cerradura
    drawCircle(
        color = color.copy(alpha = 0.8f),
        radius = size/12,
        center = Offset(center.x, center.y)
    )
}

private fun DrawScope.drawAdminIcons(
    colorScheme: androidx.compose.material3.ColorScheme,
    canvasSize: Size,
    offset: Float
) {
    // Calculator flotante
    val calcX = canvasSize.width * 0.9f
    val calcY = canvasSize.height * 0.3f + sin(offset / 50f) * 10f

    drawRoundRect(
        color = colorScheme.surfaceVariant.copy(alpha = 0.6f),
        topLeft = Offset(calcX - 25f, calcY - 30f),
        size = Size(50f, 60f),
        cornerRadius = CornerRadius(6f, 6f)
    )

    // Pantalla de la calculadora
    drawRoundRect(
        color = colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
        topLeft = Offset(calcX - 20f, calcY - 25f),
        size = Size(40f, 15f),
        cornerRadius = CornerRadius(2f, 2f)
    )

    // Botones
    repeat(3) { row ->
        repeat(3) { col ->
            drawCircle(
                color = colorScheme.outline.copy(alpha = 0.4f),
                radius = 3f,
                center = Offset(calcX - 15f + col * 10f, calcY - 5f + row * 8f)
            )
        }
    }
}