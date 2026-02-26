# DSCommerce

API REST de e-commerce desenvolvida com Java e Spring Boot.

---

## 📌 Sobre o Projeto

Sistema backend para gerenciamento de produtos, categorias, usuários, pedidos e pagamentos. O projeto implementa uma arquitetura em camadas seguindo as melhores práticas do Spring Boot, incluindo **autenticação OAuth2 com JWT**, **autorização baseada em roles**, CRUD completo de produtos com tratamento de exceções personalizado.

**Funcionalidades Implementadas:**

**🔐 Autenticação e Autorização:**
- ✅ OAuth2 Authorization Server
- ✅ Autenticação com username/password (Custom Grant Type)
- ✅ Tokens JWT assinados com RSA 2048 bits
- ✅ Autorização baseada em roles (`ROLE_ADMIN`, `ROLE_OPERATOR`, `ROLE_CLIENT`)
- ✅ Proteção de endpoints com `@PreAuthorize`
- ✅ CORS configurável para frontend
- ✅ Criptografia de senhas com BCrypt
- ✅ UserDetailsService customizado
- ✅ Endpoint para obter dados do usuário autenticado (`/users/me`)

**📦 Gestão de Produtos:**
- ✅ Buscar produto por ID
- ✅ Listar produtos (paginado)
- ✅ Inserir novo produto (requer `ROLE_ADMIN`)
- ✅ Atualizar produto existente (requer `ROLE_ADMIN` ou `ROLE_OPERATOR`)
- ✅ Deletar produto (requer `ROLE_ADMIN`)
- ✅ Validação de dados com Bean Validation
- ✅ Tratamento de exceções personalizado com `@ControllerAdvice`
- ✅ Validação de integridade referencial

**👤 Gestão de Usuários:**
- ✅ Obter dados do usuário logado (requer autenticação)
- ✅ DTO de usuário sem exposição de dados sensíveis
- ✅ Extração de informações do token JWT


## 📁 Estrutura de Arquivos

```
dscommerce/
├── src/main/java/br/com/klsys/dscommerce/
│   ├── DscommerceApplication.java
│   │
│   ├── config/                         # Configurações de Segurança
│   │   ├── AuthorizationServerConfig.java     # OAuth2 Authorization Server
│   │   ├── ResourceServerConfig.java          # Resource Server (validação JWT)
│   │   └── customgrant/                       # Custom Grant Type (Password)
│   │       ├── CustomPasswordAuthenticationConverter.java
│   │       ├── CustomPasswordAuthenticationProvider.java
│   │       ├── CustomPasswordAuthenticationToken.java
│   │       └── CustomUserAuthorities.java
│   │
│   ├── controllers/                    # Camada de Apresentação (REST API)
│   │   ├── ProductController.java
│   │   ├── UserController.java                # Endpoints de usuário
│   │   ├── OrderController.java               # Endpoints de pedidos
│   │   └── handlers/
│   │       └── ControllerExceptionHandler.java
│   │
│   ├── services/                       # Camada de Lógica de Negócio
│   │   ├── ProductService.java
│   │   ├── UserService.java                   # UserDetailsService (autenticação)
│   │   ├── OrderService.java                  # Lógica de negócio de pedidos
│   │   └── exceptions/
│   │       └── ResourceNotFoundException.java
│   │
│   ├── repositories/                   # Camada de Acesso a Dados
│   │   ├── ProductRepository.java
│   │   ├── UserRepository.java
│   │   ├── OrderRepository.java               # Repositório de pedidos
│   │   └── OrderItemRepository.java           # Repositório de itens de pedido
│   │
│   ├── entities/                       # Entidades JPA (Modelo de Domínio)
│   │   ├── Product.java
│   │   ├── Category.java
│   │   ├── User.java                          # Implementa UserDetails
│   │   ├── Role.java                          # Implementa GrantedAuthority
│   │   ├── Order.java
│   │   ├── OrderItem.java
│   │   ├── OrderItemPk.java
│   │   ├── Payment.java
│   │   └── OrderStatus.java
│   │
│   ├── projections/                    # Projeções JPA
│   │   └── UserDetailsProjection.java
│   │
│   └── dto/                            # Data Transfer Objects
│       ├── ProductDTO.java
│       ├── UserDTO.java                       # DTO de usuário (sem senha)
│       ├── OrderDTO.java                      # DTO de pedido
│       ├── OrderItemDTO.java                  # DTO de item de pedido
│       ├── ClientDTO.java                     # DTO de cliente (dentro do pedido)
│       ├── PaymentDTO.java                    # DTO de pagamento
│       ├── CustomError.java
│       ├── FieldMessage.java
│       └── ValidationError.java
│
├── src/main/resources/
│   ├── application.properties          # Configurações principais
│   ├── application-test.properties     # Configurações de teste
│   ├── import.sql                      # Dados iniciais (seed)
│   └── META-INF/
│       └── additional-spring-configuration-metadata.json
│
├── documentacao/                       # Documentação técnica
│   ├── autenticacao_autorizacao.md     # Guia completo OAuth2 + JWT
│   ├── excecoes.md                     # Tratamento de exceções
│   ├── jpa.md                          # Guia JPA
│   ├── GuiaRelacionamentoJPA.md        # Relacionamentos JPA
│   └── anotacoes_bean_validation.md    # Bean Validation
│
└── pom.xml                             # Dependências Maven
```

---

## 🛠️ Tecnologias

| Tecnologia | Versão | Descrição |
|------------|--------|-----------|
| Java | 17 | Linguagem de programação |
| Spring Boot | 3.4.1 | Framework para aplicações Java |
| Spring Data JPA | 3.4.1 | Abstração de persistência de dados com Hibernate |
| Spring Web | 3.4.1 | Desenvolvimento de APIs REST |
| Spring Security | 6.4.2 | Framework de segurança e autenticação |
| Spring OAuth2 Authorization Server | 1.4.1 | Servidor de autorização OAuth2 |
| Spring Validation | 3.4.1 | Validação de dados com Bean Validation |
| JWT (JSON Web Token) | - | Tokens de autenticação stateless |
| BCrypt | - | Algoritmo de criptografia de senhas |
| H2 Database | 2.3.232 | Banco de dados em memória (desenvolvimento) |
| Maven | 4.0.0 | Gerenciador de dependências e build |

**Dependências principais:**
```xml
<dependencies>
    <!-- Spring Data JPA -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    
    <!-- Spring Web (REST API) -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    
    <!-- Bean Validation -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-validation</artifactId>
    </dependency>
    
    <!-- Spring Security -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
    
    <!-- OAuth2 Authorization Server -->
    <dependency>
        <groupId>org.springframework.security</groupId>
        <artifactId>spring-security-oauth2-authorization-server</artifactId>
    </dependency>
    
    <!-- H2 Database -->
    <dependency>
        <groupId>com.h2database</groupId>
        <artifactId>h2</artifactId>
        <scope>runtime</scope>
    </dependency>
    
    <!-- Spring Test -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

**Parent POM:**
```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.4.1</version>
    <relativePath/>
</parent>
```

---

## 🚀 Como Executar

### Pré-requisitos
- ☕ Java 17 ou superior instalado
- 📦 Maven instalado (ou usar o wrapper `mvnw`)
- 🔧 IDE (IntelliJ IDEA, Eclipse, VS Code)

### Passos para Execução

**1. Clonar o repositório (se aplicável):**
```bash
git clone <url-do-repositorio>
cd devsuperior-springboot-dscommerce
```

**2. Executar via Maven:**
```bash
# Linux/Mac
./mvnw spring-boot:run

# Windows
mvnw.cmd spring-boot:run
```

**3. Executar via IDE:**
- Abra o projeto na IDE
- Execute a classe `DscommerceApplication.java`

**4. Acessar a aplicação:**
- **API REST:** http://localhost:8080
- **Console H2:** http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:testdb`
  - Username: `sa`
  - Password: (deixe em branco)

### Configuração

**application.properties:**
```properties
spring.profiles.active=${APP_PROFILE:test}
spring.jpa.open-in-view=false

# OAuth2 - Client Credentials
security.client-id=${CLIENT_ID:myclientid}
security.client-secret=${CLIENT_SECRET:myclientsecret}

# JWT - Duração em segundos (86400 = 24 horas)
security.jwt.duration=${JWT_DURATION:86400}

# CORS - Origens permitidas
cors.origins=${CORS_ORIGINS:http://localhost:3000,http://localhost:5173}
```

**application-test.properties:**
```properties
# H2 Database
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.username=sa
spring.datasource.password=

# H2 Console
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# JPA
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.defer-datasource-initialization=true
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

### Variáveis de Ambiente (Opcional)

Para customizar as configurações, defina variáveis de ambiente:

**Linux/Mac:**
```bash
export APP_PROFILE=test
export CLIENT_ID=myclientid
export CLIENT_SECRET=myclientsecret
export JWT_DURATION=86400
export CORS_ORIGINS=http://localhost:3000,http://localhost:5173
```

**Windows (PowerShell):**
```powershell
$env:APP_PROFILE="test"
$env:CLIENT_ID="myclientid"
$env:CLIENT_SECRET="myclientsecret"
$env:JWT_DURATION="86400"
$env:CORS_ORIGINS="http://localhost:3000,http://localhost:5173"
```

### Testar Autenticação

**1. Obter Token JWT:**
```bash
curl -X POST http://localhost:8080/oauth2/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -H "Authorization: Basic bXljbGllbnRpZDpteWNsaWVudHNlY3JldA==" \
  -d "grant_type=password" \
  -d "username=maria@gmail.com" \
  -d "password=123456"
```

**2. Usar Token na API:**
```bash
curl -X GET http://localhost:8080/products \
  -H "Authorization: Bearer <seu-token-jwt>"
```

**Usuários de teste:**
| Email | Senha | Roles |
|-------|-------|-------|
| alex@gmail.com | 123456 | ROLE_OPERATOR |
| maria@gmail.com | 123456 | ROLE_OPERATOR, ROLE_ADMIN |


---

## 🏗️ Arquitetura do Sistema

### Visão Geral

O projeto segue uma **arquitetura em camadas (Layered Architecture)** com separação clara de responsabilidades, incluindo **camada de segurança OAuth2 + JWT**:

```
┌─────────────────────────────────────────────────────────────┐
│                     CLIENTE HTTP                            │
│              (Postman, Browser, Apps)                       │
└────────────────────────┬────────────────────────────────────┘
                         │ HTTP Request (JSON + JWT Token)
                         ↓
