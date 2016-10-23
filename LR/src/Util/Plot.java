package Util; /**
 * Created by shambala on 25/09/16.
 */

import com.xeiam.xchart.*;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Ilya239 on 17.09.2016.
 */
public class Plot {

    private Chart chart;

    public Plot(String XAsis, String YAsis) {
        chart = new ChartBuilder().width(1280).height(960).build();
        chart.getStyleManager().setChartType(StyleManager.ChartType.Scatter);
        chart.setXAxisTitle(XAsis);
        chart.setYAxisTitle(YAsis);
    }

    public Plot addGraphic(List<Flat> points, String graphicName) {
        if (!points.isEmpty()) {
            List<Double> xData = new LinkedList<>();
            List<Double> yData = new LinkedList<>();
            for (Flat point : points) {
                xData.add(point.area);
                yData.add(point.price);
            }
            chart.addSeries(graphicName, xData, yData).setLineColor(new Color(255, 255, 255, 0)).setMarker(SeriesMarker.CIRCLE);
        }
        return this;
    }

    public Plot addGraphic(List<Flat> points, String graphicName, boolean flag) {
        if (!points.isEmpty()) {
            List<Double> xData = new LinkedList<>();
            List<Double> yData = new LinkedList<>();
            for (Flat point : points) {
                xData.add(point.roomsCount);
                yData.add(point.price);
            }
            chart.addSeries(graphicName, xData, yData).setLineColor(new Color(255, 255, 255, 0)).setMarker(SeriesMarker.CIRCLE);
        }
        return this;
    }

    Plot addLine(Flat begin, Flat end, String lineName) {
        List<Double> xData = new LinkedList<>();
        List<Double> yData = new LinkedList<>();
        xData.add(begin.area);
        xData.add(end.area);
        yData.add(begin.price);
        yData.add(end.price);
        chart.addSeries(lineName, xData, yData).setMarker(SeriesMarker.CIRCLE);
        return this;
    }

    public void show() {
        new SwingWrapper(chart).displayChart();
    }
}
