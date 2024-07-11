package id.anantyan.livecode.data.model

import com.google.gson.annotations.SerializedName

data class Movies(

    @SerializedName("results")
    val results: List<ResultsItem>? = null
)

data class ResultsItem(

    @SerializedName("overview")
    val overview: String? = null,

    @SerializedName("original_language")
    val originalLanguage: String? = null,

    @SerializedName("original_title")
    val originalTitle: String? = null,

    @SerializedName("video")
    val video: Boolean? = null,

    @SerializedName("title")
    val title: String? = null,

    @SerializedName("genre_ids")
    val genreIds: List<Int>? = null,

    @SerializedName("poster_path")
    val _posterPath: String? = null,

    @SerializedName("backdrop_path")
    val _backdropPath: String? = null,

    @SerializedName("release_date")
    val releaseDate: String? = null,

    @SerializedName("media_type")
    val mediaType: String? = null,

    @SerializedName("vote_average")
    val voteAverage: Double? = null,

    @SerializedName("popularity")
    val popularity: Double? = null,

    @SerializedName("id")
    val id: Int? = null,

    @SerializedName("adult")
    val adult: Boolean? = null,

    @SerializedName("vote_count")
    val voteCount: Int? = null
) {
    val backdropPath get() = "https://image.tmdb.org/t/p/w500/${_backdropPath}"
    val posterPath get() = "https://image.tmdb.org/t/p/w500/${_posterPath}"
}