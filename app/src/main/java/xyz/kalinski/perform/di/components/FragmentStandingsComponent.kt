package xyz.kalinski.perform.di.components

import dagger.Component
import xyz.kalinski.perform.activities.main.fragments.standings.StandingsFragment
import xyz.kalinski.perform.di.modules.AppModule
import xyz.kalinski.perform.di.modules.NetworkModule
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(
        AppModule::class,
        NetworkModule::class
))
interface FragmentStandingsComponent {
    fun inject(fragment: StandingsFragment)
}