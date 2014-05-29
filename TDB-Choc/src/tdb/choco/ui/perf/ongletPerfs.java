package tdb.choco.ui.perf;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.Configuration;
import com.vaadin.addon.charts.model.ListSeries;
import com.vaadin.addon.charts.model.XAxis;
import com.vaadin.addon.charts.model.YAxis;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.VerticalLayout;

public class ongletPerfs extends CustomComponent {

    public ongletPerfs() {

        final VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        Chart chart = new Chart(ChartType.BAR);
        chart.setWidth("400px");
        chart.setHeight("300px");

        // Modify the default configuration a bit
        Configuration conf = chart.getConfiguration();
        conf.setTitle("Planets");
        conf.setSubTitle("The bigger they are the harder they pull");
        conf.getLegend().setEnabled(false); // Disable legend

        // The data
        ListSeries series = new ListSeries("Diameter");
        series.setData(4900, 12100, 12800, 6800, 143000, 125000, 51100, 49500);
        conf.addSeries(series);

        // Set the category labels on the axis correspondingly
        XAxis xaxis = new XAxis();
        xaxis.setCategories("Mercury", "Venus", "Earth", "Mars", "Jupiter",
                "Saturn", "Uranus", "Neptune");
        xaxis.setTitle("Planet");
        conf.addxAxis(xaxis);

        // Set the Y axis title
        YAxis yaxis = new YAxis();
        yaxis.setTitle("Diameter");
        yaxis.getLabels().setFormatter(
                "function() {return Math.floor(this.value/1000) + \'Mm\';}");
        yaxis.getLabels().setStep(2);
        conf.addyAxis(yaxis);

        layout.addComponent(chart);
        setCompositionRoot(layout);

    }
}
