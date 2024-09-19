package com.example.abschlussprojekt.ui.main

import android.Manifest
import android.animation.ObjectAnimator
import android.content.pm.PackageManager
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.abschlussprojekt.R
import com.example.abschlussprojekt.databinding.FragmentHomeBinding
import com.example.abschlussprojekt.datamodel.localmodels.DateCalculator
import com.example.abschlussprojekt.datamodel.remotemodels.firebasefirestore.GlobalEventProfile
import com.example.abschlussprojekt.datamodel.remotemodels.weather.WeatherResponse
import com.example.abschlussprojekt.ui.viewmodels.UserViewModel
import com.example.abschlussprojekt.ui.viewmodels.NotificationViewModel
import com.example.abschlussprojekt.ui.viewmodels.AuthViewModel
import com.example.abschlussprojekt.ui.viewmodels.HomeViewModel
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val authViewModel: AuthViewModel by activityViewModels()
    private val userViewModel: UserViewModel by activityViewModels()
    private val notificationViewModel: NotificationViewModel by activityViewModels()
    private val homeViewModel: HomeViewModel by activityViewModels()
    private var eventListenerRegistration: ListenerRegistration? = null
    private lateinit var dateCalculator: DateCalculator
    private var remainingDays: Int = 0

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            homeViewModel.startLocationUpdates()
        } else {
            Toast.makeText(
                requireContext(),
                "Standortberechtigung wurde verweigert. Einige Funktionen der App werden möglicherweise nicht korrekt funktionieren.",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        observeCurrentUser()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dateCalculator = DateCalculator()
        initializeUIComponents()
        notificationViewModel.requestBatteryOptimizationExemption(requireContext())
        notificationViewModel.checkNotificationPermission(requireContext())
    }


    /**
     * Beobachtet den aktuellen Benutzer und navigiert zur Login-Seite, wenn kein Benutzer angemeldet ist.
     */
    private fun observeCurrentUser() {
        authViewModel.currentUser.observe(viewLifecycleOwner) { user ->
            if (user == null) {
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToSetupLoginFragment())
                Log.d("HomeFragment", "User is null")
            }
            Log.d("HomeFragment", "User is ${user?.email} ${user?.uid} ")
        }
    }

    /**
     * Initialisiert die UI-Komponenten.
     */
    private fun initializeUIComponents() {
        setupCountdownUI()
        checkLocationPermission()
        setupLoveFactsUI()
        setupNewsUI()
        setupWeatherUI()
    }

    /**
     * Überprüft die Standortberechtigung und fordert sie an, falls sie nicht erteilt wurde.
     */
    private fun checkLocationPermission() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                homeViewModel.startLocationUpdates()
            }

            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                locationPermissionRequest.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }

            else -> {
                locationPermissionRequest.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }

    /**
     * Initialisiert die UI-Komponenten für Liebesfakten.
     */
    private fun setupLoveFactsUI() {
        homeViewModel.fetchNewRandomLoveFact()
        homeViewModel.randomLoveFact.observe(viewLifecycleOwner) { fact ->
            binding.tvLoveFacts.text = fact.fact
        }
        binding.ibRenewLoveFacts.setOnClickListener {
            homeViewModel.fetchNewRandomLoveFact()
        }
    }

    /**
     * Initialisiert die UI-Komponenten für Nachrichten.
     */
    private fun setupNewsUI() {
        homeViewModel.getNews()
        homeViewModel.news.observe(viewLifecycleOwner) { newsList ->
            if (newsList.isNotEmpty()) {
                val randomArticle = newsList.random()
                binding.tvNewsTitle.text = randomArticle.title
                binding.tvLoveNews.text = randomArticle.description
            }
        }
        binding.ibRenewLoveNews.setOnClickListener {
            homeViewModel.getNews()
        }
    }

    /**
     * Initialisiert die UI-Komponenten für den Countdown.
     */
    private fun setupCountdownUI() {
        setupCountdownTimer()
        val textViews = listOf(
            binding.itemAnniversary.tvTitle1,
            binding.itemAnniversary.tvTitle2,
            binding.itemAnniversary.tvTitle3,
            binding.itemAnniversary.tvCountdown1,
            binding.itemAnniversary.tvCountdown2,
            binding.itemAnniversary.tvCountdown3,
            binding.itemAnniversary.tvTimer1,
            binding.itemAnniversary.tvTimer2,
            binding.itemAnniversary.tvTimer3
        )

        textViews.forEach { textView ->
            if (textView in listOf(
                    binding.itemAnniversary.tvCountdown1,
                    binding.itemAnniversary.tvTimer1,
                    binding.itemAnniversary.tvTitle1
                )
            ) {
                setupTextViewStyle(textView)
            }
        }
    }

    /**
     * Initialisiert den Countdown-Timer.
     */
    private fun setupCountdownTimer() {
        eventListenerRegistration = userViewModel.eventRef?.addSnapshotListener { snapshot, error ->
            if (error == null && snapshot != null) {
                val eventProfile = snapshot.toObject(
                    GlobalEventProfile::class.java
                )
                eventProfile?.startDate?.let { eventDateString ->
                    notificationViewModel.updateEventDateString(eventDateString)
                    remainingDays = dateCalculator.daysUntilNextAnniversary(eventDateString)
                    startCountdownTimer(eventDateString)
                }
                binding.ivEventAdd.load(eventProfile?.imageString)
                binding.itemAnniversary.tvWeather.text = getString(R.string.weather_home_fragment,eventProfile?.nextAnniversary).replace("/",".")
            }
        }
    }

    /**
     * Startet den Countdown-Timer.
     * @param eventDateString Das Datum des Events als String.
     */
    private fun startCountdownTimer(eventDateString: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            while (true) {
                dateCalculator.calculateAndUpdate(eventDateString) { countdown, timeSinceEvent ->
                    updateTimeSinceEventUI(timeSinceEvent)
                    binding.itemAnniversary.tvTimer1.text = countdown
                    binding.itemAnniversary.tvTimer2.text = countdown
                    binding.itemAnniversary.tvTimer3.text = countdown
                }
                delay(1000) // Aktualisiere jede Sekunde
            }
        }
    }

    /**
     * Aktualisiert die UI mit der Zeit seit dem Event.
     * @param timeSinceEvent Die Zeit seit dem Event als String.
     */
    private fun updateTimeSinceEventUI(timeSinceEvent: String) {
        val components = timeSinceEvent.split(", ")
        for (component in components) {
            val parts = component.trim().split(" ")
            when {
                parts[1].startsWith("Jahr") -> binding.tvYears.text = "${parts[0]} Jahre"
                parts[1].startsWith("Monat") -> binding.tvMonths.text = "${parts[0]} Monate"
                parts[1].startsWith("Tag") -> binding.tvDays.text = "${parts[0]} Tage"
                parts[1].startsWith("Stunde") -> binding.tvHours.text = "${parts[0]} Stunden"
                parts[1].startsWith("Minute") -> binding.tvMinutes.text = "${parts[0]} Minuten"
            }
        }
    }

    /**
     * Setzt den Stil für TextViews.
     * @param textView Die TextView, deren Stil gesetzt werden soll.
     */
    private fun setupTextViewStyle(textView: TextView) {
        textView.paint.style = Paint.Style.FILL_AND_STROKE
        textView.paint.strokeWidth = 1f
        alphaAnimator(textView)
    }

    /**
     * Initialisiert die UI-Komponenten für das Wetter.
     */
    private fun setupWeatherUI() {
        userViewModel.fetchProfileData()
        userViewModel.profileData.observe(viewLifecycleOwner) { profile ->
            if (remainingDays <= 15 && profile?.location != null) {
                observeEventDateStrings(profile.location.latitude, profile.location.longitude)
            } else {
                hideWeatherUI()
            }
        }
    }

    /**
     * Beobachtet die Event-Daten und lädt die Wetterdaten.
     * @param latitude Die Breite des Standorts.
     * @param longitude Die Länge des Standorts.
     */
    private fun observeEventDateStrings(latitude: Double, longitude: Double) {
        notificationViewModel.eventDateStrings.observe(viewLifecycleOwner) { eventDateStrings ->
            eventDateStrings?.let {
                val nextAnniversaryDate = dateCalculator.getNextAnniversaryDate(it)
                Log.d("HomeFragment", "nextAnniversaryDate: $nextAnniversaryDate")
                nextAnniversaryDate?.let { date ->
                    homeViewModel.loadWeatherData(
                        latitude,
                        longitude,
                        dateCalculator.convertDateFormat(date)
                    )
                }
            }
        }
        homeViewModel.weatherData.observe(viewLifecycleOwner) { weather ->
            displayWeatherData(weather)
        }
    }

    /**
     * Zeigt die Wetterdaten an.
     * @param weather Die Wetterdaten.
     */
    private fun displayWeatherData(weather: WeatherResponse) {
        binding.itemAnniversary.tvMinTemp.text =
            getString(R.string.min, weather.daily.temperatureMin.first())
        binding.itemAnniversary.tvMaxTemp.text =
            getString(R.string.max, weather.daily.temperatureMax.first())
        binding.itemAnniversary.tvRainPercent.text =
            getString(R.string.rain_percent, weather.daily.precipitationProbabilityMax.first())
    }

    /**
     * Versteckt die Wetter-UI-Komponenten.
     */
    private fun hideWeatherUI() {
        binding.itemAnniversary.tvMinTemp.visibility = View.GONE
        binding.itemAnniversary.tvWeather.visibility = View.GONE
        binding.itemAnniversary.tvMaxTemp.visibility = View.GONE
        binding.itemAnniversary.tvRainPercent.visibility = View.GONE
    }

    /**
     * Animiert die Alpha-Eigenschaft einer TextView.
     * @param textView Die TextView, die animiert werden soll.
     */
    private fun alphaAnimator(textView: TextView) {
        val alphaAnimator = ObjectAnimator.ofFloat(textView, "alpha", 0.7f, 1f)
        alphaAnimator.duration = 1000
        alphaAnimator.repeatMode = ObjectAnimator.REVERSE
        alphaAnimator.repeatCount = ObjectAnimator.INFINITE
        alphaAnimator.start()
    }

    /**
     * Wird aufgerufen, wenn die View zerstört wird.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        eventListenerRegistration?.remove()
        homeViewModel.stopLocationUpdates()
    }
}
