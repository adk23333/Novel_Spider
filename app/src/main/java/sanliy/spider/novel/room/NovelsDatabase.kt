package sanliy.spider.novel.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import sanliy.spider.novel.model.DbSfNovel
import sanliy.spider.novel.model.Task
import sanliy.spider.novel.net.sfacg.model.SysTag

@Database(
    entities = [DbSfNovel::class, Task.Base::class, SysTag::class],
    version = 1,
    exportSchema = false
)
abstract class NovelsDatabase : RoomDatabase() {
    abstract fun sfNovelsDao(): SfNovelsDao
    abstract fun taskDao(): TaskDao

    companion object {
        @Volatile
        private var INSTANCE: NovelsDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope,
        ): NovelsDatabase {

            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NovelsDatabase::class.java,
                    "novels"
                )

                    .fallbackToDestructiveMigration()
                    .addCallback(NovelDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }

        private class NovelDatabaseCallback(
            private val scope: CoroutineScope,
        ) : Callback() {
            /**
             * Override the onCreate method to populate the database.
             */
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                // If you want to keep the data through app restarts,
                // comment out the following line.
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateDatabase(database.sfNovelsDao())
                    }
                }
            }
        }

        suspend fun populateDatabase(dao: SfNovelsDao) {
            // Start the app with a clean database every time.
            // Not needed if you only populate on creation.

        }
    }
}