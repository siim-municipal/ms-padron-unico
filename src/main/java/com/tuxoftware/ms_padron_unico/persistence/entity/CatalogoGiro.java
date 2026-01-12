package com.tuxoftware.ms_padron_unico.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cat_giros_comerciales")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CatalogoGiro {

    // Usamos la clave como ID porque es el código natural (ej: "MINISUPER")
    // y es lo que compartiremos con ms-calculo.
    @Id
    @Column(name = "clave", length = 50, nullable = false)
    private String clave;

    @Column(name = "descripcion", nullable = false)
    private String descripcion;

    @Column(name = "requiere_licencia_alcohol")
    private Boolean requiereLicenciaAlcohol;

    @Column(name = "activo")
    private Boolean activo;
}