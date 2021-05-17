package ru.ok.technopolis.training.personal.items

class SingleSelectableList<Item: SelectableItem>(items: MutableList<Item>): ItemsList<Item>(items) {

    var selectedItem: Item? = null

    fun select(item: Item) {
        selectedItem?.let {
            val oldPos = items.indexOf(it)
            it.isChosen = false
            update(oldPos, it)
        }
        val position = items.indexOf(item)
        item.isChosen = true
        update(position, item)
        selectedItem = item
    }

    fun select(position: Int) {
        select(items[position])
    }

}