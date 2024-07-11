package id.anantyan.livecode.data.service

import id.anantyan.livecode.common.Constant.API_KEY
import id.anantyan.livecode.data.model.Credits
import id.anantyan.livecode.data.model.Movies
import id.anantyan.livecode.data.model.MoviesDetail
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesApi {
    @GET("trending/movie/week")
    suspend fun byTrendingWeek(
        @Query("api_key") apiKey: String? = API_KEY,
        @Query("page") page: Int? = 1
    ): Response<Movies>

    @GET("search/movie")
    suspend fun bySearch(
        @Query("api_key") apiKey: String? = API_KEY,
        @Query("query") query: String?,
        @Query("page") page: Int? = 1
    ): Response<Movies>

    @GET("movie/{id}")
    suspend fun getIdMovie(
        @Path("id") id: String,
        @Query("api_key") apiKey: String? = API_KEY
    ): Response<MoviesDetail>

    @GET("movie/{id}/credits")
    suspend fun getCredits(
        @Path("id") id: String,
        @Query("api_key") apiKey: String? = API_KEY
    ): Response<Credits>
}