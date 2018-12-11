package com.beantownloner.popularmoviesstg2.viewholders;

import android.view.View;
import android.widget.TextView;

import com.beantownloner.popularmoviesstg2.R;
import com.beantownloner.popularmoviesstg2.objects.Trailer;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

public class TrailerEntryViewHolder extends ChildViewHolder {
    TextView trailerTextView;

    public TrailerEntryViewHolder(View itemView) {
        super(itemView);
        trailerTextView = itemView.findViewById(R.id.trailerTextView);


    }

    public void onBind(Trailer trailer) {

        trailerTextView.setText(trailer.getTitle());


    }


}
