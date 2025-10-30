package nl.q42.template.domain.main.model

import kotlin.jvm.JvmInline

@JvmInline
value class EmailAddress(val value: String)

data class User(val email: EmailAddress)
