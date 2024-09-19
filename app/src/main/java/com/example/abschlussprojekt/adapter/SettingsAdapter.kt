package com.example.abschlussprojekt.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.abschlussprojekt.databinding.ItemSettingsBinding
import com.example.abschlussprojekt.datamodel.localmodels.Setting
import com.example.abschlussprojekt.ui.viewmodels.UIViewModel
import com.example.abschlussprojekt.utils.OnSettingClickListener

class SettingsAdapter(
    private val settings: MutableList<Setting>,
    private val uiViewModel: UIViewModel,
    private val listener: OnSettingClickListener
) : RecyclerView.Adapter<SettingsAdapter.SettingViewHolder>() {
    inner class SettingViewHolder(val binding: ItemSettingsBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SettingViewHolder {
        val binding =
            ItemSettingsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SettingViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return settings.size
    }

    override fun onBindViewHolder(holder: SettingViewHolder, position: Int) {
        val setting = settings[position]
        val binding = holder.binding
        binding.apply {
            if (setting == settings[5]) {
                tvTitleSettings.setText(setting.text)
                icSettings.setImageResource(setting.icon)
                swDarkmode.visibility = android.view.View.VISIBLE
                swDarkmode.isChecked = uiViewModel.darkMode.value?.darkModeActivated == true
                swDarkmode.setOnCheckedChangeListener(null)
                swDarkmode.setOnCheckedChangeListener { _, isChecked ->
                    uiViewModel.darkMode.value?.darkModeActivated = isChecked
                    uiViewModel.setDarkMode(isChecked)
                }
            } else {
                tvTitleSettings.setText(setting.text)
                icSettings.setImageResource(setting.icon)
                cvSettings.setOnClickListener {
                    listener.onSettingClicked(position)
                }
            }
        }
    }
    /**
     * Aktualisiert die Liste der Einstellungen.
     *
     * @param newSettings Die neue Liste der Einstellungen.
     */
    fun updateSettings(newSettings: List<Setting>) {
        settings.clear()
        settings.addAll(newSettings)
        notifyDataSetChanged()
    }
}