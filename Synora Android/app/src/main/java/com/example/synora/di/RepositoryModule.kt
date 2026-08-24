package com.example.synora.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

// Repository bindings will be added here as features are implemented in later phases.
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule
