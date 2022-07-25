package vn.trinhtung;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ProductController {
	private final ProductService productService;
	private final RedisService redisService;

	@GetMapping("/reset")
	public ResponseEntity<?> resetProduct() {
		productService.resetProduct();
		return ResponseEntity.ok().build();
	}

	@GetMapping("/{id}")
	public ResponseEntity<Product> getProduct(@PathVariable Integer id) {
		return ResponseEntity.ok(productService.getProductById(id));
	}

	@GetMapping
	public ResponseEntity<?> getAllProduct() {
		return ResponseEntity.ok(productService.getAllProducts());
	}

	@PostMapping
	public ResponseEntity<Product> createProduct(@RequestBody Product product) {
		return ResponseEntity.status(HttpStatus.CREATED).body(productService.saveProduct(product));
	}

	@PutMapping("/{id}")
	public ResponseEntity<Product> updateProduct(@RequestBody Product product,
			@PathVariable Integer id) {
		product.setId(id);

		return ResponseEntity.ok(productService.saveProduct(product));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteProduct(@PathVariable Integer id) {
		productService.deledeProductById(id);

		return ResponseEntity.noContent().build();
	}

	@GetMapping("/buy/{id}")
	public ResponseEntity<?> buyProduct(@PathVariable Integer id) {
		return ResponseEntity.ok(productService.buyProduct(id));
	}

	@GetMapping("/lock-1")
	public String lock_1() {
		redisService.tryLock_1("lock-1");

		try {
			System.out.println("Lock 1");
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		redisService.unLock_1("lock-1");

		return "Lock 1";
	}

	@GetMapping("/lock-2")
	public String lock_2() {
		String value = UUID.randomUUID().toString();

		System.out.println(redisService.tryLock_2("lock-2", value));

		try {
			System.out.println("Lock 2");
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println(redisService.unLock_2("lock-2", value));

		return "Lock 2";
	}

}
