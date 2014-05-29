package tdb.choco.ui.recap;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class ongletRecap extends CustomComponent {

    public ongletRecap() {

        final VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);

        Button button = new Button("Click Me");
        Button b2 = new Button("Test");
        b2.setId("test");
        button.addClickListener(new Button.ClickListener() {
            public void buttonClick(ClickEvent event) {
                layout.addComponent(new Label("Thank you for clicking"));
            }
        });
        layout.addComponent(button);

        setCompositionRoot(layout);

    }

}
