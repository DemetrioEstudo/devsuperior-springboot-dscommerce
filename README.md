# DSCommerce

API REST de e-commerce desenvolvida com Java e Spring Boot.

---

## 📌 Sobre o Projeto

Sistema backend para gerenciamento de produtos, categorias, usuários, pedidos e pagamentos. O projeto implementa uma arquitetura em camadas seguindo as melhores práticas do Spring Boot, incluindo CRUD completo de produtos com tratamento de exceções personalizado.

**Funcionalidades Implementadas:**
- ✅ Buscar produto por ID
- ✅ Listar produtos (paginado)
- ✅ Inserir novo produto
- ✅ Atualizar produto existente
- ✅ Deletar produto
- ✅ Tratamento de exceções personalizado com `@ControllerAdvice`
- ✅ Validação de integridade referencial


## 📁 Estrutura de Arquivos

```
dscommerce/
├── src/main/java/br/com/klsys/dscommerce/
│   ├── DscommerceApplication.java
│   │
│   ├── controllers/                    # Camada de Apresentação (REST API)
│   │   ├── ProductController.java
│   │   └── handlers/
│   │       └── ControllerExceptionHandler.java
│   │
│   ├── services/                       # Camada de Lógica de Negócio
│   │   ├── ProductService.java
│   │   └── exceptions/
│   │       └── ResourceNotFoundException.java
│   │
│   ├── repositories/                   # Camada de Acesso a Dados
│   │   └── ProductRepository.java
│   │
│   ├── entities/                       # Entidades JPA (Modelo de Domínio)
│   │   ├── Product.java
│   │   ├── Category.java
│   │   ├── User.java
│   │   ├── Order.java
│   │   ├── OrderItem.java
│   │   ├── OrderItemPk.java
│   │   ├── Payment.java
│   │   └── OrderStatus.java
│   │
│   └── dto/                            # Data Transfer Objects
│       ├── ProductDTO.java
│       └── CustomError.java
│
├── src/main/resources/
│   ├── application.properties          # Configurações principais
│   ├── application-test.properties     # Configurações de teste
│   └── import.sql                      # Dados iniciais (seed)
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
| Spring Validation | 3.4.1 | Validação de dados com Bean Validation |
| H2 Database | runtime | Banco de dados em memória (desenvolvimento) |
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
- API: http://localhost:8080
- Console H2: http://localhost:8080/h2-console

### Configuração

**application.properties:**
```properties
spring.profiles.active=test
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


---

## 🏗️ Arquitetura do Sistema

### Visão Geral

O projeto segue uma **arquitetura em camadas (Layered Architecture)** com separação clara de responsabilidades:

```
┌─────────────────────────────────────────────────────────────┐
│                     CLIENTE HTTP                             │
│              (Postman, Browser, Apps)                        │
└────────────────────────┬────────────────────────────────────┘
                         │ HTTP Request (JSON)
                         ↓
┌─────────────────────────────────────────────────────────────┐
│                  PRESENTATION LAYER                          │
│                     (@RestController)                        │
├─────────────────────────────────────────────────────────────┤
│  • ProductController                                         │
│  • Recebe requisições HTTP                                   │
│  • Valida parâmetros de entrada                             │
│  • Retorna ResponseEntity<DTO>                              │
│  • Define rotas e métodos HTTP                              │
└────────────────────────┬────────────────────────────────────┘
                         │ ProductDTO
                         ↓
┌─────────────────────────────────────────────────────────────┐
│                   BUSINESS LAYER                             │
│                      (@Service)                              │
├─────────────────────────────────────────────────────────────┤
│  • ProductService                                            │
│  • Implementa regras de negócio                             │
│  • Gerencia transações (@Transactional)                     │
│  • Converte Entity ↔ DTO                                    │
│  • Lança exceções de negócio                                │
└────────────────────────┬────────────────────────────────────┘
                         │ Entity (Product)
                         ↓
┌─────────────────────────────────────────────────────────────┐
│                 PERSISTENCE LAYER                            │
│                    (@Repository)                             │
├─────────────────────────────────────────────────────────────┤
│  • ProductRepository (JpaRepository)                         │
│  • Abstração de acesso a dados                              │
│  • Operações CRUD automáticas                               │
│  • Queries derivadas de métodos                             │
└────────────────────────┬────────────────────────────────────┘
                         │ SQL/JDBC
                         ↓
┌─────────────────────────────────────────────────────────────┐
│                    DATABASE LAYER                            │
│                  (H2 / PostgreSQL)                           │
├─────────────────────────────────────────────────────────────┤
│  • tb_product, tb_category, tb_user                         │
│  • tb_order, tb_order_item, tb_payment                      │
└─────────────────────────────────────────────────────────────┘
```

