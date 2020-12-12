package ru.netology.nmedia.model.ad

import androidx.lifecycle.LiveData

interface AdRepository {

    fun getAll(): LiveData<List<Ad>>

}