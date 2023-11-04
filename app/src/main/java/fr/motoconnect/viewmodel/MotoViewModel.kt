package fr.motoconnect.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import fr.motoconnect.data.model.Moto
import fr.motoconnect.data.service.MotoService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MotoViewModel: ViewModel() {

    private val motoService: MotoService by lazy { MotoService() }
    private val user = Firebase.auth.currentUser

    private val _uiState = MutableStateFlow(MotoUiState())
    val uiState: StateFlow<MotoUiState> = _uiState.asStateFlow()

}

data class MotoUiState(
    val motos: List<Moto> = emptyList(),
)