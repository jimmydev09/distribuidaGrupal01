-- Script para insertar varios datos en tablas

-- Insercion en tabla customers
INSERT INTO public.customers (name, email, version)
VALUES ('John Smith', 'john.smith@mail.com', 1),
       ('Maria Garcia', 'maria.garcia@mail.com', 1),
       ('David Johnson', 'david.johnson@mail.com', 1),
       ('Sarah Wilson', 'sarah.wilson@mail.com', 1);

-- Insercion en tabla inventory
INSERT INTO public.inventory (isbn, sold, supplied)
VALUES ('9780060853976', 5, 10),
       ('9780765356130', 15, 20),
       ('9780345391803', 0, 30),
       ('9780307887894', 7, 7),
       ('9781455515789', 12, 25),
       ('9780345464187', 3, 15),
       ('9780671578275', 8, 20),
       ('9780345334312', 2, 12),
       ('9780425286654', 6, 18),
       ('9780345368959', 4, 14);

-- Insercion en tabla purchase_orders
INSERT INTO public.purchase_orders (customer_id, total, status, placed_on, delivered_on)
VALUES (1, 32, 1, '2025-06-01 09:30:00', '2025-06-03 14:45:00'),
       (2, 150, 2, '2025-06-05 11:15:00', '2025-06-07 10:00:00'),
       (3, 75, 1, '2025-06-10 16:00:00', '2025-06-12 18:20:00'),
       (4, 40, 3, '2025-06-12 08:00:00', '2025-06-14 12:00:00');

-- Insercion en tabla line_items
INSERT INTO public.line_items (order_id, quantity, isbn)
VALUES (1, 2, '9780060853976'),
       (1, 1, '9780765356130'),
       (2, 5, '9780345391803'),
       (3, 3, '9780307887894'),
       (4, 4, '9781455515789'),
       (2, 2, '9780345464187'),
       (3, 1, '9780671578275'),
       (1, 1, '9780345334312'),
       (4, 2, '9780425286654'),
       (2, 1, '9780345368959');