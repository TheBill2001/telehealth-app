package it.app.telehealth.ui.viewmodels

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.app.telehealth.client.TeleHealthAPI
import it.app.telehealth.client.models.VaccinationHistory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class VaccinationHistoryViewModel : ViewModel() {
    private val _vaccinationHistory = MutableStateFlow<List<VaccinationHistory>>(listOf())
    val vaccinationHistory: StateFlow<List<VaccinationHistory>> = _vaccinationHistory.asStateFlow()

    fun fetchVaccinationHistory(context: Context) {
        viewModelScope.launch {
            try {
                _vaccinationHistory.value = TeleHealthAPI.vaccinationService.getVaccineHistory()
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show()
            }
        }
    }
}