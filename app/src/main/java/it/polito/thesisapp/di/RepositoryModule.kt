package it.polito.thesisapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import it.polito.thesisapp.repository.ProfileRepository
import it.polito.thesisapp.repository.TeamRepository
import javax.inject.Singleton

/**
 * Dagger module that provides repository instances for dependency injection.
 */
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    /**
     * Provides a singleton instance of ProfileRepository.
     *
     * @return a singleton instance of ProfileRepository
     */
    @Provides
    @Singleton
    fun provideProfileRepository(): ProfileRepository {
        return ProfileRepository()
    }

    /**
     * Provides a singleton instance of TeamRepository.
     *
     * @return a singleton instance of TeamRepository
     */
    @Provides
    @Singleton
    fun provideTeamRepository(): TeamRepository {
        return TeamRepository()
    }
}