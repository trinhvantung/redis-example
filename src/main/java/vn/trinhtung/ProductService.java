package vn.trinhtung;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

	@Autowired
	private RedissonClient redissonClient;

	@Autowired
	private ProductRepository repository;

	public void resetProduct() {
		repository.saveAll(Arrays.asList(new Product(1, "Iphone 13 Pro Max", 3),
				new Product(2, "Samsung Galaxy S22 Ultra", 6),
				new Product(3, "Xiaomi 12S Ultra", 2), new Product(4, "Oppo Reno 8 Pro", 1),
				new Product(5, "Vivo X80 Pro", 5)));
	}

	public Collection<Product> getAllProducts() {

		return repository.findAll();
	}

	@Cacheable(value = "product", key = "#id")
	public Product getProductById(Integer id) {
		return repository.findById(id).get();
	}

	@CachePut(value = "product", key = "#product.id")
	public Product saveProduct(Product product) {
		return repository.save(product);
	}

	@CacheEvict(value = "product", key = "#id")
	public void deledeProductById(Integer id) {
		repository.deleteById(id);
	}

	public String buyProduct(Integer id) {
		RLock lock = redissonClient.getLock("buy-product-" + id);
		try {
			lock.tryLock(10, TimeUnit.SECONDS);

			Product product = repository.findById(id).get();
			Integer quantity = product.getQuantity();

			if (quantity < 1) {
				System.out.println("Not enough quantity");

				lock.unlock();
				return "Not enough quantity";
			}

			product.setQuantity(quantity - 1);

			repository.save(product);

			lock.unlock();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println("Successful purchase");
		return "Successful purchase";
	}
}
