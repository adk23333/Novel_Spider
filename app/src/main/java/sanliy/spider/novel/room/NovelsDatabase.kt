package sanliy.spider.novel.room

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import sanliy.spider.novel.NovelApplication
import sanliy.spider.novel.room.model.Genre
import sanliy.spider.novel.room.model.SfacgNovel
import sanliy.spider.novel.room.model.SfacgNovelListTask
import sanliy.spider.novel.room.model.Tag
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Singleton

@Database(
    entities = [SfacgNovel::class, SfacgNovelListTask::class, Tag::class, Genre::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class NovelsDatabase : RoomDatabase() {
    abstract fun sfacgNovelDao(): SfacgNovelDao

    abstract fun sfacgTaskDao(): SfacgTaskDao

    abstract fun tagDao(): TagDao

    abstract fun genreDao(): GenreDao

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

    @Provides
    fun provideSfacgNovelDao(ndb: NovelsDatabase): SfacgNovelDao {
        return ndb.sfacgNovelDao()
    }

    @Provides
    fun provideSfacgTaskDao(ndb: NovelsDatabase): SfacgTaskDao {
        return ndb.sfacgTaskDao()
    }

    @Provides
    fun provideTagDao(ndb: NovelsDatabase): TagDao {
        return ndb.tagDao()
    }

    @Provides
    fun provideGenreDao(ndb: NovelsDatabase): GenreDao {
        return ndb.genreDao()
    }
}

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long): LocalDateTime {
        val instant = Instant.ofEpochMilli(value)
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
    }

    @TypeConverter
    fun dateToTimestamp(date: LocalDateTime): Long {
        return date.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }
}