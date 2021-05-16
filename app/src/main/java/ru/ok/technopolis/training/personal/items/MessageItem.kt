package ru.ok.technopolis.training.personal.items

import ru.ok.technopolis.training.personal.items.interfaces.WithId
import java.sql.Time

class MessageItem (
        override val id: String,
        val text: String,
        val sendTime: Time,
        //TODO: use id in the future
        val senderId: ProfileItem,
        val chatId: Long,
        val invitation: RequestItem?,
        val workout: ShortWorkoutItem?,
        val exercise: ShortExerciseItem?
) : WithId
