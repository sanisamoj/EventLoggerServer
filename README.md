## EventLoggerServer
Um servidor que centraliza e gerencia logs de várias aplicações, proporcionando uma visão unificada e consolidada dos eventos registrados, facilitando a monitoração, análise e resolução de problemas.

O EventLoggerServer tem como objetivo centralizar logs de aplicações ou microservices, reunindo todas as informações em um único lugar para facilitar a visualização e gerenciamento.

## Funcionalidades do Sistema
O sistema possui dois tipos principais de usuários: Operadores e Aplicações.
- Operadores: Podem visualizar os logs gerados pelas aplicações, além de criar e deletar aplicações no sistema.
- Aplicações: Acessam a API por meio de um token gerado durante o processo de autenticação.
- Gerar logs e notificação quando ocorre mudanças na quantidade de logs.

## Funcionalidades dos Logs
- Níveis de Severidade dos Logs: Low, Medium, High, Critical.
- Tipos de Logs: Info, Error, Attack.
- Podem ser marcados como lidos e não lidos.

## Tecnologias e Ferramentas Utilizadas

- Backend: **Ktor (Kotlin)**
- Banco de Dados:
    - **MongoDB** - Para armazenamento de dados sensíveis.
    - **Redis** - Para armazenamento de dados que precisam de acesso mais rápido.

## Instalação
ara instalar o projeto para testes, utilizaremos o Docker.

- Instale a última versão do **Docker** em sua máquina.
- Instale o **Mongodb** (Verifique na página oficial, ou monte uma imagem com o Docker).
- Instale o **Redis** na sua máquina (Verifique a página oficial, ou monte uma imagem com o Docker).
- Para a autenticação além do uso de E-mail, estou utilizando os bots do projeto [NotifyBot](https://github.com/sanisamoj/NotifyBot).
- Crie um arquivo **.env** na pasta raiz do projeto, ou adicione um arquivo **.env** manualmente na construção da imagem docker.

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

#URL do banco de dados do REDIS
REDIS_SERVER_URL=host.docker.internal  #No Docker - host.docker.internal

#Porta do banco de dados do REDIS
REDIS_SERVER_PORT=6379

# E-mail do sistema
EMAIL_SYSTEM=
EMAIL_PASSWORD=

#Configuração para envios de email
SMTP_HOST=smtp.gmail.com
SMTP_STARTTLS_ENABLE=true
SMTP_SSL_PROTOCOLS=TLSv1.2
SMTP_SOCKETFACTORY_PORT=465
SMTP_SOCKETFACTORY_CLASS=javax.net.ssl.SSLSocketFactory
SMTP_AUTH=true
SMTP_PORT=465
SMTP_SSL_TRUST=*

#Token para notificações do bot
BOT_URL=http://localhost:8585/
BOT_LOGIN_EMAIL=
BOT_LOGIN_PASSWORD=
BOT_ID=
```
> Nas configurações de Email, foi adicionado uma pré-configuração para utilizar os serviços do Gmail. Para a Senha do Email, é necessário gerar uma senha para aplicação em "Apps menos seguros" na sua conta do Gmail.

#### Execute o comando a seguir para construir a imagem Docker.

    docker build -t eventlog:latest .

#### Execute o comando a seguir para executar a imagem criada com o Docker.

    docker run --name eventlog -p 9096:9096 eventlog:latest

#### Configuração de E-mail

> O e-mail deve ser configurado conforme indicado na documentação do Javax. As configurações expostas no .env acima, é apenas uma configuração padrão.

## Visualização de Endpoints.
https://documenter.getpostman.com/view/29175154/2sA3e1BAFK
