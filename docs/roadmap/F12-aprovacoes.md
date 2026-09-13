# F12 — Aprovações vinculadas ao payload

Estado: NÃO INICIADA. Este arquivo é especificação de trabalho, não relato de execução.

## Resultado esperado

Permitir decisões móveis seguras sem confundir toque local com autorização efetivada.

## Entrada e leitura

- Dependências: F11 concluída; EXT-06 comprovada; resolver OPEN-09.
- Referências: INTEGRATION_CONTRACT (aprovações), IMPLEMENTATION e VALIDATION Q05/Q06/Q08.
- Aplicam-se [protocolo](../execution/PROTOCOL.md), [guia técnico](../planning/IMPLEMENTATION.md) e [validação](../quality/VALIDATION.md).
- Escopo: Caixa, prévia, confirmação, envio e histórico; política e auditoria autoritativas ficam na plataforma.

## Tarefas em ordem

### F12-T01 — Criar caixa e prévia imutável

- [ ] CONCLUÍDA somente após aceite e evidência.
- Execução: Consultar pendências por contrato real; mostrar ação, alvo, projeto, risco, prazo e hash exatos. Oferecer detalhe legível sem segredo. Refetch/versionar ao abrir para impedir decisão sobre prévia alterada.
- Entrega: feature/approvals e mapeamento de pendência/expiração/resolução.
- Aceite verificável: Usuário identifica o que autoriza; prévia incompleta, desconhecida ou desatualizada impede aprovação.

### F12-T02 — Aplicar confirmação local apropriada

- [ ] CONCLUÍDA somente após aceite e evidência.
- Execução: Integrar biometria/credencial de dispositivo conforme política escolhida; testar cancelamento e indisponibilidade. Não tratar biometria como aprovação do servidor. Sem mecanismo exigido, bloquear ação forte com explicação.
- Entrega: Confirmação local e política de fallback documentada/testada.
- Aceite verificável: Falha/cancelamento biométrico não envia aprovação; caminho alternativo nunca reduz exigência acordada.

### F12-T03 — Enviar decisão exata e reconciliar

- [ ] CONCLUÍDA somente após aceite e evidência.
- Execução: Aprovar/rejeitar com identidade, hash, expiração e idempotência do protocolo. Nunca reconstruir payload diferente do exibido. Offline não enfileira decisão sensível; timeout mantém desconhecido até consulta/auditoria.
- Entrega: Envio online e histórico derivado da plataforma.
- Aceite verificável: Recebimento não vira ação executada; duas decisões concorrentes/expiradas não reautorizam nem mudam alvo.

### F12-T04 — Executar testes negativos reais

- [ ] CONCLUÍDA somente após aceite e evidência.
- Execução: Cobrir hash/payload alterado, vencimento, replay, revogação, relógio deslocado, duplo toque e permissão insuficiente. Verificar rejeição na plataforma, não só botão desabilitado. Inspecionar logs/notificações.
- Entrega: Relatório real de rejeições e pelo menos decisão válida auditada em ação de teste.
- Aceite verificável: Nenhum caso inválido efetiva ação; prévia e decisão válida correspondem ao mesmo payload imutável.

## Gate de saída

Decisões válidas auditadas; alteração, replay e expiração rejeitados de ponta a ponta.

Demonstração: Revisar e decidir uma ação de teste, depois demonstrar que mudar payload/usar decisão vencida não autoriza outra ação.

## Limite e bloqueio

Não compensar contrato incompleto com hash inventado, expiração só local ou trust-all. Sem EXT-06, fase bloqueada.

## Registro e próxima fase

Use o [modelo de entrega](../execution/DELIVERY_TEMPLATE.md), aplique AUDIT e atualize PROJECT_STATE/CHANGELOG.
Não marque a fase concluída apenas por escrever os arquivos. A sequência completa está no [roadmap](../../ROADMAP.md).
