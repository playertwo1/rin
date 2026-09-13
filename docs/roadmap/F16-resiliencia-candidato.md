# F16 — Resiliência, segurança e candidato ao piloto

Estado: NÃO INICIADA. Este arquivo é especificação de trabalho, não relato de execução.

## Resultado esperado

Produzir APK atualizável e recuperação comprovada antes do uso diário.

## Entrada e leitura

- Dependências: F15 concluída; EXT-10 comprovada; OPEN-06 resolvida.
- Referências: VALIDATION completa, RELEASE_1_0 e EXTERNAL_DEPENDENCIES.
- Aplicam-se [protocolo](../execution/PROTOCOL.md), [guia técnico](../planning/IMPLEMENTATION.md) e [validação](../quality/VALIDATION.md).
- Escopo: Endurecimento, diagnóstico, migração, assinatura e preparação de piloto interno.

## Tarefas em ordem

### F16-T01 — Executar falhas de persistência/rede

- [ ] CONCLUÍDA somente após aceite e evidência.
- Execução: Testar app/processo morto durante sync/outbox, armazenamento insuficiente, migração, resposta perdida e restart PC. Validar preservação de projetos e caminhos de recuperação. Não editar migração já distribuída; adicionar nova quando necessário.
- Entrega: Matriz de resiliência e correções com testes reproduzíveis.
- Aceite verificável: Nenhuma falha causa reset silencioso, duplicação de ação ou falso estado terminal.

### F16-T02 — Revisar segurança de ponta a ponta

- [ ] CONCLUÍDA somente após aceite e evidência.
- Execução: Repetir testes afetados de TLS, revogação, replay, expiração, payload, links e sanitização. Inspecionar dependências, manifest/permissões, logs e conteúdo do APK. Confirmar que gateway fake não participa da variante real de distribuição.
- Entrega: Revisão de segurança com severidade e evidências.
- Aceite verificável: Sem achado relevante aberto nem token embutido; ação inválida rejeitada na plataforma, não apenas na UI.

### F16-T03 — Criar diagnóstico e runbooks

- [ ] CONCLUÍDA somente após aceite e evidência.
- Execução: Gerar export de diagnóstico por período com versões/correlationIds/erros sanitizados e prévia. Escrever procedimentos para conexão, pareamento perdido, revogação, conflito, migração e recuperação. Backup local preserva dados; chave/credencial não é exportada.
- Entrega: docs/operations/RUNBOOK.md e diagnóstico seguro, caminhos previstos até criação.
- Aceite verificável: Pessoa consegue seguir recuperação em ambiente de teste sem apagar projeto e sem depender do chat.

### F16-T04 — Preparar APK assinado e testar upgrade

- [ ] CONCLUÍDA somente após aceite e evidência.
- Execução: Definir canal interno e custódia da chave fora do Git. Gerar candidato identificado, hash e versão; instalar limpo e atualizar a versão anterior de teste mantendo banco. Testar restauração de backup numa instalação limpa. Não presumir downgrade de schema seguro.
- Entrega: APK candidato, checksums e roteiro de instalação/atualização.
- Aceite verificável: Assinatura/identidade consistente, versionCode crescente, upgrade preserva dados. Não publicar nem afirmar 1.0 neste gate.

## Gate de saída

Candidato interno assinado, sem falhas relevantes e com atualização/recuperação validadas.

Demonstração: Instalar candidato, criar dados, atualizar, simular falha de conexão e seguir runbook de recuperação.

## Limite e bloqueio

Chave de assinatura/identidade de distribuição pertence ao responsável; ausência bloqueia artefato distribuível, não autoriza segredo improvisado no Git.

## Registro e próxima fase

Use o [modelo de entrega](../execution/DELIVERY_TEMPLATE.md), aplique AUDIT e atualize PROJECT_STATE/CHANGELOG.
Não marque a fase concluída apenas por escrever os arquivos. A sequência completa está no [roadmap](../../ROADMAP.md).
