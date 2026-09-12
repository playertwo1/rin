# Roadmap do RIN

O RIN é o aplicativo Android da AI Workstation. O Projeto Vivo é seu primeiro módulo. Este roadmap cobre apenas experiência móvel, persistência local e integração pelo contrato público da plataforma.

## Fase 0 — Fundação Android

- Definir package, minSdk, módulos e convenções.
- Criar scaffold Kotlin/Compose/Material 3.
- Modelar Project, Checkpoint, Decision e SyncState.
- Implementar Room e migrações testadas.
- Definir `WorkstationGateway` conforme `docs/INTEGRATION_CONTRACT.md`.
- Criar fake determinístico e testes de contrato.
- Configurar lint, testes, build e CI.

**Saída:** app compila, persiste um projeto e troca de gateway sem alterar domínio/UI.

## Fase 1 — Projeto Vivo local

- Home com projetos, prioridade, última atividade, bloqueio e próximo passo.
- CRUD de projeto e checkpoint.
- Histórico e resumo determinístico “Onde parei?”.
- Estados vazio, carregando, erro e informação obsoleta.
- Navegação e acessibilidade para uso 100% móvel.
- Backup/exportação aberta.

**Saída:** três projetos reais sobrevivem a reinícios e são retomados sem reconstrução manual.

## Fase 2 — Conexão simulada

- Tela de workstation e capacidades.
- Lista/detalhe de projetos vindos do fake.
- Cursor de eventos e reconexão.
- Estado offline e sincronização.
- Erros estruturados e degradação por capability.
- Testes de contrato compartilháveis.

**Saída:** fluxo completo funciona contra servidor simulado reproduzível.

## Fase 3 — Integração com AI Workstation

- Pareamento seguro.
- Health/version/capabilities.
- Sincronização incremental de projetos e eventos.
- Resolução explícita de conflitos.
- Telemetria operacional permitida: online, degradado, recursos e serviços.
- Notificações de conclusão, falha, limite e decisão.

**Dependência:** endpoints e autenticação implementados em `playertwo1/aiworkstation`.

**Saída:** reiniciar PC/celular não perde estado e nenhuma informação stale aparece como atual.

## Fase 4 — Sessões e Projeto Vivo conectado

- Acompanhar sessão e agente atual.
- Mostrar eventos sanitizados.
- Solicitar início/cancelamento por comandos tipados.
- Diferenciar fila, execução, espera, falha, interrupção e conclusão.
- Mostrar evidências do estado final.
- Nunca aceitar comando arbitrário.

**Saída:** uma sessão de teste é acompanhada e cancelada com auditoria correta.

## Fase 5 — Aprovações

- Caixa de decisões pendentes.
- Prévia de ação, alvo, risco, expiração e hash.
- Aprovação biométrica quando aplicável.
- Aprovar/rejeitar sem alterar o payload.
- Histórico móvel derivado da auditoria da plataforma.
- Revogação de dispositivo e modo somente leitura.

**Saída:** payload alterado invalida aprovação e ação crítica não ocorre sem decisão válida.

## Fase 6 — AI Shift

- Prévia do Handoff Manifest.
- Exibir origem, destino, objetivo, branch, diff, testes, bloqueios e próximos passos.
- Solicitar handoff e acompanhar confirmação do destino.
- Permitir nova tentativa sem perder a origem.
- Mostrar incompatibilidades de capability.

**Saída:** troca entre dois adaptadores simulados e depois reais preserva contexto verificável.

## Fase 7 — Piloto pessoal

- Usar Projeto Vivo com projeto pequeno e com o 360.
- Medir retomadas após 24 e 72 horas.
- Medir tempo para entender estado e concluir handoff.
- Ajustar notificações, densidade e fricções.
- Validar atualização segura e diagnóstico.
- Congelar MVP após duas semanas estáveis.

## Pós-MVP

- Roteamento manual assistido e, depois, automático quando permitido.
- Conselho multiagente com visualização de propostas.
- Knowledge Hub/Obsidian na interface.
- Multi-PC.
- Novos módulos do RIN.
- Acesso remoto zero-trust.

## Fora deste repositório

Runtime de agentes, servidor/API, Git local, Hermes, Obsidian/Vault, policy engine, memória canônica, filas e supervisor de processos são implementados na AI Workstation.
