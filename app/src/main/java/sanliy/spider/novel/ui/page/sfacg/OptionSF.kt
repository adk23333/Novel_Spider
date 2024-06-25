@file:OptIn(ExperimentalLayoutApi::class)

package sanliy.spider.novel.ui.page.sfacg

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults.filterChipColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import sanliy.spider.novel.R
import sanliy.spider.novel.Screen
import sanliy.spider.novel.TripleSwitch
import sanliy.spider.novel.UiState
import sanliy.spider.novel.net.sfacg.CharCount
import sanliy.spider.novel.net.sfacg.FinishedStatus
import sanliy.spider.novel.net.sfacg.FreeStatus
import sanliy.spider.novel.net.sfacg.NovelType
import sanliy.spider.novel.net.sfacg.SysTag
import sanliy.spider.novel.net.sfacg.UpdatedDate
import sanliy.spider.novel.onFailure
import sanliy.spider.novel.onLoading
import sanliy.spider.novel.onSuccess
import sanliy.spider.novel.room.model.Genre
import sanliy.spider.novel.room.model.SfacgNovelListTask
import sanliy.spider.novel.room.model.toTagNameList
import sanliy.spider.novel.ui.page.SingleSelectionFilterChipList
import sanliy.spider.novel.ui.page.TextWithPressTopBar
import sanliy.spider.novel.ui.page.Toast

const val TAG = "ui.page.sfacg.OptionSF"

