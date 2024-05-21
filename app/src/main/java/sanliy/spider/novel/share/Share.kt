package sanliy.spider.novel.share

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.content.FileProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import sanliy.spider.novel.excelkt.workbook
import sanliy.spider.novel.model.DbSfNovel
import sanliy.spider.novel.model.Task
import java.io.File

const val DownloadPath = "/storage/emulated/0/Download"

fun writeToExcelAndShare(task: Task, context: Context, novels: List<DbSfNovel>) {
    val fileName = "ID-${task.base.id}-${task.base.name}.xlsx"
    var file = File(DownloadPath, "/${context.packageName}")
    file.mkdir()
    file = File("$DownloadPath/${context.packageName}", fileName)
    if (!file.exists()) {
        CoroutineScope(Dispatchers.Main).launch {
            Toast.makeText(context, "正在导出文件到Download文件夹", Toast.LENGTH_SHORT).show()
        }
        writeNovelsToExcel(novels, "$DownloadPath/${context.packageName}", fileName)
    }
    shareExcel(context, file)
}

fun writeNovelsToExcel(novels: List<DbSfNovel>, path: String, fileName: String) {
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
                    cell(it.typeName)
                    cell(it.bgBanner)
                    cell(it.point)
                    cell(it.addTime)
                    cell(it.isSensitive)
                    cell(it.tag1.toString())
                    cell(it.tag2.toString())
                    cell(it.tag3.toString())
                    cell(it.tag4.toString())
                }
            }
        }
    }.write("$path/$fileName")

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
        Log.d("share.Share", e.message.toString())
        CoroutineScope(Dispatchers.Main).launch {
            Toast.makeText(context, "请先下载WPS或者MS Excel", Toast.LENGTH_SHORT)
                .show()
        }
    }
}