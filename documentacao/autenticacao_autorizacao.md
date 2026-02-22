# Autenticação e Autorização - DSCommerce

## 📑 Índice

1. [Visão Geral](#visão-geral)
2. [Conceitos Fundamentais](#conceitos-fundamentais)
3. [Arquitetura da Implementação](#arquitetura-da-implementação)
4. [Authorization Server](#authorization-server)
5. [Resource Server](#resource-server)
6. [Custom Grant Type (Password)](#custom-grant-type-password)
7. [JWT (JSON Web Token)](#jwt-json-web-token)
8. [CORS (Cross-Origin Resource Sharing)](#cors-cross-origin-resource-sharing)
9. [Fluxo de Autenticação](#fluxo-de-autenticação)
10. [Configurações](#configurações)
11. [Como Usar](#como-usar)
12. [Troubleshooting](#troubleshooting)

---

## Visão Geral

Este projeto implementa um sistema completo de **autenticação** e **autorização** utilizando **OAuth2** com **Spring Security** e **JWT (JSON Web Tokens)**. 

A arquitetura segue o padrão **Authorization Server** + **Resource Server**, onde:

- **Authorization Server**: Responsável por autenticar usuários e emitir tokens JWT
- **Resource Server**: Protege as APIs REST e valida os tokens JWT

### Tecnologias Utilizadas

- **Spring Security 6.4.2**
- **Spring Security OAuth2 Authorization Server 1.4.1**
- **JWT (JSON Web Tokens)**
- **BCrypt** para criptografia de senhas
- **RSA 2048 bits** para assinatura de tokens

---

## Conceitos Fundamentais

### Autenticação vs Autorização

| Conceito | Descrição | Exemplo |
|----------|-----------|---------|
| **Autenticação** | Verifica a **identidade** do usuário | Login com email e senha |
| **Autorização** | Define **o que** o usuário pode fazer | Usuário ADMIN pode deletar produtos |

### OAuth2

OAuth2 é um **protocolo de autorização** que permite que aplicações obtenham acesso limitado a recursos protegidos. No nosso projeto, usamos o **Grant Type Password** (Resource Owner Password Credentials).

### JWT (JSON Web Token)

Token auto-contido que carrega informações do usuário e suas permissões. Estrutura:

```
Header.Payload.Signature
```

**Vantagens:**
- ✅ Stateless (não precisa armazenar sessão no servidor)
- ✅ Escalável
- ✅ Seguro (assinado com RSA)
- ✅ Auto-contido (contém todas as informações necessárias)

---

## Arquitetura da Implementação

### Estrutura de Pacotes

```
br.com.klsys.dscommerce/
├── config/
│   ├── AuthorizationServerConfig.java      # Configuração do OAuth2 Authorization Server
│   ├── ResourceServerConfig.java           # Configuração de proteção das APIs
│   └── customgrant/
│       ├── CustomPasswordAuthenticationConverter.java
│       ├── CustomPasswordAuthenticationProvider.java
│       ├── CustomPasswordAuthenticationToken.java
│       └── CustomUserAuthorities.java
├── entities/
│   ├── User.java                            # Implementa UserDetails
│   └── Role.java                            # Implementa GrantedAuthority
├── services/
│   └── UserService.java                     # Implementa UserDetailsService
└── controllers/
    └── ProductController.java               # APIs protegidas com @PreAuthorize
```

### Fluxo de Filter Chains

O Spring Security utiliza **múltiplas filter chains** com diferentes prioridades:

| Order | Filter Chain | Descrição | Matcher |
|-------|-------------|-----------|---------|
| `@Order(1)` | `h2SecurityFilterChain` | Console H2 (apenas profile test) | `/h2-console/**` |
| `@Order(2)` | `asSecurityFilterChain` | Endpoints OAuth2 | `/oauth2/**`, `/login/**` |
| `@Order(3)` | `rsSecurityFilterChain` | APIs REST protegidas | `/**` (demais rotas) |

---

## Authorization Server

### Responsabilidades

1. ✅ Autenticar usuários (validar credenciais)
2. ✅ Gerar tokens JWT
3. ✅ Assinar tokens com chave RSA privada
4. ✅ Gerenciar clientes OAuth2

### Configuração Principal

**Arquivo:** `AuthorizationServerConfig.java`

```java
@Configuration
public class AuthorizationServerConfig {
    
    @Bean
    @Order(2)
    public SecurityFilterChain asSecurityFilterChain(HttpSecurity http) throws Exception {
        // Aplica configuração padrão do Authorization Server
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
        
        // Configura o endpoint de token com custom grant type
        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
            .tokenEndpoint(tokenEndpoint -> tokenEndpoint
                .accessTokenRequestConverter(new CustomPasswordAuthenticationConverter())
                .authenticationProvider(new CustomPasswordAuthenticationProvider(...)));
        
        // Habilita validação de JWT
        http.oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));
        
        return http.build();
    }
}
```

### Componentes Principais

#### 1. **RegisteredClient** - Cliente OAuth2

```java
@Bean
public RegisteredClientRepository registeredClientRepository() {
    RegisteredClient registeredClient = RegisteredClient
        .withId(UUID.randomUUID().toString())
        .clientId(clientId)                    // Ex: "myclientid"
        .clientSecret(passwordEncoder().encode(clientSecret))  // Ex: "myclientsecret"
        .scope("read")
        .scope("write")
        .authorizationGrantType(new AuthorizationGrantType("password"))
        .tokenSettings(tokenSettings())
        .build();
    
    return new InMemoryRegisteredClientRepository(registeredClient);
}
```

#### 2. **Token Generator** - Gerador de JWT

```java
@Bean
public OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator() {
    NimbusJwtEncoder jwtEncoder = new NimbusJwtEncoder(jwkSource());
    JwtGenerator jwtGenerator = new JwtGenerator(jwtEncoder);
    jwtGenerator.setJwtCustomizer(tokenCustomizer());  // Adiciona claims customizadas
    OAuth2AccessTokenGenerator accessTokenGenerator = new OAuth2AccessTokenGenerator();
    return new DelegatingOAuth2TokenGenerator(jwtGenerator, accessTokenGenerator);
}
```

#### 3. **Token Customizer** - Adiciona Claims ao JWT

```java
@Bean
public OAuth2TokenCustomizer<JwtEncodingContext> tokenCustomizer() {
    return context -> {
        OAuth2ClientAuthenticationToken principal = context.getPrincipal();
        CustomUserAuthorities user = (CustomUserAuthorities) principal.getDetails();
        List<String> authorities = user.getAuthorities().stream()
            .map(x -> x.getAuthority())
            .toList();
        
        if (context.getTokenType().getValue().equals("access_token")) {
            context.getClaims()
                .claim("authorities", authorities)      // Adiciona roles ao token
                .claim("username", user.getUsername()); // Adiciona username ao token
        }
    };
}
```

**Exemplo de JWT gerado:**

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
  "iat": 1708623847,
  "jti": "a1b2c3d4-e5f6-7890-abcd-ef1234567890"
}
```

#### 4. **JWK Source** - Chaves RSA para assinatura

```java
@Bean
public JWKSource<SecurityContext> jwkSource() {
    RSAKey rsaKey = generateRsa();
    JWKSet jwkSet = new JWKSet(rsaKey);
    return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
}

private static RSAKey generateRsa() {
    KeyPair keyPair = generateRsaKey();
    RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
    RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
    return new RSAKey.Builder(publicKey)
        .privateKey(privateKey)
        .keyID(UUID.randomUUID().toString())
        .build();
}

private static KeyPair generateRsaKey() {
    KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
    keyPairGenerator.initialize(2048);  // Chave de 2048 bits
    return keyPairGenerator.generateKeyPair();
}
```

#### 5. **Password Encoder** - Criptografia BCrypt

```java
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();  // Algoritmo BCrypt
}
```

---

## Resource Server

### Responsabilidades

1. ✅ Validar tokens JWT nas requisições
2. ✅ Extrair authorities (roles) do token
3. ✅ Aplicar regras de autorização nos endpoints
4. ✅ Configurar CORS

### Configuração Principal

**Arquivo:** `ResourceServerConfig.java`

```java
@Configuration
@EnableWebSecurity
@EnableMethodSecurity  // Habilita @PreAuthorize
public class ResourceServerConfig {
    
    @Bean
    @Order(3)
    public SecurityFilterChain rsSecurityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable());  // Desabilita CSRF (API REST stateless)
        
        // Permite todas as requisições (controle por @PreAuthorize)
        http.authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll());
        
        // Valida JWT em todas as requisições
        http.oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));
        
        // Configura CORS
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()));
        
        return http.build();
    }
}
```

### JWT Authentication Converter

Extrai as **authorities** do token JWT:

```java
@Bean
public JwtAuthenticationConverter jwtAuthenticationConverter() {
    JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = 
        new JwtGrantedAuthoritiesConverter();
    
    grantedAuthoritiesConverter.setAuthoritiesClaimName("authorities");
    grantedAuthoritiesConverter.setAuthorityPrefix("");  // Sem prefixo
    
    JwtAuthenticationConverter jwtAuthenticationConverter = 
        new JwtAuthenticationConverter();
    jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(
        grantedAuthoritiesConverter
    );
    
    return jwtAuthenticationConverter;
}
```

### H2 Console (Profile Test)

Para desenvolvimento, permite acesso ao console H2:

```java
@Bean
@Profile("test")
@Order(1)
public SecurityFilterChain h2SecurityFilterChain(HttpSecurity http) throws Exception {
    http.securityMatcher(PathRequest.toH2Console())
        .csrf(csrf -> csrf.disable())
        .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()));
    return http.build();
}
```

---

## Custom Grant Type (Password)

### Por que Custom Grant?

O Spring Authorization Server **não implementa** o grant type `password` por padrão (deprecated no OAuth2.1). Criamos uma implementação customizada para permitir login com **username/password**.

### Componentes do Custom Grant

#### 1. **CustomPasswordAuthenticationToken**

Representa a requisição de autenticação com username/password.

```java
public class CustomPasswordAuthenticationToken 
    extends OAuth2AuthorizationGrantAuthenticationToken {
    
    private final String username;
    private final String password;
    private final Set<String> scopes;
    
    public CustomPasswordAuthenticationToken(
        Authentication clientPrincipal,
        Set<String> scopes,
        Map<String, Object> additionalParameters
    ) {
        super(new AuthorizationGrantType("password"), clientPrincipal, additionalParameters);
        this.username = (String) additionalParameters.get("username");
        this.password = (String) additionalParameters.get("password");
        this.scopes = scopes != null ? new HashSet<>(scopes) : Collections.emptySet();
    }
}
```

#### 2. **CustomPasswordAuthenticationConverter**

Converte a requisição HTTP em `CustomPasswordAuthenticationToken`.

```java
public class CustomPasswordAuthenticationConverter 
    implements AuthenticationConverter {
    
    @Override
    public Authentication convert(HttpServletRequest request) {
        String grantType = request.getParameter(OAuth2ParameterNames.GRANT_TYPE);
        
        // Só processa se for grant_type=password
        if (!"password".equals(grantType)) {
            return null;
        }
        
        // Extrai username e password da requisição
        String username = request.getParameter(OAuth2ParameterNames.USERNAME);
        String password = request.getParameter(OAuth2ParameterNames.PASSWORD);
        
        // Valida parâmetros obrigatórios
        if (!StringUtils.hasText(username) || !StringUtils.hasText(password)) {
            throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_REQUEST);
        }
        
        // Cria o token customizado
        return new CustomPasswordAuthenticationToken(
            clientPrincipal, 
            requestedScopes, 
            additionalParameters
        );
    }
}
```

#### 3. **CustomPasswordAuthenticationProvider**

Valida as credenciais e gera o token JWT.

```java
public class CustomPasswordAuthenticationProvider implements AuthenticationProvider {
    
