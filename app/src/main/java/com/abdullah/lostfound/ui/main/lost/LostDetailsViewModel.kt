package com.abdullah.lostfound.ui.main.lost

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdullah.lostfound.Repositories.LostRepository
import com.abdullah.lostfound.ui.dataclasses.Lost
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class LostDetailsViewModel: ViewModel() {



        val lostRepository= LostRepository()

        val isUpdated= MutableStateFlow<Boolean?>(null)
        val failureMessage= MutableStateFlow<String?>(null)

        public fun updateLost(lost: Lost){
            viewModelScope.launch {
                val result=lostRepository.updateLost(lost)
                if (result.isSuccess)
                    isUpdated.value=true
                else
                    failureMessage.value=result.exceptionOrNull()?.message
            }
        }
    }
