ALTER TABLE purchase_orders
DROP CONSTRAINT IF EXISTS purchase_orders_customer_id_fkey;

ALTER TABLE purchase_orders
    ADD CONSTRAINT purchase_orders_customer_id_fkey
        FOREIGN KEY (customer_id)
            REFERENCES customers(id)
            ON DELETE CASCADE;
