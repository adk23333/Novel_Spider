package akabc.crawler.novel.ui.page.crawler

import akabc.crawler.novel.MainViewModel
import akabc.crawler.novel.NovelApplication
import akabc.crawler.novel.R
import akabc.crawler.novel.ui.page.unit.TextWithPressTopBar
import akabc.crawler.novel.ui.theme.Novel_CrawlerTheme
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun CrawlerScreen(mainViewModel: MainViewModel, onPressBack: () -> Unit, onCrawlerToHome: () -> Unit) {
    val context = LocalContext.current
    val app = remember { context.applicationContext }
    val viewModel: CrawlerViewModel =
        viewModel(factory = CrawlerViewModelFactory((app as NovelApplication).database))
    LaunchedEffect(Unit) {
        viewModel.task = mainViewModel.task
    }
    Scaffold(
        Modifier.fillMaxSize(),
        topBar = {
            TextWithPressTopBar(stringResource(R.string.crawler_sf), { onPressBack() }){
                IconButton(onClick = { onCrawlerToHome() }) {
                    Icon(Icons.Filled.Home, null)
                }
            }
        }) {
        SfCrawlerContext(it, viewModel)
    }
}


@Composable
fun SfCrawlerContext(paddingValues: PaddingValues, viewModel: CrawlerViewModel) {
    val context = LocalContext.current
    val novels = viewModel.novelsStateFlow.collectAsState()
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
                if (viewModel.task.base.id != null) {
                    if (!viewModel.task.base.isMark) viewModel.insertTask()
                } else
                    viewModel.insertTask()

                viewModel.getNovels(context)

            }, enabled = !viewModel.export.value) {
                Text(text = stringResource(R.string.crawler_sf_1))
            }
            Button(onClick = { viewModel.shareToExcel(context) }, enabled = viewModel.export.value) {
                Text(text = stringResource(R.string.crawler_sf_2))
            }
        }

    }


}

@Composable
@Preview(showBackground = true)
fun SfCrawlerContextPreview() {
    Novel_CrawlerTheme {
        SfCrawlerContext(paddingValues = PaddingValues(0.dp), viewModel = viewModel())
    }
}



