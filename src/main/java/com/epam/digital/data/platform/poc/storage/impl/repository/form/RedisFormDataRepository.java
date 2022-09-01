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

package com.epam.digital.data.platform.poc.storage.impl.repository.form;

import com.epam.digital.data.platform.poc.storage.api.dto.FormDataDto;
import com.epam.digital.data.platform.poc.storage.api.repository.FormDataRepository;
import com.epam.digital.data.platform.poc.storage.impl.model.FormDataRedis;
import com.epam.digital.data.platform.poc.storage.impl.repository.base.BaseRedisRepository;
import com.epam.digital.data.platform.poc.storage.impl.repository.FormDataKeyValueRepository;
import lombok.Builder;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Optional;
import java.util.Set;

@Builder
public class RedisFormDataRepository extends BaseRedisRepository implements FormDataRepository {

    private FormDataKeyValueRepository repository;
    private RedisTemplate<String, Object> template;

    @Override
    public Set<String> getKeys(String pattern) {
        return execute(() -> template.keys(String.format("bpm-form-submissions:%s*", pattern)));
    }

    @Override
    public void delete(Set<String> keys) {
        execute(() -> template.delete(keys));
    }

    @Override
    public void putFormData(String key, FormDataDto content) {
        execute(() -> repository.save(toFormDataRedis(key, content)));
    }

    @Override
    public Optional<FormDataDto> getFormData(String key) {
        var data = repository.findById(key);
        return execute(() -> data.map(this::toFormDataDto));
    }

    private FormDataDto toFormDataDto(FormDataRedis formDataRedis) {
        return FormDataDto.builder()
                .data(formDataRedis.getData())
                .accessToken(formDataRedis.getAccessToken())
                .signature(formDataRedis.getSignature())
                .build();
    }

    private FormDataRedis toFormDataRedis(String key, FormDataDto formDataDto) {
        return FormDataRedis.builder()
                .id(key)
                .accessToken(formDataDto.getAccessToken())
                .data(formDataDto.getData())
                .signature(formDataDto.getSignature())
                .build();
    }
}
