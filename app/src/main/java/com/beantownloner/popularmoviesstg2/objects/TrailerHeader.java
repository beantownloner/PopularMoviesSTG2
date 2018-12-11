package com.beantownloner.popularmoviesstg2.objects;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class TrailerHeader extends ExpandableGroup<Trailer> {
    public TrailerHeader(String title, List<Trailer> items) {
        super(title, items);
    }
}
