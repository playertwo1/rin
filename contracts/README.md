# Contratos Candidatos e Fixtures JSON do RIN (v0.1)

Este diretório contém a formalização dos envelopes e schemas de comunicação entre o aplicativo RIN e a AI Workstation, baseados no rascunho [INTEGRATION_CONTRACT.md](../docs/INTEGRATION_CONTRACT.md).

> **Aviso de Conformidade**: Estes arquivos representam a especificação do contrato candidato para consumo móvel e implementação do fake determinístico na Fase F02. Eles **não comprovam** que a API real da AI Workstation já implementou ou publicou estes endpoints.

---

## 1. Padrão de Envelope

Toda resposta HTTP da AI Workstation utiliza o envelope padronizado:

### Sucesso (`envelope-success.json`)
- `schemaVersion`: String semver (ex: `"1.0"`).
- `requestId`: UUID v4 de rastreabilidade.
- `occurredAt`: Timestamp UTC ISO-8601.
- `data`: Payload tipado do recurso.
- `error`: `null`.

### Erro (`envelope-error.json`)
- `schemaVersion`: String semver.
- `requestId`: UUID v4.
- `occurredAt`: Timestamp UTC.
- `data`: `null`.
- `error`: Objeto estruturado com `code`, `message`, `category` e `details`.

---

## 2. Recursos Mapeados

1. `health`: Status da workstation, versão da API e lista de capabilities anunciadas.
2. `projects`: Listagem de projetos com ID, nome, branch ativa e status de saúde.
3. `project-detail`: Detalhes de um projeto específico com último checkpoint e metadados.
4. `events`: Log de eventos ordenado por cursor incremental para resincronização.
