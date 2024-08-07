package sanliy.spider.novel

import android.Manifest
import android.content.pm.PermissionInfo
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import sanliy.spider.novel.ui.page.HomeScreen
import sanliy.spider.novel.ui.page.crawler.CrawlerScreen
import sanliy.spider.novel.ui.page.record.HistoryScreen
import sanliy.spider.novel.ui.page.record.MarkScreen
import sanliy.spider.novel.ui.page.sfacg.SfacgScreen
import sanliy.spider.novel.ui.theme.Novel_SpiderTheme


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            this.requestPermissions(
                arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ), PermissionInfo.PROTECTION_DANGEROUS
            )
        }

        setContent {
            Novel_SpiderTheme {
                MainNavHost()
            }
        }
    }
}


enum class Screen(val route: String, @StringRes val stringId: Int) {
    HOME("home", R.string.home),
    OPTION_SF("option_sf", R.string.option_sf),
    SPIDER("crawler", R.string.spider_sf),
    HISTORY("history", R.string.history_1),
    MARK("mark", R.string.mark_1)
}

@Composable
fun MainNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.HOME.route,
    viewModel: MainViewModel = hiltViewModel(),
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.HOME.route) { HomeScreen(navController) }
        composable(Screen.OPTION_SF.route) { SfacgScreen(navController) }
        composable(Screen.HISTORY.route) { HistoryScreen(navController) }
        composable(Screen.MARK.route) { MarkScreen(navController) }
        composable(
            "${Screen.SPIDER.route}?taskID={taskID}",
            arguments = listOf(
                navArgument("taskID") { defaultValue = 0L; type = NavType.LongType },
            )
        ) { navBackStackEntry ->
            val taskID = navBackStackEntry.arguments?.getLong("taskID") ?: 0L
            CrawlerScreen(navController, taskID)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainNavHostPreview() {
    Novel_SpiderTheme {
        MainNavHost()
    }
}



