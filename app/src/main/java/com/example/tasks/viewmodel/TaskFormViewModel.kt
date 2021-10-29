package com.example.tasks.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tasks.service.listener.APIlistener
import com.example.tasks.service.listener.ValidationListener
import com.example.tasks.service.model.PriorityModel
import com.example.tasks.service.model.TaskModel
import com.example.tasks.service.repository.PriorityRepository
import com.example.tasks.service.repository.TaskRepository

class TaskFormViewModel(application: Application) : AndroidViewModel(application) {

    private val mPriorityRepository = PriorityRepository(application)
    private val mTaskRepository = TaskRepository(application)

    private val mPriorityList = MutableLiveData<List<PriorityModel>>()
    val priorities: LiveData<List<PriorityModel>> = mPriorityList

    private val mValidation = MutableLiveData<ValidationListener>()
    val validation: LiveData<ValidationListener> = mValidation


    private val mTask = MutableLiveData<TaskModel>()
    val Task: LiveData<TaskModel> = mTask


    fun listPriorities() {
        Log.d("PRIORITYLIST", "CHAMOU AQUI no VIWMODEL")
        mPriorityList.value = mPriorityRepository.list()

    }

    fun save(task: TaskModel) {

        if (task.Id == 0) {
            mTaskRepository.create(task, object : APIlistener<Boolean> {
                override fun onSucecess(model: Boolean) {
                    mValidation.value = ValidationListener()
                }

                override fun onFailure(str: String) {
                    mValidation.value = ValidationListener(str)
                }

            })
        } else {
            mTaskRepository.update(task, object : APIlistener<Boolean> {
                override fun onSucecess(model: Boolean) {
                    mValidation.value = ValidationListener()
                }

                override fun onFailure(str: String) {
                    mValidation.value = ValidationListener(str)
                }

            })

        }
    }

    fun load(id: Int) {
        mTaskRepository.load(id, object : APIlistener<TaskModel> {
            override fun onSucecess(model: TaskModel) {

                mTask.value = model
            }


            override fun onFailure(str: String) {


            }
        })
    }
}