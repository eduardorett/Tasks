package com.example.tasks.view

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.tasks.R
import com.example.tasks.service.constants.TaskConstants
import com.example.tasks.service.model.TaskModel
import com.example.tasks.viewmodel.TaskFormViewModel
import kotlinx.android.synthetic.main.activity_register.button_save
import kotlinx.android.synthetic.main.activity_task_form.*
import java.text.SimpleDateFormat
import java.util.*

class TaskFormActivity : AppCompatActivity(), View.OnClickListener,DatePickerDialog.OnDateSetListener {

    private lateinit var mViewModel: TaskFormViewModel
private val mDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
private val mListPriorityId:MutableList<Int> = arrayListOf()
  private val mTaskId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_form)

        mViewModel = ViewModelProvider(this).get(TaskFormViewModel::class.java)

        // Inicializa eventos
        listeners()
        observe()

        mViewModel.listPriorities()

        loadDataFromActitivty()
    }

    private fun loadDataFromActitivty(){
        val bundle = intent.extras
    if (bundle != null){
        mTaskId = bundle.getInt(TaskConstants.BUNDLE.TASKID)
        mViewModel.load(taskId)
        }

    }
    override fun onClick(v: View) {
        val id = v.id
        if (id == R.id.button_save) {
            handleSave()
        } else if (id == R.id.button_date){
            showDatePicker()
        }
    }

    private fun handleSave(){
        val task = TaskModel().apply{
           this.Id = mTaskId
            this.Descripiton = edit_description.text.toString()
            this.Complete = check_complete.isChecked
            this.DueDate = button_date.text.toString()
            this.PriorityId = mListPriorityId[spinner_priority.selectedItemPosition]
        }
        mViewModel.save(task)
    }



    private fun showDatePicker(){

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day =  c.get(Calendar.DAY_OF_MONTH)


        DatePickerDialog(this,this,year,month,day).show()

    }

    private fun observe() {

        mViewModel.priorities.observe(this, androidx.lifecycle.Observer {

            val list:MutableList<String> = arrayListOf()
            for (item in it){
                list.add(item.Descripition)
                mListPriorityId.add(item.id)

            }

            val adapter = ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,list)

            spinner_priority.adapter = adapter
        })
        mViewModel.validation.observe(this,androidx.lifecycle.Observer {
            if (it.succes()){
                Toast.makeText(this,"Sucesso",Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, it.failure(),Toast.LENGTH_SHORT).show()

            }
        })

    }

    private fun listeners() {
        button_save.setOnClickListener(this)
        button_date.setOnClickListener(this)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
val calendar = Calendar.getInstance()
    calendar.set(year,month,dayOfMonth)

        val str = mDateFormat.format(calendar.time)
        button_date.text = str

    }

}