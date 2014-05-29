package tdb.choco.ui;

import javax.servlet.annotation.WebServlet;

import tdb.choco.ui.bug.ongletBugs;
import tdb.choco.ui.perf.ongletPerfs;
import tdb.choco.ui.recap.ongletRecap;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@Theme("tdb_choco")
public class Tdb_chocoUI extends UI {

    private final ongletBugs oB = new ongletBugs();
    private final ongletPerfs oP = new ongletPerfs();
    private final ongletRecap oR = new ongletRecap();


	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = Tdb_chocoUI.class, widgetset = "tdb.choco.ui.widgetset.Tdb_chocWidgetset")
	public static class Servlet extends VaadinServlet {
	}

	 @Override
	    protected void init(VaadinRequest request) {
	        final VerticalLayout layout = new VerticalLayout();
	        layout.setMargin(true);
	        setContent(layout);

	        TabSheet onglets = new TabSheet();
	        VerticalLayout ongletBugs = new VerticalLayout();
	        ongletBugs.addComponent(oB);
	        onglets.addTab(ongletBugs, "Bugs");

	        VerticalLayout ongletPerfs = new VerticalLayout();
	        ongletPerfs.addComponent(oP);
	        onglets.addTab(ongletPerfs, "Performances");

	        VerticalLayout ongletRecaps = new VerticalLayout();
	        ongletRecaps.addComponent(oR);
	        onglets.addTab(ongletRecaps, "Récapitulatif");

	        layout.addComponent(onglets);

	    }

}