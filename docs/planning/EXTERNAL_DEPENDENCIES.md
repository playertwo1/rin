# Dependências externas e decisões abertas

Status inicial em 2026-09-13: NÃO VERIFICADAS. Nenhum endpoint foi testado contra a AI Workstation.
Este arquivo é uma lista de entregas necessárias, não um pedido enviado nem uma promessa da outra equipe.

## Entregas da AI Workstation

| ID | Entrega necessária | Evidência de disponibilidade | Bloqueia |
|---|---|---|---|
| EXT-01 | Contrato v1 de health, projetos, detalhe, erro e capabilities | Schema/revisão referenciada + respostas de serviço real | F08–F09 |
| EXT-02 | Pareamento, TLS, credencial de dispositivo, escopo, renovação e revogação | Protocolo aceito + teste positivo/negativo no PC e Android | F08 |
| EXT-03 | Eventos paginados, cursor, dedup, retenção, resync e transporte ao vivo | Replay após restart, cursor inválido e duplicatas verificados | F09 |
| EXT-04 | Escritas de metadados, vínculo de projeto local/remoto e controle de versão | Tipos allowlisted + idempotência + conflito reproduzível | F10 |
| EXT-05 | Consulta de comando/sessão e início/cancelamento tipados | Recibo, estado final, evidência e cancelamento auditado | F11 |
| EXT-06 | Consulta de aprovações, hash, expiração, decisão e auditoria | Payload adulterado/replay/expirado rejeitados no servidor | F12 |
| EXT-07 | Manifesto, preparação/execução de handoff, acknowledgement e recuperação | Dois adaptadores simulados e dois reais com aceite verificável | F13 e 1.0 |
| EXT-08 | Dados de tarefas, bugs, testes, decisões, Git, artefatos e telemetria de recursos/serviços sanitizados | Exemplos reais autorizados e rastreáveis | F09/F11 |
| EXT-09 | Matriz de agentes/capabilities e disponibilidade real | Revisão do adaptador, ferramenta e teste com data | F11/F13 |
| EXT-10 | Serviço no Galaxy Book/PC e procedimento de atualização/recuperação | Instalação real e reinício mantendo estado | F09/F16/F17 |

Campos de protocolo ainda ausentes no rascunho: paginação, semântica do cursor, versionamento
de entidades, status de comandos, códigos de erro, discovery, escrita de metadados, listagem de
aprovações, manifestos e bootstrap. Registrar contrato antes de criar cliente real dessas operações.

## Decisões e momento de fechamento

| ID | Decisão | Responsável/papel | Prazo técnico | Condição |
|---|---|---|---|---|
| OPEN-01 | Package, minSdk, targetSdk, toolchain e aparelho alvo | Implementador | F00 | **RESOLVIDA** na F00 via [ADR 0002](../adr/0002-identidade-e-toolchain.md) |
| OPEN-02 | Pilha HTTP e injeção de dependência | Implementador | F00/F02 | **RESOLVIDA** na F00 via [ADR 0002](../adr/0002-identidade-e-toolchain.md) |
| OPEN-03 | REST + SSE/WebSocket/polling e comportamento em background | Engenharia RIN + plataforma | F07/F09 | Replay confiável e limites Android demonstrados |
| OPEN-04 | Pareamento, bootstrap TLS e ciclo da credencial | Engenharia RIN + plataforma | F08 | Protocolo verificável, sem atalho trust-all |
| OPEN-05 | Conflito, vínculo e versão das escritas | Engenharia RIN + plataforma | F10 | Base/local/remoto preservados, sem overwrite silencioso |
| OPEN-06 | Distribuição interna, identidade final e guarda da chave de assinatura | Rafael + implementador | F16 | APK atualizável e credencial fora do Git |
| OPEN-07 | Licença | Rafael | Antes de distribuição pública | Texto escolhido, sem inventar autorização/licença |
| OPEN-08 | Acesso fora da LAN | Rafael + engenharia | Pós-1.0 | Não necessário para piloto local |
| OPEN-09 | Biometria e alternativa quando indisponível | Engenharia RIN + política da plataforma | F12 | Ações fortes bloqueiam se não há confirmação exigida |

Decisões de engenharia reversíveis podem ser resolvidas pelo implementador dentro da tarefa.
Decisão de identidade/distribuição não deve ser inventada como preferência de Rafael.

## Como registrar um bloqueio real

Use ID EXT/OPEN + tarefa: tentativa/data, resultado sanitizado, o que falta, responsável por papel,
próxima verificação possível e critério exato de liberação. Não invente prazo ou resposta de terceiro.
Exemplo: “F08-T02 bloqueada por EXT-02: não há protocolo de pareamento publicado na fonte X;
libera com schema/revisão e teste de troca de credencial”. Falta de acesso não prova que API não existe.
Não criar o serviço faltante em RIN. Tarefas de fake independentes podem continuar se autorizadas.
