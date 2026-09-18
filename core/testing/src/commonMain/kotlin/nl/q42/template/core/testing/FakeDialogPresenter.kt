package nl.q42.template.core.testing

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import nl.q42.template.core.ui.presentation.dialog.DialogData
import nl.q42.template.core.ui.presentation.dialog.DialogPresenter
import nl.q42.template.core.ui.presentation.dialog.DialogViewState

class FakeDialogPresenter : DialogPresenter {
    private val _dialogUIState = MutableStateFlow<DialogViewState>(DialogViewState.None)
    override val dialogUIState: Flow<DialogViewState> = _dialogUIState

    override fun onDialogDismissed(tag: Any) {
        _dialogUIState.value = DialogViewState.None
    }

    override fun onDialogConfirmed(tag: Any) {
        _dialogUIState.value = DialogViewState.None
    }

    override fun showDialog(data: DialogData) {
        _dialogUIState.value = DialogViewState.ShowDialog(data)
    }
}
