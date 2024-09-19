package com.example.abschlussprojekt

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.abschlussprojekt.databinding.ActivityMainBinding
import com.example.abschlussprojekt.ui.viewmodels.UserViewModel
import com.example.abschlussprojekt.ui.viewmodels.HomeViewModel
import com.example.abschlussprojekt.ui.viewmodels.RecommendationViewModel
import com.example.abschlussprojekt.ui.viewmodels.NotificationViewModel
import com.example.abschlussprojekt.ui.viewmodels.UIViewModel
import com.example.abschlussprojekt.ui.viewmodels.AuthViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
//    private lateinit var authViewModel: AuthViewModel
//    private lateinit var homeViewModel: HomeViewModel
//    private lateinit var notificationViewModel: NotificationViewModel
//    private lateinit var recommendationViewModel: RecommendationViewModel
//    private lateinit var userViewModel: UserViewModel
    private lateinit var uiViewModel: UIViewModel
    private lateinit var bottomNav: BottomNavigationView
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivityUI()
    }

    /**
     * Initialisiert die Benutzeroberfläche der MainActivity.
     * Bindet die ViewModel-Instanzen und setzt den Inhalt der Aktivität.
     */
    private fun mainActivityUI() {
        binding = ActivityMainBinding.inflate(layoutInflater)
//        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]
//        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
//        notificationViewModel = ViewModelProvider(this)[NotificationViewModel::class.java]
//        recommendationViewModel = ViewModelProvider(this)[RecommendationViewModel::class.java]
//        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        uiViewModel = ViewModelProvider(this)[UIViewModel::class.java]
        setContentView(binding.root)
        bottomNav = binding.bottomNavigationView
        val navHost =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        binding.bottomNavigationView.setupWithNavController(navHost.navController)
        navController = navHost.navController
        keyboardAndBottomNavObserver()
        if (uiViewModel.darkModeRef != null) {
            darkModeObserver()
        }
        uiViewModel.addKeyboardVisibilityListener(this.window)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            uiViewModel.updateBottomNavVisibility(destination.id)
        }

    }

    /**
     * Beobachtet Änderungen im Dunkelmodus und aktualisiert die Benutzeroberfläche entsprechend.
     */
    private fun darkModeObserver() {
        uiViewModel.fetchDarkModeData()
        uiViewModel.darkMode.observe(this) { isDarkMode ->
            if (isDarkMode != null) {
                uiViewModel.setDarkMode(isDarkMode.darkModeActivated)
            }
        }
    }

    /**
     * Beobachtet die Sichtbarkeit der Tastatur und der unteren Navigationsleiste und aktualisiert die Benutzeroberfläche entsprechend.
     */
    private fun keyboardAndBottomNavObserver() {
        uiViewModel.bottomNavVisibility.observe(this) { visibility ->
            binding.bottomNavigationView.visibility = visibility
        }
        uiViewModel.isKeyboardVisible.observe(this) { isVisible ->
            if (isVisible) {
                bottomNav.visibility = View.GONE
            } else {
                uiViewModel.updateBottomNavVisibility(navController.currentDestination?.id ?: 0)
            }
        }
    }
}