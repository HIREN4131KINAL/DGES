package com.tranetech.dges;

/**
 * Created by marka on 15-Apr-16.
 */
public class GetIP {


    public String updateip(String name) {
        String link = "http://54.254.175.172/GPSAttandence/" + name;
        return link;
    }

}