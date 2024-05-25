package sanliy.spider.novel.room

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import sanliy.spider.novel.NovelApplication
import sanliy.spider.novel.model.DbSfNovel
import sanliy.spider.novel.model.Task
import sanliy.spider.novel.net.sfacg.model.SysTag
import javax.inject.Singleton

@Database(
    entities = [DbSfNovel::class, Task.Base::class, SysTag::class],
    version = 1,
    exportSchema = false
)
abstract class NovelsDatabase : RoomDatabase() {


    abstract fun sfNovelsDao(): SfNovelsDao
    abstract fun taskDao(): TaskDao

    companion object {
        const val DATABASE_NAME = "novels.sqlite"
    }
}

@Module
@InstallIn(SingletonComponent::class)
class NovelsDatabaseModule {
    @Singleton
    @Provides
    fun provideNovelDatabase(
        application: NovelApplication,
    ): NovelsDatabase {
        return Room.databaseBuilder(
            application,
            NovelsDatabase::class.java,
            NovelsDatabase.DATABASE_NAME
        ).build()
    }
}