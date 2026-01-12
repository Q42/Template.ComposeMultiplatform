package nl.q42.template.data.main.local.model

import nl.q42.template.domain.main.model.UserName
import nl.q42.template.domain.main.model.User

internal data class UserEntity(val name: String)

internal fun UserEntity.mapToUser() = User(name = UserName(name))
