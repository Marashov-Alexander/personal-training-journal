package ru.ok.technopolis.training.personal.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
        foreignKeys = [
            ForeignKey(
                    entity = UserEntity::class,
                    parentColumns = ["id"],
                    childColumns = ["subscriptionId"]
            ),
            ForeignKey(
                    entity = UserEntity::class,
                    parentColumns = ["id"],
                    childColumns = ["subscriberId"]
            )
        ]
)

data class SubscriptionEntity (
    @ColumnInfo
    var subscriptionId: Long,
    @ColumnInfo
    var subscriberId: Long,
    @ColumnInfo
    var serverId: Long = -1L,
    @PrimaryKey(autoGenerate = true) var id: Long = 0
    ) : WithServerId {
        override fun serverId(newId: Long) {
            serverId = newId
        }
    }