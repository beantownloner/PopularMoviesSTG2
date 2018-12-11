package com.beantownloner.popularmoviesstg2.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.beantownloner.popularmoviesstg2.R;
import com.beantownloner.popularmoviesstg2.objects.Review;
import com.beantownloner.popularmoviesstg2.viewholders.ReviewEntryViewHolder;
import com.beantownloner.popularmoviesstg2.viewholders.ReviewHeaderViewHolder;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.ArrayList;
import java.util.List;

public class ReviewAdapter extends ExpandableRecyclerViewAdapter<ReviewHeaderViewHolder, ReviewEntryViewHolder> {


    private String TAG = "ReviewAdapter";

    private List<Review> reviews = new ArrayList<>();
    private final Context context;

    private LayoutInflater mInflater;
    private TextView authorTextView;
    private TextView reviewTextView;

    public ReviewAdapter(Context context, List<? extends ExpandableGroup> groups) {
        super(groups);
        Log.d(TAG, " review size() " + groups.size());
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
    }


    public Review getItem(int position) {
        return reviews.get(position);
    }


    @Override
    public ReviewHeaderViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.review_header, parent, false);
        // Log.d(TAG, " in onCreateGroupViewHolder 2 "+trailers.size());
        return new ReviewHeaderViewHolder(view);
    }

    @Override
    public ReviewEntryViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.reviews_list, parent, false);
        return new ReviewEntryViewHolder(view);
    }

    @Override
    public void onBindChildViewHolder(ReviewEntryViewHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {
        final Review review = (Review) group.getItems().get(childIndex);
        holder.onBind(review);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
    }

    @Override
    public void onBindGroupViewHolder(ReviewHeaderViewHolder holder, int flatPosition, ExpandableGroup group) {
        holder.setReviewHeader(group);
    }

    public void setReviewsData(ArrayList<Review> reviewData) {

        reviews = reviewData;
        notifyDataSetChanged();
    }


}
