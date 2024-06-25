@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package sanliy.spider.novel.ui.page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import sanliy.spider.novel.R
import sanliy.spider.novel.Screen
import sanliy.spider.novel.model.NovelPlatform
import sanliy.spider.novel.ui.theme.Novel_SpiderTheme

@Composable
fun HomeScreen(navController: NavHostController = rememberNavController()) {
    val snackBarState = remember { SnackbarHostState() }
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        topBar = { HomeTopBar(navController) },
        snackbarHost = { SnackbarHost(snackBarState) },
    ) {
        HomeContext(it, snackBarState, navController)
    }
}


@Composable
fun HomeTopBar(navController: NavHostController) {
    TopAppBar(
        { Text(stringResource(Screen.HOME.stringId)) },
        actions = {
            IconButton(onClick = {
                navController.navigate(Screen.MARK.route)
            }) {
                Icon(Icons.Filled.Favorite, null)
            }
            IconButton(onClick = {
                navController.navigate(Screen.HISTORY.route)
            }) {
                Icon(painterResource(R.drawable.history), null)
            }
            IconButton(onClick = { /*TODO*/ }) {
                Icon(Icons.Filled.Settings, null)
            }
        }
    )
}

@Composable
fun HomeContext(
    paddingValues: PaddingValues,
    snackBarState: SnackbarHostState,
    navController: NavHostController,
) {
    val scope = rememberCoroutineScope()
    Box(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        Column(
            Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            IconWithTextButton(NovelPlatform.SFACG) {
                navController.navigate(Screen.OPTION_SF.route)
            }
            IconWithTextButton(NovelPlatform.CI_WEI_MAO) {
                scope.launch {
                    snackBarState.showSnackbar("暂未支持刺猬猫", withDismissAction = true)
                }
            }
        }

    }

}

@Composable
fun IconWithTextButton(
    platform: NovelPlatform,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick, shape = RoundedCornerShape(16.dp), colors = ButtonDefaults.buttonColors(
            MaterialTheme.colorScheme.primaryContainer, MaterialTheme.colorScheme.primary
        )
    ) {
        Icon(
            painterResource(platform.drawableID), null,
            Modifier
                .width(30.dp)
                .height(30.dp), Color.Unspecified
        )
        Text(stringResource(platform.stringID), fontSize = 28.sp)
    }
}

@Preview(showBackground = true)
@Composable
fun HomeActivityPagePreview() {
    Novel_SpiderTheme {
        HomeScreen()
    }
}