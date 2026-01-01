# Tratamento de Exceções em Spring Boot

## 📌 Visão Geral

Este documento descreve a implementação completa de um sistema de tratamento de exceções padronizado em aplicações Spring Boot REST API, utilizando **`@ControllerAdvice`** e exceções personalizadas.

## 🎯 Objetivos

- ✅ Centralizar o tratamento de exceções em um único lugar
- ✅ Fornecer respostas consistentes e padronizadas
- ✅ Retornar códigos HTTP apropriados
- ✅ Separar exceções de negócio das exceções técnicas
- ✅ Facilitar debug e manutenção
- ✅ Melhorar a experiência do desenvolvedor que consome a API

---

## 🏗️ Arquitetura do Tratamento de Exceções

### Fluxo Completo

```
┌─────────────────────────────────────────────────────────────────┐
│                          CLIENT                                  │
│                    (HTTP Request)                                │
└────────────────────────┬────────────────────────────────────────┘
                         │
                         ↓
┌─────────────────────────────────────────────────────────────────┐
│                      CONTROLLER                                  │
│                  ProductController                               │
│  • Recebe requisição                                            │
│  • Chama Service                                                │
│  • Não trata exceções (delega)                                  │
└────────────────────────┬────────────────────────────────────────┘
                         │
                         ↓
┌─────────────────────────────────────────────────────────────────┐
│                       SERVICE                                    │
│                   ProductService                                 │
│  • Executa lógica de negócio                                    │
│  • Valida regras                                                │
│  • LANÇA ResourceNotFoundException (se necessário)              │
└────────────────────────┬────────────────────────────────────────┘
                         │
                         ↓ (Exception lançada)
┌─────────────────────────────────────────────────────────────────┐
│                  @ControllerAdvice                               │
│           ControllerExceptionHandler                             │
│  • Intercepta TODAS as exceções da aplicação                    │
│  • Identifica tipo de exceção                                   │
│  • Cria CustomError padronizado                                 │
│  • Define status HTTP apropriado                                │
│  • Retorna ResponseEntity<CustomError>                          │
└────────────────────────┬────────────────────────────────────────┘
                         │
                         ↓
┌─────────────────────────────────────────────────────────────────┐
│                        CLIENT                                    │
│                  (HTTP Response)                                 │
│  {                                                              │
│    "timestamp": "2026-01-01T10:30:00.123Z",                     │
│    "status": 404,                                               │
│    "error": "Recurso não encontrado",                           │
│    "path": "/products/999"                                      │
│  }                                                              │
└─────────────────────────────────────────────────────────────────┘
```

---

## 🧩 Componentes da Solução

### 1. Exceção Personalizada

#### ResourceNotFoundException.java

**Localização:** `src/main/java/br/com/klsys/dscommerce/services/exceptions/`

```java
package br.com.klsys.dscommerce.services.exceptions;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
```

**Características:**

| Aspecto | Detalhamento |
|---------|--------------|
| **Tipo** | Extends `RuntimeException` (unchecked exception) |
| **Pacote** | `services.exceptions` - junto com lógica de negócio |
| **Uso** | Lançada quando recurso não é encontrado (ID inválido) |
| **Construtor** | Recebe mensagem customizada |
| **Propagação** | Não requer declaração `throws` nas assinaturas |

**Por que RuntimeException?**

- ✅ Não obriga try-catch em cada camada
- ✅ Permite propagação automática até o `@ControllerAdvice`
- ✅ Simplifica código (sem poluição de `throws`)
- ✅ Adequado para exceções de negócio

---

### 2. DTO de Erro Padronizado

#### CustomError.java

**Localização:** `src/main/java/br/com/klsys/dscommerce/dto/`

```java
package br.com.klsys.dscommerce.dto;

import java.time.Instant;

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
    public Instant getTimestamp() {
        return timestamp;
    }

    public Integer getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getPath() {
        return path;
    }
}
```

**Estrutura do Erro:**

