# F10 — Escritas de metadados e conflitos offline

Estado: NÃO INICIADA. Este arquivo é especificação de trabalho, não relato de execução.

## Resultado esperado

Sincronizar intenções móveis permitidas sem perder edições nem sobrescrever estado canônico.

## Entrada e leitura

- Dependências: F09 concluída e EXT-04 comprovada; fechar OPEN-05.
- Referências: IMPLEMENTATION (política de escrita), INTEGRATION_CONTRACT e VALIDATION Q01/Q04.
- Aplicam-se [protocolo](../execution/PROTOCOL.md), [guia técnico](../planning/IMPLEMENTATION.md) e [validação](../quality/VALIDATION.md).
- Escopo: Metadados allowlisted: projetos/notas/checkpoints/decisões conforme contrato. Aprovações e sessões ficam fora da fila offline.

## Tarefas em ordem

### F10-T01 — Fechar catálogo de intenções e vínculo

- [ ] CONCLUÍDA somente após aceite e evidência.
- Execução: Definir operações realmente disponíveis e sua semântica: criar/vincular projeto, publicar checkpoint, editar próximo passo ou decisão. Registrar versão base e política de conflito. Se plataforma não publicar uma operação, mantê-la local explicitamente.
- Entrega: Catálogo aceito e ADR local/remoto com EXT-04 resolvida por operação.
- Aceite verificável: Cada ação habilitada corresponde a contrato existente e authority clara; não inventar POST de CRUD.

### F10-T02 — Implementar outbox persistente

- [ ] CONCLUÍDA somente após aceite e evidência.
- Execução: Gravar alteração local e intenção na mesma transação; usar commandId estável e payload imutável. Mostrar pendência e tentativas; limitar retry. Em timeout após envio, reconciliar recibo/estado antes de repetir; não trocar ID para contornar incerteza.
- Entrega: Outbox/repository/worker e estados visuais de envio.
- Aceite verificável: Matar processo/repetir entrega não duplica operação real; aceite em fila continua pendente até confirmação.

### F10-T03 — Implementar conflito explícito

- [ ] CONCLUÍDA somente após aceite e evidência.
- Execução: Conservar base/local/remoto e permitir escolha consciente ou edição resultante. Reenviar com versão atual apenas após decisão; nunca resolver por relógio do dispositivo ou overwrite silencioso. Tratar projeto removido/permissão revogada.
- Entrega: Tela de conflito e testes de versões concorrentes.
- Aceite verificável: Duas edições concorrentes não apagam texto; usuário pode inspecionar e preservar sua versão.

### F10-T04 — Testar reconexão e isolamento

- [ ] CONCLUÍDA somente após aceite e evidência.
- Execução: Cobrir offline com várias edições, resposta perdida, duplicata, atualização remota, troca de workstation, revogação e payload rejeitado. Confirmar que fila não inclui ação sensível e import não a reativa.
- Entrega: Testes fake/real de escrita tipada, recibos e estado persistido.
- Aceite verificável: Metadados convergem após reconciliação; ações sensíveis não executam no retorno da rede sem nova decisão.

## Gate de saída

Escritas acordadas sincronizam idempotentemente e conflitos preservam dados. Toda limitação por capability fica explícita.

Demonstração: Editar próximo passo offline, alterar a versão remota no projeto de teste e resolver conflito mantendo ambas visíveis.

## Limite e bloqueio

EXT-04 ausente mantém CRUD local funcional, mas não encerra a fase conectada nem fabrica sincronização.

## Registro e próxima fase

Use o [modelo de entrega](../execution/DELIVERY_TEMPLATE.md), aplique AUDIT e atualize PROJECT_STATE/CHANGELOG.
Não marque a fase concluída apenas por escrever os arquivos. A sequência completa está no [roadmap](../../ROADMAP.md).
