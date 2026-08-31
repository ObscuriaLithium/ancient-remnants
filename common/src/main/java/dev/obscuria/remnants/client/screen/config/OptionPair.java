package dev.obscuria.remnants.client.screen.config;

import dev.isxander.yacl3.api.Option;

public record OptionPair<K extends Option<?>, V extends Option<?>>(K firstOption, V secondOption) {}