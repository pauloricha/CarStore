package br.com.fiap.carstore.presentation.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.fiap.carstore.util.CodeErrorEnum
import br.com.fiap.carstore.util.isValidEmail
import br.com.fiap.carstore.domain.AuthState
import com.google.firebase.auth.FirebaseAuth

class LoginViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val _authState by lazy { MutableLiveData<AuthState>(AuthState.Idle) }
    val authState: LiveData<AuthState> = _authState

    fun register(email: String, password: String) {
        if (verifyInputs(email, password)) return
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(
            email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authState.value = AuthState.Success
                } else {
                    task.exception?.let {
                        _authState.value = AuthState.Error(message = it.localizedMessage)
                    }
                }
            }
    }

    fun login(email: String, password: String){
        if (verifyInputs(email, password)) return
        FirebaseAuth.getInstance().signInWithEmailAndPassword(
            email,password)
            .addOnCompleteListener { task ->
            if(task.isSuccessful){
                _authState.value = AuthState.Success
            }
        }.addOnFailureListener { exception ->
                _authState.value = AuthState.Error(message = exception.localizedMessage)
        }
    }

    private fun verifyInputs(email: String, password: String): Boolean {
        if (email.isEmpty() || !email.isValidEmail()) {
            _authState.value = AuthState.Error(CodeErrorEnum.INVALID_EMAIL)
            return true
        }
        if (password.isEmpty()) {
            _authState.value = AuthState.Error(CodeErrorEnum.MUST_ENTER_PASSWORD)
            return true
        }
        return false
    }
}