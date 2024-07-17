@file:OptIn(ExperimentalMaterial3Api::class)

package sanliy.spider.novel.ui.page.record

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import sanliy.spider.novel.NovelFormatter
import sanliy.spider.novel.R
import sanliy.spider.novel.Screen
import sanliy.spider.novel.model.NovelPlatform
import sanliy.spider.novel.net.sfacg.CharCount
import sanliy.spider.novel.room.model.SfacgNovelListTaskImpl
import sanliy.spider.novel.room.model.TagImpl
import sanliy.spider.novel.room.model.toTagNameList
import sanliy.spider.novel.ui.theme.Novel_SpiderTheme

@Composable
fun RecordCard(
    modifier: Modifier = Modifier,
    task: SfacgNovelListTaskImpl = SfacgNovelListTaskImpl(null),
    color: Color = MaterialTheme.colorScheme.surface,
    onClickSurface: () -> Unit = {},
    onClickExtend: (() -> Unit)? = null,
) {
    Surface(
        onClick = onClickSurface,
        modifier,
        color = color,
        shadowElevation = 2.5.dp,
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(8.dp), Arrangement.SpaceBetween
        ) {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painterResource(task.platform.drawableID), null,
                    Modifier.size(32.dp), Color.Unspecified
                )
                Column(Modifier.padding(start = 8.dp)) {
                    Text(
                        task.taskName,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        stringResource(task.platform.stringID),
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        task.created.format(NovelFormatter.dateTimeFormatter),
                        style = MaterialTheme.typography.bodySmall
                    )
                }

            }

            onClickExtend?.let {
                Icon(
                    Icons.Filled.MoreVert, null,
                    Modifier
                        .rotate(90f)
                        .clickable {
                            it()
                        },
                    Color.Gray
                )
            }

        }

    }
}

@Composable
@Preview(showBackground = true)
fun RecordCardPreview() {
    Novel_SpiderTheme {
        Column {
            RecordCard()
            RecordCard(onClickExtend = {})
        }
    }
}

@Composable
fun DetailsBottomSheet(
    task: SfacgNovelListTaskImpl,
    navController: NavHostController = rememberNavController(),
    actionContext: @Composable (() -> Unit),
) {
    val scrollState = rememberScrollState()
    Column(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        spacedBy(8.dp)
    ) {
        Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painterResource(task.platform.drawableID), null,
                    Modifier
                        .padding(8.dp)
                        .size(48.dp), Color.Unspecified
                )
                Column {
                    Text(
                        task.taskName,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        stringResource(task.platform.stringID),
                        style = MaterialTheme.typography.labelMedium
                    )
                }

            }
            Row(
                Modifier.clickable {
                    navController.navigate("${Screen.SPIDER.route}?taskID=${task.taskID}")
                },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "重制", color = MaterialTheme.colorScheme.primary)
                Icon(
                    Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }

        }

        actionContext()

        Column(
            Modifier
                .background(
                    MaterialTheme.colorScheme.primaryContainer,
                    RoundedCornerShape(12.dp)
                )
                .padding(16.dp)
                .scrollable(scrollState, Orientation.Vertical),
            spacedBy(4.dp)
        ) {
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                Text(text = stringResource(R.string.option_sf_1))
                Text(text = task.tags.toTagNameList())
            }
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                Text(text = stringResource(R.string.option_sf_16))
                Text(text = task.antiTags.toTagNameList())
            }
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                Text(text = stringResource(R.string.option_sf_2))
                Text(text = "${task.startPage} -> ${task.endPage}")
            }
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                Text(text = stringResource(R.string.option_sf_15))
                Text(text = task.genre.genreName)
            }
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                Text(text = stringResource(R.string.option_sf_7))
                Text(text = task.isFinish.zh)
            }
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                Text(text = stringResource(R.string.option_sf_8))
                Text(text = task.isFree.zh)
            }
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                Text(text = stringResource(R.string.option_sf_9))
                Text(text = task.updateDate.zh)
            }
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                Text(text = stringResource(R.string.option_sf_10))
                Text(text = CharCount.fromValue(task.beginCount, task.endCount).zh)
            }
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                Text(text = "任务创建时间")
                Text(
                    text = task.created.format(NovelFormatter.dateTimeFormatter).replace(" ", "\n")
                )
            }
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                Text(text = "任务最后更新时间")
                Text(
                    text = task.created.format(NovelFormatter.dateTimeFormatter).replace(" ", "\n")
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun DetailsBottomSheetPreview() {
    DetailsBottomSheet(
        task = SfacgNovelListTaskImpl(
            1,
            tags = listOf(
                TagImpl("123", NovelPlatform.SFACG, "一二三"),
                TagImpl("456", NovelPlatform.SFACG, "四五六"),
            )
        )
    ) {
        Text(text = "Action Context")
    }
}