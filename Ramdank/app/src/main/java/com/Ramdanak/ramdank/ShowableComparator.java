package com.Ramdanak.ramdank;

import com.Ramdanak.ramdank.model.Showable;

import java.util.Comparator;

/**
 * compare two Showable objects used to sort listViews
 */
public class ShowableComparator implements Comparator<Showable> {

    //sort from highest priority to lowest
    public int compare(Showable first, Showable second) {
        if (first != null && second != null)
            return ((Integer)first.getPriority()).compareTo(second.getPriority());
        else if (first != null)
            return -1;
        else if (second != null)
            return 1;
        else
            return 0;
    }
}