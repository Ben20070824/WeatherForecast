package com.example.weatherforecast.tool;

import java.util.List;

public class ManyDayWeather {
    private String city;
    private List<WeatherInfo> list;

    public ManyDayWeather(String city, List<WeatherInfo> list) {
        this.city = city;
        this.list = list;
    }

    public List<WeatherInfo> getList() {
        return list;
    }

    public void setList(List<WeatherInfo> list) {
        this.list = list;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public static class WeatherInfo {
        String date;
        String wea;
        String tem_high;
        String tem_low;
        String win;

        String weaDay;
        String weaNight;

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

        public String getTem_high() {
            return tem_high;
        }

        public void setTem_high(String tem_high) {
            this.tem_high = tem_high;
        }

        public String getTem_low() {
            return tem_low;
        }

        public void setTem_low(String tem_low) {
            this.tem_low = tem_low;
        }

        public String getWin() {
            return win;
        }

        public void setWin(String win) {
            this.win = win;
        }

        public String getWeaDay() {
            return weaDay;
        }

        public void setWeaDay(String weaDay) {
            this.weaDay = weaDay;
        }

        public String getWeaNight() {
            return weaNight;
        }

        public void setWeaNight(String weaNight) {
            this.weaNight = weaNight;
        }

        public WeatherInfo(String date, String wea, String tem_high, String tem_low, String win, String weaDay, String weaNight) {
            this.date = date;
            this.wea = wea;
            this.tem_high = tem_high;
            this.tem_low = tem_low;
            this.win = win;
            this.weaDay = weaDay;
            this.weaNight = weaNight;
        }

    }
}
