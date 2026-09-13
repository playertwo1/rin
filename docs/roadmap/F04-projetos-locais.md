# F04 — Home e gestão de projetos locais

Estado: CONCLUÍDA com gate de saída satisfeito. Evidências em F04-T01 a F04-T04.

## Resultado esperado

Entregar organização útil no celular antes da conexão real.

## Entrada e leitura

- Dependências: F03 concluída.
- Referências: README, IMPLEMENTATION e VALIDATION Q01/Q11.
- Aplicam-se [protocolo](../execution/PROTOCOL.md), [guia técnico](../planning/IMPLEMENTATION.md) e [validação](../quality/VALIDATION.md).
- Escopo: CRUD local de projeto, navegação e feedback de estado; Git remoto continua somente dado recebido.

## Tarefas em ordem

### F04-T01 — Construir Home e lista

- [x] CONCLUÍDA com evidência em [docs/evidence/F04-T01-2026-09-13.md](../evidence/F04-T01-2026-09-13.md).
- Execução: Exibir nome, prioridade, última atividade confirmada, bloqueio e próximo passo quando existirem. Diferenciar local de simulado/cache; separar decisões pendentes sem inventar contagens. Ordenação deve ser definida e testável.
- Entrega: Home e feature/projects observando repositório.
- Aceite verificável: Vazio, um projeto e lista longa renderizam; valores ausentes não viram zero/saudável.

### F04-T02 — Implementar criação e edição

- [x] CONCLUÍDA com evidência em [docs/evidence/F04-T02-2026-09-13.md](../evidence/F04-T02-2026-09-13.md).
- Execução: Validar nome obrigatório, preservar objetivo e prioridade e suportar cancelamento do formulário. Gerar ID persistente. Edição local não dispara endpoint nem altera Git do PC.
- Entrega: Formulário com ViewModel e testes de validação/persistência.
- Aceite verificável: Criar, editar, cancelar e reabrir funcionam; rotação não duplica submissão nem perde rascunho inesperadamente.

### F04-T03 — Construir detalhe e remoção local segura

- [x] CONCLUÍDA com evidência em [docs/evidence/F04-T03-2026-09-13.md](../evidence/F04-T03-2026-09-13.md).
- Execução: Exibir origem e estado; oferecer arquivamento e confirmação explícita para excluir registro local. Especificar impacto nos checkpoints e testar transação. Projeto remoto não oferece exclusão canônica por ação local.
- Entrega: feature/projectdetail e fluxo de arquivamento/exclusão local.
- Aceite verificável: Excluir registro local não faz comando remoto nem perde outro projeto; cancelar confirmação preserva dados.

### F04-T04 — Cobrir fluxos e acessibilidade inicial

- [x] CONCLUÍDA com evidência em [docs/evidence/F04-T04-2026-09-13.md](../evidence/F04-T04-2026-09-13.md).
- Execução: Testar CRUD, voltar, erro de persistência, teclado e nome longo. Verificar TalkBack básico, foco, labels e fonte ampliada. Não adiar erros/estados para polimento final.
- Entrega: Roteiro instrumentado/manual registrado, correções e gate.
- Aceite verificável: Três projetos locais persistem após reinício; ações principais são utilizáveis no emulador e claramente locais.

## Gate de saída

Home e CRUD local completos, persistentes e com estados acessíveis.

Demonstração: Cadastrar três projetos, editar um, arquivar outro, reiniciar e verificar conteúdo e ordem (SATISFEITO com evidência em [docs/evidence/F04-T04-2026-09-13.md](../evidence/F04-T04-2026-09-13.md)).

## Limite e bloqueio

Não representar projeto local como descoberto no PC; sem discovery real até contrato.

## Registro e próxima fase

Use o [modelo de entrega](../execution/DELIVERY_TEMPLATE.md), aplique AUDIT e atualize PROJECT_STATE/CHANGELOG.
Não marque a fase concluída apenas por escrever os arquivos. A sequência completa está no [roadmap](../../ROADMAP.md).
