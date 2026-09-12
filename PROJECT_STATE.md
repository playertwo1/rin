# Estado do RIN

Atualizado em: 2026-09-12

## Situação

- Fase: Zero
- Estado: fronteira de produto consolidada
- Implementação: não iniciada
- Produto: aplicativo Android da AI Workstation
- Primeiro módulo: Projeto Vivo
- Próximo marco: app local-first com gateway simulado
- Risco geral: contrato real com a plataforma ainda não validado

## Concluído

- Sobreposição com o repositório AI Workstation identificada.
- RIN definido como Control Plane Android.
- Projeto Vivo reposicionado como módulo do RIN.
- Antigo “RIN Server” incorporado conceitualmente à AI Workstation.
- Responsabilidades e fontes de verdade separadas.
- Contrato inicial entre os repositórios documentado.

## Próximas ações

1. Definir package, minSdk e estrutura Gradle.
2. Criar scaffold Android.
3. Implementar Project e Checkpoint com Room.
4. Criar `WorkstationGateway` e fake determinístico.
5. Implementar Home e “Onde parei?” local.
6. Alinhar schemas v1 com a AI Workstation.
7. Provar o primeiro caminho vertical sem execução real de agentes.

## Bloqueios e dúvidas

- Transporte em tempo real ainda será escolhido.
- Pareamento e autenticação do dispositivo precisam de spike conjunto.
- O Galaxy Book ainda será usado para validar o nó real.
- Interfaces oficiais de Codex, Claude e Antigravity pertencem aos spikes da AI Workstation.
