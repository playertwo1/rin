package com.playertwo1.rin.ui.screens.projects

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.playertwo1.rin.R
import com.playertwo1.rin.RinApplication
import com.playertwo1.rin.core.model.DataOrigin
import com.playertwo1.rin.core.model.LocalCheckpoint
import com.playertwo1.rin.core.model.LocalProject
import com.playertwo1.rin.ui.theme.StatusError
import com.playertwo1.rin.ui.theme.StatusSuccess
import com.playertwo1.rin.ui.theme.StatusWarning
import com.playertwo1.rin.ui.theme.TextMuted
import com.playertwo1.rin.ui.theme.TextSecondary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ProjectsScreen(
    modifier: Modifier = Modifier,
    viewModel: ProjectsViewModel = run {
        val app = LocalContext.current.applicationContext as RinApplication
        viewModel(
            factory = ProjectsViewModelFactory(
                app.container.projectRepository,
                app.container.workstationSyncManager
            )
        )
    },
    formViewModel: ProjectFormViewModel = run {
        val app = LocalContext.current.applicationContext as RinApplication
        viewModel(
            factory = ProjectFormViewModelFactory(
                app.container.projectRepository
            )
        )
    },
    checkpointFormViewModel: com.playertwo1.rin.ui.screens.projects.checkpoints.CheckpointFormViewModel = run {
        val app = LocalContext.current.applicationContext as RinApplication
        viewModel(
            factory = com.playertwo1.rin.ui.screens.projects.checkpoints.CheckpointFormViewModelFactory(
                app.container.projectRepository
            )
        )
    }
) {
    val uiState by viewModel.uiState.collectAsState()
    val formState by formViewModel.uiState.collectAsState()
    val checkpointFormState by checkpointFormViewModel.uiState.collectAsState()

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (uiState.selectedProject != null) {
                ProjectDetailScreen(
                    project = uiState.selectedProject!!,
                    checkpoint = uiState.selectedProjectCheckpoint,
                    checkpoints = uiState.selectedProjectCheckpoints,
                    onBack = { viewModel.clearSelectedProject() },
                    onEdit = { formViewModel.openForEdit(it) },
                    onToggleArchive = { viewModel.toggleArchive(it) },
                    onDeleteProject = { viewModel.deleteProjectLocally(it) },
                    onCreateCheckpoint = {
                        checkpointFormViewModel.openForCreate(uiState.selectedProject!!.id)
                    },
                    onEditCheckpoint = {
                        checkpointFormViewModel.openForEdit(it)
                    },
                    onDeleteCheckpoint = {
                        viewModel.deleteCheckpointLocally(it)
                    }
                )
            } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Projetos",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Persistência Room • Local-first",
                        style = MaterialTheme.typography.labelMedium,
                        color = TextMuted
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = { viewModel.loadProjects() },
                        enabled = !uiState.isRefreshing
                    ) {
                        if (uiState.isRefreshing) {
                            CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                        } else {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Sincronizar com Workstation"
                            )
                        }
                    }
                    SuggestionChip(
                        onClick = {},
                        label = { Text("SIMULADO", color = MaterialTheme.colorScheme.primary) }
                    )
                }
            }

            if (uiState.errorMessage != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CloudOff,
                            contentDescription = null,
                            tint = StatusWarning
                        )
                        Text(
                            text = uiState.errorMessage ?: "",
                            style = MaterialTheme.typography.bodySmall,
                            color = StatusWarning
                        )
                    }
                }
            }

            when {
                uiState.isLoading && uiState.projects.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                uiState.projects.isEmpty() && uiState.errorMessage == null -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .background(
                                color = MaterialTheme.colorScheme.surface,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.FolderOpen,
                                contentDescription = null,
                                modifier = Modifier.size(64.dp),
                                tint = MaterialTheme.colorScheme.secondary
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = stringResource(R.string.empty_projects_title),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = stringResource(R.string.empty_projects_subtitle),
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextMuted,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
                else -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(uiState.projects, key = { it.id }) { project ->
                            ProjectCard(
                                project = project,
                                onClick = { viewModel.selectProject(project.id) },
                                onEditClick = { formViewModel.openForEdit(project) }
                            )
                        }
                    }
                }
            }
        }
        }

        if (uiState.selectedProject == null) {
            FloatingActionButton(
                onClick = { formViewModel.openForCreate() },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(24.dp),
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Criar Projeto Local"
                )
            }
        }

        ProjectFormDialog(
            state = formState,
            onNameChange = { formViewModel.updateName(it) },
            onDescriptionChange = { formViewModel.updateDescription(it) },
            onPriorityChange = { formViewModel.updatePriority(it) },
            onStatusChange = { formViewModel.updateStatus(it) },
            onDismiss = { formViewModel.dismiss() },
            onSave = {
                formViewModel.save(onSuccess = {
                    viewModel.refreshSelectedProject()
                })
            }
        )

        com.playertwo1.rin.ui.screens.projects.checkpoints.CheckpointFormDialog(
            state = checkpointFormState,
            onTitleChange = { checkpointFormViewModel.updateTitle(it) },
            onSummaryChange = { checkpointFormViewModel.updateSummary(it) },
            onNextStepChange = { checkpointFormViewModel.updateNextStep(it) },
            onBlockersChange = { checkpointFormViewModel.updateBlockers(it) },
            onReferencesChange = { checkpointFormViewModel.updateReferencesText(it) },
            onDismiss = { checkpointFormViewModel.dismiss() },
            onSave = {
                checkpointFormViewModel.save(onSuccess = {
                    viewModel.refreshSelectedProject()
                })
            }
        )
    }
}

@Composable
private fun ProjectCard(
    project: LocalProject,
    onClick: () -> Unit,
    onEditClick: (() -> Unit)? = null
) {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    val confirmedText = if (project.lastConfirmedAt != null) {
        "Confirmado: ${dateFormat.format(Date(project.lastConfirmedAt))}"
    } else {
        "Não confirmado"
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClickLabel = "Abrir detalhes do projeto ${project.name}",
                onClick = onClick
            ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Folder,
                contentDescription = null,
                tint = if (project.origin == DataOrigin.LOCAL) {
                    MaterialTheme.colorScheme.tertiary
                } else {
                    MaterialTheme.colorScheme.primary
                },
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.size(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = project.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.weight(1f, fill = false),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "[${project.origin.name}]",
                        style = MaterialTheme.typography.labelSmall,
                        color = if (project.origin == DataOrigin.LOCAL) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.primary
                    )
                }
                Text(
                    text = project.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Status: ${project.businessStatus.name} • Prioridade: ${project.priority.name}",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextMuted
                )
                Text(
                    text = confirmedText,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (project.lastConfirmedAt != null) StatusSuccess else TextMuted
                )
            }
            if (project.origin == DataOrigin.LOCAL) {
                IconButton(
                    onClick = { onEditClick?.invoke() }
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Editar projeto ${project.name}",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = TextMuted
            )
        }
    }
}

