package sanliy.spider.novel.share

import android.content.Context
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
import sanliy.spider.novel.NovelApplication
import sanliy.spider.novel.room.model.SfacgNovel
import sanliy.spider.novel.room.model.SfacgNovelListTask
import java.io.File

suspend fun writeToExcelAndShare(task: SfacgNovelListTask, novels: List<SfacgNovel>) {
    val context = NovelApplication.instance.applicationContext
    val fileName = "ID-${task.taskID}-${task.taskName}.xlsx"
    val documentPath =
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
    val path = "$documentPath/${context.packageName}"
    var file = File(path)
    file.mkdir()
    file = File(path, fileName)

    if (!file.exists()) {
        withContext(Dispatchers.Main) {
            Toast.makeText(context, "正在导出文件到${path}文件夹", Toast.LENGTH_SHORT).show()
        }
        writeNovelsToExcel(novels, "$path/$fileName")
    }

    shareExcel(context, file)
}

fun writeNovelsToExcel(novels: List<SfacgNovel>, filePath: String) {
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
                    cell(it.lastUpdateTime)
                    cell(it.genreID)
                    cell(it.bgBanner)
                    cell(it.point)
                    cell(it.createdTime)
                    cell(it.isSensitive)
                    it.tags.split(",").forEach {
                        cell(it)
                    }
                }
            }
        }
    }.write(filePath)

}

fun shareExcel(context: Context, file: File) {
    try {
        val authority = context.packageName + ".fileprovider"
        val uri = FileProvider.getUriForFile(context, authority, file)
        context.startActivity(Intent.createChooser(Intent(Intent.ACTION_SEND).apply {
            type = "application/vnd.ms-excel"
            putExtra(Intent.EXTRA_STREAM, uri)
        }, "分享"))
    } catch (e: Exception) {
        Log.d("Share.shareExcel", e.message.toString())
        CoroutineScope(Dispatchers.Main).launch {
            Toast.makeText(context, "请先下载WPS或者MS Excel", Toast.LENGTH_SHORT)
                .show()
        }
    }
}