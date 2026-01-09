package com.lokal.mume.di

import com.lokal.mume.data.repository.NetworkCallRepoImpl
import com.lokal.mume.domain.repository.NetworkCallRepo
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindRepo(
        impl: NetworkCallRepoImpl
    ): NetworkCallRepo
}
