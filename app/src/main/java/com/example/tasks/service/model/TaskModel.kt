package com.example.tasks.service.model

import com.google.gson.annotations.SerializedName

class TaskModel {

    @SerializedName("Id")
    var Id:Int = 0

    @SerializedName("PriorityId")
    var PriorityId: Int = 0

    @SerializedName("Description")
    var Descripiton:String = ""

    @SerializedName("DueDate")
    var DueDate:String = ""

    @SerializedName("Complete")
    var Complete:Boolean = false


}