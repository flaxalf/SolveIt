package it.sapienza.solveit.ui

import android.content.Context
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import it.sapienza.solveit.R
import org.json.JSONObject
import kotlin.collections.ArrayList

class LeaderboardAdapter(var context: Context, private var dataset: JSONObject) : RecyclerView.Adapter<LeaderboardAdapter.ViewHolder>() {
    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val positionTV: TextView
        val squadTV: TextView
        val timeTV: TextView

        init {
            positionTV = view.findViewById(R.id.positionTV)
            squadTV = view.findViewById(R.id.squadNameTV)
            timeTV = view.findViewById(R.id.timeTV)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(viewGroup.context).inflate(R.layout.item, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        var realPosition = position + 1
        val squad = dataset.getJSONArray(realPosition.toString()).getString(0)
        val time = dataset.getJSONArray(realPosition.toString()).getString(1)

        viewHolder.positionTV.text = realPosition.toString()
        viewHolder.squadTV.text = squad
        viewHolder.timeTV.text = time

    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataset.length()

}