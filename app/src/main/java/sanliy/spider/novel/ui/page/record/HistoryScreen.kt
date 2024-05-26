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
import sanliy.spider.novel.MainViewModel
import sanliy.spider.novel.Screen
import sanliy.spider.novel.ui.page.TextWithPressTopBar


@Composable
fun HistoryScreen(
    navController: NavHostController,
    mainViewModel: MainViewModel,
) {
    val recordViewModel: RecordViewModel = hiltViewModel()
    val tasks = recordViewModel.tasks.collectAsState(listOf())
    Scaffold(Modifier.fillMaxSize(),
        {
            TextWithPressTopBar(
                stringResource(Screen.HISTORY.stringId),
                { navController.popBackStack() })
        }
    ) {

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
                    false,
                    navController,
                    mainViewModel
                )
            }
        }
    }
}



