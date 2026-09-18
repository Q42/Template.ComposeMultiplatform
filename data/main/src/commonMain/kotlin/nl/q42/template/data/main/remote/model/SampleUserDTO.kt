package nl.q42.template.data.main.remote.model

import kotlinx.serialization.Serializable
import nl.q42.template.domain.main.model.SampleUser
import nl.q42.template.domain.main.model.SampleUserName

@Serializable
data class SampleUserDTO(val title: String)

internal fun SampleUserDTO.toSampleUser(): SampleUser = SampleUser(name = SampleUserName(value = title))