package com.dozkan.glow.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dozkan.glow.data.model.response.ProductEntity

@Database(entities = [ProductEntity::class], version = 1, exportSchema = false)
abstract class ProductDB : RoomDatabase() {

    abstract fun productsDAO(): ProductDAO

}