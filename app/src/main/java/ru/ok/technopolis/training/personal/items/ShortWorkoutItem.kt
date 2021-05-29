package ru.ok.technopolis.training.personal.items

data class ShortWorkoutItem(
        val id: Long,
//        var timeStart: Time,
        var name: String,
//        var description: String,
        var category: String,
        var sport: String,
//        var duration: String,
        val downloadsNumber: Int,
        val rank: Double
//        val private: Boolean,
//        val invisible: Boolean
)
