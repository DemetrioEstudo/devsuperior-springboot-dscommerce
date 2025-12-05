# DSCommerce

Resumo rápido: aplicação backend em Java + Spring Boot para um projeto de e-commerce (exemplo didático).

## Arquivo
`README.md`

## Tecnologias
- Java 21
- Spring Boot 3.5.8
- Spring Data JPA / Hibernate 6
- H2 (banco em memória para profile `test`)
- Maven
- Jakarta Persistence API
- Embedded Tomcat (via Spring Boot)
- IDE recomendada: IntelliJ IDEA

## Estrutura do projeto
- `src/main/java` — código-fonte (pacote `br.com.klsys.dscommerce`)
- `src/main/resources` — arquivos de configuração (`application.properties`, profiles)
- `pom.xml` — dependências e build

## Pré-requisitos (Windows)
- Java 21 instalado e configurado no `PATH`
- Maven ou usar Maven Wrapper (`mvnw.cmd`)

## Como executar (Windows)
1. Pelo Maven Wrapper (recomendado):
    - Rodar direto:  
      `mvnw.cmd spring-boot:run`
    - Ou empacotar e executar jar:  
      `mvnw.cmd clean package`  
      `java -jar target\dscommerce-*.jar --spring.profiles.active=test`

2. Pelo Maven (se não usar wrapper):
    - `mvn spring-boot:run`

3. Pela IDE (IntelliJ):
    - Executar a classe `br.com.klsys.dscommerce.DscommerceApplication` (Run).

## Perfil e banco H2
- O projeto ativa `spring.profiles.active=test` em `src/main/resources/application.properties`.
- Confirme que existe `src/main/resources/application-test.properties` (nome exato `application-test.properties`) com configurações do H2; exemplo mínimo recomendado:
  ```properties
  spring.datasource.url=jdbc:h2:mem:testdb
  spring.datasource.username=sa
  spring.datasource.password=
  spring.h2.console.enabled=true
  spring.jpa.hibernate.ddl-auto=update
