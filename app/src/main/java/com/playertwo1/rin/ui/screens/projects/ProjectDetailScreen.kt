package com.playertwo1.rin.ui.screens.projects

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Unarchive
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.playertwo1.rin.R
import com.playertwo1.rin.core.model.DataOrigin
import com.playertwo1.rin.core.model.LocalCheckpoint
import com.playertwo1.rin.core.model.LocalProject
import com.playertwo1.rin.core.model.ProjectStatus
import com.playertwo1.rin.ui.theme.StatusError
import com.playertwo1.rin.ui.theme.StatusSuccess
import com.playertwo1.rin.ui.theme.StatusWarning
import com.playertwo1.rin.ui.theme.TextMuted
import com.playertwo1.rin.ui.theme.TextSecondary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.ui.text.style.TextAlign

@Composable
fun ProjectDetailScreen(
    project: LocalProject,
    checkpoint: LocalCheckpoint?,
    checkpoints: List<LocalCheckpoint> = emptyList(),
    onBack: () -> Unit,
    onEdit: (LocalProject) -> Unit,
    onToggleArchive: (String) -> Unit,
    onDeleteProject: (String) -> Unit,
    onCreateCheckpoint: () -> Unit = {},
    onEditCheckpoint: (LocalCheckpoint) -> Unit = {},
    onDeleteCheckpoint: (String) -> Unit = {}
) {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
    val scrollState = rememberScrollState()

    var showDeleteDialog by remember { mutableStateOf(false) }
    var showRemoveCacheDialog by remember { mutableStateOf(false) }
    var checkpointToDelete by remember { mutableStateOf<LocalCheckpoint?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Voltar à lista de projetos"
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = project.name,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "[${project.origin.name}] • ${project.syncState.name}",
                    style = MaterialTheme.typography.labelSmall,
                    color = if (project.origin == DataOrigin.LOCAL) {
                        MaterialTheme.colorScheme.tertiary
                    } else {
                        MaterialTheme.colorScheme.primary
                    }
                )
            }
            if (project.businessStatus == ProjectStatus.ARCHIVED) {
                SuggestionChip(
                    onClick = {},
                    label = { Text("ARQUIVADO", color = StatusWarning) }
                )
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Metadados do Projeto",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                Text(text = "ID Local: ${project.id}", style = MaterialTheme.typography.bodySmall)
                Text(text = "Origem: ${project.origin}", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Prioridade: ${project.priority.name}", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Workstation ID: ${project.workstationId ?: "Nenhum (Local)"}", style = MaterialTheme.typography.bodyMedium)
                Text(text = "ID Remoto: ${project.remoteProjectId ?: "Nenhum (Local)"}", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Descrição: ${project.description}", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Branch: ${project.currentBranch ?: "Desconhecida"}", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Commit Git: ${project.gitCommitHash ?: "Desconhecido"}", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Status de Negócio: ${project.businessStatus}", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Estado de Sincronização: ${project.syncState}", style = MaterialTheme.typography.bodyMedium)
                Text(
                    text = "Criado em: ${dateFormat.format(Date(project.createdAt))}",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextMuted
                )
                Text(
                    text = "Modificado em: ${dateFormat.format(Date(project.updatedAt))}",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextMuted
                )
                Text(
                    text = "Última confirmação: ${project.lastConfirmedAt?.let { dateFormat.format(Date(it)) } ?: "Não confirmada (sem rede)"}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (project.lastConfirmedAt != null) StatusSuccess else TextMuted
                )
                Text(
                    text = "Cota de recursos: ${project.quotaUsagePercent?.let { "$it%" } ?: "Não informada (desconhecida)"}",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextMuted
                )
                Text(
                    text = "Suíte de testes: ${project.testRunStatus ?: "Não informada (desconhecida)"}",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextMuted
                )
            }
        }

        // Seção: Histórico de Checkpoints
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = stringResource(R.string.section_checkpoints_history),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${checkpoints.size} registro(s) persistido(s)",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextMuted
                        )
                    }
                    Button(
                        onClick = onCreateCheckpoint,
                        contentPadding = ButtonDefaults.ButtonWithIconContentPadding
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "Novo")
                    }
                }

                HorizontalDivider()

                if (checkpoints.isEmpty()) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.BookmarkBorder,
                                contentDescription = null,
                                modifier = Modifier.size(36.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = stringResource(R.string.empty_checkpoints_title),
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = stringResource(R.string.empty_checkpoints_desc),
                                style = MaterialTheme.typography.bodySmall,
                                color = TextMuted,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            OutlinedButton(onClick = onCreateCheckpoint) {
                                Text(stringResource(R.string.action_add_first_checkpoint))
                            }
                        }
                    }
                } else {
                    checkpoints.forEach { item ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Bookmark,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Text(
                                            text = item.title,
                                            style = MaterialTheme.typography.titleSmall,
                                            fontWeight = FontWeight.Bold,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                    SuggestionChip(
                                        onClick = {},
                                        label = { Text(item.origin.name, style = MaterialTheme.typography.labelSmall) }
                                    )
                                    IconButton(
                                        onClick = { onEditCheckpoint(item) },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Edit,
                                            contentDescription = stringResource(R.string.action_edit_checkpoint),
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                    IconButton(
                                        onClick = { checkpointToDelete = item },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.DeleteOutline,
                                            contentDescription = stringResource(R.string.action_delete_checkpoint),
                                            tint = StatusError,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }

                                Text(
                                    text = item.summary,
                                    style = MaterialTheme.typography.bodyMedium
                                )

                                if (!item.nextStep.isNullOrBlank()) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Text(
                                            text = "${stringResource(R.string.checkpoint_next_step_prefix)}${item.nextStep}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.primary,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }

                                if (!item.blockers.isNullOrBlank()) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Warning,
                                            contentDescription = null,
                                            tint = StatusWarning,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Text(
                                            text = "${stringResource(R.string.checkpoint_blockers_prefix)}${item.blockers}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = StatusWarning
                                        )
                                    }
                                }

                                if (!item.referencesText.isNullOrBlank()) {
                                    Text(
                                        text = "${stringResource(R.string.checkpoint_references_prefix)}${item.referencesText}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = TextMuted
                                    )
                                }

                                Text(
                                    text = "Criado em: ${dateFormat.format(Date(item.createdAt))}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TextMuted
                                )
                            }
                        }
                    }
                }
            }
        }

        // Ações locais seguras
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Ações Disponíveis",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                if (project.origin == DataOrigin.LOCAL) {
                    Button(
                        onClick = { onEdit(project) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Editar Projeto")
                    }

                    OutlinedButton(
                        onClick = { onToggleArchive(project.id) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = if (project.businessStatus == ProjectStatus.ARCHIVED) {
                                Icons.Default.Unarchive
                            } else {
                                Icons.Default.Archive
                            },
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (project.businessStatus == ProjectStatus.ARCHIVED) {
                                stringResource(R.string.action_unarchive_project)
                            } else {
                                stringResource(R.string.action_archive_project)
                            }
                        )
                    }

                    Button(
                        onClick = { showDeleteDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = stringResource(R.string.action_delete_local_project))
                    }
                } else {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Cloud,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Column {
                                Text(
                                    text = stringResource(R.string.remote_project_notice_title),
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = stringResource(R.string.remote_project_notice_desc),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextMuted
                                )
                            }
                        }
                    }

                    OutlinedButton(
                        onClick = { showRemoveCacheDialog = true },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = StatusWarning),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(imageVector = Icons.Default.DeleteSweep, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = stringResource(R.string.action_remove_cache))
                    }
                }
            }
        }
    }

    // Diálogo de confirmação de exclusão local definitiva
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            icon = {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    tint = StatusError
                )
            },
            title = {
                Text(
                    text = stringResource(R.string.delete_project_confirm_title),
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(text = stringResource(R.string.delete_project_confirm_message))
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteDialog = false
                        onDeleteProject(project.id)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = StatusError)
                ) {
                    Text(stringResource(R.string.action_confirm_delete))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text(stringResource(R.string.action_cancel))
                }
            }
        )
    }

    // Diálogo de confirmação de remoção do cache local
    if (showRemoveCacheDialog) {
        AlertDialog(
            onDismissRequest = { showRemoveCacheDialog = false },
            icon = {
                Icon(
                    imageVector = Icons.Default.Cloud,
                    contentDescription = null,
                    tint = StatusWarning
                )
            },
            title = {
                Text(
                    text = stringResource(R.string.remove_cache_confirm_title),
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(text = stringResource(R.string.remove_cache_confirm_message))
            },
            confirmButton = {
                Button(
                    onClick = {
                        showRemoveCacheDialog = false
                        onDeleteProject(project.id)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = StatusWarning)
                ) {
                    Text(stringResource(R.string.action_remove_cache))
                }
            },
            dismissButton = {
                TextButton(onClick = { showRemoveCacheDialog = false }) {
                    Text(stringResource(R.string.action_cancel))
                }
            }
        )
    }

    // Diálogo de confirmação de exclusão de checkpoint
    if (checkpointToDelete != null) {
        AlertDialog(
            onDismissRequest = { checkpointToDelete = null },
            icon = {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    tint = StatusError
                )
            },
            title = {
                Text(
                    text = stringResource(R.string.delete_checkpoint_confirm_title),
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(text = stringResource(R.string.delete_checkpoint_confirm_message))
            },
            confirmButton = {
                Button(
                    onClick = {
                        val id = checkpointToDelete!!.id
                        checkpointToDelete = null
                        onDeleteCheckpoint(id)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = StatusError)
                ) {
                    Text(stringResource(R.string.action_confirm_delete))
                }
            },
            dismissButton = {
                TextButton(onClick = { checkpointToDelete = null }) {
                    Text(stringResource(R.string.action_cancel))
                }
            }
        )
    }
}
