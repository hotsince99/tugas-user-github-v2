package com.dicoding.temansaya.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.temansaya.R
import com.dicoding.temansaya.data.UserFavorite
import com.dicoding.temansaya.databinding.ItemListBinding

class ListUserAdapter : RecyclerView.Adapter<ListUserAdapter.ViewHolder>() {

    private val listUsers = ArrayList<UserFavorite>()

    fun setData(items: ArrayList<UserFavorite>) {
        listUsers.clear()
        listUsers.addAll(items)
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemListBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(user: UserFavorite) {
            binding.apply {
                Glide.with(itemView.context)
                        .load(user.avatarUrl)
                        .placeholder(R.drawable.ic_placeholder_avatar)
                        .into(imgAvatar)

                tvUsername.text = user.login

                root.setOnClickListener {
                    callback?.onItemClick(user)
                }
            }
        }
    }

    private var callback: Callback? = null

    fun setCallback(callback: Callback) {
        this.callback = callback
    }

    interface Callback {
        fun onItemClick(user: UserFavorite)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listUsers[position])
    }

    override fun getItemCount(): Int {
        return listUsers.size
    }

}