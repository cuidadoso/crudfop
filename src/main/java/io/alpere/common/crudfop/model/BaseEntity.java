package io.alpere.common.crudfop.model;

import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
@Setter
@ToString(of = "id")
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, updatable = false)
    protected UUID id;

    @CreatedBy
    @Column(name = "created_by", nullable = false, updatable = false)
    protected UUID createdBy;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    protected ZonedDateTime createdAt;

    @LastModifiedBy
    @Column(name = "updated_by", nullable = false)
    protected UUID updatedBy;

    @Version
    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private ZonedDateTime updatedAt;


    @Column(name = "deleted_by")
    protected UUID deletedBy;

    @Column(name = "deleted_at")
    private ZonedDateTime deletedAt;

}
