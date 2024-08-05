package com.example.studybuddy.domain.model

data class User(
    val userId:String = "",
    val name: String = "",
    val email:String = "",
    val password:String = "",
    val PremiumMember:Boolean
)
