package ru.ok.technopolis.training.personal.views

import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ru.ok.technopolis.training.personal.R
import ru.ok.technopolis.training.personal.db.entity.LevelExerciseParameterEntity
import ru.ok.technopolis.training.personal.db.entity.ParameterEntity
import ru.ok.technopolis.training.personal.db.entity.UserLevelEntity
import ru.ok.technopolis.training.personal.fragments.BaseFragment
import ru.ok.technopolis.training.personal.fragments.dialogs.ParameterDialogFragment
import ru.ok.technopolis.training.personal.items.ItemsList
import ru.ok.technopolis.training.personal.items.ParameterItem
import ru.ok.technopolis.training.personal.utils.recycler.adapters.ParameterAdapter
import ru.ok.technopolis.training.personal.viewholders.ParameterViewHolder
import java.lang.Integer.min
import java.lang.Integer.max
import kotlin.random.Random

class ExerciseParametersWrapper(
    private val fragment: BaseFragment,
    parametersRecycler: RecyclerView,
    addParameterBtn: FloatingActionButton,
    addLevelBtn: FloatingActionButton,
    removeLevelBtn: FloatingActionButton,
    private val levelSpinner: Spinner,
    var levelsMap: MutableMap<Int, MutableList<ParameterItem>>,
    userLevel: UserLevelEntity?,
    maxLevel: Int,
    editable: Boolean

) : ParameterDialogFragment.ParameterDialogListener {

    private var parametersList: ItemsList<ParameterItem>? = null
    private var levelsList: MutableList<String> = mutableListOf()
    private var currentLevel: Int = 0

    var levelsCount: Int = 0
    val removedParameters = mutableSetOf<Long>()

    init {
        for (i in 1..maxLevel) {
            createNewLevel(false)
        }
        setCurrentLevel(userLevel?.level ?: 1)

        if (levelsList.isEmpty()) {
            createNewLevel()
            levelsMap[currentLevel] = mutableListOf()
        }

        addParameterBtn.setOnClickListener {
            createNewParameter()
        }
        val arrayAdapter = ArrayAdapter<String>(fragment.requireContext(), R.layout.spinner_item, levelsList)
        arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        levelSpinner.adapter = arrayAdapter
        levelSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                setCurrentLevel(position + 1)
            }
        }

        parametersList = ItemsList(levelsMap[currentLevel]!!)
        parametersRecycler.adapter = ParameterAdapter(
            holderType = ParameterViewHolder::class,
            layoutId = R.layout.item_parameter_short,
            dataSource = parametersList!!,
            editable = editable,
            onEdit = {
                ParameterDialogFragment(it, this)
                    .show(fragment.requireActivity().supportFragmentManager, "ParameterDialogFragment")
            },
            onValueChanged = { newValue, item ->
                println("Changed ${item?.id} to $newValue")
                item?.levelExerciseParameterEntity?.value = newValue
            }
        )
        val layoutManager = LinearLayoutManager(fragment.requireContext(), RecyclerView.VERTICAL, false)
        parametersRecycler.layoutManager = layoutManager

        if (editable) {
            addLevelBtn.setOnClickListener {
                createNewLevel()
            }
            removeLevelBtn.setOnClickListener {
                removeLastLevel()
            }

            val simpleItemTouchCallback: ItemTouchHelper.SimpleCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                    val position = viewHolder.adapterPosition
                    removeParameterAt(position)
                }
            }
            val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
            itemTouchHelper.attachToRecyclerView(parametersRecycler)
            addParameterBtn.visibility = VISIBLE
        } else {
            addLevelBtn.setOnClickListener {
                levelSpinner.setSelection(min(maxLevel - 1, currentLevel))
            }
            removeLevelBtn.setOnClickListener {
                levelSpinner.setSelection(max(0, currentLevel - 2))
            }
            addParameterBtn.visibility = INVISIBLE
        }

        if (userLevel == null) {
            levelSpinner.setSelection(0)
        } else {
            levelSpinner.setSelection(userLevel.level - 1)
        }
    }

    private fun removeParameterAt(position: Int) {
        for (entry in levelsMap.entries) {
            if (entry.key != currentLevel) {
                entry.value.removeAt(position)
            }
        }
        val parameterItem: ParameterItem? = parametersList?.items?.get(position)
        // для удаления параметра из упражнения необходимо записать его id и при сохранении удалить связь между параметром и упражнением
        parameterItem?.let {
            removedParameters.add(it.parameter.id)
        }
        parametersList?.remove(position)
    }

    private fun removeLastLevel() {
        if (levelsCount > 1) {
            levelsCount--
            levelsList.removeAt(levelsCount)
            levelsMap.remove(levelsCount + 1)
            levelSpinner.setSelection(levelsCount - 1)
        }
    }

    private fun setCurrentLevel(level: Int) {
        currentLevel = level
        var levelsList = levelsMap[currentLevel]
        if (levelsList == null) {
            levelsList = mutableListOf()
            val oldList = levelsMap[currentLevel - 1]
            oldList?.let {
                for (parameterItem in oldList) {
                    levelsList.add(ParameterItem(parameterItem, currentLevel))
                }
            }
        }
        levelsMap[currentLevel] = levelsList
        parametersList?.setData(levelsList)
    }

    private fun createNewLevel(select: Boolean = true) {
        levelsCount++
        levelsList.add("Уровень $levelsCount")
        if (select) levelSpinner.setSelection(levelsCount - 1)
    }

    override fun onSaveParameterClick(item: ParameterItem) {
        println(item.toString())
        parametersList?.let {
            val indexOf = it.indexOf(item)
            if (indexOf == -1) {
                for (entry in levelsMap.entries) {
                    if (entry.key != currentLevel) entry.value.add(ParameterItem(item, entry.key))
                }
                it.addLast(item)
            } else {
                it.update(indexOf, item)
            }
        }
    }

    private fun createNewParameter() {
        ParameterDialogFragment(
            ParameterItem(
                Random.nextInt().toString(),
                ParameterEntity("", ""),
                LevelExerciseParameterEntity(currentLevel, 0.0f, -1),
                true
            ),
            this
        ).show(fragment.requireActivity().supportFragmentManager, "ParameterDialogFragment")
    }

}