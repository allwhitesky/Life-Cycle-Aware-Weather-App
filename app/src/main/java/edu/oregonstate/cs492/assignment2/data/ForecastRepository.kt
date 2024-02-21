package edu.oregonstate.cs492.assignment2.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ForecastRepository(
    private val service: ForecastService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    // Define a cache to store previously loaded results
    private val cache: MutableMap<String, List<ForecastPeriod>> = mutableMapOf()

    suspend fun loadRepositoriesSearch(lat: String, lon: String, appid: String, unit: String): Result<List<ForecastPeriod>> =
        withContext(ioDispatcher) {
            try {
                // Check if the query has been cached
                val cacheKey = "$lat|$lon|$appid|$unit"
                if (cache.containsKey(cacheKey)) {
                    // Return cached result if available
                    return@withContext Result.success(cache[cacheKey]!!)
                }

                // Query the API if not cached
                val response = service.searchForecast(lat, lon, appid, unit)
                if (response.isSuccessful) {
                    val resultList = response.body()?.list ?: listOf()
                    // Cache the result for future use
                    cache[cacheKey] = resultList
                    return@withContext Result.success(resultList)
                } else {
                    Result.failure(Exception(response.errorBody()?.string()))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
}
