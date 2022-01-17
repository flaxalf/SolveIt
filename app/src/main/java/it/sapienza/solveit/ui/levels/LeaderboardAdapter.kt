package it.sapienza.solveit.ui.levels

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import it.sapienza.solveit.R
import kotlin.collections.ArrayList

class LeaderboardAdapter(var context: Context, var personNames: ArrayList<*>) : RecyclerView.Adapter<LeaderboardAdapter.ViewHolder>() {
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
        // infalte the item Layout
        val view: View =
            LayoutInflater.from(viewGroup.context).inflate(R.layout.item, viewGroup, false)
        // set the view's size, margins, paddings and layout parameters

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element

        //TODO: viewHolder.positionTV.text = "integer_pos"
        viewHolder.squadTV.text = personNames[position].toString()
        //TODO: viewHolder.timeTV.text = "time_string"
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = personNames.size

}