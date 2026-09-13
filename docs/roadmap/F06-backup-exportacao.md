# F06 — Exportação aberta e restauração local

Estado: NÃO INICIADA. Este arquivo é especificação de trabalho, não relato de execução.

## Resultado esperado

Permitir recuperar registros locais e transportar dados sem credenciais.

## Entrada e leitura

- Dependências: F05 concluída.
- Referências: IMPLEMENTATION, VALIDATION Q08/Q10 e limites de autoridade.
- Aplicam-se [protocolo](../execution/PROTOCOL.md), [guia técnico](../planning/IMPLEMENTATION.md) e [validação](../quality/VALIDATION.md).
- Escopo: Export/import de dados móveis e diagnóstico inicial; memória canônica permanece na plataforma.

## Tarefas em ordem

### F06-T01 — Definir formato versionado

- [ ] CONCLUÍDA somente após aceite e evidência.
- Execução: Especificar JSON legível de projetos locais, checkpoints, decisões locais e referências, com schemaVersion e origem. Excluir credenciais, chaves, approval payload e comandos executáveis. Definir limites de tamanho e versões suportadas.
- Entrega: Schema e exemplos válidos/inválidos de exportação.
- Aceite verificável: Validador detecta campos proibidos, relações quebradas e versão não suportada; fixture explicitamente sintética.

### F06-T02 — Implementar exportação pelo seletor do sistema

- [ ] CONCLUÍDA somente após aceite e evidência.
- Execução: Permitir destino escolhido pelo usuário e descrição do conteúdo. Tratar cancelamento/sem espaço/erro sem arquivo anunciado como completo. Cache remoto exportável deve ser rotulado snapshot, nunca autoridade.
- Entrega: Fluxo de exportação local e feedback real.
- Aceite verificável: Arquivo realmente criado, íntegro e reimportável; não exporta secrets nem afirma backup do PC.

### F06-T03 — Implementar importação com prévia

- [ ] CONCLUÍDA somente após aceite e evidência.
- Execução: Validar arquivo inteiro antes de escrever, mostrar contagem/conflitos e aplicar transação. Definir comportamento para IDs duplicados sem overwrite silencioso. Importar nunca reativa outbox ou reaproveita identidade de dispositivo.
- Entrega: Prévia, importação e tratamento de falhas.
- Aceite verificável: Import inválido não altera banco; duplicata exige política visível; repetir import não duplica inadvertidamente.

### F06-T04 — Provar restauração em instalação limpa

- [ ] CONCLUÍDA somente após aceite e evidência.
- Execução: Exportar dados conhecidos, instalar em ambiente limpo, importar e comparar conteúdo/relações. Verificar cancelamento, truncamento, tamanho excessivo e falha de armazenamento.
- Entrega: Evidência de round trip e checklist de exclusão de dados sensíveis.
- Aceite verificável: Todos os registros previstos restauram com referências; cache, se incluído, permanece obsoleto e requer nova confirmação.

## Gate de saída

Export/import validado em instalação limpa, sem dados sensíveis e sem reexecução de comandos.

Demonstração: Recuperar três projetos e seus checkpoints num segundo ambiente a partir de arquivo escolhido.

## Limite e bloqueio

Backup local não é backup da AI Workstation; nunca importar credencial ou aprovação para “restaurar sessão”.

## Registro e próxima fase

Use o [modelo de entrega](../execution/DELIVERY_TEMPLATE.md), aplique AUDIT e atualize PROJECT_STATE/CHANGELOG.
Não marque a fase concluída apenas por escrever os arquivos. A sequência completa está no [roadmap](../../ROADMAP.md).
