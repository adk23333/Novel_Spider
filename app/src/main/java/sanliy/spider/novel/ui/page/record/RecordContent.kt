@file:OptIn(ExperimentalMaterial3Api::class)

package sanliy.spider.novel.ui.page.record

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import sanliy.spider.novel.Screen
import sanliy.spider.novel.room.model.SfacgNovelListTask
import sanliy.spider.novel.ui.page.TextWithPressTopBar

@Composable
fun MarkContent(
    title: String,
    tasks: List<SfacgNovelListTask>,
    navController: NavHostController = rememberNavController(),
    onClickDelete: (it: SfacgNovelListTask) -> Unit,
    onClickMark: (it: SfacgNovelListTask) -> Unit,
    onClickShare: (it: SfacgNovelListTask) -> Unit,
) {
    val sheetState: SheetState = rememberModalBottomSheetState()
    var bottomSheetIndex: Int? by remember { mutableStateOf(null) }
    var showBottomSheet by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        Modifier.fillMaxSize(),
        topBar = {
            TextWithPressTopBar(
                title,
                { navController.popBackStack() })
        }) {
        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            itemsIndexed(tasks) { index, task ->
                RecordCard(
                    task = task,
                    onClickSurface = {
                        navController.navigate("${Screen.SPIDER.route}?taskID=${task.taskID}")
                    },
                    onClickExtend = {
                        bottomSheetIndex = index
                        showBottomSheet = true
                    }
                )
            }
        }
        bottomSheetIndex?.let { index ->
            tasks.getOrNull(index)?.let { bottomSheetTask ->
                if (showBottomSheet) {
                    ModalBottomSheet(
                        {
                            showBottomSheet = false
                            coroutineScope.launch { sheetState.hide() }.invokeOnCompletion {
                                if (!sheetState.isVisible) {
                                    showBottomSheet = false
                                }
                            }
                        },
                        Modifier.heightIn(500.dp),
                        sheetState
                    ) {
                        DetailsBottomSheet(bottomSheetTask, navController) {
                            Row(horizontalArrangement = Arrangement.Absolute.spacedBy(16.dp)) {
                                Button(
                                    onClick = {
                                        onClickDelete(bottomSheetTask)
                                    },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonColors(
                                        MaterialTheme.colorScheme.primaryContainer,
                                        MaterialTheme.colorScheme.onSurface,
                                        MaterialTheme.colorScheme.surfaceVariant,
                                        MaterialTheme.colorScheme.surfaceDim
                                    )
                                ) {
                                    Icon(Icons.Filled.Delete, contentDescription = null)
                                }
                                Button(
                                    onClick = { onClickMark(bottomSheetTask) },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonColors(
                                        MaterialTheme.colorScheme.primaryContainer,
                                        MaterialTheme.colorScheme.onSurface,
                                        MaterialTheme.colorScheme.surfaceVariant,
                                        MaterialTheme.colorScheme.surfaceDim
                                    )
                                ) {
                                    Icon(
                                        if (bottomSheetTask.isMark) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                                        contentDescription = null,
                                        tint = if (bottomSheetTask.isMark) Color.Red else LocalContentColor.current
                                    )
                                }


                                Button(
                                    onClick = {
                                        onClickShare(bottomSheetTask)
                                    },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonColors(
                                        MaterialTheme.colorScheme.primaryContainer,
                                        MaterialTheme.colorScheme.onSurface,
                                        MaterialTheme.colorScheme.surfaceVariant,
                                        MaterialTheme.colorScheme.surfaceDim
                                    )
                                ) {
                                    Icon(Icons.Filled.Share, contentDescription = null)
                                }
                            }
                        }
                    }

                }
            }

        }
    }
}


@Composable
@Preview(showBackground = true)
fun MarkContextPreview() {
    val tasks = mutableListOf<SfacgNovelListTask>()
    repeat(30) {
        tasks.add(SfacgNovelListTask(it.toLong()))
    }
    MarkContent("测试页面", tasks, onClickDelete = {}, onClickMark = {}) {}
}