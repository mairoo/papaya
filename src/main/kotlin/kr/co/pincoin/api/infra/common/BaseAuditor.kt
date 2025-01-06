package kr.co.pincoin.api.infra.common

import jakarta.persistence.Column
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.LastModifiedBy

@MappedSuperclass
abstract class BaseAuditor : BaseDateTime() {
    @CreatedBy
    @Column(name = "created_by", updatable = false)
    val createdBy: Long? = null

    @LastModifiedBy
    @Column(name = "last_modified_by")
    val lastModifiedBy: Long? = null
}