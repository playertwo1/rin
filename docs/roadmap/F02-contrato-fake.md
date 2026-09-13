# F02 — Contrato móvel e gateway determinístico

Estado: NÃO INICIADA. Este arquivo é especificação de trabalho, não relato de execução.

## Resultado esperado

Provar a fronteira Android → WorkstationGateway com respostas reproduzíveis.

## Entrada e leitura

- Dependências: F01 concluída; rascunho INTEGRATION_CONTRACT lido.
- Referências: INTEGRATION_CONTRACT, IMPLEMENTATION e EXT-01/03/08.
- Aplicam-se [protocolo](../execution/PROTOCOL.md), [guia técnico](../planning/IMPLEMENTATION.md) e [validação](../quality/VALIDATION.md).
- Escopo: Contratos candidatos e fake para consumo Android. Nenhum runtime de agente.

## Tarefas em ordem

### F02-T01 — Materializar o contrato candidato

- [ ] CONCLUÍDA somente após aceite e evidência.
- Execução: Criar schemas e exemplos positivos/negativos para envelope, health, projeto/lista/detalhe, evento e erro a partir do rascunho. Marcar campos adicionais e perguntas em aberto. Versionar o candidato local; não afirmar acordo da plataforma.
- Entrega: contracts/README.md, schemas e fixtures sintéticas identificadas.
- Aceite verificável: Validador aceita fixtures válidas, rejeita obrigatórios ausentes/data+error inválidos; exemplos não contêm dados privados.

### F02-T02 — Definir interfaces e mapeamento

- [ ] CONCLUÍDA somente após aceite e evidência.
- Execução: Criar WorkstationGateway para health, lista/detalhe e eventos; DTOs ficam em dados/rede. Modelar resultado, erro, indisponibilidade e capability desconhecida sem lançar tudo como erro genérico. Mapear ao domínio sem referência de provedor.
- Entrega: Interfaces em core/model e implementações/mappers em core/network.
- Aceite verificável: UI/domínio não importam Retrofit/Ktor/DTOs; erro desconhecido é exibível e não vira sucesso.

### F02-T03 — Implementar fake controlável

- [ ] CONCLUÍDA somente após aceite e evidência.
- Execução: Criar cenários vazio, três projetos sintéticos, offline, erro, dado stale, capability ausente, eventos repetidos e versão incompatível. Injetar relógio, IDs e cenário; resetar entre testes. Marcar conexão como SIMULADA na tela.
- Entrega: FakeWorkstationGateway e tela mínima de saúde/lista via gateway.
- Aceite verificável: Mesma entrada/reset produz mesma sequência; nenhum acesso a rede/CLI/Git real; identidade do modo sempre visível.

### F02-T04 — Testar contrato e fronteira visual

- [ ] CONCLUÍDA somente após aceite e evidência.
- Execução: Rodar o mesmo conjunto de expectativas de consumidor contra fake e preparar runner para cliente real futuro. Testar lista/detalhe/erro e não apenas igualdade de constantes. Demonstrar troca por dependência injetada sem editar UI.
- Entrega: Testes de contrato do consumidor e registro do primeiro caminho vertical.
- Aceite verificável: Contrato candidato validado no fake, navegação mostra lista e detalhe consistentes; plataforma real explicitamente NÃO VALIDADA.

## Gate de saída

Primeiro caminho vertical Android → fake → lista/detalhe comprovado e contrato candidato verificável.

Demonstração: Trocar cenário determinístico de três projetos para erro e observar UI correta sem mexer no código de tela.

## Limite e bloqueio

Campo ausente no contrato deve ser proposta identificada; não inventar rota real para completar o fake.

## Registro e próxima fase

Use o [modelo de entrega](../execution/DELIVERY_TEMPLATE.md), aplique AUDIT e atualize PROJECT_STATE/CHANGELOG.
Não marque a fase concluída apenas por escrever os arquivos. A sequência completa está no [roadmap](../../ROADMAP.md).
