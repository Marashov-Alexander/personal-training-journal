package ru.ok.technopolis.training.personal.viewholders

import android.view.View;
import android.widget.ImageView
import android.widget.TextView
import com.facebook.drawee.view.SimpleDraweeView
import kotlinx.android.synthetic.main.fragment_account_settings.*
import kotlinx.android.synthetic.main.item_profile.view.*
import ru.ok.technopolis.training.personal.items.ProfileItem;

class ProfileViewHolder(
        itemView: View
) : BaseViewHolder<ProfileItem>(itemView) {

    private var profileImage: SimpleDraweeView = itemView.user_image
    private var profileName: TextView = itemView.profile_name
    private var profileInfo: TextView = itemView.profile_description
    private var complaint: ImageView = itemView.complaint

    override fun bind(item: ProfileItem){
        profileName.text = item.name
        profileInfo.text = item.sports.toString()
        if (item.isUser) {
            complaint.visibility = View.INVISIBLE
        } else {
           complaint.visibility = View.VISIBLE
        }
        profileImage.setImageURI(item.pictureUrlStr)
    }

}