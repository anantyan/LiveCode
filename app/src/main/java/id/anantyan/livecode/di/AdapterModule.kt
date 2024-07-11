package id.anantyan.livecode.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import id.anantyan.livecode.ui.detail.CasterAdapter
import id.anantyan.livecode.ui.detail.GenresAdapter
import id.anantyan.livecode.ui.home.HomeAdapter
import id.anantyan.livecode.ui.home.HomeSearchAdapter
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AdapterModule {
    @Singleton
    @Provides
    fun provideCasterAdapter(): CasterAdapter {
        return CasterAdapter()
    }

    @Singleton
    @Provides
    fun provideGenresAdapter(): GenresAdapter {
        return GenresAdapter()
    }

    @Singleton
    @Provides
    fun provideHomeAdapter(): HomeAdapter {
        return HomeAdapter()
    }

    @Singleton
    @Provides
    fun provideHomeSearchAdapter(): HomeSearchAdapter {
        return HomeSearchAdapter()
    }
}