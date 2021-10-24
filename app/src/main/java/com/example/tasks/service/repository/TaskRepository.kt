package com.example.tasks.service.repository

import android.content.Context
import com.example.tasks.R
import com.example.tasks.service.constants.TaskConstants
import com.example.tasks.service.listener.APIlistener
import com.example.tasks.service.model.HeaderModel
import com.example.tasks.service.model.TaskModel
import com.example.tasks.service.repository.remote.RetrofitClient
import com.example.tasks.service.repository.remote.TaskService
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TaskRepository( val context: Context) {

    private val mRemote = RetrofitClient.createService(TaskService::class.java)

    fun all (listener:APIlistener<List<TaskModel>>){
        val call:Call<List<TaskModel>> = mRemote.all()
        list(call,listener)
    }


    fun nextWeek (listener:APIlistener<List<TaskModel>>){
        val call:Call<List<TaskModel>> = mRemote.NextWeek()
        list(call,listener)
    }

fun overdue (listener:APIlistener<List<TaskModel>>){
val call:Call<List<TaskModel>> = mRemote.Overdue()
list(call,listener)
}

   private  fun list(call: Call<List<TaskModel>>,listener:APIlistener<List<TaskModel>>) {

        call.enqueue(object: Callback<List<TaskModel>>{
            override fun onResponse(
                call: Call<List<TaskModel>>,
                response: Response<List<TaskModel>>
            ) {
                if (response.code() != TaskConstants.HTTP.SUCCESS) {////////////////////////////////////
                    val validation =
                        Gson().fromJson(response.errorBody()!!.string(), String::class.java)
                    listener.onFailure(validation)
                }else{///////////////////////////////////////

                    response.body()?.let { listener.onSucecess(it) } // original = Listener.onsucess(responde.body()) ai concerta o nulo no solve
                }

            }

            override fun onFailure(call: Call<List<TaskModel>>, t: Throwable) {

                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))


            }


        })
   }

        fun create(task:TaskModel, listener:APIlistener<Boolean>) {
            val call: Call<Boolean> = mRemote.create(task.PriorityId,task.Descripiton,task.DueDate,task.Complete)

            call.enqueue(object: Callback<Boolean>{

                override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                    if (response.code() != TaskConstants.HTTP.SUCCESS) {////////////////////////////////////
                        val validation =
                            Gson().fromJson(response.errorBody()!!.string(), String::class.java)
                        listener.onFailure(validation)
                    }else{///////////////////////////////////////

                        response.body()?.let { listener.onSucecess(it) } // original = Listener.onsucess(responde.body()) ai concerta o nulo no solve
                    }
                }

                override fun onFailure(call: Call<Boolean>, t: Throwable) {
                    listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
                }

            })

        }

    fun load(id:Int, listener:APIlistener<TaskModel>) {
        val call: Call<TaskModel> = mRemote.load(id)

        call.enqueue(object: Callback<TaskModel>{

            override fun onResponse(call: Call<TaskModel>, response: Response<TaskModel>) {
                if (response.code() != TaskConstants.HTTP.SUCCESS) {////////////////////////////////////
                    val validation =
                        Gson().fromJson(response.errorBody()!!.string(), String::class.java)
                    listener.onFailure(validation)
                }else{///////////////////////////////////////

                    response.body()?.let { listener.onSucecess(it) } // original = Listener.onsucess(responde.body()) ai concerta o nulo no solve
                }
            }

            override fun onFailure(call: Call<TaskModel>, t: Throwable) {
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
            }

        })

    }




}