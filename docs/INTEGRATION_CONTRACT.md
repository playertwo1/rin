# Contrato RIN ↔ AI Workstation

Status: rascunho v0.1  
Compatibilidade pretendida: `/api/v1`

## Princípio

O RIN envia **intenções tipadas** e apresenta estados. A AI Workstation executa, persiste e audita. Nenhum lado depende de classes internas do outro.

## Recursos mínimos

| Operação | Direção | Finalidade |
|---|---|---|
| `GET /health` | RIN → plataforma | Saúde, versão e capacidades |
| `GET /api/v1/projects` | RIN → plataforma | Projetos autorizados |
| `GET /api/v1/projects/{id}` | RIN → plataforma | Estado e evidências |
| `GET /api/v1/events?cursor=` | RIN → plataforma | Recuperação incremental |
| `POST /api/v1/commands` | RIN → plataforma | Intenção allowlisted e idempotente |
| `POST /api/v1/approvals/{id}/decision` | RIN → plataforma | Aprovar ou rejeitar payload imutável |
| canal de eventos | plataforma → RIN | Atualizações sanitizadas em tempo real |

## Envelopes comuns

Toda resposta inclui:

- `schemaVersion`;
- `requestId`;
- `occurredAt` em UTC;
- `data` ou `error`;
- versão/capacidades quando relevante.

Todo comando inclui:

- `commandId` idempotente;
- `projectId`;
- `commandType` allowlisted;
- `payload` tipado;
- `requestedAt`;
- identidade do dispositivo;
- contexto de aprovação quando necessário.

## Estados

Sessões: `queued`, `running`, `waiting_user`, `paused`, `succeeded`, `failed`, `cancelled`, `interrupted`.

Sincronização móvel: `local`, `pending`, `synced`, `stale`, `conflict`, `failed`.

Uma fila aceitar o comando não significa sucesso. O estado terminal precisa trazer evidência verificável.

## Aprovações

A prévia deve mostrar ação, alvo, risco, prazo e hash do payload. A decisão vale somente para o payload exibido e expira. A plataforma, não o app, é a autoridade da auditoria.

## Segurança

- TLS e pareamento de dispositivo.
- Escopos mínimos e revogação.
- Sem token de provedor no Android.
- Sem shell genérico.
- Conteúdo sanitizado antes de eventos/notificações.
- Cursor e repetição idempotente após reconexão.
- Comandos desconhecidos são rejeitados por padrão.

## Compatibilidade

Mudanças aditivas podem manter a mesma versão. Remoção, renomeação ou alteração semântica exige nova versão. O RIN deve consultar `capabilities` e degradar a interface quando um recurso não existir.

## Critério do primeiro contrato executável

O fake e a implementação real devem passar os mesmos testes de contrato para `health`, lista/detalhe de projetos, cursor de eventos e erro estruturado.
