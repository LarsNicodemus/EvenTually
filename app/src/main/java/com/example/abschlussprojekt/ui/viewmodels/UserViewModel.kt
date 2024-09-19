package com.example.abschlussprojekt.ui.viewmodels

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import com.example.abschlussprojekt.data.FirebaseRepository
import com.example.abschlussprojekt.datamodel.remotemodels.firebasefirestore.GlobalEventProfile
import com.example.abschlussprojekt.datamodel.remotemodels.firebasefirestore.GlobalPartnerProfile
import com.example.abschlussprojekt.datamodel.remotemodels.firebasefirestore.GlobalProfile
import com.google.firebase.firestore.DocumentReference

/**
 * ViewModel zur Verwaltung der Benutzerprofile, Partnerprofile und Eventprofile.
 * @param application Die Anwendung, die dieses ViewModel verwendet.
 */
class UserViewModel(application: Application) : AndroidViewModel(application) {

    private val firebaseRepository = FirebaseRepository()

    var profileRef: DocumentReference? = firebaseRepository.profileRef
    var partnerRef: DocumentReference? = firebaseRepository.partnerRef
    var eventRef: DocumentReference? = firebaseRepository.eventRef
    val profileData = firebaseRepository.profileData
    val partnerData = firebaseRepository.partnerData

    /**
     * Aktualisiert das Benutzerprofil in Firebase.
     * @param profile Das zu aktualisierende Benutzerprofil.
     */
    fun firebaseUpdateProfile(profile: GlobalProfile) {
        firebaseRepository.firebaseUpdateProfile(profile)
    }

    /**
     * Aktualisiert ein bestimmtes Feld des Benutzerprofils in Firebase.
     * @param field Das zu aktualisierende Feld.
     * @param value Der neue Wert des Feldes.
     */
    fun firebaseUpdatePartialProfile(field: String, value: Any) {
        firebaseRepository.firebaseUpdatePartialProfile(field, value)
    }

    /**
     * Lädt ein Profilbild in Firebase hoch.
     * @param uri Die URI des Bildes.
     */
    fun firebaseUploadProfileImage(uri: Uri) {
        firebaseRepository.firebaseUploadProfileImage(uri)
    }

    /**
     * Ruft das Benutzerprofil aus Firebase ab.
     * @return Das Benutzerprofil.
     */
    fun getProfile(): GlobalProfile? {
        return firebaseRepository.getProfile()
    }

    /**
     * Holt die Profildaten aus Firebase.
     */
    fun fetchProfileData() {
        firebaseRepository.fetchProfileData()
    }

    /**
     * Aktualisiert das Partnerprofil in Firebase.
     * @param partnerProfile Das zu aktualisierende Partnerprofil.
     */
    fun firebaseUpdatePartnerProfile(partnerProfile: GlobalPartnerProfile) {
        firebaseRepository.firebaseUpdatePartnerProfile(partnerProfile)
    }

    /**
     * Lädt ein Partnerbild in Firebase hoch.
     * @param uri Die URI des Bildes.
     */
    fun firebaseUploadPartnerImage(uri: Uri) {
        firebaseRepository.firebaseUploadPartnerImage(uri)
    }

    /**
     * Holt die Partnerprofildaten aus Firebase.
     */
    fun fetchPartnerProfileData() {
        firebaseRepository.fetchPartnerProfileData()
    }

    /**
     * Aktualisiert das Eventprofil in Firebase.
     * @param eventProfile Das zu aktualisierende Eventprofil.
     */
    fun firebaseUpdateEventProfile(eventProfile: GlobalEventProfile) {
        firebaseRepository.firebaseUpdateEventProfile(eventProfile)
    }

    /**
     * Aktualisiert ein bestimmtes Feld des Eventprofils in Firebase.
     * @param field Das zu aktualisierende Feld.
     * @param value Der neue Wert des Feldes.
     */
    fun firebaseUpdatePartialEventProfile(field: String, value: Any) {
        firebaseRepository.firebaseUpdatePartialEventProfile(field, value)
    }

    /**
     * Lädt ein Eventbild in Firebase hoch.
     * @param uri Die URI des Bildes.
     */
    fun firebaseUploadEventImage(uri: Uri) {
        firebaseRepository.firebaseUploadEventImage(uri)
    }

    /**
     * Holt die Eventprofildaten aus Firebase.
     */
    fun fetchEventProfileData() {
        firebaseRepository.fetchEventProfileData()
    }
}