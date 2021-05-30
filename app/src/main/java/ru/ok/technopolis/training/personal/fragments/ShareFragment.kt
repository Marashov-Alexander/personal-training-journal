package ru.ok.technopolis.training.personal.fragments

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_share.view.*
import kotlinx.android.synthetic.main.view_appbar.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.ok.technopolis.training.personal.R
import ru.ok.technopolis.training.personal.db.entity.MessageEntity
import ru.ok.technopolis.training.personal.items.ItemsList
import ru.ok.technopolis.training.personal.items.ProfileItem
import ru.ok.technopolis.training.personal.lifecycle.Page
import ru.ok.technopolis.training.personal.repository.CurrentUserRepository
import ru.ok.technopolis.training.personal.utils.recycler.adapters.AuthorsAdapter
import ru.ok.technopolis.training.personal.viewholders.AuthorViewHolder

class ShareFragment: UserFragment() {
    private var peoplesRecycler: RecyclerView? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var workoutId = (activity?.intent?.extras?.get(Page.WORKOUT_ID_KEY)) as Long?
        if (workoutId == 0L) {workoutId = null}
        var exerciseId = (activity?.intent?.extras?.get(Page.EXERCISE_ID_KEY)) as Long?
        if (exerciseId == 0L) {exerciseId = null}
        val userId = CurrentUserRepository.currentUser.value?.id
        super.onViewCreated(view, savedInstanceState)
        activity?.base_toolbar?.title = getString(R.string.share)
        peoplesRecycler = view.people_list


            getChats(userId!!) { authors ->
                val authorsList = ItemsList(authors)
                val workoutsLayoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
                peoplesRecycler?.layoutManager = workoutsLayoutManager

                val sports = mutableListOf<String>()
                for (author in authors) {
                    sports.addAll(author.sports)
                }
                setAdapter(authorsList, userId, workoutId, exerciseId)
            }

        }

    private fun setAdapter(list: ItemsList<ProfileItem>, userId: Long, workoutId: Long?, exerciseId: Long?) {
        val authorsAdapter = AuthorsAdapter(
                holderType = AuthorViewHolder::class,
                layoutId = R.layout.item_authors,
                dataSource = list,
                onClick = { authorItem ->
                    println("workout ${authorItem.id} clicked")
                    formAndSend(userId, authorItem, workoutId, exerciseId)
                },
                onStart = { authorItem  ->

                    formAndSend(userId, authorItem, workoutId, exerciseId)
                }
        )
        peoplesRecycler?.adapter = authorsAdapter
    }

    private fun formAndSend(userId: Long, authorItem: ProfileItem, workoutId: Long?, exerciseId: Long?) {
        GlobalScope.launch(Dispatchers.IO) {
            database!!.let {
                val message = MessageEntity(getString(R.string.want_to_share), System.currentTimeMillis(), userId, authorItem.userId, workoutId, exerciseId, true)
                val messageId = it.messageDao().insert(message)
                withContext(Dispatchers.Main) {
                    router?.showChatPage(authorItem.userId, messageId)
                }
            }
        }
    }

    override fun getFragmentLayoutId() = R.layout.fragment_share
}