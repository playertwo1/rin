# Roadmap do RIN

O roadmap representa a ordem de execução. As fases terminam com demonstração e evidências, não apenas com código escrito.

## Fase Zero Fundação e redução de risco

Objetivo: provar integrações críticas antes de construir a experiência completa.

- Criar monorepo com Android, servidor, contratos e simuladores.
- Escolher Fastify ou Ktor por ADR e spike executável.
- Definir schemas versionados de projeto, sessão, evento, handoff, aprovação e erro.
- Criar simuladores de Codex, Claude e Antigravity.
- Validar Claw Orchestrator no sistema operacional alvo.
- Configurar lint, testes, builds e CI.
- Implementar `GET /health` e a primeira conexão Android servidor.

Critérios de saída:

- Repositório compila do zero.
- Android mostra a saúde do servidor.
- Simuladores emitem eventos reproduzíveis.
- Capacidades reais dos adaptadores estão documentadas.

## Fase Um Leitura do projeto e Onde parei

Objetivo: entregar valor sem permitir mutações remotas.

- Cadastrar projetos apenas sob raízes autorizadas.
- Ler branch, HEAD, working tree, diff, commits e arquivos não rastreados.
- Persistir projetos, eventos e snapshots.
- Criar telas Projetos, Detalhe, Atividade e Onde parei.
- Adicionar Room, estados offline e sincronização incremental.
- Sanitizar caminhos e conteúdo sensível.
- Testar primeiro em repositório pequeno e depois no projeto 360.

Critérios de saída:

- Reiniciar PC e celular não perde o estado.
- Alterações Git chegam ao Android em até três segundos na rede local.
- Nenhum diretório fora das raízes autorizadas é acessível.
- Onde parei aponta para fatos rastreáveis.

## Fase Dois Sessões e observabilidade

Objetivo: iniciar e acompanhar um agente com segurança.

- Implementar `AgentAdapter` e adaptador falso completo.
- Implementar o primeiro adaptador real.
- Criar fila serial por projeto e supervisor de processo.
- Publicar eventos sanitizados em tempo real.
- Implementar início, pausa quando suportada, cancelamento e timeout.
- Classificar espera do usuário, limite, autenticação e falhas de processo.
- Reconciliar processos órfãos após reinício.

Critérios de saída:

- Sessão real inicia pelo Android.
- Cancelamento encerra o processo e registra resultado.
- Falha nunca aparece como sucesso.
- A API não aceita comandos arbitrários.

## Fase Três Memória estruturada

Objetivo: tornar o projeto independente do chat do agente.

- Implementar tarefas, bugs, decisões, testes, artefatos e próximas ações.
- Exportar `PROJECT_STATE.md`, `DECISIONS.md`, `TASKS.json` e `BUGS.json` de forma atômica.
- Criar checkpoint e política de atualização da memória.
- Relacionar afirmações a eventos e evidências.
- Criar resumo Onde parei determinístico e redação opcional por IA.
- Testar reconstrução em instalação limpa.

Critérios de saída:

- Estado é reconstruído sem o histórico do chat proprietário.
- Decisões não confirmadas não entram no registro oficial.
- Exportações são versionadas, válidas e sem segredos.

## Fase Quatro Passar turno

Objetivo: transferir trabalho entre agentes de forma auditável.

- Definir Handoff Manifest v1.
- Solicitar checkpoint e congelar comandos concorrentes.
- Capturar Git, objetivo, alterações, testes, decisões, bloqueios e próximos passos.
- Validar segredos, conflitos, arquivos não rastreados e tamanho.
- Mostrar uma prévia no Android.
- Iniciar o destino com prompt canônico.
- Exigir confirmação de recebimento e compreensão.
- Preservar a origem quando o destino falhar.

Critérios de saída:

- Pacote possui manifesto e hashes.
- Destino confirma tarefa, branch, arquivos e próxima ação.
- Falha permite nova tentativa sem perder o estado anterior.
- Auditoria identifica origem, destino, aprovação e resultado.

## Fase Cinco Aprovações e endurecimento

Objetivo: permitir ações úteis sem criar um terminal remoto irrestrito.

- Implementar policy engine e aliases de comandos.
- Adicionar aprovação biométrica, expiração e hash do payload.
- Aplicar rate limiting e revogação de dispositivos.
- Redigir tokens, chaves e arquivos `.env` dos logs.
- Executar threat modeling e testes negativos.
- Implementar backup criptografado e modo somente leitura.

Critérios de saída:

- Aprovação não funciona para payload diferente.
- Path traversal e comandos proibidos são bloqueados.
- Logs móveis não expõem segredos.
- Leitura continua disponível quando adaptadores falham.

## Fase Seis Piloto pessoal

Objetivo: validar o RIN em uso diário.

- Testar com um projeto pequeno e com o 360.
- Executar retomadas após 24 e 72 horas.
- Medir tempo para entender estado e concluir handoff.
- Ajustar notificações e fricções.
- Criar pacote de diagnóstico.
- Validar atualização segura do servidor e APK.
- Congelar o MVP após duas semanas estáveis.

## Fase Sete Pós MVP

- Roteamento por capacidade, disponibilidade e tipo de tarefa.
- Fallback automático por limite comprovado.
- Loops Planner Coder Reviewer.
- Conselho multiagente em worktrees isoladas.
- Integração com issues, PRs e checks do GitHub.
- Acesso remoto opcional com modelo zero trust.
- Multi PC e equipe apenas após novo desenho de autorização.