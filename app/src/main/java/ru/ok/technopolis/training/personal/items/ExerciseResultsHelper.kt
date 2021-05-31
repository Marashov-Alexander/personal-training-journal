package ru.ok.technopolis.training.personal.items

import ru.ok.technopolis.training.personal.db.entity.ExerciseEntity
import ru.ok.technopolis.training.personal.db.entity.ParameterEntity
import ru.ok.technopolis.training.personal.db.entity.ResultEntity
import ru.ok.technopolis.training.personal.views.ProgressChartView
import java.sql.Date
import java.text.DateFormat
import kotlin.math.roundToInt

data class ExerciseResultsHelper(
        val exercise: ExerciseEntity,
        val results: List<ResultEntity>
) {
    private val formatter = DateFormat.getDateInstance(DateFormat.SHORT)

    fun getResults(chartMode: ProgressChartView.ChartMode): MutableList<ProgressItem> {
        return results.map { resultEntity ->
            val result = getResult(resultEntity)
            val dateStr = getDateStr(chartMode, resultEntity.timestamp)
            Pair(result, dateStr)
        }.groupBy { pair ->
            pair.second
        }.map { entry ->
            ProgressItem(entry.value.sumBy { pair -> pair.first }.div(results.size).toFloat(), entry.key.substringBeforeLast('.'))
        }.toMutableList()
    }

    private fun getDateStr(chartMode: ProgressChartView.ChartMode, timestamp: Long): String {
        val str = formatter.format(Date(timestamp))

        val delimiter = if (str.contains('.')) '.' else '/'
        return when (chartMode) {
            ProgressChartView.ChartMode.DAY -> str
            ProgressChartView.ChartMode.WEEK -> TODO()
            ProgressChartView.ChartMode.MONTH -> str.substringAfter(delimiter)
            ProgressChartView.ChartMode.YEAR -> str.substringAfter(delimiter).substringAfter(delimiter)
        }

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