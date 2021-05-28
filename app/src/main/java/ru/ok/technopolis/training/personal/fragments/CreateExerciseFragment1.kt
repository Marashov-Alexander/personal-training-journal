package ru.ok.technopolis.training.personal.fragments

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Spinner
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_new_exercise_1.*
import kotlinx.android.synthetic.main.view_appbar.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.ok.technopolis.training.personal.R
import ru.ok.technopolis.training.personal.db.entity.ExerciseEntity
import ru.ok.technopolis.training.personal.db.entity.ExerciseParameterEntity
import ru.ok.technopolis.training.personal.db.entity.LevelExerciseParameterEntity
import ru.ok.technopolis.training.personal.db.entity.ParameterEntity
import ru.ok.technopolis.training.personal.db.entity.UserLevelEntity
import ru.ok.technopolis.training.personal.fragments.dialogs.ParameterDialogFragment
import ru.ok.technopolis.training.personal.items.ItemsList
import ru.ok.technopolis.training.personal.items.ParameterItem
import ru.ok.technopolis.training.personal.lifecycle.Page.Companion.EXERCISE_ID_KEY
import ru.ok.technopolis.training.personal.lifecycle.Page.Companion.USER_ID_KEY
import ru.ok.technopolis.training.personal.lifecycle.Page.Companion.WORKOUT_ID_KEY
import ru.ok.technopolis.training.personal.utils.recycler.adapters.ParameterAdapter
import ru.ok.technopolis.training.personal.viewholders.ParameterViewHolder
import java.lang.Integer.max
import kotlin.random.Random


class CreateExerciseFragment1 : BaseFragment(), ParameterDialogFragment.ParameterDialogListener {

    private var nextStepCard: MaterialCardView? = null
    private var parametersRecycler: RecyclerView? = null
    private var parametersList: ItemsList<ParameterItem>? = null
    private var addParameterBtn: FloatingActionButton? = null

    private var levelsMap: MutableMap<Int, MutableList<ParameterItem>> = mutableMapOf()
    private var levelsCount: Int = 0
    private var levelsList: MutableList<String> = mutableListOf()

    private var levelSpinner: Spinner? = null

    private var addLevelBtn: FloatingActionButton? = null
    private var removeLevelBtn: FloatingActionButton? = null
    private var currentLevel: Int = 0

    private var userId: Long = -1L
    private var workoutId: Long = -1L
    private var exerciseId: Long = -1L
    private lateinit var exercise: ExerciseEntity
    private lateinit var userLevel: UserLevelEntity

    private lateinit var nameTextView: AutoCompleteTextView
    private lateinit var exerciseTypeSpinner: Spinner

    private val removedParameters = mutableSetOf<Long>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.base_toolbar?.title = getString(R.string.exercise_creation)
        activity?.let {
            userId = (it.intent.extras?.get(USER_ID_KEY)) as Long
            workoutId = (it.intent.extras?.get(WORKOUT_ID_KEY)) as Long
            exerciseId = (it.intent.extras?.get(EXERCISE_ID_KEY)) as Long
        }

