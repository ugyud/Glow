package com.dozkan.glow.data.source.local

import androidx.room.*
import com.dozkan.glow.data.model.response.ProductEntity

@Dao
interface ProductDAO {

    @Query("SELECT * FROM products")
    fun getProducts(): List<ProductEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addToFavorite(product: ProductEntity)

    @Delete
    fun deleteFromFavorites(product: ProductEntity)

    @Query("SELECT title FROM products")
    fun getFavoritesTitles(): List<String>
}