package ru.ok.technopolis.training.personal.items

import ru.ok.technopolis.training.personal.db.entity.LevelExerciseParameterEntity
import ru.ok.technopolis.training.personal.db.entity.ParameterEntity
import ru.ok.technopolis.training.personal.items.interfaces.WithId

data class ParameterItem(
    override val id: String,
    val parameter: ParameterEntity,
    val levelExerciseParameterEntity: LevelExerciseParameterEntity,
    var editable: Boolean,
    var invisible: Boolean = false
) : WithId {
    constructor(item: ParameterItem, newLevel: Int) : this(
        item.id,
        item.parameter,
        LevelExerciseParameterEntity(item.levelExerciseParameterEntity, newLevel),
        editable = true
    )
}
