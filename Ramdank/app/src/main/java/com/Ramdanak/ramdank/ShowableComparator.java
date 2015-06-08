package com.Ramdanak.ramdank;

import com.Ramdanak.ramdank.model.Showable;

import java.util.Comparator;

/**
 * compare two Showable objects used to sort listViews
 */
public class ShowableComparator implements Comparator<Showable> {

    //sort from highest priority to lowest
    public int compare(Showable first, Showable second) {
         return ((Integer)first.getPriority()).compareTo(second.getPriority());
    }
}


