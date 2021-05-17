package ru.ok.technopolis.training.personal.items

data class BundleItem(
    override val id: String,
    val itemId: Int,
    val title: String
) : SelectableItem(id)
