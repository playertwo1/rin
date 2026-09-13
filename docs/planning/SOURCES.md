# Fontes, precedência e divergências

## Inventário lido para este plano

Base local: commit `3d24216`, documentação inspecionada em 2026-09-13.
Todos os dez arquivos rastreados da base foram lidos; o texto do DOCX foi extraído
das partes Word, incluindo tabelas, cabeçalho e rodapé. Não houve avaliação visual
do DOCX nem alteração do arquivo histórico.

| Fonte original | Papel neste plano |
|---|---|
| [README](../../README.md) | Produto, Android, Projeto Vivo e fronteira com a plataforma |
| [AGENTS](../../AGENTS.md) | Limites e fluxo de trabalho; revisado nesta preparação |
| [PROJECT_STATE](../../PROJECT_STATE.md) | Base sem implementação, riscos e próximos passos |
| [ROADMAP](../../ROADMAP.md) | Oito fases originais desdobradas em F00–F18 |
| [CHANGELOG](../../CHANGELOG.md) | Evolução documental 0.1.0 → 0.2.0 |
| [ARCHITECTURE](../ARCHITECTURE.md) | Kotlin/Compose, Room e gateway único |
| [DECISIONS](../DECISIONS.md) | D001–D014 vigentes na base |
| [INTEGRATION_CONTRACT](../INTEGRATION_CONTRACT.md) | Rascunho v0.1; não prova endpoints operantes |
| [CONVERSA_CONSOLIDADA](../CONVERSA_CONSOLIDADA.md) | Motivação e seis provas iniciais |
| [DOCX inicial](../RIN_Especificacao_e_Roadmap_Inicial.docx) | Histórias US001–US012, métricas, cenários e contexto histórico |

O repositório AI Workstation e suas capacidades reais não foram auditados nesta preparação.
Sua URL na documentação indica a dependência, não confirma disponibilidade do serviço.

## Conflitos resolvidos pelo estado mais recente

| Tema antigo | Direção vigente para implementação |
|---|---|
| RIN Server e monorepo Android + servidor | RIN é Android; servidor fica em AI Workstation |
| AgentAdapter e Claw dentro do RIN | RIN conhece WorkstationGateway; adaptadores pertencem à plataforma |
| Memória canônica “pertence ao RIN” | Plataforma é autoridade operacional; Android mantém rascunhos e cache |
| Rotas específicas no DOCX | Contrato atual prevalece; novas rotas exigem alinhamento versionado |
| Fase Zero já pareia e controla servidor | Fundação e fake primeiro; integração real em fases com gate externo |
| Progresso redigido por modelo | Resumo determinístico na 1.0, com fatos e referências |
| Escolha automática de agente | AI Shift manual na 1.0; roteamento automático pós-1.0 |
| CRUD local e cache canônico parecem se misturar | Registro local tem identidade/origem; sincronização exige confirmação remota |

IDs D001 etc. do DOCX pertencem à versão histórica e não substituem IDs atuais de DECISIONS.
O visual aprovado citado no histórico não está no repositório; não inventar fidelidade a uma imagem ausente.
Aplicar Material 3 e requisitos funcionais; registrar escolhas de layout como propostas.

## Origem dos guardrails

Pasta consultada: [AI_PROJECT_GUARDRAILS no Drive](https://drive.google.com/drive/folders/12JieR_wGOPqdBmWmJdHRqquLFgjF0fza).
Foram encontrados três arquivos, sem subpastas na listagem retornada.
O conteúdo textual retornado pelo conector foi copiado integralmente, sem reescrita,
para [AI_PROJECT_GUARDRAILS](../../AI_PROJECT_GUARDRAILS/README.md).
A cópia é snapshot local de 2026-09-13; não há sincronização automática com o Drive.
O bloco original de AGENTS foi integrado por referência e regras curtas na raiz.

## O que é novo neste plano

A divisão em 19 fases, IDs de tarefas, gates, modelos de evidência, matriz de requisitos,
política conservadora de reenvio e critérios detalhados de release são especificações de engenharia
desta preparação. Não representam funcionalidades existentes nem acordo confirmado com a plataforma.
Decisões em aberto permanecem registradas com fase e condição de fechamento.
