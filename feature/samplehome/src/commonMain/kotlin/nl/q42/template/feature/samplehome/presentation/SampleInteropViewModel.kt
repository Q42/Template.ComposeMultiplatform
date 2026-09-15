package nl.q42.template.feature.samplehome.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import nl.q42.template.core.navigation.viewmodel.Navigator
import nl.q42.template.domain.main.usecase.SampleExecuteNativeAsyncMethodUseCase
import nl.q42.template.domain.main.usecase.SampleExecuteNativeMethodUseCase
import org.koin.core.annotation.InjectedParam

class SampleInteropViewModel(
    private val executeNativeMethodUseCase: SampleExecuteNativeMethodUseCase,
    private val executeNativeAsyncMethodUseCase: SampleExecuteNativeAsyncMethodUseCase,
    @InjectedParam private val navigator: Navigator,
) : ViewModel(), Navigator by navigator {

    fun onExecuteNativeMethodClicked() {
        executeNativeMethodUseCase.invoke()
    }

    fun onExecuteNativeAsyncMethodClicked() {
        viewModelScope.launch {
            executeNativeAsyncMethodUseCase.invoke()
        }
    }

    fun onBackClicked() {
        navigateBack()
    }
}
