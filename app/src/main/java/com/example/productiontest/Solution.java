package com.example.productiontest;

import android.util.Log;

class Solution {
    public String solution(String my_string, int n) {
        String answer = "";
        String[] array = my_string.split("");
        Log.e("cyc", "array => " + array.length);
        for(int i = 0; i<my_string.length(); i++){
            for(int j=0; j<n; j++){
                answer=array[j];
            }
        }
        return answer;
    }
}
