# Prompts dos mockups RIN — proposta 01

Data: 2026-09-13. Método: geração de imagens integrada (image_gen), uma chamada por tela.
Estas imagens são propostas visuais com dados sintéticos; não são capturas de um app implementado.
Não alteram decisões de produto nem comprovam capabilities da AI Workstation.

## Direção compartilhada

```text
Use case: ui-mockup.
Create ONE high-fidelity Android mobile app mockup for RIN, the personal Android control panel for AI Workstation. This is a proposed UI, not an existing app screenshot.
Composition: portrait 2:3 image, one straight-on modern Android phone filling almost the entire frame, subtle thin dark frame, complete screen visible, very narrow neutral outer margin, no hands, no perspective, no surrounding desk, no collage.
Design: coherent polished Material 3 dark theme, charcoal background, slightly lighter elevated surfaces, white and muted grey typography, restrained Material-style pale lavender primary buttons, amber only for warnings, soft green only for confirmed states. Clean simple line icons, generous spacing, clear hierarchy, large perfectly readable Portuguese UI text, realistic touch targets. Compact Android status bar at top and navigation gesture at bottom. No charts unless explicitly asked, no neon, no sci-fi.
Every screen has tiny discreet text "CONCEITO • DADOS DE EXEMPLO" immediately below app header. This is an illustrative mockup, not evidence of real implementation.
Use only the requested content. Never show arbitrary shell, billing, team collaboration, invented quotas, invented percentages, or direct API tokens. No external provider logos. RIN wordmark in plain typography.
```

## Prompts individuais

### 01 — Home — visão do dia

Prompt enviado: direção compartilhada acima, seguida deste texto literal.

```text
Screen 01 of 10. Home dashboard. Top wordmark "RIN", greeting "Bom dia, Rafael", subtitle "Seu trabalho, em um só lugar." Small green-dot connection chip "Workstation online". Prominent hero card "Onde parei?" project "360" with text "Revisar o fluxo de login" and lavender button "Retomar projeto". Section "Aguardando você" with one amber card "1 aprovação pendente" and "Revisar envio para o GitHub". Section "Projetos" with two compact project rows: "360" — "Aguardando revisão"; "Projeto pessoal" — "Sem sessão ativa". Bottom navigation four destinations "Hoje", "Projetos", "Atividade", "Ajustes", Hoje selected. Keep text concise, no fake progress ring.
```

### 02 — Lista de projetos

Prompt enviado: direção compartilhada acima, seguida deste texto literal.

```text
Screen 02 of 10. Projects catalog screen. Header "Projetos", small text "3 projetos", search field "Buscar projeto", filter chips "Todos", "Em andamento", "Bloqueados". Three spacious stacked project cards: "360", tag "Aguardando você", subtitle "Revisar fluxo de login", metadata "Atualizado há 2 min"; "Projeto pessoal", tag "Em andamento", subtitle "Acompanhar validação", metadata "Atualizado há 8 min"; "Ideias", tag "Local", subtitle "Registrar próximo passo", metadata "Salvo no celular". Card metadata uses clear source labels. Floating lavender plus button "Novo projeto". Bottom nav Hoje/Projetos/Atividade/Ajustes with Projetos active. No invented completion percentages.
```

### 03 — Projeto Vivo — onde parei

Prompt enviado: direção compartilhada acima, seguida deste texto literal.

```text
Screen 03 of 10. Project detail and deterministic resumption. Back arrow, title "360", subtitle "Projeto Vivo". Small tags "main" and "Sincronizado". Main heading "Onde parei?" card: "O fluxo de login está pronto para revisão." Secondary label "Fonte: checkpoint de hoje, 14:20". Section "Próximo passo" with text "Validar a recuperação de senha". Section "O que mudou" two compact rows "Tela de login atualizada" and "Testes de autenticação registrados", each tiny source-link icon. Amber card "Bloqueio" — "Decidir o texto da mensagem de erro". Bottom sticky primary button "Continuar projeto", secondary "Novo checkpoint". No fake percentage, no chat box, no terminal. All sample facts linkable visually.
```

### 04 — Novo checkpoint

Prompt enviado: direção compartilhada acima, seguida deste texto literal.

```text
Screen 04 of 10. Checkpoint creation form. Header with back arrow "Novo checkpoint", project chip "360", label "Registro local". Comfortable form with four fields: "O que foi feito" filled "Revisei o fluxo de login"; "Próximo passo" filled "Testar recuperação de senha"; "Bloqueios" filled "Definir mensagem de erro"; "Evidências" with outlined attachment row "Adicionar referência". Small explanatory note "Um registro manual não confirma a execução de testes." Bottom sticky lavender "Salvar checkpoint", quiet text button "Cancelar". No keyboard occupying the screen, no fake save success, no remote approval UI.
```

### 05 — Conexão com a workstation

Prompt enviado: direção compartilhada acima, seguida deste texto literal.

