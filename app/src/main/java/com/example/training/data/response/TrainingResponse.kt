package com.example.training.data.response

import com.google.firebase.Timestamp

class TrainingResponse(
    val name: Int? = 0,
    val description: String? = "",
    val data: Timestamp? = null
)