@file:OptIn(ExperimentalMaterial3Api::class)

package sanliy.spider.novel.ui.page.record

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import sanliy.spider.novel.Screen


@Composable
fun MarkScreen(
    navController: NavHostController = rememberNavController(),
    viewModel: RecordViewModel = hiltViewModel(),
) {
    val tasks by viewModel.markTasks.collectAsState(listOf())
    MarkContent(
        stringResource(Screen.MARK.stringId),
        tasks,
        navController,
        {
            viewModel.delete(it)
        },
        {
            viewModel.switchIsMark(it)
        }
    ) {
        viewModel.writeExcel(it)
    }
}

