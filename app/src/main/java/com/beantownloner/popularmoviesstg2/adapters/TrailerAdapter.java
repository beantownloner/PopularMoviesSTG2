package com.beantownloner.popularmoviesstg2.adapters;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.beantownloner.popularmoviesstg2.R;
import com.beantownloner.popularmoviesstg2.objects.Trailer;
import com.beantownloner.popularmoviesstg2.viewholders.TrailerEntryViewHolder;
import com.beantownloner.popularmoviesstg2.viewholders.TrailerHeaderViewHolder;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.ArrayList;
import java.util.List;

public class TrailerAdapter extends ExpandableRecyclerViewAdapter<TrailerHeaderViewHolder,TrailerEntryViewHolder> {

    private String TAG = TrailerAdapter.class.getSimpleName();
    private final Context context;



    public List<Trailer> getTrailers() {
        return trailers;
    }

    private List<Trailer> trailers = new ArrayList<>();


    private LayoutInflater mInflater;
    private TextView trailerTextView;


    public TrailerAdapter(Context context, List<? extends ExpandableGroup> groups) {
        super(groups);
        this.context = context;
        this.mInflater = LayoutInflater.from(context);

        Log.d(TAG,"trailer size() "+groups.size());
    }




    public Trailer getItem(int position) {
        return trailers.get(position);
    }


    @Override
    public TrailerHeaderViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        //Log.d(TAG, " in onCreateGroupViewHolder");
        View view = mInflater.inflate(R.layout.trailer_header,parent,false);
       // Log.d(TAG, " in onCreateGroupViewHolder 2 "+trailers.size());
        return new TrailerHeaderViewHolder(view);
    }

    @Override
    public TrailerEntryViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, " in onCreateChildViewHolder");
        View view = mInflater.inflate(R.layout.trailer_list,parent,false);
        return new TrailerEntryViewHolder(view);
    }

    @Override
    public void onBindChildViewHolder(TrailerEntryViewHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {
        Log.d(TAG, " in onBindChildViewHolder");
        final Trailer trailer = (Trailer) group.getItems().get(childIndex);
                holder.onBind(trailer);
       holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(context,trailer.getTitle(),Toast.LENGTH_SHORT).show();
                watchYoutubeVideo(v.getContext(),trailer.getKey());

            }
        });
    }
    public static void watchYoutubeVideo(Context context, String id){
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + id));
        try {
            context.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            context.startActivity(webIntent);
        }
    }
    @Override
    public void onBindGroupViewHolder(TrailerHeaderViewHolder holder, int flatPosition, ExpandableGroup group) {
        Log.d(TAG, " in onBindGroupViewHolder");
   holder.setTrailerHeader(group);
        Log.d(TAG, " in onBindGroupViewHolder 2");
    }


    public void setTrailerData(ArrayList<Trailer> trailerData) {

        trailers = trailerData;
        notifyDataSetChanged();
    }








}


/*

package com.beantownloner.popularmoviesstg2.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.beantownloner.popularmoviesstg2.R;

import java.util.ArrayList;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ViewHolder> {

    private String TAG = "TrailersAdapter";

    public ArrayList<String> getTrailers() {
        return trailers;
    }

    private ArrayList<String> trailers = new ArrayList<>();
    private final Context context;
    private TrailerAdapter.ItemClickListener mClickListener;
    private LayoutInflater mInflater;
    private TextView trailerTextView;


    public TrailerAdapter(Context context, ArrayList<String> trailers) {
        this.mInflater = LayoutInflater.from(context);
        this.trailers = trailers;

        this.context = context;
    }




    public String getItem(int position) {
        return trailers.get(position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG,"in onCreateViewHolder");
        View view = mInflater.inflate(R.layout.trailer_list,parent,false);
        return new TrailerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int trailerNUmber = position+1;
        String text = "Trailer "+trailerNUmber;
        trailerTextView.setText(text);

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        if (trailers.size() == 0) {
            return 0;
        }
        return trailers.size();
    }



    public void setTrailerData(ArrayList<String> trailerData) {

        trailers = trailerData;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //  TextView myTextView;

        ViewHolder(View itemView) {
            super(itemView);
            trailerTextView = itemView.findViewById(R.id.trailerTextView);
            itemView.setOnClickListener(this);


        }

       @Override
        public void onClick(View view) {

            if (mClickListener != null) {
                mClickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }


    public void setClickListener(TrailerAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}

 */