package sanliy.spider.novel.ui.page.crawler

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import sanliy.spider.novel.MainViewModel
import sanliy.spider.novel.R
import sanliy.spider.novel.Screen
import sanliy.spider.novel.ui.page.unit.TextWithPressTopBar
import sanliy.spider.novel.ui.theme.Novel_SpiderTheme


@Composable
fun CrawlerScreen(
    navController: NavHostController = rememberNavController(),
    mainViewModel: MainViewModel = hiltViewModel(),
) {
    val crawlerViewModel: CrawlerViewModel = hiltViewModel()
    LaunchedEffect(Unit) {
        crawlerViewModel.task = mainViewModel.task
    }
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
        SfCrawlerContext(it, crawlerViewModel)
    }
}


@Composable
fun SfCrawlerContext(
    paddingValues: PaddingValues,
    crawlerViewModel: CrawlerViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val novels = crawlerViewModel.novelsStateFlow.collectAsState()
    val state = ScrollState(0)
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
            Text(
                novels.value,
                Modifier
                    .padding(8.dp)
                    .fillMaxSize()
                    .verticalScroll(state)
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Button(onClick = {
                if (crawlerViewModel.task.base.id != null) {
                    if (!crawlerViewModel.task.base.isMark) crawlerViewModel.insertTask()
                } else
                    crawlerViewModel.insertTask()

                crawlerViewModel.getNovels(context)

            }, enabled = !crawlerViewModel.export.value) {
                Text(text = stringResource(R.string.spider_sf_1))
            }
            Button(
                onClick = { crawlerViewModel.shareToExcel(context) },
                enabled = crawlerViewModel.export.value
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
        SfCrawlerContext(paddingValues = PaddingValues(0.dp))
    }
}



