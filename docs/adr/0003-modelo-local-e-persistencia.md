# ADR 0003 — Modelo local de identidade, persistência Room e invariantes

## Contexto

A fase F03 estabelece a camada de persistência local do RIN Android.
O aplicativo é orientado a local-first e opera tanto de maneira autônoma e offline quanto conectado a uma ou mais AI Workstations na rede local.

Existem desafios centrais de modelagem:
1. Dois servidores distintos da AI Workstation (ou reinstalações do servidor) podem usar os mesmos IDs numéricos ou identificadores relativos de projetos (ex.: `proj-1` ou nome de repositório).
2. O aplicativo deve suportar projetos criados puramente no cliente móvel (`LOCAL`), que inicialmente não possuem representação remota.
3. Não se deve confundir o estado de sincronização e frescor (`SyncState`) com o estado de negócio do projeto (`ProjectStatus`).
4. Dados ou métricas ausentes da plataforma física (como cotas de GPU/tokens ou status de suítes de teste) não devem ser inferidos como zero ou aprovados (invariante de honestidade técnica).
5. Checkpoints e rascunhos de decisão não podem existir desvinculados de um projeto válido (integridade referencial).
6. Migrações futuras de esquema de banco de dados não podem apagar silenciosamente os dados e rascunhos do usuário (`fallbackToDestructiveMigration` proibido).

## Decisão

1. **Identidade Primária Local**: Cada entidade (`LocalProject`, `LocalCheckpoint`, `LocalDecisionDraft`) possui uma chave primária local única (`id: String` baseada em UUID v4 gerado no cliente).
2. **Isolamento de Origem Remota**:
   - Entidades vinculadas à Workstation registram explicitamente `workstationId: String?` e `remoteProjectId: String?`.
   - A unicidade e isolamento de cache remoto são garantidos pela combinação lógica `workstationId + remoteProjectId`.
   - Projetos criados localmente mantêm `workstationId = null`, `remoteProjectId = null` e `origin = DataOrigin.LOCAL`.
3. **Desacoplamento de Dimensões de Estado**:
   - `ProjectStatus`: `ACTIVE`, `PAUSED`, `COMPLETED`, `ARCHIVED`, `UNKNOWN`.
   - `SyncState`: `LOCAL_ONLY`, `SYNCED`, `PENDING_SYNC`, `STALE`, `CONFLICT`.
   - `lastConfirmedAt: Long?`: Timestamp UTC em milissegundos registrado apenas no momento em que um payload válido e confirmado pela Workstation é persistido.
4. **Integridade Referencial**:
   - `LocalCheckpoint` e `LocalDecisionDraft` utilizam `projectId` como chave estrangeira obrigatória para `LocalProject`.
5. **Preservação de Desconhecidos**:
   - Campos de cota e teste ausentes são tipados como anuláveis (`Float?`, `String?`) e permanecem `null` quando não reportados pela plataforma.
6. **Política de Persistência e Migrações**:
   - Room SQLite com KSP e schema JSON exportado (`app/schemas`).
   - Proibido `fallbackToDestructiveMigration()`. Atualizações de schema exigirão migrações incrementais testadas.

## Consequências

- Positivas:
  - Elimina risco de colisões entre múltiplas estações ou projetos locais e remotos.
  - UI reage com fidelidade ao status de sincronização sem poluir o status de negócio.
  - Segurança de dados: rascunhos do usuário nunca são apagados silenciosamente por atualização de schema.
- Negativas/Custos:
  - Exige mapeamento explícito entre DTOs de rede, entidades de banco e modelos de domínio.
  - Migrações exigem disciplina de versionamento de arquivos `.json` de schema no repositório.
