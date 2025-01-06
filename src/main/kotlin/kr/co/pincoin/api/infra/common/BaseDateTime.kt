package kr.co.pincoin.api.infra.common

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseDateTime {
    @CreatedDate
    @Column(name = "created", updatable = false)
    val created: LocalDateTime? = null

    @LastModifiedDate
    @Column(name = "modified")
    val modified: LocalDateTime? = null
}