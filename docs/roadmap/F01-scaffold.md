# F01 — Scaffold Android e verificação contínua

Estado: CONCLUÍDA NO AMBIENTE LOCAL; CI em validação.

## Resultado esperado

Produzir o primeiro app instalável com uma base pequena e testável.

## Entrada e leitura

- Dependências: F00 concluída.
- Referências: IMPLEMENTATION, VALIDATION e ADRs de F00.
- Aplicam-se [protocolo](../execution/PROTOCOL.md), [guia técnico](../planning/IMPLEMENTATION.md) e [validação](../quality/VALIDATION.md).
- Escopo: App, tema, módulos usados, build e CI. Dados de negócio entram nas próximas fases.

## Tarefas em ordem

### F01-T01 — Criar build reproduzível

- [x] CONCLUÍDA em 2026-09-13; ver [evidência](../evidence/F01-T01-2026-09-13.md).
- Execução: Criar wrapper Gradle, catálogo de versões fixas, settings, app e módulos core usados. Aplicar decisões F00, .gitignore e configuração sem SDK path versionado. Gerar namespace/package coerentes; documentar tarefas reais.
- Entrega: [docs/evidence/F01-T01-2026-09-13.md](../evidence/F01-T01-2026-09-13.md), wrapper, configuração Gradle e manifesto.
- Aceite verificável: Build de checkout limpo em caminho distinto não depende de diretórios pessoais; wrapper e dependências são verificáveis.

### F01-T02 — Criar shell de navegação

- [x] CONCLUÍDA em 2026-09-13; ver [evidência](../evidence/F01-T02-2026-09-13.md).
- Execução: Implementar tema Material 3 e entradas Home/Projetos/Workstation/Configurações com estado vazio explícito. Usar ViewModel/estado e composição de dependências. Não apresentar métricas ou projetos inventados como dados reais.
- Entrega: [docs/evidence/F01-T02-2026-09-13.md](../evidence/F01-T02-2026-09-13.md), shell Compose executável e testes unitários.
- Aceite verificável: Abrir app, navegar e voltar não fecha inesperadamente; vazio informa ausência de dados.

### F01-T03 — Instalar e testar o primeiro app

- [x] CONCLUÍDA em 2026-09-13; ver [evidência](../evidence/F01-T03-2026-09-13.md).
- Execução: Rodar build, lint, teste de comportamento de navegação e execução em emulador. Registrar API do emulador, variante, logs e imagem se usada como evidência. Testar rotação/recriação preservando navegação esperada.
- Entrega: [docs/evidence/F01-T03-2026-09-13.md](../evidence/F01-T03-2026-09-13.md), APK debug e screenshot em emulador.
- Aceite verificável: Gradle com exit 0 nos checks aplicáveis; app abre no ambiente registrado. Sem emulador, gate fica aberto.

### F01-T04 — Configurar CI e provar reprodutibilidade

- [x] CONCLUÍDA LOCALMENTE em 2026-09-13; ver [evidência](../evidence/F01-T04-2026-09-13.md).
- Execução: Criar workflow para build, testes unitários e lint com versões compatíveis. Configurar teste instrumentado como job quando viável e documentar requisito. Executar workflow se houver autorização/acesso; não fazer push só para afirmar CI pronta.
- Entrega: [docs/evidence/F01-T04-2026-09-13.md](../evidence/F01-T04-2026-09-13.md) e [.github/workflows/android-ci.yml](../../.github/workflows/android-ci.yml).
- Aceite verificável: Build/lint/testes locais passam; CI precisa de run real para ser declarada validada. Se ausente, F01 fica em validação, com trabalho local comprovado.

## Gate de saída

App executado, build/lint/testes locais aprovados e CI com execução comprovada para fechar F01 por completo.

Demonstração: Instalar APK em emulador limpo, abrir Home vazia, navegar e reproduzir build a partir do setup.

## Limite e bloqueio

Falha de toolchain deve ser diagnosticada. Não remover lint/testes nem apresentar YAML como CI verde; registrar eventual dependência de publicação autorizada.

## Registro e próxima fase

Use o [modelo de entrega](../execution/DELIVERY_TEMPLATE.md), aplique AUDIT e atualize PROJECT_STATE/CHANGELOG.
Não marque a fase concluída apenas por escrever os arquivos. A sequência completa está no [roadmap](../../ROADMAP.md).
