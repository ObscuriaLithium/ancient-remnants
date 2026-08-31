package dev.obscuria.remnants.config;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.*;
import dev.obscuria.fragmentum.v2.api.FragmentumProxy;
import dev.obscuria.fragmentum.v2.api.config.ConfigValue;
import dev.obscuria.remnants.AncientRemnants;
import dev.obscuria.remnants.client.screen.config.DualControllerBuilder;
import dev.obscuria.remnants.client.screen.config.HolderOption;
import dev.obscuria.remnants.client.screen.config.OptionPair;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.*;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public final class ConfigScreenBuilder {

    private static final String NAME_PLACEMENT_SPACING = "placement_spacing";
    private static final String NAME_PLACEMENT_SEPARATION = "placement_separation";
    private static final String NAME_PLACEMENT_BIOMES = "placement_biomes";

    public static Screen create(Screen parent) {
        return YetAnotherConfigLib.createBuilder()
                .title(Component.literal("Ancient Remnants Options"))
                .category(categoryBuilder("world")
                        .group(structureConfigGroup("hunter_monolith",
                                CommonConfig.HUNTER_MONOLITH_PLACEMENT_SPACING,
                                CommonConfig.HUNTER_MONOLITH_PLACEMENT_SEPARATION,
                                CommonConfig.HUNTER_MONOLITH_BIOMES))
                        .group(structureConfigGroup("warrior_monolith",
                                CommonConfig.WARRIOR_MONOLITH_PLACEMENT_SPACING,
                                CommonConfig.WARRIOR_MONOLITH_PLACEMENT_SEPARATION,
                                CommonConfig.WARRIOR_MONOLITH_BIOMES))
                        .group(structureConfigGroup("sentinel_monolith",
                                CommonConfig.SENTINEL_MONOLITH_PLACEMENT_SPACING,
                                CommonConfig.SENTINEL_MONOLITH_PLACEMENT_SEPARATION,
                                CommonConfig.SENTINEL_MONOLITH_BIOMES))
                        .group(structureConfigGroup("cherry_monument",
                                CommonConfig.CHERRY_MONUMENT_PLACEMENT_SPACING,
                                CommonConfig.CHERRY_MONUMENT_PLACEMENT_SEPARATION,
                                CommonConfig.CHERRY_MONUMENT_BIOMES))
                        .group(structureConfigGroup("oakwood_monument",
                                CommonConfig.OAKWOOD_MONUMENT_PLACEMENT_SPACING,
                                CommonConfig.OAKWOOD_MONUMENT_PLACEMENT_SEPARATION,
                                CommonConfig.OAKWOOD_MONUMENT_BIOMES))
                        .group(structureConfigGroup("coniferous_monument",
                                CommonConfig.CONIFEROUS_MONUMENT_PLACEMENT_SPACING,
                                CommonConfig.CONIFEROUS_MONUMENT_PLACEMENT_SEPARATION,
                                CommonConfig.CONIFEROUS_MONUMENT_BIOMES))
                        .build())
                .save(ConfigScreenBuilder::saveAll)
                .build().generateScreen(parent);
    }

    private static void saveAll() {
        CommonConfig.VALUES.forEach(ConfigValue::save);
    }

    private static ConfigCategory.Builder categoryBuilder(String key) {
        return ConfigCategory.createBuilder().name(translate("category." + key));
    }

    private static ConfigCategory.Builder categoryBuilderLiteral(String key) {
        return ConfigCategory.createBuilder().name(Component.translatable(key));
    }

    private static OptionGroup.Builder groupBuilder(String key) {
        return OptionGroup.createBuilder().name(translate("group." + key));
    }

    private static OptionGroup.Builder groupBuilder(String key, Identifier image) {
        return groupBuilder(key).description(OptionDescription.createBuilder()
                .text(translate("group." + key + ".desc").withStyle(ChatFormatting.GRAY))
                .webpImage(image)
                .build());
    }

    private static OptionGroup.Builder groupBuilderLiteral(String key) {
        return OptionGroup.createBuilder().name(Component.translatable(key));
    }

    static MutableComponent translate(String key) {
        return Component.translatable("config.%s.%s".formatted(AncientRemnants.MOD_ID, key));
    }

    static MutableComponent translate(String key, Object... args) {
        return Component.translatable("config.%s.%s".formatted(AncientRemnants.MOD_ID, key), args);
    }

    static OptionGroup structureConfigGroup(
            String key,
            ConfigValue<Integer> spacing,
            ConfigValue<Integer> separation,
            ConfigValue<List<? extends String>> biomes
    ) {
        return groupBuilderLiteral("structure.ancient_remnants.%s".formatted(key))
                .option(Opts.pair(
                        Opts.intField(spacing, 0, Integer.MAX_VALUE).key(NAME_PLACEMENT_SPACING).build(),
                        Opts.intField(separation, 0, Integer.MAX_VALUE).key(NAME_PLACEMENT_SEPARATION).anyReloadHint().build()))
                .option(Opts.innerScreen(NAME_PLACEMENT_BIOMES, builder -> builder
                        .option(Opts.biomeIdList(biomes).anyReloadHint().build())))
                .build();
    }

    public static final class Opts {

        private Opts() {}

        public static OptionDescription descriptionOf(Component text) {
            return OptionDescription.of(text.copy().withStyle(ChatFormatting.GRAY));
        }

        public static <T> OptionSpec<T> opt(ConfigValue<T> value) {
            return new OptionSpec<>(new ConfigValueAdapter<>(value));
        }

        public static <K extends Option<?>, V extends Option<?>> Option<OptionPair<K, V>> pair(K first, V second) {
            var optionPair = new OptionPair<>(first, second);
            return HolderOption.<K, V>createBuilder()
                    .optionPair(optionPair)
                    .controller(_ -> DualControllerBuilder.create(optionPair))
                    .build();
        }

        public static OptionSpec<Boolean> bool(ConfigValue<Boolean> value) {
            return opt(value).controller(TickBoxControllerBuilder::create);
        }

        public static <E extends Enum<E>> OptionSpec<E> enumCycle(ConfigValue<E> value, Class<E> enumClass) {
            return opt(value).controller(o -> EnumControllerBuilder.create(o).enumClass(enumClass));
        }

        public static OptionSpec<Double> doubleSlider(ConfigValue<Double> value, double min, double max, double step, ValueFormat format) {
            return opt(value).controller(o -> DoubleSliderControllerBuilder.create(o)
                    .range(min, max)
                    .step(step)
                    .formatValue(format.formatter));
        }

        public static OptionSpec<Integer> intSlider(ConfigValue<Integer> value, int min, int max, int step, ValueFormat format) {
            return opt(value).controller(o -> IntegerSliderControllerBuilder.create(o)
                    .range(min, max)
                    .step(step)
                    .formatValue(v -> format.formatter.format(v.doubleValue())));
        }

        public static OptionSpec<Integer> intField(ConfigValue<Integer> value, int min, int max) {
            return opt(value).controller(o -> IntegerFieldControllerBuilder.create(o).range(min, max));
        }

        public static OptionSpec<Double> doubleField(ConfigValue<Double> value, double min, double max) {
            return opt(value).controller(o -> DoubleFieldControllerBuilder.create(o).range(min, max));
        }

        public static OptionSpec<String> string(ConfigValue<String> value) {
            return opt(value).controller(StringControllerBuilder::create);
        }

        public static ListOptionSpec<String> stringList(ConfigValue<List<? extends String>> value, String initialEntry) {
            return listOpt(value).controller(StringControllerBuilder::create).initial(initialEntry);
        }

        public static ListOptionSpec<String> biomeIdList(ConfigValue<List<? extends String>> value) {
            return biomeIdList(value, "");
        }

        public static ListOptionSpec<String> biomeIdList(ConfigValue<List<? extends String>> value, String exclude) {
            try {
                var biomeLookup = FragmentumProxy.registryAccess().lookupOrThrow(Registries.BIOME);
                return dropdownList(value, () -> Stream.concat(
                        biomeLookup.listTagIds()
                                .map(TagKey::location)
                                .map(Identifier::toString)
                                .map("#%s"::formatted)
                                .filter(Predicate.not(exclude::equals))
                                .sorted(),
                        biomeLookup.listElements()
                                .map(Holder.Reference::key)
                                .map(ResourceKey::identifier)
                                .map(Identifier::toString)
                                .sorted()
                ).toList());
            } catch (Exception ignored) {
                return stringList(value, "");
            }
        }

        public static ListOptionSpec<String> itemIdList(ConfigValue<List<? extends String>> value) {
            return dropdownList(value, () -> BuiltInRegistries.ITEM.keySet().stream()
                    .map(Identifier::toString)
                    .sorted()
                    .toList());
        }

        public static ListOptionSpec<String> itemTagList(ConfigValue<List<? extends String>> value) {
            return dropdownList(value, () -> BuiltInRegistries.ITEM.listTagIds()
                    .map(TagKey::location)
                    .map(Identifier::toString)
                    .sorted()
                    .toList());
        }

        public static ListOptionSpec<String> modIdList(ConfigValue<List<? extends String>> value) {
            return dropdownList(value, () -> BuiltInRegistries.ITEM.keySet().stream()
                    .map(Identifier::getNamespace)
                    .distinct()
                    .sorted()
                    .toList());
        }

        private static ListOptionSpec<String> dropdownList(ConfigValue<List<? extends String>> value, Supplier<List<String>> valuesSupplier) {
            return listOpt(value)
                    .controller(o -> DropdownStringControllerBuilder.create(o)
                            .values(valuesSupplier.get())
                            .allowAnyValue(false)
                            .allowEmptyValue(false))
                    .initial("");
        }

        public static ButtonOption innerScreen(String key, Consumer<ConfigCategory.Builder> builder) {
            return ButtonOption.createBuilder()
                    .name(translate("option." + key))
                    .text(translate("button.configure"))
                    .action((screen, option) -> {
                        var categoryBuilder = categoryBuilderLiteral(key);
                        builder.accept(categoryBuilder);
                        Minecraft.getInstance().setScreen(YetAnotherConfigLib.createBuilder()
                                .title(Component.translatable(key))
                                .category(categoryBuilder.build())
                                .save(ConfigScreenBuilder::saveAll)
                                .build().generateScreen(screen));
                    }).build();
        }

        public static ButtonOption screenButton(String key, Function<Screen, Screen> screenFactory) {
            return ButtonOption.createBuilder()
                    .name(translate("option." + key))
                    .text(translate("option." + key + ".button"))
                    .description(descriptionOf(translate("option." + key + ".desc")))
                    .action((screen, option) -> Minecraft.getInstance().setScreen(screenFactory.apply(screen)))
                    .build();
        }

        private static <T> ListOptionSpec<T> listOpt(ConfigValue<List<? extends T>> value) {
            return new ListOptionSpec<>(new ConfigValueAdapter<>(value));
        }

        static <T> Binding<T> binding(Value<T> value) {
            return Binding.generic(value.getDefault(), value::get, value::set);
        }

        @SuppressWarnings("unchecked")
        static <T> Binding<List<T>> listBinding(Value<List<? extends T>> value) {
            return Binding.generic(
                    (List<T>) value.getDefault(),
                    () -> (List<T>) value.get(),
                    value::set);
        }
    }

    private static abstract class AbstractSpec<T, SELF extends AbstractSpec<T, SELF>> {

        String nameKey;
        @Nullable Component nameOverride;
        String descKey;
        @Nullable String descWrapper;
        @Nullable Identifier descImage;
        boolean noDesc;
        @Nullable Function<Option<T>, ControllerBuilder<T>> controllerFactory;

        AbstractSpec(String defaultKey) {
            this.nameKey = defaultKey;
            this.descKey = defaultKey;
        }

        @SuppressWarnings("unchecked")
        private SELF self() {
            return (SELF) this;
        }

        public SELF key(String key) {
            this.nameKey = key;
            this.descKey = key;
            return self();
        }

        public SELF name(String key) {
            this.nameKey = key;
            return self();
        }

        public SELF name(Component component) {
            this.nameOverride = component;
            return self();
        }

        public SELF desc(String key) {
            this.descKey = key;
            return self();
        }

        public SELF webpImage(Identifier texture) {
            this.descImage = texture;
            return self();
        }

        public SELF noDesc() {
            this.noDesc = true;
            return self();
        }

        public SELF anyReloadHint() {
            this.descWrapper = "config.%s.descWithAnyReload".formatted(AncientRemnants.MOD_ID);
            return self();
        }

        public SELF worldReloadHint() {
            this.descWrapper = "config.%s.descWithWorldReload".formatted(AncientRemnants.MOD_ID);
            return self();
        }

        public SELF controller(Function<Option<T>, ControllerBuilder<T>> factory) {
            this.controllerFactory = factory;
            return self();
        }

        Component resolvedName() {
            return nameOverride != null ? nameOverride : translate("option." + nameKey);
        }

        @Nullable OptionDescription resolvedDesc() {
            if (noDesc) return null;
            var builder = OptionDescription.createBuilder();
            var description = translate("option." + descKey + ".desc").withStyle(ChatFormatting.GRAY);
            builder.text(descWrapper != null
                    ? Component.translatable(descWrapper, description)
                    : description);
            if (descImage != null) builder.webpImage(descImage);
            return builder.build();
        }

        Function<Option<T>, ControllerBuilder<T>> requireController() {
            assert controllerFactory != null;
            return controllerFactory;
        }
    }

    public static final class OptionSpec<T> extends AbstractSpec<T, OptionSpec<T>> {

        private final Value<T> value;

        private OptionSpec(Value<T> value) {
            super(value.name());
            this.value = value;
        }

        public Option<T> build() {
            var builder = Option.<T>createBuilder()
                    .flag()
                    .name(resolvedName())
                    .binding(Opts.binding(value));
            @Nullable var desc = resolvedDesc();
            if (desc != null) builder.description(desc);
            return builder.controller(requireController()).build();
        }
    }

    public static final class ListOptionSpec<T> extends AbstractSpec<T, ListOptionSpec<T>> {

        private final Value<List<? extends T>> value;
        private T initial;

        private ListOptionSpec(Value<List<? extends T>> value) {
            super(value.name());
            this.value = value;
        }

        public ListOptionSpec<T> initial(T initial) {
            this.initial = initial;
            return this;
        }

        public ListOption<T> build() {
            var builder = ListOption.<T>createBuilder()
                    .name(resolvedName())
                    .binding(Opts.listBinding(value))
                    .controller(requireController());
            @Nullable var desc = resolvedDesc();
            if (desc != null) builder.description(desc);
            if (initial != null) builder.initial(initial);
            return builder.build();
        }
    }

    private interface Value<T> {

        String name();

        T get();

        T getDefault();

        void set(T value);
    }

    private record ConfigValueAdapter<T>(ConfigValue<T> configValue) implements Value<T> {

        @Override
        public String name() {
            return configValue.name();
        }

        @Override
        public T get() {
            return configValue.get();
        }

        @Override
        public T getDefault() {
            return configValue.getDefault();
        }

        @Override
        public void set(T value) {
            configValue.set(value);
        }
    }

    public enum ValueFormat {
        POTENCY(v -> translate("format.potency.%s".formatted(Math.round(v)))),
        BLOCKS(v -> translate("format.blocks", v.intValue())),
        CHUNKS(v -> translate("format.chunks", v.intValue())),
        PIXELS(v -> translate("format.pixels", v.intValue())),
        ENTRIES(v -> translate("format.entries", v.intValue()));

        final ValueFormatter<Double> formatter;

        ValueFormat(ValueFormatter<Double> formatter) {
            this.formatter = formatter;
        }
    }
}