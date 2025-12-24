# DSCommerce

Sistema backend de e-commerce desenvolvido com Java e Spring Boot para fins didáticos.

---

## 📌 Sobre o Projeto

API REST para gerenciamento de produtos, categorias, usuários, pedidos e pagamentos de uma loja virtual.

**Status:** Em desenvolvimento  
**Funcionalidades atuais:** 
- Busca de produtos por ID
- Listagem paginada de produtos

---

## 🛠️ Tecnologias

| Tecnologia | Versão | Descrição |
|------------|--------|-----------|
| Java | 21 | Linguagem principal |
| Spring Boot | 3.5.8 | Framework web |
| Spring Data JPA | - | Persistência de dados |
| Hibernate | 6 | ORM (mapeamento objeto-relacional) |
| H2 Database | - | Banco em memória (perfil test) |
| Maven | - | Gerenciamento de dependências |

---

## 🗄️ Banco de Dados

### Modelo de Domínio

```
User ──1:N─→ Order ──1:1─→ Payment
              │
              └──1:N─→ OrderItem ←─N:1── Product ←─N:N─→ Category
```

### Entidades

| Entidade | Descrição | Atributos Principais |
|----------|-----------|---------------------|
| **Product** | Produtos à venda | id, name, description, price, imgUrl |
| **Category** | Categorias de produtos | id, name |
| **User** | Usuários/clientes | id, name, email, phone, birthDate |
| **Order** | Pedidos realizados | id, moment, status, client (User) |
| **OrderItem** | Itens do pedido | quantity, price, order, product |
| **Payment** | Pagamento do pedido | id, moment, order |
| **OrderStatus** | Status do pedido | WAITING_PAYMENT, PAID, SHIPPED, DELIVERED, CANCELED |

### Relacionamentos JPA

**1:1 (One-to-One)** - Order → Payment
```java
// Order.java
@OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
private Payment payment;
```

**1:N (One-to-Many)** - User → Order
```java
// User.java
@OneToMany(mappedBy = "client")
private List<Order> orders;

// Order.java
@ManyToOne
@JoinColumn(name = "client_id")
private User client;
```

**N:N (Many-to-Many)** - Product ↔ Category
```java
// Product.java
@ManyToMany
@JoinTable(name = "tb_product_category",
    joinColumns = @JoinColumn(name = "product_id"),
    inverseJoinColumns = @JoinColumn(name = "category_id"))
private Set<Category> categories;
```

**N:N com atributos extras** - Order ↔ Product (via OrderItem)
```java
// OrderItem.java - classe associativa
@EmbeddedId
private OrderItemPk id; // chave composta (order + product)
private Integer quantity;
private Double price;
```

### Configuração H2

**Arquivo:** `application-test.properties`
```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.username=sa
spring.datasource.password=
spring.h2.console.enabled=true
spring.jpa.hibernate.ddl-auto=update
```

**Console H2:** http://localhost:8080/h2-console

---

## 🏗️ Arquitetura em Camadas

```
Cliente HTTP → Controller → Service → Repository → Banco de Dados
                   ↓           ↓          ↓
                  DTO   ←    Entity ←  JPA/SQL
```

### 1️⃣ Repository (Acesso a Dados)

**Função:** Interface para operações no banco  
**Tecnologia:** Spring Data JPA

```java
public interface ProductRepository extends JpaRepository<Product, Long> {
    // Métodos herdados: findById, findAll, save, delete
}
```

### 2️⃣ Service (Lógica de Negócio)

**Função:** Regras de negócio, transações, conversões  
**Tecnologia:** Spring `@Service` + `@Transactional`

```java
@Service
public class ProductService {
    @Autowired
    private ProductRepository repository;

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        //Lógica para buscar o produto no banco de dados (simulada aqui)
        Optional<Product> result = repository.findById(id);
        Product product = result.get();
        ProductDTO dto = new ProductDTO(product);
        return dto;
    }

    @Transactional(readOnly = true)
    public Page<ProductDTO> findAll(Pageable pageable) {
        //Lógica para buscar o produto no banco de dados (simulada aqui)
        Page<Product> result = repository.findAll(pageable);
        return result.map(x -> new ProductDTO(x));
    }
}
```

**Boas práticas:**
- `@Transactional(readOnly = true)` para leitura
- `@Transactional` para escrita
- Retornar sempre DTOs, nunca Entities

### 3️⃣ Controller (API REST)

**Função:** Expor endpoints HTTP  
**Tecnologia:** Spring `@RestController`

