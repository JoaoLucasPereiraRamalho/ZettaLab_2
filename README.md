🚀 Zetta Todo API - Backend
API REST robusta para gerenciamento de tarefas e organização via quadros Kanban. O sistema conta com autenticação segura, versionamento de banco de dados e uma arquitetura focada em escalabilidade e manutenção.

🛠️ Tecnologias Utilizadas
Java 21 & Spring Boot 3.4.2

Spring Security + JWT (Autenticação e Autorização)

Spring Data JPA (Persistência de Dados)

PostgreSQL (Banco de Dados Relacional)

Docker & Docker Compose (Containerização do Banco)

Liquibase (Migrações e Versionamento do Schema)

Lombok (Redução de Boilerplate)

Maven (Gerenciamento de Dependências)

🏗️ Padrões de Projeto e Arquitetura
A API foi construída seguindo princípios de Clean Code e SOLID:

Camada de Mapper: Utilização de classes Mapper dedicadas para converter Entidades em DTOs, garantindo que a lógica de apresentação não polua as regras de negócio.

Service Layer: Centralização da inteligência do sistema (ex: regras para conclusão de tarefas com subtarefas pendentes e reabertura automática de itens).

Global Exception Handling: Tratamento padronizado de erros e validações.

⚙️ Como Rodar o Projeto
Pré-requisitos
Java 21 instalado.

Docker e Docker Compose instalados.

Maven instalado.

Passo 1: Subir o Banco de Dados (Docker)
Na raiz do projeto backend, onde se encontra o arquivo docker-compose.yml, execute:

Bash
docker compose up -d
Isso iniciará um container PostgreSQL com as configurações necessárias para a aplicação.

Passo 2: Executar a Aplicação
Com o banco de dados ativo, rode o comando:

Bash
mvn clean install
mvn spring-boot:run
A API estará disponível em http://localhost:8080.
