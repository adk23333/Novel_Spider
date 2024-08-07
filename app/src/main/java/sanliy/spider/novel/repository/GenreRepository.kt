package sanliy.spider.novel.repository

import io.ktor.client.call.body
import sanliy.spider.novel.model.NovelPlatform
import sanliy.spider.novel.net.sfacg.ResponseNovelTypes
import sanliy.spider.novel.net.sfacg.SfacgService
import sanliy.spider.novel.room.GenreDao
import sanliy.spider.novel.room.model.GenreImpl
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GenreRepository @Inject constructor(
    private val genreDao: GenreDao,
    private val sfacgService: SfacgService,
) {
    suspend fun getSfacgGenre(genreID: String): GenreImpl {
        return genreDao.getSfacgGenreWithId(genreID, NovelPlatform.SFACG)
    }

    suspend fun getSfacgGenres(): Result<ResponseNovelTypes> = runCatching {
        sfacgService.getNovelTypes().body()
    }

    suspend fun insertGenre(vararg genre: GenreImpl) {
        genreDao.insert(*genre)
    }

    suspend fun getSfacgGenreByName(name: String): GenreImpl? {
        return genreDao.getGenreByName(name, NovelPlatform.SFACG)
    }
}