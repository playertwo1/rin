# Roadmap de execução do RIN até 1.0

Plano preparado em 2026-09-13 a partir de toda a documentação local e dos guardrails do Drive.
**Estado de implementação: não iniciada.** Há 19 fases e 76 tarefas; nenhuma foi marcada como concluída.
Os números do changelog histórico eram revisões documentais, não APKs já publicados.

## Como usar

Comece em [START_HERE](docs/execution/START_HERE.md) e siga [AGENTS](AGENTS.md).
Leia o [protocolo](docs/execution/PROTOCOL.md), identifique a fase em PROJECT_STATE e execute
uma tarefa por vez. Cada arquivo de fase contém entrada, passos, entrega, aceite, demonstração,
gate e bloqueio. “Gate” é a condição verificável para liberar a fase seguinte.
O detalhamento está nos arquivos vinculados; este índice evita um AGENTS extenso.

## Produto 1.0 e fronteira

RIN é um aplicativo Android pessoal para uma workstation na LAN. Projeto Vivo reúne retomada,
checkpoints, fatos de Git/tarefas/bugs/testes, decisões, sessões, aprovações, notificações e AI Shift manual.
O app preserva dados locais e cache offline. A AI Workstation fornece API, memória canônica,
Git, adaptadores, políticas, processos, auditoria operacional e confirmação dos efeitos.

A 1.0 exige integração real e duas semanas estáveis de piloto; um fake funcional é um marco
de desenvolvimento. Não existe backend independente RIN Server nesta sequência.

## Fases em ordem

Todas as fases começam em NÃO INICIADA; o estado atual é mantido em PROJECT_STATE.
A dependência interna padrão de cada fase é a anterior. Os arquivos detalham os gates externos.

| Fase | Missão detalhada | Prova principal |
|---|---|---|
| F00 | [Inventário e decisões de fundação](docs/roadmap/F00-preparacao.md) | Inventário comprovado, toolchain/identidade definidas e setup utilizável. Não exige API real; exige honestidade sobre ausências. |
| F01 | [Scaffold Android e verificação contínua](docs/roadmap/F01-scaffold.md) | App executado, build/lint/testes locais aprovados e CI com execução comprovada para fechar F01 por completo. |
| F02 | [Contrato móvel e gateway determinístico](docs/roadmap/F02-contrato-fake.md) | Primeiro caminho vertical Android → fake → lista/detalhe comprovado e contrato candidato verificável. |
| F03 | [Modelo local e persistência Room](docs/roadmap/F03-persistencia.md) | Projeto, checkpoint e cache persistem; identidades e autoridade dos dados são demonstradas. |
| F04 | [Home e gestão de projetos locais](docs/roadmap/F04-projetos-locais.md) | Home e CRUD local completos, persistentes e com estados acessíveis. |
| F05 | [Checkpoints, decisões locais e Onde parei](docs/roadmap/F05-onde-parei.md) | Onde parei reproduzível e rastreável após reinício; projeto local útil diariamente. |
| F06 | [Exportação aberta e restauração local](docs/roadmap/F06-backup-exportacao.md) | Export/import validado em instalação limpa, sem dados sensíveis e sem reexecução de comandos. |
| F07 | [Eventos, cursor e offline com simulação](docs/roadmap/F07-sincronizacao-simulada.md) | Replay e persistência resistem a duplicatas, reinício e desconexão em ambiente simulado. |
| F08 | [Pareamento seguro e cliente real em leitura](docs/roadmap/F08-pareamento.md) | Aparelho pareado com serviço real, TLS/credencial/revogação testados e leitura funcional. |
| F09 | [Projeto Vivo conectado e fatos verificáveis](docs/roadmap/F09-projeto-conectado.md) | Leitura real e sincronização incremental consistentes, com fonte para os fatos e restart comprovado. |
| F10 | [Escritas de metadados e conflitos offline](docs/roadmap/F10-escritas-conflitos.md) | Escritas acordadas sincronizam idempotentemente e conflitos preservam dados. Toda limitação por capability fica explícita. |
| F11 | [Sessões e comandos tipados](docs/roadmap/F11-sessoes.md) | Sessão real inicia/acompanhada e cancelamento tem confirmação operacional. Ações ainda sem aprovação móvel continuam bloqueadas por política. |
| F12 | [Aprovações vinculadas ao payload](docs/roadmap/F12-aprovacoes.md) | Decisões válidas auditadas; alteração, replay e expiração rejeitados de ponta a ponta. |
| F13 | [AI Shift e confirmação do destino](docs/roadmap/F13-ai-shift.md) | Handoff validado no fake e entre dois adaptadores reais, com manifesto e aceite do destino rastreáveis. |
| F14 | [Notificações e operação em segundo plano](docs/roadmap/F14-notificacoes.md) | Notificações relevantes funcionam nas condições verificadas, deduplicam e respeitam privacidade e permissão. |
| F15 | [Experiência móvel e acessibilidade completas](docs/roadmap/F15-experiencia-acessibilidade.md) | Todos os fluxos previstos são legíveis, navegáveis e acessíveis nos ambientes testados. |
| F16 | [Resiliência, segurança e candidato ao piloto](docs/roadmap/F16-resiliencia-candidato.md) | Candidato interno assinado, sem falhas relevantes e com atualização/recuperação validadas. |
| F17 | [Piloto pessoal e duas semanas estáveis](docs/roadmap/F17-piloto.md) | Piloto real atende métricas e duas semanas estáveis; evidências datadas permitem auditoria. |
| F18 | [Auditoria final e versão 1.0](docs/roadmap/F18-release-1-0.md) | RIN 1.0 distribuído no canal autorizado com artefato verificado, documentação e piloto comprovados. |

