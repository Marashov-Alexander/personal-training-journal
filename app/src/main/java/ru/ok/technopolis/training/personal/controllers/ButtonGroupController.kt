package ru.ok.technopolis.training.personal.controllers

import io.reactivex.disposables.Disposable
import ru.ok.technopolis.training.personal.items.SingleSelectableList
import ru.ok.technopolis.training.personal.items.SelectableItem
import ru.ok.technopolis.training.personal.views.SelectableButtonWrapper

class ButtonGroupController(
    private val data: SingleSelectableList<SelectableItem>,
    private val wrappers: List<SelectableButtonWrapper>
) {
    private val updateDisposable: Disposable
    init {
        updateDisposable = data.updatingSubject().subscribe { position ->
            wrappers[position].setSelected(data.items[position].isChosen)
        }
    }

    fun detach() {
        updateDisposable.dispose()
    }
}