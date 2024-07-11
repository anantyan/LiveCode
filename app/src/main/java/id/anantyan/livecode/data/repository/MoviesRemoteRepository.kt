package id.anantyan.livecode.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import id.anantyan.livecode.common.UIState
import id.anantyan.livecode.data.model.Credits
import id.anantyan.livecode.data.model.MoviesDetail
import id.anantyan.livecode.data.model.ResultsItem
import id.anantyan.livecode.data.service.MoviesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Created by Arya Rezza Anantya on 11/07/24.
 */
class MoviesRemoteRepository(
    private val api: MoviesApi
) {
    fun trendingPage(): Flow<PagingData<ResultsItem>> {
        return Pager(
            config = PagingConfig(pageSize = 10, enablePlaceholders = false),
            pagingSourceFactory = { MoviesPagingSource(api) }
        ).flow
    }

    fun searchPage(search: String?): Flow<PagingData<ResultsItem>> {
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = { MoviesSearchPagingSource(api, search) }
        ).flow
    }

    fun detailPage(id: String): Flow<UIState<MoviesDetail>> {
        return flow {
            emit(UIState.Loading())
            try {
                val response = api.getIdMovie(id)
                if (response.isSuccessful) {
                    emit(UIState.Success(response.body()!!))
                } else {
                    emit(UIState.Error(null, "Terjadi kesalahan!"))
                }
            } catch (e: Exception) {
                emit(UIState.Error(null, e.message ?: ""))
            }
        }
    }

    fun creditsPage(id: String): Flow<UIState<Credits>> {
        return flow {
            emit(UIState.Loading())
            try {
                val response = api.getCredits(id)
                if (response.isSuccessful) {
                    emit(UIState.Success(response.body()!!))
                } else {
                    emit(UIState.Error(null, "Terjadi kesalahan!"))
                }
            } catch (e: Exception) {
                emit(UIState.Error(null, e.message ?: ""))
            }
        }
    }
}