```java
@RestController
@RequestMapping(value="/products")
public class ProductController {
    @Autowired
    private ProductService service;

    @GetMapping(value = "/{id}")
    public ProductDTO findById(@PathVariable Long id){
        return service.findById(id);
    }

    @GetMapping
    public Page<ProductDTO> findAll(Pageable pageable){
        return service.findAll(pageable);
    }
}
```

**Endpoints disponíveis:**
- `GET /products/{id}` - Buscar produto por ID
- `GET /products` - Listar todos os produtos (com paginação)

### 4️⃣ DTO (Transferência de Dados)

**Função:** Objeto para trafegar dados entre camadas  
**Por quê?** Não expor entidades JPA (segurança + performance)

```java
public class ProductDTO {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private String imgUrl;

    public ProductDTO(Product entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.description = entity.getDescription();
        this.price = entity.getPrice();
        this.imgUrl = entity.getImgUrl();
    }
}
```

### Fluxo Completo

**Exemplo 1: Buscar por ID**
```
1. GET /products/2
2. ProductController.findById(2)
3. ProductService.findById(2)
4. ProductRepository.findById(2) → SQL
5. Retorna Product (entity)
6. Converte para ProductDTO
7. Retorna JSON ao cliente
```

**Exemplo 2: Listar com paginação**
```
1. GET /products?page=0&size=10
2. ProductController.findAll(Pageable)
3. ProductService.findAll(Pageable)
4. ProductRepository.findAll(Pageable) → SQL
5. Retorna Page<Product> (entities)
6. Converte para Page<ProductDTO> usando map
7. Retorna JSON paginado ao cliente
```

---

## 🚀 Como Executar

### Pré-requisitos
- Java 21
- Maven (ou usar o wrapper `mvnw.cmd`)

### Execução

**Opção 1: Maven Wrapper (recomendado)**
```bash
mvnw.cmd spring-boot:run
```

**Opção 2: Maven**
```bash
mvn spring-boot:run
```

**Opção 3: IntelliJ IDEA**
- Executar classe `DscommerceApplication.java`

### Testar API

**Buscar produto por ID:**
```bash
GET http://localhost:8080/products/1
GET http://localhost:8080/products/2
```

**Resposta esperada:**
```json
{
  "id": 1,
  "name": "The Lord of the Rings",
  "description": "Lorem ipsum...",
  "price": 90.5,
  "imgUrl": "https://..."
}
```

**Listar produtos (paginado):**
```bash
GET http://localhost:8080/products
GET http://localhost:8080/products?page=0&size=5
GET http://localhost:8080/products?page=0&size=10&sort=name,asc
GET http://localhost:8080/products?page=1&size=10&sort=price,desc
```

**Parâmetros de paginação:**
- `page` - Número da página (inicia em 0)
- `size` - Quantidade de itens por página (padrão: 20)
- `sort` - Ordenação (ex: `name,asc` ou `price,desc`)

**Resposta esperada (paginada):**
```json
{
  "content": [
    {
      "id": 1,
      "name": "The Lord of the Rings",
      "description": "Lorem ipsum...",
      "price": 90.5,
      "imgUrl": "https://..."
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 20
  },
  "totalPages": 1,
  "totalElements": 25,
  "last": true,
  "first": true,
  "numberOfElements": 25,
  "empty": false
}
```

---

## 📁 Estrutura de Arquivos

```
dscommerce/
├── src/main/java/br/com/klsys/dscommerce/
│   ├── controllers/          # Endpoints REST
│   │   └── ProductController.java
│   ├── services/             # Lógica de negócio
│   │   └── ProductService.java
│   ├── repositories/         # Acesso a dados
│   │   └── ProductRepository.java
│   ├── entities/             # Entidades JPA
│   │   ├── Product.java
│   │   ├── Category.java
│   │   ├── User.java
│   │   ├── Order.java
│   │   ├── OrderItem.java
│   │   ├── OrderItemPk.java
│   │   ├── Payment.java
│   │   └── OrderStatus.java
│   └── dto/                  # Data Transfer Objects
│       └── ProductDTO.java
├── src/main/resources/
│   ├── application.properties
│   ├── application-test.properties
│   └── import.sql            # Dados iniciais
└── pom.xml
```

---

## 📝 Próximos Passos

- [ ] Implementar CRUD completo (POST, PUT, DELETE)
- [ ] Tratamento de exceções (`@ControllerAdvice`)
- [ ] Validação de dados (`@Valid`)
- [x] Paginação de resultados
- [ ] Documentação Swagger
- [ ] Testes unitários e de integração
- [ ] Deploy em produção (PostgreSQL)
