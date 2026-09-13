# F05 — Checkpoints, decisões locais e Onde parei

Estado: EM ANDAMENTO. Este arquivo é especificação de trabalho, não relato de execução.

## Resultado esperado

Retomar o projeto por fatos persistidos, sem depender do histórico de chat.

## Entrada e leitura

- Dependências: F04 concluída.
- Referências: SOURCES (DOCX 4.3 e US003/US010), IMPLEMENTATION.
- Aplicam-se [protocolo](../execution/PROTOCOL.md), [guia técnico](../planning/IMPLEMENTATION.md) e [validação](../quality/VALIDATION.md).
- Escopo: Histórico local, próximo passo, bloqueios e resumo determinístico; sem síntese por LLM.

## Tarefas em ordem

### F05-T01 — Criar registro de checkpoint

- [x] CONCLUÍDA com evidência em [docs/evidence/F05-T01-2026-09-13.md](../evidence/F05-T01-2026-09-13.md).
- Execução: Formulário com resumo factual, próximo passo, bloqueios e referências opcionais. Persistir e ordenar com IDs/timestamps estáveis. Corrigir registro com histórico claro; não apresentar relato manual como teste executado.
- Entrega: feature/checkpoints e telas de histórico.
- Aceite verificável: Criar, editar e reabrir checkpoint preserva conteúdo; estado vazio explica como registrar o primeiro.

### F05-T02 — Registrar decisões locais

- [ ] CONCLUÍDA somente após aceite e evidência.
- Execução: Permitir pergunta, opções, escolha e justificativa. Separar rascunho de escolha confirmada localmente; não equiparar a aprovação de ação remota. Referenciar decisão no projeto/checkpoint.
- Entrega: DecisionDraft e fluxo de confirmação local.
- Aceite verificável: Escolha não confirmada não aparece como decisão aceita; nenhuma chamada de aprovação é enviada.

### F05-T03 — Implementar resumo por fatos

- [ ] CONCLUÍDA somente após aceite e evidência.
- Execução: Definir ordem determinística: próximo passo, bloqueios, último checkpoint, decisões e última confirmação. Cada afirmação aponta à origem local/evento. Ausência de fatos gera orientação para registrar, sem inventar atividade.
- Entrega: Use case de Onde parei e componentes de origem/evidência.
- Aceite verificável: Fixtures de projeto vazio, bloqueado, sem próximo passo e stale produzem resultados esperados com relógio fixo.

### F05-T04 — Provar retomada após perda de processo

- [ ] CONCLUÍDA somente após aceite e evidência.
- Execução: Criar checkpoints/decisões, encerrar processo, abrir offline e navegar às fontes de cada frase. Verificar ordenação em timestamps iguais e fuso local. Preparar medição futura sem alegar espera de 24/72h.
- Entrega: Teste de retomada local e registro de referências.
- Aceite verificável: Resumo reconstruído só do banco, sem chat, sem progresso inventado e sem decisão não confirmada.

## Gate de saída

Onde parei reproduzível e rastreável após reinício; projeto local útil diariamente.

Demonstração: Abrir projeto sem rede, ler próximo passo e tocar em uma afirmação para encontrar o checkpoint de origem.

## Limite e bloqueio

Não preencher lacunas com texto gerado; evento ou evidência ausente deve continuar ausente.

## Registro e próxima fase

Use o [modelo de entrega](../execution/DELIVERY_TEMPLATE.md), aplique AUDIT e atualize PROJECT_STATE/CHANGELOG.
Não marque a fase concluída apenas por escrever os arquivos. A sequência completa está no [roadmap](../../ROADMAP.md).
