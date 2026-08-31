package dev.obscuria.remnants.client.screen.config;

import com.google.common.collect.ImmutableSet;
import dev.isxander.yacl3.api.Binding;
import dev.isxander.yacl3.api.Controller;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.OptionEventListener;
import dev.isxander.yacl3.api.OptionFlag;
import dev.isxander.yacl3.api.StateManager;
import dev.isxander.yacl3.api.controller.ControllerBuilder;
import dev.isxander.yacl3.impl.OptionImpl;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@SuppressWarnings("UnstableApiUsage")
public final class HolderOption<K extends Option<?>, V extends Option<?>> extends OptionImpl<OptionPair<K, V>> {

    private final OptionPair<K, V> optionPair;
    private final K firstOption;
    private final V secondOption;

    private HolderOption(
            Component name,
            Function<OptionPair<K, V>, OptionDescription> descriptionFunction,
            Function<Option<OptionPair<K, V>>, DualController<K, V>> controlGetter,
            StateManager<OptionPair<K, V>> stateManager,
            boolean available,
            ImmutableSet<OptionFlag> flags,
            Collection<OptionEventListener<OptionPair<K, V>>> listeners
    ) {
        super(name, descriptionFunction, controlGetter::apply, stateManager, available, flags, listeners);

        this.optionPair = stateManager.get();
        this.firstOption = optionPair.firstOption();
        this.secondOption = optionPair.secondOption();
    }

    @Override
    public boolean available() {
        return firstOption.available() && secondOption.available();
    }

    @Override
    public void setAvailable(boolean available) {
        boolean firstChanged = firstOption.available() != available;
        boolean secondChanged = secondOption.available() != available;

        firstOption.setAvailable(available);
        secondOption.setAvailable(available);

        if (firstChanged || secondChanged) {
            if (!available) {
                firstOption.stateManager().sync();
                secondOption.stateManager().sync();
            }
            super.setAvailable(available);
        }
    }

    public static <K extends Option<?>, V extends Option<?>> HolderOptionBuilder<K, V> createBuilder() {
        return new HolderOptionBuilder<>();
    }

    public static <K extends Option<?>, V extends Option<?>> HolderOptionBuilder<K, V> createBuilder(OptionPair<K, V> optionPair) {
        return new HolderOptionBuilder<K, V>().optionPair(optionPair);
    }

    private static final class PairStateManager<K extends Option<?>, V extends Option<?>> implements StateManager<OptionPair<K, V>> {

        private final OptionPair<K, V> optionPair;
        private final K firstOption;
        private final V secondOption;
        private final List<StateListener<OptionPair<K, V>>> listeners = new ArrayList<>();

        private PairStateManager(OptionPair<K, V> optionPair) {
            this.optionPair = optionPair;
            this.firstOption = optionPair.firstOption();
            this.secondOption = optionPair.secondOption();

            firstOption.stateManager().addListener((_, _) -> fire());
            secondOption.stateManager().addListener((_, _) -> fire());
        }

        @Override
        public void set(OptionPair<K, V> value) {
            Validate.notNull(value, "`value` cannot be null");

            boolean isSamePair = value.firstOption() == firstOption && value.secondOption() == secondOption;
            Validate.isTrue(isSamePair, "HolderOption does not support replacing its option pair");

            fire();
        }

        @Override
        public OptionPair<K, V> get() {
            return optionPair;
        }

        @Override
        public void apply() {
            firstOption.applyValue();
            secondOption.applyValue();
        }

        @Override
        public void resetToDefault(ResetAction action) {
            firstOption.stateManager().resetToDefault(action);
            secondOption.stateManager().resetToDefault(action);
        }

        @Override
        public void sync() {
            firstOption.stateManager().sync();
            secondOption.stateManager().sync();
        }

        @Override
        public boolean isSynced() {
            return firstOption.stateManager().isSynced() && secondOption.stateManager().isSynced();
        }

        @Override
        public boolean isAlwaysSynced() {
            return firstOption.stateManager().isAlwaysSynced() && secondOption.stateManager().isAlwaysSynced();
        }

        @Override
        public boolean isDefault() {
            return firstOption.stateManager().isDefault() && secondOption.stateManager().isDefault();
        }

        @Override
        public void addListener(StateListener<OptionPair<K, V>> stateListener) {
            Validate.notNull(stateListener, "`stateListener` cannot be null");
            listeners.add(stateListener);
        }

        private void fire() {
            for (StateListener<OptionPair<K, V>> listener : listeners) {
                listener.onStateChange(optionPair, optionPair);
            }
        }
    }

    @ApiStatus.Internal
    public static final class HolderOptionBuilder<K extends Option<?>, V extends Option<?>> implements Builder<OptionPair<K, V>> {

        private Component name = Component.literal("Name not specified!").withStyle(ChatFormatting.RED);
        private Function<OptionPair<K, V>, OptionDescription> descriptionFunction = pending -> OptionDescription.EMPTY;
        private Function<Option<OptionPair<K, V>>, DualController<K, V>> controlGetter;
        private OptionPair<K, V> optionPair;
        private boolean available = true;
        private final Set<OptionFlag> flags = new HashSet<>();
        private final List<OptionEventListener<OptionPair<K, V>>> listeners = new ArrayList<>();

