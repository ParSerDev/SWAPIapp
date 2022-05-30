package com.parserdev.swapiapp.presentation.swdetails.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.parserdev.swapiapp.data.SWLibraryRepository
import com.parserdev.swapiapp.domain.model.SWCharacter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class SWDetailsViewModel @Inject constructor(
    private val swLibraryRepository: SWLibraryRepository
) : ViewModel() {

    private val TAG = "Details"

    private val _progressBarVisibilityFlow = MutableStateFlow(true)
    val progressBarVisibilityFlow = _progressBarVisibilityFlow.asStateFlow()

    fun getSWCharacterDataFlow(url: String): Flow<SWCharacter> {
        return flow {
            _progressBarVisibilityFlow.value = true
            val response = try {
                swLibraryRepository.getCharacter(url = url)
            } catch (e: IOException) {
                Log.e(TAG, "IOException, you might not have internet connection")
                _progressBarVisibilityFlow.value = false
                return@flow
            } catch (e: HttpException) {
                Log.e(TAG, "HttpException, unexpected response")
                _progressBarVisibilityFlow.value = false
                return@flow
            }
            if (response.isSuccessful && response.body() != null) {
                emit(response.body()!!)
            } else {
                Log.e(TAG, "Response not successful")
            }
            _progressBarVisibilityFlow.value = false
        }
    }

    fun toggleProgressBarVisibility() {
        _progressBarVisibilityFlow.value = !progressBarVisibilityFlow.value
    }


}