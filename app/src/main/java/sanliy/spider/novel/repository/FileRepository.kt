package sanliy.spider.novel.repository

import android.app.Application
import android.content.Intent
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.core.content.FileProvider
import io.github.evanrupert.excelkt.workbook
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import sanliy.spider.novel.R
import sanliy.spider.novel.room.model.SfacgNovelImpl
import sanliy.spider.novel.room.model.SfacgNovelListTaskImpl
import java.io.File
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FileRepository @Inject constructor(
    private val application: Application,
) {
    val documentPath: File =
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
    val excelPath = "$documentPath/${application.packageName}"

    suspend fun writeToExcelAndShare(task: SfacgNovelListTaskImpl, novels: List<SfacgNovelImpl>) {
        val fileName = "ID-${task.taskID}-${task.taskName}.xlsx"

        var file = File(excelPath)
        file.mkdir()
        file = File(excelPath, fileName)

        if (!file.exists()) {
            withContext(Dispatchers.Main) {
                Toast.makeText(application, "正在导出文件到${excelPath}文件夹", Toast.LENGTH_SHORT)
                    .show()
            }
            writeNovelsToExcel(novels, "$excelPath/$fileName")
        }

        shareExcel(File(excelPath, fileName))
    }

    private fun writeNovelsToExcel(novels: List<SfacgNovelImpl>, filePath: String) {
        val pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
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
                    cell("敏感的")
                    cell("标签1")
                    cell("标签2")
                    cell("标签3")
                    cell("标签4")
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
                        cell(it.lastUpdateTime.format(pattern))
                        cell(it.genre.genreName)
                        cell(it.bgBanner)
                        cell(it.point)
                        cell(it.createdTime.format(pattern))
                        cell(it.isSensitive)
                        it.tags.forEach {
                            cell(it.tagName)
                        }
                    }
                }
            }
        }.write(filePath)

    }

    private fun shareExcel(file: File) {
        try {
            val authority = application.packageName + ".fileprovider"
            val uri = FileProvider.getUriForFile(application, authority, file)
            application.startActivity(
                Intent.createChooser(
                    Intent(Intent.ACTION_SEND).apply {
                        type = "application/vnd.ms-excel"
                        putExtra(Intent.EXTRA_STREAM, uri)
                    },
                    application.getString(R.string.spider_sf_2)
                ).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            )
        } catch (e: Exception) {
            Log.w(FileRepository::class.simpleName, e.message.toString())
            CoroutineScope(Dispatchers.Main).launch {
                Toast.makeText(application, "发生错误", Toast.LENGTH_SHORT).show()
            }
        }
    }
}