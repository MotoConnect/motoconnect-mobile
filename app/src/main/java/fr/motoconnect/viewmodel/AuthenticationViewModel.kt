package fr.motoconnect.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthenticationViewModel(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore
) : ViewModel() {

    private val _authUiState = MutableStateFlow(AuthUIState())
    val authUiState: StateFlow<AuthUIState> = _authUiState.asStateFlow()

    fun signIn(email: String, password: String){
        if (!isMandatoryFieldsFilled(email, password)) {
            _authUiState.update { AuthUIState(isLogged = false, errorMessage = "Veuillez remplir tous les champs") }
            return
        }
        auth.signInWithEmailAndPassword(email, password)
            .addOnFailureListener() { exception ->
                _authUiState.update { AuthUIState(isLogged = false, errorMessage = exception.message) }
            }
            .addOnCompleteListener { task ->
                viewModelScope.launch(Dispatchers.IO) {
                    if (task.isSuccessful) {
                        _authUiState.update { AuthUIState(isLogged = true, errorMessage = null) }
                    }
                }
            }
    }

    fun signUp(email: String, password: String) {
        if (!isMandatoryFieldsFilled(email, password)) {
            _authUiState.update { AuthUIState(isLogged = false, errorMessage = "Veuillez remplir tous les champs") }
            return
        }
        auth.createUserWithEmailAndPassword(email, password)
            .addOnFailureListener() { exception ->
                _authUiState.update { AuthUIState(isLogged = false, errorMessage = exception.message) }
            }
            .addOnCompleteListener { task ->
                viewModelScope.launch(Dispatchers.IO) {
                    if (task.isSuccessful) {
                        _authUiState.update { AuthUIState(isLogged = true, errorMessage = null) }
                    }
                }
            }
    }

    fun resetPassword(email: String) {
        if (email.isEmpty()) {
            _authUiState.update { AuthUIState(errorMessage = "Veuillez remplir tous les champs") }
            return
        }
        auth.sendPasswordResetEmail(email)
            .addOnFailureListener { exception ->
                _authUiState.update { AuthUIState(errorMessage = exception.message) }
            }
            .addOnCompleteListener { task ->
                viewModelScope.launch(Dispatchers.IO) {
                    if (task.isSuccessful) {
                        _authUiState.update { AuthUIState(errorMessage = "Un mail de réinitialisation vous a été envoyé") }
                    }
                }
            }
    }

    fun signOut() {
        auth.signOut()
        _authUiState.update { AuthUIState(isLogged = false, errorMessage = null) }
    }

    fun resetErrorMessage() {
        _authUiState.update { AuthUIState(errorMessage = null) }
    }

    private fun isMandatoryFieldsFilled(email: String, password: String): Boolean {
        return email.isNotEmpty() && password.isNotEmpty()
    }

}


data class AuthUIState(
    val isLogged: Boolean = false,
    val errorMessage: String? = null
)