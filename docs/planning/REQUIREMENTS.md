# Requisitos e rastreabilidade até 1.0

Status inicial de todos os requisitos: NÃO IMPLEMENTADO / NÃO VALIDADO.
As fontes históricas valem apenas após resolver a fronteira atual descrita em [SOURCES](SOURCES.md).
A divisão abaixo consolida requisitos existentes; detalhes operacionais são especificados pelo plano.

| ID | Resultado exigido | Fonte | Fases / tarefas principais | Aceite e ambiente exigido |
|---|---|---|---|---|
| R01 | Build e arquitetura Android substituível | ARCHITECTURE; AGENTS original | F00–F03 | Build + emulador + contrato; nenhuma dependência de provedor na UI |
| R02 | Home e projetos/checkpoints persistentes | ROADMAP anterior F1 | F03–F05 | Três projetos locais sobrevivem ao restart, CRUD completo |
| R03 | Onde parei rastreável | DOCX 4.3, US003 | F05-T03/T04; F09; F17 | Frases com origem; retomada real até 60s no piloto |
| R04 | Git, tarefas, bugs, testes, decisões, próximos passos e saúde/recursos/serviços da workstation | README; ROADMAP anterior F3; DOCX US002/US010 | F09-T02; F10 | Fatos reais correspondem à plataforma; edits só allowlisted |
| R05 | Parear e revogar aparelho | DOCX US001/US011; contrato atual | F08; F12; F16 | Protocolo real, TLS, revogação, credencial protegida |
| R06 | Offline e sync sem perda/duplicação | ARCHITECTURE; ROADMAP F2/F3 | F07; F09; F10 | Cursor/transação/restart/conflito verificados fake e real |
| R07 | Sessão observável e comandos tipados | DOCX US004/US005; contrato | F11 | Início e cancelamento reais com recibo e evidência final |
| R08 | Aprovar/rejeitar payload exato | DOCX US007; contrato | F12 | Ação válida e rejeições de hash/replay/expiração no real |
| R09 | AI Shift com aceite do destino | DOCX US008/US009; D011 | F13 | Dois adaptadores reais + manifesto/ack + recuperação |
| R10 | Notificar sem vazamento e duplicação | README; DOCX US006 | F14 | Evento real, permissão negada, deep link, privacidade |
| R11 | Backup/export e diagnóstico seguros | ROADMAP F1; DOCX US012 | F06; F16-T03 | Restauração limpa e diagnóstico sem credencial/dado indevido |
| R12 | Nunca declarar falso sucesso | README; D012/D013; guardrails | F02–F18 | 202/timeout/evidência ausente/unknown não viram sucesso |
| R13 | Experiência acessível e móvel | ROADMAP F1; DOCX 4 | F04; F15 | TalkBack/fonte ampliada/estados/navegação demonstrados |
| R14 | Atualizar e recuperar sem perder dados | DOCX 11.4; ROADMAP F7 | F03; F16; F18 | APK assinado, upgrade e migração/restauração testados |
| R15 | Piloto estável e metas observadas | DOCX 1.4/9; ROADMAP F7 | F17 | 14 dias reais; 24/72h; dois projetos; nenhum falso sucesso |
| R16 | Segurança e fronteira da plataforma | README; D004/D006/D010 | F08; F11–F13; F16 | Sem backend/shell/provedor no APK; negativos ponta a ponta |

## Histórias de usuário da fonte original

US001 → R05; US002 → R04; US003 → R03; US004 → R07; US005 → R07;
US006 → R10; US007 → R08; US008/US009 → R09; US010 → R02/R04;
US011 → R05; US012 → R11.
As histórias mantêm o objetivo de produto; implementação de Git/execução/políticas é externa.

## Cobertura de risco por fase

F00–F02 impedem stack/API inventadas; F03–F07 protegem persistência e recuperação;
F08–F10 protegem identidade e autoridade; F11–F13 protegem comandos e confirmações;
F14–F16 cobrem lifecycle, UX e segurança; F17–F18 impedem release sem uso real.

## Como registrar comprovação

Ao implementar, adicionar abaixo uma linha por requisito e cenário:
ID, tarefa, revisão, ambiente, artefato de evidência, resultado e limitação.
Não preencher “PASS” apenas por encontrar o nome de uma classe no código.
Requisito com simulação e real deve manter os dois resultados separados.
Sem evidência, o requisito permanece aberto.
