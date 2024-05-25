package sanliy.spider.novel.ui.page.record

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import sanliy.spider.novel.Screen
import sanliy.spider.novel.ui.page.unit.TextWithPressTopBar


@Composable
fun MarkScreen(navController: NavHostController = rememberNavController()) {
    val viewModel: RecordViewModel = hiltViewModel()
    val tasks = viewModel.db.taskDao().getMarkTasks().collectAsState(listOf())
    Scaffold(
        Modifier.fillMaxSize(),
        topBar = {
            TextWithPressTopBar(
                stringResource(Screen.MARK.stringId),
                { navController.popBackStack() })
        }) {
        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            itemsIndexed(tasks.value) { index, task ->
                TaskCard(
                    Modifier
                        .padding(16.dp, 8.dp)
                        .wrapContentHeight()
                        .fillMaxWidth(),
                    index,
                    task,
                    true,
                    navController
                )
            }
        }
    }
}