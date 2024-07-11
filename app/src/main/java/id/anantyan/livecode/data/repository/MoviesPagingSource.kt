package id.anantyan.livecode.data.repository

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import id.anantyan.livecode.data.model.ResultsItem
import id.anantyan.livecode.data.service.MoviesApi
import retrofit2.HttpException
import java.io.IOException

class MoviesPagingSource(
    private val moviesApi: MoviesApi
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
            val response = moviesApi.byTrendingWeek(page = position)
            val list = response.body()?.results ?: emptyList()
            Log.d("PagingSource", "Page Number: $position, Data Size: ${list.size}")
            LoadResult.Page(
                data = list,
                prevKey = if (position == 1) null else position - 1,
                nextKey = if (list.isEmpty()) null else position + 1
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }
}