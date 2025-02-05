INSERT INTO products (name, quantity, available, price, category) VALUES 
('Coxinha', 10, TRUE, 2999.99, (SELECT id FROM categorias_produtos WHERE nome = 'Salgados'));
INSERT INTO products (name, quantity, available, price, category) VALUES 
('Pastel', 10, TRUE, 2999.99, (SELECT id FROM categorias_produtos WHERE nome = 'Salgados'));
INSERT INTO products (name, quantity, available, price, category) VALUES 
('Coca_cola', 10, TRUE, 2999.99, (SELECT id FROM categorias_produtos WHERE nome = 'Bebidas'));
INSERT INTO products (name, quantity, available, price, category) VALUES 
('Fanta', 10, TRUE, 2999.99, (SELECT id FROM categorias_produtos WHERE nome = 'Bebidas'));
INSERT INTO products (name, quantity, available, price, category) VALUES 
('Esfiha', 10, TRUE, 2999.99, (SELECT id FROM categorias_produtos WHERE nome = 'Salgados'));
INSERT INTO products (name, quantity, available, price, category) VALUES 
('Suco Life', 10, TRUE, 2999.99, (SELECT id FROM categorias_produtos WHERE nome = 'Bebidas'));
INSERT INTO products (name, quantity, available, price, category) VALUES 
('Pastel', 10, TRUE, 2999.99, (SELECT id FROM categorias_produtos WHERE nome = 'Salgados'));