        @Override
        public Builder<OptionPair<K, V>> name(Component name) {
            Validate.notNull(name, "`name` cannot be null");
            this.name = name;
            return this;
        }

        @Override
        public Builder<OptionPair<K, V>> description(OptionDescription description) {
            Validate.notNull(description, "`content` cannot be null");
            return description(pending -> description);
        }

        @Override
        public Builder<OptionPair<K, V>> description(Function<OptionPair<K, V>, OptionDescription> descriptionFunction) {
            Validate.notNull(descriptionFunction, "`descriptionFunction` cannot be null");
            this.descriptionFunction = descriptionFunction;
            return this;
        }

        @Override
        public Builder<OptionPair<K, V>> controller(Function<Option<OptionPair<K, V>>, ControllerBuilder<OptionPair<K, V>>> controllerBuilder) {
            Validate.notNull(controllerBuilder, "`controllerBuilder` cannot be null");
            return customController(opt -> controllerBuilder.apply(opt).build());
        }

        @Override
        public Builder<OptionPair<K, V>> customController(Function<Option<OptionPair<K, V>>, Controller<OptionPair<K, V>>> control) {
            Validate.notNull(control, "`control` cannot be null");
            this.controlGetter = opt -> (DualController<K, V>) control.apply(opt);
            return this;
        }

        @Override
        public Builder<OptionPair<K, V>> stateManager(StateManager<OptionPair<K, V>> stateManager) {
            throw new UnsupportedOperationException("HolderOption uses its own state manager. Use the child options' state managers instead.");
        }

        @Override
        public Builder<OptionPair<K, V>> binding(Binding<OptionPair<K, V>> binding) {
            throw new UnsupportedOperationException("HolderOption does not support binding(). Bind the child options instead.");
        }

        @Override
        public Builder<OptionPair<K, V>> binding(
                OptionPair<K, V> def,
                Supplier<OptionPair<K, V>> getter,
                Consumer<OptionPair<K, V>> setter
        ) {
            throw new UnsupportedOperationException("HolderOption does not support binding(). Bind the child options instead.");
        }

        public HolderOptionBuilder<K, V> optionPair(OptionPair<K, V> optionPair) {
            Validate.notNull(optionPair, "`optionPair` cannot be null");
            this.optionPair = optionPair;
            return this;
        }

        @Override
        public Builder<OptionPair<K, V>> available(boolean available) {
            this.available = available;
            return this;
        }

        @Override
        public Builder<OptionPair<K, V>> flag(OptionFlag... flag) {
            Validate.notNull(flag, "`flag` must not be null");
            flags.addAll(Arrays.asList(flag));
            return this;
        }

        @Override
        public Builder<OptionPair<K, V>> flags(Collection<? extends OptionFlag> flags) {
            Validate.notNull(flags, "`flags` must not be null");
            this.flags.addAll(flags);
            return this;
        }

        @Override
        public Builder<OptionPair<K, V>> addListener(OptionEventListener<OptionPair<K, V>> optionEventListener) {
            Validate.notNull(optionEventListener, "`optionEventListener` must not be null");
            listeners.add(optionEventListener);
            return this;
        }

        @Override
        public Builder<OptionPair<K, V>> addListeners(Collection<OptionEventListener<OptionPair<K, V>>> collection) {
            Validate.notNull(collection, "`collection` must not be null");
            listeners.addAll(collection);
            return this;
        }

        @Override
        @Deprecated
        public Builder<OptionPair<K, V>> instant(boolean instant) {
            throw new UnsupportedOperationException("HolderOption does not support instant(). Use the child options' state managers instead.");
        }

        @Override
        @Deprecated
        public Builder<OptionPair<K, V>> listener(BiConsumer<Option<OptionPair<K, V>>, OptionPair<K, V>> listener) {
            Validate.notNull(listener, "`listener` must not be null");
            return addListener((opt, event) -> listener.accept(opt, opt.pendingValue()));
        }

        @Override
        @Deprecated
        public Builder<OptionPair<K, V>> listeners(Collection<BiConsumer<Option<OptionPair<K, V>>, OptionPair<K, V>>> listeners) {
            Validate.notNull(listeners, "`listeners` must not be null");
            for (BiConsumer<Option<OptionPair<K, V>>, OptionPair<K, V>> listener : listeners) {
                listener(listener);
            }
            return this;
        }

        @Override
        public Option<OptionPair<K, V>> build() {
            Validate.notNull(optionPair, "`optionPair` must not be null when building HolderOption");
            Validate.notNull(controlGetter, "`control` must not be null when building HolderOption");

            K firstOption = optionPair.firstOption();
            V secondOption = optionPair.secondOption();

            Component combinedName = firstOption.name().copy().append(" & ").append(secondOption.name().copy());

            Function<OptionPair<K, V>, OptionDescription> combinedDescriptionFunction =
                    pending -> OptionDescription.of(
                            firstOption.description().text().copy()
                                    .append("\n\n")
                                    .append(secondOption.description().text())
                    );

            StateManager<OptionPair<K, V>> stateManager = new PairStateManager<>(optionPair);

            return new HolderOption<>(
                    combinedName,
                    combinedDescriptionFunction,
                    controlGetter,
                    stateManager,
                    available,
                    ImmutableSet.copyOf(flags),
                    listeners);
        }
    }
}