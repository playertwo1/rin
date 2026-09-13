#requires -Version 7.0
param([switch]$RequireUnstarted)

$ErrorActionPreference = 'Stop'
$repoRoot = [System.IO.Path]::GetFullPath((Join-Path $PSScriptRoot '../..'))
$errorsFound = [System.Collections.Generic.List[string]]::new()
$phaseFiles = @(Get-ChildItem -LiteralPath (Join-Path $repoRoot 'docs/roadmap') -Filter 'F*.md' -File)
$taskIds = @()
$roadmapText = Get-Content -Raw -Encoding utf8 -LiteralPath (Join-Path $repoRoot 'ROADMAP.md')

foreach ($phaseFile in $phaseFiles) {
    $phaseText = Get-Content -Raw -Encoding utf8 -LiteralPath $phaseFile.FullName
    $phaseId = $phaseFile.BaseName.Substring(0, 3)
    $matchesFound = [regex]::Matches($phaseText, '(?m)^### (F\d{2}-T\d{2}) ')
    $taskIds += $matchesFound | ForEach-Object { $_.Groups[1].Value }
    if ($matchesFound.Count -eq 0) { $errorsFound.Add("Sem tarefas: $($phaseFile.Name)") }
    foreach ($match in $matchesFound) {
        if (-not $match.Groups[1].Value.StartsWith($phaseId)) {
            $errorsFound.Add("ID de outra fase em $($phaseFile.Name)")
        }
    }
    foreach ($required in @('## Resultado esperado', '## Entrada e leitura', '## Gate de saída', '## Limite e bloqueio', '## Registro e próxima fase')) {
        if (-not $phaseText.Contains($required)) { $errorsFound.Add("Falta $required em $($phaseFile.Name)") }
    }
    if (-not $roadmapText.Contains("docs/roadmap/$($phaseFile.Name)")) {
        $errorsFound.Add("Fase não indexada: $($phaseFile.Name)")
    }
    if ($RequireUnstarted -and (-not $phaseText.Contains('Estado: NÃO INICIADA.') -or $phaseText -match '(?m)^- \[[xX]\]')) {
        $errorsFound.Add("Baseline deve estar não iniciada: $($phaseFile.Name)")
    }
}

if ($phaseFiles.Count -ne 19) { $errorsFound.Add('Esperadas 19 fases para esta revisão do roadmap.') }
if ($taskIds.Count -ne 76) { $errorsFound.Add('Esperadas 76 tarefas para esta revisão do roadmap.') }
if (@($taskIds | Sort-Object -Unique).Count -ne $taskIds.Count) { $errorsFound.Add('IDs de tarefa duplicados.') }
$agentsLines = @(Get-Content -Encoding utf8 -LiteralPath (Join-Path $repoRoot 'AGENTS.md')).Count
if ($agentsLines -gt 80) { $errorsFound.Add('AGENTS.md excedeu o limite documental de 80 linhas.') }

$allDocs = @(
    Get-ChildItem -LiteralPath $repoRoot -Filter '*.md' -File
    Get-ChildItem -LiteralPath (Join-Path $repoRoot 'docs') -Filter '*.md' -File -Recurse
    Get-ChildItem -LiteralPath (Join-Path $repoRoot 'AI_PROJECT_GUARDRAILS') -Filter '*.md' -File -Recurse
)
$linkCount = 0
foreach ($doc in $allDocs) {
    $docText = Get-Content -Raw -Encoding utf8 -LiteralPath $doc.FullName
    foreach ($link in [regex]::Matches($docText, '\[[^\]]+\]\(([^)]+)\)')) {
        $target = $link.Groups[1].Value
        if ($target -match '^(https?://|#)') { continue }
        $target = ($target -split '#')[0]
        $linkCount++
        if (-not (Test-Path -LiteralPath (Join-Path $doc.DirectoryName $target))) {
            $errorsFound.Add("Link inexistente em $($doc.Name): $target")
        }
    }
}

# Este verificador cobre estrutura e caminhos, não validade semântica, âncoras ou URLs externas.
# Alteração autorizada na quantidade de fases/tarefas exige atualizar as expectativas acima.
$result = [pscustomobject]@{
    MarkdownFiles = $allDocs.Count
    PhaseFiles = $phaseFiles.Count
    Tasks = $taskIds.Count
    UniqueTasks = @($taskIds | Sort-Object -Unique).Count
    AgentsLines = $agentsLines
    LocalLinks = $linkCount
    RequireUnstarted = [bool]$RequireUnstarted
    Errors = @($errorsFound.ToArray())
}
$result | ConvertTo-Json -Depth 5
if ($errorsFound.Count -gt 0) { exit 1 }
exit 0
