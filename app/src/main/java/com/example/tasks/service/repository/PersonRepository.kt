package com.example.tasks.service.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.example.tasks.R
import com.example.tasks.service.model.HeaderModel
import com.example.tasks.service.constants.TaskConstants
import com.example.tasks.service.listener.APIlistener
import com.example.tasks.service.repository.remote.PersonService
import com.example.tasks.service.repository.remote.RetrofitClient
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PersonRepository(val context: Context):BaseRepository(context) {

    private val mRemote = RetrofitClient.createService(PersonService::class.java)

    // TEM QUE POR NO MANIFEST PRA FORÇAR O HTTPS "USES CLEAR TRAFIC TRUE"
    fun login(email:String, password:String, listener: APIlistener<HeaderModel>){

        if(!isConnectionAvailable(context)){
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }

           val call: Call<HeaderModel> = mRemote.login(email,password)

        call.enqueue(object: Callback<HeaderModel>{ // ISSO VE APENAS SE CONCECTOU, OS DADOS PODEM VIR ERRADOS ainda
            override fun onResponse(call: Call<HeaderModel>, response: Response<HeaderModel>) {

                if (response.code() != TaskConstants.HTTP.SUCCESS) {////////////////////////////////////
                    val validation =
                        Gson().fromJson(response.errorBody()!!.string(), String::class.java)
                    listener.onFailure(validation)
                }else{///////////////////////////////////////

                response.body()?.let { listener.onSucecess(it) } // original = Listener.onsucess(responde.body()) ai concerta o nulo no solve
            }
            }

            override fun onFailure(call: Call<HeaderModel>, t: Throwable) {
          listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))///////////////
            }

        })
    }

    fun create(name:String,email:String,password:String, listener: APIlistener<HeaderModel>){

        if(!isConnectionAvailable(context)){
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }

        val call: Call<HeaderModel> = mRemote.create(name,email,password)

        call.enqueue(object: Callback<HeaderModel>{ // ISSO VE APENAS SE CONCECTOU, OS DADOS PODEM VIR ERRADOS ainda
            override fun onResponse(call: Call<HeaderModel>, response: Response<HeaderModel>) {

                if (response.code() != TaskConstants.HTTP.SUCCESS) {////////////////////////////////////
                    val validation =
                        Gson().fromJson(response.errorBody()!!.string(), String::class.java)
                    listener.onFailure(validation)
                }else{///////////////////////////////////////

                    response.body()?.let { listener.onSucecess(it) } // original = Listener.onsucess(responde.body()) ai concerta o nulo no solve
                }
            }

            override fun onFailure(call: Call<HeaderModel>, t: Throwable) {
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))///////////////
            }

        })
    }


}