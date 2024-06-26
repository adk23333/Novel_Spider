package sanliy.spider.novel

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.components.SingletonComponent

@HiltAndroidApp
class NovelApplication : Application() {
    companion object {
        lateinit var instance: NovelApplication
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {
    @Provides
    fun provideNovelApplication(application: Application): NovelApplication {
        return application as NovelApplication
    }
}


