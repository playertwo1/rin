# F00 — Inventário e decisões de fundação

Estado: NÃO INICIADA. Este arquivo é especificação de trabalho, não relato de execução.

## Resultado esperado

Estabelecer um ponto de partida reproduzível antes de gerar o app.

## Entrada e leitura

- Dependências: Nenhuma fase anterior. Ler todas as fontes vigentes e os guardrails.
- Referências: SOURCES, IMPLEMENTATION e EXTERNAL_DEPENDENCIES em docs/planning.
- Aplicam-se [protocolo](../execution/PROTOCOL.md), [guia técnico](../planning/IMPLEMENTATION.md) e [validação](../quality/VALIDATION.md).
- Escopo: Inventário, decisões e instruções de ambiente. Nenhuma integração real ou servidor.

## Tarefas em ordem

### F00-T01 — Inventariar a base e o ambiente

- [ ] CONCLUÍDA somente após aceite e evidência.
- Execução: Inspecionar Git, arquivos, Java, Android SDK, emuladores e ferramentas disponíveis; não instalar ferramentas por tentativa. Registrar versões realmente encontradas, ausências e alterações prévias do usuário. Ler o contrato e distinguir rascunho de API publicada.
- Entrega: docs/evidence/F00-T01-AAAA-MM-DD.md com base real e matriz presente/ausente.
- Aceite verificável: Inventário reexecutável com comandos e saídas resumidas; nenhum build alegado sem wrapper.

### F00-T02 — Definir identidade e toolchain

- [ ] CONCLUÍDA somente após aceite e evidência.
- Execução: Fechar OPEN-01/02 com compatibilidade das versões em fontes oficiais, package e minSdk justificados. Registrar aparelho alvo conhecido ou pendente; não atribuir modelo inventado a Rafael. Definir stack HTTP, injeção e estratégia de módulos sem criar módulos ociosos.
- Entrega: ADR em docs/adr e atualização de DECISIONS; tabela exata JDK/Gradle/AGP/Kotlin/Compose/Room.
- Aceite verificável: Versões fixadas e compatíveis por fonte verificável; dúvidas que impedem scaffold explicitadas, não ocultas.

### F00-T03 — Preparar o procedimento de desenvolvimento

- [ ] CONCLUÍDA somente após aceite e evidência.
- Execução: Documentar como abrir a raiz, configurar SDK local, iniciar emulador e rodar Gradle após F01. Definir arquivos ignorados: build, local.properties, secrets e signing. Separar comandos disponíveis dos planejados; não criar instrução dependente de caminho pessoal fixo.
- Entrega: docs/development/SETUP.md e convenções de configuração local, caminhos previstos até criação.
- Aceite verificável: Outra sessão consegue identificar pré-requisitos e próxima ação; credenciais não aparecem nos exemplos.

### F00-T04 — Fechar o primeiro gate documental

- [ ] CONCLUÍDA somente após aceite e evidência.
- Execução: Conferir todas as decisões da fase contra arquitetura e escopo. Conferir links, próxima tarefa e contrato rascunho; registrar OPEN ainda não necessário ao scaffold. Executar auditoria e atualizar estado.
- Entrega: Registro de F00 e estado pronto para F01, somente se critérios atendidos.
- Aceite verificável: Não há decisão obrigatória para scaffold sem resolução; documento não diz que Android já compila.

## Gate de saída

Inventário comprovado, toolchain/identidade definidas e setup utilizável. Não exige API real; exige honestidade sobre ausências.

Demonstração: Reabrir o projeto em nova sessão e identificar ambiente, escolhas e comando inicial sem recorrer ao chat.

## Limite e bloqueio

JDK/SDK inacessível ou identidade indispensável indefinida: registrar fato. Resolver decisões reversíveis dentro da autorização; não inventar preferências.

## Registro e próxima fase

Use o [modelo de entrega](../execution/DELIVERY_TEMPLATE.md), aplique AUDIT e atualize PROJECT_STATE/CHANGELOG.
Não marque a fase concluída apenas por escrever os arquivos. A sequência completa está no [roadmap](../../ROADMAP.md).
