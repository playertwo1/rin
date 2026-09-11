# RIN

RIN é um aplicativo Android mobile first para acompanhar projetos desenvolvidos com assistência de IA, preservar a memória técnica e transferir o trabalho entre Codex, Claude e Antigravity.

O aplicativo responde a cinco perguntas:

- Onde parei?
- O que mudou?
- O que falta?
- Qual agente está trabalhando?
- O que precisa da minha decisão?

## Decisão central

O Android funciona como controle remoto seguro. A execução acontece no computador por meio do RIN Server. O Git é a fonte da verdade do código e a memória do projeto pertence ao RIN, não ao histórico proprietário de um agente.

## Funções principais

- Catálogo de projetos e estado do Git.
- Resumo persistente Onde parei.
- Tarefas, bugs, testes, decisões e bloqueios.
- Acompanhamento de sessões em tempo real.
- AI Shift para passar o turno entre agentes.
- Aprovações pelo celular para ações sensíveis.
- Notificações de conclusão, falha e decisão pendente.

## Arquitetura inicial

```text
Android Kotlin e Compose
        |
   HTTPS e WebSocket
        |
      RIN Server
        |-- Git e GitHub
        |-- Memória do projeto
        |-- Políticas e aprovações
        |-- Adaptadores de agentes
              |-- Codex
              |-- Claude
              |-- Antigravity
              |-- Claw Orchestrator
```

## Estado do projeto

O projeto está na Fase Zero, dedicada a contratos, arquitetura e redução dos riscos de integração. Ainda não há código de produção.

## Documentação

- [Roadmap completo](ROADMAP.md)
- [Estado atual](PROJECT_STATE.md)
- [Arquitetura](docs/ARCHITECTURE.md)
- [Decisões](docs/DECISIONS.md)
- [Conversa consolidada](docs/CONVERSA_CONSOLIDADA.md)
- [Especificação completa em DOCX](docs/RIN_Especificacao_e_Roadmap_Inicial.docx)
- [Instruções para agentes](AGENTS.md)

## Primeira entrega executável

O primeiro caminho vertical deverá:

1. Iniciar o RIN Server.
2. Responder `GET /health`.
3. Listar projetos simulados em `GET /api/v1/projects`.
4. Conectar o Android ao servidor.
5. Exibir a lista simulada no aplicativo.
6. Validar os contratos em testes automatizados.

## Restrições do MVP

- Sem terminal genérico no Android.
- Sem contorno de cotas ou termos de uso.
- Sem `force push`, reset destrutivo ou leitura de segredos.
- Sem progresso inventado; apenas etapas confirmadas.
- Sem acoplamento direto do domínio ao Claw ou a outro provedor.

## Licença

A licença ainda será definida antes da primeira distribuição pública.