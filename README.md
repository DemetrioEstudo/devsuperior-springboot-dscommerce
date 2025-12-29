# DSCommerce

API REST de e-commerce desenvolvida com Java e Spring Boot.

---

## 📝 Atualizações Recentes

### ✅ Último Commit - Implementação de Insert Product
- ✅ Adicionado endpoint `POST /products` para inserir novos produtos
- ✅ Implementado método `insert()` no `ProductService`
- ✅ Refatorado `ProductController` para retornar `ResponseEntity` com status HTTP adequados
- ✅ Adicionado header `Location` na resposta do POST (RFC 7231)

### 🔄 Alterações Não Commitadas
- 🔄 Aprimorado todos os endpoints GET para retornar `ResponseEntity`
- 🔄 Adicionado construção de URI para recurso criado no POST
- 🔄 Padronização de respostas HTTP em todos os endpoints

---

## 📌 Sobre o Projeto

Sistema backend para gerenciamento de produtos, categorias, usuários, pedidos e pagamentos.

**Funcionalidades Implementadas:**
- ✅ Buscar produto por ID
- ✅ Listar produtos (paginado)
- ✅ Inserir novo produto

---

## 🛠️ Tecnologias

| Tecnologia | Versão |
|------------|--------|
| Java | 21 |
| Spring Boot | 3.5.8 |
| Spring Data JPA | - |
| H2 Database | - |
| Maven | - |

---

## 🗄️ Estrutura do Banco de Dados

### Modelo Relacional

```
User ──1:N─→ Order ──1:1─→ Payment
              │
              └──1:N─→ OrderItem ←─N:1── Product ←─N:N─→ Category
```

### Entidades

| Entidade | Atributos Principais |
|----------|---------------------|
| **Product** | id, name, description, price, imgUrl |
| **Category** | id, name |
| **User** | id, name, email, phone, birthDate |
| **Order** | id, moment, status, client |
| **OrderItem** | quantity, price (chave composta: order + product) |
| **Payment** | id, moment, order |

### Relacionamentos

- **1:N** - User → Order
- **1:1** - Order → Payment  
- **N:N** - Product ↔ Category
- **N:N com atributos** - Order ↔ Product (via OrderItem)

**Console H2:** http://localhost:8080/h2-console

---

## 🏗️ Arquitetura em Camadas

```
Cliente HTTP → Controller → Service → Repository → Banco de Dados
                   ↓           ↓          ↓
                  DTO   ←    Entity ←  JPA/SQL
```

### 📦 Repository (Acesso a Dados)

Interface para operações no banco usando Spring Data JPA.

```java
public interface ProductRepository extends JpaRepository<Product, Long> {
    // Métodos herdados: findById, findAll, save, delete
}
```

---

### ⚙️ Service (Lógica de Negócio)

Camada de regras de negócio e transações.

```java
@Service
public class ProductService {
    @Autowired
    private ProductRepository repository;

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        Product product = repository.findById(id).get();
        return new ProductDTO(product);
    }

    @Transactional(readOnly = true)
    public Page<ProductDTO> findAll(Pageable pageable) {
        Page<Product> result = repository.findAll(pageable);
        return result.map(ProductDTO::new);
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
}
```

**Boas práticas:**
- `@Transactional(readOnly = true)` para operações de leitura
- `@Transactional` para operações de escrita
- Sempre retornar DTOs, nunca entidades

---

### 🌐 Controller (API REST)

Camada de exposição de endpoints HTTP com ResponseEntity para respostas padronizadas.

```java
@RestController
@RequestMapping(value="/products")
public class ProductController {
    @Autowired
    private ProductService service;

    @GetMapping(value = "/{id}")
    public ResponseEntity<ProductDTO> findById(@PathVariable Long id) {
        ProductDTO dto = service.findById(id);
        return ResponseEntity.ok().body(dto);
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
}
```

**Endpoints disponíveis:**
- `GET /products/{id}` - Buscar produto por ID (Status: 200 OK)
- `GET /products` - Listar todos os produtos com paginação (Status: 200 OK)
- `POST /products` - Inserir novo produto (Status: 201 Created + Location header)

---

### 📋 DTO (Data Transfer Object)

Objeto para transferência de dados entre camadas (não expõe entidades JPA).

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
    // getters and setters
}
```

---

## 🚀 Como Executar

### Pré-requisitos
- Java 21
- Maven (ou usar o wrapper `mvnw.cmd`)

### Execução

```bash
# Windows
mvnw.cmd spring-boot:run

# Linux/Mac
./mvnw spring-boot:run

# Ou via IDE
# Execute a classe DscommerceApplication.java
```

**Importante:** Certifique-se de que o perfil `test` está ativo em `application.properties`:
```properties
spring.profiles.active=test
```

---

## 🐛 Resolução de Problemas

### Erro 500 ao buscar produto

Se você receber um erro 500 ao tentar buscar um produto (ex: `GET /products/2`), verifique:

1. **Banco de dados não foi populado:**
   - Verifique se `spring.jpa.defer-datasource-initialization=true` está em `application-test.properties`
   - Reinicie a aplicação para recarregar o `import.sql`

2. **ID do produto não existe:**
   - Use IDs de 1 a 25 (conforme dados em `import.sql`)
   - Teste primeiro com `GET /products/1`

3. **Console H2 para verificar dados:**
   - Acesse: http://localhost:8080/h2-console
   - JDBC URL: `jdbc:h2:mem:testdb`
   - Username: `sa`
   - Password: (deixe em branco)
   - Execute: `SELECT * FROM TB_PRODUCT;`

---

## 🧪 Testando a API

### 1. Buscar produto por ID

**Request:**
```http
GET http://localhost:8080/products/1
```

**Response:** `200 OK`
```json
{
  "id": 1,
  "name": "The Lord of the Rings",
  "description": "Lorem ipsum dolor sit amet...",
  "price": 90.5,
  "imgUrl": "https://raw.githubusercontent.com/..."
}
```

---

### 2. Listar produtos (paginado)

**Request:**
```http
GET http://localhost:8080/products
GET http://localhost:8080/products?page=0&size=5
GET http://localhost:8080/products?page=0&size=10&sort=name,asc
GET http://localhost:8080/products?page=1&size=10&sort=price,desc
```

**Parâmetros:**
- `page` - Número da página (padrão: 0)
- `size` - Itens por página (padrão: 20)
- `sort` - Ordenação (ex: `name,asc` ou `price,desc`)

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
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 20
  },
  "totalPages": 2,
  "totalElements": 25,
  "first": true,
  "last": false
}
```

---

### 3. Inserir novo produto

**Request:**
```http
POST http://localhost:8080/products
Content-Type: application/json

{
  "name": "Smart TV",
  "description": "TV LED 50 polegadas 4K",
  "price": 2199.90,
  "imgUrl": "https://exemplo.com/tv.jpg"
}
```

**Response:** `201 Created`
```json
{
  "id": 26,
  "name": "Smart TV",
  "description": "TV LED 50 polegadas 4K",
  "price": 2199.90,
  "imgUrl": "https://exemplo.com/tv.jpg"
}
```

**Headers:**
```
Location: http://localhost:8080/products/26
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

- [ ] Implementar UPDATE (PUT) de produtos
- [ ] Implementar DELETE de produtos
- [ ] Tratamento de exceções (`@ControllerAdvice`)
- [ ] Validação de dados (`@Valid`, Bean Validation)
- [ ] Documentação Swagger/OpenAPI
- [ ] Testes unitários e de integração
- [ ] Deploy em produção (PostgreSQL)
