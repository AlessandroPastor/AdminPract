package com.example.adminmovile.presentation.screens.configuration.ad.modulos_padres

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.adminmovile.data.remote.dto.configuracion.ParentModule
import com.example.adminmovile.presentation.components.AppPaginationControls
import com.example.adminmovile.presentation.components.NotificationHost
import com.example.adminmovile.presentation.components.StatisticCard
import com.example.adminmovile.presentation.components.rememberNotificationState
import com.example.adminmovile.presentation.components.showNotification
import org.koin.androidx.compose.koinViewModel


@Composable
fun ParentModuleScreen(
    navController: NavHostController,
    paddingValues: PaddingValues,
    viewModel: ParentModuleViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val notificationState = rememberNotificationState()
    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(state.notification) {
        if (state.notification.isVisible) {
            notificationState.showNotification(
                message = state.notification.message,
                type = state.notification.type,
                duration = state.notification.duration
            )
        }
    }

    NotificationHost(state = notificationState) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                // Estadísticas
                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    StatisticCard(
                        title = "Total Módulos",
                        value = state.totalElements.toString(),
                        icon = Icons.Default.Apps,
                        modifier = Modifier.weight(1f)
                    )
                    StatisticCard(
                        title = "Activos",
                        value = state.items.count { it.status == 1 }.toString(),
                        icon = Icons.Default.CheckCircle,
                        modifier = Modifier.weight(1f)
                    )
                    StatisticCard(
                        title = "Inactivos",
                        value = state.items.count { it.status == 1 }.toString(),
                        icon = Icons.Default.Cancel,
                        modifier = Modifier.weight(1f)
                    )
                }

                // Barra de herramientas
                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        value = searchQuery,
                        onValueChange = {
                            searchQuery = it
                            viewModel.loadParentModules(searchQuery = it.ifEmpty { null })
                        },
                        placeholder = { Text("Buscar ...") },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )

                    Button(
                        onClick = {
                            viewModel.setSelectedParentModule(
                                ParentModule(
                                    id = 0, // 👈 0 = nuevo
                                    title = "",
                                    code = "",
                                    subtitle = "",
                                    type = "",
                                    icon = "",
                                    status = 1,
                                    moduleOrder = 0,
                                    link = "",
                                    createdAt = null,
                                    updatedAt = null,
                                    deletedAt = null
                                )
                            )
                        },
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Nuevo")
                        Spacer(Modifier.width(8.dp))
                        Text("Nuevo Módulo")
                    }

                }

                // Lista de módulos
                Surface(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium,
                    shadowElevation = 1.dp
                ) {
                    LazyColumn {
                        items(state.items) { parentModule ->
                            ParentModuleRow(
                                parentModule = parentModule,
                                onEdit = { viewModel.setSelectedParentModule(parentModule) },
                                onDelete = { viewModel.deleteParentModule(parentModule.id) }
                            )
                        }
                    }
                }

                if (state.totalPages > 1) {
                    Spacer(modifier = Modifier.height(16.dp))
                    AppPaginationControls(
                        currentPage = state.currentPage,
                        totalPages = state.totalPages,
                        onPreviousPage = {
                            viewModel.loadParentModules(state.currentPage - 1, searchQuery)
                        },
                        onNextPage = {
                            viewModel.loadParentModules(state.currentPage + 1, searchQuery)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    )
                }
            }
        }

        // Diálogo para crear/editar módulo
        if (state.isDialogOpen) {
            ParentModuleDialog(
                parentModule = state.selectedItem,
                onDismiss = { viewModel.closeDialog() },
                onSave = { module ->
                    if (module.id == 0) {
                        viewModel.createParentModule(module)
                    } else {
                        viewModel.updateParentModule(module)
                    }
                }
            )
        }
    }
}

@Composable
fun ParentModuleRow(
    parentModule: ParentModule,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(2f)) {
                Text(
                    text = parentModule.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = parentModule.subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Text(
                text = parentModule.code,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(1f)
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                parentModule.status?.let { StatusBadge(status = it) }
            }

            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = onEdit) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Editar",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Eliminar",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@Composable
fun StatusBadge(status: Int) {
    val isActive = status == 1
    Surface(
        color = if (isActive) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.errorContainer,
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = if (isActive) "Activo" else "Inactivo",
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.bodySmall,
            color = if (isActive) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onErrorContainer
        )
    }
}

@Composable
fun ParentModuleDialog(
    parentModule: ParentModule?,
    onDismiss: () -> Unit,
    onSave: (ParentModule) -> Unit
) {
    var title by remember { mutableStateOf(parentModule?.title ?: "") }
    var code by remember { mutableStateOf(parentModule?.code ?: "") }
    var subtitle by remember { mutableStateOf(parentModule?.subtitle ?: "") }
    var status by remember { mutableStateOf(parentModule?.status) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(if (parentModule?.id == 0) "Nuevo Módulo" else "Editar Módulo")
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Título") })
                OutlinedTextField(value = code, onValueChange = { code = it }, label = { Text("Código") })
                OutlinedTextField(value = subtitle, onValueChange = { subtitle = it }, label = { Text("Subtítulo") })

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Switch(
                        checked = status == 1,
                        onCheckedChange = { status = if (it) 1 else 0 }
                    )
                    Text("Activo", modifier = Modifier.padding(start = 8.dp))
                }

            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onSave(
                        ParentModule(
                            id = parentModule?.id ?: 0, // 👈 Int
                            title = title,
                            code = code,
                            subtitle = subtitle,
                            type = parentModule?.type ?: "",
                            icon = parentModule?.icon ?: "",
                            status = status,
                            moduleOrder = parentModule?.moduleOrder ?: 0,
                            link = parentModule?.link ?: "",
                            createdAt = parentModule?.createdAt ?: "",
                            updatedAt = parentModule?.updatedAt ?: "",
                            deletedAt = parentModule?.deletedAt
                        )
                    )
                },
                enabled = title.isNotEmpty() && code.isNotEmpty()
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}