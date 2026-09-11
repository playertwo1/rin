# Conversa consolidada sobre o RIN

Rafael procurava uma ideia de aplicativo Android com utilidade diária. Entre as propostas apareceu o Projeto Vivo, um gerenciador de projetos para vibe coding que mostraria onde o usuário parou, mudanças recentes, bugs, decisões, próximos passos e qual IA estava trabalhando.

Rafael perguntou se o aplicativo poderia alternar entre suas assinaturas. A resposta definiu a ideia central: o aplicativo não transfere uma sessão proprietária e não contorna cotas. Ele cria um handoff verificável para que Codex, Claude ou Antigravity continuem o mesmo trabalho. Esse processo recebeu o nome AI Shift.

Foi decidido que o histórico de uma IA não poderia ser a memória oficial. Git e o estado mantido pelo aplicativo seriam a base permanente; os agentes seriam trabalhadores substituíveis.

Uma pesquisa identificou projetos próximos, incluindo Handoff, Claw Orchestrator, Hydra, Agent Handoff, superharness e context-handoff. A conclusão foi evitar reconstruir toda a orquestração. O Claw deve ser avaliado como motor, mas permanecer atrás de um adaptador para que o produto não fique preso a ele.

A arquitetura proposta separou duas partes:

- Android mobile first para visualização, decisões, aprovações e Passar turno.
- Servidor no PC para Git, memória, políticas, sessões e integração com agentes.

O MVP foi reduzido a seis provas:

1. Descobrir projetos autorizados.
2. Mostrar o estado do Git.
3. Iniciar agentes por adaptadores.
4. Transmitir eventos em tempo real.
5. Transferir o trabalho entre agentes.
6. Preservar Onde parei após reinícios.

Também foram definidos princípios de segurança: o Android não acessa diretamente o terminal; ações perigosas exigem aprovação; comandos fora da allowlist são bloqueados; o sistema nunca anuncia sucesso sem evidência.

Após aprovação do conceito e do caminho visual, Rafael escolheu o nome RIN e solicitou a documentação completa e o roadmap para iniciar o desenvolvimento com Codex Astra.