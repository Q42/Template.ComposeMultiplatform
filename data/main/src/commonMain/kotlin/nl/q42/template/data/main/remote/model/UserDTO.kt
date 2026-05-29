package nl.q42.template.data.main.remote.model

import kotlinx.serialization.Serializable
import nl.q42.template.domain.main.model.User
import nl.q42.template.domain.main.model.UserName

@Serializable
data class UserDTO(val title: String)

internal fun UserDTO.toUser(): User = User(name = UserName(value = title))