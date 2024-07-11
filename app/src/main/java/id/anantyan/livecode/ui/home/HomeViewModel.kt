package id.anantyan.livecode.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import id.anantyan.livecode.data.model.ResultsItem
import id.anantyan.livecode.data.repository.MoviesRemoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: MoviesRemoteRepository
) : ViewModel() {
    private var _searchPage: MutableLiveData<String> = MutableLiveData("")

    val homePage: LiveData<PagingData<ResultsItem>> = repository.trendingPage().cachedIn(viewModelScope).asLiveData()
    val searchPage: LiveData<PagingData<ResultsItem>> = _searchPage.switchMap { repository.searchPage(it).cachedIn(viewModelScope).asLiveData() }

    fun searchPage(search: String) {
        _searchPage.postValue(search)
    }
}