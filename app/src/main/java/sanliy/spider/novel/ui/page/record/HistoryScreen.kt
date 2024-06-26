@file:OptIn(ExperimentalMaterial3Api::class)

package sanliy.spider.novel.ui.page.record

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import sanliy.spider.novel.Screen


@Composable
fun HistoryScreen(
    navController: NavHostController,
    recordViewModel: RecordViewModel = hiltViewModel(),
) {
    val tasks by recordViewModel.tasks.collectAsState(listOf())
    MarkContent(
        stringResource(Screen.HISTORY.stringId),
        tasks,
        navController,
        {
            recordViewModel.delete(it)
        },
        {
            recordViewModel.switchIsMark(it)
        }
    ) {
        recordViewModel.writeExcel(it)
    }
}



