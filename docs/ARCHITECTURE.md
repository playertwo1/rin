# Arquitetura inicial do RIN

## Componentes

### Android

- Kotlin, Jetpack Compose e Material 3.
- MVVM com camadas de interface, domínio e dados.
- Room como cache offline.
- WorkManager para sincronização e notificações.
- HTTPS para comandos e WebSocket para eventos.
- Android Keystore para chaves do dispositivo.

### RIN Server

- API versionada em `/api/v1`.
- Autenticação por dispositivo pareado.
- Fila serial por projeto.
- Supervisor de processos com timeout e cancelamento.
- Banco transacional e event log append only.
- Exportação atômica de arquivos de memória.
- Adaptadores isolados para Git, GitHub e agentes.

### Adaptadores de agentes

```text
probe
capabilities
startSession
sendInstruction
streamEvents
requestCheckpoint
pause
cancel
resume
```

O domínio depende dessa interface, não do Claw ou do formato de uma CLI específica.

## Estado canônico

O banco do servidor é a autoridade operacional. Arquivos como `PROJECT_STATE.md`, `DECISIONS.md`, `TASKS.json`, `BUGS.json` e `HANDOFF.md` são exportações legíveis e recuperáveis.

## Fluxo de comando

1. Android envia comando idempotente assinado.
2. Servidor autentica dispositivo e valida a política.
3. Comando entra na fila do projeto.
4. Adaptador executa e publica eventos.
5. Cada transição é persistida antes da transmissão.
6. Android recupera desconexões por cursor de eventos.
7. Estado final exige evidência verificável.

## Segurança

- Projetos ficam restritos a raízes canônicas autorizadas.
- Não existe endpoint de shell genérico.
- Comandos são aliases allowlisted.
- Ações sensíveis exigem aprovação com expiração.
- Segredos são removidos antes de persistência, transmissão e notificação.
- `force push`, reset destrutivo e alteração de segredos ficam bloqueados no MVP.