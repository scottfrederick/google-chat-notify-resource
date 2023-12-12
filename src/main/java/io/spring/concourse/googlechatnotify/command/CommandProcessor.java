/*
 * Copyright 2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.spring.concourse.googlechatnotify.command;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * {@link ApplicationRunner} to delegate incoming requests to commands.
 *
 * @author Phillip Webb
 * @author Madhura Bhave
 */
@Component
@Profile("!test")
public class CommandProcessor implements ApplicationRunner {

	private static final Logger logger = LoggerFactory.getLogger(CommandProcessor.class);

	private final List<Command> commands;

	public CommandProcessor(List<Command> commands) {
		this.commands = Collections.unmodifiableList(commands);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		List<String> nonOptionArgs = args.getNonOptionArgs();
		logger.debug("Processing command {}", nonOptionArgs);
		Assert.state(!nonOptionArgs.isEmpty(), "No command argument specified");
		String request = nonOptionArgs.get(0);
		this.commands.stream()
			.filter((c) -> c.getName().equals(request))
			.findFirst()
			.orElseThrow(() -> new IllegalStateException("Unknown command '" + request + "'"))
			.run(args);
	}

}
