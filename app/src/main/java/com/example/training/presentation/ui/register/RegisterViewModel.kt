package com.example.training.presentation.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.training.data.repository.RegisterRepository
import com.example.training.data.response.RegisterResult
import com.example.training.domain.model.FieldStatus
import com.example.training.domain.model.User
import com.example.treinoacademia.R
import kotlinx.coroutines.launch

class RegisterViewModel(private val registerRepository: RegisterRepository) : ViewModel() {
    private val successfullyRegisteredUserMutableLiveData = MutableLiveData<Int>()
    val successfullyRegisteredUserLiveData: LiveData<Int> =
        successfullyRegisteredUserMutableLiveData
    private val errorWhenRegisteringUserMutableLiveData = MutableLiveData<Int>()
    val errorWhenRegisteringUserLiveData: LiveData<Int> =
        errorWhenRegisteringUserMutableLiveData
    private val emailErrorMessageMutableLiveData = MutableLiveData<Int?>()
    val emailErrorMessageLiveData: LiveData<Int?> = emailErrorMessageMutableLiveData
    private val passwordErrorMessageMutableLiveData = MutableLiveData<Int?>()
    val passwordErrorMessageLiveData: LiveData<Int?> = passwordErrorMessageMutableLiveData

    fun registerUser(user: User) {
        viewModelScope.launch {
            val isValidEmail = validateEmail(user)
            val isValidPassword = validatePassword(user)
            if (isValidEmail && isValidPassword) {
                registerRepository.getUserRegistration(user) { register ->
                    when (register) {
                        is RegisterResult.Success -> {
                            successfullyRegisteredUserMutableLiveData.postValue(R.string.register_successfully_registered_user)
                        }
                        is RegisterResult.Error -> {
                            errorWhenRegisteringUserMutableLiveData.postValue(
                                R.string.register_failed_to_register_user
                            )
                        }
                    }
                }
            }
        }
    }

    private fun validateEmail(user: User): Boolean {
        return when (user.validateEmail()) {
            FieldStatus.VALID -> {
                emailErrorMessageMutableLiveData.postValue(null)
                true
            }
            FieldStatus.INVALID -> {
                emailErrorMessageMutableLiveData.postValue(R.string.login_email_error_message_text)
                false
            }
            FieldStatus.BLANK -> {
                emailErrorMessageMutableLiveData.postValue(R.string.login_email_error_message_text)
                false
            }
        }
    }

    private fun validatePassword(user: User): Boolean {
        return when (user.validatePassword()) {
            FieldStatus.VALID -> {
                passwordErrorMessageMutableLiveData.postValue(null)
                true
            }
            FieldStatus.INVALID -> {
                passwordErrorMessageMutableLiveData.postValue(R.string.login_password_error_message_text)
                false
            }
            FieldStatus.BLANK -> {
                passwordErrorMessageMutableLiveData.postValue(R.string.login_password_error_message_text)
                false
            }
        }
    }
}