package ru.ok.technopolis.training.personal.fragments

import android.os.Bundle
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_upper_navigation.view.*
import ru.ok.technopolis.training.personal.R
import ru.ok.technopolis.training.personal.utils.recycler.adapters.NavigationAdapter

class NavigationFragment: BaseFragment() {
    private var navigationTabs: TabLayout? = null
    private var search: View? = null
    private var tabView: ViewPager2? = null
    private val tabNames: Array<String> = arrayOf(
            "Тренировки",
            "Упражнения",
            "Библиотека\nтренировок",
            "Библиотека\nупражнений",
            "Авторы"
    )
    private var adapter: NavigationAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigationTabs = view.navigation_tabs
        search = view.search_input
        tabView = view.tab_view
        adapter = NavigationAdapter(this)
        tabView?.adapter = adapter
        TabLayoutMediator(navigationTabs!!, tabView!!) { tab, position ->
            tab.text = tabNames[position]
        }.attach()
    }


    override fun getFragmentLayoutId() = R.layout.fragment_upper_navigation
}