package id.anantyan.livecode.data.model

import com.google.gson.annotations.SerializedName

data class Credits(

	@SerializedName("cast")
	val cast: List<CastItem>? = null
)

data class CastItem(

	@SerializedName("cast_id")
	val castId: Int? = null,

	@SerializedName("character")
	val character: String? = null,

	@SerializedName("gender")
	val gender: Int? = null,

	@SerializedName("credit_id")
	val creditId: String? = null,

	@SerializedName("known_for_department")
	val knownForDepartment: String? = null,

	@SerializedName("original_name")
	val originalName: String? = null,

	@SerializedName("popularity")
	val popularity: Double? = null,

	@SerializedName("name")
	val name: String? = null,

	@SerializedName("profile_path")
	val _profilePath: String? = null,

	@SerializedName("id")
	val id: Int? = null,

	@SerializedName("adult")
	val adult: Boolean? = null,

	@SerializedName("order")
	val order: Int? = null
) {
	val profilePath get() = "https://image.tmdb.org/t/p/w500/${_profilePath}"
}
