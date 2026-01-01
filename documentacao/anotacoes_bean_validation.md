# Bean Validation no Java com Spring

## Parte 1 — Como usar Bean Validation

### O que é
Bean Validation é uma especificação do Java para validar dados de objetos (beans) usando anotações. No Spring Boot, a implementação padrão é o **Hibernate Validator**.

Ela permite validar dados **antes** da lógica de negócio ser executada, garantindo consistência e segurança na entrada de dados.

---

### Dependência
Na maioria dos projetos Spring Boot, já vem incluída ao usar `spring-boot-starter-web`.

Caso precise adicionar explicitamente:

```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

---

### Onde usar
- DTOs (boa prática principal)
- Entidades JPA (com cuidado)
- Parâmetros de métodos
- Requests de API

> **Regra de ouro:**
> DTO valida entrada de dados, Service valida regra de negócio.

---

### Exemplo de DTO com validações

#### ProductDTO (Implementado no Projeto)

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

    // Construtor vazio
    public ProductDTO() {
    }

    // Construtor a partir da entidade
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
- ✅ `name`: obrigatório, entre 3 e 80 caracteres
- ✅ `description`: obrigatório, mínimo 10 caracteres
- ✅ `price`: deve ser positivo
- ✅ `imgUrl`: sem validação (opcional)

---

### Ativando a validação no Controller

```java
@PostMapping("/usuarios")
public ResponseEntity<Void> criar(@Valid @RequestBody UsuarioDTO dto) {
    return ResponseEntity.ok().build();
}
```

- `@Valid` ativa o processo de validação
- Em caso de erro, o Spring retorna **HTTP 400 (Bad Request)** automaticamente

---

### Tratamento global de erros

#### ControllerExceptionHandler (Implementado no Projeto)

```java
package br.com.klsys.dscommerce.controllers.handlers;

import br.com.klsys.dscommerce.dto.CustomError;
import br.com.klsys.dscommerce.dto.ValidationError;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.time.Instant;

@ControllerAdvice
public class ControllerExceptionHandler {
    
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
}
```

#### ValidationError (extends CustomError)

```java
package br.com.klsys.dscommerce.dto;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

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

#### FieldMessage

```java
package br.com.klsys.dscommerce.dto;

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

**Retorno exemplo (HTTP 422):**
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

**Por que HTTP 422 (Unprocessable Entity)?**
- ✅ Mais semântico que 400 (Bad Request)
- ✅ Indica que a estrutura está correta, mas os dados são inválidos
- ✅ Padrão recomendado para erros de validação

---

### @Valid vs @Validated

| Anotação | Descrição |
|--------|----------|
| `@Valid` | Padrão Java, simples e mais usado |
| `@Validated` | Spring, suporta grupos de validação |

---

## Parte 2 — Lista de Anotações de Bean Validation

### Nulidade e Texto
| Anotação | Descrição |
|--------|----------|
| `@NotNull` | Não permite null |
| `@NotEmpty` | Não permite null nem vazio |
| `@NotBlank` | Não permite null, vazio ou espaços |

---

### Tamanho
| Anotação | Descrição |
|--------|----------|
| `@Size(min, max)` | Tamanho mínimo e máximo |
| `@Length(min, max)` | Variante do Hibernate |

---

### Numéricos
| Anotação | Descrição |
|--------|----------|
| `@Min` / `@Max` | Valor mínimo e máximo |
| `@Positive` | Apenas positivos |
| `@PositiveOrZero` | Positivo ou zero |
| `@Negative` | Apenas negativos |
| `@NegativeOrZero` | Negativo ou zero |
| `@Digits(integer, fraction)` | Limite de dígitos |
| `@DecimalMin` / `@DecimalMax` | Limite decimal |

---

### Datas
| Anotação | Descrição |
|--------|----------|
| `@Past` | Data no passado |
| `@PastOrPresent` | Passado ou presente |
| `@Future` | Data no futuro |
| `@FutureOrPresent` | Futuro ou presente |

---

### Formato e Regex
| Anotação | Descrição |
|--------|----------|
| `@Email` | Valida e-mail |
| `@Pattern(regexp)` | Regex personalizada |
| `@URL` | Valida URL (Hibernate) |

---

### Boolean
| Anotação | Descrição |
|--------|----------|
| `@AssertTrue` | Deve ser true |
| `@AssertFalse` | Deve ser false |

---

### Coleções
| Anotação | Descrição |
|--------|----------|
| `@NotEmpty` | Coleção não vazia |
| `@Size` | Quantidade mínima/máxima |
| `@Valid` | Valida objetos internos |

---

### Validações Customizadas
| Anotação / Interface | Descrição |
|--------------------|----------|
| `@Constraint` | Define validação customizada |
| `ConstraintValidator` | Implementa a lógica |

---

### Extras (Hibernate Validator)
| Anotação | Descrição |
|--------|----------|
| `@Range` | Intervalo numérico |
| `@CPF` / `@CNPJ` | Documentos brasileiros |
| `@ISBN` | ISBN |

---

---

## Parte 3 — Boas Práticas para Mensagens de Validação

### Mensagens Amigáveis vs Mensagens Técnicas

#### ❌ Mensagens Ruins (Técnicas/Genéricas)

```java
@NotBlank(message = "Campo obrigatório")
private String name;

@Size(min = 3, max = 80, message = "Tamanho inválido")
private String description;

