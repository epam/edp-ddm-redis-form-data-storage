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

package com.epam.digital.data.platform.poc.storage.impl.config;

import com.epam.digital.data.platform.poc.storage.api.repository.FormDataRepository;
import com.epam.digital.data.platform.poc.storage.api.repository.MessagePayloadRepository;
import com.epam.digital.data.platform.poc.storage.impl.factory.StorageFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;


@Configuration
public class RedisConfiguration {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(StorageFactory storageFactory,
                                                       RedisStorageConfiguration configuration) {
        return storageFactory.redisTemplate(configuration);
    }

    @Bean
    public FormDataRepository formDataRepository(StorageFactory storageFactory,
                                                 RedisTemplate<String, Object> redisTemplate) {
        return storageFactory.formDataRepository(redisTemplate);
    }

    @Bean
    public MessagePayloadRepository messagePayloadRepository(StorageFactory storageFactory,
                                                             RedisTemplate<String, Object> redisTemplate) {
        return storageFactory.messagePayloadRepository(redisTemplate);
    }

    @Bean
    public StorageFactory storageFactory() {
        return new StorageFactory();
    }

    @Bean
    @ConfigurationProperties(prefix = "storage.redis")
    public RedisStorageConfiguration redisStorageConfiguration() {
        return new RedisStorageConfiguration();
    }
}
