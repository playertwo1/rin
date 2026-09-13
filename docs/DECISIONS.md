# Decisões do RIN

| ID | Decisão | Estado |
|---|---|---|
| D001 | O aplicativo se chama RIN | Aceita |
| D002 | RIN é o aplicativo Android da AI Workstation | Aceita |
| D003 | Projeto Vivo é o primeiro módulo do RIN, não um produto ou backend separado | Aceita |
| D004 | Execução ocorre no PC pela AI Workstation | Aceita |
| D005 | Git/GitHub é a fonte da verdade do código | Aceita |
| D006 | Memória canônica, sessões, políticas e auditoria pertencem à AI Workstation | Aceita |
| D007 | RIN mantém cache offline, preferências e rascunhos locais | Aceita |
| D008 | Integração ocorre por `WorkstationGateway` e contrato versionado | Aceita |
| D009 | O MVP é single user e local-first | Aceita |
| D010 | Não existe shell genérico no Android | Aceita |
| D011 | Handoff só termina após confirmação do destino pela plataforma | Aceita |
| D012 | Cotas só aparecem quando houver fonte tecnicamente confiável | Aceita |
| D013 | Progresso vem de etapas confirmadas | Aceita |
| D014 | Roteamento inteligente e conselho multiagente ficam pós-MVP | Aceita |
| D015 | Execução F00–F18 com gates e evidências; AGENTS curto e guardrails importados | Adotada para este plano; [ADR 0001](adr/0001-plano-android-ate-1-0.md) |
| D016 | Identidade `com.playertwo1.rin`, minSdk 26, target/compileSdk 35 e toolchain fixada | Aceita; [ADR 0002](adr/0002-identidade-e-toolchain.md) |
| D017 | Stack OkHttp + Retrofit + Kotlinx Serialization, DI manual inicial e módulo único `:app` | Aceita; [ADR 0002](adr/0002-identidade-e-toolchain.md) |
| D018 | Modelo de identidade local desacoplado de remoteId; isolamento por (workstationId, remoteId) com Room SQLite | Aceita; [ADR 0003](adr/0003-modelo-local-e-persistencia.md) |

## Decisões ainda abertas

- REST + WebSocket ou REST + SSE (transporte de eventos da LAN).
- Modelo de pareamento do dispositivo.
- Estratégia de conflito entre edição offline e estado canônico.
- Estratégia de acesso fora da rede local.
- Política futura de distribuição do APK.
- Licença do repositório.

Responsáveis por papel, fase de fechamento e prova necessária estão em
[EXTERNAL_DEPENDENCIES](planning/EXTERNAL_DEPENDENCIES.md). Este plano não fecha
SDK, pareamento, transporte ou conflito por suposição.
