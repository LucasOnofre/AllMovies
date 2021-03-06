package onoffrice.wikimovies.fragment.category_fragment

import onoffrice.wikimovies.model.Genre

class CategoryFragmentModel: CategoryFragmentContract.Model{


    override fun populateGenres(listener: CategoryFragmentContract.Model.Data) {

        val genres = generateGenres()

        // Send's the array of genres to the model allready populated
         listener.populateListData(genres)
        }

     fun generateGenres(): Array<Genre> {
        return arrayOf(

                Genre(18    , "Action")         ,
                Genre(12    , "Adventure")      ,
                Genre(16    , "Animation")      ,
                Genre(35    , "Comedy")         ,
                Genre(80    , "Crime")          ,
                Genre(99    , "Documentary")    ,
                Genre(10751 , "Family")         ,
                Genre(14    , "Fantasy")        ,
                Genre(36    , "History")        ,
                Genre(27    , "Horror")         ,
                Genre(10402 , "Music")          ,
                Genre(9648  , "Mystery")        ,
                Genre(10749 , "Romance")        ,
                Genre(878   , "Science Fiction"),
                Genre(10770 , "TV Movie")       ,
                Genre(53    , "Thriller")       ,
                Genre(10752 , "War")            ,
                Genre(37    , "Western")
        )
    }
}