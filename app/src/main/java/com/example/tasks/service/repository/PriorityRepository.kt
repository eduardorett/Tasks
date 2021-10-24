package com.example.tasks.service.repository

import android.content.Context
import com.example.tasks.R
import com.example.tasks.service.constants.TaskConstants
import com.example.tasks.service.listener.APIlistener
import com.example.tasks.service.model.HeaderModel
import com.example.tasks.service.model.PriorityModel
import com.example.tasks.service.repository.local.TaskDatabase
import com.example.tasks.service.repository.remote.PersonService
import com.example.tasks.service.repository.remote.PriorityService
import com.example.tasks.service.repository.remote.RetrofitClient
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PriorityRepository(context:Context) {

    private val mRemote = RetrofitClient.createService(PriorityService::class.java) // ??????? COMEÇA AQUI O PROBLEMA 1
    private val mPriorityDataBase = TaskDatabase.getDatabase(context).priorityDAO()


    fun all(){
        val call: Call<List<PriorityModel>> = mRemote.list() // PORQUE TA DANDO MISMATCH??????????????? TERMINA AQUI 2

        call.enqueue(object: Callback<List<PriorityModel>> {
            override fun onResponse(
                call: Call<List<PriorityModel>>,
                response: Response<List<PriorityModel>>
            ) {

                if (response.code() != TaskConstants.HTTP.SUCCESS){
                   mPriorityDataBase.clear() // tem que limpar o usuario prévio se não crasha, mesmo que for relogar
                    response.body()?.let { mPriorityDataBase.save(it) } // mDatabase.save(response.body())

            }
            }

            override fun onFailure(call: Call<List<PriorityModel>>, t: Throwable) {
                TODO("Not yet implemented")
            }

        }

            )
    }

    fun list() = mPriorityDataBase.list()

    fun getDescription(id:Int) = mPriorityDataBase.getDescription(id)

}