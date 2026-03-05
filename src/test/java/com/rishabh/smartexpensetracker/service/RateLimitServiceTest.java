package com.rishabh.smartexpensetracker.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RateLimitServiceTest {

    @Mock
    private StringRedisTemplate redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @InjectMocks
    private RateLimitService rateLimitService;

    @Test
    void isAllowed_allowsAndSetsExpiryOnFirstRequest() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.increment("rate-limit:user-1")).thenReturn(1L);

        boolean allowed = rateLimitService.isAllowed("user-1");

        assertTrue(allowed);
        verify(redisTemplate).expire("rate-limit:user-1", Duration.ofSeconds(60));
    }

    @Test
    void isAllowed_blocksWhenLimitExceeded() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.increment("rate-limit:user-1")).thenReturn(51L);

        boolean allowed = rateLimitService.isAllowed("user-1");

        assertFalse(allowed);
        verify(redisTemplate, never()).expire("rate-limit:user-1", Duration.ofSeconds(60));
    }
}
