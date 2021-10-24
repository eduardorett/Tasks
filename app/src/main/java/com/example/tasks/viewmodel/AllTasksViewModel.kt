package com.example.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tasks.service.listener.APIlistener
import com.example.tasks.service.model.PriorityModel
import com.example.tasks.service.model.TaskModel
import com.example.tasks.service.repository.TaskRepository

class AllTasksViewModel(application: Application) : AndroidViewModel(application) {

    private val mTaskRepository =   TaskRepository(application)


    private val mList = MutableLiveData<List<TaskModel>> ()
    var tasks: LiveData<List<TaskModel>> = mList

fun list(){

    mTaskRepository.all(object: APIlistener <List<TaskModel>>{
        override fun onSucecess(model: List<TaskModel>) {
mList.value = model
        }

        override fun onFailure(str: String) {
         mList.value = arrayListOf()
        }

    })



}

}