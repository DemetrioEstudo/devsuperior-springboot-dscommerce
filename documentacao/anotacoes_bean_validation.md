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

```java
public class UsuarioDTO {

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @Email(message = "E-mail inválido")
    private String email;

    @Size(min = 6, max = 20, message = "Senha deve ter entre 6 e 20 caracteres")
    private String senha;

    @Min(value = 18, message = "Idade mínima é 18 anos")
    private Integer idade;
}
```

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

```java
@RestControllerAdvice
public class ValidationExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handle(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors()
          .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

        return ResponseEntity.badRequest().body(errors);
    }
}
```

Retorno exemplo:
```json
{
  "nome": "Nome é obrigatório",
  "senha": "Senha deve ter entre 6 e 20 caracteres"
}
```

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

### Imports comuns

```java
import jakarta.validation.constraints.*;
```

Hibernate extras:
```java
import org.hibernate.validator.constraints.*;
```

**Desenvolvido para fins de consulta - Flávio Antonio Demétrio**