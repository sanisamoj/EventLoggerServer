# Estágio de build com Ubuntu e Maven manual
FROM ubuntu:22.04 AS build

# Atualiza o repositório e instale OpenJDK e Maven
RUN apt-get update && \
    apt-get install -y openjdk-21-jdk wget tar && \
    wget https://downloads.apache.org/maven/maven-3/3.9.8/binaries/apache-maven-3.9.8-bin.tar.gz && \
    tar -xzf apache-maven-3.9.8-bin.tar.gz -C /opt && \
    ln -s /opt/apache-maven-3.9.8/bin/mvn /usr/bin/mvn

# Define o diretório de trabalho no contêiner
WORKDIR /app

# Copia os arquivos de origem do projeto para o diretório de trabalho
COPY pom.xml .
COPY src ./src

# Realiza o build do projeto e crie o arquivo .jar
RUN mvn clean package -DskipTests && ls -l target

FROM openjdk:21-jdk

# Define o diretório de trabalho no contêiner
WORKDIR /app

# Copia o arquivo .env e o arquivo .jar criado no estágio de build para o diretório de trabalho
COPY .env .
COPY --from=build /app/target/eventloggerserver-0.2.1-jar-with-dependencies.jar eventloggerserver.jar

EXPOSE 9096

CMD ["java", "-jar", "eventloggerserver.jar"]
