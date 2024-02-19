package br.com.fiap.carstore.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.com.fiap.carstore.model.Car

@Database(entities = [Car::class], version = 1, exportSchema = false)
abstract class CarListRoomDatabase : RoomDatabase() {

    abstract fun carDAO(): CarDAO

    companion object {
        @Volatile
        private var INSTANCE: CarListRoomDatabase? = null

        fun getDatabase(context: Context): CarListRoomDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CarListRoomDatabase::class.java,
                    "car_list"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}