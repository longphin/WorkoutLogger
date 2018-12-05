package com.longlife.workoutlogger.AndroidUtils;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.longlife.workoutlogger.R;

import java.util.ArrayList;
import java.util.List;

// String Adapter for a list of strings. The search functionality is not case-sensitive.
public class StringArrayAdapter
        extends ArrayAdapter<String> {
    // List of strings to search through, which will be filtered.
    private List<String> dataList;
    // Context.
    private Context mContext;
    // Layout for views to inflate.
    private int itemLayout;
    // Filter to determine which strings to keep in results.
    private ListFilter listFilter = new ListFilter();
    // List of all strings that this adapter was filled with. It will not be filtered.
    private List<String> dataListAllItems;

    public StringArrayAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
        super(context, resource, objects);

        dataList = objects;
        mContext = context;
        itemLayout = resource;
    }


    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public String getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {
        if (view == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
        }

        TextView strName = view.findViewById(R.id.textView);
        strName.setText(getItem(position));
        return view;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        if (listFilter == null) {
            listFilter = new ListFilter();
        }
        return listFilter;
    }

    // Set the full list of items for the adapter.
    public void setList(List<String> items) {
        dataListAllItems = items;
    }

    // Check if the string is within the possible options.
    public boolean contains(String s) {
        // If Build is of high enough level, can do the search efficiently.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return dataList.stream().anyMatch(s::equalsIgnoreCase);
        }

        // Else, loop through entire list.
        for (String exerciseName : dataList) {
            if (exerciseName.equalsIgnoreCase(s)) {
                return true;
            }
        }

        // Searched item was not found.
        return false;
    }

    // This class will filter through the strings as the user types.
    private class ListFilter
            extends Filter {
        // Keep method calls in synch.
        private final Object lock = new Object();


        @Override
        // Start filtering results.
        protected FilterResults performFiltering(CharSequence prefix) {
            // Get list of all options.
            FilterResults results = new FilterResults();
            if (dataListAllItems == null) {
                synchronized (lock) {
                    dataListAllItems = new ArrayList<>(dataList);
                }
            }

            // No filter, so we return the entire list of items.
            if (prefix == null || prefix.length() == 0) {
                synchronized (lock) {
                    results.values = dataListAllItems;
                    results.count = dataListAllItems.size();
                }
            } else { // Else, filter by string.
                // The string to look for.
                final String searchStrLowerCase = prefix.toString().toLowerCase();
                // List of matched items.
                ArrayList<String> matchValues = new ArrayList<>();
                // Go through all items and check if there are matches.
                for (String dataItem : dataListAllItems) {
                    if (dataItem.toLowerCase().contains(searchStrLowerCase)) {
                        // A match!
                        matchValues.add(dataItem);
                    }
                }

                // Return filtered list.
                results.values = matchValues;
                results.count = matchValues.size();
            }

            return results;
        }

        @Override
        // When list is filtered, notify the changes.
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.values != null) { // Filtered list contains values.
                dataList = (ArrayList<String>) results.values;
            } else { // Else, filtered list is empty.
                dataList = null;
            }

            if (results.count > 0) { // When filtered list contains values, notify the changes.
                notifyDataSetChanged();
            } else { // Else, notify that the list is invalid.
                notifyDataSetInvalidated();
            }
        }
    }

}

