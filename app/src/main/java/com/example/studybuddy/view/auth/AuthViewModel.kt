package com.example.studybuddy.view.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.studybuddy.domain.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AuthViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    val _authState = MutableLiveData<AuthState>()
    val authState:LiveData<AuthState> = _authState

    init {
        checkAuthStatus()
    }

    fun checkAuthStatus(){
        if (auth.currentUser!=null)
        {
            _authState.value = AuthState.Authenticated
        }
        else{
            _authState.value=AuthState.Unauthenticated
        }
    }

    fun login(email:String, password:String)
    {
        if (email.isEmpty()||password.isEmpty())
        {
            _authState.value = AuthState.Error("Please fill all the credentials before login")
            return
        }
        _authState.value = AuthState.Loading
        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener {task ->
                if (task.isSuccessful)
                {
                    _authState.value = AuthState.Authenticated
                }else{
                    _authState.value = AuthState.Error(task.exception?.message?:"Something went wrong")
                }
            }
    }

    fun signUp(email: String, password: String, name: String, premiumMember: Boolean) {
        if (email.isEmpty() || password.isEmpty() || name.isEmpty()) {
            _authState.value = AuthState.Error("Please fill all the credentials before signing up")
            return
        }
        _authState.value = AuthState.Loading
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid ?: ""
                    val user = User(
                        userId = userId,
                        name = name,
                        email = email,
                        password = password,
                        PremiumMember = false
                    )
                    firestore.collection("users").document(userId)
                        .set(user)
                        .addOnSuccessListener {
                            _authState.value = AuthState.RegisterSuccess
                        }
                        .addOnFailureListener { exception ->
                            _authState.value = AuthState.Error(exception.message ?: "Failed to store user data in Firestore")
                        }
                } else {
                    _authState.value = AuthState.Error(task.exception?.message ?: "Something went wrong")
                }
            }
    }

    fun logout(){
        auth.signOut()
        _authState.value = AuthState.Unauthenticated
    }



}


sealed class AuthState{
    data object Authenticated: AuthState()
    data object Unauthenticated: AuthState()
   data object Loading: AuthState()

    data object RegisterSuccess: AuthState()
    data class Error(val message: String): AuthState()
}