package sanliy.spider.novel.repository

import android.app.Application
import android.content.Intent
import android.os.Environment
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.core.content.FileProvider
import io.github.evanrupert.excelkt.workbook
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import sanliy.spider.novel.NovelFormatter
import sanliy.spider.novel.R
import sanliy.spider.novel.room.model.SfacgNovelImpl
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FileRepository @Inject constructor(
    private val application: Application,
) {
    companion object {
        const val APP_DIR_PATH = "NovelSpider"

        fun getMimeType(fileName: String): String? {
            return MimeTypeMap.getSingleton()
                .getMimeTypeFromExtension(
                    fileName.substring(fileName.lastIndexOf(".") + 1)
                )
        }

        val savePath =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                .absolutePath + File.separator + APP_DIR_PATH
    }


    fun createAppDir(): Boolean {
        return File(savePath).mkdirs()
    }

    suspend fun saveToExcel(novels: List<SfacgNovelImpl>, fileName: String) {
        createAppDir()
        withContext(Dispatchers.Main) {
            Toast.makeText(
                application,
                "正在导出文件到Documents/${APP_DIR_PATH}文件夹",
                Toast.LENGTH_SHORT
            )
                .show()
        }
        var maxTagCount = 0
        novels.forEach {
            if (it.tags.size > maxTagCount) {
                maxTagCount = it.tags.size
            }
        }
        workbook {
            sheet {
                row {
                    cell("书名")
                    cell("作者")
                    cell("收藏")
                    cell("字数")
                    cell("完结状态")
                    cell("签约状态")
                    cell("封面")
                    cell("最后更新时间")
                    cell("类型")
                    cell("bgBanner")
                    cell("评分")
                    cell("建书时间")
                    cell("isSensitive")
                    repeat(maxTagCount) {
                        cell("标签 ${it + 1}")
                    }
                }
                novels.forEach {
                    row {
                        cell(it.novelName)
                        cell(it.authorName)
                        cell(it.markCount)
                        cell(it.charCount)
                        cell(it.isFinish)
                        cell(it.signStatus)
                        cell(it.novelCover)
                        cell(it.lastUpdateTime.format(NovelFormatter.dateTimeFormatter))
                        cell(it.genre.genreName)
                        cell(it.bgBanner)
                        cell(it.point)
                        cell(it.createdTime.format(NovelFormatter.dateTimeFormatter))
                        cell(it.isSensitive)
                        it.tags.forEach {
                            cell(it.tagName)
                        }
                    }
                }
            }
        }.write("$savePath/$fileName")

    }

    fun shareFile(fileName: String) {
        try {
            val authority = application.packageName + ".fileprovider"
            val uri = FileProvider.getUriForFile(application, authority, File(savePath, fileName))
            val mime = getMimeType(fileName)
            application.startActivity(
                Intent.createChooser(
                    Intent(Intent.ACTION_SEND).apply {
                        setType(mime)
                        putExtra(Intent.EXTRA_STREAM, uri)
                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                    },
                    application.getString(R.string.spider_sf_2)
                ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    .addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            )
        } catch (e: Exception) {
            Log.w(FileRepository::class.simpleName, e)
            CoroutineScope(Dispatchers.Main).launch {
                Toast.makeText(application, "发生错误", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun openFile(fileName: String) {
        val authority = application.packageName + ".fileprovider"
        val mime = getMimeType(fileName)
        application.startActivity(
            Intent(Intent.ACTION_VIEW).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                setDataAndType(
                    FileProvider.getUriForFile(application, authority, File(savePath, fileName)),
                    mime,
                )
            }
        )
    }
}