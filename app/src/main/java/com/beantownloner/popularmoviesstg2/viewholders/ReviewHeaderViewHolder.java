package com.beantownloner.popularmoviesstg2.viewholders;

import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.beantownloner.popularmoviesstg2.R;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

public class ReviewHeaderViewHolder extends GroupViewHolder {

    private TextView reviewHeader;
    String TAG = ReviewHeaderViewHolder.class.getSimpleName();


    public ReviewHeaderViewHolder(View itemView) {
        super(itemView);
        reviewHeader = itemView.findViewById(R.id.reviewHeaderTextView);
    }

    public void setReviewHeader(ExpandableGroup group) {

        reviewHeader.setText(group.getTitle());
        Log.d(TAG, "Setting the header text " + reviewHeader.getText());
    }
}
