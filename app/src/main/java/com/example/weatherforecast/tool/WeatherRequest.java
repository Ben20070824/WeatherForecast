package com.example.weatherforecast.tool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class WeatherRequest {



    private String streamToString(InputStream inputStream) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line).append('\n');
        }
        reader.close();
        return stringBuilder.toString();
    }
}
