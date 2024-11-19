package com.vegagroup4.opsctimetracker.Entries

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vegagroup4.opsctimetracker.R
import java.text.SimpleDateFormat
import java.util.Locale

class EntriesListAdapter(private val entriesList: List<EntryData>) : RecyclerView.Adapter<EntriesListAdapter.ViewHolder>()
{
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.tv_Title)
        val projectTextView: TextView = itemView.findViewById(R.id.tv_Project)
        val categoryTextView: TextView = itemView.findViewById(R.id.tv_Category)
        val startTimeTextView: TextView = itemView.findViewById(R.id.tv_StartTimeAndDate)
        val endTimeTextView: TextView = itemView.findViewById(R.id.tv_EndTimeAndDate)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EntriesListAdapter.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.entry_view, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: EntriesListAdapter.ViewHolder, position: Int) {
        val currentItem = entriesList[position]
        holder.titleTextView.text = currentItem.title
        holder.projectTextView.text = currentItem.project.name
        holder.categoryTextView.text = currentItem.category.name
        holder.startTimeTextView.text = formatDateTime(currentItem.startTime)
        holder.endTimeTextView.text = formatDateTime(currentItem.endTime)
    }

    override fun getItemCount(): Int {
        return entriesList.size
    }

    private fun formatDateTime(dateTime: DateTimeData): String
    {
        // TODO: Fix DateTime format not working
        // val dateFormat = SimpleDateFormat("dd/MMM/yyyy hh:mm", Locale.getDefault())
        // return dateFormat.format(dateTime)
        return "${dateTime.day}/${dateTime.month}/${dateTime.year} | ${dateTime.hour}:${dateTime.minute}"
    }
}