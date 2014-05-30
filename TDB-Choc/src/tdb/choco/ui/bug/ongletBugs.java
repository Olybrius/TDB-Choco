package tdb.choco.ui.bug;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;

public class ongletBugs extends CustomComponent {

    String url = "jdbc:mysql://localhost/test";
    String login = "root";
    String passwrd = "";
    Connection cn = null;
    Statement st = null;
    ResultSet rs = null;
    List<String> nomBenchmarks1 = new ArrayList<String>();
    List<String> nomProblemes1 = new ArrayList<String>();
    List<String> nomBenchmarks2 = new ArrayList<String>();
    List<String> nomProblemes2 = new ArrayList<String>();
    List<String> nomBenchmarks3 = new ArrayList<String>();
    List<String> nomProblemes3 = new ArrayList<String>();

    public ongletBugs() {

        try {
            Class.forName("com.mysql.jdbc.Driver");

            cn = DriverManager.getConnection(url, login, passwrd);
            st = cn.createStatement();

            // Requête Solving_time < 900000 et pas de solution pour récupérer
            // le nom du problème
            String nomProbleme1 = "Select p.name FROM problems p, resolutions r WHERE r.solving_time<900000 AND r.nb_sol=0 AND r.pid=p.pid";
            rs = st.executeQuery(nomProbleme1);
            while (rs.next()) {
                nomProblemes1.add(rs.getString("name"));
            }

            // Requête Solving_time < 900000 et pas de solution pour récupérer
            // le nom du benchmark
            String nomBenchmark1 = "Select b.name FROM benchmarks b, resolutions r WHERE r.solving_time<900000 AND r.nb_sol=0 AND r.bid=b.bid";
            rs = st.executeQuery(nomBenchmark1);
            while (rs.next()) {
                nomBenchmarks1.add(rs.getString("name"));
            }

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

        final HorizontalLayout layout = new HorizontalLayout();
        layout.setMargin(true);

        Table table1 = new Table("Bugs");

        table1.addContainerProperty("Type du bug", String.class, null);
        table1.addContainerProperty("Nom du Benchmark", String.class, null);
        table1.addContainerProperty("Nom du Problème", String.class, null);

        table1.addItem(new Object[] {
                "Solving_Time < 90000 et pas de solution", "", "" },
                new Integer(1));
        for (int i = 2; i < 2 + nomProblemes1.size(); i++) {
            table1.addItem(new Object[] { "", nomBenchmarks1.get(i - 2),
                    nomProblemes1.get(i - 2) }, new Integer(i));
        }

        layout.addComponent(table1);

        Table table2 = new Table("Bugs");

        table2.addContainerProperty("Type du bug", String.class, null);
        table2.addContainerProperty("Nom du Benchmark", String.class, null);
        table2.addContainerProperty("Nom du Problème", String.class, null);

        table2.addItem(new Object[] { "Valeur exceptionnelle", "", "" },
                new Integer(1));
        // for (int i = 2; i < 2 + nomProblemes2.size(); i++) {
        // table1.addItem(new Object[] { "", nomBenchmarks2.get(i - 2),
        // nomProblemes2.get(i - 2) }, new Integer(i));
        // }

        layout.addComponent(table2);

        Table table3 = new Table("Bugs");

        table3.addContainerProperty("Type du bug", String.class, null);
        table3.addContainerProperty("Nom du Benchmark", String.class, null);
        table3.addContainerProperty("Nom du Problème", String.class, null);

        table3.addItem(new Object[] {
                "Solving_Time < 90000 et pas la meilleure solution (MIN, MAX)",
                "", "" }, new Integer(1));
        for (int i = 2; i < 2 + nomProblemes3.size(); i++) {
            table1.addItem(new Object[] { "", nomBenchmarks3.get(i - 2),
                    nomProblemes3.get(i - 2) }, new Integer(i));
        }

        layout.addComponent(table3);

        setCompositionRoot(layout);
    }
}
