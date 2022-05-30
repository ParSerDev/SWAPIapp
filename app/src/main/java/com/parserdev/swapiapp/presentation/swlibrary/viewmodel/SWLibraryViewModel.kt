package com.parserdev.swapiapp.presentation.swlibrary.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.parserdev.swapiapp.data.SWLibraryRepository
import com.parserdev.swapiapp.data.models.categories.characters.SWLibraryCategoryCharacters
import com.parserdev.swapiapp.domain.model.SWLibraryItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class SWLibraryViewModel @Inject constructor(
    private val swLibraryRepository: SWLibraryRepository
) : ViewModel() {


    private val TAG = "Library"

    private val _progressBarVisibilityFlow = MutableStateFlow(true)
    val progressBarVisibilityFlow = _progressBarVisibilityFlow.asStateFlow()

    val itemsListFlow = flow {
        val response = try {
            swLibraryRepository.requestAllCharacters()
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
            val body = response.body()!!
            if (body::class.java == SWLibraryCategoryCharacters::class.java) {
                emit(
                    body.results.map {
                        SWLibraryItem(
                            title = it.name,
                            url = it.url
                        )
                    }.sortedBy { it.title }
                )
            }
        } else {
            Log.e(TAG, "Response not successful")
        }
        _progressBarVisibilityFlow.value = false
    }

    fun toggleProgressBarVisibility() {
        _progressBarVisibilityFlow.value = !progressBarVisibilityFlow.value
    }

}