package ru.ok.technopolis.training.personal.items

import ru.ok.technopolis.training.personal.db.entity.ParameterEntity
import ru.ok.technopolis.training.personal.db.entity.ResultEntity
import ru.ok.technopolis.training.personal.views.ProgressChartView
import java.sql.Date
import java.text.DateFormat
import kotlin.math.roundToInt

data class ExerciseResultsHelper(
        val exercise: ExerciseItem,
        val results: List<ResultEntity>
) {
    companion object {
        val formatMap : Map<ProgressChartView.ChartMode, DateFormat> = mapOf(
               Pair(ProgressChartView.ChartMode.DAY, DateFormat.getDateInstance(DateFormat.SHORT)),
                Pair(ProgressChartView.ChartMode.WEEK, DateFormat.getDateInstance(DateFormat.WEEK_OF_YEAR_FIELD)),
                Pair(ProgressChartView.ChartMode.MONTH, DateFormat.getDateInstance(DateFormat.MONTH_FIELD)),
                Pair(ProgressChartView.ChartMode.YEAR, DateFormat.getDateInstance(DateFormat.YEAR_FIELD))
        )

    }
    private val formatter: DateFormat = DateFormat.getDateInstance(DateFormat.SHORT)


    fun getResults(chartMode: ProgressChartView.ChartMode): MutableList<ProgressItem> {
      return results.map { resultEntity ->
            val result = getResult(resultEntity)
            Pair(result, formatter.format(Date(resultEntity.timestamp)))
        }.groupBy { pair ->
            pair.second
        }.map { entry ->
            ProgressItem(entry.value.sumBy { pair -> pair.first }.div(results.size).toFloat(), entry.key.substringBeforeLast('.'))
        }.toMutableList()
    }

    private fun getResult(resultEntity: ResultEntity): Int {
        var percents = when (resultEntity.resultType) {
            ParameterEntity.LESS_BETTER -> {
                resultEntity.goal * 100f / resultEntity.result
            }
            ParameterEntity.EQUALS_BETTER -> {
                100f - Math.abs(resultEntity.result - resultEntity.goal) * 100f / resultEntity.goal
            }
            ParameterEntity.GREATER_BETTER -> {
                resultEntity.result * 100f / resultEntity.goal
            }
            else -> {
                0f
            }
        }
        if (percents.isInfinite()) {
            percents = 100f
        } else if (percents.isNaN()) {
            percents = 0f
        }
        return percents.roundToInt()
    }
}