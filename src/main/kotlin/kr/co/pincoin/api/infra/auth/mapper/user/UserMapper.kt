package kr.co.pincoin.api.infra.auth.mapper.user

import kr.co.pincoin.api.domain.auth.model.user.User
import kr.co.pincoin.api.infra.auth.entity.user.UserEntity

fun UserEntity?.toModel(): User? =
    this?.let { entity ->
        User.of(
            id = entity.id,
            dateJoined = entity.dateJoined,
            password = entity.password,
            email = entity.email,
            lastLogin = entity.lastLogin,
            username = entity.username,
            firstName = entity.firstName,
            lastName = entity.lastName,
            isSuperuser = entity.isSuperuser,
            isStaff = entity.isStaff,
            isActive = entity.isActive
        )
    }

fun List<UserEntity>?.toModelList(): List<User> = this?.mapNotNull { it.toModel() } ?: emptyList()

fun User?.toEntity(): UserEntity? =
    this?.let { user ->
        UserEntity.of(
            id = user.id,
            dateJoined = user.dateJoined,
            password = user.password,
            email = user.email,
            lastLogin = user.lastLogin,
            username = user.username,
            firstName = user.firstName,
            lastName = user.lastName,
            isSuperuser = user.isSuperuser,
            isStaff = user.isStaff,
            isActive = user.isActive
        )
    }

fun List<User>?.toEntityList(): List<UserEntity> = this?.mapNotNull { it.toEntity() } ?: emptyList()