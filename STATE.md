# STATE — ponto de retomada

Qualquer agente deve conseguir retomar só com este arquivo + a fase ativa do `ROADMAP.md`, sem a conversa anterior. Curto: o que não for estado atual sai daqui. Este arquivo era o `PROJECT_STATE.md`; o registro original segue abaixo.

**Atualizado:** 2026-10-17 (estado de 2026-09-13) · **Marco ativo:** F05 — Checkpoints, decisões locais e Onde parei · **Estado:** IN_PROGRESS (pausado a pedido do usuário)

## O que funciona hoje (confirmado no código)
- App Android Compose (`com.playertwo1.rin`, `versionName 0.1.0-alpha`) com Home, Projetos, Workstation e Configurações; Room com projetos, checkpoints, decisões e cache; `FakeWorkstationGateway` determinístico.
- F00–F04 concluídas e F05-T01 concluída, com evidências em `docs/evidence/`.
- CI `Android CI` (`lintDebug`, `testDebugUnitTest`, `assembleDebug`): execução `34764944118` no commit `acf91bf` terminou com sucesso em 2026-09-13 12:13 (BRT).

## Última atividade
- ID / commit: `acf91bf` (2026-09-13) — F00 a F05-T01 com testes, persistência Room e evidências.
- Resultado real: segundo `docs/evidence/F05-T01-2026-09-13.md`, 45 testes unitários e 18 instrumentados aprovados, lint sem erros.
- Testes executados: NOT_RUN nesta sessão de padronização (2026-10-17, só documentação; sem Android SDK aqui).
- Evidência: `docs/evidence/F05-T01-2026-09-13.md`; CI acima.

## Próxima ação
- F05-T02 — registrar decisões locais ([F05](docs/roadmap/F05-onde-parei.md)), quando Rafael retomar.

## Bloqueios e aprovações humanas
- Trabalho pausado a pedido do usuário para envio ao GitHub e auditoria do Codex (registro de 2026-09-13).
- F08 em diante depende da API real da AI Workstation (`docs/planning/EXTERNAL_DEPENDENCIES.md`).

## Divergências entre roadmap e implementação
- `docs/roadmap/F01-scaffold.md` ainda diz "CI em validação", mas a execução de CI no `acf91bf` passou.
- A seção "Próximo ciclo" do registro original abaixo descreve o início de F00, já concluído.

---

## Registro original (antigo PROJECT_STATE.md, 2026-09-13)

## Situação comprovada

- Produto: RIN Android da AI Workstation; primeiro módulo Projeto Vivo.
- Base inspecionada: commit `cd49296`, repositório limpo e inventariado.
- Implementação Android: EM ANDAMENTO (F01, F02, F03 e F04 concluídas; F05 em andamento).
- Fase de execução: F05 — Checkpoints e "Onde parei" local.
- Última tarefa concluída: **F05-T01**, [criar registro de checkpoint](docs/evidence/F05-T01-2026-09-13.md).
- Tarefa ativa: Pausado a pedido do usuário para envio ao GitHub e auditoria do Codex.
- Próxima tarefa: **F05-T02**, [registrar decisões locais](docs/roadmap/F05-onde-parei.md).
- Gates de aplicativo concluídos: Gate F00, Gate F01 local, Gate F02, Gate F03 e Gate F04 (Scaffold Compose, lint 0 erros, testes unitários, testes de contrato, Room SQLite persistente e isolado, atomicidade com rollback, Home observando repositório, CRUD local de projetos completo, arquivamento e exclusão protegida, testes instrumentados no Pixel 10 Pro XL e persistência de 3 projetos pós-reinício comprovada).
- Build, lint, testes Android e CI: EXECUTADOS COM SUCESSO LOCALMENTE (CI em nuvem aguardando push).
- API real/pareamento/agentes da AI Workstation: NÃO VERIFICADOS (desenvolvimento com gateway fake determinístico).
- Release/assinatura/distribuição/piloto: NÃO INICIADOS.

## Entregue na preparação documental

- Leitura dos nove Markdown e do texto integral do DOCX da base.
- Separação explícita entre histórico “RIN Server” e fronteira Android vigente.
- Roadmap de 19 fases e 76 tarefas até 1.0, com dependências, entregas e gates.
- Cópia dos três arquivos da pasta AI_PROJECT_GUARDRAILS do Drive e registro de origem.
- AGENTS curto, protocolo do Antigravity, matriz de requisitos, validação e critério de release.
- Registro de revisão documental em [evidência de preparação](docs/evidence/DOC-2026-09-13.md).
- Dez [mockups de telas](docs/design/mockups-v1/README.md) gerados como proposta visual, com dados de exemplo; implementação e aprovação do design ainda pendentes.

Isto não conclui F00: o Antigravity ainda precisa inspecionar seu ambiente e resolver a
toolchain/identidade Android. Os itens acima são documentos produzidos, não funcionalidades implementadas.

## Próximo ciclo

Skills de apoio instaladas em `.agents/skills`: visual-plan, visual-recap,
visualize-repo e agent-watchdog. Instalação local ao projeto; não significa monitor ativo.

Ler [START_HERE](docs/execution/START_HERE.md), cumprir F00-T01–T04 e registrar resultados reais.
Somente depois criar o scaffold F01. Atualizar este arquivo com ID, estado, evidência e próxima ação.

## Riscos e dependências

- Package, SDKs e toolchain: OPEN-01, a decidir em F00.
- Contrato existente é rascunho v0.1; não comprova API operante.
- Pareamento, transporte, conflito, comandos e handoff dependem de entregas externas.
- Custódia da chave de assinatura e distribuição interna precisam de definição antes do piloto.
- Licença permanece aberta e impede distribuição pública sem decisão.
- Detalhes em [EXTERNAL_DEPENDENCIES](docs/planning/EXTERNAL_DEPENDENCIES.md).

## Como manter o estado

Após cada entrega, registrar ID da tarefa, status, qualificador de evidência (fake/real),
caminho de relatório, bloqueio concreto e próxima tarefa elegível.
Não sobrescrever falha com “concluído” por causa de documentação, mock ou teste não executado.
