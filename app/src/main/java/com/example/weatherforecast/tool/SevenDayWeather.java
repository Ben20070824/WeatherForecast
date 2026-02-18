package com.example.weatherforecast.tool;

import java.util.List;

public class SevenDayWeather {
    private String city;
    private List<WeatherData> data;

    public SevenDayWeather(String city, List<WeatherData> data) {
        this.city = city;
        this.data = data;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public List<WeatherData> getData() {
        return data;
    }

    public void setData(List<WeatherData> data) {
        this.data = data;
    }

    public static class WeatherData {
        private String date;
        private String wea;
        private String temDay;
        private String temNight;
        private String win;
        private String winSpeed;

        public WeatherData(String date, String wea, String temDay, String temNight, String win, String winSpeed) {
            this.date = date;
            this.wea = wea;
            this.temDay = temDay;
            this.temNight = temNight;
            this.win = win;
            this.winSpeed = winSpeed;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getWea() {
            return wea;
        }

        public void setWea(String wea) {
            this.wea = wea;
        }

        // 修正：getter和setter方法名与变量名保持一致
        public String getTemDay() {
            return temDay;
        }

        public void setTemDay(String temDay) {
            this.temDay = temDay;
        }

        public String getTemNight() {
            return temNight;
        }

        public void setTemNight(String temNight) {
            this.temNight = temNight;
        }

        public String getWin() {
            return win;
        }

        public void setWin(String win) {
            this.win = win;
        }

        public String getWinSpeed() {
            return winSpeed;
        }

        public void setWinSpeed(String winSpeed) {
            this.winSpeed = winSpeed;
        }
    }
}