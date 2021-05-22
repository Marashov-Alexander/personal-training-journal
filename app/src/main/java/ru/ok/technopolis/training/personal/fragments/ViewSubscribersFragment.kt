package ru.ok.technopolis.training.personal.fragments

import android.os.Bundle
import android.view.View
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.core.view.size
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_authors.view.*
import kotlinx.android.synthetic.main.view_appbar.*
import ru.ok.technopolis.training.personal.R
import ru.ok.technopolis.training.personal.items.ItemsList
import ru.ok.technopolis.training.personal.items.ProfileItem
import ru.ok.technopolis.training.personal.utils.recycler.adapters.AuthorsAdapter
import ru.ok.technopolis.training.personal.viewholders.AuthorViewHolder

class ViewSubscribersFragment : BaseFragment() {
    private var buttonsScroll: HorizontalScrollView? = null
    private var buttonsGroup: RadioGroup? = null

    private var recycler: RecyclerView? = null
    private var authorsMutableList = mutableListOf<ProfileItem>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recycler = view.authors_list
        buttonsScroll = view.scrollview
        buttonsGroup = view.author_categories_buttons
        activity?.base_toolbar?.title = getString(R.string.subscribers_text)
        exDummyToRecView()
    }

    override fun getFragmentLayoutId() = R.layout.fragment_authors

    private fun exDummyToRecView() {
        var list = listOf("assss", "sasab", "casada")
        for (i in 1..5) pushAuthor(i.toLong(), list)
        list = listOf("assssk", "sasaba", "casadarr")
        for (i in 1..5) pushAuthor(i.toLong(), list)
        val authorsList = ItemsList(authorsMutableList)
        setAdapter(authorsList)
        val workoutsLayoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        recycler?.layoutManager = workoutsLayoutManager

        val layoutParams: LinearLayout.LayoutParams = RadioGroup.LayoutParams(
                RadioGroup.LayoutParams.MATCH_PARENT,
                RadioGroup.LayoutParams.MATCH_PARENT)
        layoutParams.setMargins(15, 0, 15, 0)
        val sports = mutableListOf<String>()

        for (author in authorsMutableList) {
            sports.addAll(author.sports!!)
        }

        val newRadioButton = RadioButton(activity)
        newRadioButton.setText(R.string.all_filter_text)
        newRadioButton.id = 0
        newRadioButton.buttonDrawable = null
        newRadioButton.textSize = 20.0F
        newRadioButton.isChecked = true
        newRadioButton.setBackgroundResource(R.drawable.border_radio_button_selector)
        newRadioButton.setPadding(35,10,35,10)
        newRadioButton.textAlignment = View.TEXT_ALIGNMENT_CENTER
        newRadioButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.design_default_color_secondary_variant))
        buttonsGroup?.addView(newRadioButton, layoutParams)
        var i = 1
        val categories = sports.distinct()
        for (category in categories) {
            val newRB= RadioButton(activity)
            newRB.text = category
            newRB.textAlignment = View.TEXT_ALIGNMENT_CENTER
            newRB.id = i
            newRB.buttonDrawable = null
            newRB.textSize = 20.0F
            newRB.setBackgroundResource(R.drawable.border_radio_button_selector)
            newRB.setPadding(35,10,35,10)
            i ++
            buttonsGroup?.addView(newRB, layoutParams)
        }

        buttonsGroup?.setOnCheckedChangeListener { radioGroup, index ->
            print(radioGroup.checkedRadioButtonId)
            for (j in 0 until radioGroup.size)  {
                val but = radioGroup[j] as RadioButton
                but.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            }
            val button = radioGroup[radioGroup.checkedRadioButtonId] as RadioButton
            button.setTextColor(ContextCompat.getColor(requireContext(), R.color.design_default_color_secondary_variant))
            if (index != 0) {
                val newList = mutableListOf<ProfileItem>()
                for (author in authorsMutableList) {
                    val textOnButton = button.text
                    if (author.sports!!.contains(textOnButton)) {
                        newList.add(author)
                    }
                }
                val selectedAuthorsList = ItemsList(newList)
                setAdapter(selectedAuthorsList)
            } else {
                setAdapter(authorsList)
            }
        }
    }

    private fun setAdapter(list: ItemsList<ProfileItem>) {
        val authorsAdapter = AuthorsAdapter(
                holderType = AuthorViewHolder::class,
                layoutId = R.layout.item_authors,
                dataSource = list,
                onClick = { authorItem ->
                    router?.showAuthorPage(authorItem.userId)
                    println("workout ${authorItem.id} clicked") },
                onStart = { workoutItem ->
                    println("workout ${workoutItem.id} started")
                    router?.showAuthorPage(workoutItem.userId)
                }
        )
        recycler?.adapter = authorsAdapter
    }

    private fun pushAuthor(id: Long, list: List<String>) {
        authorsMutableList.add(
                ProfileItem(id.toString(), id,"dfdhf", list, false, null, 123,23,23,2)
        )
    }

}