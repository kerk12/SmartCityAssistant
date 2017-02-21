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
            for (int i = 0; i < 3; i++){
                Elder elderNew = new Elder("Γιάννης", "Νέο Ηράκλειο", Elder.Condition.GOOD);
                elders.add(elderNew);
            }
            Elder elderNew = new Elder("Mπάμπης", "Νέο Ηράκλειο", Elder.Condition.NEEDS_ATTENTION);
            elders.add(elderNew);
            Elder elderNew2 = new Elder("Χρήστος", "Νέο Ηράκλειο", Elder.Condition.NEEDS_ATTENTION_FINE);
            elders.add(elderNew2);
        }

        return elders;
    }
}
