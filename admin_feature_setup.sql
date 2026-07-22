-- ==========================================================
-- Jalankan ini di database mcd_pbo (via psql atau pgAdmin Query Tool)
-- ==========================================================

-- 1. Tabel akun staff (admin & cashier)
CREATE TABLE IF NOT EXISTS accounts (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL CHECK (role IN ('admin', 'cashier'))
);

-- 2. Tabel kategori (dikelola admin, dipakai buat isi sub_category menu)
CREATE TABLE IF NOT EXISTS categories (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL
);

-- 3. Tabel order + item order (buat ditampilkan & diupdate statusnya di admin)
CREATE TABLE IF NOT EXISTS orders (
    id SERIAL PRIMARY KEY,
    status VARCHAR(20) NOT NULL DEFAULT 'paid' CHECK (status IN ('paid', 'finished')),
    total_price NUMERIC(10, 2) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS order_items (
    id SERIAL PRIMARY KEY,
    order_id INT NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
    menu_name VARCHAR(100) NOT NULL,
    price NUMERIC(10, 2) NOT NULL,
    quantity INT NOT NULL
);

INSERT INTO accounts (username, password, role) VALUES
('admin',   '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 'admin'),
('cashier', 'b4c94003c562bb0d89535eca77f07284fe560fd48a7cc1ed99f0a56263d616ba', 'cashier')
ON CONFLICT (username) DO NOTHING;

-- contoh kategori (sesuaikan sama sub_category yang udah kamu pakai di tabel menus)
INSERT INTO categories (name) VALUES
('Burger'), ('Ayam'), ('Minuman'), ('Paket Hebat')
ON CONFLICT (name) DO NOTHING;

-- contoh order dummy biar tab Orders ga kosong pas demo
INSERT INTO orders (status, total_price) VALUES
('paid', 55000),
('finished', 32000);

INSERT INTO order_items (order_id, menu_name, price, quantity) VALUES
(1, 'Big Mac', 45000, 1),
(1, 'Coca Cola', 10000, 1),
(2, 'McSpaghetti', 32000, 1);
