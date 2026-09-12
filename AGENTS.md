# Instruções para agentes

## Missão

Construir o **RIN**, aplicativo Android da AI Workstation, em ciclos pequenos e verificáveis. O **Projeto Vivo** é o primeiro módulo do app.

## Fonte da verdade

- Escopo Android e UX: este repositório.
- API, execução, agentes, memória canônica e políticas: `playertwo1/aiworkstation`.
- Código e commits: Git.
- Estado resumido deste app: `PROJECT_STATE.md`.
- Decisões: `docs/DECISIONS.md`.
- Integração entre repositórios: `docs/INTEGRATION_CONTRACT.md`.
- Sequência: `ROADMAP.md`.

## Regras obrigatórias

1. Ler README, ROADMAP, PROJECT_STATE, ARCHITECTURE, DECISIONS e INTEGRATION_CONTRACT antes de implementar.
2. Não criar backend, runtime de agentes, orquestrador, vault ou integração direta com CLIs neste repositório.
3. Consumir a AI Workstation somente por contratos versionados e por um gateway substituível.
4. Começar com `FakeWorkstationGateway`; integração real entra após contrato estável.
5. Atualizar `PROJECT_STATE.md` e `CHANGELOG.md` em toda entrega relevante.
6. Registrar decisões arquiteturais em `docs/adr/`.
7. Não armazenar tokens, cookies, credenciais, `.env` ou transcrições sensíveis.
8. Não oferecer shell genérico nem comandos arbitrários.
9. Não declarar sucesso sem evidência confirmada pela plataforma.
10. Preservar operação offline e distinguir dado local, sincronizado, obsoleto e em conflito.

## Estrutura pretendida

```text
app/
core/model/
core/database/
core/network/
core/ui/
feature/projects/
feature/projectdetail/
feature/checkpoints/
feature/approvals/
feature/workstation/
docs/adr/
```

## Primeira missão

1. Criar o scaffold Kotlin/Compose/Material 3.
2. Implementar Project e Checkpoint em Room.
3. Criar Home e detalhe do Projeto Vivo com dados locais.
4. Definir `WorkstationGateway` a partir do contrato publicado.
5. Implementar gateway falso determinístico.
6. Exibir estado da workstation e lista simulada de projetos.
7. Configurar lint, testes, build e CI.

## Critério do primeiro ciclo

O app compila do zero, mantém projetos/checkpoints após reinício, mostra “Onde parei?” determinístico e troca entre gateway falso e implementação futura sem alterar a UI ou o domínio.
