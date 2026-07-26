package de.witchcafe.wator;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Push
public class WatorApplication implements AppShellConfigurator {

	public static void main(String[] args) {
		SpringApplication.run(WatorApplication.class, args);
	}

}
