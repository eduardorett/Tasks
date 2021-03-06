package com.example.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tasks.service.model.HeaderModel
import com.example.tasks.service.constants.TaskConstants
import com.example.tasks.service.listener.APIlistener
import com.example.tasks.service.listener.ValidationListener
import com.example.tasks.service.repository.PersonRepository
import com.example.tasks.service.repository.PriorityRepository
import com.example.tasks.service.repository.local.SecurityPreferences
import com.example.tasks.service.repository.remote.RetrofitClient

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val mPriorityRepository = PriorityRepository(application)
    private val mPersonRepository = PersonRepository(application)
    private val mSharedPreferences = SecurityPreferences(application) ////////

    private val mLogin = MutableLiveData<ValidationListener>()///////////////
    var login: LiveData<ValidationListener> = mLogin////////////////

    private val mLoggedUser = MutableLiveData<Boolean>()///////////////
    var LoggedUser: LiveData<Boolean> = mLoggedUser////////////////

    /**
     * Faz login usando API
     */
    fun doLogin(email: String, password: String) {
        mPersonRepository.login(email,password, object: APIlistener<HeaderModel>{
            override fun onSucecess(model: HeaderModel) {

                mSharedPreferences.store(TaskConstants.SHARED.TOKEN_KEY,model.token)/////////////
                mSharedPreferences.store(TaskConstants.SHARED.PERSON_KEY,model.personKey)///////
                mSharedPreferences.store(TaskConstants.SHARED.PERSON_NAME,model.name)////////

                RetrofitClient.addHeader(model.token,model.personKey)



                mLogin.value = ValidationListener()
            }

            override fun onFailure(str: String) {
              mLogin.value = ValidationListener(str)

            }

        })
    }

    /**
     * Verifica se usu??rio est?? logado
     */
    fun verifyLoggedUser() {

        val token= mSharedPreferences.get(TaskConstants.SHARED.TOKEN_KEY)
        val person= mSharedPreferences.get(TaskConstants.SHARED.PERSON_KEY)

        RetrofitClient.addHeader(token,person)


        val logged = (token != "" && person !="")

        if(!logged){
            mPriorityRepository.all() // PORQUE???????
        }

        mLoggedUser.value = logged
    }

}