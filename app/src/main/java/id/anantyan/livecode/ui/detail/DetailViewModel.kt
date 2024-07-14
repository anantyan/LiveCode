package id.anantyan.livecode.ui.main.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.anantyan.livecode.common.UIState
import id.anantyan.livecode.data.model.Credits
import id.anantyan.livecode.data.model.MoviesDetail
import id.anantyan.livecode.data.repository.MoviesRemoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: MoviesRemoteRepository
) : ViewModel() {
    private var _detailPage: MutableLiveData<UIState<MoviesDetail>> = MutableLiveData(UIState.Success(MoviesDetail()))
    private var _creditsPage: MutableLiveData<UIState<Credits>> = MutableLiveData(UIState.Success(Credits()))

    val detailPage: LiveData<UIState<MoviesDetail>> = _detailPage
    val creditsPage: LiveData<UIState<Credits>> = _creditsPage

    fun detailWithCreditsPage(id: String) {
        viewModelScope.launch {
            repository.detailPage(id).collect {
                _detailPage.value = it
            }
        }

        viewModelScope.launch {
            repository.creditsPage(id).collect {
                _creditsPage.value = it
            }
        }
    }
}