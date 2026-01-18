package com.tuxoftware.ms_padron_unico.persistence.entity;


import com.tuxoftware.ms_padron_unico.enums.EstatusRegistro;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.UUID;

@MappedSuperclass
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "estatus", columnDefinition = "estatus_registro_enum")
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @Builder.Default
    private EstatusRegistro estatus = EstatusRegistro.ACTIVO;

    @CreatedBy
    @Column(name = "created_by", updatable = false)
    private String createdBy; // Guardará el username (ej. "admin", "jperez")

    @LastModifiedBy
    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Instant updatedAt;
}