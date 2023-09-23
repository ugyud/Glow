package com.dozkan.glow.module

import android.content.Context
import androidx.room.Room
import com.dozkan.glow.data.source.local.ProductDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomDBModule {

    @Provides
    @Singleton
    fun provideRoomDB(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, ProductDB::class.java, "product_room_db").build()

    @Provides
    @Singleton
    fun provideDAO(roomDB: ProductDB) = roomDB.productsDAO()
}