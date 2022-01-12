package com.example.mvvm_kotlin.common.db

import androidx.room.Dao
import com.example.mvvm_kotlin.common.bean.DogModel

@Dao
abstract class DogDao : BaseDao<DogModel>() {

}