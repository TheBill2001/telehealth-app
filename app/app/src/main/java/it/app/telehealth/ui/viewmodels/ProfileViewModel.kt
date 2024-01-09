package it.app.telehealth.ui.viewmodels

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.app.telehealth.R
import it.app.telehealth.client.TeleHealthAPI
import it.app.telehealth.client.models.UserProfile
import kotlinx.coroutines.launch
import retrofit2.HttpException

class ProfileViewModel : ViewModel() {

    private val _currentUserProfile = MutableLiveData<UserProfile>()
    val currentUserProfile: LiveData<UserProfile> = _currentUserProfile

    fun fetchCurrentUserProfile(context: Context) {
        viewModelScope.launch {
            try {
                val userProfile = TeleHealthAPI.profileService.getCurrentUserProfile()
                _currentUserProfile.value = userProfile
            } catch (e: HttpException) {
                if (e.code() == 401) {
                    Log.i("Profile", context.resources.getString(R.string.token_invalid))
                    Toast.makeText(
                        context,
                        context.resources.getString(R.string.token_invalid),
                        Toast.LENGTH_LONG
                    ).show()
                } else if (e.code() == 404) {
                    Log.i("Profile", context.resources.getString(R.string.user_profile_missing))
                    Toast.makeText(
                        context,
                        context.resources.getString(R.string.user_profile_missing),
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Log.i("Profile", e.toString())
                    Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Log.i("Profile", e.toString())
                Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show()
            }

        }
    }
}