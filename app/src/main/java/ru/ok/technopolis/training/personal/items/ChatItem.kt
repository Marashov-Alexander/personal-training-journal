package ru.ok.technopolis.training.personal.items

import android.text.BoringLayout
import ru.ok.technopolis.training.personal.items.interfaces.WithId
import java.sql.Time

data class ChatItem (
        override val id: String,
        val chatId: Long,
        val name: String,
        val pictureUrlStr: String?,
        val description: String?,
        val unreadMessages: Int,
        val lastMessageTime: Time?,
        val participants: List<ProfileItem>,
        val owner: ProfileItem
) : WithId