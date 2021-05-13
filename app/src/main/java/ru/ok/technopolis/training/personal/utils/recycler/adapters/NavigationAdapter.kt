package ru.ok.technopolis.training.personal.utils.recycler.adapters

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import kotlinx.android.synthetic.main.item_workout.*
import ru.ok.technopolis.training.personal.fragments.ARG_OBJECT
import ru.ok.technopolis.training.personal.fragments.AuthorsFragment
import ru.ok.technopolis.training.personal.fragments.CategoryExercisesFragment
import ru.ok.technopolis.training.personal.fragments.CategoryWorkoutsFragment
import ru.ok.technopolis.training.personal.fragments.LibraryExercisesFragment
import ru.ok.technopolis.training.personal.fragments.LibraryWorkoutsFragment
import ru.ok.technopolis.training.personal.items.ItemsList
import kotlin.reflect.KClass

class NavigationAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 5

    override fun createFragment(position: Int): Fragment {

//        if ((position == 0) or (position == 2)) {
//                val fragment = CategoryWorkoutsFragment()
//                fragment.arguments = Bundle().apply {
//                    putInt(ARG_OBJECT, position + 1)
//                }
//                return fragment
//        } else if ((position == 1) or (position == 3)) {
//
//                val fragment = CategoryExercisesFragment()
//                fragment.arguments = Bundle().apply {
//                    putInt(ARG_OBJECT, position + 1)
//                }
//               return fragment
//        } else {
//                val fragment = AuthorsFragment()
//                fragment.arguments = Bundle().apply {
//                    putInt(ARG_OBJECT, position + 1)
//                }
//                return fragment
//        }

        if ((position == 0) or (position == 2)) {
            return if (position == 0) {
                val fragment = CategoryWorkoutsFragment()
                fragment.arguments = Bundle().apply {
                    putInt(ARG_OBJECT, position + 1)
                }
                fragment
            } else {
                val fragment =LibraryWorkoutsFragment()
                fragment.arguments = Bundle().apply {
                    putInt(ARG_OBJECT, position + 1)
                }
                fragment
            }
        } else if ((position == 1) or (position == 3)) {
            return if (position == 1) {
                val fragment = CategoryExercisesFragment()
                fragment.arguments = Bundle().apply {
                    putInt(ARG_OBJECT, position + 1)
                }
                fragment
            } else {
                val fragment = LibraryExercisesFragment()
                fragment.arguments = Bundle().apply {
                    putInt(ARG_OBJECT, position + 1)
                }
                fragment
            }
        } else {
            if (position + 1 == 5) {
                val fragment = AuthorsFragment()
                fragment.arguments = Bundle().apply {
                    putInt(ARG_OBJECT, position + 1)
                }
                return fragment
            }
            val fragment = LibraryExercisesFragment()
            fragment.arguments = Bundle().apply {
                putInt(ARG_OBJECT, position + 1)
            }
            return fragment
        }
    }
}