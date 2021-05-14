package ru.ok.technopolis.training.personal.utils.recycler.adapters

import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.ok.technopolis.training.personal.fragments.AuthorsFragment
import ru.ok.technopolis.training.personal.fragments.BaseFragment
import ru.ok.technopolis.training.personal.fragments.CategoryExercisesFragment
import ru.ok.technopolis.training.personal.fragments.CategoryWorkoutsFragment
import ru.ok.technopolis.training.personal.fragments.LibraryExercisesFragment
import ru.ok.technopolis.training.personal.fragments.LibraryWorkoutsFragment

class NavigationAdapter(fragment: BaseFragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 5

    override fun createFragment(position: Int): BaseFragment {

        return when (position) {
            0 -> {
                CategoryWorkoutsFragment()
            }
            2 -> {
                LibraryWorkoutsFragment()
            }
            1 -> {
                CategoryExercisesFragment()
            }
            3 -> {
              LibraryExercisesFragment()
            }
            4 -> {
                 AuthorsFragment()
            }
            else -> {
                return LibraryExercisesFragment()
            }
        }
    }
}