## Marcos de produto

| Marco | Abrange | O que pode ser demonstrado |
|---|---|---|
| Fundação | F00–F03 | Build, gateway simulado e dados persistidos |
| Projeto Vivo local | F04–F06 | Projetos, retomada e restauração sem rede |
| Integração de leitura | F07–F09 | Replay, pareamento e fatos reais no celular |
| Operação móvel | F10–F14 | Escritas, sessões, aprovações, AI Shift e notificações |
| Candidato ao piloto | F15–F16 | UX, segurança, recuperação e APK assinado |
| Versão 1.0 | F17–F18 | Uso real por duas semanas e distribuição verificada |

Estes marcos não reservam números de versão intermediários. A numeração de APK é definida
antes da distribuição e não deve ser confundida com a documentação antiga.

## Dependências que impedem avanço real

[EXTERNAL_DEPENDENCIES](docs/planning/EXTERNAL_DEPENDENCIES.md) lista EXT-01–EXT-10 e
OPEN-01–OPEN-09, responsáveis por papel e provas necessárias. O estado inicial é NÃO VERIFICADO.
F08 precisa de contrato e pareamento reais; F10 precisa de escrita/versão/idempotência;
F11–F13 precisam de sessões, aprovações e handoff publicados na plataforma.
Ausência de acesso/capacidade deve ser registrada. Não criar o serviço faltante neste repositório.

A sequência é padrão; uma tarefa independente de fase posterior pode ser preparada apenas
dentro da autorização recebida e com justificativa no estado. Isso nunca encerra uma fase
bloqueada nem elimina um gate anterior. Não iniciar duas tarefas EM ANDAMENTO simultaneamente.

## Mapa do roadmap anterior

| Plano Android anterior | Desdobramento atual |
|---|---|
| Fase 0 — Fundação Android | F00–F03 e validação contínua desde F01 |
| Fase 1 — Projeto Vivo local | F04–F06 |
| Fase 2 — Conexão simulada | F02 e F07 |
| Fase 3 — Integração real | F08–F10 e F14 |
| Fase 4 — Sessões | F11 |
| Fase 5 — Aprovações | F12 e revisão F16 |
| Fase 6 — AI Shift | F13 |
| Fase 7 — Piloto | F15–F18 |

## Documentos de apoio

- [Origem, leitura e divergências históricas](docs/planning/SOURCES.md).
- [Guia técnico de implementação](docs/planning/IMPLEMENTATION.md).
- [Requisitos e rastreabilidade](docs/planning/REQUIREMENTS.md).
- [Matriz de qualidade](docs/quality/VALIDATION.md).
- [Checklist de release 1.0](docs/quality/RELEASE_1_0.md).
- [Modelo de evidência por tarefa](docs/execution/DELIVERY_TEMPLATE.md).
- [Guardrails importados](AI_PROJECT_GUARDRAILS/README.md).
- [Backlog após 1.0](docs/planning/POST_1_0.md).

## Definição de conclusão

Uma tarefa só conclui com aceite satisfeito, validação executada e evidência real.
Uma fase só conclui com todas as tarefas e gate comprovados.
O produto só chega à 1.0 após [todos os critérios de release](docs/quality/RELEASE_1_0.md).
Não estimar datas por número de fases: dependências externas e piloto têm tempo real.
