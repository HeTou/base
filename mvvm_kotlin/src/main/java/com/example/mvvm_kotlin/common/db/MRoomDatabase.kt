package com.example.mvvm_kotlin.common.db

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.SupportSQLiteStatement
import com.example.mvvm_kotlin.common.bean.DogModel
import com.example.mvvm_kotlin.common.bean.UserModel
import kotlin.math.sin


@Database(entities = [UserModel::class, DogModel::class], version = 3, exportSchema = true)
abstract class MRoomDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    abstract fun dogDao(): DogDao


    companion object {
        val TAG = "MRoomDatabase"
        val DATABASE_NAME = "room"


        /**
         * 关于数据库迁移问题：
         * 如下所示 有三个迁移：
         * 1->2  2->3  1->3
         * 现在有2个问题 如果v1直接升级到v3 执行流程是如何？ 如果1->3以前不存在又是如何
         *
         * 答：1、 当v1->v3直接升级时，如果存在v1->v3的迁移时，会直接跑该迁移，如果不存在，则会从大到小进行执行，如 （1>2->(2>3) 这样子
         * */

        var migration1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
//              删除表字段
//                database.execSQL("alter table user2 drop column age")
                database.execSQL("alter table user2 add column age INTEGER NOT NULL default 0")
            }
        }
        var migration2_3: Migration = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
//              删除表字段
//                database.execSQL("alter table user2 drop column age")
                database.execSQL("CREATE TABLE IF NOT EXISTS dogModel (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `faceId` TEXT)")
            }
        }

        var migration1_3: Migration = object : Migration(1, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
//              删除表字段
//                database.execSQL("alter table user2 drop column age")
                database.execSQL("alter table user2 add column age INTEGER NOT NULL default 0")
                database.execSQL("CREATE TABLE IF NOT EXISTS dogModel (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `faceId` TEXT)")
            }
        }

        // For Singleton instantiation
        @Volatile
        private var instance: MRoomDatabase? = null

        fun getInstance(context: Context): MRoomDatabase {


            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }


        private fun buildDatabase(appContext: Context): MRoomDatabase {


            return Room.databaseBuilder(appContext, MRoomDatabase::class.java, DATABASE_NAME)
                    //下面注释表示允许主线程进行数据库操作，但是不推荐这样做。
                    //我这里是为了Demo展示，稍后会介绍和LiveData、RxJava的使用
//                    .allowMainThreadQueries() //
                    // .openHelperFactory(new SafeHelperFactory("123456".toCharArray()))
                    .addMigrations(migration1_2, migration2_3, migration1_3)
                    .addCallback(
                            object : Callback() {
                                override fun onCreate(db: SupportSQLiteDatabase) {
                                    Log.d(TAG, "onCreate() called with: db = $db")
                                    super.onCreate(db)
                                }

                                override fun onOpen(db: SupportSQLiteDatabase) {
                                    super.onOpen(db)
                                }
                            })
                    .build()
        }
    }

}