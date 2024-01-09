package it.app.telehealth.ui.viewmodels

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.app.telehealth.client.TeleHealthAPI
import it.app.telehealth.client.models.Symptom
import it.app.telehealth.client.models.SymptomRequest
import kotlinx.coroutines.launch

class SymptomViewModel : ViewModel() {
    private val _userSymptoms = MutableLiveData<List<Symptom>>()
    val userSymptoms: LiveData<List<Symptom>> = _userSymptoms

    var selectedSymptomId: String? = null

    init {
        _userSymptoms.value = listOf()
    }

    fun fetchUserSymptoms(context: Context) {
        viewModelScope.launch {
            try {
                val userSymptom = TeleHealthAPI.symptomService.getUserSymptoms()
                _userSymptoms.value = userSymptom
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show()
            }
        }
    }

    fun deleteUserSymptoms(id: String, context: Context) {
        viewModelScope.launch {
            try {
                TeleHealthAPI.symptomService.deleteSymptomById(id)
                fetchUserSymptoms(context)
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show()
            }
        }
    }

    suspend fun getUserSymptomById(id: String, context: Context): Symptom? {
        try {
            return TeleHealthAPI.symptomService.getUserSymptomById(id)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show()
        }
        return null
    }

    suspend fun updateUserSymptomById(
        id: String,
        description: String,
        severity: Float,
        note: String?,
        context: Context
    ): Symptom? {
        try {
            return TeleHealthAPI.symptomService.updateSymptomById(
                id,
                SymptomRequest(description, severity, note)
            )
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show()
        }
        return null
    }

    suspend fun newUserSymptom(
        description: String,
        severity: Float,
        note: String?,
        context: Context
    ): Symptom? {
        try {
            return TeleHealthAPI.symptomService.addUserSymptom(
                SymptomRequest(description, severity, note)
            )
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show()
        }
        return null
    }
}