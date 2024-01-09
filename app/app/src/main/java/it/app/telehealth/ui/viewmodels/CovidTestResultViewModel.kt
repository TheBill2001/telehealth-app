package it.app.telehealth.ui.viewmodels

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.app.telehealth.client.TeleHealthAPI
import it.app.telehealth.client.models.CovidTestResult
import it.app.telehealth.client.models.NewCovidTestResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CovidTestResultViewModel : ViewModel() {
    private val _testResults = MutableStateFlow<List<CovidTestResult>>(listOf())
    val testResults: StateFlow<List<CovidTestResult>> = _testResults.asStateFlow()

    fun fetchUserTestResult(context: Context) {
        viewModelScope.launch {
            try {
                _testResults.value = TeleHealthAPI.covidTestService.getUserCovidTests()
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show()
            }
        }
    }

    fun addNewTestResult(positive: Boolean, context: Context) {
        viewModelScope.launch {
            try {
                TeleHealthAPI.covidTestService.addNewTestResult(NewCovidTestResult(positive))
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show()
            }
        }
    }
}