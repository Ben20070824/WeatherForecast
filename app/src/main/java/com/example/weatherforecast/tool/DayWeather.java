package com.example.weatherforecast.tool;

public class DayWeather {
    String city;
    String date;
    String wea;
    String tem_high;
    String tem_low;
    String win;
    String win_speed;
    String win_meter;
    String air;
    String pressure;
    String humidity;

    public DayWeather() {
    }

    public DayWeather(String city,
                      String date,
                      String wea,
                      String tem_high,
                      String tem_low,
                      String win,
                      String win_speed,
                      String win_meter,
                      String air,
                      String pressure,
                      String humidity) {

        this.city = city;
        this.date = date;
        this.wea = wea;
        this.tem_high = tem_high;
        this.tem_low = tem_low;
        this.win = win;
        this.win_speed = win_speed;
        this.win_meter = win_meter;
        this.air = air;
        this.pressure = pressure;
        this.humidity = humidity;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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

    public String getWin_speed() {
        return win_speed;
    }

    public void setWin_speed(String win_speed) {
        this.win_speed = win_speed;
    }

    public String getWin_meter() {
        return win_meter;
    }

    public void setWin_meter(String win_meter) {
        this.win_meter = win_meter;
    }

    public String getAir() {
        return air;
    }

    public void setAir(String air) {
        this.air = air;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }
}
