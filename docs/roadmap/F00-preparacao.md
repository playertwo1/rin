# F00 — Inventário e decisões de fundação

Estado: CONCLUÍDA em 2026-09-13; gate de saída satisfeito.

## Resultado esperado

Estabelecer um ponto de partida reproduzível antes de gerar o app.

## Entrada e leitura

- Dependências: Nenhuma fase anterior. Ler todas as fontes vigentes e os guardrails.
- Referências: SOURCES, IMPLEMENTATION e EXTERNAL_DEPENDENCIES em docs/planning.
- Aplicam-se [protocolo](../execution/PROTOCOL.md), [guia técnico](../planning/IMPLEMENTATION.md) e [validação](../quality/VALIDATION.md).
- Escopo: Inventário, decisões e instruções de ambiente. Nenhuma integração real ou servidor.

## Tarefas em ordem

### F00-T01 — Inventariar a base e o ambiente

- [x] CONCLUÍDA em 2026-09-13; ver [evidência](../evidence/F00-T01-2026-09-13.md).
- Execução: Inspecionar Git, arquivos, Java, Android SDK, emuladores e ferramentas disponíveis; não instalar ferramentas por tentativa. Registrar versões realmente encontradas, ausências e alterações prévias do usuário. Ler o contrato e distinguir rascunho de API publicada.
- Entrega: [docs/evidence/F00-T01-2026-09-13.md](../evidence/F00-T01-2026-09-13.md) com base real e matriz presente/ausente.
- Aceite verificável: Inventário reexecutável com comandos e saídas resumidas; nenhum build alegado sem wrapper.

### F00-T02 — Definir identidade e toolchain

- [x] CONCLUÍDA em 2026-09-13; ver [evidência](../evidence/F00-T02-2026-09-13.md).
- Execução: Fechar OPEN-01/02 com compatibilidade das versões em fontes oficiais, package e minSdk justificados. Registrar aparelho alvo conhecido ou pendente; não atribuir modelo inventado a Rafael. Definir stack HTTP, injeção e estratégia de módulos sem criar módulos ociosos.
- Entrega: [ADR 0002](../adr/0002-identidade-e-toolchain.md), atualização de [DECISIONS](../DECISIONS.md) e [docs/evidence/F00-T02-2026-09-13.md](../evidence/F00-T02-2026-09-13.md).
- Aceite verificável: Versões fixadas e compatíveis por fonte verificável; dúvidas que impedem scaffold explicitadas, não ocultas.

### F00-T03 — Preparar o procedimento de desenvolvimento

- [x] CONCLUÍDA em 2026-09-13; ver [evidência](../evidence/F00-T03-2026-09-13.md).
- Execução: Documentar como abrir a raiz, configurar SDK local, iniciar emulador e rodar Gradle após F01. Definir arquivos ignorados: build, local.properties, secrets e signing. Separar comandos disponíveis dos planejados; não criar instrução dependente de caminho pessoal fixo.
- Entrega: [docs/development/SETUP.md](../development/SETUP.md), [.gitignore](../../.gitignore) e [docs/evidence/F00-T03-2026-09-13.md](../evidence/F00-T03-2026-09-13.md).
- Aceite verificável: Outra sessão consegue identificar pré-requisitos e próxima ação; credenciais não aparecem nos exemplos.

### F00-T04 — Fechar o primeiro gate documental

- [x] CONCLUÍDA em 2026-09-13; ver [evidência](../evidence/F00-T04-2026-09-13.md).
- Execução: Conferir todas as decisões da fase contra arquitetura e escopo. Conferir links, próxima tarefa e contrato rascunho; registrar OPEN ainda não necessário ao scaffold. Executar auditoria e atualizar estado.
- Entrega: [docs/evidence/F00-T04-2026-09-13.md](../evidence/F00-T04-2026-09-13.md) com encerramento de F00 e aprovação para F01.
- Aceite verificável: Não há decisão obrigatória para scaffold sem resolução; documento não diz que Android já compila.

## Gate de saída

Inventário comprovado, toolchain/identidade definidas e setup utilizável. Não exige API real; exige honestidade sobre ausências.

Demonstração: Reabrir o projeto em nova sessão e identificar ambiente, escolhas e comando inicial sem recorrer ao chat.

## Limite e bloqueio

JDK/SDK inacessível ou identidade indispensável indefinida: registrar fato. Resolver decisões reversíveis dentro da autorização; não inventar preferências.

## Registro e próxima fase

Use o [modelo de entrega](../execution/DELIVERY_TEMPLATE.md), aplique AUDIT e atualize PROJECT_STATE/CHANGELOG.
Não marque a fase concluída apenas por escrever os arquivos. A sequência completa está no [roadmap](../../ROADMAP.md).
