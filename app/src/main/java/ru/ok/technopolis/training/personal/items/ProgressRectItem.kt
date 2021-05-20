package ru.ok.technopolis.training.personal.items

import ru.ok.technopolis.training.personal.items.interfaces.WithId

class ProgressRectItem(
    override val id: String,
    var status: ProgressStatus
) : WithId {
    enum class ProgressStatus {
        PASSED, FAILED, CURRENT, FUTURE
    }
}
