# F08 — Pareamento seguro e cliente real em leitura

Estado: NÃO INICIADA. Este arquivo é especificação de trabalho, não relato de execução.

## Resultado esperado

Conectar o aparelho à workstation com identidade e confiança verificáveis.

## Entrada e leitura

- Dependências: F07 concluída; EXT-01 e EXT-02 disponíveis e comprovadas. Fechar OPEN-04.
- Referências: INTEGRATION_CONTRACT, EXTERNAL_DEPENDENCIES, IMPLEMENTATION e VALIDATION Q05/Q08.
- Aplicam-se [protocolo](../execution/PROTOCOL.md), [guia técnico](../planning/IMPLEMENTATION.md) e [validação](../quality/VALIDATION.md).
- Escopo: Autenticação de dispositivo, TLS, health e acesso de leitura. Nenhum comando de agente.

## Tarefas em ordem

### F08-T01 — Fixar protocolo aceito

- [ ] CONCLUÍDA somente após aceite e evidência.
- Execução: Obter contrato publicado/revisão do serviço e registrar matriz app/plataforma. Comparar com candidato local e explicitar divergências. Documentar bootstrap TLS, desafio de uso único, expiração, renovação, revogação e erros; não usar rotas históricas sem comprovação.
- Entrega: Contrato aceito referenciado, ADR de pareamento e matriz de compatibilidade.
- Aceite verificável: Schema e comportamento reais confirmados. Se inexistentes/inacessíveis, dependência BLOQUEADA sem servidor inventado.

### F08-T02 — Implementar fluxo de pareamento

- [ ] CONCLUÍDA somente após aceite e evidência.
- Execução: Implementar leitura de QR ou entrada autorizada conforme protocolo, validação de destino, confirmação de identidade e chaves no Keystore. Solicitar permissão de câmera no contexto, se usada. Cancelar/expirar não deixa dispositivo meio autorizado.
- Entrega: Tela de conexão e armazenamento protegido da identidade/credencial de dispositivo.
- Aceite verificável: Pareamento correto funciona no real; desafio usado/expirado ou identidade divergente falha com mensagem clara.

### F08-T03 — Conectar gateway real e ciclo da credencial

- [ ] CONCLUÍDA somente após aceite e evidência.
- Execução: Implementar health e leitura autenticada pelo gateway; TLS com verificação correta. Tratar renovação, expiração, revogação e mudança de workstation. Não armazenar tokens de provedor. Cache por identidade permanece separado.
- Entrega: RealWorkstationGateway e gestão de credencial sem vazamento em logs/backup.
- Aceite verificável: Revogação impede novas chamadas autenticadas; dados antigos exibidos apenas como cache. Modo real nunca usa fake em falha.

### F08-T04 — Executar testes negativos de segurança

- [ ] CONCLUÍDA somente após aceite e evidência.
- Execução: Testar certificado/destino incorreto, credencial inválida, permissão negada, QR malformado e reconexão. Validar modo leitura e exclusão de segredos de logs. Documentar política quando biometria/chave não disponível, sem bypass.
- Entrega: Evidência real de pareamento e relatório de controles Android/plataforma.
- Aceite verificável: Health real identificado por versão/horário; tentativas inválidas rejeitadas. UI não prova sozinha revogação no servidor.

## Gate de saída

Aparelho pareado com serviço real, TLS/credencial/revogação testados e leitura funcional.

Demonstração: Parear, consultar health real, revogar no PC e demonstrar bloqueio autenticado mantendo cache identificado.

## Limite e bloqueio

Sem protocolo/ambiente real não encerrar F08. Não desativar TLS ou criar credencial hardcoded para passar.

## Registro e próxima fase

Use o [modelo de entrega](../execution/DELIVERY_TEMPLATE.md), aplique AUDIT e atualize PROJECT_STATE/CHANGELOG.
Não marque a fase concluída apenas por escrever os arquivos. A sequência completa está no [roadmap](../../ROADMAP.md).