┌─────────────────────────────────────────────────────────────┐
│                   SECURITY LAYER                            │
│          (@Configuration + SecurityFilterChain)             │
├─────────────────────────────────────────────────────────────┤
│  • AuthorizationServerConfig (OAuth2)                       │
│    - Emite tokens JWT                                       │
│    - Valida credenciais (username/password)                 │
│    - Customiza claims do token                              │
│                                                             │
│  • ResourceServerConfig                                     │
│    - Valida JWT em todas as requisições                     │
│    - Extrai authorities (roles) do token                    │
│    - Aplica regras de autorização                           │
│                                                             │
│  • CorsConfig                                               │
│    - Permite requisições de origens diferentes              │
│                                                             │
│  • Custom Grant Type (Password)                             │
│    - CustomPasswordAuthenticationConverter                  │
│    - CustomPasswordAuthenticationProvider                   │
│    - CustomPasswordAuthenticationToken                      │
└────────────────────────┬────────────────────────────────────┘
                         │ JWT validado + authorities extraídas
                         ↓
┌─────────────────────────────────────────────────────────────┐
│                  PRESENTATION LAYER                         │
│                     (@RestController)                       │
├─────────────────────────────────────────────────────────────┤
│  • ProductController                                        │
│    - @PreAuthorize("hasRole('ROLE_ADMIN')")                 │
│    - Recebe requisições HTTP                                │
│    - Valida parâmetros de entrada (@Valid)                  │
│    - Retorna ResponseEntity<DTO>                            │
│    - Define rotas e métodos HTTP                            │
│                                                             │
│  • ControllerExceptionHandler (@ControllerAdvice)           │
│    - Trata exceções globalmente                             │
│    - Retorna respostas padronizadas de erro                 │
└────────────────────────┬────────────────────────────────────┘
                         │ ProductDTO
                         ↓
┌─────────────────────────────────────────────────────────────┐
│                   BUSINESS LAYER                            │
│                      (@Service)                             │
├─────────────────────────────────────────────────────────────┤
│  • ProductService                                           │
│    - Implementa regras de negócio                           │
│    - Gerencia transações (@Transactional)                   │
│    - Converte Entity ↔ DTO                                  │
│    - Lança exceções de negócio                              │
│                                                             │
│  • UserService (implements UserDetailsService)              │
│    - Carrega dados do usuário para autenticação             │
│    - Busca usuário + roles do banco                         │
└────────────────────────┬────────────────────────────────────┘
                         │ Entity (Product, User)
                         ↓
┌─────────────────────────────────────────────────────────────┐
│                 PERSISTENCE LAYER                           │
│                    (@Repository)                            │
├─────────────────────────────────────────────────────────────┤
│  • ProductRepository (JpaRepository)                        │
│    - Abstração de acesso a dados                            │
│    - Operações CRUD automáticas                             │
│    - Queries derivadas de métodos                           │
│                                                             │
│  • UserRepository                                           │
│    - Query customizada para buscar User + Roles             │
│    - Projection para UserDetailsProjection                  │
└────────────────────────┬────────────────────────────────────┘
                         │ SQL/JDBC
                         ↓
┌─────────────────────────────────────────────────────────────┐
│                    DATABASE LAYER                           │
│                  (H2 / PostgreSQL)                          │
├─────────────────────────────────────────────────────────────┤
│  • tb_product, tb_category                                  │
│  • tb_user, tb_role, tb_user_role                           │
│  • tb_order, tb_order_item, tb_payment                      │
└─────────────────────────────────────────────────────────────┘
```

### Fluxo de Autenticação e Autorização

**1. Login (Obter Token JWT):**
```
Cliente envia: POST /oauth2/token
  - grant_type=password
  - username=maria@gmail.com
  - password=123456
  - Authorization: Basic base64(clientId:clientSecret)
    ↓
AuthorizationServerConfig
  ↓
CustomPasswordAuthenticationConverter
  - Extrai username e password da requisição
    ↓
CustomPasswordAuthenticationProvider
  - UserService.loadUserByUsername(username)
  - PasswordEncoder.matches(password, user.password)
  - Extrai roles do usuário
  - Gera JWT com claims customizadas
    ↓
Retorna: 
{
  "access_token": "eyJhbGc...",
  "token_type": "Bearer",
  "expires_in": 86400,
  "scope": "read write"
}
```

**2. Acessar API Protegida:**
```
Cliente envia: GET /products/1
  - Authorization: Bearer eyJhbGc...
    ↓
ResourceServerConfig
  ↓
OAuth2ResourceServerFilter
  - Valida assinatura do JWT (RSA)
  - Verifica expiração
  - Extrai authorities do token
    ↓
JwtAuthenticationConverter
  - Converte claims "authorities" em GrantedAuthority
  - Cria Authentication com roles
    ↓
@PreAuthorize("hasRole('ROLE_ADMIN')")
  - Verifica se usuário tem a role necessária
  - ✅ Autorizado → executa método
  - ❌ Negado → 403 Forbidden
    ↓
ProductController.findById(1)
  ↓
ProductService.findById(1)
  ↓
ProductRepository.findById(1)
  ↓
Retorna ProductDTO
```

### Fluxo de Dados (CRUD)

**Requisição (Cliente → Servidor):**
```
1. Cliente HTTP envia request (JSON)
   ↓
2. Controller recebe e extrai dados
   ↓
3. Controller chama Service passando DTO
   ↓
4. Service converte DTO → Entity
   ↓
5. Service chama Repository com Entity
   ↓
6. Repository executa operação SQL
   ↓
7. Banco de Dados persiste/busca dados
```

**Resposta (Servidor → Cliente):**
```
1. Banco retorna registros
   ↓
2. Repository retorna Entity
   ↓
3. Service recebe Entity
   ↓
4. Service converte Entity → DTO
   ↓
5. Service retorna DTO para Controller
   ↓
6. Controller monta ResponseEntity
   ↓
7. Cliente recebe resposta JSON
```

### Camadas e Responsabilidades

| Camada | Anotação | Responsabilidades | Exemplo |
|--------|----------|-------------------|---------|
| **Presentation** | `@RestController` | • Receber requisições HTTP<br>• Validar entrada<br>• Serializar/deserializar JSON<br>• Retornar códigos HTTP | `ProductController` |
| **Business** | `@Service` | • Regras de negócio<br>• Gerenciar transações<br>• Conversão Entity/DTO<br>• Tratamento de exceções | `ProductService` |
| **Persistence** | `@Repository` | • Acesso ao banco de dados<br>• Operações CRUD<br>• Queries customizadas | `ProductRepository` |
| **Domain** | `@Entity` | • Representar modelo de domínio<br>• Definir relacionamentos<br>• Mapeamento ORM | `Product`, `Order` |
| **Data Transfer** | POJO | • Transferir dados entre camadas<br>• Não expor entidades<br>• Controlar dados expostos | `ProductDTO` |

### Componentes Transversais

```
┌────────────────────────────────────────────────────────────┐
│                CROSS-CUTTING CONCERNS                      │
├────────────────────────────────────────────────────────────┤
│                                                            │
│  ┌──────────────────────────────────────────────────────┐  │
│  │          @ControllerAdvice                           │  │
│  │   • Tratamento global de exceções                    │  │
│  │   • Intercepta erros de todas as camadas             │  │
│  │   • Retorna respostas padronizadas                   │  │
│  └──────────────────────────────────────────────────────┘  │
│                                                            │
│  ┌──────────────────────────────────────────────────────┐  │
│  │          @Transactional                              │  │
│  │   • Gerenciamento de transações                      │  │
│  │   • Controle de commit/rollback                      │  │
│  │   • Isolamento e propagação                          │  │
│  └──────────────────────────────────────────────────────┘  │
│                                                            │
│  ┌──────────────────────────────────────────────────────┐  │
│  │          Spring Data JPA                             │  │
│  │   • Abstração de persistência                        │  │
│  │   • Geração automática de queries                    │  │
│  │   • Gerenciamento de EntityManager                   │  │
│  └──────────────────────────────────────────────────────┘  │
│                                                            │
└────────────────────────────────────────────────────────────┘
```

### Princípios Aplicados

| Princípio | Descrição | Implementação |
|-----------|-----------|---------------|
| **Separation of Concerns** | Cada camada tem uma responsabilidade específica | Controller (HTTP) ≠ Service (Negócio) ≠ Repository (Dados) |
| **Dependency Injection** | Inversão de controle via Spring | `@Autowired` em vez de `new` |
| **Single Responsibility** | Cada classe tem apenas um motivo para mudar | ProductService só cuida de lógica de produtos |
| **Open/Closed Principle** | Aberto para extensão, fechado para modificação | JpaRepository pode ser estendido sem alteração |
| **DRY (Don't Repeat Yourself)** | Reutilização de código | Spring Data JPA gera métodos CRUD automaticamente |
| **DTO Pattern** | Não expor entidades diretamente | ProductDTO protege a estrutura interna |

### Padrões de Projeto Utilizados

```
📦 Repository Pattern
   └─ ProductRepository extends JpaRepository
      • Abstração de acesso a dados
      • Desacopla lógica de negócio da persistência

📦 DTO Pattern
   └─ ProductDTO, CustomError
      • Transferência de dados entre camadas
      • Controle sobre dados expostos

📦 Service Layer Pattern
   └─ ProductService
      • Centraliza lógica de negócio
      • Reutilização em múltiplos controllers

📦 Dependency Injection
   └─ @Autowired
      • Inversão de controle
      • Facilita testes e manutenção

📦 Exception Handler Pattern
   └─ @ControllerAdvice + @ExceptionHandler
      • Tratamento centralizado de exceções
      • Respostas consistentes
```

### Exemplo de Fluxo Completo

**Cenário: Buscar produto por ID**

```java
// 1. CONTROLLER - Recebe requisição HTTP
@GetMapping("/{id}")
public ResponseEntity<ProductDTO> findById(@PathVariable Long id) {
    ProductDTO dto = service.findById(id);  // Delega para Service
    return ResponseEntity.ok(dto);           // Retorna HTTP 200
}

// 2. SERVICE - Processa lógica de negócio
@Transactional(readOnly = true)
public ProductDTO findById(Long id) {
    Product product = repository.findById(id)  // Busca no Repository
        .orElseThrow(() -> new ResourceNotFoundException("Recurso não encontrado"));
    return new ProductDTO(product);  // Converte Entity → DTO
}

// 3. REPOSITORY - Acessa banco de dados
public interface ProductRepository extends JpaRepository<Product, Long> {
    // findById() herdado - executa: SELECT * FROM tb_product WHERE id = ?
}

// 4. ENTITY - Mapeamento ORM
@Entity
@Table(name = "tb_product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // ... outros campos
}

