package onoffrice.wikimovies.extension

import onoffrice.wikimovies.model.Movie
import org.junit.Assert.assertEquals
import org.junit.Test


class ExtensionKtTest {

    @Test
    fun test_formatDataToShowOnlyYear(){

        val dataTeste     = "2019-02-03"
        val result = dataTeste.formatDateToYear()

        assertEquals("2019",result)
    }

    @Test
    fun test_formatForChosenType(){
        val teste       = "12345"
        val result = teste.parseJson<Int>()

        assertEquals(12345,result)

    }


    @Test
    fun test_favoriteMovie() {
        val movie             = Movie()
        val listFavoritesTest = ArrayList<Movie>()

        var sizeList = movie.favoriteMovie(listFavoritesTest)

        assertEquals(1, sizeList.size)
        assertEquals(true, movie.isFavorite)
    }

    @Test
    fun unFavoriteMovie() {

        val movie  = Movie()

        val listFavoritesTest = ArrayList<Movie>()
        listFavoritesTest.add(movie)

        var sizeList = movie.unFavoriteMovie(listFavoritesTest)

        assertEquals(0, sizeList.size)
        assertEquals(false, movie.isFavorite)
    }

    @Test
    fun testIfTheMovieIsFavorite(){

        val movie             = Movie()
        val listFavoritesTest = ArrayList<Movie>()

        listFavoritesTest.add(movie)

        listFavoritesTest.checkFavorite(movie)

        assertEquals(true,movie.isFavorite)
    }
    @Test
    fun testIfTheMovieIsFavorite_error(){

        val movie             = Movie()
        val listFavoritesTest = ArrayList<Movie>()

        listFavoritesTest.checkFavorite(movie)
        assertEquals(false,movie.isFavorite)
    }
}