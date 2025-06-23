package org.challenge.repository.garment.model

import kotlinx.serialization.Serializable

@Serializable
data class Garment(
    val id: Int,
    val name: String,
    val category: String,
    val color: String,
    val material: String,
    val compatibleWith: List<String>
)
