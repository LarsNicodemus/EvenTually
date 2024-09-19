package com.example.abschlussprojekt.data

import android.net.Uri
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.abschlussprojekt.datamodel.remotemodels.firebasefirestore.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class FirebaseRepository {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseStorage = FirebaseStorage.getInstance()
    private val firebaseFireStore = FirebaseFirestore.getInstance()

    private var _currentUser = MutableLiveData<FirebaseUser?>(firebaseAuth.currentUser)
    val currentUser: LiveData<FirebaseUser?> get() = _currentUser

    var profileRef: DocumentReference? = null
    var partnerRef: DocumentReference? = null
    private var eventRef: DocumentReference? = null
    var reminderRef: DocumentReference? = null
    var darkModeRef: DocumentReference? = null
    var recsRef: DocumentReference? = null

    private val _profileData = MutableLiveData<GlobalProfile?>()
    val profileData: LiveData<GlobalProfile?> get() = _profileData

    private val _partnerData = MutableLiveData<GlobalPartnerProfile?>()
    val partnerData: LiveData<GlobalPartnerProfile?> get() = _partnerData

    private val _eventData = MutableLiveData<GlobalEventProfile?>()
    val eventData: LiveData<GlobalEventProfile?> get() = _eventData

    private val _reminderData = MutableLiveData<GlobalReminder?>()
    val reminderData: LiveData<GlobalReminder?> get() = _reminderData

    private val _darkModeData = MutableLiveData<GlobalDarkMode?>()
    val darkModeData: LiveData<GlobalDarkMode?> get() = _darkModeData

    private val _recsData = MutableLiveData<GlobalRecommendation?>()
    val recsData: LiveData<GlobalRecommendation?> get() = _recsData

    init {
        if (firebaseAuth.currentUser != null) {
            firebaseSetUpUserEnv()
            firebaseSetUpPartnerEnv()
            firebaseSetUpEventEnv()
            firebaseSetUpReminderEnv()
            firebaseSetUpDarkModeEnv()
            firebaseSetUpRecsEnv()
        }
    }

    fun firebaseLogin(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d("MainViewModel", "Firebase Login successfully")
                    firebaseSetUpUserEnv()
                } else {
                    Log.d("MainViewModel", "Firebase Login failed")
                }
            }
    }

    fun firebaseRegister(email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d("MainViewModel", "Firebase Register successfully")
                    firebaseSetUpUserEnv()
                    firebaseSetUpNewProfile()
                    firebaseSetUpPartnerEnv()
                    firebaseSetUpNewPartnerProfile()
                    firebaseSetUpEventEnv()
                    firebaseSetupNewEventProfile()
                    firebaseSetUpReminderEnv()
                    firebaseSetupNewReminder()
                    firebaseSetUpDarkModeEnv()
                    firebaseSetupNewDarkMode()
                    firebaseSetUpRecsEnv()
                    firebaseSetupRecs()
                } else {
                    Log.d("MainViewModel", "Firebase Register failed")
                }
            }
    }

    private fun firebaseSetUpUserEnv() {
        _currentUser.value = firebaseAuth.currentUser
        profileRef = firebaseFireStore.collection("profile").document(firebaseAuth.currentUser?.uid!!)
    }

    private fun firebaseSetUpNewProfile() {
        profileRef?.set(GlobalProfile())
            ?.addOnSuccessListener {
                Log.d("Firestore", "DocumentSnapshot successfully written!")
            }
            ?.addOnFailureListener { e ->
                Log.w("Firestore", "Error writing document", e)
            }
    }

    fun firebaseLogout() {
        firebaseAuth.signOut()
        _currentUser.value = null
        _profileData.value = null
        _partnerData.value = null
        _eventData.value = null
        _reminderData.value = null
        _darkModeData.value = null
        _recsData.value = null
        Log.d("MainViewModel", "Firebase Logout successfully")
    }

    fun firebaseUpdateProfile(profile: GlobalProfile) {
        profileRef?.set(profile)
            ?.addOnSuccessListener {
                Log.d("Firestore", "DocumentSnapshot successfully written!")
            }
            ?.addOnFailureListener { e ->
                Log.w("Firestore", "Error writing document", e)
            }
    }

    fun firebaseUpdatePartialProfile(field: String, value: Any) {
        profileRef?.update(field, value)
            ?.addOnSuccessListener {
                Log.d("Firestore", "DocumentSnapshot successfully updated!")
            }
            ?.addOnFailureListener { e ->
                Log.w("Firestore", "Error updating document", e)
            }
    }

    fun firebaseResetPassword(email: String) {
        firebaseAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d("MainViewModel", "Password reset email sent.")
                } else {
                    Log.d("MainViewModel", "Failed to send password reset email.")
                }
            }
    }

    fun firebaseDeleteUser() {
        firebaseAuth.currentUser?.delete()
            ?.addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d("MainViewModel", "User account deleted.")
                } else {
                    Log.d("MainViewModel", "Failed to delete user account.")
                }
            }
    }

    fun firebaseUploadProfileImage(uri: Uri) {
        val storageRef = firebaseStorage.reference.child("profileImages/${firebaseAuth.currentUser?.uid}")
        storageRef.putFile(uri)
            .addOnSuccessListener {
                Log.d("Storage", "Profile image uploaded successfully.")
                setImageProfile(uri)
            }
            .addOnFailureListener { e ->
                Log.w("Storage", "Error uploading profile image", e)
            }
    }

    private fun setImageProfile(uri: Uri) {
        profileRef?.update("profileImageUri", uri.toString())
            ?.addOnSuccessListener {
                Log.d("Firestore", "Profile image URI updated successfully.")
            }
            ?.addOnFailureListener { e ->
                Log.w("Firestore", "Error updating profile image URI", e)
            }
    }

    fun getProfile(): GlobalProfile? {
        return _profileData.value
    }

    fun fetchProfileData() {
        profileRef?.get()?.addOnSuccessListener { snapShot ->
            if (snapShot != null && snapShot.exists()) {
                _profileData.value = snapShot.toObject(GlobalProfile::class.java)
            }
        }?.addOnFailureListener { exception ->
            Log.d("FirebaseRepository", "get failed with ", exception)
        }
    }

    private fun firebaseSetUpPartnerEnv() {
        _currentUser.value = firebaseAuth.currentUser
        partnerRef = firebaseFireStore.collection("partner").document(firebaseAuth.currentUser?.uid!!)
    }

    private fun firebaseSetUpNewPartnerProfile() {
        partnerRef?.set(GlobalPartnerProfile())
            ?.addOnSuccessListener {
                Log.d("Firestore", "DocumentSnapshot successfully written!")
            }
            ?.addOnFailureListener { e ->
                Log.w("Firestore", "Error writing document", e)
            }
    }

    fun firebaseUpdatePartnerProfile(partnerProfile: GlobalPartnerProfile) {
        partnerRef?.set(partnerProfile)
            ?.addOnSuccessListener {
                Log.d("Firestore", "DocumentSnapshot successfully written!")
            }
            ?.addOnFailureListener { e ->
                Log.w("Firestore", "Error writing document", e)
            }
    }

    fun firebaseUploadPartnerImage(uri: Uri) {
        val storageRef = firebaseStorage.reference.child("partnerImages/${firebaseAuth.currentUser?.uid}")
        storageRef.putFile(uri)
            .addOnSuccessListener {
                Log.d("Storage", "Partner image uploaded successfully.")
                setImagePartner(uri)
            }
            .addOnFailureListener { e ->
                Log.w("Storage", "Error uploading partner image", e)
            }
    }

    private fun setImagePartner(uri: Uri) {
        partnerRef?.update("partnerImageUri", uri.toString())
            ?.addOnSuccessListener {
                Log.d("Firestore", "Partner image URI updated successfully.")
            }
            ?.addOnFailureListener { e ->
                Log.w("Firestore", "Error updating partner image URI", e)
            }
    }

    fun fetchPartnerProfileData() {
        partnerRef?.get()?.addOnSuccessListener { snapShot ->
            if (snapShot != null && snapShot.exists()) {
                _partnerData.value = snapShot.toObject(GlobalPartnerProfile::class.java)
            }
        }?.addOnFailureListener { exception ->
            Log.d("FirebaseRepository", "get failed with ", exception)
        }
    }

    private fun firebaseSetUpEventEnv() {
        _currentUser.value = firebaseAuth.currentUser
        eventRef = firebaseFireStore.collection("event").document(firebaseAuth.currentUser?.uid!!)
    }

    private fun firebaseSetupNewEventProfile() {
        eventRef?.set(GlobalEventProfile())
            ?.addOnSuccessListener {
                Log.d("Firestore", "DocumentSnapshot successfully written!")
            }
            ?.addOnFailureListener { e ->
                Log.w("Firestore", "Error writing document", e)
            }
    }

    fun firebaseUpdateEventProfile(eventProfile: GlobalEventProfile) {
        eventRef?.set(eventProfile)
            ?.addOnSuccessListener {
                Log.d("Firestore", "DocumentSnapshot successfully written!")
            }
            ?.addOnFailureListener { e ->
                Log.w("Firestore", "Error writing document", e)
            }
    }

    fun firebaseUpdatePartialEventProfile(field: String, value: Any) {
        eventRef?.update(field, value)
            ?.addOnSuccessListener {
                Log.d("Firestore", "DocumentSnapshot successfully updated!")
            }
            ?.addOnFailureListener { e ->
                Log.w("Firestore", "Error updating document", e)
            }
    }

    fun firebaseUploadEventImage(uri: Uri) {
        val storageRef = firebaseStorage.reference.child("eventImages/${firebaseAuth.currentUser?.uid}")
        storageRef.putFile(uri)
            .addOnSuccessListener {
                Log.d("Storage", "Event image uploaded successfully.")
                setImageEvent(uri)
            }
            .addOnFailureListener { e ->
                Log.w("Storage", "Error uploading event image", e)
            }
    }

    private fun setImageEvent(uri: Uri) {
        eventRef?.update("eventImageUri", uri.toString())
            ?.addOnSuccessListener {
                Log.d("Firestore", "Event image URI updated successfully.")
            }
            ?.addOnFailureListener { e ->
                Log.w("Firestore", "Error updating event image URI", e)
            }
    }

    fun fetchEventProfileData() {
        eventRef?.get()?.addOnSuccessListener { snapShot ->
            if (snapShot != null && snapShot.exists()) {
                _eventData.value = snapShot.toObject(GlobalEventProfile::class.java)
            }
        }?.addOnFailureListener { exception ->
            Log.d("FirebaseRepository", "get failed with ", exception)
        }
    }

    private fun firebaseSetUpReminderEnv() {
        _currentUser.value = firebaseAuth.currentUser
        reminderRef = firebaseFireStore.collection("reminder").document(firebaseAuth.currentUser?.uid!!)
    }

    private fun firebaseSetupNewReminder() {
        reminderRef?.set(GlobalReminder())
            ?.addOnSuccessListener {
                Log.d("Firestore", "DocumentSnapshot successfully written!")
            }
            ?.addOnFailureListener { e ->
                Log.w("Firestore", "Error writing document", e)
            }
    }

    fun firebaseUpdateReminder(reminder: GlobalReminder) {
        reminderRef?.set(reminder)
            ?.addOnSuccessListener {
                Log.d("Firestore", "DocumentSnapshot successfully written!")
            }
            ?.addOnFailureListener { e ->
                Log.w("Firestore", "Error writing document", e)
            }
    }

    fun firebaseUpdatePartialReminder(field: String, value: Any) {
        reminderRef?.update(field, value)
            ?.addOnSuccessListener {
                Log.d("Firestore", "DocumentSnapshot successfully updated!")
            }
            ?.addOnFailureListener { e ->
                Log.w("Firestore", "Error updating document", e)
            }
    }

    fun fetchReminderData() {
        reminderRef?.get()?.addOnSuccessListener { snapShot ->
            if (snapShot != null && snapShot.exists()) {
                _reminderData.value = snapShot.toObject(GlobalReminder::class.java)
            }
        }?.addOnFailureListener { exception ->
            Log.d("FirebaseRepository", "get failed with ", exception)
        }
    }

    private fun firebaseSetUpDarkModeEnv() {
        _currentUser.value = firebaseAuth.currentUser
        darkModeRef = firebaseFireStore.collection("darkMode").document(firebaseAuth.currentUser?.uid!!)
    }

    private fun firebaseSetupNewDarkMode() {
        darkModeRef?.set(GlobalDarkMode())
            ?.addOnSuccessListener {
                Log.d("Firestore", "DocumentSnapshot successfully written!")
            }
            ?.addOnFailureListener { e ->
                Log.w("Firestore", "Error writing document", e)
            }
    }

    fun firebaseUpdateDarkMode(darkMode: GlobalDarkMode) {
        darkModeRef?.set(darkMode)
            ?.addOnSuccessListener {
                Log.d("Firestore", "DocumentSnapshot successfully written!")
            }
            ?.addOnFailureListener { e ->
                Log.w("Firestore", "Error writing document", e)
            }
    }

    fun firebaseUpdatePartialDarkMode(field: String, value: Any) {
        darkModeRef?.update(field, value)
            ?.addOnSuccessListener {
                Log.d("Firestore", "DocumentSnapshot successfully updated!")
            }
            ?.addOnFailureListener { e ->
                Log.w("Firestore", "Error updating document", e)
            }
    }

    fun fetchDarkModeData() {
        darkModeRef?.get()?.addOnSuccessListener { snapShot ->
            if (snapShot != null && snapShot.exists()) {
                _darkModeData.value = snapShot.toObject(GlobalDarkMode::class.java)
            }
        }?.addOnFailureListener { exception ->
            Log.d("FirebaseRepository", "get failed with ", exception)
        }
    }

    fun setDarkMode(isEnabled: Boolean) {
        if (isEnabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    private fun firebaseSetUpRecsEnv() {
        _currentUser.value = firebaseAuth.currentUser
        recsRef = firebaseFireStore.collection("recommendations").document(firebaseAuth.currentUser?.uid!!)
    }

    private fun firebaseSetupRecs() {
        recsRef?.set(GlobalRecommendation())
            ?.addOnSuccessListener {
                Log.d("Firestore", "DocumentSnapshot successfully written!")
            }
            ?.addOnFailureListener { e ->
                Log.w("Firestore", "Error writing document", e)
            }
    }

    fun firebaseUpdateRecs(recs: GlobalRecommendation) {
        recsRef?.set(recs)
            ?.addOnSuccessListener {
                Log.d("Firestore", "DocumentSnapshot successfully written!")
            }
            ?.addOnFailureListener { e ->
                Log.w("Firestore", "Error writing document", e)
            }
    }

    fun firebaseUpdatePartialRecs(field: String, value: Any) {
        recsRef?.update(field, value)
            ?.addOnSuccessListener {
                Log.d("Firestore", "DocumentSnapshot successfully updated!")
            }
            ?.addOnFailureListener { e ->
                Log.w("Firestore", "Error updating document", e)
            }
    }

    fun fetchRecsData() {
        recsRef?.get()?.addOnSuccessListener { snapShot ->
            if (snapShot != null && snapShot.exists()) {
                val recs = snapShot.toObject(GlobalRecommendation::class.java)
                _recsData.value = recs
            } else {
                Log.d("MainViewModel", "No such snapShot")
            }
        }?.addOnFailureListener { exception ->
            Log.d("FirebaseRepository", "get failed with ", exception)
        }
    }
}