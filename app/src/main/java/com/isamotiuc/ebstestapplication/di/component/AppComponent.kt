package com.isamotiuc.ebstestapplication.di.component

import com.isamotiuc.ebstestapplication.CustomApplication
import com.isamotiuc.ebstestapplication.di.modules.AppModule
import com.isamotiuc.ebstestapplication.di.modules.BuildersModule
import com.isamotiuc.ebstestapplication.di.modules.DatabaseModule
import com.isamotiuc.ebstestapplication.di.modules.NetworkModule
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AndroidInjectionModule::class, BuildersModule::class, AppModule::class,
        NetworkModule::class, DatabaseModule::class]
)
interface AppComponent {
    fun inject(app: CustomApplication)
}