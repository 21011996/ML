package KNN;

/**
 * Created by Ilya239 on 17.09.2016.
 */
class Dot {
    double x;
    double y;
    double z;
    int type;

    Dot(double x, double y, int type) {
        this.x = x;
        this.y = y;
        this.type = type;
        transform();
    }

    Dot(double x, double y, double z, int type) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.type = type;
    }

    private void transform() {
        //this.z = Math.sqrt(2.88-this.x*this.x-this.y*this.y); // spere
        this.z = 2 * this.x * this.x + 1.5 * this.y * this.y; // parabaloid
    }
}
