package id.anantyan.livecode.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import id.anantyan.livecode.data.model.ResultsItem
import id.anantyan.livecode.data.service.MoviesApi
import retrofit2.HttpException
import java.io.IOException

class MoviesSearchPagingSource(
    private val moviesApi: MoviesApi,
    private val search: String?
) : PagingSource<Int, ResultsItem>() {
    override fun getRefreshKey(state: PagingState<Int, ResultsItem>): Int? {
        return state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ResultsItem> {
        val position = params.key ?: 1
        return try {
            val items: List<ResultsItem> = if (search.isNullOrEmpty()) {
                listOf()
            } else {
                val response = moviesApi.bySearch(query = search, page = position)
                response.body()?.results ?: listOf()
            }
            LoadResult.Page(
                data = items,
                prevKey = if (position == 1) null else position - 1,
                nextKey = if (items.isEmpty()) null else position + 1
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }
}