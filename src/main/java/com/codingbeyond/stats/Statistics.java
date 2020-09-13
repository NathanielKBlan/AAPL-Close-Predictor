package com.codingbeyond.stats;

import java.util.ArrayList;

public class Statistics {

    public static double sum(ArrayList<Double> nums){

        double sum = 0;
        for(double i : nums){
            sum += i;
        }

        return sum;
    }

    public static double mean(ArrayList<Double> nums){

        return sum(nums) / nums.size();

    }

    public static double std(ArrayList<Double> terms){

        double avg = mean(terms);
        ArrayList<Double> sumTerms = new ArrayList<>();

        for(double i : terms){
            sumTerms.add(Math.pow(i - avg, 2));
        }

        return Math.sqrt((1.0/(terms.size() - 1)) * sum(sumTerms));
    }

}
