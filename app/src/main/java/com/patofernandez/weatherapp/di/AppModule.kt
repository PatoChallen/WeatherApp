package com.patofernandez.weatherapp.di

import com.patofernandez.weatherapp.api.OpenWeatherService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
class AppModule {
    @Singleton
    @Provides
    fun provideWeatherService(): OpenWeatherService {
        return Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
//            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .build()
            .create(OpenWeatherService::class.java)
    }

//    @Singleton
//    @Provides
//    fun provideDb(app: Application): GithubDb {
//        return Room
//            .databaseBuilder(app, GithubDb::class.java, "github.db")
//            .fallbackToDestructiveMigration()
//            .build()
//    }
//
//    @Singleton
//    @Provides
//    fun provideUserDao(db: GithubDb): UserDao {
//        return db.userDao()
//    }
//
//    @Singleton
//    @Provides
//    fun provideRepoDao(db: GithubDb): RepoDao {
//        return db.repoDao()
//    }
}
