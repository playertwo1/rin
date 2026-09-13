# F09 — Projeto Vivo conectado e fatos verificáveis

Estado: NÃO INICIADA. Este arquivo é especificação de trabalho, não relato de execução.

## Resultado esperado

Apresentar dados reais de projetos, Git, trabalho e evidências após reconexão.

## Entrada e leitura

- Dependências: F08 concluída; EXT-03, EXT-08 e EXT-10 comprovadas.
- Referências: IMPLEMENTATION, EXTERNAL_DEPENDENCIES e DOCX histórico US002/US004/US010 em SOURCES.
- Aplicam-se [protocolo](../execution/PROTOCOL.md), [guia técnico](../planning/IMPLEMENTATION.md) e [validação](../quality/VALIDATION.md).
- Escopo: Leitura real, dados operacionais sanitizados e canal de eventos; plataforma ainda sem escrita móvel.

## Tarefas em ordem

### F09-T01 — Validar contrato de leitura no serviço real

- [ ] CONCLUÍDA somente após aceite e evidência.
- Execução: Rodar as expectativas de consumidor da F02 contra cliente real e serviço identificado. Conferir lista/detalhe, autorização, paginação, erros e capabilities; registrar diferenças como mudanças explícitas de contrato.
- Entrega: Testes compartilhados e matriz de compatibilidade com revisões reais.
- Aceite verificável: Mesmas invariantes passam no fake e no real; divergência bloqueante não é escondida com if especial na UI.

### F09-T02 — Exibir projeto e memória canônica

- [ ] CONCLUÍDA somente após aceite e evidência.
- Execução: Apresentar branch, commit, alterações resumidas, tarefas, bugs, testes, decisões, bloqueios e próximos passos quando fornecidos. Mostrar saúde online/degradada, recursos e serviços da workstation apenas com fonte real e última confirmação. Relacionar referências de evidência e timestamps. Não acessar filesystem/Git/CLI no celular.
- Entrega: Projeções e detalhe conectado com filtros/links autorizados.
- Aceite verificável: Dados conferem com fontes autorizadas da plataforma; unknown fica desconhecido e registro local é visualmente distinto.

### F09-T03 — Ativar eventos reais e reconciliar restart

- [ ] CONCLUÍDA somente após aceite e evidência.
- Execução: Implementar transporte acordado (SSE/WebSocket ou polling) e replay canônico. Testar queda de rede, restart PC/app e cursor expirado. Não deixar sessão antiga parecer rodando após reconciliação que a marcou interrompida.
- Entrega: Canal real + estratégia de resync com dados locais preservados.
- Aceite verificável: Eventos e snapshot convergem após falha; sem perda/duplicata e sem stale tratado como atual.

### F09-T04 — Medir caminho vertical no Galaxy Book/PC

- [ ] CONCLUÍDA somente após aceite e evidência.
- Execução: Usar projeto pequeno autorizado em leitura e registrar latência foreground LAN, origem e limites dos relógios. Demonstrar detalhe e evidências após reinício de ambos. Estado offline segue útil e honesto.
- Entrega: Roteiro real, amostras de latência e relatório de restart.
- Aceite verificável: Projeto listado e fatos verificáveis no aparelho; medir alvo de 3 segundos sem inventar garantia de background.

## Gate de saída

Leitura real e sincronização incremental consistentes, com fonte para os fatos e restart comprovado.

Demonstração: Alteração controlada no projeto de teste pelo fluxo autorizado do PC aparece no celular, com evidência e última confirmação.

## Limite e bloqueio

Ausência de campo/capability desabilita a função correspondente; não criar valores de Git, testes ou agentes.

## Registro e próxima fase

Use o [modelo de entrega](../execution/DELIVERY_TEMPLATE.md), aplique AUDIT e atualize PROJECT_STATE/CHANGELOG.
Não marque a fase concluída apenas por escrever os arquivos. A sequência completa está no [roadmap](../../ROADMAP.md).