        clearData()
        loadExerciseInfo(userId, workoutId, exerciseId) { exercise, userLevel, levelsMap, maxLevel ->

            this.userLevel = userLevel
            this.exercise = exercise
            this.levelsMap = levelsMap

            for (i in 1..maxLevel) {
                createNewLevel(false)
            }
            setCurrentLevel(userLevel.level)

            nameTextView.setText(exercise.name)

            if (levelsList.isEmpty()) {
                createNewLevel()
                levelsMap[currentLevel] = mutableListOf()
            }
            nextStepCard?.setOnClickListener {
                saveChanges {
                    router?.showNewExercisePage2()
                }
            }
            addParameterBtn?.setOnClickListener {
                createNewParameter()
            }
            val arrayAdapter = ArrayAdapter<String>(requireContext(), R.layout.spinner_item, levelsList)
            arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
            levelSpinner?.adapter = arrayAdapter
            levelSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    setCurrentLevel(position + 1)
                }
            }

            addLevelBtn?.setOnClickListener {
                createNewLevel()
            }

            removeLevelBtn?.setOnClickListener {
                removeLastLevel()
            }

            parametersList = ItemsList(levelsMap[currentLevel]!!)
            parametersRecycler = parameters_recycler
            parametersRecycler?.adapter = ParameterAdapter(
                holderType = ParameterViewHolder::class,
                layoutId = R.layout.item_parameter_short,
                dataSource = parametersList!!,
                onEdit = {
                    ParameterDialogFragment(it, this)
                        .show(requireActivity().supportFragmentManager, "ParameterDialogFragment")
                },
                onValueChanged = { newValue, item ->
                    println("Changed ${item?.id} to $newValue")
                    item?.levelExerciseParameterEntity?.value = newValue
                }
            )
            val layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            parametersRecycler?.layoutManager = layoutManager

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

            levelSpinner?.setSelection(userLevel.level - 1)
        }

        exerciseTypeSpinner = exercise_type_spinner
        nameTextView = name
        nextStepCard = next_step_card
        levelSpinner = level_value
        addLevelBtn = increase_level
        removeLevelBtn = decrease_level
        addParameterBtn = add_parameter_button
    }

    private fun loadExerciseInfo(
        userId: Long,
        workoutId: Long,
        exerciseId: Long,
        actionsAfter: (
            exercise: ExerciseEntity,
            userLevel: UserLevelEntity,
            levelsMap: MutableMap<Int, MutableList<ParameterItem>>,
            maxLevel: Int
        ) -> Unit) {
        GlobalScope.launch(Dispatchers.IO) {
            var maxLevel = 0
            database!!.let {
                val exercise = it.exerciseDao().getById(exerciseId)
                val exerciseParameters = it.exerciseParameterDao().getAllByExercise(exerciseId)
                val parameters = it.exerciseParameterDao().getParametersForExercise(exerciseId)
                val levelsMap = mutableMapOf<Int, MutableList<ParameterItem>>()
                parameters.forEach { param ->
                    val exerciseParameter = exerciseParameters.find { exerciseParam -> exerciseParam.parameterId == param.id }!!
                    val levels = it.levelExerciseParameterDao().getAllByExerciseParameterId(exerciseParameter.id)

                    for (levelExerciseParam in levels) {
                        val parameterItems = levelsMap.getOrPut(levelExerciseParam.level, { mutableListOf() })
                        parameterItems.add(ParameterItem(
                            Random.nextInt().toString(),
                            param,
                            levelExerciseParam,
                            editable = true
                        ))
                        maxLevel = max(maxLevel, levelExerciseParam.level)
                    }
                }
                val workoutExercise = it.workoutExerciseDao().getById(workoutId, exerciseId)
                val usrLvl = it.userLevelDao().getById(userId, workoutExercise.id)
                val userLevel =
                    if (usrLvl == null) {
                        val newUserLevel = UserLevelEntity(userId, workoutExercise.id, 1)
                        it.userLevelDao().insert(newUserLevel)
                        newUserLevel
                    } else {
                        usrLvl
                    }
                withContext(Dispatchers.Main) {
                    actionsAfter.invoke(exercise, userLevel, levelsMap, maxLevel)
                }
            }

        }
    }

    private fun clearData() {
        levelsMap.clear()
        levelsCount = 0
        levelsList.clear()
        removedParameters.clear()
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
            levelSpinner?.setSelection(levelsCount - 1)
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
        if (select) levelSpinner?.setSelection(levelsCount - 1)
    }

    override fun getFragmentLayoutId(): Int = R.layout.fragment_new_exercise_1

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
        ).show(requireActivity().supportFragmentManager, "ParameterDialogFragment")
    }

    private fun saveChanges(actionsAfter: () -> Unit) {
        GlobalScope.launch(Dispatchers.IO) {
            database?.let { database ->
                for (entry in levelsMap.entries) {
                    val parameters = entry.value
                    for (parameter in parameters) {
                        val parameterEntity = parameter.parameter
                        val levelEntity = parameter.levelExerciseParameterEntity
                        if (parameterEntity.id == 0L) {
                            parameterEntity.id = database.parameterDao().insert(parameterEntity)
                        } else {
                            database.parameterDao().update(parameterEntity)
                        }
                        val exerciseParametersList = database.exerciseParameterDao().getByExerciseAndParameter(exerciseId, parameterEntity.id)
                        var exerciseParameterEntity = exerciseParametersList.getOrNull(0)
                        if (exerciseParameterEntity == null) {
                            exerciseParameterEntity = ExerciseParameterEntity(exerciseId, parameterEntity.id)
                            exerciseParameterEntity.id = database.exerciseParameterDao().insert(exerciseParameterEntity)
                        }
                        levelEntity.exerciseParameterId = exerciseParameterEntity.id

                        val levelExerciseParameterEntityList = database.levelExerciseParameterDao().getById(levelEntity.level, levelEntity.exerciseParameterId)
                        val levelExerciseParameterEntity = levelExerciseParameterEntityList.getOrNull(0)
                        if (levelExerciseParameterEntity == null) {
                            database.levelExerciseParameterDao().insert(levelEntity)
                        } else {
                            database.levelExerciseParameterDao().update(levelEntity)
                        }
                    }
                }
                // очистка удалённых уровней
                database.levelExerciseParameterDao().deleteLevelsGreaterThan(levelsCount, exerciseId)
                // очистка удалённых параметров
                for (removedParameterId in removedParameters) {
                    database.exerciseParameterDao().delete(exerciseId, removedParameterId)
                }
                // сохранение упражнения
                exercise.name = nameTextView.text.toString()
                database.exerciseDao().update(exercise)
            }
            withContext(Dispatchers.Main) {
                removedParameters.clear()
                actionsAfter.invoke()
            }
        }
    }

}