| Campo | Tipo | Descrição | Exemplo |
|-------|------|-----------|---------|
| `timestamp` | `Instant` | Data/hora do erro em UTC | `2026-01-01T10:30:00.123Z` |
| `status` | `Integer` | Código HTTP do erro | `404`, `400`, `500` |
| `error` | `String` | Mensagem descritiva | `"Recurso não encontrado"` |
| `path` | `String` | URI da requisição que falhou | `"/products/999"` |

**Vantagens:**

- ✅ Formato consistente em todos os erros
- ✅ Facilita parsing no frontend
- ✅ Informações suficientes para debug
- ✅ Timestamp para correlação com logs
- ✅ Path identifica endpoint problemático

---

### 3. Handler Global de Exceções

#### ControllerExceptionHandler.java

**Localização:** `src/main/java/br/com/klsys/dscommerce/controllers/handlers/`

```java
package br.com.klsys.dscommerce.controllers.handlers;

import br.com.klsys.dscommerce.dto.CustomError;
import br.com.klsys.dscommerce.services.exceptions.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.time.Instant;

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

**Anotações:**

| Anotação | Função |
|----------|--------|
| `@ControllerAdvice` | Torna a classe um interceptor global de exceções |
| `@ExceptionHandler(ResourceNotFoundException.class)` | Define qual exceção será tratada |

**Parâmetros do método:**

| Parâmetro | Tipo | Descrição |
|-----------|------|-----------|
| `e` | `ResourceNotFoundException` | Exceção capturada (acesso à mensagem) |
| `request` | `HttpServletRequest` | Request HTTP (acesso ao path) |

**Fluxo interno:**

1. Define status HTTP apropriado (`404 NOT_FOUND`)
2. Cria objeto `CustomError` com:
   - Timestamp atual
   - Código de status
   - Mensagem da exceção
   - URI da requisição
3. Retorna `ResponseEntity` com status e corpo

---

## 🔄 Uso nos Services

### ProductService.java (Exemplos)

#### Exemplo 1: findById - Optional.orElseThrow()

```java
@Transactional(readOnly = true)
public ProductDTO findById(Long id) {
    Product product = repository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Recurso não encontrado"));
    return new ProductDTO(product);
}
```

**Explicação:**
- `findById()` retorna `Optional<Product>`
- Se vazio, lança `ResourceNotFoundException`
- Exceção propaga até `@ControllerAdvice`

---

#### Exemplo 2: update - Try-Catch + Conversão

```java
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
```

**Explicação:**
- `getReferenceById()` lança `EntityNotFoundException` (JPA)
- Capturamos e convertemos para nossa exceção de negócio
- Mantém abstração de camadas (controller não sabe de JPA)

---

#### Exemplo 3: delete - Validação + Integridade

```java
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
```

**Explicação:**
- Valida existência antes de deletar
- Captura `DataIntegrityViolationException` (FK constraint)
- Lança exceção com mensagem apropriada

---

## 📡 Exemplos de Requisições e Respostas

### Cenário 1: Buscar produto inexistente (GET)

**Request:**
```http
GET http://localhost:8080/products/999
```

**Response:** `404 Not Found`
```json
{
  "timestamp": "2026-01-01T10:30:00.123456Z",
  "status": 404,
  "error": "Recurso não encontrado",
  "path": "/products/999"
}
```

---

### Cenário 2: Atualizar produto inexistente (PUT)

**Request:**
```http
PUT http://localhost:8080/products/999
Content-Type: application/json

