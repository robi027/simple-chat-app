package com.example.simplechatapp.main

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class MainViewModel : ViewModel(){
    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }

    fun doLogout(){
        firebaseAuth.signOut()
    }

}