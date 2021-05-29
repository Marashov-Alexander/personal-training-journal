package ru.ok.technopolis.training.personal.items

import kotlin.math.max
import kotlin.math.min

class ExercisesList(items: MutableList<ExerciseItem>): ItemsList<ExerciseItem>(items) {

    private var maxSupersetGroupId = 0

    init {
        items.sortBy {it.workoutExercise.orderIndex}
        maxSupersetGroupId =
            if (items.isEmpty())
                0
            else items
                .map { it.workoutExercise.supersetGroupId ?: 0 }
                .reduce { acc, i -> max(acc, i) } + 1
        configureSuperset(items)
    }


    fun newSupersetMode() {
        for (i in 0 until items.size) {
            if (items[i].workoutExercise.supersetGroupId == null) {
                items[i].checked = false
            } else {
                items[i].checked = null
            }
        }
        rangeUpdate(0, items.size)
    }

    private fun getPrev(position: Int): ExerciseItem? {
        return if (position <= 0) null else items[position - 1]
    }

    private fun getNext(position: Int): ExerciseItem? {
        return if (position >= items.size - 1) null else items[position + 1]
    }

    override fun remove(position: Int) {
        val left = getPrev(position)
        super.remove(position)
        val prev = getPrev(position - 1)
        val right = getNext(position - 1)
        val next = getNext(position)

        left?.let {
            if (left.isSingleExerciseSuperset(prev, right)) {
                // TODO: remove superset
                left.workoutExercise.supersetGroupId = null
            }
            left.cornerMode = left.getCornerMode(prev, right)
            left.counterVisibility = left.getCounterMode(left.cornerMode)
            update(position - 1, left)
        }
        right?.let {
            print("wow")
            if (right.isSingleExerciseSuperset(left, next)) {
                // TODO: remove superset
                right.workoutExercise.supersetGroupId = null
            }
            right.cornerMode = right.getCornerMode(left, next)
            right.counterVisibility = right.getCounterMode(right.cornerMode)
            update(position, right)
        }
    }

    fun supersetFromChosen() {
        val superset = mutableListOf<Pair<Int, ExerciseItem>>()
        val others = mutableListOf<ExerciseItem>()
        for (i in 0 until items.size) {
            if (items[i].checked == true) {
                superset.add(Pair(i, items[i]))
            } else {
                others.add(items[i])
            }
        }
        if (superset.size > 1) {
            val groupId = maxSupersetGroupId++
            var minPos = Int.MAX_VALUE
            for ((position, item) in superset) {
                minPos = min(minPos, position)
                item.workoutExercise.supersetGroupId = groupId
                item.workoutExercise.counter = 1
            }
            others.addAll(minPos, superset.map { it.second })
            configureSuperset(others)
        } else {
            for (i in 0 until items.size) {
                items[i].checked = null
            }
            rangeUpdate(0, items.size)
        }
    }

    private fun configureSuperset(data: MutableList<ExerciseItem>) {
        for (i in 0 until data.size) {
            val item = data[i]
            val prevItem = if (i == 0) null else data[i - 1]
            val nextItem = if (i == data.size - 1) null else data[i + 1]
            item.cornerMode = item.getCornerMode(prevItem, nextItem)
            item.counterVisibility = item.getCounterMode(item.cornerMode)
            item.checked = null
        }
        setData(data)
    }

}