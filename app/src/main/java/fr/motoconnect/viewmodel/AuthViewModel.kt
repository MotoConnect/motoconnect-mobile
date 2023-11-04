package fr.motoconnect.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import fr.motoconnect.data.model.SignInResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AuthViewModel: ViewModel() {

    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()

    fun onSignInResult(result: SignInResult) {
        _state.update { it.copy(
            isSignInSuccessful = result.data != null,
            signInError = result.errorMessage
        ) }
    }

    fun resetState() {
        _state.update { SignInState() }
    }
}


data class SignInState(
    val isSignInSuccessful: Boolean = false,
    val signInError: String? = null,
)