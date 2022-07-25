package vn.trinhtung;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.integration.redis.util.RedisLockRegistry;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RedisService {
	private final RedisLockRegistry redisLockRegistry;
	private final StringRedisTemplate redisTemplate;
	
	
	public boolean tryLock_1(String key) {
		
		
		Lock lock = redisLockRegistry.obtain(key);
		try {
			return lock.tryLock(10, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			return false;
		}
	}

	public void unLock_1(String key) {
		Lock lock = redisLockRegistry.obtain(key);
		lock.unlock();
	}

	public boolean tryLock_2(String key, String value) {
		return redisTemplate.opsForValue().setIfAbsent(key, value, 10, TimeUnit.SECONDS);
	}

	public boolean unLock_2(String key, String value) {
		String script = "if(redis.call('GET', KEYS[1]) == ARGV[1]) then "
				+ "return redis.call('DEL', KEYS[1]) " + "else return 0 end";
		DefaultRedisScript<Boolean> redisScript = new DefaultRedisScript<>(script);
		redisScript.setResultType(Boolean.class);
		
		return redisTemplate.execute(redisScript, Arrays.asList(key), value);
	}
}
