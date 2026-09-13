# F18 — Auditoria final e versão 1.0

Estado: NÃO INICIADA. Este arquivo é especificação de trabalho, não relato de execução.

## Resultado esperado

Liberar somente o artefato que corresponde ao código, aos gates e ao piloto comprovados.

## Entrada e leitura

- Dependências: F17 concluída e checklist RELEASE_1_0 satisfeito; autorização de distribuição definida.
- Referências: RELEASE_1_0, VALIDATION, REQUIREMENTS e AUDIT.
- Aplicam-se [protocolo](../execution/PROTOCOL.md), [guia técnico](../planning/IMPLEMENTATION.md) e [validação](../quality/VALIDATION.md).
- Escopo: Congelamento, verificação do artefato, documentação e distribuição autorizada.

## Tarefas em ordem

### F18-T01 — Auditar cobertura e evidências

- [ ] CONCLUÍDA somente após aceite e evidência.
- Execução: Revisar F00–F17 e R01–R16, contrato suportado e dependências reais. Abrir evidências, não apenas confiar em checkboxes. Conferir todos os achados relevantes encerrados e limitações menores visíveis.
- Entrega: Registro final de auditoria com links para gates.
- Aceite verificável: Ausência de API, adaptador, teste ou piloto impede 1.0; fake nunca preenche lacuna real.

### F18-T02 — Produzir artefato final reproduzível

- [ ] CONCLUÍDA somente após aceite e evidência.
- Execução: Congelar revisão, gerar APK release assinado e calcular SHA-256. Conferir que o código funcional corresponde ao candidato do piloto; mudança funcional exige revalidar F17 conforme seu impacto. Rodar build/lint/testes aplicáveis na revisão final, instalação limpa e upgrade. Conferir assinatura, variante real e dados preservados; não reconstruir artefato diferente após verificar hash.
- Entrega: APK 1.0.0 identificado e relatórios vinculados à revisão exata.
- Aceite verificável: Artefato distribuível é exatamente o testado; não há mudanças não rastreadas que invalidem a evidência.

### F18-T03 — Preparar entrega operacional

- [ ] CONCLUÍDA somente após aceite e evidência.
- Execução: Preencher docs/releases/1.0.0.md com compatibilidade da AI Workstation, notas, instalação/upgrade, runbook, limitações e checksums. Resolver licença antes de distribuição pública. Atualizar README/PROJECT_STATE/CHANGELOG com fatos da release.
- Entrega: Pacote de release revisável e instruções de recuperação.
- Aceite verificável: Responsável entende como instalar, atualizar e recuperar; nenhum dado privado ou credencial acompanha pacote.

### F18-T04 — Distribuir no canal autorizado e confirmar

- [ ] CONCLUÍDA somente após aceite e evidência.
- Execução: Verificar autorização já existente para canal/destino; se faltar, deixar candidato concreto pronto e solicitar apenas essa decisão final. Tag/commit devem apontar ao artefato validado. Após distribuição, verificar acesso/instalação e registrar resultado.
- Entrega: Registro de distribuição real ou estado explícito de candidato aguardando distribuição.
- Aceite verificável: Só dizer publicado/distribuído com operação e destino confirmados; só encerrar 1.0 após gates e entrega autorizada.

## Gate de saída

RIN 1.0 distribuído no canal autorizado com artefato verificado, documentação e piloto comprovados.

Demonstração: Instalar o APK final identificado pelo hash, parear e retomar projeto preservando estado em upgrade.

## Limite e bloqueio

Documentação pronta, build debug, demo ou piloto incompleto não autorizam rotular produto como 1.0.

## Registro e próxima fase

Use o [modelo de entrega](../execution/DELIVERY_TEMPLATE.md), aplique AUDIT e atualize PROJECT_STATE/CHANGELOG.
Não marque a fase concluída apenas por escrever os arquivos. A sequência completa está no [roadmap](../../ROADMAP.md).
