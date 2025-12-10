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
  ```

---

## 📚 Relacionamentos JPA

### ✅ 1. One-to-One (1:1)
**Use quando cada entidade só pode ter uma do outro lado.**

**Exemplo do projeto:**
- **Order ↔ Payment** (um pedido tem um único pagamento e um pagamento pertence a um único pedido)
  ```java
  // Em Order.java
  @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
  private Payment payment;
  
  // Em Payment.java
  @OneToOne
  @MapsId  // Payment usa o mesmo ID do Order
  private Order order;
  ```

📌 **Se puder ter mais de um → não é 1:1, é 1:N.**

---

### ✅ 2. One-to-Many / Many-to-One (1:N)
**É o mais comum. Um lado tem vários; o outro tem apenas um.**

**Exemplo do projeto:**
- **User ↔ Order** (um usuário pode ter vários pedidos, mas cada pedido pertence a um único usuário)
  ```java
  // Em User.java (lado One - tem vários)
  @OneToMany(mappedBy = "client")
  private List<Order> orders = new ArrayList<>();
  
  // Em Order.java (lado Many - tem apenas um)
  @ManyToOne
  @JoinColumn(name = "client_id")  // FK fica aqui no lado Many
  private User client;
  ```

📌 **A FK (Foreign Key) sempre fica no lado Many (Order).**

---

### ✅ 3. Many-to-Many (N:N) simples
**Use somente quando a tabela intermediária não tem nenhuma coluna extra.**

**Exemplo do projeto:**
- **Product ↔ Category** (um produto pode ter várias categorias e uma categoria pode ter vários produtos)
  ```java
  // Em Product.java (lado proprietário - define a tabela intermediária)
  @ManyToMany
  @JoinTable(
      name = "tb_product_category",
      joinColumns = @JoinColumn(name = "product_id"),
      inverseJoinColumns = @JoinColumn(name = "category_id")
  )
  private Set<Category> categories = new HashSet<>();
  
  // Em Category.java (lado inverso)
  @ManyToMany(mappedBy = "categories")
  private Set<Product> products = new HashSet<>();
  ```

📌 **Se for só ligação (sem dados extras), pode usar @ManyToMany.**

---

### ❌ 4. Many-to-Many real (com dados extras) → classe de associação
**Quando o relacionamento precisa de atributos, NÃO é N:N técnico.**

**Exemplo real do projeto:**
- **Order ↔ Product** através de **OrdemItem** → precisa de `quantity` (quantidade) e `price` (preço unitário)

**Solução implementada no projeto:**

1. **Chave composta (OrderItemPk.java):**
   ```java
   @Embeddable
   public class OrderItemPk {
       @ManyToOne
       @JoinColumn(name = "order_id")
       private Order order;
       
       @ManyToOne
       @JoinColumn(name = "product_id")
       private Product product;
   }
   ```

2. **Entidade de associação (OrdemItem.java):**
   ```java
   @Entity
   @Table(name = "tb_order_item")
   public class OrdemItem {
       @EmbeddedId
       private OrderItemPk id = new OrderItemPk();
       
       private Integer quantity;  // dado extra
       private Double price;      // dado extra
       
       // Métodos auxiliares para acessar order e product
       public Order getOrder() {
           return id.getOrder();
       }
       
       public Product getProduct() {
           return id.getProduct();
       }
   }
   ```

3. **Referência nas entidades principais:**
   ```java
   // Em Order.java
   @OneToMany(mappedBy = "id.order")
   private Set<OrdemItem> items = new HashSet<>();
   
   // Em Product.java
   @OneToMany(mappedBy = "id.product")
   private Set<OrdemItem> items = new HashSet<>();
   ```

📌 **Cria-se uma entidade própria (OrdemItem) com:**
- Chave composta embutida (`@EmbeddedId`)
- Duas relações `@ManyToOne` dentro da chave (order e product)
- Atributos extras (quantity, price)

---

### 🎯 REGRA DE OURO (a única que você precisa lembrar)

> **👉 Se o relacionamento tiver dados extras → NÃO é ManyToMany.**  
> **Use uma entidade associativa com duas relações ManyToOne.**

---

### 🎯 Tabela de Decisão - Regra simples para escolher:

| Situação | Tipo |
|----------|------|
| Só 1 do outro lado | **1:1** |
| 1 tem vários | **1:N + N:1** |
| Ambos têm vários, sem atributos extras | **N:N simples** (`@ManyToMany`) |
| Ambos têm vários, com atributos extras | **Classe de associação** (N:N real) |
