-- 1) Permitir NULL en customer_id
ALTER TABLE purchase_orders
    ALTER COLUMN customer_id DROP NOT NULL;

-- 2) Quitar FK vieja
ALTER TABLE purchase_orders
DROP CONSTRAINT IF EXISTS purchase_orders_customer_id_fkey;

-- 3) Añadir nueva FK con SET NULL
ALTER TABLE purchase_orders
    ADD CONSTRAINT purchase_orders_customer_id_fkey
        FOREIGN KEY (customer_id)
            REFERENCES customers(id)
            ON DELETE SET NULL;
