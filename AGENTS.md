# Instruções para agentes — RIN

## Missão e leitura obrigatória

Construir o RIN Android até 1.0, executando uma tarefa verificável por vez.
Projeto Vivo é o primeiro módulo. O roadmap é planejamento, não prova de execução.

1. Leia [WATCHDOG](AI_PROJECT_GUARDRAILS/WATCHDOG.md) antes de alterar qualquer arquivo.
2. Leia [README](README.md), [estado atual](PROJECT_STATE.md) e [roadmap](ROADMAP.md).
3. Leia [arquitetura](docs/ARCHITECTURE.md), [decisões](docs/DECISIONS.md) e [contrato](docs/INTEGRATION_CONTRACT.md).
4. Leia o [protocolo de execução](docs/execution/PROTOCOL.md), a fase ativa e as referências indicadas nela.
5. Antes de entregar, aplique [AUDIT](AI_PROJECT_GUARDRAILS/AUDIT.md) e registre o resultado com evidências.

## Limites obrigatórios

1. Este repositório contém o app; API, Git local, agentes, memória canônica, políticas e auditoria operacional pertencem à AI Workstation.
2. Integre somente por `WorkstationGateway` e contrato versionado. Comece com fake determinístico explicitamente identificado.
3. Não crie backend, runtime, shell genérico, vault, orquestrador ou integração direta com CLIs de agentes.
4. Nunca invente endpoint, capability, versão, resultado de teste, evidência, percentual ou trabalho concluído.
5. Diferencie PLANEJADO, IMPLEMENTADO, VALIDADO NO FAKE e VALIDADO NO REAL. Aceitação em fila não é sucesso.
6. Não marque tarefa CONCLUÍDA sem cumprir todos os critérios e registrar a validação. Ausência de ambiente significa NÃO EXECUTADO.
7. Preserve dados e alterações do usuário; não esconda falhas, remova testes ou enfraqueça segurança para obter verde.
8. Não armazene tokens, cookies, chaves privadas, `.env` ou transcrições sensíveis no Git/APK/logs.
9. Mantenha offline e estados local, pendente, sincronizado, obsoleto, conflito e falha distinguíveis.
10. Execute apenas a tarefa ativa e seus pré-requisitos. Melhorias extras vão ao backlog; dependência externa ausente vai ao registro de bloqueios.

## Fontes e conflitos

Pedido explícito de Rafael prevalece dentro das regras do ambiente. Depois, siga este arquivo,
os guardrails e as decisões vigentes. O DOCX e a conversa são históricos; consulte
[origem e divergências](docs/planning/SOURCES.md). Não implemente o antigo “RIN Server”.
O contrato atual é rascunho: exemplos novos não provam que a plataforma os suporta.

## Fechamento de cada tarefa

Revise todo o diff, valide conforme [matriz de qualidade](docs/quality/VALIDATION.md),
registre evidência pelo [modelo de entrega](docs/execution/DELIVERY_TEMPLATE.md),
atualize PROJECT_STATE e CHANGELOG; decisões arquiteturais vão a `docs/adr/`.
Informe arquivos alterados, comandos realmente executados, resultados e próximo ID.
Não faça push, publicação ou release apenas porque uma etapa os menciona; siga a autorização da tarefa.

## Comece aqui

Próxima tarefa inicial: **F00-T01** em [F00](docs/roadmap/F00-preparacao.md).
Use [START_HERE](docs/execution/START_HERE.md) para iniciar ou retomar no Antigravity.
