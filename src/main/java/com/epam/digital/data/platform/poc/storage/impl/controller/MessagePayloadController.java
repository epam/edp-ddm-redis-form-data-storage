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

package com.epam.digital.data.platform.poc.storage.impl.controller;

import com.epam.digital.data.platform.poc.storage.api.repository.MessagePayloadRepository;
import com.epam.digital.data.platform.poc.storage.api.dto.MessagePayloadDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class MessagePayloadController {

    @Autowired
    private MessagePayloadRepository repository;

    @PostMapping("/message")
    public void createMessagePayload(@RequestHeader("key") String key,
                     @RequestBody MessagePayloadDto messagePayload) {
        repository.putMessagePayload(key, messagePayload);
    }

    @DeleteMapping("/message")
    public void deleteMessagePayload(@RequestHeader("key") String key) {
        repository.delete(key);
    }

    @GetMapping("/message")
    public Optional<MessagePayloadDto> getMessagePayload(@RequestHeader("key") String key) {
        return repository.getMessagePayload(key);
    }
}
