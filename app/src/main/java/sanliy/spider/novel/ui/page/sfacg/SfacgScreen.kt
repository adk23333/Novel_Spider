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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
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
import sanliy.spider.novel.model.NovelPlatform
import sanliy.spider.novel.net.sfacg.CharCount
import sanliy.spider.novel.net.sfacg.FinishedStatus
import sanliy.spider.novel.net.sfacg.FreeStatus
import sanliy.spider.novel.net.sfacg.NovelType
import sanliy.spider.novel.net.sfacg.SysTag
import sanliy.spider.novel.net.sfacg.UpdatedDate
import sanliy.spider.novel.onFailure
import sanliy.spider.novel.onLoading
import sanliy.spider.novel.onSuccess
import sanliy.spider.novel.room.model.GenreImpl
import sanliy.spider.novel.room.model.SfacgNovelListTaskImpl
import sanliy.spider.novel.room.model.toTagNameList
import sanliy.spider.novel.ui.page.SingleSelectionFilterChipList
import sanliy.spider.novel.ui.page.TextWithPressTopBar

const val TAG = "ui.page.sfacg.OptionSF"

@Composable
fun SfacgScreen(
    navController: NavHostController,
) {
    Scaffold(
        Modifier.fillMaxSize(),
        topBar = {
            TextWithPressTopBar(
                stringResource(Screen.OPTION_SF.stringId),
                { navController.popBackStack() })
        },
    ) {
        SfacgContext(it, navController)
    }
}