{
  "name": "Produto Teste",
  "description": "Descrição",
  "price": 100.00,
  "imgUrl": "http://example.com/img.jpg"
}
```

**Response:** `404 Not Found`
```json
{
  "timestamp": "2026-01-01T10:31:20.456789Z",
  "status": 404,
  "error": "Recurso não encontrado",
  "path": "/products/999"
}
```

---

### Cenário 3: Deletar produto inexistente (DELETE)

**Request:**
```http
DELETE http://localhost:8080/products/999
```

**Response:** `404 Not Found`
```json
{
  "timestamp": "2026-01-01T10:32:10.789012Z",
  "status": 404,
  "error": "Recurso não encontrado",
  "path": "/products/999"
}
```

---

### Cenário 4: Deletar produto com integridade referencial

**Request:**
```http
DELETE http://localhost:8080/products/1
```

**Response:** `404 Not Found`
```json
{
  "timestamp": "2026-01-01T10:33:45.321654Z",
  "status": 404,
  "error": "Falha de integridade referencial",
  "path": "/products/1"
}
```

**Observação:** Produto tem relacionamento com outras entidades (OrderItem).

---

## 🚀 Expandindo o Sistema

### Adicionando Mais Exceções

#### 1. DatabaseException (Erro de Integridade)

```java
package br.com.klsys.dscommerce.services.exceptions;

public class DatabaseException extends RuntimeException {
    public DatabaseException(String message) {
        super(message);
    }
}
```

**Handler:**
```java
@ExceptionHandler(DatabaseException.class)
public ResponseEntity<CustomError> database(DatabaseException e, HttpServletRequest request) {
    HttpStatus status = HttpStatus.BAD_REQUEST;
    CustomError err = new CustomError(Instant.now(), status.value(), e.getMessage(), request.getRequestURI());
    return ResponseEntity.status(status).body(err);
}
```

**Uso no Service:**
```java
@Transactional(propagation = Propagation.SUPPORTS)
public void delete(Long id) {
    if (!repository.existsById(id)) {
        throw new ResourceNotFoundException("Recurso não encontrado");
    }
    try {
        repository.deleteById(id);
    } catch (DataIntegrityViolationException e) {
        throw new DatabaseException("Falha de integridade referencial");
    }
}
```

---

#### 2. ValidationException (Bean Validation)

**Dependência necessária:**
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

**DTO com validações:**
```java
public class ProductDTO {
    private Long id;
    
    @NotBlank(message = "Nome não pode ser vazio")
    @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
    private String name;
    
    @NotBlank(message = "Descrição não pode ser vazia")
    private String description;
    
    @Positive(message = "Preço deve ser positivo")
    private Double price;
    
    private String imgUrl;
    
    // construtores, getters e setters
}
```

**Controller com validação:**
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
```

**DTO de erro com campos:**
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

public class FieldMessage {
    private String fieldName;
    private String message;

    public FieldMessage(String fieldName, String message) {
        this.fieldName = fieldName;
        this.message = message;
    }

