package nl.q42.template.domain.main.model

import kotlin.jvm.JvmInline

@JvmInline
value class UserName(val value: String)

data class User(val name: UserName)
