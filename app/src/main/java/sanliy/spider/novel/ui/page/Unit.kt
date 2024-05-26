@file:OptIn(ExperimentalMaterial3Api::class)

package sanliy.spider.novel.ui.page

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

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
fun UnitFilterChip(text: String, selected: Boolean, onClick: () -> Unit) {
    FilterChip(selected, { onClick() }, label = { Text(text) })
}

@Composable
@Preview(showBackground = true)
fun OptionScreenTopBarPreview() {
    MaterialTheme {
        TextWithPressTopBar("TEST", {})
    }
}

