# Critério de liberação RIN 1.0

Versão alvo: produto Android pessoal, um usuário, uma workstation, uso na LAN.
Nenhuma release foi produzida nesta preparação. F18 deve preencher um registro real,
sem checkboxes pré-marcados.

## Condições cumulativas

- [ ] F00–F17 com gates satisfeitos e links para evidências.
- [ ] Requisitos R01–R16 da matriz cobertos no ambiente exigido.
- [ ] Instalação limpa e atualização de APK assinado preservam projetos/checkpoints.
- [ ] Pareamento, revogação, expiração e incompatibilidade de API testados no real.
- [ ] Sessão real iniciada/acompanhada/cancelada com auditoria e confirmação do efeito.
- [ ] AI Shift com dois adaptadores reais, manifesto verificável e confirmação do destino.
- [ ] Sem falso sucesso, perda de dados ou execução fora de escopo no piloto.
- [ ] Duas semanas consecutivas de piloto estável concluídas; datas e registros existentes.
- [ ] Retomadas após 24 e 72 horas medidas no projeto pequeno e no 360.
- [ ] Retomada em até 60 segundos demonstrada nos roteiros do piloto.
- [ ] Alvo de latência de até 3 segundos atendido nos roteiros foreground na LAN; não prometer prazo em background.
- [ ] Notificações negadas/offline/bateria tratadas; limitações comunicadas.
- [ ] Sem achado CRITICAL/HIGH/MEDIUM relevante aberto; limitações menores identificadas.
- [ ] Build de release contém gateway real; demo/fake não é fallback de falha.
- [ ] Runbook, diagnóstico sanitizado, backup/restauração e recuperação de pareamento validados.
- [ ] Assinatura sob controle do responsável, versionCode crescente e checksums registrados.
- [ ] Licença decidida antes de qualquer distribuição pública.
- [ ] Autorização de distribuição/publicação existe e cobre o destino efetivo.

## Piloto e tempo real

O alvo original do DOCX é duas semanas estáveis, não duas semanas “simuladas”.
Testes com relógio virtual verificam lógica; não substituem dias de uso.
Registrar por dia: versões app/plataforma, aparelhos, projetos, roteiros executados, falhas,
tempo de retomada, handoffs aceitos/tentados e latência de eventos.
Comparar emissão/recepção com método que explicite sincronização e erro dos relógios.
Registrar todas as amostras e distribuição; não selecionar só medições favoráveis.
Se o alvo de retomada ou latência não for atendido no roteiro, manter F17 aberta e diagnosticar.
Alterar o alvo exige decisão explícita registrada, não interpretação silenciosa do implementador.
Incidente de perda/falso sucesso/ação indevida interrompe o gate; corrigir e iniciar
nova janela estável para a versão corrigida. Ajuste cosmético exige documentar impacto
e repetir checks afetados, sem fingir que dados antigos são da nova revisão.

## Pacote de release

F18 prepara `docs/releases/1.0.0.md` (caminho previsto) com revisão exata do código e
plataforma compatível, APK identificado, SHA-256, relatório de assinatura sem segredo,
instruções de instalação/upgrade, limitações, release notes e links para gates.
Não marcar tag 1.0.0 em commit diferente do artefato. Se não houver autorização para publicar,
o estado correto é “candidato validado, aguardando distribuição”, não “publicado”.
Se integração real ou piloto faltarem, entregar versão de desenvolvimento identificada;
não renomear demo como 1.0 para encerrar tarefa.
