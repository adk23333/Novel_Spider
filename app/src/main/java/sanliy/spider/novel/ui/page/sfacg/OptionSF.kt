@file:OptIn(ExperimentalLayoutApi::class)

package sanliy.spider.novel.ui.page.sfacg

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults.filterChipColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import sanliy.spider.novel.MainViewModel
import sanliy.spider.novel.R
import sanliy.spider.novel.Screen
import sanliy.spider.novel.model.Task
import sanliy.spider.novel.net.sfacg.model.CharCount
import sanliy.spider.novel.net.sfacg.model.FinishedStatus
import sanliy.spider.novel.net.sfacg.model.FreeStatus
import sanliy.spider.novel.net.sfacg.model.NovelsType
import sanliy.spider.novel.net.sfacg.model.SysTag
import sanliy.spider.novel.net.sfacg.model.UpdatedDate
import sanliy.spider.novel.tripleSwitch
import sanliy.spider.novel.ui.page.unit.TextWithPressTopBar
import sanliy.spider.novel.ui.page.unit.UnitFilterChip

@Composable
fun OptionSF(navController: NavHostController = rememberNavController()) {
    Scaffold(
        Modifier.fillMaxSize(),
        topBar = {
            TextWithPressTopBar(
                stringResource(Screen.OPTION_SF.stringId),
                { navController.popBackStack() })
        }) {
        OptionSFContext(it, navController)
    }
}

@Composable
fun OptionSFContext(
    paddingValues: PaddingValues,
    navController: NavHostController = rememberNavController(),
    mainViewModel: MainViewModel = hiltViewModel(),
    sfViewModel: SFViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val text = stringResource(R.string.option_sf_13)
    val text2 = stringResource(R.string.option_sf_14)
    var isMark by remember { mutableStateOf(true) }
    var taskName by remember { mutableStateOf(sfViewModel.task.base.name) }
    Column(
        Modifier
            .padding(paddingValues)
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(taskName, {
            taskName = it
            sfViewModel.task.base.name = taskName
        }, label = {
            Text(stringResource(R.string.option_sf_task_name))
        }, maxLines = 3)
        TagsSelect(sfViewModel)
        OtherOptions(sfViewModel)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(isMark, {
                isMark = it
                sfViewModel.task.base.isMark = isMark
            })
            Text(stringResource(R.string.option_sf_12))
        }
        Button(onClick = {
            if (sfViewModel.task.base.name != "") {
                if (sfViewModel.task.base.isMark) {
                    sfViewModel.insertTask()
                    Toast.makeText(context, text2, Toast.LENGTH_SHORT).show()
                }
                mainViewModel.task = sfViewModel.task
                navController.navigate(Screen.SPIDER.route)
            } else
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
        }, Modifier.width(200.dp)) {
            Text(stringResource(R.string.option_sf_11))
        }
    }
}


@Composable
fun TagsSelect(sfViewModel: SFViewModel = hiltViewModel()) {
    var rotate by remember { mutableFloatStateOf(0f) }
    val tags by sfViewModel.tagsStateFlow.collectAsState()
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        sfViewModel.getSysTags(context)
    }
    Column(
        Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(32.dp, 8.dp)
            .border(1.dp, Color.Gray, RoundedCornerShape(16.dp))
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            Modifier
                .wrapContentSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(stringResource(R.string.option_sf_1))

            Image(painterResource(R.drawable.reflash), null,
                Modifier
                    .clip(CircleShape)
                    .clickable {
                        sfViewModel.getSysTags(context)
                        CoroutineScope(Dispatchers.IO).launch {
                            //旋转时长 1s  旋转速度 2f/1ms
                            while (sfViewModel.refreshTags) {
                                (0..360 * 1).forEach { _ ->
                                    rotate += 2f
                                    delay(1)
                                }
                            }
                        }
                    }
                    .rotate(rotate))
        }
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            tags.forEach {
                TagFilterChip(it, sfViewModel)
            }
        }
    }
}

@Composable
fun TagFilterChip(tag: SysTag, sfViewModel: SFViewModel = hiltViewModel()) {
    var selected by rememberSaveable { mutableIntStateOf(0) }
    val checkLeadingIcon: @Composable () -> Unit = { Icon(Icons.Default.Check, null) }
    val shieldLeadingIcon: @Composable () -> Unit = { Icon(Icons.Default.Close, null) }
    FilterChip(
        selected != 0,
        onClick = {
            selected = tripleSwitch(selected)
            when (selected) {
                -1 -> {
                    sfViewModel.task.systagids.remove(tag)
                    sfViewModel.task.notexcludesystagids.add(tag)
                }

                0 -> sfViewModel.task.notexcludesystagids.remove(tag)
                1 -> sfViewModel.task.systagids.add(tag)
            }

        },
        label = { Text(tag.tagName) },
        colors = filterChipColors(
            selectedContainerColor = MaterialTheme.colorScheme.let {
                if (selected == 1) it.primaryContainer else it.errorContainer
            }),
        leadingIcon = when (selected) {
            -1 -> shieldLeadingIcon
            1 -> checkLeadingIcon
            else -> null
        }
    )
}


