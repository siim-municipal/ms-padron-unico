package com.tuxoftware.ms_padron_unico.persistence.entity;

import com.tuxoftware.ms_padron_unico.enums.TipoPersona;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

import java.util.List;

@Entity
@Table(name = "sujetos_pasivos")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class SujetoPasivo extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_persona", nullable = false)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private TipoPersona tipoPersona;

    @Column(unique = true, length = 13)
    private String rfc;

    @Column(length = 18)
    private String curp;

    @Column(name = "nombre_razon_social", nullable = false)
    private String nombreRazonSocial;

    @Column(name = "apellido_paterno")
    private String apellidoPaterno;

    @Column(name = "apellido_materno")
    private String apellidoMaterno;

    private String email;

    @Column(name = "telefono_movil")
    private String telefonoMovil;

    @Column(name = "direccion_fiscal", columnDefinition = "TEXT")
    private String direccionFiscal;

    // Relación Inversa (Opcional, útil para ver propiedades de una persona)
    @OneToMany(mappedBy = "sujeto", fetch = FetchType.LAZY)
    private List<PropiedadPredio> propiedades;
}
