# PROJECT — RIN

Contrato de visão. Atualize quando o projeto nasce e quando a visão muda; o resto (marcos, estado) fica em `ROADMAP.md` e `STATE.md`. Resumo do [README](README.md), do [ROADMAP](ROADMAP.md) ("Produto 1.0 e fronteira") e de [ARCHITECTURE](docs/ARCHITECTURE.md)/[DECISIONS](docs/DECISIONS.md), que continuam sendo o detalhe.

## Problema
O trabalho de agentes de IA acontece no computador (AI Workstation), mas Rafael precisa saber de qualquer lugar onde cada projeto parou, o que mudou, o que falta, qual agente está trabalhando e o que precisa da decisão dele.

## Para quem
Rafael, como painel móvel pessoal para uma workstation na LAN.

## MVP — até três fluxos que entregam valor
1. Projeto Vivo local: projetos, checkpoints, decisões e "Onde parei?" persistentes e úteis sem rede (F04–F06).
2. Integração de leitura: pareamento seguro e fatos reais (Git, tarefas, testes) vindos da AI Workstation (F07–F09).
3. Operação móvel: sessões, aprovações vinculadas ao payload, AI Shift e notificações (F10–F14).

## Fora do MVP (escrito para não voltar escondido)
- Backend próprio ("RIN Server"), runtime, shell genérico, vault, orquestrador ou integração direta com CLIs de agentes: pertencem à AI Workstation.
- Execução de agentes no celular; contorno de cotas ou termos de uso.
- Ação destrutiva sem política e aprovação.

## Restrições
- Plataforma/ambiente: Android (`com.playertwo1.rin`, minSdk 26, target/compileSdk 35), Kotlin, Jetpack Compose, Room; integração só por `WorkstationGateway` e contrato versionado (`docs/INTEGRATION_CONTRACT.md`).
- Dados sensíveis e privacidade: nada de tokens, cookies, chaves privadas, `.env` ou transcrições sensíveis no Git, APK ou logs; offline e estados de sincronização distinguíveis.
- Custos, licenças, integrações obrigatórias: licença a definir antes da primeira distribuição pública; depende das entregas da AI Workstation (`docs/planning/EXTERNAL_DEPENDENCIES.md`).

## Como saberemos que funcionou
Critérios de [RELEASE_1_0](docs/quality/RELEASE_1_0.md): integração real e duas semanas estáveis de piloto pessoal (F17), com artefato verificado (F18).

## Autoridade de produto
Rafael.

## Alternativas consideradas
O antigo "RIN Server" como backend independente, descartado: suas funções passaram para a API/nó da AI Workstation ([origem e divergências](docs/planning/SOURCES.md)).
