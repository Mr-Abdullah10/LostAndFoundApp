package com.abdullah.lostfound.ui.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdullah.lostfound.Repositories.LostRepository
import com.abdullah.lostfound.ui.dataclasses.Lost
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class FoundFragmentViewModel: ViewModel() {

    val lostRepository = LostRepository()

    val failureMessage = MutableStateFlow<String?>(null)
    val data = MutableStateFlow<List<Lost>?>(null)

    init {
        readFoundItem(found = true, status = "Pending")
    }


    fun readFoundItem(found: Boolean, status: String) {
        viewModelScope.launch {
            lostRepository.getFoundItem(found = true, status = "Pending")
                .catch { exception ->
                    failureMessage.value = exception.message // Handle errors gracefully
                }
                .collect { itemList ->
                    data.value = itemList // Update the live data with the retrieved items
                }
        }
    }

}