@Composable
fun OptionSF(
    navController: NavHostController,
) {
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
    navController: NavHostController,
    sfViewModel: SFViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val text = stringResource(R.string.option_sf_13)
    val text2 = stringResource(R.string.option_sf_14)
    var isMark by remember { mutableStateOf(true) }
    val tagsState by produceState<UiState<List<SysTag>>>(UiState.Loading) {
        value = sfViewModel.getSysTags()
    }

    tagsState.onSuccess {
        sfViewModel.setTags(*it.toTypedArray())
    }

    val genresState by produceState<UiState<List<NovelType>>>(UiState.Loading) {
        value = sfViewModel.getSfacgGenres()
    }

    genresState.onSuccess {
        sfViewModel.setGenres(*it.toTypedArray())
    }

    val taskGenre: Genre? by sfViewModel.getGenreName(sfViewModel.taskState.genreID)
        .collectAsState(Genre.DefaultSFACG)
    val optionsModifier = Modifier.fillMaxWidth()

    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        Modifier
            .padding(paddingValues)
            .fillMaxSize()
            .clickable(interactionSource, null) { focusManager.clearFocus() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            sfViewModel.taskState.taskName,
            onValueChange = {
                sfViewModel.taskState = sfViewModel.taskState.copy(taskName = it)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp, 8.dp),
            label = {
                Text(stringResource(R.string.option_sf_task_name))
            },
            maxLines = 3,
            shape = RoundedCornerShape(16.dp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
                focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.primaryContainer,
                errorContainerColor = MaterialTheme.colorScheme.primaryContainer,
            )
        )

        Column(
            Modifier
                .wrapContentSize()
                .padding(32.dp, 8.dp)
                .background(MaterialTheme.colorScheme.tertiaryContainer, RoundedCornerShape(16.dp))
                .padding(16.dp),
            Arrangement.spacedBy(8.dp),
            Alignment.CenterHorizontally
        ) {
            Option(
                optionsModifier,
                stringResource(R.string.option_sf_1),
                sfViewModel.taskState.tags.toTagNameList()
            ) { title, _ ->
                Column(
                    Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .sizeIn(maxHeight = 600.dp)
                        .background(MaterialTheme.colorScheme.background)
                        .padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TagSelectedDialog(title, sfViewModel.taskState, tagsState, sfViewModel)
                }
            }

            Option(
                optionsModifier,
                stringResource(R.string.option_sf_16),
                sfViewModel.taskState.antiTags.toTagNameList()
            ) { title, _ ->
                Column(
                    Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .sizeIn(maxHeight = 600.dp)
                        .background(MaterialTheme.colorScheme.background)
                        .padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TagSelectedDialog(title, sfViewModel.taskState, tagsState, sfViewModel)
                }
            }


            Option(
                optionsModifier,
                stringResource(R.string.option_sf_2),
                "${sfViewModel.taskState.startPage} -> ${sfViewModel.taskState.endPage}"
            ) { title, _ ->
                Column(
                    optionsModifier
                        .wrapContentSize()
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.onSecondary)
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(title)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(stringResource(R.string.option_sf_4))
                        OutlinedTextField(
                            sfViewModel.taskState.startPage.toString(),
                            { v ->
                                v.toIntOrNull()?.let {
                                    sfViewModel.taskState =
                                        sfViewModel.taskState.copy(startPage = it)
                                }
                            },
                            Modifier.width(80.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        )
                        Text(text = stringResource(R.string.option_sf_5))
                        OutlinedTextField(
                            sfViewModel.taskState.endPage.toString(),
                            { v ->
                                v.toIntOrNull()?.let {
                                    sfViewModel.taskState = sfViewModel.taskState.copy(endPage = it)
                                }
                            },
                            Modifier.width(80.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                    }
                }

            }

            Option(
                optionsModifier,
                stringResource(R.string.option_sf_15),
                taskGenre?.genreName ?: Genre.DefaultSFACG.genreName
            ) { title, _ ->
                genresState
                    .onLoading {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    .onSuccess { novelTypes ->
                        SingleSelectionFilterChipList(
                            optionsModifier
                                .wrapContentSize()
                                .clip(RoundedCornerShape(10.dp))
                                .background(MaterialTheme.colorScheme.onSecondary)
                                .padding(32.dp),
                            Alignment.CenterHorizontally,
                            title,
                            novelTypes,
                            { it.typeName },
                            { it.typeId.toString() == sfViewModel.taskState.genreID }
                        ) {
                            sfViewModel.taskState =
                                sfViewModel.taskState.copy(genreID = it.typeId.toString())
                        }

                    }
                    .onFailure {
                        Icon(
                            ImageVector.vectorResource(R.drawable.wifi_valid),
                            null,
                            Modifier
                                .size(64.dp)
                                .background(MaterialTheme.colorScheme.background)
                        )
                        Toast("OptionSF-Genre", "网络错误")
                    }

            }

            Option(
                optionsModifier,
                stringResource(R.string.option_sf_7),
                sfViewModel.taskState.isFinish.zh,
            ) { title, _ ->
                SingleSelectionFilterChipList(
                    optionsModifier
                        .wrapContentSize()
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.onSecondary)
                        .padding(32.dp),
                    Alignment.CenterHorizontally,
                    title,
                    FinishedStatus.entries,
                    { it.zh },
                    { it == sfViewModel.taskState.isFinish }
                ) {
                    sfViewModel.taskState = sfViewModel.taskState.copy(isFinish = it)
                }
            }

            Option(
                optionsModifier,
                stringResource(R.string.option_sf_8),
                sfViewModel.taskState.isFree.zh,
            ) { title, _ ->
                SingleSelectionFilterChipList(
                    optionsModifier
                        .wrapContentSize()
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.onSecondary)
                        .padding(32.dp),
                    Alignment.CenterHorizontally,
                    title,
                    FreeStatus.entries,
                    { it.zh },
                    { it == sfViewModel.taskState.isFree }
                ) {
                    sfViewModel.taskState = sfViewModel.taskState.copy(isFree = it)
                }
            }

            Option(
                optionsModifier,
                stringResource(R.string.option_sf_9),
                sfViewModel.taskState.updateDate.zh,
            ) { title, _ ->
                SingleSelectionFilterChipList(
                    optionsModifier
                        .wrapContentSize()
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.onSecondary)
                        .padding(32.dp),
                    Alignment.CenterHorizontally,
                    title,
                    UpdatedDate.entries,
                    { it.zh },
                    { it == sfViewModel.taskState.updateDate }
                ) {
                    sfViewModel.taskState = sfViewModel.taskState.copy(updateDate = it)
                }
            }

            Option(
                optionsModifier,
                stringResource(R.string.option_sf_10),
                CharCount.fromValue(
                    sfViewModel.taskState.beginCount,
                    sfViewModel.taskState.endCount
                ).zh,
            ) { title, _ ->
                SingleSelectionFilterChipList(
                    optionsModifier
                        .wrapContentSize()
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.onSecondary)
                        .padding(32.dp),
                    Alignment.CenterHorizontally,
                    title,
                    CharCount.entries,
                    { it.zh },
                    {
                        it == CharCount.fromValue(
                            sfViewModel.taskState.beginCount,
                            sfViewModel.taskState.endCount
                        )
                    }
                ) {
                    sfViewModel.taskState = sfViewModel.taskState.copy(
                        beginCount = it.beginCount,
                        endCount = it.endCount
                    )
                }
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(isMark, {
                isMark = it
                sfViewModel.taskState = sfViewModel.taskState.copy(isMark = isMark)
            })
            Text(stringResource(R.string.option_sf_12))
        }

        Button(onClick = {
            if (sfViewModel.taskState.taskName.isNotBlank()) {
                var taskID: Long?
                CoroutineScope(Dispatchers.IO).launch {
                    taskID = sfViewModel.insertTask(sfViewModel.taskState)
                    withContext(Dispatchers.Main) {
                        sfViewModel.taskState = sfViewModel.taskState.copy(
                            taskID = taskID
                        )
                        navController.navigate("${Screen.SPIDER.route}?taskID=${sfViewModel.taskState.taskID}")
                    }
                }
            } else {
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
            }
        }, Modifier.width(200.dp)) {
            Text(stringResource(R.string.option_sf_11))
        }
    }
}


