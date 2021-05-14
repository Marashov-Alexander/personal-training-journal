package ru.ok.technopolis.training.personal.fragments

import android.os.Bundle
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_upper_navigation.view.*
import ru.ok.technopolis.training.personal.R
import ru.ok.technopolis.training.personal.utils.recycler.adapters.NavigationAdapter

class NavigationFragment : BaseFragment() {
    private var navigationTabs: TabLayout? = null
    private var search: View? = null
    private var tabView: ViewPager2? = null
    private var tabNames: Array<String>? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tabNames =  arrayOf(
                getString(R.string.workouts),
                getString(R.string.exercises),
                getString(R.string.lib_workouts),
                getString(R.string.lib_exercises),
                getString(R.string.authors)
        )
        navigationTabs = view.navigation_tabs
        tabView = view.tab_view
        tabView?.adapter = NavigationAdapter(this)
        print(navigationTabs?.tabCount)
        TabLayoutMediator(navigationTabs!!, tabView!!) { tab, position ->
            tab.text = tabNames!![position]
        }.attach()
        search = view.search_input
    }


    override fun getFragmentLayoutId() = R.layout.fragment_upper_navigation
}