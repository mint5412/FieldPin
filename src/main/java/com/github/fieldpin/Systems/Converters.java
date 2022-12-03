package com.github.fieldpin.Systems;

public class Converters {
    public boolean isConvertDouble(String num){
        try {
            Double.parseDouble(num);
            return true;
        }catch (Exception e) {
            return false;
        }
    }
}
