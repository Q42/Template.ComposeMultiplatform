package nl.q42.template.data.main.remote.model

import nl.q42.template.data.main.local.model.UserEntity

data class UserDTO(val email: String)

internal fun UserDTO.toUserEntity(): UserEntity = UserEntity(email = email)