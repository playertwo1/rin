# Guia técnico de implementação Android

Este guia especifica o caminho pretendido. Diretórios, tipos e testes abaixo são previstos
até que a fase correspondente registre sua criação. Não são APIs existentes.

## Stack e dependências

A stack já definida é Kotlin, Compose, Material 3, MVVM, Room, WorkManager,
Keystore e WorkstationGateway. F00 deve verificar ambiente e documentação oficial das
versões selecionadas, registrar compatibilidade JDK/Gradle/AGP/Kotlin/Compose/Room
e usar catálogo de versões fixas. Não usar “latest” nem inventar versão compatível.
Escolher Retrofit/OkHttp ou Ktor Client por ADR; uma única pilha HTTP basta.
Escolher injeção de dependências simples e testável; framework extra exige justificativa.
Package, minSdk e targetSdk ainda não foram decididos; package precisa permanecer estável
antes do primeiro APK distribuído. Inspecionar o aparelho alvo sem presumir modelo/versão.

## Estrutura prevista

| Caminho | Responsabilidade | Introdução |
|---|---|---|
| app/ | Inicialização, navegação, composição de dependências e variantes | F01 |
| core/model/ | Modelos e interfaces de domínio sem Android/rede | F01–F03 |
| core/network/ | Gateway, DTOs, mapeamento, fake e cliente real isolados | F02 |
| core/database/ | Room, DAOs, transações, migrações e repositories | F03 |
| core/ui/ | Componentes, tema e estados visuais comuns | F01 |
| feature/projects/ | Home e lista | F04 |
| feature/projectdetail/ | Detalhe, resumo e fatos conectados | F04–F09 |
| feature/checkpoints/ | Registro local e histórico | F05 |
| feature/workstation/ | Conexão, capacidade, dispositivo e pareamento | F02/F08 |
| feature/approvals/ | Caixa, prévia e decisão | F12 |
| feature/sessions/ | Observação e intenções de sessão | F11 |
| feature/handoff/ | Prévia e acompanhamento AI Shift | F13 |
| contracts/ | Cópia versionada do contrato aceito e fixtures identificadas | F02 |
| docs/evidence/ | Registros reais de entregas e gates | Todas |

Crie módulos apenas quando forem usados na fase; não gere dezenas de módulos vazios.
UI depende de ViewModel/use cases; domínio define interfaces; dados implementam.
Componha tudo em app. Não permita Compose importar DTO HTTP ou UI executar DAO direto.
Uma interface de repositório distingue cache persistido de acesso remoto por gateway.

## Modelo local proposto

| Tipo | Campos mínimos e regra |
|---|---|
| Project | id local estável, remoteId opcional, workstationId opcional, nome, objetivo, prioridade, estado local, timestamps |
| Checkpoint | id, projectId, resumo factual, próximo passo, bloqueios, referências, createdAt |
| DecisionDraft | id, projectId, pergunta, opções, escolha opcional, confirmação local e timestamps |
| SyncMetadata | entidade, estado, versão remota opcional, lastConfirmedAt, erro classificado |
| EventCache | eventId, origem, cursor/ordem conforme contrato, tipo, occurredAt, payload validado |
| OutboxEntry | commandId, intenção tipada, entidade, versão base, payload imutável, estado de envio e timestamps |

Campos wire, enums e serialização dependem do contrato aceito. Isto não publica schema de servidor.
Nunca usar caminho absoluto do PC como identificador local de projeto.
Caches são chaveados pela identidade da workstation + ID remoto; projetos locais não têm
remoteId até vinculação confirmada. Trocar workstation não pode misturar caches ou pendências.
Git, testes e sessão remotos são fatos da plataforma, não editáveis como se fossem notas locais.

## Estado e evidências

- UTC para persistência/transporte; apresentação local. Injete relógio e gerador de IDs nos testes.
- “Sem informação” difere de zero, saudável, concluído e sem bugs.
- “Última confirmação” é sucesso de leitura/evento válido, não o horário de abrir a tela.
- Status de transporte, frescor do cache e resultado de negócio são dimensões separadas.
- Evento terminal pode afirmar succeeded sem artefato verificável: exibir “conclusão informada,
  evidência indisponível”, sem selo de resultado verificado. Não esconder o estado recebido.
- Checkpoint escrito pelo usuário é registro local; não prova execução de teste/commit.
- Progresso percentual só se a plataforma fornecer total e etapas confirmadas com semântica definida;
  padrão da 1.0 é texto de etapas e estados.

## Persistência e sincronização

A UI observa Room. Refresh usa gateway → valida DTO → transação de persistência → UI.
Atualize eventos, projeção e cursor na mesma transação; não avance cursor antes do commit.
Deduplicação por chave estável; cursor é opaco até o contrato definir sua semântica.
Evento desconhecido não produz sucesso inferido. Versão incompatível bloqueia escrita e mostra diagnóstico.
Lacuna/expiração do cursor exige ressincronização documentada, preservando rascunhos/outbox.
Evite last-write-wins baseado no relógio do celular. Conflito conserva base, local e remoto.

## Política de escrita

- F00–F09: dados locais editáveis e plataforma em leitura; não sincronizar notas por endpoint inventado.
- F10: apenas intenções de metadados publicadas e idempotentes entram em outbox persistente.
- Sessões, cancelamentos, aprovações e handoffs exigem confirmação online atual e capacidades.
  Nunca reproduzir automaticamente uma ação sensível armazenada durante offline.
- Em timeout após envio, o resultado é desconhecido: consultar/reconciliar o commandId antes
  de repetir. Repetição só mantém o mesmo ID e payload se o contrato permitir.
- Uma decisão local não se transforma em aprovação operacional.
- Cancelar uma pendência local não prova que o servidor cancelou um comando já recebido.

## Segurança por camadas

TLS e confiança do dispositivo são obrigatórios para cliente real. Não criar trust-all,
desativar hostname verification nem embutir certificado privado/chave/token de provedor.
O método de bootstrap e renovação precisa de contrato aceito em F08.
Keystore guarda material de chave; documentar onde e como a credencial de dispositivo é protegida.
Backups e diagnósticos excluem credenciais, material de chave e payloads sensíveis.
Logs de rede não imprimem Authorization nem corpo de aprovação.
Conteúdo de artefatos e manifestos é texto não confiável: sem execução, intents arbitrários
ou abertura automática de URLs. Abrir somente referências validadas e autorizadas.

## Limites dos simuladores

Fake determinístico usa relógio e fixtures fixos; permite reset, falha, atraso e duplicação.
Fixture server/mock HTTP existe somente para testes, sem autenticação de produção, Git ou subprocessos.
Modo real nunca cai no fake para esconder falha. Build real deve falhar se selecionar fake em produção.
Uma distribuição demo, se criada, deve ter nome/identificador visual distinto e não se apresentar como 1.0 real.
