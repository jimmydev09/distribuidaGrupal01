-- Actualizar secuencia de line_items
SELECT setval('line_items_id_seq', COALESCE((SELECT MAX(id) FROM line_items), 1));