# Estado do RIN

Atualizado em: 2026-09-13.

## Situação comprovada

- Produto: RIN Android da AI Workstation; primeiro módulo Projeto Vivo.
- Base inspecionada: commit `3d24216`, com dez arquivos documentais e nenhum código de produção.
- Entrega atual: planejamento detalhado e guardrails incorporados ao checkout local.
- Implementação Android: NÃO INICIADA.
- Fase de execução: F00 — NÃO INICIADA.
- Tarefa ativa: nenhuma.
- Próxima tarefa: **F00-T01**, [inventário de preparação](docs/roadmap/F00-preparacao.md).
- Gates de aplicativo concluídos: nenhum.
- Build, lint, testes Android e CI: NÃO EXECUTADOS; não existem na base.
- API real/pareamento/agentes da AI Workstation: NÃO VERIFICADOS nesta preparação.
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
