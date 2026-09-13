# ADR 0001 — Execução incremental do RIN Android até 1.0

Data: 2026-09-13.
Estado: adotada para a organização documental solicitada; implementação não iniciada.

## Contexto

A base contém apenas documentação. O histórico inclui monorepo e servidor RIN,
mas D002, D004, D006 e D008 atuais atribuem a plataforma ao repositório AI Workstation.
Rafael solicitou um plano detalhado, guardrails do Drive e AGENTS curto para o Antigravity.

## Decisão

Desdobrar as oito fases Android em 19 fases F00–F18 com tarefas e gates verificáveis.
Manter AGENTS como entrada curta, detalhes em docs/execution, docs/planning,
docs/quality e docs/roadmap. Importar os três guardrails como snapshot textual.
Usar fake identificado no início e exigir prova real nas fases conectadas.
Manter SDK, bibliotecas, pareamento e contrato real como decisões a verificar em suas fases.

## Consequências

O plano é navegável por tarefa e expõe dependências externas antes da implementação.
Mais arquivos exigem validação de links e rastreabilidade.
Nenhuma fase de aplicativo foi concluída por esta decisão.
As revisões históricas do changelog não correspondem a APKs publicados.

## Alternativas

Um único AGENTS extenso dificultaria leitura e manutenção.
Um roadmap resumido repetiria as lacunas da base.
Reintroduzir servidor no RIN contrariaria as decisões atuais.
