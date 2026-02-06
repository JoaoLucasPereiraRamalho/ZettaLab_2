# Zetta Todo API

API REST para gerenciamento de tarefas e organização via quadros Kanban. Sistema completo com autenticação JWT, versionamento de banco de dados e mensagens internacionalizadas.

---

## Índice

- [Funcionalidades](#-funcionalidades)
- [Stack Tecnológica](#-stack-tecnológica)
- [Arquitetura](#-arquitetura)
- [Pré-requisitos](#-pré-requisitos)
- [Como Executar](#-como-executar)
- [Variáveis de Ambiente](#-variáveis-de-ambiente)
- [Documentação da API](#-documentação-da-api)
- [Endpoints](#-endpoints)
- [Autenticação](#-autenticação)
- [Internacionalização](#-internacionalização)

---

## Funcionalidades

- **Autenticação** — Login com JWT (JSON Web Token)
- **Usuários** — Cadastro de usuários
- **Categorias** — CRUD de categorias com cores para organização visual
- **Tarefas** — CRUD de tarefas com prioridade, status e data de vencimento
- **Subtasks** — Tarefas vinculadas com regras de conclusão (bloqueio se houver pendentes)
- **Dashboard** — Visualização agrupada por categorias
- **i18n** — Mensagens de erro em português e inglês

---

## Stack Tecnológica

| Tecnologia | Versão |
|------------|--------|
| Java | 21 |
| Spring Boot | 4.0.2 |
| Spring Security + JWT | — |
| Spring Data JPA | — |
| PostgreSQL | 16 |
| Liquibase | — |
| Docker & Docker Compose | — |
| Maven | — |
| Lombok | — |

---

## Arquitetura

O projeto segue princípios de **Clean Code** e **SOLID**:

- **Módulos por domínio** — `usuario`, `tarefa` (categoria, tarefa, subtarefa)
- **Camadas** — Controller → Service → Repository
- **Mapper** — Conversão Entidade ↔ DTO
- **Global Exception Handler** — Tratamento centralizado de erros e validações
- **BusinessException** — Regras de negócio com chaves i18n

---

## Pré-requisitos

- **Java 21** ou superior
- **Docker** e **Docker Compose**
- **Maven 3.8+** (ou use o wrapper `./mvnw`)

---

## Como Executar

### 1. Clonar e entrar no diretório

```bash
git clone <url-do-repositorio>
cd todo-api
```

### 2. Subir o banco de dados

```bash
docker compose up -d
```

Isso inicia um container PostgreSQL na porta **5434** com as variáveis definidas em `.env` ou valores padrão.

### 3. Rodar a aplicação

```bash
./mvnw clean spring-boot:run
```

A API ficará disponível em **http://localhost:8080**.

> **Nota:** Se a porta 8080 estiver em uso, configure `server.port` em `application.properties` ou encerre o processo que a utiliza.

---

## Variáveis de Ambiente

Crie um arquivo `.env` na raiz do projeto (ou exporte as variáveis):

| Variável | Descrição | Padrão |
|----------|-----------|--------|
| `DB_NAME` | Nome do banco de dados | `zetta_todo_db` |
| `DB_USER` | Usuário PostgreSQL | `postgres` |
| `DB_PASSWORD` | Senha PostgreSQL | `postgres` |
| `JWT_SECRET` | Chave secreta para assinatura do JWT | `segredo_padrao_dev` |

> **Atenção:** Em produção, use sempre um `JWT_SECRET` forte e seguro. (deve ter mais de 32 caracteres)

---

## Endpoints

### Autenticação
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/auth/login` | Login (retorna JWT) |

### Usuários
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/users` | Cadastro de usuário |

### Categorias
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/categories` | Listar categorias |
| GET | `/categories/{id}` | Buscar por ID |
| POST | `/categories` | Criar categoria |
| PUT | `/categories/{id}` | Atualizar categoria |
| DELETE | `/categories/{id}` | Excluir categoria |

### Tarefas
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/tasks` | Listar tarefas |
| GET | `/tasks/dashboard` | Dashboard agrupado por categoria |
| GET | `/tasks/{id}` | Buscar por ID |
| POST | `/tasks` | Criar tarefa |
| PUT | `/tasks/{id}` | Atualizar tarefa |
| PATCH | `/tasks/{id}/status` | Atualizar status |
| DELETE | `/tasks/{id}` | Excluir tarefa |

### Subtarefas
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/subtasks` | Criar subtarefa |
| PATCH | `/subtasks/{id}/status` | Atualizar status |
| DELETE | `/subtasks/{id}` | Excluir subtarefa |

> Endpoints de categorias, tarefas e subtarefas exigem autenticação via header `Authorization: Bearer <token>`.

---

## Autenticação

1. Faça login em `POST /auth/login` com `email` e `password`.
2. Use o token retornado no header das requisições:

```
Authorization: Bearer <seu-token-jwt>
```

---

## Internacionalização

As mensagens de erro suportam **português** e **inglês**. O idioma é definido pelo header:

```
Accept-Language: pt-BR
```

ou

```
Accept-Language: en
```

Arquivos de mensagens em `src/main/resources/`:

- `messages.properties` (inglês)
- `messages_pt.properties` (português)

---

## Estrutura do Projeto

```
src/main/java/com/zetta/todo/
├── common/           # Configurações e exceções globais
│   ├── config/       # Security, CORS, Swagger
│   ├── dto/          # ErrorResponseDTO
│   └── exception/    # BusinessException, GlobalExceptionHandler
├── modules/
│   ├── usuario/      # Auth, User
│   └── tarefa/
│       ├── categoria/
│       ├── tarefa/
│       └── subtarefa/
└── TodoApiApplication.java
```

---

## Licença

Projeto desenvolvido para **zettaLab**.
