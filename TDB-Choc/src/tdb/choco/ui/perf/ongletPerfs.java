package tdb.choco.ui.perf;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.Configuration;
import com.vaadin.addon.charts.model.ListSeries;
import com.vaadin.addon.charts.model.XAxis;
import com.vaadin.addon.charts.model.YAxis;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.VerticalLayout;

public class ongletPerfs extends CustomComponent {

    String url = "jdbc:mysql://localhost/test";
    String login = "root";
    String passwrd = "";
    Connection cn = null;
    Statement st = null;
    ResultSet rs = null;
    HashMap<Integer, Double> bencmark1 = new HashMap<Integer, Double>();
    HashMap<Integer, Double> bencmark2 = new HashMap<Integer, Double>();
    List<Number> pid = new ArrayList<Number>();
    List<Number> minData = new ArrayList<Number>();


    int bidMax = 0;

    public ongletPerfs() {

        try {
            Class.forName("com.mysql.jdbc.Driver");

            cn = DriverManager.getConnection(url, login, passwrd);
            st = cn.createStatement();

            //Récupération du benchmark avec le BID max
            String reqBidMax = "Select MAX(bid) as bid FROM benchmarks";
            rs = st.executeQuery(reqBidMax);
            rs.next();
            bidMax = rs.getInt("bid");
            int bidMaxMoinsUn = bidMax-1;

            //Requête pour récupérer solving_time du benchmark1
            String reqSolvingTime1 = "Select solving_time, r.pid FROM resolutions r, problems p WHERE p.resolution ='" + "MIN" + "' AND r.bid ='" + bidMax + "'";
            rs = st.executeQuery(reqSolvingTime1);
            while (rs.next()) {
                bencmark1.put(rs.getInt("PID"), rs.getDouble("solving_time"));
            }

            //Requête pour récupérer solving_time du benchmark2
            String reqSolvingTime2 = "Select solving_time, r.pid FROM resolutions r, problems p WHERE p.resolution ='" + "MIN" +"' AND r.bid ='" + bidMaxMoinsUn + "'";
            rs = st.executeQuery(reqSolvingTime2);
            while (rs.next()) {
                bencmark2.put(rs.getInt("PID"), rs.getDouble("solving_time"));
            }

            ListSeries seriesT = new ListSeries("PID");

            for(Integer key : bencmark1.keySet()){
                if (bencmark1.get(key) != bencmark2.get(key)){
                    double tmp = bencmark1.get(key) - bencmark2.get(key);

                    if(tmp < 0){
                        tmp = tmp * -1;
                    }
                    pid.add(key);
                    minData.add(tmp);
                }
            }

            seriesT.setData(minData);

            final VerticalLayout layout = new VerticalLayout();
            layout.setMargin(true);
            Chart chart = new Chart(ChartType.COLUMN);
            chart.setWidth("800px");
            chart.setHeight("600px");

            // Modify the default configuration a bit
            Configuration conf = chart.getConfiguration();
            conf.setTitle("MIN");
            conf.setSubTitle("Min en fonction du temps de résolution");
            conf.getLegend().setEnabled(false); // Disable legend

            conf.addSeries(seriesT);


            // Set the category labels on the axis correspondingly
            XAxis xaxis = new XAxis();
            xaxis.setTitle("PID");
            conf.addxAxis(xaxis);

            // Set the Y axis title
            YAxis yaxis = new YAxis();
            yaxis.setTitle("Solving_Time");
            conf.addyAxis(yaxis);

            layout.addComponent(chart);
            setCompositionRoot(layout);

        }
        catch (SQLException se) {
            se.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                cn.close();
                st.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }



       /* // The data
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
        conf.addyAxis(yaxis);*/



    }
}
