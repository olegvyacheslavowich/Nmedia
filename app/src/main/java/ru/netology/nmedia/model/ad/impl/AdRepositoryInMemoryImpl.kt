package ru.netology.nmedia.model.ad.impl

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.model.ad.Ad
import ru.netology.nmedia.model.ad.AdRepository

class AdRepositoryInMemoryImpl : AdRepository {

    private val ads = listOf(
        Ad(0, "Шлакоблоки", "Продажа стройматериалов недорого www.stroy.ru"),
        Ad(1, "Работа.ру", "Требутеся повар-бухгалтер. Оплата сдельная - www.workkkk.ru"),
    )

    private val data: LiveData<List<Ad>> = MutableLiveData(ads)

    override fun getAll(): LiveData<List<Ad>> = data
}