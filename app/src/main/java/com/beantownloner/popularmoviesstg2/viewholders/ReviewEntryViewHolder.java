package com.beantownloner.popularmoviesstg2.viewholders;

import android.view.View;
import android.widget.TextView;
import com.beantownloner.popularmoviesstg2.R;
import com.beantownloner.popularmoviesstg2.objects.Review;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

public class ReviewEntryViewHolder extends ChildViewHolder {

    TextView reviewerTextView;
    TextView reviewTextView;

    public ReviewEntryViewHolder(View itemView) {
        super(itemView);

        reviewTextView = itemView.findViewById(R.id.contentTextView);
        reviewerTextView = itemView.findViewById(R.id.authorTextView);
    }

    public void onBind(Review review) {

        reviewerTextView.setText(review.getAuthor());
        reviewTextView.setText(review.getContent());
    }

}
