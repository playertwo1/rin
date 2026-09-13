# ADR 0002 — Identidade do aplicativo e toolchain Android

Data: 2026-09-13.  
Estado: Aceita (resolve OPEN-01 e OPEN-02 para F00/F01).  
Contexto de decisão: [F00-T02](../roadmap/F00-preparacao.md), [ARCHITECTURE](../ARCHITECTURE.md) e [IMPLEMENTATION](../planning/IMPLEMENTATION.md).

## Contexto

A preparação documental estabeleceu o RIN como aplicativo Android para controle pessoal da AI Workstation na LAN. Para iniciar a geração de código (scaffold em F01), é mandatório fixar a identidade do pacote, os níveis de SDK, a matriz exata de versões da toolchain (compatíveis entre si e com o JDK 17 localmente comprovado em F00-T01), a stack de rede e injeção de dependência, e a estratégia modular sem criar módulos ociosos prematuros.

## Decisões

### 1. Identidade e Aparelho Alvo (OPEN-01)
- **Application ID / Package Name**: `com.playertwo1.rin`
- **Nome do App**: `RIN`
- **minSdk**: `26` (Android 8.0 Oreo). Justificativa: fornece suporte nativo a `java.time`, Android Keystore moderno e NotificationChannels sem necessidade de desugaring pesado, cobrindo virtualmente todos os aparelhos ativos modernos (>96%).
- **targetSdk / compileSdk**: `35` (Android 15). Justificativa: alinhado à plataforma instalada localmente no SDK (`android-35`) e aos padrões atuais de compatibilidade e segurança.
- **Aparelho alvo**: Emulador AVD localmente configurado `Pixel_10_Pro_XL` (Android 15 / API 35). O modelo de aparelho físico de Rafael permanece pendente de informação; nenhum modelo físico foi inventado.

### 2. Matriz Exata da Toolchain (OPEN-01)
Todas as versões foram selecionadas com base nas matrizes de compatibilidade oficiais do Android Gradle Plugin, Kotlin e Jetpack Compose:

| Componente | Versão Fixada | Fonte / Justificativa |
|---|---|---|
| **JDK** | OpenJDK 17 (Temurin 17.0.20.1) | Instalado localmente em `C:\Program Files\Eclipse Adoptium\jdk-17.0.20.101-hotspot\` |
| **Gradle** | `8.11.1` | Suporta JDK 17, AGP 8.7+ e Kotlin 2.0+ |
| **Android Gradle Plugin (AGP)** | `8.7.3` | Compatível com Gradle 8.9+ e compileSdk 35 |
| **Kotlin** | `2.0.21` | Versão estável com Compose Compiler integrado |
| **Kotlin Symbol Processing (KSP)**| `2.0.21-1.0.28` | Alinhamento estrito 1:1 com Kotlin 2.0.21 (necessário para Room) |
| **Compose BOM** | `2024.12.01` | Estável, com Material 3 (`androidx.compose.material3:material3`) |
| **Room** | `2.6.1` | Suporte a coroutines, KSP e SQLite estável |
| **AndroidX Core KTX** | `1.15.0` | Estável para API 35 |
| **AndroidX Lifecycle / ViewModel** | `2.8.7` | Suporte moderno a Compose e coroutines |

### 3. Stack de Rede, Serialização e Injeção (OPEN-02)
- **HTTP Client**: `OkHttp` (`4.12.0`) + `Retrofit` (`2.11.0`).
  - Justificativa: estabilidade para chamadas REST, suporte a interceptors de autenticação e mTLS para a LAN, e suporte nativo a Server-Sent Events via extensão `okhttp-sse` e WebSockets.
- **Serialização**: `Kotlinx Serialization` (`1.7.3`) com converter para Retrofit.
  - Justificativa: tipagem estrita, performance e alinhamento com Kotlin multiplatform/Compose sem reflexão Java pesada.
- **Injeção de Dependências**: Inicialmente **Container Manual / Injeção por Construtor** estruturada (`AppContainer`), sem introdução de Hilt/Koin no scaffold inicial.
  - Justificativa: evita overhead de anotações e geração de código KSP para um app com poucos componentes iniciais; permite migração limpa para Hilt em fases posteriores caso o grafo justifique.

### 4. Estratégia de Módulos
- Módulo único inicial `:app`, com arquitetura interna em camadas e pacotes limpos:
  - `com.playertwo1.rin.core`: utilitários, extensões, despachantes de coroutines.
  - `com.playertwo1.rin.data`: Room database, DAOs, repositórios locais e entidades.
  - `com.playertwo1.rin.domain`: modelos de domínio e regras de negócio.
  - `com.playertwo1.rin.gateway`: interfaces do `WorkstationGateway` e implementação fake determinística.
  - `com.playertwo1.rin.ui`: telas Jetpack Compose (Theme, Home, Projetos, etc.).
- Justificativa: Evita a proliferação de submódulos Gradle ociosos antes de existir código real que justifique fronteiras de compilação isoladas.

## Consequências

- O scaffold da F01 terá parâmetros e dependências estritamente determinados sem ambiguidades.
- Nenhum segredo ou configuração dependente de máquina será versionado no Git; caminhos locais serão resolvidos via `local.properties`.
- O AVD `Pixel_10_Pro_XL` será o ambiente padrão de teste e validação na emulação.
