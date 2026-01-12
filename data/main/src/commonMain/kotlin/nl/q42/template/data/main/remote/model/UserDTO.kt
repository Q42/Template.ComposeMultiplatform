package nl.q42.template.data.main.remote.model

import kotlinx.serialization.Serializable
import nl.q42.template.data.main.local.model.UserEntity

@Serializable
data class UserDTO(val title: String)

internal fun UserDTO.toUserEntity(): UserEntity = UserEntity(name = title)