@Composable
fun SfacgContext(
    paddingValues: PaddingValues,
    navController: NavHostController,
    sfacgViewModel: SfacgViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val text = stringResource(R.string.option_sf_13)
    var isMark by remember { mutableStateOf(true) }
    val tagsState by produceState<UiState<List<SysTag>>>(UiState.Loading) {
        value = sfacgViewModel.getSysTags()
    }

    tagsState.onSuccess {
        sfacgViewModel.setTags(*it.toTypedArray())
    }

    val genresState by produceState<UiState<List<NovelType>>>(UiState.Loading) {
        value = sfacgViewModel.getSfacgGenres()
    }

    genresState.onSuccess {
        sfacgViewModel.setGenres(*it.toTypedArray())
    }
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
            sfacgViewModel.taskState.taskName,
            onValueChange = {
                sfacgViewModel.taskState = sfacgViewModel.taskState.copy(taskName = it)
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
                sfacgViewModel.taskState.tags.toTagNameList()
            ) { title, _ ->
                Column(
                    Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .sizeIn(maxHeight = 600.dp)
                        .background(MaterialTheme.colorScheme.background)
                        .padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TagSelectedDialog(title, sfacgViewModel.taskState, tagsState, sfacgViewModel)
                }
            }

            Option(
                optionsModifier,
                stringResource(R.string.option_sf_16),
                sfacgViewModel.taskState.antiTags.toTagNameList()
            ) { title, _ ->
                Column(
                    Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .sizeIn(maxHeight = 600.dp)
                        .background(MaterialTheme.colorScheme.background)
                        .padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TagSelectedDialog(title, sfacgViewModel.taskState, tagsState, sfacgViewModel)
                }
            }


            Option(
                optionsModifier,
                stringResource(R.string.option_sf_2),
                "${sfacgViewModel.taskState.startPage} -> ${sfacgViewModel.taskState.endPage}"
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
                            sfacgViewModel.taskState.startPage.toString(),
                            { v ->
                                v.toIntOrNull()?.let {
                                    sfacgViewModel.taskState =
                                        sfacgViewModel.taskState.copy(startPage = it)
                                }
                            },
                            Modifier.width(80.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        )
                        Text(text = stringResource(R.string.option_sf_5))
                        OutlinedTextField(
                            sfacgViewModel.taskState.endPage.toString(),
                            { v ->
                                v.toIntOrNull()?.let {
                                    sfacgViewModel.taskState =
                                        sfacgViewModel.taskState.copy(endPage = it)
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
                sfacgViewModel.taskState.genre.genreName
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
                            { it.typeId.toString() == sfacgViewModel.taskState.genre.genreID }
                        ) {
                            sfacgViewModel.taskState =
                                sfacgViewModel.taskState.copy(
                                    genre = GenreImpl(
                                        it.typeId.toString(),
                                        NovelPlatform.SFACG,
                                        it.typeName
                                    )
                                )
                        }
                    }
                    .onFailure {
                        NetworkErrorDialogContent(
                            MaterialTheme.colorScheme.errorContainer,
                            MaterialTheme.shapes.large
                        )
                    }

            }

            Option(
                optionsModifier,
                stringResource(R.string.option_sf_7),
                sfacgViewModel.taskState.isFinish.zh,
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
                    { it == sfacgViewModel.taskState.isFinish }
                ) {
                    sfacgViewModel.taskState = sfacgViewModel.taskState.copy(isFinish = it)
                }
            }

            Option(
                optionsModifier,
                stringResource(R.string.option_sf_8),
                sfacgViewModel.taskState.isFree.zh,
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
                    { it == sfacgViewModel.taskState.isFree }
                ) {
                    sfacgViewModel.taskState = sfacgViewModel.taskState.copy(isFree = it)
                }
            }

            Option(
                optionsModifier,
                stringResource(R.string.option_sf_9),
                sfacgViewModel.taskState.updateDate.zh,
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
                    { it == sfacgViewModel.taskState.updateDate }
                ) {
                    sfacgViewModel.taskState = sfacgViewModel.taskState.copy(updateDate = it)
                }
            }

            Option(
                optionsModifier,
                stringResource(R.string.option_sf_10),
                CharCount.fromValue(
                    sfacgViewModel.taskState.beginCount,
                    sfacgViewModel.taskState.endCount
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
                            sfacgViewModel.taskState.beginCount,
                            sfacgViewModel.taskState.endCount
                        )
                    }
                ) {
                    sfacgViewModel.taskState = sfacgViewModel.taskState.copy(
                        beginCount = it.beginCount,
                        endCount = it.endCount
                    )
                }
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(isMark, {
                isMark = it
                sfacgViewModel.taskState = sfacgViewModel.taskState.copy(isMark = isMark)
            })
            Text(stringResource(R.string.option_sf_12))
        }

        Button(
            onClick = {
                if (sfacgViewModel.taskState.taskName.isNotBlank()) {
                    var taskID: Long?
                    CoroutineScope(Dispatchers.IO).launch {
                        taskID = sfacgViewModel.insertTask(sfacgViewModel.taskState)
                        withContext(Dispatchers.Main) {
                            sfacgViewModel.taskState = sfacgViewModel.taskState.copy(
                                taskID = taskID
                            )
                            navController.navigate("${Screen.SPIDER.route}?taskID=${sfacgViewModel.taskState.taskID}")
                        }
                    }
                } else {
                    Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
                }
            },
            Modifier.width(200.dp),
            colors = ButtonDefaults.buttonColors().copy(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                contentColor = MaterialTheme.colorScheme.onTertiaryContainer
            )
        ) {
            Text(stringResource(R.string.option_sf_11))
        }
    }
}


@Composable
fun TagSelectedDialog(
    title: String,
    taskState: SfacgNovelListTaskImpl,
    tagsState: UiState<List<SysTag>>,
    sfacgViewModel: SfacgViewModel = hiltViewModel(),
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
                        sfacgViewModel
                    )
                }
            }
            .onFailure {
                NetworkErrorDialogContent()
            }
    }
}

@Composable
fun TagFilterChip(
    tag: SysTag,
    defaultSelected: TripleSwitch,
    sfacgViewModel: SfacgViewModel = hiltViewModel(),
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
                    sfacgViewModel.addAntiTag(tag.sysTagId.toString())
                }

                TripleSwitch.NULL -> {
                    sfacgViewModel.removeTag(tag.sysTagId.toString())
                }

                TripleSwitch.TRUE -> {
                    sfacgViewModel.addTag(tag.sysTagId.toString())
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

@Composable
fun NetworkErrorDialogContent(color: Color = Color.Unspecified, shape: Shape = RectangleShape) {
    Column(
        Modifier
            .background(color, shape)
            .padding(32.dp),
        Arrangement.Center,
        Alignment.CenterHorizontally
    ) {
        Icon(
            Icons.Filled.Warning, null, Modifier.size(64.dp),
            MaterialTheme.colorScheme.error
        )
        Text(
            stringResource(R.string.option_sf_17),
            style = MaterialTheme.typography.displaySmall
        )
    }
}



