package com.example.demo.util;

import com.example.demo.domain.BaseStation;
import com.example.demo.domain.MobileStation;

public class CalculateDetectionDistance {

//    distance formula used to calculate distance between mobile station and base station
    public static Float calculate(BaseStation baseStation, MobileStation mobileStation){
        Float horizontalChange = baseStation.getX() - mobileStation.getLastKnownX();
        Float verticalChange = baseStation.getY() - mobileStation.getLastKnownY();
        return new Float(Math.sqrt(Math.pow(horizontalChange, 2) + Math.pow(verticalChange, 2)));
    }
}
