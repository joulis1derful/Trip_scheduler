package com.joulis1derful.tripscheduler.model;

public class TripInfo {
    private City city_from;
    private City city_to;
    private String date_from;
    private String date_to;
    private String time_from;
    private String time_to;
    private String info_from;
    private String info_to;
    private String info;
    private int price;
    private int bus_id;
    private int reservation_count;

   public static class City {
        private int id;
        private int highlight;
        private String name;

        public City(int id, int highlight, String name) {
            this.id = id;
            this.highlight = highlight;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public int getHighlight() {
            return highlight;
        }

        public String getName() {
            return name;
        }
    }

    public TripInfo(City city_from, City city_to, String date_from, String date_to, String time_from,
                    String time_to, String info_from, String info_to, String info, int price,
                    int bus_id, int reservation_count) {
        this.city_from = city_from;
        this.city_to = city_to;
        this.date_from = date_from;
        this.date_to = date_to;
        this.time_from = time_from;
        this.time_to = time_to;
        this.info_from = info_from;
        this.info_to = info_to;
        this.info = info;
        this.price = price;
        this.bus_id = bus_id;
        this.reservation_count = reservation_count;
    }

    public City getCity_from() {
        return city_from;
    }

    public City getCity_to() {
        return city_to;
    }

    public String getDate_from() {
        return date_from;
    }

    public String getDate_to() {
        return date_to;
    }

    public String getTime_from() {
        return time_from;
    }

    public String getTime_to() {
        return time_to;
    }

    public String getInfo_from() {
        return info_from;
    }

    public String getInfo_to() {
        return info_to;
    }

    public String getInfo() {
        return info;
    }

    public int getPrice() {
        return price;
    }

    public int getBus_id() {
        return bus_id;
    }

    public int getReservation_count() {
        return reservation_count;
    }
}
