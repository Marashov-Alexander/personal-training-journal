package ru.ok.technopolis.training.personal.items

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

open class ItemsList<Item>(var items: MutableList<Item>) {

    private val addingSubject: PublishSubject<Item> = PublishSubject.create()
    private val removingSubject: PublishSubject<Int> = PublishSubject.create()
    private val updatingSubject: PublishSubject<Int> = PublishSubject.create()
    private val updatingRangeSubject: PublishSubject<Pair<Int, Int>> = PublishSubject.create()
    private val replacingSubject: PublishSubject<List<Item>> = PublishSubject.create()
    private val isEmptySubject: PublishSubject<Boolean> = PublishSubject.create()

    fun addingSubject(): Observable<Item> {
        return addingSubject
    }

    fun removingSubject(): Observable<Int> {
        return removingSubject
    }

    fun updatingSubject(): Observable<Int> {
        return updatingSubject
    }

    fun updatingRangeSubject(): Observable<Pair<Int, Int>> {
        return updatingRangeSubject
    }

    fun replacingSubject(): Observable<List<Item>> {
        return replacingSubject
    }

    fun isEmptySubject(): Observable<Boolean> {
        return isEmptySubject
    }

    fun add(item: Item) {
        items.add(0, item)
        isEmptySubject.onNext(items.isEmpty())
        addingSubject.onNext(item)
    }

    fun remove(item: Item) {
        val ind = items.indexOf(item)
        items.remove(item)
        isEmptySubject.onNext(items.isEmpty())
        removingSubject.onNext(ind)
    }

    open fun remove(position: Int) {
        items.removeAt(position)
        isEmptySubject.onNext(items.isEmpty())
        removingSubject.onNext(position)
    }

    fun update(position: Int, item: Item) {
        items[position] = item
        updatingSubject.onNext(position)
    }

    fun rangeUpdate(start: Int, count: Int) {
        updatingRangeSubject.onNext(Pair(start, count))
    }

    fun setData(data: MutableList<Item>) {
        items = data
        isEmptySubject.onNext(items.isEmpty())
        replacingSubject.onNext(items)
    }

    fun size(): Int {
        return items.size
    }

    fun contains(item: Item): Boolean {
        return items.contains(item)
    }
}