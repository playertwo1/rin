# F15 — Experiência móvel e acessibilidade completas

Estado: NÃO INICIADA. Este arquivo é especificação de trabalho, não relato de execução.

## Resultado esperado

Tornar os fluxos 1.0 claros em tela pequena, inclusive sob falha ou informação incompleta.

## Entrada e leitura

- Dependências: F14 concluída.
- Referências: README, VALIDATION Q01/Q02/Q07/Q11 e matriz REQUIREMENTS.
- Aplicam-se [protocolo](../execution/PROTOCOL.md), [guia técnico](../planning/IMPLEMENTATION.md) e [validação](../quality/VALIDATION.md).
- Escopo: Refino de fluxos existentes; nenhuma nova função de produto.

## Tarefas em ordem

### F15-T01 — Auditar todas as telas e estados

- [ ] CONCLUÍDA somente após aceite e evidência.
- Execução: Inventariar Home, projetos, detalhe, checkpoint, workstation, sessão, aprovação, handoff e configurações. Cobrir vazio/loading/erro/offline/stale/conflict e sucesso confirmado. Padronizar origem, última confirmação e retry coerente.
- Entrega: Matriz tela/estado com capturas ou testes e correções.
- Aceite verificável: Nenhum estado importante gera tela em branco, spinner infinito ou valor de sucesso fictício.

### F15-T02 — Validar acessibilidade e configuração

- [ ] CONCLUÍDA somente após aceite e evidência.
- Execução: Testar TalkBack, ordem de foco, labels, contraste, alvos de toque segundo referências Android/Material vigentes, fonte ampliada, teclado, rotação e tema claro/escuro. Documentar versões/dispositivos testados.
- Entrega: Relatório de acessibilidade e testes dos fluxos críticos.
- Aceite verificável: Usuário consegue revisar decisão e próximo passo sem conteúdo cortado ou controles inacessíveis.

### F15-T03 — Reduzir fricção de retomada e decisões

- [ ] CONCLUÍDA somente após aceite e evidência.
- Execução: Medir passos para abrir projeto, identificar próximo passo e revisar risco de aprovação. Organizar informação e textos sem esconder evidências. Validar strings em português e estados longos; não alegar fidelidade a mockup ausente.
- Entrega: Ajustes de UX e roteiro comparável de uso.
- Aceite verificável: Informação principal fica encontrável; melhoria medida por cenário, sem porcentagem inventada.

### F15-T04 — Revisar matriz de requisitos

- [ ] CONCLUÍDA somente após aceite e evidência.
- Execução: Executar caminhos completos com dados locais e reais, confirmar transições e links. Conferir cada requisito R01–R16 contra implementação e evidência; ausência não pode virar status concluído.
- Entrega: Matriz REQUIREMENTS atualizada com evidências reais onde existirem.
- Aceite verificável: Gaps identificados retornam às tarefas responsáveis e bloqueiam gate quando relevantes; não são escondidos no pós-1.0.

## Gate de saída

Todos os fluxos previstos são legíveis, navegáveis e acessíveis nos ambientes testados.

Demonstração: Percorrer retomada → sessão → decisão → handoff usando fonte ampliada e leitor de tela.

## Limite e bloqueio

Polimento não autoriza redesenhar arquitetura nem adicionar novas telas/recursos fora do escopo.

## Registro e próxima fase

Use o [modelo de entrega](../execution/DELIVERY_TEMPLATE.md), aplique AUDIT e atualize PROJECT_STATE/CHANGELOG.
Não marque a fase concluída apenas por escrever os arquivos. A sequência completa está no [roadmap](../../ROADMAP.md).
