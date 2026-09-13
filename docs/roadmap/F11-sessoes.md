# F11 — Sessões e comandos tipados

Estado: NÃO INICIADA. Este arquivo é especificação de trabalho, não relato de execução.

## Resultado esperado

Iniciar, acompanhar e cancelar trabalho com estado operacional comprovado.

## Entrada e leitura

- Dependências: F10 concluída; EXT-05 e EXT-09 disponíveis. Ações sensíveis aguardam aprovação, cuja UI entra em F12.
- Referências: INTEGRATION_CONTRACT (estados), IMPLEMENTATION e VALIDATION Q02/Q07/Q12.
- Aplicam-se [protocolo](../execution/PROTOCOL.md), [guia técnico](../planning/IMPLEMENTATION.md) e [validação](../quality/VALIDATION.md).
- Escopo: Cliente Android de sessões; fila, subprocesso, policy engine e adaptadores são da plataforma.

## Tarefas em ordem

### F11-T01 — Modelar acompanhamento de sessão

- [ ] CONCLUÍDA somente após aceite e evidência.
- Execução: Mapear queued, running, waiting_user, paused, succeeded, failed, cancelled e interrupted conforme contrato. Exibir agente/modelo apenas quando conhecidos, horário e evidências. Separar status remoto informado de resultado verificado.
- Entrega: feature/sessions, modelos e testes de transição/projeção.
- Aceite verificável: Estados desconhecidos/inconsistentes não viram sucesso; UI informa evidência ausente sem falsificar o evento.

### F11-T02 — Enviar início tipado

- [ ] CONCLUÍDA somente após aceite e evidência.
- Execução: Formulário coleta objetivo e opções allowlisted; texto livre é conteúdo do objetivo, nunca comando shell. Validar capability e versão atuais, enviar commandId e apresentar recibo. Tratar waiting_user para decisão exigida sem bypass.
- Entrega: Fluxo online de início e reconciliação por commandId.
- Aceite verificável: Duplo toque/timeout não inicia sessões duplicadas; 202 aparece como solicitação recebida, não execução concluída.

### F11-T03 — Implementar cancelamento e pausa opcional

- [ ] CONCLUÍDA somente após aceite e evidência.
- Execução: Solicitar cancelamento tipado online; aguardar confirmação da plataforma. Pausa/retomada só aparecem com capability comprovada. Timeout mostra resultado desconhecido, oferece reconciliar e não anuncia processo encerrado.
- Entrega: Controles de sessão e testes de corrida entre finalizar/cancelar.
- Aceite verificável: Cancelamento real só aparece confirmado após evento/consulta válida e auditoria; sessão já terminal recebe tratamento coerente.

### F11-T04 — Validar uma sessão real de teste

- [ ] CONCLUÍDA somente após aceite e evidência.
- Execução: Executar tarefa pequena autorizada no projeto de teste por adaptador real disponível. Observar início, eventos, final/evidência e outra sessão cancelada. Testar crash/rate limit/auth failure por fixtures e casos reais seguros quando disponíveis.
- Entrega: Evidência de sessão real + matriz de erros sem promessa universal de provedor.
- Aceite verificável: Uma sessão real rastreada e um cancelamento auditado; sem shell genérico nem token de provedor no app.

## Gate de saída

Sessão real inicia/acompanhada e cancelamento tem confirmação operacional. Ações ainda sem aprovação móvel continuam bloqueadas por política.

Demonstração: Enviar objetivo de teste, observar estado do agente e fonte de resultado; cancelar outra sessão e conferir confirmação.

## Limite e bloqueio

Não implementar adaptador Codex/Claude/Antigravity no Android. Fonte de cota ausente continua desconhecida.

## Registro e próxima fase

Use o [modelo de entrega](../execution/DELIVERY_TEMPLATE.md), aplique AUDIT e atualize PROJECT_STATE/CHANGELOG.
Não marque a fase concluída apenas por escrever os arquivos. A sequência completa está no [roadmap](../../ROADMAP.md).
