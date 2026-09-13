# F14 — Notificações e operação em segundo plano

Estado: NÃO INICIADA. Este arquivo é especificação de trabalho, não relato de execução.

## Resultado esperado

Avisar conclusão, falha, limite e decisão sem duplicação ou promessa de entrega instantânea.

## Entrada e leitura

- Dependências: F13 concluída; eventos e capabilities reais estabilizados.
- Referências: ARCHITECTURE, VALIDATION Q03/Q08 e contrato de eventos vigente.
- Aplicam-se [protocolo](../execution/PROTOCOL.md), [guia técnico](../planning/IMPLEMENTATION.md) e [validação](../quality/VALIDATION.md).
- Escopo: Notificações Android, preferências, deep links e sincronização adiável.

## Tarefas em ordem

### F14-T01 — Definir política de notificação

- [ ] CONCLUÍDA somente após aceite e evidência.
- Execução: Mapear eventos confirmados para canais/categorias: conclusão, falha, limite confiável e decisão. Definir conteúdo mínimo na tela bloqueada, preferência por categoria e agrupamento. Sucesso sem evidência recebe texto compatível com estado informado.
- Entrega: Tabela evento → mensagem → ação → privacidade, com fixtures.
- Aceite verificável: Cota ausente não dispara limite inventado; evento não terminal não envia notificação de conclusão.

### F14-T02 — Implementar permissão e deep links

- [ ] CONCLUÍDA somente após aceite e evidência.
- Execução: Solicitar permissão quando aplicável à versão Android, respeitar negação e oferecer status no app. Deep link usa ID validado e verifica sessão/capability antes de abrir ação; não aprovar diretamente da notificação sem fluxo seguro.
- Entrega: Canais, preferências e navegação até projeto/sessão/aprovação.
- Aceite verificável: Permissão negada não quebra app; link expirado/removido/revogado mostra estado correto.

### F14-T03 — Deduplicar e recuperar eventos

- [ ] CONCLUÍDA somente após aceite e evidência.
- Execução: Persistir controle de evento notificado, agrupar rajadas e limitar repetição. Na retomada, refletir pendências atuais sem bombardear eventos já resolvidos. WorkManager executa trabalho adiável, não substitui canal realtime contínuo.
- Entrega: Persistência de notificações e estratégia de retomada.
- Aceite verificável: Evento repetido/restart não gera duplicação lógica; decisão já resolvida não aparece como ainda pendente.

### F14-T04 — Testar lifecycle e privacidade

- [ ] CONCLUÍDA somente após aceite e evidência.
- Execução: Verificar foreground, background, processo morto, restrição de bateria, sem rede e retorno. Registrar entrega observada e limitações do aparelho; não prometer prazo de 3s com app suspenso. Inspecionar texto de tela bloqueada.
- Entrega: Roteiro em aparelho/emulador e resultado real por condição.
- Aceite verificável: Falhas/restrições ficam visíveis e recuperáveis; nenhuma credencial, diff sensível ou payload de aprovação exposto.

## Gate de saída

Notificações relevantes funcionam nas condições verificadas, deduplicam e respeitam privacidade e permissão.

Demonstração: Receber evento, abrir detalhe, repetir evento e demonstrar ausência de duplicata; negar permissão e manter app útil.

## Limite e bloqueio

Sem transporte push/background compatível, documentar entrega eventual; não adicionar nuvem/relay não planejado.

## Registro e próxima fase

Use o [modelo de entrega](../execution/DELIVERY_TEMPLATE.md), aplique AUDIT e atualize PROJECT_STATE/CHANGELOG.
Não marque a fase concluída apenas por escrever os arquivos. A sequência completa está no [roadmap](../../ROADMAP.md).
