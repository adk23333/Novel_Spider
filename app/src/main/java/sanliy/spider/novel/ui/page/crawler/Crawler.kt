package sanliy.spider.novel.ui.page.crawler

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import sanliy.spider.novel.R
import sanliy.spider.novel.Screen
import sanliy.spider.novel.room.model.SfacgNovelListTaskImpl
import sanliy.spider.novel.ui.page.TextWithPressTopBar
import sanliy.spider.novel.ui.theme.Novel_SpiderTheme

const val TAG = "ui.page.crawler.Crawler"

@Composable
fun CrawlerScreen(
    navController: NavHostController,
    taskID: Long,
    crawlerViewModel: CrawlerViewModel = hiltViewModel(),
) {
    val task by crawlerViewModel.getTask(taskID).collectAsState(SfacgNovelListTaskImpl(null))
    Scaffold(
        Modifier.fillMaxSize(),
        topBar = {
            TextWithPressTopBar(
                stringResource(Screen.SPIDER.stringId),
                { navController.popBackStack() }) {
                IconButton(onClick = { navController.navigate(Screen.HOME.route) }) {
                    Icon(Icons.Filled.Home, null)
                }
            }
        }) {
        SfCrawlerContext(it, task, crawlerViewModel)
    }
}


@Composable
fun SfCrawlerContext(
    paddingValues: PaddingValues,
    task: SfacgNovelListTaskImpl,
    crawlerViewModel: CrawlerViewModel = hiltViewModel(),
) {
    val state = ScrollState(0)
    val logsState = rememberLazyListState()
    LaunchedEffect(crawlerViewModel.logs.size) {
        logsState.animateScrollToItem(crawlerViewModel.logs.size - 1)
    }

    var logOrderWidth by remember { mutableIntStateOf(18 + 8) }
    LaunchedEffect(crawlerViewModel.logSize.toString().length) {
        logOrderWidth = crawlerViewModel.logSize.toString().length * 18 + 8
    }

    Column(
        Modifier
            .fillMaxWidth()
            .padding(paddingValues)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            Modifier
                .padding(16.dp)
                .height(400.dp)
                .fillMaxWidth()
                .border(1.dp, Color.Gray, RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            LazyColumn(
                Modifier
                    .padding(8.dp)
                    .fillMaxSize()
                    .horizontalScroll(state),
                logsState
            ) {

                itemsIndexed(crawlerViewModel.logs) { index, log ->
                    Row {
                        Text(
                            text = (crawlerViewModel.logSize - crawlerViewModel.logs.size + index).toString(),
                            modifier = Modifier
                                .width(logOrderWidth.dp)
                                .padding(end = 8.dp)
                                .drawBehind {
                                    val strokeWidth = 1.dp.toPx()
                                    val x = size.width - strokeWidth / 2
                                    drawLine(
                                        Color.Gray,
                                        Offset(x, size.height),
                                        Offset(x, 0f),
                                        strokeWidth
                                    )
                                }
                                .padding(end = 8.dp),
                            textAlign = TextAlign.Right,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(text = log)
                    }
                }
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Button(
                onClick = { crawlerViewModel.finishTask(task) },
                enabled = !crawlerViewModel.export
            ) {
                Text(text = stringResource(R.string.spider_sf_1))
            }
            Button(
                onClick = {
                    crawlerViewModel.saveToExcelAndShare(task)
                },
                enabled = crawlerViewModel.export
            ) {
                Text(text = stringResource(R.string.spider_sf_2))
            }
        }

    }


}

@Composable
@Preview(showBackground = true)
fun SfCrawlerContextPreview() {
    Novel_SpiderTheme {
        SfCrawlerContext(paddingValues = PaddingValues(0.dp), SfacgNovelListTaskImpl(0))
    }
}



