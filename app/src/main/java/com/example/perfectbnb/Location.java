package com.example.perfectbnb;



public class Location {


    int id;
    String name;
    String longitude,latitude;




    public Location(int id, String name,String longitude,String latitude){
        this.id = id;
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public Location(String name,String longitude,String latitude){
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;

    }


    public Location(){
        id = 0;
        name = "";
        longitude = "";
        latitude = "";

    }
}