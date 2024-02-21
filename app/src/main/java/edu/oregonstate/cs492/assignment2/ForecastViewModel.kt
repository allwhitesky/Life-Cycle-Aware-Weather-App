package edu.oregonstate.cs492.assignment2

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.oregonstate.cs492.assignment2.data.ForecastPeriod
import edu.oregonstate.cs492.assignment2.data.ForecastRepository
import edu.oregonstate.cs492.assignment2.data.ForecastService
import kotlinx.coroutines.launch

class ForecastViewModel : ViewModel() {
    private val repository = ForecastRepository(ForecastService.create())

    private val _searchResults = MutableLiveData<List<ForecastPeriod>?>(null)
    val searchResults: LiveData<List<ForecastPeriod>?> = _searchResults

    private val _loadingStatus = MutableLiveData<LoadingStatus>(LoadingStatus.SUCCESS)
    val loadingStatus: LiveData<LoadingStatus> = _loadingStatus

    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?> = _error

    fun loadSearchResults(lat: String, lon: String, appid: String, unit: String) {
        viewModelScope.launch {
            _loadingStatus.value = LoadingStatus.LOADING
            val result = repository.loadRepositoriesSearch(lat, lon, appid, unit)
            _searchResults.value = result.getOrNull()
            _error.value = result.exceptionOrNull()?.message
            _loadingStatus.value = when (result.isSuccess) {
                true -> LoadingStatus.SUCCESS
                false -> LoadingStatus.ERROR
            }
        }
    }
}