package com.maritimo.control.di

import com.maritimo.control.data.repository.AuthRepositoryImpl
import com.maritimo.control.data.repository.PuertoRepositoryImpl
import com.maritimo.control.data.repository.MuelleRepositoryImpl
import com.maritimo.control.data.repository.BuqueRepositoryImpl
import com.maritimo.control.data.repository.CapitanRepositoryImpl
import com.maritimo.control.data.repository.AtraqueRepositoryImpl
import com.maritimo.control.data.repository.InspeccionRepositoryImpl
import com.maritimo.control.domain.repository.AuthRepository
import com.maritimo.control.domain.repository.PuertoRepository
import com.maritimo.control.domain.repository.MuelleRepository
import com.maritimo.control.domain.repository.BuqueRepository
import com.maritimo.control.domain.repository.CapitanRepository
import com.maritimo.control.domain.repository.AtraqueRepository
import com.maritimo.control.domain.repository.InspeccionRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindPuertoRepository(
        puertoRepositoryImpl: PuertoRepositoryImpl
    ): PuertoRepository

    @Binds
    @Singleton
    abstract fun bindMuelleRepository(
        muelleRepositoryImpl: MuelleRepositoryImpl
    ): MuelleRepository

    @Binds
    @Singleton
    abstract fun bindBuqueRepository(
        buqueRepositoryImpl: BuqueRepositoryImpl
    ): BuqueRepository

    @Binds
    @Singleton
    abstract fun bindCapitanRepository(
        capitanRepositoryImpl: CapitanRepositoryImpl
    ): CapitanRepository

    @Binds
    @Singleton
    abstract fun bindAtraqueRepository(
        atraqueRepositoryImpl: AtraqueRepositoryImpl
    ): AtraqueRepository

    @Binds
    @Singleton
    abstract fun bindInspeccionRepository(
        inspeccionRepositoryImpl: InspeccionRepositoryImpl
    ): InspeccionRepository
}
