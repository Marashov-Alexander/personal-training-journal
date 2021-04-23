package ru.ok.technopolis.training.personal.activities.settings

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.preference.PreferenceFragmentCompat
import kotlinx.android.synthetic.main.activity_base_fragment.*
import kotlinx.android.synthetic.main.view_appbar.*
import ru.ok.technopolis.training.personal.R
import ru.ok.technopolis.training.personal.activities.AppbarActivity

abstract class BaseSettingsActivity : AppbarActivity() {

    companion object {
        const val EMPTY_KEY = "KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        println("On create 1")
        super.onCreate(savedInstanceState)
        setSettingsFragment()
        println("On create 2")
    }

    private fun setSettingsFragment() {
        supportFragmentManager.beginTransaction().add(R.id.main_container, getPreferencesFragment()).commit()
        supportFragmentManager.executePendingTransactions()
    }

    override fun getToolbarView(): Toolbar = base_toolbar

    override fun getActivityLayoutId(): Int = R.layout.activity_base_fragment

    abstract fun getPreferencesFragment(): PreferenceFragmentCompat

    override fun canOpenNavMenuFromToolbar(): Boolean = false

    override fun isBottomNavVisible(): Boolean = false
}
