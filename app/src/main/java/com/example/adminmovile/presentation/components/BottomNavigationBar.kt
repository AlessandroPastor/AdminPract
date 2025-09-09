package com.example.adminmovile.presentation.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DriveFileMove
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.adminmovile.presentation.navigation.Routes

@Composable
fun BottomNavigationBar(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    isVisible: Boolean = true
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val navItems = listOf(
        BottomNavItem(
            icon = Icons.Outlined.Home,
            selectedIcon = Icons.Filled.SpaceDashboard,
            label = "Inicio",
            route = Routes.LAND_PAGE
        ),
        BottomNavItem(
            icon = Icons.Outlined.GraphicEq,
            selectedIcon = Icons.Filled.AutoGraph,
            label = "Graficos",
            route = Routes.PROGRESS
        ),
        BottomNavItem(
            icon = Icons.Outlined.DriveFileMoveRtl,
            selectedIcon = Icons.AutoMirrored.Filled.DriveFileMove,
            label = "Practicas",
            route = Routes.PRACTICE
        ),
        BottomNavItem(
            icon = Icons.Outlined.FavoriteBorder,
            selectedIcon = Icons.Filled.Favorite,
            label = "Favoritos",
            route = Routes.RECOMMENDATIONS
        ),
    )

    val surfaceColor = MaterialTheme.colorScheme.surface
    val borderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.12f)

    val animatedElevation by animateFloatAsState(
        targetValue = if (isVisible) 12f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "elevation"
    )

    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(
            initialOffsetY = { it },
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMedium
            )
        ) + fadeIn(
            animationSpec = tween(400, easing = FastOutSlowInEasing)
        ),
        exit = slideOutVertically(
            targetOffsetY = { it },
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioLowBouncy,
                stiffness = Spring.StiffnessHigh
            )
        ) + fadeOut(
            animationSpec = tween(300, easing = FastOutLinearInEasing)
        )
    ) {
        Surface(
            modifier = modifier
                .fillMaxWidth()
                .navigationBarsPadding(),
            color = surfaceColor,
            shadowElevation = animatedElevation.dp,
            tonalElevation = 6.dp,
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
            border = BorderStroke(
                width = 0.8.dp,
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        borderColor,
                        borderColor.copy(alpha = 0.3f),
                        borderColor
                    )
                )
            )
        ) {
            Column(
                modifier = Modifier
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                surfaceColor,
                                surfaceColor.copy(alpha = 0.98f)
                            )
                        )
                    )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp, bottom = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .width(40.dp)
                            .height(4.dp)
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f),
                                        Color.Transparent
                                    )
                                ),
                                shape = RoundedCornerShape(2.dp)
                            )
                    )
                }

                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    items(navItems) { item ->
                        val isSelected = currentRoute == item.route
                        BottomNavItemComponent(
                            item = item,
                            isSelected = isSelected,
                            onClick = {
                                // Navegación segura entre tabs
                                navController.navigate(item.route) {
                                    launchSingleTop = true
                                    restoreState = true
                                    popUpTo(Routes.LAND_PAGE) { saveState = true }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun BottomNavItemComponent(
    item: BottomNavItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val iconColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
        animationSpec = tween(300, easing = FastOutSlowInEasing),
        label = "iconColor"
    )

    val textColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
        animationSpec = tween(300, easing = FastOutSlowInEasing),
        label = "textColor"
    )

    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.05f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "scale"
    )

    val backgroundAlpha by animateFloatAsState(
        targetValue = if (isSelected) 0.12f else 0f,
        animationSpec = tween(400, easing = FastOutSlowInEasing),
        label = "backgroundAlpha"
    )

    val indicatorScale by animateFloatAsState(
        targetValue = if (isSelected) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessHigh
        ),
        label = "indicatorScale"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .width(65.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(
                color = MaterialTheme.colorScheme.primary.copy(alpha = backgroundAlpha),
                shape = RoundedCornerShape(18.dp)
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(
                    bounded = true,
                    radius = 26.dp,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                )
            ) { onClick() }
            .padding(vertical = 10.dp, horizontal = 6.dp)
            .graphicsLayer { scaleX = scale; scaleY = scale }
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(28.dp)) {
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .scale(indicatorScale)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.08f),
                                    Color.Transparent
                                ),
                                radius = 24f
                            ),
                            shape = CircleShape
                        )
                )
            }

            Icon(
                imageVector = if (isSelected) item.selectedIcon else item.icon,
                contentDescription = item.label,
                tint = iconColor,
                modifier = Modifier.size(22.dp)
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        AnimatedContent(
            targetState = isSelected,
            transitionSpec = { fadeIn(animationSpec = tween(200)) togetherWith fadeOut(animationSpec = tween(200)) },
            label = "textAnimation"
        ) { selected ->
            Text(
                text = item.label,
                color = textColor,
                fontSize = if (selected) 12.sp else 11.sp,
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
                style = MaterialTheme.typography.labelSmall.copy(
                    letterSpacing = if (selected) 0.3.sp else 0.2.sp
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 2.dp)
            )
        }

        AnimatedVisibility(
            visible = isSelected,
            enter = scaleIn(spring(dampingRatio = Spring.DampingRatioMediumBouncy)) + fadeIn(),
            exit = scaleOut() + fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .padding(top = 2.dp)
                    .size(4.dp)
                    .background(color = MaterialTheme.colorScheme.primary, shape = CircleShape)
            )
        }
    }
}

data class BottomNavItem(
    val icon: ImageVector,
    val selectedIcon: ImageVector,
    val label: String,
    val route: String
)
