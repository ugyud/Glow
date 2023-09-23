package com.dozkan.glow.module

import com.dozkan.glow.data.repository.ProductRepository
import com.dozkan.glow.data.repository.UserRepository
import com.dozkan.glow.data.source.local.ProductDAO
import com.dozkan.glow.data.source.remote.ProductService
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideProductRepository(productService: ProductService, productDAO: ProductDAO): ProductRepository =
        ProductRepository(productService, productDAO)

    @Provides
    @Singleton
    fun provideUserRepository(firebaseAuth: FirebaseAuth): UserRepository =
        UserRepository(firebaseAuth)
}