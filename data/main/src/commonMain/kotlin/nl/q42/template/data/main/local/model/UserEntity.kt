package nl.q42.template.data.main.local.model

import nl.q42.template.domain.main.model.UserName
import nl.q42.template.domain.main.model.User
import kotlin.jvm.JvmInline

internal data class UserEntity(val name: UserNameEntity)

@JvmInline
value class UserNameEntity(val value: String)

internal fun UserEntity.mapToUser() = User(name = UserName(name.value))
