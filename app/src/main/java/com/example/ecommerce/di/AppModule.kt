package com.example.ecommerce.di

import com.example.ecommerce.data.repository.ProductsRepositoryImpl
import com.example.ecommerce.data.source.remote.ApiUtils
import com.example.ecommerce.data.source.remote.ProductsRemoteDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideProductsRepositoryImpl(productsRemoteDao: ProductsRemoteDao): ProductsRepositoryImpl {
        return ProductsRepositoryImpl(productsRemoteDao)
    }

    @Provides
    @Singleton
    fun provideProductsRemoteDao(): ProductsRemoteDao {
        return ApiUtils.getProductsDao()
    }
}