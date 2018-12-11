package com.beantownloner.popularmoviesstg2.objects;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class ReviewHeader extends ExpandableGroup<Review> {
    public ReviewHeader(String title, List<Review> items) {
        super(title, items);
    }
}
