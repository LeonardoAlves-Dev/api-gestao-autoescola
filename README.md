# 🚗 API Gestão de Autoescola

*Acelerando e automatizando a gestão de centros de formação de condutores.*

Este projeto consiste em uma API RESTful completa desenvolvida para gerenciar as rotinas operacionais de uma autoescola, contemplando desde o cadastro de alunos e instrutores até o agendamento de instruções e mensageria assíncrona.

O grande diferencial deste projeto é a implementação rigorosa da **Arquitetura Hexagonal (Ports and Adapters)**, garantindo um código altamente escalável, testável e totalmente desacoplado de frameworks e bancos de dados externos.

## 🚀 Funcionalidades Principais

* **Gestão de Usuários:** CRUD completo de Alunos e Instrutores com validações de regras de negócio (ex: imutabilidade de CPF e CNH).
* **Soft Delete (Exclusão Lógica):** Registros não são apagados do banco, garantindo o histórico de auditoria e conformidade com LGPD.
* **Agendamento Inteligente:** Validação de choque de horários, regras de antecedência mínima e horário de funcionamento comercial.
* **Mensageria e E-mails:** Integração com **RabbitMQ** para processamento assíncrono. Quando uma instrução é cancelada, o sistema enfileira o evento e dispara um e-mail automático de notificação aos envolvidos.

## 🛠️ Stack Tecnológica & Arquitetura

* **Linguagem:** Java 25
* **Framework:** Spring Boot 3+ (Web, Data JPA, Validation)
* **Banco de Dados:** MySQL (Rodando em container Docker)
* **Migrações:** Flyway Migration (Controle de versão do banco de dados)
* **Mensageria:** RabbitMQ via Docker
* **Arquitetura:** Hexagonal (Domain-Driven Design concepts)

## 🔧 Estrutura do Projeto (Hexagonal)

O projeto foi desenhado para isolar o domínio principal:
```text
src/
├── adapter/          # Adaptadores externos (Entrada e Saída)
│   ├── in/           # Controllers REST (Web)
│   └── out/          # Repositórios JPA, RabbitMQ, Envio de Email
├── application/      # Portas (Interfaces de caso de uso)
└── domain/           # Regras de Negócio puras (Modelos sem anotações JPA)
```
## ⚙️ Como Executar o Projeto (Localmente)

Para clonar e executar este projeto na sua máquina, você precisará do [Git](https://git-scm.com), [Java 25](https://jdk.java.net/25/), [Maven](https://maven.apache.org/) e do [Docker](https://www.docker.com/) instalados.

**1. Clone o repositório:**
```bash
git clone [https://github.com/LeonardoAlves-Dev/api-gestao-autoescola.git](https://github.com/LeonardoAlves-Dev/api-gestao-autoescola.git)
cd NOME-DO-SEU-REPOSITORIO
```
**2. Configure as Variáveis de Ambiente:**<br>
Crie um arquivo ```.env``` na raiz do projeto com as credenciais do banco de dados:
```bash
MYSQL_ROOT_PASSWORD=sua_senha_root
MYSQL_DATABASE=nome_do_banco
MYSQL_USER=seu_usuario
MYSQL_PASSWORD=sua_senha
```
*Configure também as variáveis correspondentes na sua IDE (ex: MYSQL_DEV_USER, MYSQL_DEV_PASS, JWT_SECRET).*

**3. Suba a infraestrutura via Docker:**
```bash
docker-compose up -d
```
**4. Execute a aplicação (Spring Boot):**<br>
Rode a classe principal ```Application.java``` na sua IDE ou utilize o Maven pelo terminal:
```bash
mvn spring-boot:run
```
*A API estará disponível em http://localhost:8080.*