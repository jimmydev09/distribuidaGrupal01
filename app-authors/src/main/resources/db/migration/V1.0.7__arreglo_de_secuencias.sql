-- Actualizar secuencia de purchase_orders
SELECT setval('purchase_orders_id_seq', COALESCE((SELECT MAX(id) FROM purchase_orders), 1));

-- Actualizar secuencia de customers
SELECT setval('customers_id_seq', COALESCE((SELECT MAX(id) FROM customers), 1));