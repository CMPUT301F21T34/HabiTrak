package com.cmput301f21t34.habittrak.recycler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cmput301f21t34.habittrak.R;
import com.cmput301f21t34.habittrak.user.HabitEvent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


/**
 * EventAdapter
 * <p>
 * recycler view adapter for viewing events
 *
 * @author Pranav
 * @version 1.0
 * @since Nov 26, 2021
 */
public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    // attributes
    private ArrayList<HabitEvent> events;
    private final EventClickListener listener;

    public EventAdapter(ArrayList<HabitEvent> events, EventClickListener listener) {
        this.events = events;
        this.listener = listener;
    }

    // interface for listener functions
    public interface EventClickListener {
        void menuButtonClick(View view, int position);

        void itemClick(View view, int position);
    }

    /**
     * view holder for EventAdapter
     * holds and connects the views of recycler view
     */
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // views
        private final TextView comment;
        private final TextView date;
        private final ImageButton menuButton;
        // listener reference
        private EventClickListener listenerRef;

        public ViewHolder(View view) {
            super(view);
            // set views
            comment = view.findViewById(R.id.event_list_comment);
            date = view.findViewById(R.id.event_list_date);
            menuButton = view.findViewById(R.id.events_menu);
            // set listeners
            view.setOnClickListener(this);
            menuButton.setOnClickListener(this);
        }

        /**
         * getter for comment TextView
         *
         * @return textView of comment
         */
        public TextView getComment() {
            return comment;
        }

        /**
         * getter for data textView
         *
         * @return textView of data
         */
        public TextView getDate() {
            return date;
        }

        @Override
        public void onClick(View view) {
            if (listenerRef != null) {
                if (view.getId() == R.id.events_menu) {
                    // listener for menu button
                    listenerRef.menuButtonClick(view, getAdapterPosition());
                } else {
                    // listener for the whole recycler row
                    listenerRef.itemClick(view, getAdapterPosition());
                }
            }
        }

    }

    @NonNull
    @Override
    public EventAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_list_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventAdapter.ViewHolder holder, int position) {
        // set data from event
        holder.listenerRef = this.listener;
        HabitEvent event = events.get(position);
        Calendar eventDate = event.getCompletedDate();
        holder.getComment().setText(event.getComment());
        holder.getDate().setText(getDate(eventDate));
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    /**
     * get the String value from calendar
     *
     * @param calendar date to convert to string
     * @return string value of type Month, Day
     */
    public String getDate(Calendar calendar) {
        String month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
        String day = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
        return month + ", " + day;
    }
}
