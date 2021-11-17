package com.cmput301f21t34.habittrak.recycler;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cmput301f21t34.habittrak.R;
import com.cmput301f21t34.habittrak.user.HabitEvent;

import java.util.ArrayList;
import java.util.Calendar;


//A custom list, adapted from lab code. Creates the view for each list item.
public class EventList extends ArrayAdapter<HabitEvent> {
    private ArrayList<HabitEvent> events;
    private Context context;

    public EventList(Context context, ArrayList<HabitEvent> events){
        super(context,0, events);
        this.events = events;
        this.context = context;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup
            parent) {
        View view = convertView;

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.event_list_row, parent,false);
        }

        HabitEvent event = events.get(position);

        TextView comment = view.findViewById(R.id.event_list_comment);
        TextView date = view.findViewById(R.id.event_list_date);

        Calendar eventDate = event.getCompletedDate();

        comment.setText(event.getComment());
        date.setText((CharSequence) eventDate);

        return view;
    }
}