    private final OAuth2AuthorizationService authorizationService;
    private final UserDetailsService userDetailsService;
    private final OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator;
    private final PasswordEncoder passwordEncoder;
    
    @Override
    public Authentication authenticate(Authentication authentication) 
        throws AuthenticationException {
        
        CustomPasswordAuthenticationToken customAuth = 
            (CustomPasswordAuthenticationToken) authentication;
        
        String username = customAuth.getUsername();
        String password = customAuth.getPassword();
        
        // 1. Carrega usuário do banco
        UserDetails user = userDetailsService.loadUserByUsername(username);
        
        // 2. Valida senha
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new OAuth2AuthenticationException("Invalid credentials");
        }
        
        // 3. Extrai authorities do usuário
        Set<String> authorizedScopes = user.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toSet());
        
        // 4. Cria contexto de segurança com as authorities
        CustomUserAuthorities customUser = new CustomUserAuthorities(
            username, 
            user.getAuthorities()
        );
        OAuth2ClientAuthenticationToken clientAuth = 
            (OAuth2ClientAuthenticationToken) SecurityContextHolder
                .getContext()
                .getAuthentication();
        clientAuth.setDetails(customUser);
        
        // 5. Gera o Access Token JWT
        OAuth2TokenContext tokenContext = DefaultOAuth2TokenContext.builder()
            .registeredClient(registeredClient)
            .principal(clientPrincipal)
            .authorizedScopes(authorizedScopes)
            .authorizationGrantType(new AuthorizationGrantType("password"))
            .build();
        
        OAuth2Token generatedAccessToken = tokenGenerator.generate(tokenContext);
        
        OAuth2AccessToken accessToken = new OAuth2AccessToken(
            OAuth2AccessToken.TokenType.BEARER,
            generatedAccessToken.getTokenValue(),
            generatedAccessToken.getIssuedAt(),
            generatedAccessToken.getExpiresAt(),
            authorizedScopes
        );
        
        // 6. Salva autorização
        OAuth2Authorization authorization = authorizationBuilder.build();
        authorizationService.save(authorization);
        
        // 7. Retorna token
        return new OAuth2AccessTokenAuthenticationToken(
            registeredClient, 
            clientPrincipal, 
            accessToken
        );
    }
    
    @Override
    public boolean supports(Class<?> authentication) {
        return CustomPasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
```

#### 4. **CustomUserAuthorities**

Wrapper para transportar username e authorities.

```java
public class CustomUserAuthorities {
    private String username;
    private Collection<? extends GrantedAuthority> authorities;
    
    public CustomUserAuthorities(
        String username, 
        Collection<? extends GrantedAuthority> authorities
    ) {
        this.username = username;
        this.authorities = authorities;
    }
    
    // getters...
}
```

---

## JWT (JSON Web Token)

### Estrutura do Token

Um JWT é composto por 3 partes separadas por `.`:

```
eyJhbGc...header.eyJzdWI...payload.SflKxwR...signature
```

#### 1. **Header** (Cabeçalho)

```json
{
  "alg": "RS256",
  "typ": "JWT",
  "kid": "a1b2c3d4-e5f6-7890-abcd-ef1234567890"
}
```

- `alg`: Algoritmo de assinatura (RSA SHA-256)
- `typ`: Tipo do token
- `kid`: ID da chave pública usada

#### 2. **Payload** (Dados)

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
  "iat": 1708623847,
  "jti": "unique-token-id"
}
```

**Claims padrão:**
- `sub`: Subject (client ID)
- `aud`: Audience (para quem o token é destinado)
- `iss`: Issuer (quem emitiu o token)
- `exp`: Expiration time (Unix timestamp)
- `iat`: Issued at (Unix timestamp)
- `nbf`: Not before (Unix timestamp)
- `jti`: JWT ID (identificador único)

**Claims customizadas:**
- `authorities`: Roles do usuário (ex: `["ROLE_ADMIN"]`)
- `username`: Email do usuário
- `scope`: Escopos OAuth2

#### 3. **Signature** (Assinatura)

```
RSASHA256(
  base64UrlEncode(header) + "." + base64UrlEncode(payload),
  privateKey
)
```

Garante que o token:
- ✅ **Não foi alterado** (integridade)
- ✅ **Foi emitido por este servidor** (autenticidade)

### Tempo de Validade

Configurado em `application.properties`:

```properties
security.jwt.duration=86400  # 24 horas em segundos
```

### Validação do Token

O Resource Server valida automaticamente:

1. ✅ **Assinatura** (usando chave pública RSA)
2. ✅ **Expiração** (`exp` claim)
3. ✅ **Issuer** (emissor do token)
4. ✅ **Audience** (destinatário)

---

## CORS (Cross-Origin Resource Sharing)

### Problema

Por padrão, navegadores bloqueiam requisições JavaScript de **origens diferentes** (domínios, portas ou protocolos diferentes).

**Exemplo:**
- Frontend: `http://localhost:3000` (React/Next.js)
- Backend: `http://localhost:8080` (Spring Boot)
- ❌ **Bloqueado por CORS!**

### Solução

Configuração no `ResourceServerConfig`:

```java
@Bean
CorsConfigurationSource corsConfigurationSource() {
    String[] origins = corsOrigins.split(",");  // "http://localhost:3000,http://localhost:5173"
    
    CorsConfiguration corsConfig = new CorsConfiguration();
    corsConfig.setAllowedOriginPatterns(Arrays.asList(origins));
    corsConfig.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "DELETE", "PATCH"));
    corsConfig.setAllowCredentials(true);  // Permite envio de cookies
    corsConfig.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
    
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", corsConfig);
    return source;
}

@Bean
FilterRegistrationBean<CorsFilter> customCorsFilter() {
    FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(
        new CorsFilter(corsConfigurationSource())
    );
    bean.setOrder(Ordered.HIGHEST_PRECEDENCE);  // Máxima prioridade
    return bean;
}
```

### Configuração

**application.properties:**

```properties
cors.origins=http://localhost:3000,http://localhost:5173
```

**Variável de ambiente:**

```bash
export CORS_ORIGINS=http://localhost:3000,https://meuapp.com
```

---

## Fluxo de Autenticação

### 1️⃣ **Login - Obter Token**

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
```
Basic base64(clientId:clientSecret)
Basic base64(myclientid:myclientsecret)
```

**Resposta (200 OK):**

```json
{
  "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...",
  "token_type": "Bearer",
  "expires_in": 86400,
  "scope": "read write"
}
```

### 2️⃣ **Acessar API Protegida**

**Requisição:**

```http
GET http://localhost:8080/products/1
Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...
```

**Fluxo interno:**

1. `CorsFilter` processa CORS
2. `OAuth2ResourceServerFilter` valida o JWT
3. `JwtAuthenticationConverter` extrai authorities do token
4. `@PreAuthorize("hasRole('ROLE_ADMIN')")` verifica permissão
5. Se autorizado, executa o método do controller

**Resposta (200 OK):**

```json
{
  "id": 1,
  "name": "The Lord of the Rings",
  "price": 90.5,
  "description": "Lorem ipsum..."
}
```

### 3️⃣ **Fluxo Completo - Diagrama**

```
┌─────────────┐                                    ┌──────────────────┐
│   Cliente   │                                    │ Authorization    │
│  (Frontend) │                                    │     Server       │
└──────┬──────┘                                    └────────┬─────────┘
       │                                                    │
       │  1. POST /oauth2/token                             │
       │     grant_type=password                            │
       │     username=maria@gmail.com                       │
       │     password=123456                                │
       ├───────────────────────────────────────────────────>│
       │                                                    │
       │                       2. UserService               │
       │                          loadUserByUsername()      │
       │                          ┌────────────────────┐    │
       │                          │   UserRepository   │    │
       │                          │  (Banco de Dados)  │    │ 
       │                          └────────────────────┘    │
       │                                                    │
       │                       3. PasswordEncoder           │
       │                          matches(password)         │
       │                                                    │
       │                       4. Gera JWT com RSA          │
       │                          + Claims customizadas     │
       │                                                    │
       │  5. Retorna access_token                           │
       │<───────────────────────────────────────────────────┤
       │     { "access_token": "eyJ...", ... }              │
       │                                                    │
       │                                                    │
┌──────┴──────┐                                    ┌────────┴──────────┐
│   Cliente   │                                    │  Resource Server  │
│  (Frontend) │                                    │   (API REST)      │
└──────┬──────┘                                    └────────┬──────────┘
       │                                                    │
       │  6. GET /products/1                                │
       │     Authorization: Bearer eyJ...                   │
       ├───────────────────────────────────────────────────>│
       │                                                    │
       │                       7. Valida JWT                │
       │                          - Assinatura RSA          │
       │                          - Expiração               │
       │                          - Issuer/Audience         │
       │                                                    │
       │                       8. Extrai authorities        │
       │                          ["ROLE_ADMIN"]            │
       │                                                    │
       │                       9. @PreAuthorize             │
       │                          hasRole('ROLE_ADMIN')     │
       │                          ✅ Autorizado             │
       │                                                    │
       │  10. Retorna dados                                 │
       │<───────────────────────────────────────────────────┤
       │      { "id": 1, "name": "Product", ... }           │
       │                                                    │
```

---

## Configurações

### application.properties

```properties
# Profile ativo (test, dev, prod)
spring.profiles.active=${APP_PROFILE:test}

# OAuth2 - Client Credentials
security.client-id=${CLIENT_ID:myclientid}
security.client-secret=${CLIENT_SECRET:myclientsecret}

# JWT - Tempo de expiração em segundos (86400 = 24h)
security.jwt.duration=${JWT_DURATION:86400}

# CORS - Origens permitidas (separadas por vírgula)
cors.origins=${CORS_ORIGINS:http://localhost:3000,http://localhost:5173}

# JPA
spring.jpa.open-in-view=false
```

### Variáveis de Ambiente

Para produção, configure via variáveis de ambiente:

```bash
# Linux/Mac
export APP_PROFILE=prod
export CLIENT_ID=my-production-client
export CLIENT_SECRET=super-secret-key-production
export JWT_DURATION=3600
export CORS_ORIGINS=https://meuapp.com,https://app.meuapp.com

# Windows (PowerShell)
$env:APP_PROFILE="prod"
$env:CLIENT_ID="my-production-client"
$env:CLIENT_SECRET="super-secret-key-production"
$env:JWT_DURATION="3600"
$env:CORS_ORIGINS="https://meuapp.com,https://app.meuapp.com"
```

---

## Como Usar

### 1. Obter Token de Acesso

**cURL:**

```bash
curl -X POST http://localhost:8080/oauth2/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -H "Authorization: Basic bXljbGllbnRpZDpteWNsaWVudHNlY3JldA==" \
  -d "grant_type=password" \
  -d "username=maria@gmail.com" \
  -d "password=123456"
```

**JavaScript (Fetch API):**

```javascript
const clientId = 'myclientid';
const clientSecret = 'myclientsecret';
const credentials = btoa(`${clientId}:${clientSecret}`);

const response = await fetch('http://localhost:8080/oauth2/token', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/x-www-form-urlencoded',
    'Authorization': `Basic ${credentials}`
  },
  body: new URLSearchParams({
    grant_type: 'password',
    username: 'maria@gmail.com',
    password: '123456'
  })
});

const data = await response.json();
const accessToken = data.access_token;
```

**Axios:**

```javascript
import axios from 'axios';

const clientId = 'myclientid';
const clientSecret = 'myclientsecret';
const credentials = btoa(`${clientId}:${clientSecret}`);

const response = await axios.post(
  'http://localhost:8080/oauth2/token',
  new URLSearchParams({
    grant_type: 'password',
    username: 'maria@gmail.com',
    password: '123456'
  }),
  {
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded',
      'Authorization': `Basic ${credentials}`
    }
  }
);

const accessToken = response.data.access_token;
```

### 2. Usar Token nas Requisições

**cURL:**

```bash
curl -X GET http://localhost:8080/products \
  -H "Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9..."
```

**JavaScript (Fetch):**

```javascript
const response = await fetch('http://localhost:8080/products', {
  headers: {
    'Authorization': `Bearer ${accessToken}`
  }
});

const products = await response.json();
```

**Axios com Interceptor:**

```javascript
import axios from 'axios';

// Configurar interceptor para adicionar token automaticamente
axios.interceptors.request.use(config => {
  const token = localStorage.getItem('access_token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// Usar normalmente
const response = await axios.get('http://localhost:8080/products');
```

### 3. Controle de Acesso nos Controllers

**Permitir todos:**

```java
@GetMapping("/{id}")
public ResponseEntity<ProductDTO> findById(@PathVariable Long id) {
    ProductDTO dto = service.findById(id);
    return ResponseEntity.ok(dto);
}
```

**Apenas usuários autenticados:**

```java
@PreAuthorize("isAuthenticated()")
@GetMapping
public ResponseEntity<Page<ProductDTO>> findAll(Pageable pageable) {
    Page<ProductDTO> dto = service.findAll(pageable);
    return ResponseEntity.ok().body(dto);
}
```

**Apenas ADMIN:**

```java
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
```

**ADMIN ou OPERATOR:**

```java
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

**Expressões complexas:**

```java
@PreAuthorize("hasRole('ROLE_ADMIN') or (#id == principal.username)")
@DeleteMapping("/{id}")
public ResponseEntity<Void> delete(@PathVariable Long id) {
    service.delete(id);
    return ResponseEntity.noContent().build();
}
```

### 4. Roles Disponíveis

No banco de dados (`import.sql`):

```sql
INSERT INTO tb_role (authority) VALUES ('ROLE_OPERATOR');
INSERT INTO tb_role (authority) VALUES ('ROLE_ADMIN');

-- Alex: apenas OPERATOR
INSERT INTO tb_user_role (user_id, role_id) VALUES (1, 1);

-- Maria: OPERATOR + ADMIN
INSERT INTO tb_user_role (user_id, role_id) VALUES (2, 1);
INSERT INTO tb_user_role (user_id, role_id) VALUES (2, 2);
```

---

## Troubleshooting

### ❌ Erro: "Invalid credentials"

**Causa:** Senha incorreta ou usuário não existe.

**Solução:**
1. Verifique se o usuário existe no banco de dados
2. Verifique se a senha está criptografada com BCrypt
3. Teste com usuários do `import.sql`:
   - `alex@gmail.com` / `123456`
   - `maria@gmail.com` / `123456`

### ❌ Erro: "Invalid client"

**Causa:** Client ID ou Client Secret incorretos.

**Solução:**
Verifique o header `Authorization` na requisição:

```bash
# Correto
Authorization: Basic bXljbGllbnRpZDpteWNsaWVudHNlY3JldA==

# Gerar Base64
echo -n "myclientid:myclientsecret" | base64
```

### ❌ Erro: "Access Denied" ou 403 Forbidden

**Causa:** Usuário não tem a role necessária.

**Solução:**
1. Verifique o JWT (use https://jwt.io):
   ```json
   {
     "authorities": ["ROLE_OPERATOR"]  // Não tem ROLE_ADMIN
   }
   ```
2. Adicione a role ao usuário no banco de dados
3. Gere um novo token após alterar as roles

### ❌ Erro: "Token expired"

**Causa:** Token JWT expirou.

**Solução:**
1. Obtenha um novo token (faça login novamente)
2. Implemente refresh token (futuro)
3. Aumente `security.jwt.duration` (não recomendado para produção)

### ❌ Erro CORS

**Causa:** Origem não permitida.

**Solução:**
1. Adicione a origem no `application.properties`:
   ```properties
   cors.origins=http://localhost:3000,https://meuapp.com
   ```
2. Reinicie a aplicação
3. Verifique se o `CorsFilter` está configurado

### ❌ Erro: "Filter chain conflict"

**Causa:** Múltiplos filter chains sem `@Order` ou `securityMatcher`.

**Solução:**
```java
@Bean
@Order(2)  // IMPORTANTE!
public SecurityFilterChain asSecurityFilterChain(HttpSecurity http) {
    OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
    // ...
}

@Bean
@Order(3)  // Deve ser DEPOIS do Authorization Server
public SecurityFilterChain rsSecurityFilterChain(HttpSecurity http) {
    // ...
}
```

---

## Referências

- [Spring Security Reference](https://docs.spring.io/spring-security/reference/index.html)
- [Spring Authorization Server](https://spring.io/projects/spring-authorization-server)
- [OAuth 2.0 RFC 6749](https://datatracker.ietf.org/doc/html/rfc6749)
- [JWT.io - Debugger](https://jwt.io/)
- [BCrypt Calculator](https://bcrypt-generator.com/)

---

## Conclusão

Este projeto implementa um sistema robusto de autenticação e autorização utilizando as melhores práticas:

✅ **OAuth2** com Authorization Server separado  
✅ **JWT** stateless e escalável  
✅ **BCrypt** para senhas  
✅ **RSA 2048** para assinatura de tokens  
✅ **CORS** configurável  
✅ **Roles** granulares com `@PreAuthorize`  
✅ **Custom Grant Type** (password)  

**Use este documento como referência para implementar autenticação em outros projetos Spring Boot!** 🚀

---

**Desenvolvido para fins de consulta - Flávio Antonio Demétrio**