    // getters
}
```

**Handler:**
```java
@ExceptionHandler(MethodArgumentNotValidException.class)
public ResponseEntity<CustomError> methodArgumentNotValid(
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

**Resposta de validação:**
```json
{
  "timestamp": "2026-01-01T10:40:00.123Z",
  "status": 422,
  "error": "Dados inválidos",
  "path": "/products",
  "errors": [
    {
      "fieldName": "name",
      "message": "Nome não pode ser vazio"
    },
    {
      "fieldName": "price",
      "message": "Preço deve ser positivo"
    }
  ]
}
```

---

#### 3. ForbiddenException (Autorização)

```java
package br.com.klsys.dscommerce.services.exceptions;

public class ForbiddenException extends RuntimeException {
    public ForbiddenException(String message) {
        super(message);
    }
}
```

**Handler:**
```java
@ExceptionHandler(ForbiddenException.class)
public ResponseEntity<CustomError> forbidden(ForbiddenException e, HttpServletRequest request) {
    HttpStatus status = HttpStatus.FORBIDDEN;
    CustomError err = new CustomError(Instant.now(), status.value(), e.getMessage(), request.getRequestURI());
    return ResponseEntity.status(status).body(err);
}
```

---

#### 4. Exception Genérica (Fallback)

```java
@ExceptionHandler(Exception.class)
public ResponseEntity<CustomError> handleGenericException(Exception e, HttpServletRequest request) {
    HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
    CustomError err = new CustomError(
        Instant.now(), 
        status.value(), 
        "Erro interno do servidor", 
        request.getRequestURI()
    );
    
    // Log detalhado do erro
    e.printStackTrace();
    
    return ResponseEntity.status(status).body(err);
}
```

---

## 📊 Mapeamento de Exceções × Status HTTP

| Exceção | Status HTTP | Código | Cenário |
|---------|-------------|--------|---------|
| `ResourceNotFoundException` | `NOT_FOUND` | 404 | Recurso não existe |
| `DatabaseException` | `BAD_REQUEST` | 400 | Integridade referencial |
| `MethodArgumentNotValidException` | `UNPROCESSABLE_ENTITY` | 422 | Validação de dados |
| `ForbiddenException` | `FORBIDDEN` | 403 | Sem permissão |
| `UnauthorizedException` | `UNAUTHORIZED` | 401 | Não autenticado |
| `Exception` (genérica) | `INTERNAL_SERVER_ERROR` | 500 | Erro inesperado |

---

## ✅ Boas Práticas

### 1. Separação de Responsabilidades

```java
// ❌ ERRADO - Controller tratando exceção
@GetMapping("/{id}")
public ResponseEntity<ProductDTO> findById(@PathVariable Long id) {
    try {
        ProductDTO dto = service.findById(id);
        return ResponseEntity.ok(dto);
    } catch (Exception e) {
        return ResponseEntity.notFound().build();
    }
}

// ✅ CORRETO - Delega para @ControllerAdvice
@GetMapping("/{id}")
public ResponseEntity<ProductDTO> findById(@PathVariable Long id) {
    ProductDTO dto = service.findById(id);
    return ResponseEntity.ok(dto);
}
```

---

### 2. Mensagens Claras e Específicas

```java
// ❌ ERRADO - Mensagem genérica
throw new ResourceNotFoundException("Erro");

// ✅ CORRETO - Mensagem descritiva
throw new ResourceNotFoundException("Produto com ID " + id + " não encontrado");

// ✅ MELHOR AINDA - Internacionalização
throw new ResourceNotFoundException(messageSource.getMessage("product.not.found", new Object[]{id}, locale));
```

---

### 3. Não Expor Detalhes Internos

```java
// ❌ ERRADO - Expõe stack trace
throw new ResourceNotFoundException(e.getMessage() + "\n" + e.getStackTrace());

// ✅ CORRETO - Mensagem amigável, log interno
try {
    // operação
} catch (SQLException e) {
    logger.error("Erro ao acessar banco de dados", e);
    throw new DatabaseException("Erro ao processar requisição");
}
```

---

### 4. Logar Exceções Apropriadamente

```java
@ExceptionHandler(ResourceNotFoundException.class)
public ResponseEntity<CustomError> resourceNotFound(
        ResourceNotFoundException e, 
        HttpServletRequest request) {
    
    // Log de nível INFO (esperado)
    logger.info("Recurso não encontrado: {}", e.getMessage());
    
    HttpStatus status = HttpStatus.NOT_FOUND;
    CustomError err = new CustomError(Instant.now(), status.value(), e.getMessage(), request.getRequestURI());
    return ResponseEntity.status(status).body(err);
}

@ExceptionHandler(Exception.class)
public ResponseEntity<CustomError> handleGenericException(
        Exception e, 
        HttpServletRequest request) {
    
    // Log de nível ERROR (inesperado)
    logger.error("Erro interno: ", e);
    
    HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
    CustomError err = new CustomError(Instant.now(), status.value(), "Erro interno", request.getRequestURI());
    return ResponseEntity.status(status).body(err);
}
```

---

### 5. Usar Hierarquia de Exceções

```java
// Exceção base
public abstract class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}

// Exceções específicas
public class ResourceNotFoundException extends BusinessException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}

public class DatabaseException extends BusinessException {
    public DatabaseException(String message) {
        super(message);
    }
}

// Handler genérico
@ExceptionHandler(BusinessException.class)
public ResponseEntity<CustomError> handleBusinessException(
        BusinessException e, 
        HttpServletRequest request) {
    // Tratamento comum para todas as exceções de negócio
}
```

---

## 🧪 Testando o Tratamento de Exceções

### Teste Unitário do Handler

```java
@ExtendWith(MockitoExtension.class)
class ControllerExceptionHandlerTest {

    @InjectMocks
    private ControllerExceptionHandler handler;

    @Mock
    private HttpServletRequest request;

    @Test
    void testResourceNotFoundException() {
        // Arrange
        ResourceNotFoundException exception = new ResourceNotFoundException("Recurso não encontrado");
        when(request.getRequestURI()).thenReturn("/products/999");

        // Act
        ResponseEntity<CustomError> response = handler.resourceNotFound(exception, request);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(404, response.getBody().getStatus());
        assertEquals("Recurso não encontrado", response.getBody().getError());
        assertEquals("/products/999", response.getBody().getPath());
        assertNotNull(response.getBody().getTimestamp());
    }
}
```

### Teste de Integração

```java
@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void findByIdShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        mockMvc.perform(get("/products/999")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Recurso não encontrado"))
                .andExpect(jsonPath("$.path").value("/products/999"))
                .andExpect(jsonPath("$.timestamp").exists());
    }
}
```

---

## 📚 Estrutura de Pacotes Recomendada

```
src/main/java/
└── br/com/klsys/dscommerce/
    ├── controllers/
    │   ├── ProductController.java
    │   └── handlers/
    │       └── ControllerExceptionHandler.java
    │
    ├── services/
    │   ├── ProductService.java
    │   └── exceptions/
    │       ├── ResourceNotFoundException.java
    │       ├── DatabaseException.java
    │       ├── ForbiddenException.java
    │       └── BusinessException.java (abstrata)
    │
    └── dto/
        ├── ProductDTO.java
        ├── CustomError.java
        ├── ValidationError.java (extends CustomError)
        └── FieldMessage.java
```

---

## 🔑 Checklist de Implementação

- [ ] Criar exceções personalizadas em `services/exceptions/`
- [ ] Criar DTO `CustomError` em `dto/`
- [ ] Criar `ControllerExceptionHandler` com `@ControllerAdvice`
- [ ] Adicionar `@ExceptionHandler` para cada tipo de exceção
- [ ] Lançar exceções apropriadas nos Services
- [ ] Não tratar exceções nos Controllers
- [ ] Retornar status HTTP corretos
- [ ] Incluir timestamp, status, mensagem e path
- [ ] Logar exceções apropriadamente
- [ ] Testar todos os cenários de erro
- [ ] Documentar exceções na API (Swagger/OpenAPI)

---

## 📖 Referências

- [Spring Framework - Exception Handling](https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-controller/ann-exceptionhandler.html)
- [Spring Boot - Error Handling](https://docs.spring.io/spring-boot/docs/current/reference/html/web.html#web.servlet.spring-mvc.error-handling)
- [RFC 7807 - Problem Details for HTTP APIs](https://datatracker.ietf.org/doc/html/rfc7807)
- [HTTP Status Codes](https://developer.mozilla.org/en-US/docs/Web/HTTP/Status)

---

## 💡 Resumo

**Tratamento de exceções padronizado:**

1. **Exceção customizada** (`ResourceNotFoundException`) → lançada no Service
2. **@ControllerAdvice** (`ControllerExceptionHandler`) → intercepta globalmente
3. **CustomError** (DTO) → formato padronizado de resposta
4. **Status HTTP correto** → semântica adequada (404, 400, 500, etc.)
5. **Sem try-catch no Controller** → responsabilidade única

**Benefícios:**
- ✅ Código limpo e organizado
- ✅ Respostas consistentes
- ✅ Facilita manutenção
- ✅ Melhora experiência do desenvolvedor
- ✅ Facilita debug e troubleshooting

---

**Desenvolvido para fins de consulta - Flávio Antonio Demétrio**

