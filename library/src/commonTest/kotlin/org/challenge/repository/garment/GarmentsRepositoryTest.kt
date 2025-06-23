package org.challenge.repository.garment

import kotlinx.coroutines.test.runTest
import org.challenge.initialize
import org.challenge.repository.garment.model.Category
import org.challenge.repository.garment.model.Color
import org.junit.BeforeClass
import strikt.api.expectThat
import strikt.assertions.isContainedIn
import strikt.assertions.isEqualTo
import strikt.assertions.isGreaterThan
import strikt.assertions.isTrue
import kotlin.test.Test

class GarmentsRepositoryTest {

    companion object {

        @BeforeClass
        @JvmStatic
        fun beforeTest() {
            initialize()
        }
    }

    private val garmentsRepository = GarmentsRepository()

    @Test
    fun `should return all items`() = runTest {
        val response = garmentsRepository.getAllGarments()

        expectThat(response.isSuccess).isTrue()

        response.onSuccess { garments ->
            expectThat(garments.size).isEqualTo(100)
        }
    }

    @Test
    fun `should return items with the category shoes`() = runTest {
        val response = garmentsRepository.getAllGarments(categories = listOf(Category.SHOES))

        expectThat(response.isSuccess).isTrue()

        response.onSuccess { garments ->
            expectThat(garments.size).isEqualTo(20)

            garments.forEach { garment ->
                expectThat(garment.category.lowercase()).isEqualTo(Category.SHOES.value)
            }
        }
    }

    @Test
    fun `should return items with the categories shoes and jacket`() = runTest {
        val response = garmentsRepository.getAllGarments(categories = listOf(Category.SHOES, Category.JACKET))

        expectThat(response.isSuccess).isTrue()

        response.onSuccess { garments ->
            expectThat(garments.size).isEqualTo(39)

            garments.forEach { garment ->
                expectThat(garment.category.lowercase()).isContainedIn(listOf(Category.SHOES.value, Category.JACKET.value))
            }
        }
    }

    @Test
    fun `should return all items with all activated categories`() = runTest {
        val categories = listOf(Category.TOP, Category.JACKET, Category.BOTTOM, Category.DRESS, Category.SHOES, Category.ACCESSORIES)
        val response = garmentsRepository.getAllGarments(categories = categories)

        expectThat(response.isSuccess).isTrue()

        response.onSuccess { garments ->
            expectThat(garments.size).isEqualTo(100)
        }
    }

    @Test
    fun `should return items with the color mustard`() = runTest {
        val response = garmentsRepository.getAllGarments(colors = listOf(Color.MUSTARD))

        expectThat(response.isSuccess).isTrue()

        response.onSuccess { garments ->
            expectThat(garments.size).isEqualTo(1)

            garments.forEach { garment ->
                expectThat(garment.color.lowercase()).isEqualTo(Color.MUSTARD.value)
            }
        }
    }

    @Test
    fun `should return items with the colors mustard and black`() = runTest {
        val response = garmentsRepository.getAllGarments(colors = listOf(Color.MUSTARD, Color.BLACK))

        expectThat(response.isSuccess).isTrue()

        response.onSuccess { garments ->
            expectThat(garments.size).isEqualTo(17)

            garments.forEach { garment ->
                expectThat(garment.color.lowercase()).isContainedIn(listOf(Color.MUSTARD.value, Color.BLACK.value))
            }
        }
    }

    @Test
    fun `should return items with the category shoes and the color black`() = runTest {
        val response = garmentsRepository.getAllGarments(categories = listOf(Category.SHOES), colors = listOf(Color.BLACK))

        expectThat(response.isSuccess).isTrue()

        response.onSuccess { garments ->
            expectThat(garments.size).isGreaterThan(0)
        }
    }
}
