# Changelog

Todas as mudanças relevantes do RIN serão registradas neste arquivo.

As entradas 0.1.0 e 0.2.0 abaixo versionam a documentação histórica; não comprovam versões de APK.

## Não lançado — 2026-09-13

### Adicionado

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
