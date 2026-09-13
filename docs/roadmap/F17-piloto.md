# F17 — Piloto pessoal e duas semanas estáveis

Estado: NÃO INICIADA. Este arquivo é especificação de trabalho, não relato de execução.

## Resultado esperado

Demonstrar utilidade e estabilidade no uso real antes de chamar o app de 1.0.

## Entrada e leitura

- Dependências: F16 concluída; Rafael/aparelho e dois projetos autorizados disponíveis.
- Referências: RELEASE_1_0, REQUIREMENTS e métricas históricas registradas em SOURCES.
- Aplicam-se [protocolo](../execution/PROTOCOL.md), [guia técnico](../planning/IMPLEMENTATION.md) e [validação](../quality/VALIDATION.md).
- Escopo: Projeto pequeno + 360, medições reais, correções orientadas por incidentes.

## Tarefas em ordem

### F17-T01 — Preparar roteiro e registro diário

- [ ] CONCLUÍDA somente após aceite e evidência.
- Execução: Identificar projetos reais autorizados e versões de app/plataforma/aparelho. Definir sessão de uso diária, retomadas 24/72h, handoffs e decisões de teste seguras. Definir local dos registros e tratamento de dados privados.
- Entrega: docs/pilot/PLAN.md e modelo diário, caminhos previstos até criação.
- Aceite verificável: Roteiro reexecutável e projetos escolhidos de fato; não usar 360 sem acesso/escopo confirmado.

### F17-T02 — Medir retomadas e handoffs reais

- [ ] CONCLUÍDA somente após aceite e evidência.
- Execução: Após intervalos reais de 24 e 72 horas, medir abrir projeto → entender próximo passo/Continuar em ambos os projetos. Registrar tentativas de handoff e aceites, sem escolher só sucessos. Medir eventos foreground na LAN com metodologia de relógio.
- Entrega: Registros datados com tempos, eventos, versões e limitações.
- Aceite verificável: Retomada até 60s e latência até 3s nos roteiros foreground LAN demonstradas com amostras registradas. Alvo não atendido mantém tarefa aberta para diagnóstico. Tempo simulado não substitui espera real.

### F17-T03 — Corrigir incidentes e estabilizar

- [ ] CONCLUÍDA somente após aceite e evidência.
- Execução: Registrar reprodução, impacto e severidade. Corrigir na menor área possível, rodar checks afetados e emitir candidato identificável. Falso sucesso/perda/ação indevida exige nova janela estável após correção; não mudar metas para esconder incidente.
- Entrega: Backlog factual de piloto e versões corrigidas com evidência.
- Aceite verificável: Sem achado relevante pendente; notificações/fricções ajustadas por observação real.

### F17-T04 — Concluir janela de duas semanas

- [ ] CONCLUÍDA somente após aceite e evidência.
- Execução: Acompanhar 14 dias consecutivos estáveis da versão/candidato validado, com uso registrado. Contabilizar falhas, dias sem uso, handoffs aceitos e ações manuais. Aplicar regra de continuidade de RELEASE_1_0 e revisar gaps.
- Entrega: docs/pilot/REPORT.md com datas e dados reais, checklist de release preenchido apenas no que foi provado.
- Aceite verificável: Duas semanas reais estáveis, sem perda/falso sucesso/ação fora de escopo, e todos os cenários de piloto atendidos.

## Gate de saída

Piloto real atende métricas e duas semanas estáveis; evidências datadas permitem auditoria.

Demonstração: Rafael retoma projeto após dias, toma decisão e conclui handoff sem reconstruir contexto do chat.

## Limite e bloqueio

Não encerrar em uma sessão de testes. Tempo não transcorrido ou uso não registrado mantém F17 aberta; acompanhamento posterior depende do fluxo disponível e autorizado.

## Registro e próxima fase

Use o [modelo de entrega](../execution/DELIVERY_TEMPLATE.md), aplique AUDIT e atualize PROJECT_STATE/CHANGELOG.
Não marque a fase concluída apenas por escrever os arquivos. A sequência completa está no [roadmap](../../ROADMAP.md).
