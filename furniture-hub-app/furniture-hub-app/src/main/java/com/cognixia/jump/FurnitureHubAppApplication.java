package com.cognixia.jump;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info( title="Furniture Hub API", version="1.0",
				description="API that allows Users to view furniture items, place an order, and view orders. Allows Admins to create and update furniture items.",
				contact=@Contact(name="Jesus's Furnitures", email="jesusfurniture@gmail.com")
		)
)
public class FurnitureHubAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(FurnitureHubAppApplication.class, args);
	}

}
