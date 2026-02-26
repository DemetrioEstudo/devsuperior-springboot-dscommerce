-- 1. USUÁRIOS (Certifique-se de preencher todos os campos obrigatórios)
INSERT INTO tb_user (name, email, password, phone, birth_date) VALUES ('Maria', 'maria@gmail.com', '$2y$10$EL1OjaRlHrsTg0C5VpO9Levfu6cc6vQewysJsz9txC7Mn1SXDHdCW', '88888888', '1992-05-15');
INSERT INTO tb_user (name, email, password, phone, birth_date) VALUES ('Alex', 'alex@gmail.com', '$2y$10$EL1OjaRlHrsTg0C5VpO9Levfu6cc6vQewysJsz9txC7Mn1SXDHdCW', '99999999', '1990-07-25');


-- 2. ROLES
INSERT INTO tb_role (authority) VALUES ('ROLE_CLIENT');
INSERT INTO tb_role (authority) VALUES ('ROLE_ADMIN');

-- 3. ASSOCIAÇÃO USUÁRIO-ROLE (User_id 1 e 2 agora existem)
INSERT INTO tb_user_role (user_id, role_id) VALUES (1, 1);
INSERT INTO tb_user_role (user_id, role_id) VALUES (2, 1);
INSERT INTO tb_user_role (user_id, role_id) VALUES (2, 2);

-- 4. CATEGORIAS
INSERT INTO tb_category(name) VALUES ('Livros');
INSERT INTO tb_category(name) VALUES ('Eletrônicos');
INSERT INTO tb_category(name) VALUES ('Computadores');

-- 5. PRODUTOS (Removi as inserções duplicadas de 'TV' e 'Computer' para manter os IDs corretos)
INSERT INTO tb_product (name, price, description, img_url) VALUES ('The Lord of the Rings', 90.5, 'Lorem ipsum dolor sit amet...', 'https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/1-big.jpg');
INSERT INTO tb_product (name, price, description, img_url) VALUES ('Smart TV', 2190.0, 'Lorem ipsum dolor sit amet...', 'https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/2-big.jpg');
INSERT INTO tb_product (name, price, description, img_url) VALUES ('Macbook Pro', 1250.0, 'Lorem ipsum dolor sit amet...', 'https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/3-big.jpg');
INSERT INTO tb_product (name, price, description, img_url) VALUES ('PC Gamer', 1200.0, 'Lorem ipsum dolor sit amet...', 'https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/4-big.jpg');
INSERT INTO tb_product (name, price, description, img_url) VALUES ('Rails for Dummies', 100.99, 'Lorem ipsum dolor sit amet...', 'https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/5-big.jpg');
INSERT INTO tb_product (name, price, description, img_url) VALUES ('PC Gamer Ex', 1350.0, 'Lorem ipsum...', 'https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/6-big.jpg');
-- ... (Adicione os outros produtos aqui se necessário, seguindo a numeração)

-- 6. ASSOCIAÇÃO PRODUTO-CATEGORIA (IDs de produto batendo com a sequência acima)
INSERT INTO tb_product_category (product_id, category_id) VALUES (1, 1);
INSERT INTO tb_product_category (product_id, category_id) VALUES (2, 2);
INSERT INTO tb_product_category (product_id, category_id) VALUES (3, 3);
INSERT INTO tb_product_category (product_id, category_id) VALUES (4, 3);
INSERT INTO tb_product_category (product_id, category_id) VALUES (5, 1);

-- 7. PEDIDOS (Client_id 1 e 2 são Alex e Maria)
INSERT INTO tb_order (moment, status, client_id) VALUES (TIMESTAMP WITH TIME ZONE '2022-07-25T13:00:00Z', 1, 1);
INSERT INTO tb_order (moment, status, client_id) VALUES (TIMESTAMP WITH TIME ZONE '2022-07-29T15:50:00Z', 3, 2);
INSERT INTO tb_order (moment, status, client_id) VALUES (TIMESTAMP WITH TIME ZONE '2022-08-03T14:20:00Z', 0, 1);

-- 8. ITENS DE PEDIDO (Order_id 1, 2 e 3 agora existem)
INSERT INTO tb_order_item (order_id, product_id, quantity, price) VALUES (1, 1, 2, 90.5);
INSERT INTO tb_order_item (order_id, product_id, quantity, price) VALUES (1, 3, 1, 1250.0);
INSERT INTO tb_order_item (order_id, product_id, quantity, price) VALUES (2, 3, 1, 1250.0);
INSERT INTO tb_order_item (order_id, product_id, quantity, price) VALUES (3, 1, 1, 90.5);

-- 9. PAGAMENTOS
INSERT INTO tb_payment (order_id, moment) VALUES (1, TIMESTAMP WITH TIME ZONE '2022-07-25T15:00:00Z');
INSERT INTO tb_payment (order_id, moment) VALUES (2, TIMESTAMP WITH TIME ZONE '2022-07-30T11:00:00Z');