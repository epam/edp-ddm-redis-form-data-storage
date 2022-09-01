/*
 * Copyright 2022 EPAM Systems.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.epam.digital.data.platform.poc.storage.impl.factory;

import com.epam.digital.data.platform.poc.storage.api.repository.FormDataRepository;
import com.epam.digital.data.platform.poc.storage.api.repository.MessagePayloadRepository;
import com.epam.digital.data.platform.poc.storage.impl.config.RedisStorageConfiguration;
import com.epam.digital.data.platform.poc.storage.impl.repository.FormDataKeyValueRepository;
import com.epam.digital.data.platform.poc.storage.impl.repository.MessagePayloadKeyValueRepository;
import com.epam.digital.data.platform.poc.storage.impl.repository.form.RedisFormDataRepository;
import com.epam.digital.data.platform.poc.storage.impl.repository.message.RedisMessagePayloadRepository;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisKeyValueAdapter;
import org.springframework.data.redis.core.RedisKeyValueTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.mapping.RedisMappingContext;
import org.springframework.data.redis.repository.support.RedisRepositoryFactory;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import java.time.Duration;

public class StorageFactory {

    public FormDataRepository formDataRepository(RedisTemplate<String, Object> template) {
        return RedisFormDataRepository.builder()
                .repository(newFormDataKeyValueRepository(template))
                .template(template)
                .build();
    }

    public MessagePayloadRepository messagePayloadRepository(RedisTemplate<String, Object> template) {
        return RedisMessagePayloadRepository.builder()
                .repository(newMessagePayloadKeyValueRepository(template))
                .build();
    }

    public RedisTemplate<String, Object> redisTemplate(RedisStorageConfiguration configuration) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory(configuration));
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    private FormDataKeyValueRepository newFormDataKeyValueRepository(RedisTemplate<String, Object> template) {
        RedisKeyValueAdapter keyValueAdapter = new RedisKeyValueAdapter(template.opsForHash().getOperations());
        RedisKeyValueTemplate keyValueTemplate = new RedisKeyValueTemplate(keyValueAdapter, new RedisMappingContext());

        RepositoryFactorySupport factory = new RedisRepositoryFactory(keyValueTemplate);
        return factory.getRepository(FormDataKeyValueRepository.class);
    }

    private MessagePayloadKeyValueRepository newMessagePayloadKeyValueRepository(RedisTemplate<String, Object> template) {
        RedisKeyValueAdapter keyValueAdapter = new RedisKeyValueAdapter(template.opsForHash().getOperations());
        RedisKeyValueTemplate keyValueTemplate = new RedisKeyValueTemplate(keyValueAdapter, new RedisMappingContext());

        RepositoryFactorySupport factory = new RedisRepositoryFactory(keyValueTemplate);
        return factory.getRepository(MessagePayloadKeyValueRepository.class);
    }

    private LettuceConnectionFactory redisConnectionFactory(RedisStorageConfiguration configuration) {
        var redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(configuration.getHost());
        redisStandaloneConfiguration.setPort(configuration.getPort());
        redisStandaloneConfiguration.setDatabase(configuration.getDatabase());
        redisStandaloneConfiguration.setPassword(configuration.getPassword());

        var clientConfiguration = LettuceClientConfiguration.builder()
                .commandTimeout(Duration.ofMillis(configuration.getTimeout()))
                .build();
        var connectionFactory = new LettuceConnectionFactory(redisStandaloneConfiguration, clientConfiguration);
        connectionFactory.afterPropertiesSet();
        return connectionFactory;
    }
}
