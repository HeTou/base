package com.example.mvvm_kotlin.common.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.mvvm_kotlin.common.bean.UserModel

@Dao
interface UserDao {
    @Query("SELECT * FROM " + UserModel.USER_TABLE_NAME + " WHERE " +
            UserModel.NAME + " = :name")
    fun queryByName2Lv(name: String?): LiveData<UserModel?>?


    @Query("SELECT * FROM " + UserModel.USER_TABLE_NAME + " WHERE " + UserModel.NAME + " = :name")
    fun queryByName2Model(name: String?): UserModel?


    @Query("SELECT * FROM " + UserModel.USER_TABLE_NAME + " WHERE " + UserModel.FACE_ID + " = :faceId")
    fun queryByFaceId2Lv(faceId: String?): LiveData<UserModel?>?

    @Query("SELECT * FROM " + UserModel.USER_TABLE_NAME + " WHERE " + UserModel.FACE_ID + " = :faceId")
    fun queryByFaceId2Model(faceId: String?): UserModel?

    @Query("SELECT COUNT(*) FROM " + UserModel.USER_TABLE_NAME)
    fun count(): Int


    @Query("SELECT * FROM " + UserModel.USER_TABLE_NAME)
    fun queryAllByLv(): LiveData<List<UserModel?>?>?


    @Query("SELECT * FROM " + UserModel.USER_TABLE_NAME)
    fun queryAll(): List<UserModel?>?

    @Update
    fun updateUsers(userModels: List<UserModel?>?): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(userModel: UserModel?): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllUser(userModels: List<UserModel?>?): LongArray?

    @Delete
    fun delete(vararg userModels: UserModel?)

    @Delete
    fun deleteAll(userModels: List<UserModel?>?)

    @Query("DELETE FROM " + UserModel.USER_TABLE_NAME + " WHERE " +
            UserModel.FACE_ID + " = :faceId")
    fun deleteByFaceId(faceId: String?): Int
}