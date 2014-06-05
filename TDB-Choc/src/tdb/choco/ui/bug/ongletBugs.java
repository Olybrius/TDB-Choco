package tdb.choco.ui.bug;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

public class ongletBugs extends CustomComponent {

    String url = "jdbc:mysql://localhost/test";
    String login = "root";
    String passwrd = "";
    Connection cn = null;
    Statement st = null;
    ResultSet rs = null;
    List<String> nomProblemes1D = new ArrayList<String>();
    List<String> nomProblemes1AD = new ArrayList<String>();
    List<String> nomBenchmarks2 = new ArrayList<String>();
    List<String> nomProblemes2 = new ArrayList<String>();
    List<String> nomBenchmarks3 = new ArrayList<String>();
    List<String> nomProblemes3 = new ArrayList<String>();
    String dernierBenchmark, avantDernierBenchmark;

    public ongletBugs() {

        try {
            Class.forName("com.mysql.jdbc.Driver");

            cn = DriverManager.getConnection(url, login, passwrd);
            st = cn.createStatement();

            // Requête pour récupérer les deux derniers benchmarks
            String bid = "SELECT MAX(bid) as bid FROM benchmarks";
            rs = st.executeQuery(bid);
            rs.next();
            Integer dernierBID = rs.getInt("bid");
            Integer avantDernierBID = dernierBID - 1;
            bid = "SELECT name FROM benchmarks WHERE bid = " + avantDernierBID;
            rs = st.executeQuery(bid);
            rs.next();
            avantDernierBenchmark = rs.getString("name");
            bid = "SELECT name FROM benchmarks WHERE bid = " + dernierBID;
            rs = st.executeQuery(bid);
            rs.next();
            dernierBenchmark = rs.getString("name");

            // Requête Solving_time < 900000 et pas de solution pour récupérer
            // le nom du problème avec le dernier benchmark
            String nomProbleme1D = "Select p.name FROM problems p, resolutions r WHERE bid = "
                    + dernierBID
                    + " AND r.solving_time<900000 AND r.nb_sol=0 AND r.pid=p.pid";
            rs = st.executeQuery(nomProbleme1D);
            while (rs.next()) {
                nomProblemes1D.add(rs.getString("name"));
            }

            // Requête Solving_time < 900000 et pas de solution pour récupérer
            // le nom du problème avec l'avant dernier benchmark
            String nomProbleme1AD = "Select p.name FROM problems p, resolutions r WHERE bid = "
                    + avantDernierBID
                    + " AND r.solving_time<900000 AND r.nb_sol=0 AND r.pid=p.pid";
            rs = st.executeQuery(nomProbleme1AD);
            while (rs.next()) {
                nomProblemes1AD.add(rs.getString("name"));
            }

            // *************AU DESSUS CA MARCHE !!!!!!****************

            // Requête valeur exceptionnelle pour récupérer le nom du problème

            String nomProbleme2 = "Select p.name FROM problems p, resolutions r WHERE p.pid=r.pid AND p.objective=r.objective";
            rs = st.executeQuery(nomProbleme2);
            while (rs.next()) {
                nomProblemes2.add(rs.getString("name"));
            }

            // Requête valeur exceptionnelle pour récupérer le nom du benchmark
            String nomBenchmark2 = "Select b.name FROM benchmarks b, resolutions r WHERE b.bid=r.bid AND b.objective=r.objective";
            rs = st.executeQuery(nomBenchmark2);
            while (rs.next()) {
                nomBenchmarks2.add(rs.getString("name"));
            }

            // Requête Solving_time < 900000 et pas de solution pour récupérer
            // le nom du problème
            String nomProbleme3 = "Select p.name FROM problems p, resolutions r WHERE p.resolutions='min' OR p.resolutions='max' AND r.solving_time<900000 AND r.pid=p.pid AND r.objective!=p.objective";
            rs = st.executeQuery(nomProbleme3);
            while (rs.next()) {
                nomProblemes3.add(rs.getString("name"));
            }

            // Requête Solving_time < 900000 et pas de solution pour récupérer
            // le nom du benchmark
            String nomBenchmark3 = "Select b.name FROM problems p, resolutions r, benchmarks b WHERE p.resolutions='min' OR p.resolutions='max' AND r.solving_time<900000 AND r.bid=b.bid AND r.objective!=p.objective";
            rs = st.executeQuery(nomBenchmark3);
            while (rs.next()) {
                nomBenchmarks3.add(rs.getString("name"));
            }

        } catch (SQLException se) {
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

        final VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);

        Table table = new Table("Bugs");
        table.setSortEnabled(false);
        table.addContainerProperty("Type du bug", String.class, null);
        table.addContainerProperty("Nom du Benchmark", String.class, null);
        table.addContainerProperty("Nom du Problème", String.class, null);

        table.addItem(new Object[] { "Solving_Time < 90000 et pas de solution",
                "", "" }, new Integer(0));

        for (int i = 1; i < 1 + nomProblemes1AD.size(); i++) {
            table.addItem(new Object[] { "", avantDernierBenchmark,
                    nomProblemes1AD.get(i - 1) }, new Integer(i));
        }

        for (int i = 1000; i < 1000 + nomProblemes1D.size(); i++) {
            table.addItem(
                    new Object[] { "", dernierBenchmark,
                            nomProblemes1D.get(i - 1000) }, new Integer(i));
        }

        table.addItem(new Object[] { "Valeur exceptionnelle", "", "" },
                new Integer(2000));

        table.addItem(new Object[] {
                "Solving_Time < 90000 et pas la meilleure solution (MIN, MAX)",
                "", "" }, new Integer(3000));

        layout.addComponent(table);

        setCompositionRoot(layout);
    }
}
