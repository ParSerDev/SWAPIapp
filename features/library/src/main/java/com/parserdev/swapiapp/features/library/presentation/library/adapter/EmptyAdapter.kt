package com.parserdev.swapiapp.features.library.presentation.library.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.parserdev.characters.R

class EmptyAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return object :
            RecyclerView.ViewHolder(View.inflate(parent.context, R.layout.empty_view, null)) {}
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {}

    override fun getItemCount(): Int = 1
}