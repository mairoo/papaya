package kr.co.pincoin.api.infra.common

import jakarta.persistence.Column
import jakarta.persistence.MappedSuperclass

@MappedSuperclass
abstract class BaseRemovalDateTime : BaseDateTime() {
    @Column(name = "is_removed")
    var isRemoved: Boolean = false
}