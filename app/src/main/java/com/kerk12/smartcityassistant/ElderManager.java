package com.kerk12.smartcityassistant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kgiannakis on 21/2/2017.
 */

public class ElderManager {
    private static List<Elder> elders = null;

    public static List<Elder> getElders(){
        if (elders == null) {
            elders = new ArrayList<Elder>();
            Elder elderNew = new Elder("Γιάννης", "Νέο Ηράκλειο", Elder.Condition.GOOD);
            elders.add(elderNew);

            Elder elderNew3 = new Elder("Ευανθία", "Νέο Ηράκλειο", Elder.Condition.NEEDS_ATTENTION);
            elders.add(elderNew3);
            Elder elderNew2 = new Elder("Χαράλαμπος", "Νέο Ηράκλειο", Elder.Condition.NEEDS_ATTENTION_FINE);
            elders.add(elderNew2);
        }

        return elders;
    }

    public static void UpdateElderCondition(Elder.Condition condition, int index){
        Elder e = elders.get(index);
        e.setCondition(condition);
        elders.set(index, e);
    }
}
