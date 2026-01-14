DO $$
DECLARE
v_sujeto_id UUID;
v_predio_id UUID;
BEGIN

-- Insertar Sujeto Pasivo (Juan Pérez)
INSERT INTO sujetos_pasivos (
    id,
    tipo_persona,
    rfc,
    nombre_razon_social,
    apellido_paterno,
    apellido_materno,
    estatus,
    created_at
)
VALUES (
        gen_random_uuid(),
        'FISICA',
        'PEPJ800101XXX',
        'Juan',
        'Pérez',
        'López',
        'ACTIVO',
        NOW())
RETURNING id INTO v_sujeto_id;

-- Insertar Sujeto Pasivo (Hugo Cobos)
INSERT INTO sujetos_pasivos (
    id,
    tipo_persona,
    rfc,
    nombre_razon_social,
    apellido_paterno,
    apellido_materno,
    estatus,
    created_at
)
VALUES (
           'a1616db5-255d-4c2a-8eec-d2b490c6265a',
           'FISICA',
           'PEXJ800101XXX',
           'Hugo',
           'Cobos',
           'Bravo',
           'ACTIVO',
           NOW());

-- Insertar Predio Urbano (Casa de Juan)
INSERT INTO predios (
id, clave_catastral, tipo_predio, valor_catastral, area_terreno_m2,
calle, numero_exterior, colonia_barrio, codigo_postal, estatus, created_at, ultimo_anio_pagado
) VALUES (
gen_random_uuid(),
'U-100-200-300',
'URBANO',
1500000.00, -- Valor Catastral: $1.5 Millones
250.00,     -- Terreno: 250 m2
'Av. Independencia', '123', 'Centro', '68300',
'ACTIVO', NOW(), 2023
) RETURNING id INTO v_predio_id;

INSERT INTO predios (id, clave_catastral, tipo_predio, valor_catastral, area_terreno_m2,
                     calle, numero_exterior, colonia_barrio, codigo_postal, estatus, created_at, ultimo_anio_pagado)
VALUES ('a1616db5-255d-4c2a-8eec-d2b490c6265b',
        '001-TEST',
        'URBANO',
        1500000.00, -- Valor Catastral: $1.5 Millones
        250.00,     -- Terreno: 250 m2
        'Av. Independencia', '123', 'Centro', '68300',
        'ACTIVO', NOW(), 2024);

-- Relacionar Predio con Sujeto (Juan es dueño)
INSERT INTO propiedad_predios (id, sujeto_id, predio_id, tipo_relacion, porcentaje_propiedad, es_responsable_pago)
VALUES (gen_random_uuid(), v_sujeto_id, v_predio_id, 'PROPIETARIO', 100.00, true);

-- Insertar Licencia Comercial (Minisuper de Juan)
INSERT INTO licencias_comerciales (
id, numero_licencia, sujeto_id, predio_id, nombre_comercial,
giro_clave, metros_cuadrados, horario_funcionamiento, estado_licencia, created_at
) VALUES (
gen_random_uuid(),
'LIC-2025-001',
v_sujeto_id,
v_predio_id,
'Minisuper El Rápido',
'MINISUPER', -- Coincide con el JSON de Tarifas
50.00,
'08:00 - 22:00',
'ACTIVA',
NOW()
);

END $$;

-- Catálogo de Giros (Ejemplos basados en Ley de Ingresos típica)
INSERT INTO cat_giros_comerciales
(
 clave,
 descripcion,
 requiere_licencia_alcohol,
 activo
) VALUES
('ABARROTES', 'Tienda de Abarrotes sin venta de alcohol', false, true),
('MINISUPER', 'Minisuper con venta de cerveza y licores', true, true),
('RESTAURANTE', 'Restaurante con venta de alimentos', false, true),
('RESTAURANTE_BAR', 'Restaurante con venta de bebidas alcohólicas', true, true),
('FARMACIA', 'Farmacia y venta de artículos de higiene', false, true),
('TALLER_MECANICO', 'Taller mecánico y refacciones', false, true),
('PAPELERIA', 'Papelería y regalos', false, true);