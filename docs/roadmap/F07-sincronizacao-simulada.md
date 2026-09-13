# F07 — Eventos, cursor e offline com simulação

Estado: NÃO INICIADA. Este arquivo é especificação de trabalho, não relato de execução.

## Resultado esperado

Validar recuperação de sincronização antes de depender da workstation.

## Entrada e leitura

- Dependências: F06 concluída.
- Referências: INTEGRATION_CONTRACT, IMPLEMENTATION, EXT-03 e VALIDATION Q01/Q03/Q04/Q07.
- Aplicam-se [protocolo](../execution/PROTOCOL.md), [guia técnico](../planning/IMPLEMENTATION.md) e [validação](../quality/VALIDATION.md).
- Escopo: Eventos fake, cache persistido, transporte simulado e replay; sem mutação remota real.

## Tarefas em ordem

### F07-T01 — Especificar o fluxo incremental candidato

- [ ] CONCLUÍDA somente após aceite e evidência.
- Execução: Definir paginação, cursor, deduplicação, reordenação, lacuna e resync no contrato candidato. Marcar semântica proposta como não acordada. Separar ocorrido no servidor de recebido no app; não ordenar por relógio do celular.
- Entrega: Contrato candidato e tabela de cenários em contracts/.
- Aceite verificável: Existe resposta esperada para cursor vazio/inválido/expirado; nenhuma suposição de cursor numérico sem definição.

### F07-T02 — Implementar aplicação transacional

- [ ] CONCLUÍDA somente após aceite e evidência.
- Execução: Persistir evento, projeção e cursor juntos; deduplicar por identidade definida. Em falha, reter cursor anterior. Eventos desconhecidos não fabricam transições. Preservar rascunhos locais durante refresh/resync.
- Entrega: Processador de eventos e DAOs/repository de sincronização.
- Aceite verificável: Injetar falha entre evento e cursor não perde atualização ao reiniciar; duplicatas não duplicam linhas/efeitos.

### F07-T03 — Implementar reconexão e worker

- [ ] CONCLUÍDA somente após aceite e evidência.
- Execução: Adicionar refresh e WorkManager para sincronização adiável, com restrições de rede, backoff limitado e trabalho único. Testar conexão interrompida e retorno. Não usar WorkManager como promessa de canal contínuo de baixa latência.
- Entrega: Agendamento e estados de UI offline/stale/reconectando.
- Aceite verificável: Não surgem workers concorrentes infinitos; sair/voltar e perder rede preserva dados e explica frescor.

### F07-T04 — Executar matriz de falhas no fake HTTP

- [ ] CONCLUÍDA somente após aceite e evidência.
- Execução: Usar servidor HTTP de teste para exercitar serialização, erros e timeout do cliente candidato além do fake em memória. Testar 401/403, erro transitório, versão incompatível, duplicata, reordenação e resync. Não criar serviço de produção.
- Entrega: Testes de contrato/transporte e roteiro de falhas reproduzível.
- Aceite verificável: Cada falha possui estado esperado e recuperação; nenhum erro aciona fallback silencioso para sucesso/demo.

## Gate de saída

Replay e persistência resistem a duplicatas, reinício e desconexão em ambiente simulado.

Demonstração: Interromper conexão entre páginas, reiniciar app e retomar sem perder/duplicar fatos.

## Limite e bloqueio

Transporte real ainda depende de OPEN-03/EXT-03. Falta de acordo não autoriza afirmar SSE/WebSocket compatível.

## Registro e próxima fase

Use o [modelo de entrega](../execution/DELIVERY_TEMPLATE.md), aplique AUDIT e atualize PROJECT_STATE/CHANGELOG.
Não marque a fase concluída apenas por escrever os arquivos. A sequência completa está no [roadmap](../../ROADMAP.md).