@Composable
fun OtherOptions(sfViewModel: SFViewModel = hiltViewModel()) {
    var visible by rememberSaveable { mutableStateOf(false) }
    Column(
        Modifier
            .fillMaxWidth()
            .padding(32.dp, 8.dp)
            .border(1.dp, Color.Gray, RoundedCornerShape(16.dp)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (!visible) Text(
            stringResource(R.string.option_sf_3),
            Modifier.clickable {
                visible = !visible
            })
        else {
            PageOption(sfViewModel.task, sfViewModel)
            StateOption(sfViewModel)
        }
    }
}

@Composable
fun PageOption(task: Task, sfViewModel: SFViewModel) {
    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(stringResource(R.string.option_sf_2))
        Row(Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(stringResource(R.string.option_sf_4))
            OutlinedTextField(
                task.base.start.toString(),
                { v ->
                    v.toIntOrNull()?.let { vi ->
                        sfViewModel.task =
                            sfViewModel.task.copy(base = sfViewModel.task.base.copy(start = vi))
                    }
                },
                Modifier.width(80.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )
            Text(text = stringResource(R.string.option_sf_5))
            OutlinedTextField(
                task.base.end.toString(),
                { v ->
                    v.toIntOrNull()?.let { vi ->
                        sfViewModel.task =
                            sfViewModel.task.copy(base = sfViewModel.task.base.copy(end = vi))
                    }
                },
                Modifier.width(80.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }
    }
}


@Composable
fun StateOption(sfViewModel: SFViewModel) {
    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(stringResource(R.string.option_sf_15))
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            NovelsType.entries.forEach {
                UnitFilterChip(it.zh, it.value == sfViewModel.task.base.requestNovels.type) {
                    sfViewModel.task =
                        sfViewModel.task.copy(
                            base = sfViewModel.task.base.copy(
                                requestNovels = sfViewModel.task.base.requestNovels.copy(
                                    type = it.value
                                )
                            )
                        )
                }
            }
        }

        Text(stringResource(R.string.option_sf_7))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FinishedStatus.entries.forEach {
                UnitFilterChip(it.zh, it.value == sfViewModel.task.base.requestNovels.isfinish) {
                    sfViewModel.task =
                        sfViewModel.task.copy(
                            base = sfViewModel.task.base.copy(
                                requestNovels = sfViewModel.task.base.requestNovels.copy(
                                    isfinish = it.value
                                )
                            )
                        )
                }
            }
        }

        Text(stringResource(R.string.option_sf_8))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FreeStatus.entries.forEach {
                UnitFilterChip(it.zh, it.value == sfViewModel.task.base.requestNovels.isfree) {
                    sfViewModel.task =
                        sfViewModel.task.copy(
                            base = sfViewModel.task.base.copy(
                                requestNovels = sfViewModel.task.base.requestNovels.copy(
                                    isfree = it.value
                                )
                            )
                        )
                }
            }
        }

        Text(stringResource(R.string.option_sf_9))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            UpdatedDate.entries.forEach {
                UnitFilterChip(it.zh, it.value == sfViewModel.task.base.requestNovels.updatedays) {
                    sfViewModel.task =
                        sfViewModel.task.copy(
                            base = sfViewModel.task.base.copy(
                                requestNovels = sfViewModel.task.base.requestNovels.copy(
                                    updatedays = it.value
                                )
                            )
                        )
                }
            }
        }

        Text(stringResource(R.string.option_sf_10))
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            CharCount.entries.forEach {
                UnitFilterChip(
                    it.zh, it == CharCount.fromValue(
                        sfViewModel.task.base.requestNovels.beginCount,
                        sfViewModel.task.base.requestNovels.endCount
                    )
                ) {
                    sfViewModel.task =
                        sfViewModel.task.copy(
                            base = sfViewModel.task.base.copy(
                                requestNovels = sfViewModel.task.base.requestNovels.copy(
                                    beginCount = it.beginCount,
                                    endCount = it.endCount
                                )
                            )
                        )
                }
            }
        }
    }
}