```text
Screen 05 of 10. Workstation connection details screen. Back arrow header "Workstation". Prominent simple laptop line icon with "Galaxy Book", status "Online", subtitle "Última confirmação: agora". Card "Conexão segura" with rows "Dispositivo pareado" check, "Acesso" value "Somente leitura", "Rede" value "Local". Section "Capacidades disponíveis" compact chips "Projetos", "Eventos", "Sessões", "Aprovações". Section "Agentes" rows "Codex" green "Disponível", "Claude" neutral "Sem sessão", "Antigravity" amber "Indisponível". Quota line "Cotas: sem informação". Bottom outlined button "Testar conexão", discreet "Gerenciar pareamento". Statuses explicitly illustrative under standard concept label. No secrets, no IP necessary, no arbitrary CPU charts.
```

### 06 — Sessão em andamento

Prompt enviado: direção compartilhada acima, seguida deste texto literal.

```text
Screen 06 of 10. Live session detail screen. Back arrow title "Sessão", project label "360". Agent card "Codex" and blue/lavender state chip "Executando", task "Validar recuperação de senha". Below concise label "Etapas confirmadas" with vertical timeline: checked "Contexto carregado" at "14:32", checked "Arquivos analisados" at "14:33", currently active "Validação em andamento" at "14:34". Panel "Resultado" showing "Ainda não confirmado". Lower text "Acompanhe os eventos da workstation." Bottom outlined amber button "Solicitar cancelamento". Do not say cancelled/succeeded, no invented numeric progress, no terminal, no pause control unless capability specified (not specified).
```

### 07 — Revisão de aprovação

Prompt enviado: direção compartilhada acima, seguida deste texto literal.

```text
Screen 07 of 10. Sensitive action approval detail screen, very readable safe decision UX. Back arrow header "Revisar aprovação". Amber risk chip "Risco moderado". Large title "Enviar alterações ao GitHub". Summary rows "Projeto" "360", "Ação" "Push", "Destino" "origin / main", "Alterações" "2 commits". Disclosure card "Prévia do conteúdo" with text "Ver alterações antes de decidir" and chevron. Compact monospaced row "Hash do payload" value "7f3a…91c2", expiry "Expira em 4 min". Note "A decisão vale apenas para esta prévia." Large lavender button with fingerprint icon "Confirmar com biometria", outlined secondary "Rejeitar". No check mark indicating already approved, no auto-approve, no destructive terminal actions.
```

### 08 — AI Shift — passar turno

Prompt enviado: direção compartilhada acima, seguida deste texto literal.

```text
Screen 08 of 10. AI Shift preparation and review screen. Header back arrow "Passar turno" and project "360". Elegant horizontal transfer row simple letter badges "Codex" arrow "Claude", no provider logos. State chip "Prévia pronta". Card title "Contexto da transferência" rows "Objetivo" "Validar recuperação de senha"; "Branch" "main"; "Checkpoint" "Hoje, 14:20". Three compact checklist rows with green confirmation icons "Alterações incluídas", "Testes referenciados", "Próximo passo definido". Note "O turno só termina após o aceite do destino." Bottom lavender primary "Revisar e solicitar transferência", secondary "Voltar". No claim transfer completed, no automatic provider routing, no subscription switching.
```

### 09 — Atividade e evidências

Prompt enviado: direção compartilhada acima, seguida deste texto literal.

```text
Screen 09 of 10. Activity timeline screen. Header "Atividade", filter chips "Tudo", "Sessões", "Testes", "Decisões". Subheading "Hoje". Vertical structured timeline with four entries: time "14:34", title "Validação em andamento", subtitle "360 • Codex", muted status "Sem resultado final"; time "14:20", title "Checkpoint salvo", subtitle "360 • Registro local", action link "Ver checkpoint"; time "14:10", title "Teste concluído", subtitle "Projeto pessoal • Evidência recebida", action "Ver relatório"; time "13:55", title "Aprovação solicitada", subtitle "360 • Push de 2 commits", amber link "Revisar". Bottom nav Hoje/Projetos/Atividade/Ajustes with Atividade active. Fact-based timeline, no raw terminal or unreadable log wall.
```

### 10 — Modo offline e conflito

Prompt enviado: direção compartilhada acima, seguida deste texto literal.

```text
Screen 10 of 10. Offline/conflict project screen. Back arrow title "360". Top amber connectivity banner "Sem conexão", subtitle "Última confirmação: hoje, 14:20". Card "Seu trabalho está salvo no celular" and smaller text "Alterações locais serão revisadas ao reconectar." Section "Conflito de próximo passo" two distinguishable panels "Sua versão local" — "Testar recuperação de senha" and "Última versão recebida" — "Revisar mensagem de erro". Small note "Escolha a versão após atualizar os dados." Primary outlined button "Tentar reconectar". Disabled muted button with lock "Resolver após reconectar". Bottom small explanation "Aprovações e comandos sensíveis estão indisponíveis offline." Show clear usable cached interface, never claim sync succeeded or auto execute approvals.
```
