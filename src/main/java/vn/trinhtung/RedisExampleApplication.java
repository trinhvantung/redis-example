package vn.trinhtung;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class RedisExampleApplication implements CommandLineRunner {
	@Autowired
	private ProductRepository repository;

	@Override
	public void run(String... args) throws Exception {
		repository.saveAll(Arrays.asList(new Product(null, "Iphone 13 Pro Max", 3),
				new Product(null, "Samsung Galaxy S22 Ultra", 6),
				new Product(null, "Xiaomi 12S Ultra", 2), new Product(null, "Oppo Reno 8 Pro", 1),
				new Product(null, "Vivo X80 Pro", 5)));
	}

	public static void main(String[] args) {
		SpringApplication.run(RedisExampleApplication.class, args);
	}

}
