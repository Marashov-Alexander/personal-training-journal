package ru.ok.technopolis.training.personal.items

import ru.ok.technopolis.training.personal.items.interfaces.WithId

class ProgressRectItem(
    override val id: String,
    var status: Int
) : WithId {

    companion object {
        const val STATUS_FUTURE = 0
        const val STATUS_CURRENT = 1
        const val STATUS_PASSED = 2
        const val STATUS_FAILED = 3
    }
}