@Composable
fun TagSelectedDialog(
    title: String,
    taskState: SfacgNovelListTask,
    tagsState: UiState<List<SysTag>>,
    sfViewModel: SFViewModel = hiltViewModel(),
) {
    Text(title)
    FlowRow(
        Modifier.verticalScroll(rememberScrollState()),
        Arrangement.spacedBy(8.dp),
        maxItemsInEachRow = 3
    ) {
        tagsState
            .onLoading {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            .onSuccess { sysTags ->
                sysTags.forEach { sysTag ->
                    TagFilterChip(
                        sysTag,
                        when (sysTag.sysTagId.toString()) {
                            in taskState.tags.map { it.tagID } -> TripleSwitch.TRUE
                            in taskState.antiTags.map { it.tagID } -> TripleSwitch.FALSE
                            else -> TripleSwitch.NULL
                        },
                        sfViewModel
                    )
                }
            }
            .onFailure {
                Icon(
                    ImageVector.vectorResource(R.drawable.wifi_valid),
                    null,
                    Modifier.size(128.dp)
                )
                Toast(LocalContext.current.packageCodePath, it.toString())
            }
    }
}

@Composable
fun TagFilterChip(
    tag: SysTag,
    defaultSelected: TripleSwitch,
    sfViewModel: SFViewModel = hiltViewModel(),
) {
    var selected by remember {
        mutableStateOf(defaultSelected)
    }
    FilterChip(
        selected != TripleSwitch.NULL,
        onClick = {
            selected = selected.nextKey()
            when (selected) {
                TripleSwitch.FALSE -> {
                    sfViewModel.addAntiTag(tag.sysTagId.toString())
                }

                TripleSwitch.NULL -> {
                    sfViewModel.removeTag(tag.sysTagId.toString())
                }

                TripleSwitch.TRUE -> {
                    sfViewModel.addTag(tag.sysTagId.toString())
                }
            }
        },
        label = {
            Text(tag.tagName)
        },
        colors = filterChipColors(
            selectedContainerColor = MaterialTheme.colorScheme.let {
                if (selected == TripleSwitch.TRUE) it.primaryContainer else it.errorContainer
            }),
    )
}


@Composable
fun Option(
    modifier: Modifier,
    title: String,
    showValue: String,
    dialogContent: @Composable (title: String, showValue: String) -> Unit,
) {
    var show by remember { mutableStateOf(false) }
    Row(modifier, Arrangement.SpaceBetween) {
        if (show) {
            Dialog(onDismissRequest = { show = !show }) { dialogContent(title, showValue) }
        }
        Text(title)
        Text(showValue, Modifier.clickable {
            show = !show
        })
    }
}



