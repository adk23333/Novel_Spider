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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import sanliy.spider.novel.MainViewModel
import sanliy.spider.novel.Screen
import sanliy.spider.novel.net.sfacg.CharCount
import sanliy.spider.novel.room.model.SfacgNovelListTask
import sanliy.spider.novel.ui.theme.Novel_SpiderTheme

@Composable
fun TaskCard(
    modifier: Modifier = Modifier,
    index: Int,
    task: SfacgNovelListTask,
    isMarkScreen: Boolean,
    navController: NavHostController,
    mainViewModel: MainViewModel,
    recordViewModel: RecordViewModel = hiltViewModel(),
) {
    Surface(
        onClick = {
            mainViewModel.task = task.copy(taskID = null, isMark = false)
            navController.navigate(Screen.SPIDER.route)
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
                Text("${index + 1}. ${task.taskName}")
                Surface(
                    color = MaterialTheme.colorScheme.errorContainer,
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(task.platform.zh, Modifier.padding(4.dp))
                }
            }
            Text(
                "页码：${task.startPage}->${if (task.endCount == 0) "∞" else task.endCount}  "
                        + "字数：${
                    CharCount.fromValue(
                        task.beginCount,
                        task.endCount
                    ).zh
                }  "
                        + "类型：${task.genreID}"
            )
            HorizontalDivider()
            Text(
                "完结：${
                    task.isFinish.zh
                }  签约：${task.isFree.zh}  最近更新：${
                    task.updateDate.zh
                }"
            )
            Text("标签：${
                task.tags.ifEmpty { "不限" }
            }")
            Text("禁用：${
                task.antiTags.ifEmpty { "不限" }
            }")
            HorizontalDivider()
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                if (!isMarkScreen) {
                    Button(
                        onClick = {
                            recordViewModel.delete(task)
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
                    onClick = { recordViewModel.switchIsMark(task) },
                    Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(Color.Transparent)
                ) {
                    Icon(
                        Icons.Filled.Star,
                        null,
                        tint = if (task.isMark) Color.Yellow else Color.Gray
                    )
                }
                Button(
                    onClick = { recordViewModel.writeExcel(task) },
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
            index = 0, task = SfacgNovelListTask(1), false,
            rememberNavController(), hiltViewModel()
        )
    }
}