package nl.q42.template.domain.main.model

import kotlin.jvm.JvmInline

@JvmInline
value class SampleUserName(val value: String)

data class SampleUser(val name: SampleUserName)
