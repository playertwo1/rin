# F13 — AI Shift e confirmação do destino

Estado: NÃO INICIADA. Este arquivo é especificação de trabalho, não relato de execução.

## Resultado esperado

Transferir o trabalho com contexto verificável e recuperação quando o destino falha.

## Entrada e leitura

- Dependências: F12 concluída; EXT-07 e EXT-09 comprovadas para dois adaptadores reais.
- Referências: DECISIONS D011/D014, INTEGRATION_CONTRACT, IMPLEMENTATION e VALIDATION Q09/Q12.
- Aplicam-se [protocolo](../execution/PROTOCOL.md), [guia técnico](../planning/IMPLEMENTATION.md) e [validação](../quality/VALIDATION.md).
- Escopo: Selecionar destino, exibir manifesto, solicitar handoff e acompanhar aceite; construção e execução no PC.

## Tarefas em ordem

### F13-T01 — Exibir manifesto e capacidades

- [ ] CONCLUÍDA somente após aceite e evidência.
- Execução: Consumir manifesto versionado fornecido pela plataforma: origem/destino, objetivo, branch/commit, diff, arquivos, testes, decisões, bloqueios, próximos passos, referências e hash. Mostrar ausências/conflitos; destino escolhido manualmente.
- Entrega: feature/handoff e prévia navegável com conteúdo não executável.
- Aceite verificável: Manifesto incompleto/incompatível não é considerado pronto; nenhuma integração direta com agente é criada.

### F13-T02 — Solicitar preparação e execução

- [ ] CONCLUÍDA somente após aceite e evidência.
- Execução: Usar comandos tipados publicados e a aprovação exigida pela política. Mostrar etapas confirmadas de checkpoint, prévia e transferência. Verificar se hash/versão da prévia continua válido; não congelar fila do PC por lógica local.
- Entrega: Fluxo de preparação → revisão → pedido de execução.
- Aceite verificável: Mudar snapshot exige nova revisão/aprovação quando aplicável; clique em Passar turno não é handoff concluído.

### F13-T03 — Acompanhar aceite e recuperação

- [ ] CONCLUÍDA somente após aceite e evidência.
- Execução: Concluir somente após acknowledgement verificável do destino para manifesto correto. Tratar falha de auth/capability/timeout, origem recuperável e retry online controlado. Após perda de resposta, reconciliar ID antes de repetir.
- Entrega: Estados de handoff e opção de recuperação apoiada no contrato.
- Aceite verificável: Destino sem aceite mantém handoff pendente/falho; retry não apaga origem nem cria turnos duplicados.

### F13-T04 — Provar fake e dois adaptadores reais

- [ ] CONCLUÍDA somente após aceite e evidência.
- Execução: Primeiro executar matriz determinística com dois adaptadores simulados na plataforma de teste. Depois realizar transferência entre dois adaptadores reais autorizados; registrar quais, versões, manifesto/hash e aceite. Não exigir marca que não tenha interface comprovada.
- Entrega: Duas classes de evidência separadas: simulação e integração real.
- Aceite verificável: Destino real confirma objetivo, branch/contexto e próximo passo; falha simulada preserva origem. Sem dois reais, gate de F13 permanece aberto.

## Gate de saída

Handoff validado no fake e entre dois adaptadores reais, com manifesto e aceite do destino rastreáveis.

Demonstração: Passar tarefa de teste a outro agente e mostrar a referência ao manifesto aceito; reproduzir falha preservando origem.

## Limite e bloqueio

Não declarar transferência de sessão proprietária nem burla de cotas. Prompt copiado/manual pode ser utilidade identificada, mas não substitui AI Shift validado.

## Registro e próxima fase

Use o [modelo de entrega](../execution/DELIVERY_TEMPLATE.md), aplique AUDIT e atualize PROJECT_STATE/CHANGELOG.
Não marque a fase concluída apenas por escrever os arquivos. A sequência completa está no [roadmap](../../ROADMAP.md).
