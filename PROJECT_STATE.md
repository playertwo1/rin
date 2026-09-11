# Estado do RIN

## Situação

- Fase: Zero
- Estado: documentação inicial concluída
- Implementação: não iniciada
- Próximo marco: caminho vertical Android para servidor com dados simulados
- Risco geral: integrações externas ainda não validadas

## Decisões confirmadas

- O aplicativo se chama RIN.
- Android será o controle remoto; agentes serão executados no computador.
- Git será a fonte da verdade do código.
- A memória do projeto pertencerá ao RIN.
- O MVP será single user e local first.
- Claw Orchestrator ficará atrás de um `AgentAdapter` se for aprovado no spike.
- O Android não terá acesso a um shell genérico.
- Handoff só termina depois do aceite do agente de destino.

## Próximas ações

1. Escolher a stack do servidor por ADR.
2. Validar Claw, Codex, Claude e Antigravity no ambiente alvo.
3. Criar monorepo e contratos versionados.
4. Criar simuladores determinísticos.
5. Implementar o primeiro caminho vertical.

## Bloqueios e dúvidas

- Sistema operacional alvo do primeiro servidor ainda precisa ser formalizado.
- Suporte real e estável do Claw às versões instaladas precisa ser testado.
- Interface automatizável do Antigravity precisa ser validada.
- Medição de cotas não deve ser prometida sem fonte oficial confiável.

## Última atualização

11 de setembro de 2026.