package ru.ok.technopolis.training.personal.viewholders

import android.view.View
import android.widget.TextView
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import kotlinx.android.synthetic.main.item_result.view.*
import ru.ok.technopolis.training.personal.db.model.ParameterResultModel

class ResultViewHolder(
    itemView: View
) : BaseViewHolder<ParameterResultModel>(itemView) {

    private var graphTitle: TextView = itemView.graph_title
    private var graph: GraphView = itemView.graph_layout

    override fun bind(item: ParameterResultModel) {
        graph.series.clear()
        graphTitle.text = String.format("%s (%s)", item.parameter.name, item.parameter.measureUnit)
        var x = 1.0
        val data = item.resultsParameterList.map {
            x += 1.0
            DataPoint(x, it.value.toDouble())
        }.toTypedArray()
        val series: LineGraphSeries<DataPoint> = LineGraphSeries(data)
        graph.addSeries(series)
    }
}
