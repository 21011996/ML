import com.xeiam.xchart.*;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Ilya239 on 17.09.2016.
 */
class Plot {

    private Chart chart;

    Plot(String XAsis, String YAsis) {
        chart = new ChartBuilder().width(1280).height(960).build();
        chart.getStyleManager().setLegendPosition(StyleManager.LegendPosition.InsideNW);
        chart.setXAxisTitle(XAsis);
        chart.setYAxisTitle(YAsis);
    }

    Plot addGraphic(List<Dot> points, String graphicName) {
        if (!points.isEmpty()) {
            List<Double> xData = new LinkedList<>();
            List<Double> yData = new LinkedList<>();
            for (Dot point : points) {
                xData.add(point.x);
                yData.add(point.y);
            }
            chart.addSeries(graphicName, xData, yData).setLineColor(new Color(255, 255, 255, 0)).setMarker(SeriesMarker.CIRCLE);
        }
        return this;
    }

    Plot addLine(List<Dot> points, String graphicName) {
        if (!points.isEmpty()) {
            List<Double> xData = new LinkedList<>();
            List<Double> yData = new LinkedList<>();
            for (Dot point : points) {
                xData.add(point.x);
                yData.add(point.y);
            }
            chart.addSeries(graphicName, xData, yData).setMarker(SeriesMarker.NONE).setLineStyle(SeriesLineStyle.DASH_DASH);
        }
        return this;
    }

    void show() {
        new SwingWrapper(chart).displayChart();
    }
}
