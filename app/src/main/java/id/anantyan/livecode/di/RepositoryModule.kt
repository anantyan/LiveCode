package id.anantyan.livecode.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import id.anantyan.livecode.data.repository.MoviesRemoteRepository
import id.anantyan.livecode.data.service.MoviesApi
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Singleton
    @Provides
    fun provideMoviesRemoteRepository(api: MoviesApi): MoviesRemoteRepository {
        return MoviesRemoteRepository(api)
    }
}