package com.example.luto.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
public class RedisConfig {

  @Bean
  public LettuceConnectionFactory redisConnectionFactory(
      @Value("${REDIS_HOST:localhost}") String host,
      @Value("${REDIS_PORT:6379}") int port) {
    return new LettuceConnectionFactory(new RedisStandaloneConfiguration(host, port));
  }

  @Bean
  public StringRedisTemplate stringRedisTemplate(LettuceConnectionFactory cf) {
    return new StringRedisTemplate(cf);
  }
}
