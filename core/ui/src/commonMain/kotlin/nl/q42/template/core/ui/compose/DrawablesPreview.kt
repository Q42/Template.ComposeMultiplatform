package nl.q42.template.core.ui.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import nl.q42.template.core.ui.compose.composables.text.BodyText
import nl.q42.template.core.ui.resources._collectCommonMainDrawable0Resources
import nl.q42.template.core.ui.theme.PreviewAppTheme
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.InternalResourceApi
import org.jetbrains.compose.resources.painterResource

private data class DrawablePreviewItem(
    val name: String,
    val resource: DrawableResource,
)

/**
 * Dynamically collected from the Compose Resources generator.
 * New XML icons/images added to composeResources/drawable are automatically included
 * on the next build — no manual updates required.
 *
 * Uses the internal [_collectCommonMainDrawable0Resources] collector, which is
 * generated in the same module (core/ui) and therefore safely accessible here.
 */
@OptIn(InternalResourceApi::class)
private val allDrawables: List<DrawablePreviewItem> by lazy {
    val map = mutableMapOf<String, DrawableResource>()
    _collectCommonMainDrawable0Resources(map)
    map.entries
        .sortedBy { it.key }
        .map { (name, resource) -> DrawablePreviewItem(name = name, resource = resource) }
}

@Composable
private fun DrawableGalleryContent(
    drawables: List<DrawablePreviewItem>,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(drawables.chunked(size = 3)) { rowDrawables ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                rowDrawables.forEach { drawable ->
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Icon(
                            painter = painterResource(drawable.resource),
                            contentDescription = drawable.name,
                            modifier = Modifier.size(24.dp),
                        )
                        BodyText(
                            text = drawable.name,
                            textAlign = TextAlign.Center,
                        )
                    }
                }
                // Fill remaining cells in last row to keep columns aligned
                repeat(3 - rowDrawables.size) {
                    Column(modifier = Modifier.weight(1f)) {}
                }
            }
        }
    }
}

@Composable
@PreviewLightDark
private fun DrawablesPreview() {
    PreviewAppTheme {
        DrawableGalleryContent(
            drawables = allDrawables,
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(vertical = 8.dp),
        )
    }
}