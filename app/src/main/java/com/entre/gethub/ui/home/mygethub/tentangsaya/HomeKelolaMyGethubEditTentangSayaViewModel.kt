package com.entre.gethub.ui.home.mygethub.tentangsaya

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.entre.gethub.data.Result
import com.entre.gethub.data.remote.params.UpdateUserProfileParams
import com.entre.gethub.data.remote.response.profiles.UpdateUserProfileResponse
import com.entre.gethub.data.remote.response.profiles.UserProfileResponse
import com.entre.gethub.data.repositories.ProfileRepository
import com.entre.gethub.utils.ViewModelFactory
import kotlinx.coroutines.launch
import retrofit2.HttpException

class HomeKelolaMyGethubEditTentangSayaViewModel(private val profileRepository: ProfileRepository) : ViewModel() {

    fun getUserProfile(): LiveData<Result<UserProfileResponse>> {
        val resultLiveData = MutableLiveData<Result<UserProfileResponse>>()
        viewModelScope.launch {
            resultLiveData.value = Result.Loading
            try {
                val response = profileRepository.getUserProfile()
                if (response.success == true) {
                    resultLiveData.value = Result.Success(response)
                } else {
                    resultLiveData.value = Result.Error(response.message ?: "Unknown error")
                }
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                resultLiveData.value = Result.Error(errorBody ?: "Network Error")
            } catch (e: Exception) {
                resultLiveData.value = Result.Error(e.message ?: "Unknown error")
            }
        }
        return resultLiveData
    }

    fun editAbout(about: String): LiveData<Result<UpdateUserProfileResponse>> {
        val resultLiveData = MutableLiveData<Result<UpdateUserProfileResponse>>()
        viewModelScope.launch {
            resultLiveData.value = Result.Loading
            try {
                // Create an instance of UpdateUserProfileParams
                val updateUserProfileParams = UpdateUserProfileParams(
                    fullname = "",
                    profession = "",
                    email = "",
                    phone = "",
                    web = "",
                    address = "",
                    about = about,
                    photo = null
                )
                // Pass updateUserProfileParams to updateUserProfile
                val response = profileRepository.updateUserProfile(updateUserProfileParams)
                if (response.success == true) {
                    // Return Result.Success with UpdateUserProfileResponse
                    resultLiveData.value = Result.Success(response)
                } else {
                    resultLiveData.value = Result.Error(response.message ?: "Unknown error")
                }
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                resultLiveData.value = Result.Error(errorBody ?: "Network Error")
            } catch (e: Exception) {
                resultLiveData.value = Result.Error(e.message ?: "Unknown error")
            }
        }
        return resultLiveData
    }


}