// 5. DTO - Transferência de dados
public class ProductDTO {
    private Long id;
    private String name;
    private Double price;
    // ... construtor que recebe Product
}
```

### Vantagens da Arquitetura

| Vantagem | Benefício |
|----------|-----------|
| ✅ **Manutenibilidade** | Mudanças em uma camada não afetam as outras |
| ✅ **Testabilidade** | Cada camada pode ser testada independentemente |
| ✅ **Escalabilidade** | Fácil adicionar novas funcionalidades |
| ✅ **Reusabilidade** | Services podem ser usados por múltiplos controllers |
| ✅ **Segurança** | DTOs protegem a estrutura interna das entidades |
| ✅ **Performance** | `@Transactional` otimiza acesso ao banco |
| ✅ **Clareza** | Responsabilidades bem definidas facilitam entendimento |

---





---

## 🔐 Autenticação e Autorização

### Visão Geral

O projeto implementa um sistema robusto de autenticação e autorização utilizando:

- **OAuth 2.0** - Protocolo de autorização
- **JWT (JSON Web Tokens)** - Tokens stateless assinados com RSA
- **Spring Security** - Framework de segurança
- **BCrypt** - Criptografia de senhas
- **Custom Grant Type (Password)** - Fluxo de autenticação com username/password

### Arquitetura de Segurança

```
┌────────────────────────────────────────────────────────────┐
│                  AUTHORIZATION SERVER                      │
│              (Emite e valida tokens JWT)                   │
├────────────────────────────────────────────────────────────┤
│  • AuthorizationServerConfig (@Order(2))                   │
│    - Registra clientes OAuth2                              │
│    - Configura duração dos tokens                          │
│    - Gera chaves RSA para assinatura                       │
│    - Customiza claims do JWT                               │
│                                                            │
│  • Custom Grant Type (Password)                            │
│    - CustomPasswordAuthenticationConverter                 │
│    - CustomPasswordAuthenticationProvider                  │
│    - CustomPasswordAuthenticationToken                     │
│    - CustomUserAuthorities                                 │
└────────────────────────────────────────────────────────────┘
                              ↓
┌────────────────────────────────────────────────────────────┐
│                    RESOURCE SERVER                         │
│           (Protege APIs e valida tokens)                   │
├────────────────────────────────────────────────────────────┤
│  • ResourceServerConfig (@Order(3))                        │
│    - Valida assinatura JWT (RSA)                           │
│    - Extrai authorities do token                           │
│    - Configura CORS                                        │
│    - Aplica regras de autorização                          │
│                                                            │
│  • Security Filter Chains:                                 │
│    1. H2 Console (profile test)                            │
│    2. Authorization Server endpoints                       │
│    3. Resource Server (APIs REST)                          │
└────────────────────────────────────────────────────────────┘
```

### Como Funciona

#### 1️⃣ **Login - Obter Token JWT**

**Requisição:**
```http
POST http://localhost:8080/oauth2/token
Content-Type: application/x-www-form-urlencoded
Authorization: Basic bXljbGllbnRpZDpteWNsaWVudHNlY3JldA==

grant_type=password
&username=maria@gmail.com
&password=123456
```

**Authorization Header:**
```bash
# Formato: Basic base64(clientId:clientSecret)
echo -n "myclientid:myclientsecret" | base64
# Resultado: bXljbGllbnRpZDpteWNsaWVudHNlY3JldA==
```

**Resposta (200 OK):**
```json
{
  "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJteWNsaWVudGlkIiwiYXVkIjpbIm15Y2xpZW50aWQiXSwibmJmIjoxNzA4NjIzODQ3LCJzY29wZSI6WyJyZWFkIiwid3JpdGUiXSwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiIsIlJPTEVfT1BFUkFUT1IiXSwidXNlcm5hbWUiOiJtYXJpYUBnbWFpbC5jb20iLCJpc3MiOiJodHRwOi8vbG9jYWxob3N0OjgwODAiLCJleHAiOjE3MDg3MTAyNDcsImlhdCI6MTcwODYyMzg0N30...",
  "token_type": "Bearer",
  "expires_in": 86400,
  "scope": "read write"
}
```

**Estrutura do JWT:**
```json
{
  "sub": "myclientid",
  "aud": ["myclientid"],
  "nbf": 1708623847,
  "scope": ["read", "write"],
  "authorities": ["ROLE_ADMIN", "ROLE_OPERATOR"],
  "username": "maria@gmail.com",
  "iss": "http://localhost:8080",
  "exp": 1708710247,
  "iat": 1708623847
}
```

#### 2️⃣ **Acessar API Protegida**

**Requisição:**
```http
GET http://localhost:8080/products/1
Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...
```

**Fluxo de Validação:**
```
1. OAuth2ResourceServerFilter intercepta requisição
   ↓
2. Valida assinatura JWT com chave pública RSA
   ↓
3. Verifica expiração do token
   ↓
4. JwtAuthenticationConverter extrai "authorities"
   ↓
5. @PreAuthorize verifica se usuário tem role necessária
   ↓
6. ✅ Autorizado → executa método
   ❌ Negado → 403 Forbidden
```

### Configurações

**application.properties:**
```properties
# OAuth2 Client Credentials
security.client-id=${CLIENT_ID:myclientid}
security.client-secret=${CLIENT_SECRET:myclientsecret}

# JWT - Duração em segundos (86400 = 24 horas)
security.jwt.duration=${JWT_DURATION:86400}

# CORS - Origens permitidas
cors.origins=${CORS_ORIGINS:http://localhost:3000,http://localhost:5173}
```

### Roles e Permissões

**Roles disponíveis:**
- `ROLE_OPERATOR` - Operador (leitura e escrita básica)
- `ROLE_ADMIN` - Administrador (todas as permissões)

**Usuários de teste (import.sql):**
```sql
-- Alex: ROLE_OPERATOR
INSERT INTO tb_user (name, email, password, phone, birth_date) 
VALUES ('Alex', 'alex@gmail.com', '$2y$10$EL1Oja...', '99999999', '1990-07-25');

-- Maria: ROLE_OPERATOR + ROLE_ADMIN
INSERT INTO tb_user (name, email, password, phone, birth_date) 
VALUES ('Maria', 'maria@gmail.com', '$2y$10$EL1Oja...', '88888888', '1992-05-15');
```

**Senha padrão (BCrypt):** `123456`

### Controle de Acesso nos Endpoints

```java
// Permitir acesso público
@GetMapping("/{id}")
public ResponseEntity<ProductDTO> findById(@PathVariable Long id) {
    ProductDTO dto = service.findById(id);
    return ResponseEntity.ok(dto);
}

// Apenas usuários autenticados
@PreAuthorize("isAuthenticated()")
@GetMapping
public ResponseEntity<Page<ProductDTO>> findAll(Pageable pageable) {
    Page<ProductDTO> dto = service.findAll(pageable);
    return ResponseEntity.ok().body(dto);
}

// Apenas ADMIN
@PreAuthorize("hasRole('ROLE_ADMIN')")
@PostMapping
public ResponseEntity<ProductDTO> insert(@Valid @RequestBody ProductDTO dto) {
    dto = service.insert(dto);
    URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/{id}")
        .buildAndExpand(dto.getId())
        .toUri();
    return ResponseEntity.created(uri).body(dto);
}

// ADMIN ou OPERATOR
@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_OPERATOR')")
@PutMapping("/{id}")
public ResponseEntity<ProductDTO> update(
    @PathVariable Long id, 
    @Valid @RequestBody ProductDTO dto
) {
    dto = service.update(id, dto);
    return ResponseEntity.ok().body(dto);
}
```

### CORS (Cross-Origin Resource Sharing)

Configurado para permitir requisições de aplicações frontend:

```java
@Bean
CorsConfigurationSource corsConfigurationSource() {
    String[] origins = corsOrigins.split(",");
    
    CorsConfiguration corsConfig = new CorsConfiguration();
    corsConfig.setAllowedOriginPatterns(Arrays.asList(origins));
    corsConfig.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "DELETE", "PATCH"));
    corsConfig.setAllowCredentials(true);
    corsConfig.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
    
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", corsConfig);
    return source;
}
```

### Documentação Completa

Para detalhes completos sobre a implementação de autenticação e autorização, consulte:

📄 **[documentacao/autenticacao_autorizacao.md](documentacao/autenticacao_autorizacao.md)**

Este documento contém:
- Explicação detalhada de todos os componentes
- Diagramas de fluxo
- Estrutura do JWT
- Exemplos de uso com cURL, JavaScript e Axios
- Troubleshooting de erros comuns

---

## 🌐 Controller (Camada de Apresentação)

O Controller é responsável por receber as requisições HTTP, delegar o processamento para a camada de serviço e retornar as respostas adequadas.

### ProductController

```java
@RestController
@RequestMapping(value="/products")
public class ProductController {
    @Autowired
    private ProductService service;

