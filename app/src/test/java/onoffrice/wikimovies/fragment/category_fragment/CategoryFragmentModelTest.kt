package onoffrice.wikimovies.fragment.category_fragment

import org.junit.Assert.assertEquals
import org.junit.Test

class CategoryFragmentModelTest {

    @Test
    fun test_favoriteGenres() {

        val genres = CategoryFragmentModel().generateGenres()
        val firstGenre = genres[0].name
        assertEquals(18,genres.size)
        assertEquals("Action",firstGenre)

    }
}