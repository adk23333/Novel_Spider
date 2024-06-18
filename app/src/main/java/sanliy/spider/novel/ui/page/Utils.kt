@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)

package sanliy.spider.novel.ui.page

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun TextWithPressTopBar(
    text: String,
    onPressBack: () -> Unit,
    actions: @Composable (RowScope.() -> Unit) = {},
) {
    TopAppBar(
        { Text(text) },
        navigationIcon = {
            IconButton(onClick = { onPressBack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
            }
        },
        actions = actions
    )
}


@Composable
fun TextFilterChip(text: String, selected: Boolean, onClick: () -> Unit) {
    FilterChip(selected, { onClick() }, label = { Text(text) })
}

@Composable
fun Toast(tag: String, text: String, duration: Int = Toast.LENGTH_SHORT) {
    Log.d(tag, text)
    Toast.makeText(LocalContext.current, text, duration).show()
}

@Composable
fun CheckLeadingIcon() {
    Icon(Icons.Default.Check, null)
}

@Composable
fun ShieldLeadingIcon() {
    Icon(Icons.Default.Close, null)
}


@Composable
@Preview(showBackground = true)
fun OptionScreenTopBarPreview() {
    MaterialTheme {
        TextWithPressTopBar("TEST", {})
    }
}

@Composable
fun <T> SingleSelectionFilterChipList(
    modifier: Modifier = Modifier,
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    title: String,
    objList: List<T>,
    onChipText: (T) -> String,
    onSelected: (T) -> Boolean,
    onClick: (T) -> Unit,
) {
    Column(modifier, horizontalAlignment = horizontalAlignment) {
        Text(title)
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            objList.forEach {
                TextFilterChip(onChipText(it), onSelected(it)) { onClick(it) }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SingleSelectionFilterChipListPreview() {
    val intState = remember { mutableIntStateOf(1) }
    SingleSelectionFilterChipList(
        title = "TEST TITLE",
        objList = listOf(1, 2, 3),
        onChipText = { it.toString() },
        onSelected = { intState.intValue == it }
    ) {
        intState.intValue = it
    }
}
