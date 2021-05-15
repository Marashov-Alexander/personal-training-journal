package ru.ok.technopolis.training.personal.db.model

import ru.ok.technopolis.training.personal.db.entity.ParameterEntity
import ru.ok.technopolis.training.personal.db.entity.ParameterResultEntity

data class ParameterResultModel(
    var parameter: ParameterEntity,
    var resultsParameterList: MutableList<ParameterResultEntity>
)
