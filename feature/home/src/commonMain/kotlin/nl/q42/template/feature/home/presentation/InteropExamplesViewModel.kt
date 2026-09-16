package nl.q42.template.feature.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import nl.q42.template.core.navigation.viewmodel.Navigator
import nl.q42.template.domain.main.usecase.ExecuteNativeAsyncExampleMethodUseCase
import nl.q42.template.domain.main.usecase.ExecuteNativeExampleMethodUseCase
import org.koin.core.annotation.InjectedParam

class InteropExamplesViewModel(
    private val executeNativeExampleMethodUseCase: ExecuteNativeExampleMethodUseCase,
    private val executeNativeAsyncExampleMethodUseCase: ExecuteNativeAsyncExampleMethodUseCase,
    @InjectedParam private val navigator: Navigator,
) : ViewModel(), Navigator by navigator {

    fun onExecuteNativeExampleMethodClicked() {
        executeNativeExampleMethodUseCase.invoke()
    }

    fun onExecuteNativeAsyncExampleMethodClicked() {
        viewModelScope.launch {
            executeNativeAsyncExampleMethodUseCase.invoke()
        }
    }

    fun onBackClicked() {
        navigateBack()
    }
}
