# Início de trabalho no Antigravity

Este é o ponto de entrada manual. Não presuma que o editor carrega AGENTS.md automaticamente.
Abra a pasta `rin/` como raiz do projeto e entregue o texto abaixo ao Antigravity.
O pedido desta preparação foi produzir documentação; a implementação Android ainda não começou.

## Prompt para o primeiro ciclo

> Você é o implementador do RIN Android. Leia AGENTS.md, os três arquivos de
> AI_PROJECT_GUARDRAILS, README.md, PROJECT_STATE.md, ROADMAP.md,
> docs/ARCHITECTURE.md, docs/DECISIONS.md, docs/INTEGRATION_CONTRACT.md e
> docs/execution/PROTOCOL.md. Depois leia docs/roadmap/F00-preparacao.md.
> Inspecione o estado real do Git e confirme a raiz antes de editar.
> Execute F00-T01 e continue pelas tarefas elegíveis de F00, na ordem.
> Registre fatos observados, decisões propostas, resultados de comandos e bloqueios separadamente.
> Não crie backend ou runtime de agentes. Não trate o DOCX antigo como arquitetura atual.
> Não declare que algo foi testado sem ter executado o teste e guardado resultado verificável.
> Feche cada tarefa usando docs/execution/DELIVERY_TEMPLATE.md e aplique AUDIT.md.
> Esta missão termina no gate de F00 ou num bloqueio real; deixe o próximo ID explícito.

## Prompt de retomada

> Leia AGENTS.md e PROJECT_STATE.md. Confira se o estado escrito corresponde ao Git
> e às evidências existentes. Leia o último registro de entrega e a fase ativa inteira.
> Retome a primeira tarefa não concluída cujas dependências estejam satisfeitas.
> Informe o ID, o resultado esperado e a validação antes de editar.
> Continue dentro da fase autorizada. Não refaça tarefas concluídas sem evidência de regressão.
> Se faltar uma capacidade da AI Workstation, registre a dependência; não fabrique sua implementação.

## Como conceder uma fase seguinte

Diga “Execute a Fase Fxx do roadmap até o gate de saída, seguindo AGENTS.md”.
Isso autoriza o trabalho descrito naquela fase, respeitando dependências e limites.
Não é necessário pedir confirmação para cada arquivo ou escolha reversível dentro desse escopo.
Para executar várias fases, indique o intervalo; não avance por uma dependência externa não atendida.

## Primeiro resultado esperado

Um inventário real do ambiente, decisões Android justificadas e procedimento de build definido.
A primeira prova de app executável é F01; a primeira lista via fake é F02.
Os números 0.1.0 e 0.2.0 no changelog antigo são revisões da documentação,
não versões de APK já entregues.
