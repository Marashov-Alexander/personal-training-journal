package ru.ok.technopolis.training.personal.activities

import android.os.Bundle
import androidx.lifecycle.Observer
import com.afollestad.materialdialogs.MaterialDialog
import com.mikepenz.materialdrawer.holder.ImageHolder
import com.mikepenz.materialdrawer.holder.StringHolder
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.ProfileDrawerItem
import com.mikepenz.materialdrawer.util.addItems
import com.mikepenz.materialdrawer.widget.AccountHeaderView
import kotlinx.android.synthetic.main.activity_base_fragment.*
import ru.ok.technopolis.training.personal.R
import ru.ok.technopolis.training.personal.controllers.DrawerController
import ru.ok.technopolis.training.personal.controllers.NavigationMenuController
import ru.ok.technopolis.training.personal.model.UserInfo
import ru.ok.technopolis.training.personal.navmenu.NavigationMenuListener
import ru.ok.technopolis.training.personal.repository.AuthRepository
import ru.ok.technopolis.training.personal.repository.CurrentUserRepository

abstract class DrawerActivity : BaseActivity() {

    companion object {
        const val SETTINGS_ITEM_ID = 4L
        const val EXIT_ITEM_ID = 5L
    }

    private val profile: ProfileDrawerItem = ProfileDrawerItem()

    private var drawerController: NavigationMenuController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        drawerController = DrawerController(root_drawer, slider)
        setupItems()
        slider.apply {
            onDrawerItemClickListener = { _, item, _ ->
                when (item.identifier) {
                    EXIT_ITEM_ID -> {
                        buildExitDialog()
                    }
                    SETTINGS_ITEM_ID -> router?.showSettingsPage()
                }
                closeNavMenu()
                true
            }
        }
        attachCurrentUserToSlider()
        CurrentUserRepository.currentUser.observe(this, Observer { attachCurrentUserToSlider() })
    }

    private fun buildExitDialog() {
        MaterialDialog(this).show {
            title(R.string.quit)
            message(R.string.quit_confirm_msg)
            positiveButton(R.string.preference_exit) {
                AuthRepository.doOnLogout(this@DrawerActivity)
            }
            negativeButton(R.string.close) {
                it.cancel()
            }
        }
    }

    private fun setupItems() {

        val settingsItem = PrimaryDrawerItem().apply {
            name = StringHolder(R.string.drawer_item_settings)
            icon = ImageHolder(R.drawable.ic_settings)
            identifier = SETTINGS_ITEM_ID
            isSelectable = false
        }

        val exitItem = PrimaryDrawerItem().apply {
            name = StringHolder(R.string.preference_exit)
            identifier = EXIT_ITEM_ID
            isSelectable = false
        }

        slider.addItems(
                DividerDrawerItem(),
                settingsItem,
                exitItem
        )
    }

    override fun onBackPressed() {
        if (root_drawer.isDrawerOpen(slider)) {
            root_drawer.closeDrawer(slider)
        } else {
            super.onBackPressed()
        }
    }

    fun openNavMenu() {
        drawerController?.openMenu()
    }

    fun closeNavMenu() {
        drawerController?.closeMenu()
    }

    fun addListener(listener: NavigationMenuListener) {
        drawerController?.addMenuListener(listener)
    }

    fun removeListener(listener: NavigationMenuListener) {
        drawerController?.removeMenuListener(listener)
    }

    private fun attachCurrentUserToSlider() {
        val userInfo = CurrentUserRepository.currentUser.value
        userInfo?.let {
            when {
                it.pictureUrlStr != null -> profile.icon = ImageHolder(it.pictureUrlStr)
                it.genderType == UserInfo.GenderType.MALE -> profile.icon = ImageHolder(getDrawable(R.drawable.male_stub))
                it.genderType == UserInfo.GenderType.FEMALE -> profile.icon = ImageHolder(getDrawable(R.drawable.female_stub))
                else -> profile.icon = null
            }
            profile.name = StringHolder(it.lastName + " " + it.firstName)
            profile.description = StringHolder(it.email)
            slider.accountHeader = AccountHeaderView(this).apply {
                attachToSliderView(slider)
                addProfiles(profile)
                onAccountHeaderListener = { view, profile, current ->
                    false
                }
                selectionListEnabled = false
                onAccountHeaderProfileImageListener = { _, _, _ ->
                    router?.showAccountSettingsPage()
                    true
                }
            }
        }
    }

    protected open fun canOpenNavMenuFromToolbar(): Boolean = false
}