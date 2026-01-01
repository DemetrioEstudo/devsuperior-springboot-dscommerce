# JPA - Java Persistence API

## O que é JPA?

**JPA** (Java Persistence API) é uma especificação Java para mapeamento objeto-relacional (ORM). Permite mapear classes Java para tabelas de banco de dados e gerenciar a persistência de dados.

**Principal implementação**: Hibernate (usado por padrão no Spring Boot).

## Configuração no Spring Boot

### Dependências Maven

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>runtime</scope>
</dependency>
```

### `application.properties`

```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.username=sa
spring.datasource.password=

spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.hibernate.ddl-auto=update
```

## Modelagem de Entidades

### Anotações Básicas

```java
@Entity
@Table(name = "tb_product")
public class Product {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String name;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    private Double price;
    
    @Column(name = "img_url")
    private String imgUrl;
    
    // construtores, getters e setters
}
```

### Anotações Importantes

| Anotação | Descrição |
|----------|-----------|
| `@Entity` | Marca a classe como entidade JPA |
| `@Table(name = "...")` | Define o nome da tabela no banco |
| `@Id` | Marca o atributo como chave primária |
| `@GeneratedValue` | Define estratégia de geração automática do ID |
| `@Column` | Customiza a coluna (nome, tamanho, nullable, etc.) |
| `@Temporal` | Define tipo de dado temporal (DATE, TIME, TIMESTAMP) |
| `@Enumerated` | Mapeia enums (STRING ou ORDINAL) |
| `@Transient` | Atributo não persistido no banco |

## Relacionamentos entre Entidades

### 1️⃣ One-to-One (1:1)

**Um para um** - Ex: Pessoa e Endereço

```java
@Entity
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;
}

@Entity
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String street;
    private String city;
    
    @OneToOne(mappedBy = "address")
    private Person person;
}
```

### 2️⃣ One-to-Many / Many-to-One (1:N)

**Um para muitos** - Ex: Categoria tem vários Produtos

```java
@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private Set<Product> products = new HashSet<>();
}

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}
```

### 3️⃣ Many-to-Many (N:N)

**Muitos para muitos** - Ex: Produto tem várias Categorias e vice-versa

```java
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    
    @ManyToMany
    @JoinTable(
        name = "tb_product_category",
        joinColumns = @JoinColumn(name = "product_id"),
        inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories = new HashSet<>();
}

@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    
    @ManyToMany(mappedBy = "categories")
    private Set<Product> products = new HashSet<>();
}
```

## Atributos de Relacionamento

### Cascade

Define operações que devem ser propagadas para entidades relacionadas:

```java
@OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
private Set<Product> products;
```

**Tipos:**
- `CascadeType.PERSIST` - salvar
- `CascadeType.MERGE` - atualizar
- `CascadeType.REMOVE` - deletar
- `CascadeType.REFRESH` - recarregar
- `CascadeType.DETACH` - desanexar
- `CascadeType.ALL` - todas as operações

### FetchType

Define quando carregar os dados relacionados:

```java
@ManyToOne(fetch = FetchType.LAZY)
private Category category;
```

**Tipos:**
- `FetchType.LAZY` - carrega sob demanda (padrão para `@OneToMany`, `@ManyToMany`)
- `FetchType.EAGER` - carrega imediatamente (padrão para `@ManyToOne`, `@OneToOne`)

### mappedBy

Define o lado inverso do relacionamento bidirecional:

```java
// lado proprietário (tem @JoinColumn)
@ManyToOne
@JoinColumn(name = "category_id")
private Category category;

// lado inverso (tem mappedBy)
@OneToMany(mappedBy = "category")
private Set<Product> products;
```

## Repository Pattern

```java
public interface ProductRepository extends JpaRepository<Product, Long> {
    // métodos padrão herdados:
    // findAll(), findById(id), save(entity), deleteById(id), etc.
    
    // query methods personalizados
    List<Product> findByName(String name);
    List<Product> findByPriceGreaterThan(Double price);
    
    @Query("SELECT p FROM Product p WHERE p.price BETWEEN :min AND :max")
    List<Product> findByPriceRange(@Param("min") Double min, @Param("max") Double max);
}
```

## Boas Práticas

1. **Use `@Table`** para nomear tabelas explicitamente
2. **Defina `equals()` e `hashCode()`** baseados no ID (ou campos de negócio)
3. **Evite `FetchType.EAGER`** para evitar N+1 queries
4. **Use DTOs** para retornar dados ao cliente (não exponha entidades diretamente)
5. **Sempre defina `mappedBy`** em relacionamentos bidirecionais
6. **Use `Set` em vez de `List`** para relacionamentos `@ManyToMany` (melhor performance)
7. **Prefira `CascadeType` específicos** em vez de `ALL` (mais controle)

## Exemplo Completo de Relacionamento

```java
@Entity
@Table(name = "tb_order")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Instant moment;
    
    @ManyToOne
    @JoinColumn(name = "client_id")
    private User client;
    
    @OneToMany(mappedBy = "id.order", cascade = CascadeType.ALL)
    private Set<OrderItem> items = new HashSet<>();
    
    public Double getTotal() {
        return items.stream()
            .mapToDouble(OrderItem::getSubTotal)
            .sum();
    }
}

@Entity
public class OrderItem {
    @EmbeddedId
    private OrderItemPK id = new OrderItemPK();
    
    private Integer quantity;
    private Double price;
    
    public Double getSubTotal() {
        return quantity * price;
    }
}

@Embeddable
public class OrderItemPK {
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
    
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    
    // equals e hashCode
}
```

---

**Resumo**: JPA simplifica o acesso a dados mapeando objetos Java para tabelas SQL. Use anotações para definir entidades, relacionamentos e estratégias de persistência. Sempre combine com Repository e Service para separação de responsabilidades.

**Desenvolvido para fins de consulta - Flávio Antonio Demétrio**