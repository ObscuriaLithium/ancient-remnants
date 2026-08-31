package dev.obscuria.remnants.integration.mixin;

import dev.obscuria.remnants.integration.hook.HookTagLoader;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagLoader;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Map;

@Mixin(value = TagLoader.class, priority = 0)
public abstract class MixinTagLoader {

    private @Final @Shadow String directory;

    @SuppressWarnings("rawtypes")
    @Inject(method = "build(Ljava/util/Map;)Ljava/util/Map;", at = @At("HEAD"))
    private void build$onHead(
            Map<Identifier, List<TagLoader.EntryWithSource>> builders,
            CallbackInfoReturnable<Map> cir
    ) {
        HookTagLoader.build(directory, builders);
    }
}