package com.kryak.githubclient.util

import android.view.View
import com.kryak.model.UserModel

interface UserListClickListener {
    fun onUserListItemClickListener(view: View, userModel: UserModel)
}

interface CustomClickListener {
    fun onItemClickListener(view: View, model: Any)
}
