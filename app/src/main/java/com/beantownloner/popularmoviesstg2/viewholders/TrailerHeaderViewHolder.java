package com.beantownloner.popularmoviesstg2.viewholders;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.beantownloner.popularmoviesstg2.R;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

public class TrailerHeaderViewHolder extends GroupViewHolder {
    private TextView trailerHeader;
    String TAG = TrailerHeaderViewHolder.class.getSimpleName();

    public TrailerHeaderViewHolder(View itemView) {
        super(itemView);
        trailerHeader = itemView.findViewById(R.id.trailerHeaderTextView);
    }

    public void setTrailerHeader(ExpandableGroup group) {

        trailerHeader.setText(group.getTitle());
        Log.d(TAG, "Setting the header text " + trailerHeader.getText());
    }


}