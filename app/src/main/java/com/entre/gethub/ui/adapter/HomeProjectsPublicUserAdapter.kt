package com.entre.gethub.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.entre.gethub.R
import com.entre.gethub.data.remote.response.UserPublicProfileResponse
import com.entre.gethub.databinding.ItemPublicProjectdiselesaikanBinding

class HomeProjectsPublicUserAdapter(
    private var projectsList: MutableList<UserPublicProfileResponse.Data.Projects>,
    private val listener: (UserPublicProfileResponse.Data.Projects, Int) -> Unit
) : RecyclerView.Adapter<HomeProjectsPublicUserAdapter.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPublicProjectdiselesaikanBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(projectsList[position])
    }

    override fun getItemCount(): Int = projectsList.size

    fun setProjectsList(projectsList: MutableList<UserPublicProfileResponse.Data.Projects>) {
        this.projectsList = projectsList
        notifyDataSetChanged()
    }

    class ViewHolder(
        private val itemProjectsBinding: ItemPublicProjectdiselesaikanBinding,
        private val listener: (UserPublicProfileResponse.Data.Projects, Int) -> Unit,
    ) : RecyclerView.ViewHolder(itemProjectsBinding.root) {

        fun bindItem(projects: UserPublicProfileResponse.Data.Projects) {
            with(itemProjectsBinding) {
                tvProjectTitle.text = projects.title
                tvProjectSentiment.text = projects.sentiment
            }
        }
    }




}
