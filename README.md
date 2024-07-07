## EventLoggerServer
Um servidor, que centraliza logs de outras aplicações.

## Objetivos
O EventLoggerServer tem como objetivo centralizar logs de aplicações ou microservices, reunindo todas as informações em um único lugar para facilitar a visualização e gerenciamento.

## Fluxo
O sistema possui dois tipos principais de usuários: Operadores e Aplicações.
- Operadores: Podem visualizar os logs gerados pelas aplicações, além de criar e deletar aplicações no sistema.
- Aplicações: Acessam a API por meio de um token gerado durante o processo de autenticação.

## Funcionalidades
- Níveis de Severidade dos Logs: Low, Medium, High, Critical.
- Tipos de Logs: Info, Error, Attack.

## Instalação
Para instalar o projeto, siga os seguintes passos:
- Reconstrua o Gradle Wrapper (gradlew).
- Instale o MongoDB.
- Instale o Redis.

- Arquivo .env

Crie um arquivo .env com as seguintes informações para que o projeto funcione corretamente:

```.env

# Número do superAdmin para autenticação
SUPERADMIN_PHONE=

# E-mail do superAdmin para autenticação
SUPERADMIN_EMAIL=

# URL da aplicação
SELF_URL=http://localhost:9096

# Audience do token (quem deve processar o token)
JWT_AUDIENCE=EventLoggerServer

# Domínio do token (quem foi o emissor)
JWT_DOMAIN=

# Secret do token do operador
OPERATOR_SECRET=

# Secret do token das aplicações
APPLICATION_SERVICE_SECRET=

# URL do banco de dados MongoDB
SERVER_URL=mongodb://localhost:27017

# Nome do banco de dados MongoDB
NAME_DATABASE=EventLoggerServer

# E-mail do sistema
EMAIL_SYSTEM=
EMAIL_PASSWORD=
```

Configuração de E-mail

O e-mail deve ser configurado conforme indicado na documentação do Javax.

## Visualização de Endpoints.
https://documenter.getpostman.com/view/29175154/2sA3e1BAFK
