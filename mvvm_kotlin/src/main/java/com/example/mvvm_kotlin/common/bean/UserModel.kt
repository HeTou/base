package com.example.mvvm_kotlin.common.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mvvm_kotlin.common.bean.UserModel.Companion.USER_TABLE_NAME

@Entity(tableName = USER_TABLE_NAME, /*ignoredColumns = ["age"]*/)
class UserModel {

    companion object {
        const val USER_TABLE_NAME = "user2"
        const val NAME = "name"
        const val FACE_ID = "faceId2"
    }

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo
    var id: Long = 0

    @ColumnInfo(name = "faceId2")
    var faceId: String? = null

    @ColumnInfo
    var name: String? = null

    var age = 0
    override fun toString(): String {
        return "UserModel(id=$id, faceId=$faceId, name=$name, age=$age)"
    }


}