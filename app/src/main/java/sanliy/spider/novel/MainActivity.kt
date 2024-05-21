package sanliy.spider.novel

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import sanliy.spider.novel.ui.page.HomeScreen
import sanliy.spider.novel.ui.page.crawler.CrawlerScreen
import sanliy.spider.novel.ui.page.record.HistoryScreen
import sanliy.spider.novel.ui.page.record.MarkScreen
import sanliy.spider.novel.ui.page.sfacg.OptionSF
import sanliy.spider.novel.ui.theme.Novel_SpiderTheme


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            this.requestPermissions(
                arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ), 1
            )
        }

        setContent {
            Novel_SpiderTheme {
                MainNavHost()
            }
        }
    }
}


const val HOME_SCREEN = "Home"
const val OPTION_SF = "OptionSF"
const val Crawler = "Crawler"
const val HISTORY = "History"
const val MARK = "Mark"

@Composable
fun MainNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = HOME_SCREEN,
    viewModel: MainViewModel = viewModel(),
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable(HOME_SCREEN) {
            HomeScreen(
                { navController.navigate(OPTION_SF) },
                { navController.navigate(MARK) },
                { navController.navigate(HISTORY) })
        }
        composable(OPTION_SF) {
            OptionSF(
                viewModel,
                { navController.navigate(Crawler) }) { navController.popBackStack() }
        }
        composable(HISTORY) {
            HistoryScreen(
                viewModel,
                { navController.popBackStack() },
                { navController.navigate(Crawler) })
        }
        composable(MARK) {
            MarkScreen(
                viewModel,
                { navController.popBackStack() },
                { navController.navigate(Crawler) })
        }
        composable(Crawler) {
            CrawlerScreen(
                viewModel,
                { navController.popBackStack() },
                { navController.navigate(HOME_SCREEN) })
        }

    }
}




