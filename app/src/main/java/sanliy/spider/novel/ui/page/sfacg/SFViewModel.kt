package sanliy.spider.novel.ui.page.sfacg

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import sanliy.spider.novel.UiState
import sanliy.spider.novel.model.NovelPlatform
import sanliy.spider.novel.net.sfacg.NovelType
import sanliy.spider.novel.net.sfacg.SysTag
import sanliy.spider.novel.repository.GenreRepository
import sanliy.spider.novel.repository.TagRepository
import sanliy.spider.novel.repository.TaskRepository
import sanliy.spider.novel.room.model.Genre
import sanliy.spider.novel.room.model.SfacgNovelListTask
import sanliy.spider.novel.room.model.Tag
import javax.inject.Inject

@HiltViewModel
class SFViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val tagRepository: TagRepository,
    private val genreRepository: GenreRepository,
) : ViewModel() {
    var taskState by mutableStateOf(SfacgNovelListTask(null))
    suspend fun getSfacgGenres(): UiState<List<NovelType>> {
        return genreRepository.getSfacgGenres().fold(
            onSuccess = {
                val data = it.data.toMutableList()
                data.add(0, NovelType(0, "全部", null))
                UiState.Success(data)
            },
            onFailure = {
                UiState.Failure(it)
            }
        )
    }

    suspend fun getSysTags(): UiState<List<SysTag>> {
        return tagRepository.getSysTags(0).fold(
            onSuccess = {
                UiState.Success(it.data)
            },
            onFailure = {
                UiState.Failure(it)
            }
        )
    }

    fun addTag(tagID: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val tag = tagRepository.getByIdWithSfacg(tagID)
            taskState = taskState.addTag(tag)
        }
    }

    fun addAntiTag(tagID: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val tag = tagRepository.getByIdWithSfacg(tagID)
            taskState = taskState.addAntiTag(tag)
        }
    }

    fun removeTag(tagID: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val tag = tagRepository.getByIdWithSfacg(tagID)
            taskState = taskState.removeTag(tag)
        }
    }

    fun getTags(vararg sysTagID: String): Flow<List<Tag>> {
        return tagRepository.getWithSfacgAndID(*sysTagID)
    }


    fun setTags(vararg sysTag: SysTag) {
        viewModelScope.launch(Dispatchers.IO) {
            sysTag.forEach {
                tagRepository.insertTag(
                    Tag(
                        it.sysTagId.toString(),
                        NovelPlatform.SFACG,
                        it.tagName
                    )
                )
            }

        }
    }

    fun setGenres(vararg genre: NovelType) {
        viewModelScope.launch(Dispatchers.IO) {
            genre.forEach {
                genreRepository.insertGenre(
                    Genre(
                        it.typeId.toString(),
                        NovelPlatform.SFACG,
                        it.typeName
                    )
                )
            }
        }
    }

    fun getGenreName(genreID: String): Flow<Genre> {
        return genreRepository.getSfacgGenre(genreID)
    }


    fun insertTask(task: SfacgNovelListTask): Long {
        return taskRepository.insertSfacgNLT(task)
    }

}
