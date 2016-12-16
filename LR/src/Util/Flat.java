package Util;

/**
 * Created by shambala on 25/09/16.
 */
public class Flat {
    public double price;
    public double area;
    public double roomsCount;

    public Flat(double area, double roomsCount, double price) {
        this.price = price;
        this.area = area;
        this.roomsCount = roomsCount;
    }

    public Flat(double area, double roomsCount, double price, boolean flag) {
        this.price = price;
        this.area = area;
        this.roomsCount = roomsCount;
    }
}
