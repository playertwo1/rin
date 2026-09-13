# Changelog

Todas as mudanças relevantes do RIN serão registradas neste arquivo.

As entradas 0.1.0 e 0.2.0 abaixo versionam a documentação histórica; não comprovam versões de APK.

## Não lançado — 2026-09-13

### Adicionado

- Tarefa F05-T01 (Criar registro de checkpoint) concluída: interface e lógica de persistência para checkpoints manuais de projetos locais, formulário modal com resumo factual obrigatório (`CheckpointFormDialog` e `CheckpointFormViewModel`), geração estável de UUID, preservação do timestamp `createdAt` na edição, ordenação cronológica decrescente (`createdAt DESC`), histórico de checkpoints no detalhe do projeto (`ProjectDetailScreen`), estado vazio educativo, exclusão e edição locais com diálogo de confirmação, 7 novos testes unitários (45 no total), 5 novos testes instrumentados no emulador Pixel 10 Pro XL (18 no total), validação visual com screenshots e 0 erros de lint. Evidência em [docs/evidence/F05-T01-2026-09-13.md](docs/evidence/F05-T01-2026-09-13.md).
- Fase F04 (Home e gestão de projetos locais) concluída com gate de saída satisfeito: robustez de layout e acessibilidade com `TextOverflow.Ellipsis`, suporte a teclado virtual com `KeyboardOptions` (Next e Done), rótulos semânticos TalkBack (`onClickLabel`), 38 testes unitários passando, 13 testes instrumentados no emulador Pixel 10 Pro XL e demonstração do Gate F04 validada no emulador real (cadastro de 3 projetos locais, edição com alteração de prioridade e status, arquivamento com badge `ARQUIVADO`, encerramento forçado via `am force-stop` e persistência/ordenação verificadas pós-reinício). Evidência em [docs/evidence/F04-T04-2026-09-13.md](docs/evidence/F04-T04-2026-09-13.md).
- Tarefa F04-T03 (Construir detalhe e remoção local segura) concluída: tela rica `ProjectDetailScreen` exibindo metadados discriminados e proteções de integridade, alternância de arquivamento com atualização reativa de status e badge `ARQUIVADO`, diálogo modal de confirmação explícita para exclusão local com aviso de remoção em cascata e não alteração remota, 3 novos testes unitários (38 no total) e 2 novos testes instrumentados no Pixel 10 Pro XL (11 no total). Evidência em [docs/evidence/F04-T03-2026-09-13.md](docs/evidence/F04-T03-2026-09-13.md).
- Tarefa F04-T02 (Implementar criação e edição local de projeto) concluída: `ProjectFormViewModel` com validação de campos (nome obrigatório, erro imediato na UI), diálogo Material 3 `ProjectFormDialog`, `FloatingActionButton` em `ProjectsScreen`, botão de edição em projetos locais, persistência via Room com `origin = DataOrigin.LOCAL` e `syncState = SyncState.LOCAL_ONLY`, cancelamento sem persistência fantasma, 7 novos testes unitários (35 no total) e 2 novos testes instrumentados no Pixel 10 Pro XL (9 no total). Evidência em [docs/evidence/F04-T02-2026-09-13.md](docs/evidence/F04-T02-2026-09-13.md).
- Tarefa F04-T01 (Construir Home e lista) concluída: tela Home e feature Projects observando repositório Room, card "Onde Parei • Projeto Ativo", métricas de projetos locais vs cache simulado, ordenação determinística, migração do banco Room para v2 com campo `priority` (`ProjectPriority`) e teste de migração instrumentado. Evidência em [docs/evidence/F04-T01-2026-09-13.md](docs/evidence/F04-T01-2026-09-13.md).
- Fase F03 (Modelo local e persistência Room) concluída com gate de saída satisfeito: persistência com Android Jetpack Room 2.6.1 SQLite e KSP, entidades `ProjectEntity`, `CheckpointEntity`, `DecisionDraftEntity` e `WorkstationCacheEntity`, integridade referencial com cascading delete, garantia de atomicidade com rollback, isolamento de projetos remotos e locais por chave primária UUID e chave composta `(workstationId, remoteProjectId)`, `WorkstationSyncManager` para cache offline-first com preservação estrita de dados em falha, schema JSON versionado (v1), testes unitários JVM (21 aprovados), testes instrumentados no emulador Pixel 10 Pro XL (6 aprovados) e comprovação de recuperação de dados pós-fechamento do processo. Evidências em [docs/evidence/F03-T01-2026-09-13.md](docs/evidence/F03-T01-2026-09-13.md) a [docs/evidence/F03-T04-2026-09-13.md](docs/evidence/F03-T04-2026-09-13.md).
- Fase F02 (Contrato móvel e gateway determinístico) concluída com gate de saída satisfeito: especificação de contrato em `contracts/README.md` com 5 fixtures JSON, interfaces de domínio e rede desacopladas com mappers, `FakeWorkstationGateway` com 8 cenários determinísticos e indicador de estado `SIMULADA`, testes automatizados de contrato de consumidor e integração vertical no emulador Pixel 10 Pro XL. Evidências em [docs/evidence/F02-T01-2026-09-13.md](docs/evidence/F02-T01-2026-09-13.md) a [docs/evidence/F02-T04-2026-09-13.md](docs/evidence/F02-T04-2026-09-13.md).
- Fase F01 (Scaffold Android e verificação contínua) implementada e validada localmente: scaffold Jetpack Compose com tema Material 3 escuro, navegação por 4 telas (Home, Projetos, Workstation, Configurações), build Gradle 8.11.1 com AGP 8.7.3, testes unitários automatizados, Android Lint sem erros e APK `app-debug.apk` instalado e testado no emulador `Pixel_10_Pro_XL` (Android 17 / API 37) com captura de tela. Evidências em [docs/evidence/F01-T01-2026-09-13.md](docs/evidence/F01-T01-2026-09-13.md) a [docs/evidence/F01-T04-2026-09-13.md](docs/evidence/F01-T04-2026-09-13.md).
- Fase F00 (Inventário e decisões de fundação) concluída com gate de saída satisfeito: fechamento documental em [docs/evidence/F00-T04-2026-09-13.md](docs/evidence/F00-T04-2026-09-13.md). Próxima fase autorizada é a F01 (Scaffold Android e verificação contínua).
- Tarefa F00-T03 concluída: elaboração do procedimento de desenvolvimento e configuração local em [docs/development/SETUP.md](docs/development/SETUP.md), criação do [.gitignore](.gitignore) e registro de evidência em [docs/evidence/F00-T03-2026-09-13.md](docs/evidence/F00-T03-2026-09-13.md).
- Tarefa F00-T02 concluída: definição da identidade do aplicativo (`com.playertwo1.rin`, minSdk 26, target/compileSdk 35) e matriz exata da toolchain (Gradle 8.11.1, AGP 8.7.3, Kotlin 2.0.21, Compose BOM 2024.12.01, Room 2.6.1, OkHttp/Retrofit/Kotlinx Serialization), formalizada na [ADR 0002](docs/adr/0002-identidade-e-toolchain.md) e [docs/evidence/F00-T02-2026-09-13.md](docs/evidence/F00-T02-2026-09-13.md).
- Tarefa F00-T01 concluída: inventário completo da base e do ambiente local (Java 17 Temurin, Android SDK 35–37, ADB, AVD `Pixel_10_Pro_XL`), com matriz de ferramentas presentes/ausentes registrada em [docs/evidence/F00-T01-2026-09-13.md](docs/evidence/F00-T01-2026-09-13.md).
- Skills Agent-Native instaladas no escopo do projeto em `.agents`: visual-plan, visual-recap, visualize-repo e agent-watchdog, com planos locais e sem workflow automático de PR.
- Dez mockups conceituais das telas do RIN, com prompts registrados e imagens preservadas no projeto.
- Plano Android até 1.0 com 19 fases, 76 tarefas, dependências e gates verificáveis.
- Snapshot dos três arquivos de AI_PROJECT_GUARDRAILS obtidos do Google Drive, com origem registrada.
- Entrada de trabalho para Antigravity, protocolo por tarefa e modelo de entrega com evidências.
- Guia técnico, matriz de requisitos, dependências externas, critérios de qualidade, piloto e release.
- ADR 0001 sobre a organização documental e execução incremental.
- Verificador PowerShell para links locais, IDs de tarefas, estrutura das fases e limite de AGENTS.