    @GetMapping(value = "/{id}")
    public ResponseEntity<ProductDTO> findById(@PathVariable Long id) {
        ProductDTO dto = service.findById(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<Page<ProductDTO>> findAll(Pageable pageable) {
        Page<ProductDTO> dto = service.findAll(pageable);
        return ResponseEntity.ok().body(dto);
    }

    @PostMapping
    public ResponseEntity<ProductDTO> insert(@RequestBody ProductDTO dto) {
        dto = service.insert(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(dto.getId())
                .toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<ProductDTO> update(@PathVariable Long id, @RequestBody ProductDTO dto) {
        dto = service.update(id, dto);
        return ResponseEntity.ok().body(dto);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
```

### Endpoints Disponíveis

| Método | Endpoint | Descrição | Status de Sucesso |
|--------|----------|-----------|-------------------|
| GET | `/products/{id}` | Buscar produto por ID | 200 OK |
| GET | `/products` | Listar produtos (paginado) | 200 OK |
| POST | `/products` | Criar novo produto | 201 Created |
| PUT | `/products/{id}` | Atualizar produto | 200 OK |
| DELETE | `/products/{id}` | Deletar produto | 204 No Content |

**Características:**
- ✅ Usa `@RestController` para APIs REST
- ✅ `ResponseEntity` para controle total das respostas HTTP
- ✅ `@PathVariable` para capturar parâmetros da URL
- ✅ `@RequestBody` para receber dados JSON
- ✅ Header `Location` no POST (RFC 7231)
- ✅ Status HTTP adequados para cada operação

### UserController

Controller responsável por gerenciar endpoints relacionados ao usuário autenticado.

```java
@RestController
@RequestMapping(value = "/users")
public class UserController {

    @Autowired
    private UserService service;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_CLIENT')")
    @GetMapping(value = "/me")
    public ResponseEntity<UserDTO> getMe() {
        UserDTO dto = service.getMe();
        return ResponseEntity.ok(dto);
    }
}
```

### Endpoints de Usuário

| Método | Endpoint | Descrição | Autorização | Status de Sucesso |
|--------|----------|-----------|-------------|-------------------|
| GET | `/users/me` | Obter dados do usuário autenticado | `ROLE_ADMIN` ou `ROLE_CLIENT` | 200 OK |

**Características:**
- ✅ Protegido por autenticação JWT
- ✅ Retorna dados do usuário logado extraídos do token
- ✅ Utiliza `@PreAuthorize` para controle de acesso
- ✅ Não requer passar ID (obtido do contexto de segurança)

**Exemplo de Requisição:**

```http
GET http://localhost:8080/users/me
Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...
Accept: application/json
```

**Exemplo de Resposta:**

```json
{
    "id": 2,
    "name": "Maria Brown",
    "email": "maria@gmail.com",
    "phone": "977777777",
    "birthDate": "2001-07-25",
    "roles": ["ROLE_CLIENT"]
}
```

### OrderController

Controller responsável por gerenciar endpoints relacionados a pedidos.

```java
@RestController
@RequestMapping(value="/orders")
public class OrderController {

    @Autowired
    private OrderService service;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/{id}")
    public ResponseEntity<OrderDTO> findById(@PathVariable Long id){
        OrderDTO dto = service.findById(id);
        return ResponseEntity.ok(dto);
    }

    @PreAuthorize("hasRole('ROLE_OPERATOR')")
    @PostMapping
    public ResponseEntity<OrderDTO> insert(@Valid @RequestBody OrderDTO dto){
        dto = service.insert(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(dto.getId()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }
}
```

### Endpoints de Pedidos

| Método | Endpoint | Descrição | Autorização | Status de Sucesso |
|--------|----------|-----------|-------------|-------------------|
| GET | `/orders/{id}` | Buscar pedido por ID | `ROLE_ADMIN` | 200 OK |
| POST | `/orders` | Criar novo pedido | `ROLE_OPERATOR` | 201 Created |

**Características:**
- ✅ Protegido por autenticação JWT
- ✅ Validação de entrada com `@Valid`
- ✅ Associação automática do usuário autenticado como cliente do pedido
- ✅ Status inicial definido como `WAITING_PAYMENT`
- ✅ Momento do pedido registrado automaticamente com `Instant.now()`
- ✅ Header `Location` no POST com URI do recurso criado
- ✅ Validação de pelo menos 1 item no pedido (`@NotEmpty`)

**Exemplo de Requisição POST:**

```http
POST http://localhost:8080/orders
Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...
Content-Type: application/json

{
  "items": [
    {
      "productId": 1,
      "quantity": 2
    },
    {
      "productId": 3,
      "quantity": 1
    }
  ]
}
```

**Exemplo de Resposta POST (201 Created):**

```json
{
    "id": 4,
    "moment": "2026-02-26T15:30:00Z",
    "status": "WAITING_PAYMENT",
    "client": {
        "id": 1,
        "name": "Alex Green"
    },
    "payment": null,
    "items": [
        {
            "productId": 1,
            "name": "The Lord of the Rings",
            "price": 90.5,
            "quantity": 2,
            "subTotal": 181.0
        },
        {
            "productId": 3,
            "name": "Macbook Pro",
            "price": 1250.0,
            "quantity": 1,
            "subTotal": 1250.0
        }
    ],
    "total": 1431.0
}
```

**Exemplo de Requisição GET:**

```http
GET http://localhost:8080/orders/1
Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...
Accept: application/json
```

**Exemplo de Resposta GET (200 OK):**

```json
{
    "id": 1,
    "moment": "2022-07-25T13:00:00Z",
    "status": "PAID",
    "client": {
        "id": 1,
        "name": "Alex Green"
    },
    "payment": {
        "id": 1,
        "moment": "2022-07-25T15:00:00Z"
    },
    "items": [
        {
            "productId": 1,
            "name": "The Lord of the Rings",
            "price": 90.5,
            "quantity": 2,
            "subTotal": 181.0
        },
        {
            "productId": 3,
            "name": "Macbook Pro",
            "price": 1250.0,
            "quantity": 1,
            "subTotal": 1250.0
        }
    ],
    "total": 1431.0
}
```

**Validações Implementadas:**

| Campo | Validação | Mensagem |
|-------|-----------|----------|
| `items` | `@NotEmpty` | "A ordem deve conter pelo menos um item" |
| `productId` | Deve existir no banco | Lança exceção se produto não encontrado |
| `quantity` | Número positivo | Validado na lógica de negócio |

---

## ⚙️ Service (Camada de Lógica de Negócio)

A camada de serviço contém as regras de negócio e gerencia transações.

### ProductService

```java
@Service
public class ProductService {
    @Autowired
    private ProductRepository repository;

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        Product product = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Recurso não encontrado"));
        return new ProductDTO(product);
    }

    @Transactional(readOnly = true)
    public Page<ProductDTO> findAll(Pageable pageable) {
        Page<Product> result = repository.findAll(pageable);
        return result.map(x -> new ProductDTO(x));
    }

    @Transactional
    public ProductDTO insert(ProductDTO dto) {
        Product entity = new Product();
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setPrice(dto.getPrice());
        entity.setImgUrl(dto.getImgUrl());
        
        entity = repository.save(entity);
        return new ProductDTO(entity);
    }

    @Transactional
    public ProductDTO update(Long id, ProductDTO dto) {
        try {
            Product entity = repository.getReferenceById(id);
            entity.setName(dto.getName());
            entity.setDescription(dto.getDescription());
            entity.setPrice(dto.getPrice());
            entity.setImgUrl(dto.getImgUrl());
            
            entity = repository.save(entity);
            return new ProductDTO(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Recurso não encontrado");
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Recurso não encontrado");
        }
        try {
            repository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new ResourceNotFoundException("Falha de integridade referencial");
        }
    }
}
```

### Boas Práticas Implementadas

| Prática | Descrição |
|---------|-----------|
| `@Transactional(readOnly = true)` | Otimiza operações de leitura |
| `@Transactional` | Garante atomicidade em operações de escrita |
| `@Transactional(propagation = SUPPORTS)` | Usado em delete para permitir flexibilidade transacional |
| `orElseThrow()` | Tratamento elegante de Optional |
| `getReferenceById()` | Evita SELECT desnecessário no update |
| `existsById()` | Valida existência antes de deletar |
| Conversão Entity → DTO | Nunca expõe entidades JPA |
| Try-catch específicos | Captura exceções do JPA e lança exceções de negócio |

### UserService

Service responsável por gerenciar usuários e implementar o `UserDetailsService` para autenticação.

```java
@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<UserDetailsProjection> result = repository.searchUserAndRolesByEmail(username);
        if (result.size() == 0) {
            throw new UsernameNotFoundException("Email not found");
        }

        User user = new User();
        user.setEmail(result.get(0).getUsername());
        user.setPassword(result.get(0).getPassword());
        for (UserDetailsProjection projection : result) {
            user.addRole(new Role(projection.getRoleId(), projection.getAuthority()));
        }

        return user;
    }

    protected User authenticated() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Jwt jwtPrincipal = (Jwt) authentication.getPrincipal();
            String username = jwtPrincipal.getClaim("username");
            return repository.findByEmail(username).get();
        } catch (Exception e) {
            throw new UsernameNotFoundException("Invalid user");
        }
    }

    @Transactional(readOnly = true)
    public UserDTO getMe() {
        User entity = authenticated();
        return new UserDTO(entity);
    }
}
```

**Responsabilidades:**

1. **Autenticação (`loadUserByUsername`)**
   - Carrega usuário do banco de dados
   - Busca roles através de query customizada
   - Retorna objeto `UserDetails` para Spring Security

2. **Obter usuário autenticado (`authenticated`)**
   - Extrai dados do contexto de segurança
   - Decodifica JWT e obtém username
   - Busca usuário completo no banco

3. **Retornar dados do usuário logado (`getMe`)**
   - Utiliza o método `authenticated()` para obter usuário
   - Converte entidade para DTO
   - Garante que apenas dados seguros são expostos

### OrderService

Service responsável por gerenciar a lógica de negócio de pedidos.

```java
@Service
public class OrderService {

    @Autowired
    private OrderRepository repository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductRepository productRepository;

    @Transactional(readOnly = true)
    public OrderDTO findById(Long id) {
        Order order = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Recurso não encontrado"));
        return new OrderDTO(order);
    }

    @Transactional
    public OrderDTO insert(OrderDTO dto) {
        Order order = new Order();
        
        // Define momento atual
        order.setMoment(Instant.now());
        
        // Define status inicial
        order.setOrderStatus(OrderStatus.WAITING_PAYMENT);
        
        // Associa usuário autenticado como cliente
        User user = userService.authenticated();
        order.setClient(user);
        
        // Adiciona itens ao pedido
        for (OrderItemDTO itemDTO : dto.getItems()) {
            Product product = productRepository.getReferenceById(itemDTO.getProductId());
            OrderItem item = new OrderItem(order, product, itemDTO.getQuantity(), product.getPrice());
            order.getItems().add(item);
        }
        
        // Persiste pedido e itens
        repository.save(order);
        orderItemRepository.saveAll(order.getItems());
        
        return new OrderDTO(order);
    }
}
```

**Responsabilidades:**

1. **Buscar pedido por ID (`findById`)**
   - Busca pedido no repositório
   - Lança exceção se não encontrado
   - Converte entidade para DTO com todos os relacionamentos

2. **Inserir novo pedido (`insert`)**
   - **Momento:** Define automaticamente com `Instant.now()`
   - **Status:** Inicia como `WAITING_PAYMENT`
   - **Cliente:** Obtém usuário autenticado do contexto de segurança
   - **Itens:** Itera sobre os itens do DTO e:
     - Busca produto no banco via `getReferenceById()`
     - Captura preço atual do produto (não confia no cliente)
     - Cria `OrderItem` associando order + product + quantity + price
   - **Persistência:** Salva order e depois todos os items em lote
   - **Transação:** Tudo executado em uma única transação (@Transactional)

**Fluxo de Inserção:**

```
1. Cliente envia JSON com lista de items (productId + quantity)
   ↓
2. OrderService cria entidade Order vazia
   ↓
3. Define moment = Instant.now()
   ↓
4. Define status = WAITING_PAYMENT
   ↓
5. Busca User autenticado via UserService.authenticated()
   ↓
6. Define order.client = user
   ↓
7. Para cada item do DTO:
   - Busca Product do banco
   - Captura price do produto (segurança: não confia no cliente)
   - Cria OrderItem(order, product, quantity, price)
   - Adiciona item à lista order.items
   ↓
8. Persiste Order no banco
   ↓
9. Persiste todos OrderItems em lote
   ↓
10. Retorna OrderDTO com dados completos (incluindo total calculado)
```

**Características de Segurança:**

| Aspecto | Implementação |
|---------|---------------|
| **Autenticação** | Usuário deve estar autenticado (token JWT válido) |
| **Autorização** | Requer `ROLE_OPERATOR` no token |
| **Cliente** | Associado automaticamente (não pode ser falsificado) |
| **Preço** | Sempre obtido do banco (cliente não envia preço) |
| **Produto** | Valida existência via `getReferenceById()` |
| **Transação** | Rollback automático em caso de erro |

**Boas Práticas:**

| Prática | Justificativa |
|---------|---------------|
| `@Transactional` | Garante atomicidade: ou salva tudo ou nada |
| `getReferenceById()` | Evita SELECT completo (apenas proxy) |
| `saveAll()` | Batch insert otimizado para itens |
| `Instant.now()` | Timestamp preciso e timezone-aware |
| `authenticated()` | Segurança: cliente vem do token, não do JSON |
| Preço do banco | Evita manipulação de preços pelo cliente |

**Fluxo de Autenticação:**

```
1. Cliente envia username/password
   ↓
2. UserService.loadUserByUsername() é chamado
   ↓
3. Busca usuário e roles no banco
   ↓
4. Spring Security valida senha
   ↓
5. Token JWT é gerado com claims do usuário
   ↓
6. Cliente usa token em requisições subsequentes
   ↓
7. UserService.getMe() retorna dados do token
```

---

## 📦 Repository (Camada de Acesso a Dados)

Interface de acesso ao banco de dados usando Spring Data JPA.

### ProductRepository

```java
public interface ProductRepository extends JpaRepository<Product, Long> {
    // Métodos herdados automaticamente:
    // - Optional<Product> findById(Long id)
    // - Page<Product> findAll(Pageable pageable)
    // - Product save(Product entity)
    // - void deleteById(Long id)
    // - boolean existsById(Long id)
    // - Product getReferenceById(Long id)
}
```

### Métodos Disponíveis (Herdados de JpaRepository)

| Método | Descrição | Retorno |
|--------|-----------|---------|
| `findById(Long id)` | Busca por ID | `Optional<Product>` |
| `findAll()` | Lista todos | `List<Product>` |
| `findAll(Pageable)` | Lista paginado | `Page<Product>` |
| `save(Product)` | Insere ou atualiza | `Product` |
| `deleteById(Long id)` | Deleta por ID | `void` |
| `existsById(Long id)` | Verifica existência | `boolean` |
| `getReferenceById(Long id)` | Obtém referência (proxy) | `Product` |
| `count()` | Conta registros | `long` |

**Vantagens do Spring Data JPA:**
- ✅ Sem necessidade de implementação SQL
- ✅ Suporte automático a paginação
- ✅ Type-safe queries
- ✅ Redução de código boilerplate

### UserRepository

Repository responsável por operações de usuários, incluindo queries customizadas para autenticação.

```java
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    @Query(nativeQuery = true, value = """
        SELECT tb_user.email AS username, tb_role.id AS roleId, tb_role.authority
        FROM tb_user
        INNER JOIN tb_user_role ON tb_user.id = tb_user_role.user_id
        INNER JOIN tb_role ON tb_role.id = tb_user_role.role_id
        WHERE tb_user.email = :email
    """)
    List<UserDetailsProjection> searchUserAndRolesByEmail(String email);
}
```

**Métodos Customizados:**

| Método | Descrição | Retorno |
|--------|-----------|---------|
| `findByEmail(String email)` | Busca usuário por email | `Optional<User>` |
| `searchUserAndRolesByEmail(String email)` | Busca usuário com roles (projeção) | `List<UserDetailsProjection>` |

**Por que usar Native Query com Projeção?**

1. **Performance**: Evita carregar objetos completos desnecessariamente
2. **Simplicidade**: Retorna apenas os campos necessários para autenticação
3. **Flexibilidade**: Permite JOIN otimizado entre tabelas

**UserDetailsProjection:**

```java
public interface UserDetailsProjection {
    String getUsername();
    String getPassword();
    Long getRoleId();
    String getAuthority();
}
```

Esta projeção é usada pelo `UserService` para construir o objeto `UserDetails` necessário para autenticação.

---

## 🗂️ Entities (Modelo de Domínio)

Representação das tabelas do banco de dados usando JPA.

### Product

```java
@Entity
@Table(name = "tb_product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    private Double price;
    private String imgUrl;

    @ManyToMany
    @JoinTable(
        name = "tb_product_category",
        joinColumns = @JoinColumn(name = "product_id"),
        inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories = new HashSet<>();
    
    @OneToMany(mappedBy = "id.product")
    private Set<OrderItem> items = new HashSet<>();
    
    // Construtores, getters e setters
}
```

### Category

```java
@Entity
@Table(name = "tb_category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @ManyToMany(mappedBy = "categories")
    private Set<Product> products = new HashSet<>();
    
    // Construtores, getters e setters
}
```

### User

```java
@Entity
@Table(name = "tb_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    
    @Column(unique = true)
    private String email;
    private String phone;
    private LocalDate birthDate;
    private String password;

    @OneToMany(mappedBy = "client")
    private List<Order> orders = new ArrayList<>();
    
    // Construtores, getters e setters
}
```

### Order

```java
@Entity
@Table(name = "tb_order")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
    private Instant moment;
    
    private OrderStatus status;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private User client;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private Payment payment;

    @OneToMany(mappedBy = "id.order")
    private Set<OrderItem> items = new HashSet<>();
    
    // Construtores, getters, setters e métodos auxiliares
}
```

### OrderItem (Classe de Associação)

```java
@Entity
@Table(name = "tb_order_item")
public class OrderItem {
    @EmbeddedId
    private OrderItemPK id = new OrderItemPK();
    
    private Integer quantity;
    private Double price;

    // Construtores, getters e setters
}
```

### OrderItemPK (Chave Composta)

```java
@Embeddable
public class OrderItemPK {
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    
    // Construtores, getters, setters, equals e hashCode
}
```

### Payment

```java
@Entity
@Table(name = "tb_payment")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
    private Instant moment;

    @OneToOne
    @MapsId
    private Order order;
    
    // Construtores, getters e setters
}
```

### OrderStatus (Enum)

```java
public enum OrderStatus {
    WAITING_PAYMENT,
    PAID,
    SHIPPED,
    DELIVERED,
    CANCELED
}
```

### Relacionamentos no Modelo

```
User ──────1:N─────→ Order ──────1:1─────→ Payment
                       │
                       │
                     1:N
                       │
                       ↓
                  OrderItem ←────N:1────── Product ←────N:N────→ Category
                (chave composta)
```

| Relacionamento | Tipo | Descrição |
|----------------|------|-----------|
| User ↔ Order | 1:N | Um usuário pode ter vários pedidos |
| Order ↔ Payment | 1:1 | Cada pedido tem um pagamento |
| Order ↔ OrderItem | 1:N | Um pedido tem vários itens |
| Product ↔ OrderItem | 1:N | Um produto pode estar em vários itens |
| Product ↔ Category | N:N | Produtos têm múltiplas categorias |

---

## 📋 DTO (Data Transfer Objects)

Classes para transferência de dados entre camadas, evitando exposição de entidades JPA.

### ProductDTO

```java
package br.com.klsys.dscommerce.dto;

import br.com.klsys.dscommerce.entities.Product;
import jakarta.validation.constraints.*;

public class ProductDTO {
    private Long id;
    
    @NotBlank(message = "Nome não pode estar em branco")
    @Size(min = 3, max = 80, message = "Nome deve ter entre 3 e 80 caracteres")
    private String name;
    
    @NotBlank(message = "Descrição não pode estar em branco")
    @Size(min = 10, message = "Descrição precisa ter no mínimo 10 caracteres")
    private String description;
    
    @Positive(message = "O preço deve ser um valor positivo")
    private Double price;
    
    private String imgUrl;

    public ProductDTO() {
    }

    public ProductDTO(Product entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.description = entity.getDescription();
        this.price = entity.getPrice();
        this.imgUrl = entity.getImgUrl();
    }
    
    // Getters
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public Double getPrice() { return price; }
    public String getImgUrl() { return imgUrl; }
}
```

**Validações aplicadas:**

| Campo | Validação | Regra |
|-------|-----------|-------|
| `name` | `@NotBlank` `@Size` | Obrigatório, 3-80 caracteres |
| `description` | `@NotBlank` `@Size` | Obrigatório, mínimo 10 caracteres |
| `price` | `@Positive` | Deve ser positivo |
| `imgUrl` | - | Opcional |

---

### CustomError

```java
public class CustomError {
    private Instant timestamp;
    private Integer status;
    private String error;
    private String path;

    public CustomError(Instant timestamp, Integer status, String error, String path) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.path = path;
    }
    
    // Getters
}
```

---

### ValidationError

```java
public class ValidationError extends CustomError {

