package com.example.abschlussprojekt.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.abschlussprojekt.data.FirebaseRepository

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val firebaseRepository = FirebaseRepository()
    val currentUser = firebaseRepository.currentUser
    /**
     * Meldet den Benutzer bei Firebase an.
     * @param email Die E-Mail-Adresse des Benutzers.
     * @param password Das Passwort des Benutzers.
     */
    fun firebaseLogin(email: String, password: String) {
        firebaseRepository.firebaseLogin(email, password)
    }

    /**
     * Registriert einen neuen Benutzer bei Firebase.
     * @param email Die E-Mail-Adresse des neuen Benutzers.
     * @param password Das Passwort des neuen Benutzers.
     */
    fun firebaseRegister(email: String, password: String) {
        firebaseRepository.firebaseRegister(email, password)
    }

    /**
     * Meldet den Benutzer bei Firebase ab.
     */
    fun firebaseLogout() {
        firebaseRepository.firebaseLogout()
    }

    /**
     * Löscht den aktuellen Benutzer bei Firebase.
     */
    fun firebaseDeleteUser() {
        firebaseRepository.firebaseDeleteUser()
    }

    /**
     * Setzt das Passwort des Benutzers bei Firebase zurück.
     * @param email Die E-Mail-Adresse des Benutzers.
     */
    fun firebaseResetPassword(email: String) {
        firebaseRepository.firebaseResetPassword(email)
    }
}