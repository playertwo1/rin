# Matriz de qualidade e evidência

## Estado inicial

Não existem wrapper Gradle, código Android, testes ou CI na base `3d24216`.
Comandos Android abaixo são planejados para depois de F01. Não foram executados nesta preparação.
F01 deve descobrir as tarefas reais com `gradlew.bat tasks --all` e registrar nomes de variante.
Se usar flavors, substituir pelos nomes efetivos e atualizar esta matriz.

## Comandos de referência

Para documentação, o verificador disponível desde esta preparação usa PowerShell 7:

```powershell
.\docs\quality\CHECK_DOCUMENTATION.ps1
```

Use `-RequireUnstarted` apenas para auditar a baseline antes da implementação.
Ele confere 19 fases, 76 IDs únicos, seções obrigatórias, indexação, caminhos de links
locais e AGENTS de até 80 linhas; não verifica âncoras, URLs externas ou a qualidade semântica.
Uma mudança autorizada no tamanho do plano exige atualizar as expectativas do verificador.

Executar da raiz rin no Windows, depois que existirem:

```powershell
.\gradlew.bat --version
.\gradlew.bat tasks --all
.\gradlew.bat assembleDebug
.\gradlew.bat testDebugUnitTest
.\gradlew.bat lintDebug
.\gradlew.bat connectedDebugAndroidTest
git diff --check
git status --short
```

São comandos separados; guardar exit code e relatório real de cada um.
Teste instrumentado exige emulador/dispositivo conectado; sem isso registrar NÃO EXECUTADO.
CI só é validada quando há execução real com link/revisão e resultado; YAML escrito não é CI verde.
Release assinado usa a tarefa real documentada em F16; segredo nunca entra no comando registrado.

## Matriz por risco

| Categoria | Verificação mínima | Prova que não substitui |
|---|---|---|
| Documentação | Links, IDs, dependências, status, diff e auditoria de escopo | Não prova build do app |
| Scaffold | Build, lint, teste significativo e execução em emulador | APK existente não prova execução |
| Domínio/resumo | Fixtures, relógio fixo, estados vazios/erro e referências | Snapshot visual não prova cálculo |
| Room | DAO em ambiente Android, transação, restart e migração quando existir | Teste de objeto em memória não prova persistência |
| Gateway fake | Contrato positivo/negativo, schema, mapeamento e reset determinístico | Não prova API real |
| Sincronização | Duplicata, reordenação, lacuna, restart, transação falha e resync | Refresh único não prova recuperação |
| UI | Fluxo real, vazio/loading/erro/stale, navegação e acessibilidade | Preview Compose não prova navegação |
| Segurança | TLS/pareamento/revogação/replay/expiração, logs e backup | Biometria local não prova autorização do servidor |
| Integração | Mesmo contrato contra serviço real, revisões de ambos e rastreio de IDs | Simulador nunca encerra esse gate |
| Sessão/AI Shift | Estado final + evidência/ack confirmado pela plataforma | HTTP 2xx/202 não prova efeito |
| Release | APK assinado, hash, instalação/upgrade, restauração e piloto | Build debug não é release |

## Casos transversais obrigatórios

- Q01: local/pending/synced/stale/conflict/failed apresentados corretamente.
- Q02: queued e running não são sucesso; timeout não é cancelamento.
- Q03: evento duplicado não duplica efeito nem notificação.
- Q04: fim de processo Android entre recebimento e persistência não perde evento.
- Q05: credencial revogada impede escrita e nova leitura autenticada; cache aparece como cache.
- Q06: payload/versão/hash diferentes, expiração e replay nunca aprovam ação errada.
- Q07: contrato desconhecido e capability ausente degradam sem crash e sem ação inventada.
- Q08: nenhum segredo em log, notificação, export, relatório ou fixture.
- Q09: origem do handoff permanece recuperável se destino falhar antes do aceite.
- Q10: atualizar app preserva dados; migração falha não apaga banco silenciosamente.
- Q11: accessibility com TalkBack, fonte ampliada, teclado, rotação e voltar.
- Q12: dados do projeto/manifesto não viram comandos nem links executados automaticamente.

## Regras de bloqueio

CRITICAL, HIGH ou MEDIUM relevante não resolvido impede PASS da entrega afetada.
LOW pode ficar registrado com justificativa se não violar aceite.
Falha de ambiente não é aprovação; manter gate aberto e registrar como retomar.
Não modificar teste para refletir bug; altere expectativa só se o requisito mudou com decisão rastreável.
Não criar testes que apenas reproduzem linha por linha a implementação; provar comportamento observável.
