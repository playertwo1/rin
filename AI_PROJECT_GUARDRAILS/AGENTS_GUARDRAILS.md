# Bloco para AGENTS.md — Execution Guardrails

Todos os agentes que trabalharem neste projeto devem seguir obrigatoriamente:

- `WATCHDOG.md` durante planejamento e execução;
- `AUDIT.md` antes de considerar alterações relevantes concluídas.

## Regra operacional

Antes de editar:
1. Leia este `AGENTS.md`.
2. Leia `WATCHDOG.md`.
3. Entenda o pedido e examine o estado atual do projeto.

Durante a execução:
- mantenha-se estritamente dentro do objetivo solicitado;
- preserve comportamento existente sempre que possível;
- prefira mudanças pequenas, incrementais e reversíveis;
- não transforme suposições em fatos;
- não execute mudanças destrutivas ou arquiteturais desnecessárias;
- não reverta trabalho existente do usuário;
- não esconda erros para fazer testes ou builds passarem.

Antes de concluir:
1. revise o diff completo;
2. execute as validações disponíveis;
3. aplique o checklist de `AUDIT.md`;
4. procure regressões e mudanças fora do escopo.

Uma tarefa não está concluída apenas porque o código foi escrito.

Ela está concluída quando:
- o objetivo solicitado foi atendido;
- a solução foi validada;
- não existem regressões conhecidas;
- não existem alterações inexplicáveis no diff.

## Anti-drift

Se durante uma tarefa o agente perceber novas melhorias, refatorações ou funcionalidades que não sejam necessárias para cumprir o objetivo atual, NÃO deve implementá-las automaticamente.

Registre ou sugira a melhoria separadamente.

Primeiro termine corretamente o que foi solicitado.

## Incerteza

Quando houver dúvida entre:

A) fazer uma mudança ampla baseada em suposição;

B) preservar o estado atual;

prefira B.

Investigue antes de modificar.

## Princípio

READ → UNDERSTAND → PLAN → CHANGE → TEST → AUDIT → FINISH

Nunca:

ASSUME → CHANGE EVERYTHING → HOPE IT WORKS