### Alterado

- AGENTS reduzido a regras centrais e referências; documentação detalhada distribuída por assunto.
- Estado atual distingue planejamento concluído de implementação Android não iniciada.
- Conflitos do DOCX histórico com a arquitetura Android atual explicitamente resolvidos para execução.

Nenhum código Android, backend, APK, CI ou integração real foi implementado nesta entrega documental.

## 0.2.0 — 2026-09-12

### Alterado

- RIN consolidado como aplicativo Android da AI Workstation.
- Projeto Vivo transformado no primeiro módulo do RIN.
- O antigo RIN Server deixou de ser um backend independente e passou à responsabilidade da AI Workstation.
- Arquitetura, roadmap, estado e instruções para agentes receberam fronteiras explícitas.
- Memória canônica, agentes, Git local, políticas e auditoria foram removidos do escopo de implementação deste repositório.

### Adicionado

- Contrato inicial de integração entre RIN e AI Workstation.
- `WorkstationGateway` como fronteira obrigatória do app.
- Matriz de responsabilidade e regras contra duplicação.

## 0.1.0 — 2026-09-11

### Adicionado

- Nome oficial inicial RIN.
- Visão do produto e limites do MVP.
- Arquitetura Android e servidor local.
- Roadmap em oito fases.
- Requisitos de segurança, eventos, dados e handoff.
- Instruções para Codex Astra e outros agentes.
- Conversa consolidada e decisões iniciais.
- Especificação completa em DOCX.
