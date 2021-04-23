package ru.ok.technopolis.training.personal.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_base_fragment.*
import kotlinx.android.synthetic.main.view_appbar.*
import ru.ok.technopolis.training.personal.R
import ru.ok.technopolis.training.personal.fragments.BaseFragment
import ru.ok.technopolis.training.personal.lifecycle.Page
import ru.ok.technopolis.training.personal.lifecycle.Page.Companion.PAGE_KEY
import ru.ok.technopolis.training.personal.utils.logger.Logger
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

open class BaseFragmentActivity : AppbarActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        nav_view.selectedItemId = R.id.nav_home_item
        nav_view.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home_item -> {
                    router?.showWorkoutPlanPage()
                }
                R.id.nav_stat_item -> {
                    router?.showResultsPage()
                }
                R.id.nav_nav_item -> {
                    router?.showSettingsPage()
                }
                R.id.nav_profile_item -> {
                    router?.showCalendarPage()
                }
            }
            true
        }

        val fragment: Page.Fragment? = intent.getSerializableExtra(PAGE_KEY) as? Page.Fragment

        fragment?.let {
            setSupportingFragment(it.clazz)
        }
    }

    private fun setSupportingFragment(clazz: KClass<out BaseFragment>) {
        val supportingFragment = clazz.createInstance()
        Logger.d(this, "Set fragment ${supportingFragment::class.simpleName}")
        supportFragmentManager.beginTransaction().add(R.id.main_container, supportingFragment).commit()
        supportFragmentManager.executePendingTransactions()
    }

    fun setFragment(fragment: Fragment) {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.main_container, fragment)
        ft.addToBackStack(null)
        ft.setCustomAnimations(
                android.R.animator.fade_in, android.R.animator.fade_out)
        ft.commit()
    }

    fun setPrevFragment() {
        val fm = supportFragmentManager
        if (fm.backStackEntryCount > 0) {
            fm.popBackStack()
        } else {
            Logger.e(this, "Fragment stack is empty!")
        }
    }

    override fun getActivityLayoutId() = R.layout.activity_base_fragment

    override fun getToolbarView(): Toolbar = base_toolbar
}