@Positive(message = "Valor inválido")
private Double price;
```

**Problemas:**
- Não indica qual campo tem problema
- Não especifica os limites/regras
- Usuário não sabe como corrigir

---

#### ✅ Mensagens Boas (Descritivas e Amigáveis)

```java
@NotBlank(message = "Nome não pode estar em branco")
@Size(min = 3, max = 80, message = "Nome deve ter entre 3 e 80 caracteres")
private String name;

@NotBlank(message = "Descrição não pode estar em branco")
@Size(min = 10, message = "Descrição precisa ter no mínimo 10 caracteres")
private String description;

@Positive(message = "O preço deve ser um valor positivo")
private Double price;
```

**Vantagens:**
- ✅ Identifica claramente o campo
- ✅ Especifica limites exatos
- ✅ Linguagem natural e compreensível
- ✅ Orienta como corrigir o erro

---

### Guia de Mensagens por Tipo de Validação

#### Campos Obrigatórios

```java
// ✅ BOM
@NotBlank(message = "Nome não pode estar em branco")
@NotNull(message = "Data de nascimento é obrigatória")
@NotEmpty(message = "Lista de categorias não pode estar vazia")

// ❌ EVITAR
@NotBlank(message = "Required")
@NotNull(message = "Campo obrigatório")
```

---

#### Limites de Tamanho

```java
// ✅ BOM - Especifica os limites
@Size(min = 3, max = 80, message = "Nome deve ter entre 3 e 80 caracteres")
@Size(min = 10, message = "Descrição precisa ter no mínimo 10 caracteres")
@Size(max = 500, message = "Comentário não pode exceder 500 caracteres")

// ❌ EVITAR - Genérico
@Size(min = 3, max = 80, message = "Tamanho inválido")
```

---

#### Valores Numéricos

```java
// ✅ BOM
@Positive(message = "O preço deve ser um valor positivo")
@Min(value = 18, message = "Idade mínima permitida é 18 anos")
@Max(value = 100, message = "Quantidade máxima é 100 unidades")
@DecimalMin(value = "0.01", message = "Desconto deve ser no mínimo 0.01")

// ❌ EVITAR
@Positive(message = "Valor inválido")
@Min(value = 18, message = "Menor que o mínimo")
```

---

#### Formato e Padrões

```java
// ✅ BOM
@Email(message = "E-mail deve ser válido (ex: usuario@dominio.com)")
@Pattern(regexp = "^\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}$", 
         message = "CPF deve estar no formato 000.000.000-00")
@URL(message = "URL da imagem deve ser válida (ex: https://exemplo.com/img.jpg)")

// ❌ EVITAR
@Email(message = "Formato incorreto")
@Pattern(regexp = "...", message = "Padrão inválido")
```

---

#### Datas

```java
// ✅ BOM
@Past(message = "Data de nascimento deve estar no passado")
@Future(message = "Data de entrega deve ser futura")
@PastOrPresent(message = "Data do pedido não pode ser futura")

// ❌ EVITAR
@Past(message = "Data inválida")
@Future(message = "Erro de data")
```

---

### Checklist de Mensagens de Qualidade

- [ ] Menciona o nome do campo na mensagem
- [ ] Especifica valores/limites quando aplicável
- [ ] Usa linguagem natural (não técnica)
- [ ] Em português correto (ou idioma da aplicação)
- [ ] Orienta o usuário sobre o formato esperado
- [ ] Inclui exemplos quando útil (ex: formato de CPF)
- [ ] Evita termos técnicos (ex: "null", "regex")
- [ ] É cortês e profissional (sem humor ou sarcasmo)

---

### Internacionalização (i18n)

Para aplicações multilíngues, use arquivos de mensagens:

**messages.properties:**
```properties
product.name.notblank=Nome não pode estar em branco
product.name.size=Nome deve ter entre {min} e {max} caracteres
product.price.positive=O preço deve ser um valor positivo
```

**ProductDTO:**
```java
@NotBlank(message = "{product.name.notblank}")
@Size(min = 3, max = 80, message = "{product.name.size}")
private String name;

@Positive(message = "{product.price.positive}")
private Double price;
```

---

### Mensagens Dinâmicas com Interpolação

Você pode usar expressões nas mensagens:

```java
@Size(min = 3, max = 80, 
      message = "Nome deve ter entre {min} e {max} caracteres")
private String name;

@DecimalMin(value = "0.01", 
            message = "Preço mínimo é {value}")
private Double price;
```

Spring Boot substitui automaticamente `{min}`, `{max}`, `{value}` pelos valores da anotação.

---

### Exemplo Completo: ProductDTO com Mensagens Amigáveis

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
    
    @NotNull(message = "Preço é obrigatório")
    @Positive(message = "O preço deve ser um valor positivo")
    private Double price;
    
    @Pattern(regexp = "^https?://.*", 
             message = "URL da imagem deve começar com http:// ou https://")
    private String imgUrl;

    // Construtores, getters e setters
}
```

**Resposta de validação (422):**
```json
{
  "timestamp": "2026-01-01T10:30:00.123Z",
  "status": 422,
  "error": "Dados inválidos",
  "path": "/products",
  "errors": [
    {
      "fieldName": "name",
      "message": "Nome não pode estar em branco"
    },
    {
      "fieldName": "price",
      "message": "O preço deve ser um valor positivo"
    },
    {
      "fieldName": "imgUrl",
      "message": "URL da imagem deve começar com http:// ou https://"
    }
  ]
}
```

---

### Imports comuns

```java
import jakarta.validation.constraints.*;
```

Hibernate extras:
```java
import org.hibernate.validator.constraints.*;
```

**Desenvolvido para fins de consulta - Flávio Antonio Demétrio**