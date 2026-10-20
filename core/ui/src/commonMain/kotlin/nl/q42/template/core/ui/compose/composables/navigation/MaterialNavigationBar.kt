package nl.q42.template.core.ui.compose.composables.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import nl.q42.template.core.ui.resources.Res
import nl.q42.template.core.ui.resources.sample_extension_24
import nl.q42.template.core.ui.resources.sample_home_24
import nl.q42.template.core.ui.theme.PreviewAppTheme
import org.jetbrains.compose.resources.painterResource

@Composable
internal fun MaterialNavigationBar(
    items: List<AppNavigationBarItem>,
    selectedIndex: Int,
    onItemSelected: (index: Int) -> Unit,
    onHeightChanged: (Dp) -> Unit,
    modifier: Modifier = Modifier,
) {
    val density = LocalDensity.current
    NavigationBar(
        modifier = modifier.onSizeChanged { size ->
            onHeightChanged(with(density) { size.height.toDp() })
        }
    ) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = index == selectedIndex,
                onClick = { onItemSelected(index) },
                // The label is read by screen readers, so the icon does not need a description
                icon = { Icon(painterResource(item.icon), contentDescription = null) },
                label = { Text(item.label) },
            )
        }
    }
}

@Preview
@Composable
private fun MaterialNavigationBarPreview() {
    PreviewAppTheme {
        MaterialNavigationBar(
            items = listOf(
                AppNavigationBarItem("Home", Res.drawable.sample_home_24, "sample_tab_home"),
                AppNavigationBarItem("Interop", Res.drawable.sample_extension_24, "sample_tab_interop"),
            ),
            selectedIndex = 0,
            onItemSelected = {},
            onHeightChanged = {},
        )
    }
}
