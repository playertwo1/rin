# F03 — Modelo local e persistência Room

Estado: CONCLUÍDA em 2026-09-13. Evidências em [F03-T01](../evidence/F03-T01-2026-09-13.md), [F03-T02](../evidence/F03-T02-2026-09-13.md), [F03-T03](../evidence/F03-T03-2026-09-13.md) e [F03-T04](../evidence/F03-T04-2026-09-13.md).

## Resultado esperado

Separar dados locais e cache da workstation, preservando ambos após reinício.

## Entrada e leitura

- Dependências: F02 concluída.
- Referências: IMPLEMENTATION, ARCHITECTURE e VALIDATION.
- Aplicam-se [protocolo](../execution/PROTOCOL.md), [guia técnico](../planning/IMPLEMENTATION.md) e [validação](../quality/VALIDATION.md).
- Escopo: Project, Checkpoint, DecisionDraft, metadados de sync, DAOs e repositórios.

## Tarefas em ordem

### F03-T01 — Definir identidades e invariantes

- [x] CONCLUÍDA com evidência em [docs/evidence/F03-T01-2026-09-13.md](../evidence/F03-T01-2026-09-13.md).
- Execução: Modelar IDs locais e identidade workstation+remoteId, origem, timestamps e estados. Separar estado de negócio de frescor/sync. Decidir nulabilidade e relações; checkpoint precisa apontar para projeto existente.
- Entrega: Modelos, schema inicial e decisões justificadas.
- Aceite verificável: Projetos de duas origens com mesmo remoteId não se confundem; cota e teste ausentes continuam desconhecidos.

### F03-T02 — Implementar Room e repositórios

- [x] CONCLUÍDA com evidência em [docs/evidence/F03-T02-2026-09-13.md](../evidence/F03-T02-2026-09-13.md).
- Execução: Criar entities/DAOs e transações para projetos, checkpoints e rascunhos de decisão. Repository expõe observação ao domínio; não expor DAO na UI. Usar schema exportado e banco versionado.
- Entrega: core/database com schema versionado, repositórios e testes instrumentados.
- Aceite verificável: Inserir/editar/ler mantém dados e ordenação; falha de transação não deixa registros parciais.

### F03-T03 — Persistir cache do fake

- [x] CONCLUÍDA com evidência em [docs/evidence/F03-T03-2026-09-13.md](../evidence/F03-T03-2026-09-13.md).
- Execução: Fazer refresh buscar gateway e gravar na base antes da UI observar. Manter dados locais quando refresh falha. Marcar lastConfirmedAt só após resposta válida e preservada.
- Entrega: Fluxo gateway → repositório → Room → ViewModel.
- Aceite verificável: Fechar e reabrir app mantém cache e origem; offline exibe última confirmação sem marcar dado como atual.

### F03-T04 — Testar reinício e preparar migrações

- [x] CONCLUÍDA com evidência em [docs/evidence/F03-T04-2026-09-13.md](../evidence/F03-T04-2026-09-13.md).
- Execução: Testar fechar conexão, recriar processo e reler banco. Versionar schema inicial; migração só é testada quando existir versão anterior real/fixture legítima. Proibir fallback destrutivo silencioso e definir como testar futuras alterações.
- Entrega: Testes de persistência e política de migração em SETUP/ADRs.
- Aceite verificável: Dados sobrevivem a reinício; não há reset automático em erro. Não alegar teste de migração inexistente.

## Gate de saída

Projeto, checkpoint e cache persistem; identidades e autoridade dos dados são demonstradas.

Demonstração: Criar dados, encerrar app, iniciar offline e recuperar os mesmos IDs e conteúdo.

## Limite e bloqueio

Não corrigir incompatibilidade de banco apagando dados do usuário. Registrar limitação de armazenamento/ambiente.

## Registro e próxima fase

Use o [modelo de entrega](../execution/DELIVERY_TEMPLATE.md), aplique AUDIT e atualize PROJECT_STATE/CHANGELOG.
Não marque a fase concluída apenas por escrever os arquivos. A sequência completa está no [roadmap](../../ROADMAP.md).
