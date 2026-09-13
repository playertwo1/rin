# Procedimento de Desenvolvimento e Configuração Local (SETUP)

Este documento estabelece as instruções para configurar o ambiente de desenvolvimento, abrir a raiz do repositório, preparar o Android SDK e executar o projeto RIN.

---

## 1. Como abrir o projeto

1. Clone o repositório ou abra a pasta raiz do projeto no seu terminal ou editor (Android Studio ou VS Code / Antigravity):
   ```powershell
   # A raiz do projeto deve ser exatamente a pasta contendo AGENTS.md e README.md
   cd <caminho_do_projeto>\rin
   ```
2. No **Android Studio**:
   - Selecione **File ➔ Open...** e aponte para a pasta raiz `rin/`.
   - O projeto será sincronizado com o Gradle após a criação do scaffold na fase **F01**.

---

## 2. Pré-requisitos de Ambiente

Comprovados no inventário [F00-T01](../evidence/F00-T01-2026-09-13.md):

| Requisito | Versão Mínima / Recomendada | Verificação no Terminal |
|---|---|---|
| **JDK** | OpenJDK 17 LTS (ex: Temurin) | `java -version` e `javac -version` |
| **Android SDK** | API 35 (Android 15) | Verificado no diretório do SDK |
| **Build-Tools** | 35.0.0 ou 34.0.0 | Verificado em `<SDK>/build-tools` |
| **ADB** | 1.0.41+ | `adb version` |
| **Emulador / AVD** | Pixel com API 35 (ex: `Pixel_10_Pro_XL`) | `emulator -list-avds` |

---

## 3. Configuração do SDK Local (`local.properties`)

O Gradle exige saber onde o Android SDK está localizado na sua máquina. Essa configuração nunca deve ser comitada no Git (está listada no `.gitignore`).

Crie um arquivo chamado `local.properties` na raiz do repositório com a propriedade `sdk.dir`:

### No Windows:
```properties
## Exemplo no Windows (use barras duplas ou barras normais):
sdk.dir=C:\\Users\\<seu_usuario>\\AppData\\Local\\Android\\Sdk
## ou
sdk.dir=C:/Users/<seu_usuario>/AppData/Local/Android/Sdk
```

### No Linux / macOS:
```properties
sdk.dir=/home/<seu_usuario>/Android/Sdk
## ou macOS:
sdk.dir=/Users/<seu_usuario>/Library/Android/sdk
```

---

## 4. Inicialização de Dispositivo / Emulador

Para rodar ou depurar o aplicativo Android em modo emulado:

1. **Listar emuladores disponíveis**:
   ```powershell
   & "<caminho_do_sdk>\emulator\emulator.exe" -list-avds
   ```
2. **Iniciar o emulador**:
   ```powershell
   & "<caminho_do_sdk>\emulator\emulator.exe" -avd Pixel_10_Pro_XL
   ```
3. **Verificar se o dispositivo está pronto via ADB**:
   ```powershell
   & "<caminho_do_sdk>\platform-tools\adb.exe" devices
   ```

---

## 5. Separação de Comandos: Disponíveis vs Planejados

### 5.1. Comandos Disponíveis Atualmente (Fase F00)
Comandos que podem ser executados agora para verificar integridade e consistência da base documental:

```powershell
# Verificar estado do Git
git status

# Verificar integridade dos links e estrutura documental
pwsh -File .\docs\quality\CHECK_DOCUMENTATION.ps1

# Inspecionar Java e ADB disponíveis no ambiente
java -version
& "C:\Users\fael\AppData\Local\Android\Sdk\platform-tools\adb.exe" version
```

### 5.2. Comandos Planejados (Disponíveis a partir da Fase F01)
Após a criação do scaffold Android com Gradle wrapper na fase F01, os seguintes comandos estarão ativos:

```powershell
# Compilar o APK de depuração (Planejado F01)
.\gradlew assembleDebug

# Executar testes unitários locais (Planejado F01)
.\gradlew test

# Executar análise estática e linting (Planejado F01)
.\gradlew lintDebug

# Instalar e executar no emulador/dispositivo conectado (Planejado F01)
.\gradlew installDebug
```

> **Aviso**: Não tente rodar `.\gradlew` antes da conclusão do scaffold em **F01-T01**, pois o executável do wrapper ainda não existe na base documental.

---

## 6. Convenções de Arquivos Ignorados e Segurança

O arquivo [.gitignore](../../.gitignore) foi configurado para impedir vazamento de dados locais ou credenciais:

1. **Artefatos de Build**: `.gradle/`, `build/`, `app/build/`.
2. **Configurações Pessoais de Máquina**: `local.properties`, `.idea/`, `*.iml`.
3. **Chaves Privadas e Assinatura**: `*.jks`, `*.keystore`, `signing.properties`, `keystore.properties` — **NUNCA** adicione ou comite chaves de assinatura no repositório.
4. **Segredos e Tokens**: `*.env`, `secrets.properties`, `*.secret`, `google-services.json`.

---

## 7. Persistência Local (Room SQLite) e Política de Migração

A partir da fase **F03**, o RIN utiliza Room SQLite para persistência offline-first:

1. **Localização do Banco**: Arquivo `rin_local.db` no armazenamento interno do app.
2. **Schema Versionado**: O schema JSON é exportado automaticamente pelo KSP para `app/schemas/com.playertwo1.rin.core.database.RinDatabase/`.
3. **Proibição de Migração Destrutiva**:
   - É expressamente proibido o uso de `fallbackToDestructiveMigration()` para prevenir perda acidental de rascunhos, checkpoints e notas locais do usuário.
4. **Procedimento para Migrações Futuras**:
   - Incrementar o número da versão na anotação `@Database(version = N)`.
   - Definir objeto explícito `Migration(from, to)` com os comandos DDL SQLite necessários.
   - Adicionar o objeto de migração ao builder `addMigrations(MIGRATION_1_2)`.
   - Testar a integridade dos dados via `androidx.room.testing.MigrationTestHelper` comparando os schemas JSON antes da entrega.
5. **Estado Vigente**: O banco está na versão 1 inicial; nenhuma migração retroativa fictícia é alegada como executada.

---

## 8. Próxima Etapa no Roadmap

Acompanhe o progresso das tarefas no [ROADMAP](../../ROADMAP.md) e no [PROJECT_STATE](../../PROJECT_STATE.md). Cada fase cumprida deve ser formalmente registrada em `docs/evidence/` conforme o protocolo de execução.
