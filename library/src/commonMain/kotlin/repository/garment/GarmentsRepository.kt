package org.challenge.repository.garment

import io.ktor.client.call.body
import org.challenge.api.ApiClient
import org.challenge.repository.garment.model.Category
import org.challenge.repository.garment.model.Color
import org.challenge.repository.garment.model.Garment
import org.koin.java.KoinJavaComponent.inject

/**
 * Manages the garments-resource.
 */
class GarmentsRepository {

    private val client: ApiClient by inject(ApiClient::class.java)

    /**
     * Retrieves the garments depending on the assigned filters. Filter-objects will be applied if they are not null and not empty.
     * Otherwise, all items will be returned.
     *
     * @param categories The categories-filter as a [List] of [Category]-objects.
     * @param colors The color-filter as a [List] of [Color]-objects.
     * @return Returns the garments as a [List] of [Garment]-objects.
     */
    suspend fun getAllGarments(categories: List<Category>? = null, colors: List<Color>? = null): List<Garment> {
        return client
            .getAllGarments()
            .body<List<Garment>>()
            .run {
                if (categories != null && categories.isNotEmpty()) {
                    filter { garment -> garment.category.lowercase() in categories.map { category -> category.value } }
                } else {
                    this
                }
            }.run {
                if (colors != null && colors.isNotEmpty()) {
                    filter { garment -> garment.color.lowercase() in colors.map { color -> color.value } }
                } else {
                    this
                }
            }
    }
}