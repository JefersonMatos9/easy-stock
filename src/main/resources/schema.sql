CREATE TABLE categorias_produtos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(50) NOT NULL
);

-- Inserindo os valores da enumeração no banco de dados
INSERT INTO categorias_produtos (nome) VALUES
('Salgados'),
('Lanches'),
('Bebidas'),
('Sobremesas'),
('Doces');


CREATE TABLE products (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    quantity INT NOT NULL,
    available BOOLEAN NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    category INT NOT NULL,
    FOREIGN KEY (category) REFERENCES categorias_produtos(id)
);