    private List<FieldMessage> errors = new ArrayList<>();

    public ValidationError(Instant timestamp, Integer status, String error, String path) {
        super(timestamp, status, error, path);
    }

    public List<FieldMessage> getErrors() {
        return errors;
    }

    public void addError(String fieldName, String message) {
        errors.add(new FieldMessage(fieldName, message));
    }
}
```

---

### FieldMessage

```java
public class FieldMessage {

    private String fieldName;
    private String message;

    public FieldMessage(String fieldName, String message) {
        this.fieldName = fieldName;
        this.message = message;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getMessage() {
        return message;
    }
}
```

---

### UserDTO

DTO utilizado para transferir dados de usuários autenticados sem expor informações sensíveis (como senha).

```java
package br.com.klsys.dscommerce.dto;

import br.com.klsys.dscommerce.entities.User;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UserDTO {

    private Long id;
    private String name;
    private String email;
    private String phone;
    private LocalDate birthDate;
    private List<String> roles = new ArrayList<>();

    public UserDTO() {
    }

    public UserDTO(User entity) {
        id = entity.getId();
        name = entity.getName();
        email = entity.getEmail();
        phone = entity.getPhone();
        birthDate = entity.getBirthDate();
        for (GrantedAuthority role : entity.getRoles()) {
            roles.add(role.getAuthority());
        }
    }

    // Getters públicos (essenciais para serialização JSON)
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public LocalDate getBirthDate() { return birthDate; }
    public List<String> getRoles() { return roles; }
}
```

**Características:**

- ✅ **Não expõe senha** - Segurança em primeiro lugar
- ✅ **Serialização JSON** - Todos os campos têm getters públicos
- ✅ **Roles como Strings** - Evita referências circulares
- ✅ **LocalDate** - Suportado nativamente pelo Jackson
- ✅ **Construtor da entidade** - Conversão automática User → UserDTO

**Exemplo de resposta JSON:**

```json
{
    "id": 2,
    "name": "Maria Brown",
    "email": "maria@gmail.com",
    "phone": "977777777",
    "birthDate": "2001-07-25",
    "roles": ["ROLE_CLIENT"]
}
```

---

### Vantagens dos DTOs

| Vantagem | Descrição |
|----------|-----------|
| **Segurança** | Não expõe estrutura interna das entidades |
| **Performance** | Trafega apenas dados necessários |
| **Flexibilidade** | Permite diferentes representações da mesma entidade |
| **Desacoplamento** | Mudanças na entidade não afetam a API |
| **Evita Lazy Loading** | Previne exceções de sessão fechada |
| **Validação** | Centraliza regras de validação de entrada |

---

## ✅ Bean Validation (Validação de Dados)

### O que é Bean Validation?

Bean Validation é uma especificação Java para validar dados usando anotações. No Spring Boot, a implementação padrão é o **Hibernate Validator**.

**Benefícios:**
- ✅ Valida dados antes da lógica de negócio
- ✅ Centraliza regras de validação
- ✅ Reduz código boilerplate
- ✅ Retorna mensagens amigáveis ao usuário
- ✅ Previne dados inválidos no banco

---

### Dependência

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

---

### Como Usar no Controller

Para ativar a validação, use `@Valid` nos parâmetros do controller:

```java
@PostMapping
public ResponseEntity<ProductDTO> insert(@Valid @RequestBody ProductDTO dto) {
    dto = service.insert(dto);
    URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(dto.getId())
            .toUri();
    return ResponseEntity.created(uri).body(dto);
}

@PutMapping(value = "/{id}")
public ResponseEntity<ProductDTO> update(
        @PathVariable Long id,
        @Valid @RequestBody ProductDTO dto) {
    dto = service.update(id, dto);
    return ResponseEntity.ok(dto);
}
```

**Observação:** `@Valid` aciona automaticamente as validações definidas no DTO.

---

### Anotações de Validação Usadas

| Anotação | Aplicação | Descrição |
|----------|-----------|-----------|
| `@NotBlank` | `name`, `description` | Não permite valores vazios ou apenas espaços |
| `@Size` | `name`, `description` | Define tamanho mínimo e máximo |
| `@Positive` | `price` | Garante que o valor seja positivo |

---

### Tratamento de Erros de Validação

O `ControllerExceptionHandler` intercepta erros de validação:

```java
@ExceptionHandler(MethodArgumentNotValidException.class)
public ResponseEntity<CustomError> methodArgumentNotValidation(
        MethodArgumentNotValidException e, 
        HttpServletRequest request) {
    
    HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
    ValidationError err = new ValidationError(
        Instant.now(), 
        status.value(), 
        "Dados inválidos", 
        request.getRequestURI()
    );
    
    for (FieldError f : e.getBindingResult().getFieldErrors()) {
        err.addError(f.getField(), f.getDefaultMessage());
    }
    
    return ResponseEntity.status(status).body(err);
}
```

---

### Exemplo de Resposta de Validação

**Request (dados inválidos):**
```http
POST http://localhost:8080/products
Content-Type: application/json

{
  "name": "",
  "description": "Curta",
  "price": -10.0,
  "imgUrl": "http://img.jpg"
}
```

**Response (422 Unprocessable Entity):**
```json
{
  "timestamp": "2026-01-01T10:30:00.123456Z",
  "status": 422,
  "error": "Dados inválidos",
  "path": "/products",
  "errors": [
    {
      "fieldName": "name",
      "message": "Nome não pode estar em branco"
    },
    {
      "fieldName": "description",
      "message": "Descrição precisa ter no mínimo 10 caracteres"
    },
    {
      "fieldName": "price",
      "message": "O preço deve ser um valor positivo"
    }
  ]
}
```

---

### Mensagens Personalizadas

Todas as mensagens são **amigáveis** e **descritivas**:

```java
// ✅ BOM - Mensagens claras
@NotBlank(message = "Nome não pode estar em branco")
@Size(min = 3, max = 80, message = "Nome deve ter entre 3 e 80 caracteres")

// ❌ EVITAR - Mensagens genéricas
@NotBlank(message = "Campo obrigatório")
@Size(min = 3, max = 80, message = "Tamanho inválido")
```

**Vantagens das mensagens amigáveis:**
- Usuário sabe exatamente o que corrigir
- Especifica limites/regras claros
- Melhora experiência do desenvolvedor frontend
- Reduz suporte e dúvidas

---

### Principais Anotações Bean Validation

| Categoria | Anotações | Exemplo de Uso |
|-----------|-----------|----------------|
| **Obrigatório** | `@NotNull`, `@NotBlank`, `@NotEmpty` | Campos não podem ser nulos/vazios |
| **Tamanho** | `@Size`, `@Length` | `@Size(min=3, max=80)` |
| **Numéricos** | `@Min`, `@Max`, `@Positive`, `@Negative` | `@Positive` |
| **Formato** | `@Email`, `@Pattern`, `@URL` | `@Email(message="Email inválido")` |
| **Datas** | `@Past`, `@Future`, `@PastOrPresent` | `@Past` para data de nascimento |

📚 **Documentação completa:** Ver `documentacao/anotacoes_bean_validation.md`

---

## 🧪 Testando a API

### 0. Autenticação - Obter Token JWT

Antes de testar endpoints protegidos, você precisa obter um token JWT:

**Request:**
```http
POST http://localhost:8080/oauth2/token
Content-Type: application/x-www-form-urlencoded
Authorization: Basic bXljbGllbnRpZDpteWNsaWVudHNlY3JldA==

grant_type=password&username=maria@gmail.com&password=123456
```

**Como gerar o Authorization Header:**
```bash
# Formato: Basic base64(clientId:clientSecret)
echo -n "myclientid:myclientsecret" | base64
# Resultado: bXljbGllbnRpZDpteWNsaWVudHNlY3JldA==
```

**Response:** `200 OK`
```json
{
  "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJteWNsaWVudGlkIiwiYXVkIjpbIm15Y2xpZW50aWQiXSwibmJmIjoxNzA4NjIzODQ3LCJzY29wZSI6WyJyZWFkIiwid3JpdGUiXSwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiIsIlJPTEVfT1BFUkFUT1IiXSwidXNlcm5hbWUiOiJtYXJpYUBnbWFpbC5jb20iLCJpc3MiOiJodHRwOi8vbG9jYWxob3N0OjgwODAiLCJleHAiOjE3MDg3MTAyNDcsImlhdCI6MTcwODYyMzg0N30.signature...",
  "token_type": "Bearer",
  "expires_in": 86400,
  "scope": "read write"
}
```

**Usuários disponíveis:**
| Email | Senha | Roles |
|-------|-------|-------|
| alex@gmail.com | 123456 | ROLE_OPERATOR |
| maria@gmail.com | 123456 | ROLE_OPERATOR, ROLE_ADMIN |

**💡 Use o `access_token` recebido nos próximos requests:**
```http
Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...
```

---

### 1. Buscar Produto por ID

**Request:**
```http
GET http://localhost:8080/products/1
```

**🔓 Endpoint público** - Não requer autenticação

**Response:** `200 OK`
```json
{
  "id": 1,
  "name": "The Lord of the Rings",
  "description": "Lorem ipsum dolor sit amet, consectetur adipiscing elit...",
  "price": 90.5,
  "imgUrl": "https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/1-big.jpg"
}
```

---

### 2. Listar Produtos (Paginado)

**Requests:**
```http
GET http://localhost:8080/products
GET http://localhost:8080/products?page=0&size=5
GET http://localhost:8080/products?page=0&size=10&sort=name,asc
GET http://localhost:8080/products?page=1&size=10&sort=price,desc
```

**🔓 Endpoint público** - Não requer autenticação

**Parâmetros de Paginação:**

| Parâmetro | Descrição | Padrão | Exemplo |
|-----------|-----------|--------|---------|
| `page` | Número da página (0-based) | 0 | `page=0` |
| `size` | Quantidade de itens por página | 20 | `size=10` |
| `sort` | Campo e direção de ordenação | - | `sort=name,asc` |

**Response:** `200 OK`
```json
{
  "content": [
    {
      "id": 1,
      "name": "The Lord of the Rings",
      "description": "Lorem ipsum...",
      "price": 90.5,
      "imgUrl": "https://..."
    },
    {
      "id": 2,
      "name": "Smart TV",
      "description": "...",
      "price": 2190.0,
      "imgUrl": "https://..."
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 20,
    "sort": {
      "empty": true,
      "sorted": false,
      "unsorted": true
    },
    "offset": 0,
    "paged": true,
    "unpaged": false
  },
  "totalPages": 2,
  "totalElements": 25,
  "last": false,
  "size": 20,
  "number": 0,
  "first": true,
  "numberOfElements": 20,
  "empty": false
}
```

---

### 3. Inserir Novo Produto

**🔐 Requer:** `ROLE_ADMIN`

**Request:**
```http
POST http://localhost:8080/products
Content-Type: application/json
Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...

{
  "name": "Smart TV 50 polegadas",
  "description": "TV LED 50 polegadas 4K Ultra HD",
  "price": 2199.90,
  "imgUrl": "https://exemplo.com/tv.jpg"
}
```

**Response:** `201 Created`
```json
{
  "id": 26,
  "name": "Smart TV 50 polegadas",
  "description": "TV LED 50 polegadas 4K Ultra HD",
  "price": 2199.90,
  "imgUrl": "https://exemplo.com/tv.jpg"
}
```

**Headers:**
```
Location: http://localhost:8080/products/26
```

**⚠️ Erros possíveis:**
- `401 Unauthorized` - Token inválido ou expirado
- `403 Forbidden` - Usuário não tem `ROLE_ADMIN`

---

### 4. Atualizar Produto Existente

**🔐 Requer:** `ROLE_ADMIN` ou `ROLE_OPERATOR`

**Request:**
```http
PUT http://localhost:8080/products/1
Content-Type: application/json
Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...

{
  "name": "The Lord of the Rings - Edição Especial",
  "description": "Edição especial com ilustrações exclusivas e conteúdo adicional",
  "price": 120.00,
  "imgUrl": "https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/1-big.jpg"
}
```

**Response:** `200 OK`
```json
{
  "id": 1,
  "name": "The Lord of the Rings - Edição Especial",
  "description": "Edição especial com ilustrações exclusivas e conteúdo adicional",
  "price": 120.00,
  "imgUrl": "https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/1-big.jpg"
}
```

---

### 5. Deletar Produto

### 5. Deletar Produto

**🔐 Requer:** `ROLE_ADMIN`

**Request:**
```http
DELETE http://localhost:8080/products/1
Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...
```

**Response:** `204 No Content`
```
(Sem corpo de resposta)
```

**Observações:**
- ✅ Status `204 No Content` indica sucesso
- ✅ Não há corpo na resposta
- ⚠️ Se o ID não existir, retorna `404 Not Found`
- ⚠️ Se houver integridade referencial, retorna erro tratado
- ⚠️ `401 Unauthorized` - Token inválido ou expirado
- ⚠️ `403 Forbidden` - Usuário não tem `ROLE_ADMIN`

---

### 6. Obter Dados do Usuário Autenticado

**🔐 Requer:** `ROLE_ADMIN` ou `ROLE_CLIENT`

**Request:**
```http
GET http://localhost:8080/users/me
Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...
Accept: application/json
```

**Response:** `200 OK`
```json
{
  "id": 2,
  "name": "Maria Brown",
  "email": "maria@gmail.com",
  "phone": "977777777",
  "birthDate": "2001-07-25",
  "roles": ["ROLE_OPERATOR", "ROLE_ADMIN"]
}
```

**Como funciona:**
1. Token JWT é validado
2. Username (email) é extraído dos claims do token
3. Usuário completo é buscado no banco de dados
4. Dados são convertidos para UserDTO (sem senha)
5. JSON é retornado ao cliente

**Possíveis Erros:**

| Status | Erro | Causa |
|--------|------|-------|
| `401 Unauthorized` | Token ausente ou inválido | Não enviou header `Authorization` ou token expirado |
| `403 Forbidden` | Permissão negada | Usuário não tem `ROLE_ADMIN` ou `ROLE_CLIENT` |
| `406 Not Acceptable` | Formato não aceito | Falta header `Accept: application/json` |

**Dica:** Este endpoint é útil para:
- Exibir dados do usuário logado no frontend
- Validar se o token ainda é válido
- Carregar informações do perfil

---

### Testando com cURL

```bash
# 0. LOGIN - Obter Token JWT
curl -X POST http://localhost:8080/oauth2/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -H "Authorization: Basic bXljbGllbnRpZDpteWNsaWVudHNlY3JldA==" \
  -d "grant_type=password&username=maria@gmail.com&password=123456"

# Salvar o token em uma variável
TOKEN="eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9..."

# GET - Buscar por ID (público)
curl -X GET http://localhost:8080/products/1

# GET - Listar todos paginado (público)
curl -X GET "http://localhost:8080/products?page=0&size=5"

# POST - Inserir (requer ROLE_ADMIN)
curl -X POST http://localhost:8080/products \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"name":"Notebook","description":"Notebook Dell","price":3500.00,"imgUrl":"http://example.com/notebook.jpg"}'

# PUT - Atualizar (requer ROLE_ADMIN ou ROLE_OPERATOR)
curl -X PUT http://localhost:8080/products/1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"name":"Notebook Updated","description":"Updated","price":3200.00,"imgUrl":"http://example.com/notebook.jpg"}'

# DELETE - Deletar (requer ROLE_ADMIN)
curl -X DELETE http://localhost:8080/products/1 \
  -H "Authorization: Bearer $TOKEN"

# GET - Obter dados do usuário autenticado (requer autenticação)
curl -X GET http://localhost:8080/users/me \
  -H "Authorization: Bearer $TOKEN" \
  -H "Accept: application/json"
```

# PUT - Atualizar
curl -X PUT http://localhost:8080/products/1 \
  -H "Content-Type: application/json" \
  -d '{"name":"Product Updated","description":"Updated description","price":99.99,"imgUrl":"http://example.com/img.jpg"}'

# DELETE - Deletar
curl -X DELETE http://localhost:8080/products/1
```

---

## 🛡️ Tratamento de Exceções

Sistema de tratamento global de exceções para fornecer respostas padronizadas e informativas.

### ControllerExceptionHandler

```java
@ControllerAdvice
public class ControllerExceptionHandler {
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<CustomError> resourceNotFound(
            ResourceNotFoundException e, 
            HttpServletRequest request) {
        
        HttpStatus status = HttpStatus.NOT_FOUND;
        CustomError err = new CustomError(
            Instant.now(), 
            status.value(), 
            e.getMessage(), 
            request.getRequestURI()
        );
        return ResponseEntity.status(status).body(err);
    }
}
```


### ResourceNotFoundException

```java
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
```

### Fluxo de Tratamento

```
1. Controller recebe requisição
2. Service lança ResourceNotFoundException
3. @ControllerAdvice intercepta a exceção
4. Cria CustomError com detalhes
5. Retorna ResponseEntity com status 404
```

### Exemplos de Respostas de Erro

**1. Produto não encontrado (GET):**

**Request:**
```http
GET http://localhost:8080/products/999
```

**Response:** `404 Not Found`
```json
{
  "timestamp": "2025-12-30T15:30:45.123Z",
  "status": 404,
  "error": "Recurso não encontrado",
  "path": "/products/999"
}
```

---

**2. Tentar atualizar produto inexistente (PUT):**

**Request:**
```http
PUT http://localhost:8080/products/999
Content-Type: application/json

{
  "name": "Produto",
  "description": "Descrição",
  "price": 100.00,
  "imgUrl": "http://example.com/img.jpg"
}
```

**Response:** `404 Not Found`
```json
{
  "timestamp": "2025-12-30T15:31:20.456Z",
  "status": 404,
  "error": "Recurso não encontrado",
  "path": "/products/999"
}
```

---

**3. Tentar deletar produto inexistente (DELETE):**

**Request:**
```http
DELETE http://localhost:8080/products/999
```

**Response:** `404 Not Found`
```json
{
  "timestamp": "2025-12-30T15:32:10.789Z",
  "status": 404,
  "error": "Recurso não encontrado",
  "path": "/products/999"
}
```

---

**4. Validação de Dados (POST/PUT):**

**Request (dados inválidos):**
```http
POST http://localhost:8080/products
Content-Type: application/json

{
  "name": "",
  "description": "Curta",
  "price": -10.0,
  "imgUrl": "http://img.jpg"
}
```

**Response:** `422 Unprocessable Entity`
```json
{
  "timestamp": "2025-12-30T15:33:00.123Z",
  "status": 422,
  "error": "Dados inválidos",
  "path": "/products",
  "errors": [
    {
      "fieldName": "name",
      "message": "Nome não pode estar em branco"
    },
    {
      "fieldName": "description",
      "message": "Descrição precisa ter no mínimo 10 caracteres"
    },
    {
      "fieldName": "price",
      "message": "O preço deve ser um valor positivo"
    }
  ]
}
```

**Handler de Validação:**
```java
@ExceptionHandler(MethodArgumentNotValidException.class)
public ResponseEntity<CustomError> methodArgumentNotValidation(
        MethodArgumentNotValidException e, 
        HttpServletRequest request) {
    
    HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
    ValidationError err = new ValidationError(
        Instant.now(), 
        status.value(), 
        "Dados inválidos", 
        request.getRequestURI()
    );
    
    for (FieldError f : e.getBindingResult().getFieldErrors()) {
        err.addError(f.getField(), f.getDefaultMessage());
    }
    
    return ResponseEntity.status(status).body(err);
}
```

---

### Tipos de Exceções Tratadas

| Exceção | Status HTTP | Cenário | Mensagem |
|---------|-------------|---------|----------|
| `ResourceNotFoundException` | 404 Not Found | Recurso não existe | "Recurso não encontrado" |
| `MethodArgumentNotValidException` | 422 Unprocessable Entity | Validação de dados falhou | "Dados inválidos" + lista de erros |
| `DataIntegrityViolationException` | 400 Bad Request | Violação de integridade referencial | "Falha de integridade referencial" |

---

### Vantagens do Tratamento Centralizado

| Vantagem | Descrição |
|----------|-----------|
| ✅ **Consistência** | Todas as respostas de erro seguem o mesmo padrão |
| ✅ **Manutenibilidade** | Tratamento centralizado em um único lugar |
| ✅ **Informativo** | Detalhes completos: timestamp, status, mensagem e path |
| ✅ **Status HTTP corretos** | 404 para não encontrado, 422 para validação |
| ✅ **Melhor DX** | Desenvolvedores que consomem a API têm respostas claras |
| ✅ **Rastreabilidade** | Timestamp facilita correlação com logs |
| ✅ **Validação detalhada** | Lista todos os campos com erro de validação |

---

## 🗄️ Banco de Dados

### Modelo Relacional

```
┌─────────┐         ┌─────────┐         ┌─────────────┐
│  User   │1      N │  Order  │1      1 │   Payment   │
├─────────┤────────→├─────────┤────────→├─────────────┤
│ id (PK) │         │ id (PK) │         │ id (PK)     │
│ name    │         │ moment  │         │ moment      │
│ email   │         │ status  │         │ order_id(FK)│
│ phone   │         │client_id│         └─────────────┘
│birthDate│         └─────────┘
└─────────┘               │
                          │1
                          │
                          │N
                    ┌─────────────┐
                    │ OrderItem   │
                    ├─────────────┤
                    │order_id (PK)│
                    │product_id(PK)
                    │ quantity    │
                    │ price       │
                    └─────────────┘
                          │N       │N
                ┌─────────┘        └─────────┐
                │1                           │1
          ┌─────────┐                  ┌──────────┐
          │ Product │                  │ Category │
          ├─────────┤N               N ├──────────┤
          │ id (PK) │←────────────────→│ id (PK)  │
          │ name    │  (product_       │ name     │
          │ desc    │   category)      └──────────┘
          │ price   │
          │ imgUrl  │
          └─────────┘
```

### Tabelas

| Tabela | Descrição | Campos Principais |
|--------|-----------|-------------------|
| `tb_user` | Usuários do sistema | id, name, email, phone, birthDate, password |
| `tb_order` | Pedidos realizados | id, moment, status, client_id |
| `tb_payment` | Pagamentos | id, moment, order_id |
| `tb_product` | Produtos do catálogo | id, name, description, price, imgUrl |
| `tb_category` | Categorias de produtos | id, name |
| `tb_order_item` | Itens dos pedidos | order_id, product_id, quantity, price |
| `tb_product_category` | Relação produto-categoria | product_id, category_id |

### Relacionamentos Detalhados

| Relacionamento | Cardinalidade | Descrição | Chave Estrangeira |
|----------------|---------------|-----------|-------------------|
| User → Order | 1:N | Um usuário pode ter vários pedidos | order.client_id → user.id |
| Order → Payment | 1:1 | Cada pedido tem um pagamento | payment.order_id → order.id |
| Order → OrderItem | 1:N | Um pedido tem vários itens | order_item.order_id → order.id |
| Product → OrderItem | 1:N | Um produto pode estar em vários itens | order_item.product_id → product.id |
| Product ↔ Category | N:N | Produtos têm múltiplas categorias | Tabela associativa |

### Dados de Teste (import.sql)

O arquivo `import.sql` popula o banco com dados iniciais para testes:

**Usuários e Permissões:**
```sql
-- Usuários (senha: 123456 criptografada com BCrypt)
INSERT INTO tb_user (name, email, password, phone, birth_date) 
VALUES ('Alex', 'alex@gmail.com', '$2y$10$EL1OjaRlHrsTg0C5VpO9Levfu6cc6vQewysJsz9txC7Mn1SXDHdCW', '99999999', '1990-07-25');

INSERT INTO tb_user (name, email, password, phone, birth_date) 
VALUES ('Maria', 'maria@gmail.com', '$2y$10$EL1OjaRlHrsTg0C5VpO9Levfu6cc6vQewysJsz9txC7Mn1SXDHdCW', '88888888', '1992-05-15');

-- Roles
INSERT INTO tb_role (authority) VALUES ('ROLE_OPERATOR');
INSERT INTO tb_role (authority) VALUES ('ROLE_ADMIN');

-- Associação User-Role
INSERT INTO tb_user_role (user_id, role_id) VALUES (1, 1); -- Alex: OPERATOR
INSERT INTO tb_user_role (user_id, role_id) VALUES (2, 1); -- Maria: OPERATOR
INSERT INTO tb_user_role (user_id, role_id) VALUES (2, 2); -- Maria: ADMIN
```

**Produtos (25 itens):**
```sql
INSERT INTO tb_product (name, price, description, img_Url) VALUES 
('The Lord of the Rings', 90.5, 'Lorem ipsum dolor sit amet...', 'https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/1-big.jpg'),
('Smart TV', 2190.0, 'Lorem ipsum dolor sit amet...', 'https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/2-big.jpg'),
-- ... mais 23 produtos
```

**Categorias, Pedidos e Pagamentos:**
```sql
INSERT INTO tb_category(name) VALUES ('Livros'), ('Eletrônicos'), ('Computadores');
INSERT INTO tb_order (moment, status, client_id) VALUES (TIMESTAMP WITH TIME ZONE '2022-07-25T13:00:00Z', 1, 1);
INSERT INTO tb_payment (order_id, moment) VALUES (1, TIMESTAMP WITH TIME ZONE '2022-07-25T15:00:00Z');
```

### Console H2

Acesse o console web do H2 para gerenciar o banco:

**URL:** http://localhost:8080/h2-console

**Credenciais:**
```
JDBC URL: jdbc:h2:mem:testdb
Username: sa
Password: (deixe em branco)
```

### Consultas Úteis

```sql
-- Listar todos os produtos
SELECT * FROM TB_PRODUCT;

-- Contar produtos
SELECT COUNT(*) FROM TB_PRODUCT;

-- Produtos ordenados por preço
SELECT * FROM TB_PRODUCT ORDER BY PRICE DESC;

-- Produtos acima de R$ 100
SELECT * FROM TB_PRODUCT WHERE PRICE > 100;

-- Listar categorias
SELECT * FROM TB_CATEGORY;

-- Listar usuários
SELECT * FROM TB_USER;

-- Listar roles
SELECT * FROM TB_ROLE;

-- Usuários com suas roles
SELECT u.name, u.email, r.authority 
FROM TB_USER u
INNER JOIN TB_USER_ROLE ur ON u.id = ur.user_id
INNER JOIN TB_ROLE r ON ur.role_id = r.id
ORDER BY u.name;

-- Ver estrutura da tabela
SHOW COLUMNS FROM TB_PRODUCT;

-- Listar todos os pedidos
SELECT o.id, o.moment, o.status, u.name as client_name
FROM TB_ORDER o
INNER JOIN TB_USER u ON o.client_id = u.id;

-- Produtos de um pedido
SELECT p.name, oi.quantity, oi.price, (oi.quantity * oi.price) as subtotal
FROM TB_ORDER_ITEM oi
INNER JOIN TB_PRODUCT p ON oi.product_id = p.id
WHERE oi.order_id = 1;
```

### Configuração do Banco de Dados

**Desenvolvimento (H2):**
```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.defer-datasource-initialization=true
```

**Produção (PostgreSQL - exemplo):**
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/dscommerce
spring.datasource.username=postgres
spring.datasource.password=sua-senha
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=validate
```

---

## 🐛 Troubleshooting (Problemas Comuns)

### Erro: 403 Forbidden ao criar pedido

**Sintoma:**
```json
{
    "timestamp": "2026-02-26T13:26:54.630+00:00",
    "status": 403,
    "error": "Forbidden",
    "path": "/orders"
}
```

**Causa:** O usuário autenticado não possui a role `ROLE_OPERATOR` necessária para criar pedidos.

**Solução:**
1. Verifique as roles do usuário no banco:
```sql
SELECT u.email, r.authority 
FROM tb_user u 
INNER JOIN tb_user_role ur ON u.id = ur.user_id 
INNER JOIN tb_role r ON r.id = ur.role_id 
WHERE u.email = 'alex@gmail.com';
```

2. Adicione a role necessária no `import.sql`:
```sql
-- Se ROLE_OPERATOR não existe, crie:
INSERT INTO tb_role (authority) VALUES ('ROLE_OPERATOR');

-- Associe ao usuário (assumindo user_id=1, role_id=1):
INSERT INTO tb_user_role (user_id, role_id) VALUES (1, 1);
```

3. Faça login novamente para obter novo token com as roles atualizadas.

---

### Erro: Cannot construct instance of OrderDTO/OrderItemDTO

**Sintoma:**
```
InvalidDefinitionException: Cannot construct instance of OrderDTO 
(no Creators, like default constructor, exist)
```

**Causa:** Classes DTO não possuem construtor padrão (vazio) necessário para o Jackson deserializar JSON.

**Solução:** Adicione construtor vazio nas classes DTO:

```java
public class OrderDTO {
    // Campos...
    
    // ✅ Construtor padrão (obrigatório)
    public OrderDTO() {
    }
    
    // Construtor com parâmetros
    public OrderDTO(Order entity) {
        // ...
    }
}
```

Faça o mesmo para `OrderItemDTO`, `ClientDTO` e `PaymentDTO`.

---

### Erro: 500 Internal Server Error ao buscar produto

**Sintoma:**
```json
{
    "status": 500,
    "error": "Internal Server Error",
    "path": "/products/2"
}
```

**Causa:** Possível erro no mapeamento JPA ou dados inconsistentes no banco.

**Soluções:**

1. **Verifique os logs do console** - procure por stack traces detalhadas
2. **Verifique relacionamentos JPA** - `@OneToMany`, `@ManyToMany` configurados corretamente
3. **Verifique dados no H2 Console:**
```sql
SELECT * FROM tb_product WHERE id = 2;
SELECT * FROM tb_product_category WHERE product_id = 2;
```

4. **Modo debug** - adicione no `application.properties`:
```properties
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE
```

---

### Erro: 401 Unauthorized

**Sintoma:**
```json
{
    "status": 401,
    "error": "Unauthorized"
}
```

**Causas e Soluções:**

| Causa | Solução |
|-------|---------|
| Token ausente | Adicione header: `Authorization: Bearer <token>` |
| Token expirado | Faça login novamente (`POST /oauth2/token`) |
| Token inválido | Verifique se copiou o token completo |
| Formato incorreto | Use `Bearer <token>`, não `<token>` sozinho |

**Como testar no Postman:**
1. Aba **Authorization**
2. Type: **Bearer Token**
3. Cole o `access_token` recebido no login

---

### Erro: A ordem deve conter pelo menos um item

**Sintoma:**
```json
{
    "status": 422,
    "error": "Dados inválidos",
    "errors": [
        {
            "fieldName": "items",
            "message": "A ordem deve conter pelo menos um item"
        }
    ]
}
```

**Causa:** JSON enviado não contém itens ou lista está vazia.

**Solução:** Envie pelo menos 1 item:
```json
{
  "items": [
    {
      "productId": 1,
      "quantity": 2
    }
  ]
}
```

---

### Tabela de Erros HTTP

| Código | Significado | Ação |
|--------|-------------|------|
| 400 | Dados inválidos no body | Verifique JSON enviado |
| 401 | Não autenticado | Faça login e use o token |
| 403 | Sem permissão | Usuário precisa da role adequada |
| 404 | Recurso não encontrado | Verifique se ID existe no banco |
| 422 | Validação falhou | Corrija os campos indicados em `errors` |
| 500 | Erro no servidor | Verifique logs do console |

---

## 📚 Referências

### Documentação do Projeto

📖 **Guias de Consulta Rápida:**
- [`documentacao/autenticacao_autorizacao.md`](documentacao/autenticacao_autorizacao.md) - **Guia completo OAuth2 + JWT** (Authorization Server, Resource Server, Custom Grant, CORS)
- [`documentacao/jpa.md`](documentacao/jpa.md) - Guia completo sobre JPA, modelagem e relacionamentos
- [`documentacao/GuiaRelacionamentoJPA.md`](documentacao/GuiaRelacionamentoJPA.md) - Referência rápida de relacionamentos JPA
- [`documentacao/excecoes.md`](documentacao/excecoes.md) - Tratamento de exceções e validações
- [`documentacao/anotacoes_bean_validation.md`](documentacao/anotacoes_bean_validation.md) - Bean Validation com mensagens amigáveis
- [`documentacao/excecoes.md`](documentacao/excecoes.md) - Tratamento de exceções padronizado

### Documentação Oficial

- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/)
- [Spring Data JPA Documentation](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)
- [Bean Validation Specification](https://beanvalidation.org/2.0/)
- [Hibernate Validator](https://hibernate.org/validator/)
- [REST API Best Practices](https://restfulapi.net/)
- [HTTP Status Codes](https://developer.mozilla.org/en-US/docs/Web/HTTP/Status)

### Cursos e Materiais

- [DevSuperior - Curso Java Spring](https://devsuperior.com.br/)

---

## 📄 Licença

Este projeto foi desenvolvido para fins educacionais.

---

## 👨‍💻 Autor

Desenvolvido por Flávio Antonio Demétrio -  durante o curso DevSuperior - Formação Java Spring Boot

