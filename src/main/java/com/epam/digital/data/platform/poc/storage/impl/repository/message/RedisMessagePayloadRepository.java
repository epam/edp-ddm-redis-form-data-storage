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

package com.epam.digital.data.platform.poc.storage.impl.repository.message;

import com.epam.digital.data.platform.poc.storage.api.dto.MessagePayloadDto;
import com.epam.digital.data.platform.poc.storage.api.repository.MessagePayloadRepository;
import com.epam.digital.data.platform.poc.storage.impl.model.MessagePayloadRedis;
import com.epam.digital.data.platform.poc.storage.impl.repository.base.BaseRedisRepository;
import com.epam.digital.data.platform.poc.storage.impl.repository.MessagePayloadKeyValueRepository;
import lombok.Builder;

import java.util.Optional;

@Builder
public class RedisMessagePayloadRepository extends BaseRedisRepository implements MessagePayloadRepository {

    private MessagePayloadKeyValueRepository repository;

    @Override
    public void putMessagePayload(String key, MessagePayloadDto messagePayloadDto) {
        execute(() -> repository.save(MessagePayloadRedis.builder()
                .id(key)
                .data(messagePayloadDto.getData())
                .build()));
    }

    @Override
    public Optional<MessagePayloadDto> getMessagePayload(String key) {
        var result = execute(() -> repository.findById(key));
        return result.map(this::toMessagePayloadDto);
    }

    @Override
    public void delete(String... keys) {
        execute(() -> {
            for (String key : keys) {
                repository.deleteById(key);
            }
        });
    }

    private MessagePayloadDto toMessagePayloadDto(MessagePayloadRedis messagePayloadRedis) {
        return MessagePayloadDto.builder()
                .data(messagePayloadRedis.getData())
                .build();
    }
}
