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
    List<String> nomBenchmarks = new ArrayList<String>();
    List<String> nomProblemes = new ArrayList<String>();

    public ongletBugs() {

        try {
            Class.forName("com.mysql.jdbc.Driver");

            cn = DriverManager.getConnection(url, login, passwrd);

            st = cn.createStatement();
            String sql = "SELECT * FROM solvers";

            rs = st.executeQuery(sql);

            while (rs.next()) {
                nomBenchmarks.add(rs.getString("NAME"));
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

        table.addContainerProperty("Type du bug", String.class, null);
        table.addContainerProperty("Nom du Benchmark", String.class, null);
        table.addContainerProperty("Nom du Problème", String.class, null);

        table.addItem(new Object[] { "Solving_Time < 90000 et pas de solution",
                "", "" }, new Integer(1));
        for (int i = 2; i < 2 + nomBenchmarks.size(); i++) {
            table.addItem(new Object[] { "", nomBenchmarks.get(i - 2), "" },
                    new Integer(i));
        }
        table.addItem(new Object[] { "Valeur exceptionnelle", "", "" },
                new Integer(1000));
        table.addItem(new Object[] {
                "Solving_Time < 90000 et pas la meilleure solution (MIN, MAX)",
                "", "" }, new Integer(2000));

        layout.addComponent(table);
        setCompositionRoot(layout);
    }
}
