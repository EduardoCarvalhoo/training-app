package com.example.training.data.rest.api

import com.example.training.data.repository.RegisterRepository
import com.example.training.domain.model.RegisterResult
import com.example.training.domain.model.FieldStatus
import com.example.training.domain.model.User
import com.google.firebase.auth.FirebaseAuth

class RegisterRepositoryImpl(private val firebaseUser: FirebaseAuth): RegisterRepository {
    override suspend fun createUser(
        user: User,
        registerCallback: (register: RegisterResult) -> Unit
    ) {
        try {
            firebaseUser.createUserWithEmailAndPassword(user.email, user.password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        registerCallback(RegisterResult.Success(FieldStatus.VALID))
                    } else {
                        registerCallback(RegisterResult.Error(FieldStatus.INVALID))
                    }
                }
        } catch (e: Exception) {
            registerCallback(RegisterResult.Error(FieldStatus.INVALID))
        }
    }
}