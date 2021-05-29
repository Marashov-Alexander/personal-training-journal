package ru.ok.technopolis.training.personal.items

import ru.ok.technopolis.training.personal.items.interfaces.WithId

data class ProfileItem (
    override val id: String,
    val userId: Long,
    val name: String,
    val sports: List<String>,
    val isUser: Boolean,
    val pictureUrlStr: String?,
    val subscribersNumber: Int,
    val subscriptionsNumber: Int,
    val sharedTrainingsNumber: Int,
    val sharedExercisesNumber: Int
) : WithId
