package it.app.telehealth.ui.viewmodels

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.app.telehealth.client.TeleHealthAPI
import it.app.telehealth.client.models.NewVaccineRegistration
import it.app.telehealth.client.models.VaccinationRegistration
import it.app.telehealth.client.models.VaccineType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class VaccinationRegistrationListViewModel : ViewModel() {

    private val _vaccinationRegistrationList =
        MutableStateFlow<List<VaccinationRegistration>>(listOf())
    val vaccinationRegistrationList: StateFlow<List<VaccinationRegistration>> =
        _vaccinationRegistrationList.asStateFlow()

    fun fetchVaccinationRegistrationList(context: Context) {
        viewModelScope.launch {
            try {
                _vaccinationRegistrationList.value =
                    TeleHealthAPI.vaccinationService.getVaccineRegistration()
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show()
            }
        }
    }

    fun addVaccinationRegistration(
        vaccineName: String,
        type: VaccineType,
        facility: String,
        context: Context
    ) {
        viewModelScope.launch {
            try {
                TeleHealthAPI.vaccinationService.addNewRegistration(
                    NewVaccineRegistration(
                        vaccineName,
                        type,
                        facility
                    )
                )
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show()
            }
        }
    }

    fun cancelVaccinationRegistration(id: String, context: Context) {
        viewModelScope.launch {
            try {
                TeleHealthAPI.vaccinationService.cancelRegistrationById(id)
                fetchVaccinationRegistrationList(context)
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show()
            }
        }
    }
}