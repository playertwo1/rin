# Instruções para agentes

## Objetivo atual

Construir o RIN em ciclos pequenos e verificáveis. Começar pela Fase Zero do `ROADMAP.md`. Não iniciar pela interface completa, pelo roteamento inteligente ou por execução paralela.

## Fonte da verdade

- Código e commits: Git.
- Estado operacional: banco do RIN Server.
- Estado resumido: `PROJECT_STATE.md`.
- Decisões técnicas: `docs/adr/` e `docs/DECISIONS.md`.
- Escopo e sequência: `ROADMAP.md`.

## Regras obrigatórias

1. Inspecionar o repositório antes de alterar arquivos.
2. Atualizar `PROJECT_STATE.md` e `CHANGELOG.md` em toda entrega relevante.
3. Registrar escolhas arquiteturais em ADR.
4. Manter contratos independentes dos formatos internos dos provedores.
5. Usar agentes simulados para testes comuns e CLIs reais somente em testes opt in.
6. Não armazenar tokens, cookies, credenciais, `.env` ou transcrições sensíveis no Git.
7. Não criar endpoint de shell genérico.
8. Não executar comandos destrutivos em projetos reais.
9. Não declarar sucesso sem evidência persistida.
10. Parar e registrar bloqueio quando uma integração ou permissão não existir.

## Estrutura pretendida

```text
apps/android
apps/server
packages/contracts
tools/simulators
docs/adr
```

## Primeira missão

1. Propor ADR 001 comparando Fastify e Ktor para o servidor.
2. Criar schemas de Project, Session, Event, Handoff, Approval e Error.
3. Criar FakeAgentAdapter e FakeGitRepository determinísticos.
4. Implementar `GET /health` e `GET /api/v1/projects`.
5. Criar o app Compose com conexão e lista de projetos simulados.
6. Configurar lint, testes, build e CI.
7. Registrar os resultados reais no estado e changelog.

## Critério do primeiro ciclo

O ciclo só termina quando servidor e Android compilarem do zero, os contratos forem validados e a lista simulada aparecer no aplicativo por uma conexão real.