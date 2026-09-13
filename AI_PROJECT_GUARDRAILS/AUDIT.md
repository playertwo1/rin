# AUDIT.md
## Auditoria Final Obrigatória

Este arquivo define a revisão que deve ser feita após alterações relevantes no projeto.

O auditor deve agir como um revisor independente.

Seu objetivo não é confirmar que o agente está certo.

Seu objetivo é tentar descobrir onde ele pode estar errado.

---

## 1. ESCOPO

Verifique:

- O pedido original foi realmente atendido?
- Houve mudanças fora do escopo?
- Alguma funcionalidade não solicitada foi adicionada?
- Alguma funcionalidade existente foi removida?
- A solução ficou desnecessariamente complexa?

---

## 2. DIFF

Examine as alterações realizadas.

Procure:

- arquivos alterados sem necessidade;
- código removido acidentalmente;
- mudanças gigantes para problemas pequenos;
- duplicação;
- código morto;
- TODOs esquecidos;
- debug logs;
- hardcodes;
- mocks;
- comentários temporários;
- alterações suspeitas.

Pergunta central:

> Cada alteração presente no diff possui uma razão clara relacionada à tarefa?

Se não, investigue.

---

## 3. REGRESSÃO

Procure possíveis impactos em:

- funcionalidades existentes;
- APIs;
- banco de dados;
- migrations;
- configurações;
- autenticação;
- permissões;
- UI;
- integrações;
- persistência;
- background jobs;
- compatibilidade.

---

## 4. QUALIDADE

Verifique:

- legibilidade;
- simplicidade;
- consistência com o projeto;
- tratamento de erros;
- edge cases;
- concorrência quando aplicável;
- nullability;
- estados inesperados;
- duplicação de lógica.

Prefira a solução mais simples que resolva corretamente o problema.

---

## 5. SEGURANÇA

Procure:

- secrets;
- tokens;
- credenciais;
- permissões excessivas;
- dados sensíveis em logs;
- validações removidas;
- inputs não tratados;
- operações perigosas.

---

## 6. VALIDAÇÃO

Quando disponível, execute:

- build;
- testes;
- lint;
- static analysis;
- type checking.

Falha em validação não deve ser escondida.

---

## 7. GIT

Verifique:

- `git status`;
- diff;
- arquivos inesperados;
- arquivos grandes;
- artefatos gerados;
- secrets;
- alterações não relacionadas.

Nunca confunda alterações anteriores do usuário com alterações feitas pela tarefa atual.

---

## 8. CAÇA A ATALHOS

Procure especificamente por soluções que apenas façam o problema desaparecer visualmente:

- exceptions ignoradas;
- `catch` vazio;
- validação desativada;
- testes removidos;
- valores hardcoded;
- feature desligada;
- retorno falso de sucesso;
- mocks em produção.

---

## 9. CLASSIFICAÇÃO

Classifique problemas encontrados como:

### CRITICAL
Pode causar perda de dados, vulnerabilidade, quebra grave ou comportamento destrutivo.

### HIGH
Grande chance de regressão ou funcionamento incorreto.

### MEDIUM
Problema real que deveria ser corrigido.

### LOW
Melhoria recomendada sem impedir funcionamento.

---

## 10. RESULTADO

Ao terminar, produza:

AUDIT RESULT: PASS

ou

AUDIT RESULT: FAIL

Se FAIL, informe:

- severidade;
- arquivo/local;
- problema;
- impacto;
- correção recomendada.

Não aprove apenas porque build ou testes passaram.

Testes são evidência, não prova absoluta.

---

## PRINCÍPIO DO AUDITOR

Não pergunte:

> "Como posso provar que isso funciona?"

Pergunte:

> "Como isso pode quebrar?"

Tente falsificar a solução.

Somente aprove quando não encontrar problemas relevantes após essa tentativa.
