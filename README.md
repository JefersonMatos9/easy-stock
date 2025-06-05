# 🚀 EasyStock: Gerenciamento de Estoque e Pedidos Simplificado

**Bem-vindo ao EasyStock!**
Uma solução completa, intuitiva e eficiente para o controle de produtos e pedidos. Construído com **Spring Boot**, o sistema automatiza o gerenciamento de inventário, desde o cadastro de produtos até a movimentação de estoque com base nos pedidos.

---

## 🧰 Tecnologias Utilizadas

### 💽 Backend

* **Java 17+**
* **Spring Boot 3.x**
* **Spring Data JPA / Hibernate**
* **Maven**
* **Lombok**
* **SLF4J / Logback**

### 📔 Banco de Dados

* **MySQL** (ou **H2** para testes/desenvolvimento)

### 🌐 Frontend (Básico)

* **HTML5**
* **CSS3**

---

## ✨ Funcionalidades Principais

### 📦 Gestão de Produtos

* CRUD completo de produtos.
* Associação de produtos a **categorias**.
* Validação de **estoque mínimo**.
* Alerta para produtos com **baixo estoque**.

### 🔄 Controle de Estoque Transacional

* Atualização automática do estoque ao criar, editar ou excluir pedidos.
* Prevenção contra vendas de produtos indisponíveis.

### 🧾 Gestão de Pedidos

* Criação de pedidos com múltiplos itens.
* Atualização e gerenciamento do **status dos pedidos**.
* **Edição inteligente**: adicione, remova ou altere itens com recalculo automático do valor total e ajuste do estoque.
* **Cancelamento de pedidos**: devolução automática dos itens ao estoque.

### 📂 Categorias de Produtos

* Cadastro e gerenciamento.
* Criação automática ao associar um produto a uma nova categoria.

### 🚨 Exceções Controladas

* Mensagens claras para erros como:

  * Produto não encontrado
  * Estoque insuficiente
  * Item inválido

---

## ⚙️ Como Rodar o Projeto

### ✅ Pré-requisitos

* JDK 17+
* Maven 3.x
* MySQL Server
* IDE com suporte a Spring Boot (IntelliJ, Eclipse, VS Code)

### 🛠️ Configuração do Banco de Dados

Crie um banco no MySQL com o nome: `easystock_db`.

No arquivo `src/main/resources/application.properties`, configure:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/easystock_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
spring.datasource.username=seu_usuario_mysql
spring.datasource.password=sua_senha_mysql
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

### ▶️ Executando o Projeto

1. Clone o repositório:

```bash
git clone https://github.com/JefersonMatos9/easy-stock.git
cd easy-stock
```

2. Construa o projeto:

```bash
mvn clean install
```

3. Rode a aplicação:

```bash
mvn spring-boot:run
```

Ou execute a classe `EasyStockApplication` pela IDE.

> Acesse via navegador: `http://localhost:8080`

---

## 🔮 Próximos Passos (Roadmap)

* 🔐 Autenticação e autorização com **Spring Security**
* 🧑‍💻 Interface web mais dinâmica (React, Angular ou Vue.js)
* 📊 Relatórios avançados (vendas, estoques, produtos mais vendidos)
* 🌍 Suporte a múltiplos idiomas (i18n)
* ✅ Testes unitários e de integração

---

## 🤝 Contribuições

Contribuições são muito bem-vindas!

1. Faça um fork do repositório
2. Crie sua branch:

   ```bash
   git checkout -b feature/minha-nova-feature
   ```
3. Commit e push:

   ```bash
   git commit -m 'feat: adiciona nova funcionalidade X'
   git push origin feature/minha-nova-feature
   ```
4. Abra um **Pull Request**

---

## 📜 Licença

Este projeto está sob a [MIT License](LICENSE).

---

## 📧 Contato

👤 **Jeferson Matos**
🔗 [LinkedIn](https://www.linkedin.com/in/jeferson-matos/)
