package nl.q42.template.feature.samplehome.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import nl.q42.template.core.ui.compose.composables.text.BodyText
import nl.q42.template.core.ui.resources.Res
import nl.q42.template.core.ui.resources.sample_lorem_ipsum
import nl.q42.template.core.ui.theme.AppTheme
import nl.q42.template.core.ui.theme.Dimens
import nl.q42.template.core.ui.theme.PreviewAppTheme
import org.jetbrains.compose.resources.stringResource

/** Colored boxes and text, so there is something to see behind the translucent bottom bar. */
@Composable
internal fun SampleLoremIpsumBlocks(modifier: Modifier = Modifier) {
    val colors = AppTheme.colors
    val blockColors = listOf(colors.accent, colors.error, colors.surfaceSecondary, colors.textPrimary, colors.surfaceSelected)

    Column(
        verticalArrangement = spacedBy(Dimens.componentSpacingVertical),
        modifier = modifier.fillMaxWidth(),
    ) {
        blockColors.forEach { color ->
            Box(
                Modifier
                    .fillMaxWidth()
                    .aspectRatio(2f)
                    .background(color, AppTheme.shapes.medium)
            )
            BodyText(stringResource(Res.string.sample_lorem_ipsum))
        }
    }
}

@Preview
@Composable
private fun SampleLoremIpsumBlocksPreview() {
    PreviewAppTheme {
        SampleLoremIpsumBlocks()
    }
}
