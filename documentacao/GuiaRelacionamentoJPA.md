## 📚 Guia de Relacionamentos JPA - Referência Rápida

### ✅ 1. One-to-One (1:1)
**Use quando cada entidade só pode ter uma do outro lado.**

**Exemplos:**
- Pessoa → CPF
- Usuário → PerfilDetalhes
- Cliente → EndereçoPrincipal (um único)

📌 **Se puder ter mais de um → não é 1:1, é 1:N.**

---

### ✅ 2. One-to-Many / Many-to-One (1:N)
**É o mais comum. Um lado tem vários; o outro tem apenas um.**

**Exemplos:**
- Cliente → Endereços
- Pedido → Itens
- Departamento → Funcionários

📌 **A FK (Foreign Key) sempre fica no lado Many.**

---

### ✅ 3. Many-to-Many (N:N) simples
**Use somente quando a tabela intermediária não tem nenhuma coluna extra.**

**Exemplos:**
- Usuário ↔ Permissão
- Aluno ↔ Curso (sem notas, sem frequência)

📌 **Se for só ligação, pode usar @ManyToMany.**

---

### ❌ 4. Many-to-Many real (com dados extras) → classe de associação
**Quando o relacionamento precisa de atributos, NÃO é N:N técnico.**

**Exemplos reais:**
- **Pedido ↔ Produto** → precisa de quantidade, valorUnitário, subtotal
- **Aluno ↔ Disciplina** → precisa de nota, presença
- **Funcionário ↔ Projeto** → precisa de horasTrabalhadas

📌 **Cria-se uma entidade própria com duas FKs:**
- `@ManyToOne pedido`
- `@ManyToOne produto`

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
