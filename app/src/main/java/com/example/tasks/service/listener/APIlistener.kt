package com.example.tasks.service.listener

import com.example.tasks.service.model.HeaderModel

// O <T> FOI DEIXANDO GENERICO PARA PODER SER USADO EM QUALQUER LUGAR
interface APIlistener<T> {

           fun onSucecess(model: T)
            fun onFailure (str:String)
}