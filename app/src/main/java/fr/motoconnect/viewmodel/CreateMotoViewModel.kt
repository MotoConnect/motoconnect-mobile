package fr.motoconnect.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import fr.motoconnect.data.service.MotoService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CreateMotoViewModel: ViewModel() {

    private val motoService: MotoService by lazy { MotoService() }

    private val _uiState = MutableStateFlow(CreateMotoUiState())
    val uiState: StateFlow<CreateMotoUiState> = _uiState.asStateFlow()

    fun updateName(name: String){
        var error = ""

        if(name.length<4){
            error = "Le nom doit être composé d'au moins 4 caractères !"
        }
        _uiState.value = _uiState.value.copy(
            name = name,
            error = error
        )
    }

}

data class CreateMotoUiState(
    val name: String = "",
    val error: String = "",
)