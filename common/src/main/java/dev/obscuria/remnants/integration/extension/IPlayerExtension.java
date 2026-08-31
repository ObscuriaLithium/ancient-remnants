package dev.obscuria.remnants.integration.extension;

import dev.obscuria.remnants.common.blessing.Blessing;
import net.minecraft.core.Holder;
import org.jetbrains.annotations.Nullable;

public interface IPlayerExtension {

    @Nullable Holder<Blessing> ancientRemnants$getBlessing();

    void ancientRemnants$setBlessing(@Nullable Holder<Blessing> blessing);
}
