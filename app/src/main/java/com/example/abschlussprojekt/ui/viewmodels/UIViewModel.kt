package com.example.abschlussprojekt.ui.viewmodels

import android.app.Application
import android.graphics.Rect
import android.view.View
import android.view.Window
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.example.abschlussprojekt.R
import com.example.abschlussprojekt.data.FirebaseRepository
import com.example.abschlussprojekt.data.local.Settings
import com.example.abschlussprojekt.datamodel.localmodels.Setting
import com.example.abschlussprojekt.datamodel.remotemodels.firebasefirestore.GlobalDarkMode
import com.example.abschlussprojekt.ui.settings.SettingsEventFragment
import com.example.abschlussprojekt.ui.settings.SettingsPartnerFragment
import com.example.abschlussprojekt.ui.settings.SettingsProfileFragment
import com.google.firebase.firestore.DocumentReference

class UIViewModel(application: Application) : AndroidViewModel(application) {


    private val firebaseRepository = FirebaseRepository()

    private val _bottomNavVisibility = MutableLiveData(View.VISIBLE)
    val bottomNavVisibility: LiveData<Int> = _bottomNavVisibility
    private val _isKeyboardVisible = MutableLiveData(false)
    val isKeyboardVisible: LiveData<Boolean> = _isKeyboardVisible
    private val _settings = MutableLiveData<List<Setting>>()
    val settings: LiveData<List<Setting>> = _settings

    val darkMode = firebaseRepository.darkModeData
    var darkModeRef: DocumentReference? = firebaseRepository.darkModeRef

    init {
        _settings.value = Settings.settings
    }


    /**
     * Setzt den Dunkelmodus.
     * @param isEnabled True, wenn der Dunkelmodus aktiviert werden soll, sonst False.
     */
    fun setDarkMode(isEnabled: Boolean) {
        firebaseRepository.setDarkMode(isEnabled)
    }

    /**
     * Aktualisiert den Dunkelmodus in Firebase.
     * @param darkMode Die neuen Dunkelmodus-Einstellungen.
     */
    fun firebaseUpdateDarkMode(darkMode: GlobalDarkMode) {
        firebaseRepository.firebaseUpdateDarkMode(darkMode)
    }

    /**
     * Aktualisiert ein bestimmtes Feld des Dunkelmodus in Firebase.
     * @param field Das zu aktualisierende Feld.
     * @param value Der neue Wert des Feldes.
     */
    fun firebaseUpdatePartialDarkMode(field: String, value: Any) {
        firebaseRepository.firebaseUpdatePartialDarkMode(field, value)
    }

    /**
     * Holt die Dunkelmodus-Daten aus Firebase.
     */
    fun fetchDarkModeData() {
        firebaseRepository.fetchDarkModeData()
    }

    /**
     * Aktualisiert die Sichtbarkeit der unteren Navigation basierend auf der Ziel-ID.
     * @param destinationId Die Ziel-ID.
     */
    fun updateBottomNavVisibility(destinationId: Int) {
        _bottomNavVisibility.value = when (destinationId) {
            R.id.setupLoginFragment,
            R.id.setupRegisterFragment,
            R.id.editProfileFragment,
            R.id.editPartnerFragment,
            R.id.editEventFragment,
            R.id.addEventFragment,
            R.id.addPartnerFragment,
            R.id.addProfileFragment,
            R.id.settingsProfileFragment,
            R.id.settingsPartnerFragment,
            R.id.settingsEventFragment,
            R.id.settingsRecsFragment,
            R.id.settingsReminderFragment -> View.GONE

            else -> View.VISIBLE
        }
    }

    /**
     * Fügt einen Listener zur Überwachung der Tastatursichtbarkeit hinzu.
     * @param window Das Fenster der Anwendung.
     */
    fun addKeyboardVisibilityListener(window: Window) {
        val rootView = window.decorView.findViewById<View>(android.R.id.content)
        var wasOpened = false
        rootView.viewTreeObserver.addOnGlobalLayoutListener {
            val r = Rect()
            rootView.getWindowVisibleDisplayFrame(r)
            val screenHeight = rootView.rootView.height

            val keyboardHeight = screenHeight - r.bottom

            if (keyboardHeight > screenHeight * 0.15) {
                if (!wasOpened) {
                    wasOpened = true
                    _isKeyboardVisible.value = true
                }
            } else {
                if (wasOpened) {
                    wasOpened = false
                    _isKeyboardVisible.value = false
                }
            }
        }
    }

    /**
     * Richtet die Toolbar für ein Fragment ein.
     * @param fragment Das Fragment.
     * @param view Die View des Fragments.
     * @param title Der Titel der Toolbar.
     */
    fun setUpToolbar(fragment: Fragment, view: View, title: String) {
        val toolbar: Toolbar = view.findViewById(R.id.toolbar)
        val toolbarTitle: TextView = toolbar.findViewById(R.id.toolbar_title)
        toolbarTitle.text = title
        val backButton: ImageButton = toolbar.findViewById(R.id.toolbar_back_btn)
        backButton.setOnClickListener {
            if (fragment is SettingsProfileFragment
                || fragment is SettingsPartnerFragment
                || fragment is SettingsEventFragment
            ) {
                fragment.findNavController().navigate(R.id.settingsFragment)
            } else {
                fragment.findNavController().navigateUp()
            }
        }
    }
}