### Fluxo de Dados

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
┌─────────────────────────────────────────────────────────────┐
│                CROSS-CUTTING CONCERNS                        │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  ┌──────────────────────────────────────────────────────┐  │
│  │          @ControllerAdvice                           │  │
│  │   • Tratamento global de exceções                    │  │
│  │   • Intercepta erros de todas as camadas             │  │
│  │   • Retorna respostas padronizadas                   │  │
│  └──────────────────────────────────────────────────────┘  │
│                                                              │
│  ┌──────────────────────────────────────────────────────┐  │
│  │          @Transactional                              │  │
│  │   • Gerenciamento de transações                      │  │
│  │   • Controle de commit/rollback                      │  │
│  │   • Isolamento e propagação                          │  │
│  └──────────────────────────────────────────────────────┘  │
│                                                              │
│  ┌──────────────────────────────────────────────────────┐  │
│  │          Spring Data JPA                             │  │
│  │   • Abstração de persistência                        │  │
│  │   • Geração automática de queries                    │  │
│  │   • Gerenciamento de EntityManager                   │  │
│  └──────────────────────────────────────────────────────┘  │
│                                                              │
└─────────────────────────────────────────────────────────────┘
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
public class ProductDTO {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private String imgUrl;

    public ProductDTO(Long id, String name, String description, Double price, String imgUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imgUrl = imgUrl;
    }

    public ProductDTO(Product entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.description = entity.getDescription();
        this.price = entity.getPrice();
        this.imgUrl = entity.getImgUrl();
    }
    
    // Getters e Setters
}
```

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

### Vantagens dos DTOs

| Vantagem | Descrição |
|----------|-----------|
| **Segurança** | Não expõe estrutura interna das entidades |
| **Performance** | Trafega apenas dados necessários |
| **Flexibilidade** | Permite diferentes representações da mesma entidade |
| **Desacoplamento** | Mudanças na entidade não afetam a API |
| **Evita Lazy Loading** | Previne exceções de sessão fechada |

---

## 🧪 Testando a API

### 1. Buscar Produto por ID

**Request:**
```http
GET http://localhost:8080/products/1
```

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

**Request:**
```http
POST http://localhost:8080/products
Content-Type: application/json

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

---

### 4. Atualizar Produto Existente

**Request:**
```http
PUT http://localhost:8080/products/1
Content-Type: application/json

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

**Request:**
```http
DELETE http://localhost:8080/products/1
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

---

### Testando com cURL

```bash
# GET - Buscar por ID
curl -X GET http://localhost:8080/products/1

# GET - Listar todos (paginado)
curl -X GET "http://localhost:8080/products?page=0&size=5"

# POST - Inserir
curl -X POST http://localhost:8080/products \
  -H "Content-Type: application/json" \
  -d '{"name":"Notebook","description":"Notebook Dell","price":3500.00,"imgUrl":"http://example.com/notebook.jpg"}'

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

### Vantagens do Tratamento Centralizado

| Vantagem | Descrição |
|----------|-----------|
| ✅ **Consistência** | Todas as respostas de erro seguem o mesmo padrão |
| ✅ **Manutenibilidade** | Tratamento centralizado em um único lugar |
| ✅ **Informativo** | Detalhes completos: timestamp, status, mensagem e path |
| ✅ **Status HTTP corretos** | 404 ao invés de 500 para recursos não encontrados |
| ✅ **Melhor DX** | Desenvolvedores que consomem a API têm respostas claras |
| ✅ **Rastreabilidade** | Timestamp facilita correlação com logs |

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

O arquivo `import.sql` popula o banco com 25 produtos para testes:

```sql
INSERT INTO tb_product (name, price, description, img_Url) VALUES 
('The Lord of the Rings', 90.5, 'Lorem ipsum dolor sit amet...', 'https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/1-big.jpg'),
('Smart TV', 2190.0, 'Lorem ipsum dolor sit amet...', 'https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/2-big.jpg'),
-- ... mais 23 produtos
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

-- Ver estrutura da tabela
SHOW COLUMNS FROM TB_PRODUCT;
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

## 📚 Referências

- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/)
- [Spring Data JPA Documentation](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)
- [REST API Best Practices](https://restfulapi.net/)
- [DevSuperior - Curso Java Spring](https://devsuperior.com.br/)

---

## 📄 Licença

Este projeto foi desenvolvido para fins educacionais.

---

## 👨‍💻 Autor

Desenvolvido por Flávio Antonio Demétrio -  durante o curso DevSuperior - Formação Java Spring Boot

