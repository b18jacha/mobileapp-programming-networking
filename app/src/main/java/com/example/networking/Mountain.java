package com.example.networking;

public class Mountain {

    private String name;
    private String location;
    private int cost;


    //Constructor for a mountain
    public Mountain(String mName, String mLocation, int mCost) {
        name = mName;
        location = mLocation;
        cost = mCost;
    }

    @Override public String toString() {
        return name;
    }

    //Method that returns the name of mountain and two other properties
    public String info() {
        String str=name;
        str+=" is located in: ";
        str+=location;
        str+=" and the trip cost: ";
        str+=cost;
        str+="kr.";

        return str;
    }

}
