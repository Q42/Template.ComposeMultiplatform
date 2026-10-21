package nl.q42.template.compose

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertHeightIsEqualTo
import androidx.compose.ui.test.assertWidthIsEqualTo
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.runComposeUiTest
import androidx.compose.ui.unit.Density
import nl.q42.template.core.ui.compose.composables.widgets.AppLoadingIndicator
import nl.q42.template.core.ui.resources.Res
import nl.q42.template.core.ui.resources.loading
import nl.q42.template.core.ui.theme.AppTheme
import nl.q42.template.core.ui.theme.Dimens
import org.jetbrains.compose.resources.getString
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class AppLoadingIndicatorTest {

    @Test
    fun isAnnouncedAsIndeterminateLoading() = runComposeUiTest {
        val description = getString(Res.string.loading)
        setContent {
            AppTheme {
                AppLoadingIndicator()
            }
        }

        onNodeWithContentDescription(description)
            .assert(SemanticsMatcher.expectValue(SemanticsProperties.ProgressBarRangeInfo, ProgressBarRangeInfo.Indeterminate))
    }

    @Test
    fun keepsItsSizeWhenTextIsScaled() = runComposeUiTest {
        val description = getString(Res.string.loading)
        setContent {
            val density = LocalDensity.current
            CompositionLocalProvider(LocalDensity provides Density(density.density, fontScale = 2f)) {
                AppTheme {
                    AppLoadingIndicator(size = Dimens.loadingIndicatorSizeSmall)
                }
            }
        }

        onNodeWithContentDescription(description)
            .assertWidthIsEqualTo(Dimens.loadingIndicatorSizeSmall)
            .assertHeightIsEqualTo(Dimens.loadingIndicatorSizeSmall)
    }
}
