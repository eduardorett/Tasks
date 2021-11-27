package com.example.tasks.service.repository

import android.content.Context
import android.util.Log
import com.example.tasks.service.constants.TaskConstants
import com.example.tasks.service.model.PriorityModel
import com.example.tasks.service.repository.local.TaskDatabase
import com.example.tasks.service.repository.remote.PriorityService
import com.example.tasks.service.repository.remote.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PriorityRepository(val context: Context) : BaseRepository(context) {

    private val mRemote =
        RetrofitClient.createService(PriorityService::class.java) // ??????? COMEÇA AQUI O PROBLEMA 1
    private val mPriorityDataBase = TaskDatabase.getDatabase(context).priorityDAO()


    fun all() {
        Log.d("PRIORITYLIST", "Chegou repositorio all()")
        if (!isConnectionAvailable(context)) {
            return
        }
        Log.d("PRIORITYLIST", "Chamando API do remote")

        val call: Call<List<PriorityModel>> =
            mRemote.list() // PORQUE TA DANDO MISMATCH??????????????? TERMINA AQUI 2

        call.enqueue(object : Callback<List<PriorityModel>> {
            override fun onResponse(
                call: Call<List<PriorityModel>>,
                response: Response<List<PriorityModel>>
            ) {

                if (response.code() == TaskConstants.HTTP.SUCCESS) {
                    Log.d("PRIORITYLIST", "Response Sucesso!")
                    response.body()?.forEach {
                        Log.d("PRIORITYLIST", "priority -->"+it.toString())
                    }
                    mPriorityDataBase.clear()

                    response.body()
                        ?.let { mPriorityDataBase.save(it) } // mDatabase.save(response.body())

                } else {
                    Log.d("PRIORITYLIST", "Response fracasso! code-->" + response.code())
                }
            }

            override fun onFailure(call: Call<List<PriorityModel>>, t: Throwable) {
                TODO("Not yet implemented")
                Log.d("PRIORITYLIST", "Response erro!")
            }

        }

        )
    }

    fun list() = mPriorityDataBase.list()

    fun getDescription(id: Int) = mPriorityDataBase.getDescription(id)


}