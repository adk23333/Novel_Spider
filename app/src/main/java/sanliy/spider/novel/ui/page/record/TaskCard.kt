package sanliy.spider.novel.ui.page.record

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import sanliy.spider.novel.MainViewModel
import sanliy.spider.novel.model.APP
import sanliy.spider.novel.model.Task
import sanliy.spider.novel.net.sfacg.model.CharCount
import sanliy.spider.novel.net.sfacg.model.FinishedStatus
import sanliy.spider.novel.net.sfacg.model.FreeStatus
import sanliy.spider.novel.net.sfacg.model.NovelsType
import sanliy.spider.novel.net.sfacg.model.UpdatedDate
import sanliy.spider.novel.ui.theme.Novel_SpiderTheme

@Composable
fun TaskCard(
    modifier: Modifier = Modifier,
    index: Int,
    task: Task,
    isMarkScreen: Boolean,
    viewModel: RecordViewModel,
    mainViewModel: MainViewModel,
    onRecordToCrawler: () -> Unit,
) {
    val context = LocalContext.current
    Surface(
        onClick = {
            mainViewModel.task = task.copy(base = task.base.copy(id = null, isMark = false))
            onRecordToCrawler()
        },
        modifier,
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
        shadowElevation = 2.5.dp,
    ) {
        Column(
            Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(8.dp)
        ) {

            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
                Text("${index + 1}. ${task.base.name}")
                Surface(
                    color = MaterialTheme.colorScheme.errorContainer,
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(APP.valueOf(task.base.app).zh, Modifier.padding(4.dp))
                }
            }
            Text(
                "页码：${task.base.start}->${if (task.base.end == 0) "∞" else task.base.end}  "
                        + "字数：${
                    CharCount.fromValue(
                        task.base.requestNovels.beginCount,
                        task.base.requestNovels.endCount
                    ).zh
                }  "
                        + "类型：${NovelsType.fromValue(task.base.requestNovels.type).zh}"
            )
            HorizontalDivider()
            Text(
                "完结：${
                    FinishedStatus.fromValue(task.base.requestNovels.isfinish).zh
                }  签约：${FreeStatus.fromValue(task.base.requestNovels.isfree).zh}  最近更新：${
                    UpdatedDate.fromValue(task.base.requestNovels.updatedays).zh
                }"
            )
            Text("标签：${
                if (task.systagids.isEmpty()) "不限"
                else task.systagids.joinToString { it.tagName }
            }")
            Text("禁用：${
                if (task.notexcludesystagids.isEmpty()) "不限"
                else task.notexcludesystagids.joinToString { it.tagName }
            }")
            HorizontalDivider()
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                if (!isMarkScreen) {
                    Button(
                        onClick = {
                            viewModel.delete(task)
                        },
                        Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(Color.Transparent)
                    ) {
                        Icon(
                            Icons.Filled.Delete,
                            null,
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
                Button(
                    onClick = { viewModel.switchIsMark(task) },
                    Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(Color.Transparent)
                ) {
                    Icon(
                        Icons.Filled.Star,
                        null,
                        tint = if (task.base.isMark) Color.Yellow else Color.Gray
                    )
                }
                Button(
                    onClick = { viewModel.writeExcel(context, task) },
                    Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
                ) {
                    Icon(Icons.Filled.Share, null, tint = MaterialTheme.colorScheme.onPrimary)
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun TaskCardPreview() {
    Novel_SpiderTheme {
        TaskCard(
            Modifier
                .padding(16.dp, 8.dp)
                .wrapContentHeight()
                .fillMaxWidth(),
            index = 0, task = Task(1), false, viewModel(), viewModel()
        ) {}
    }
}