package me.ajh123.be_quiet_negotiator.mixins;

//? if <1.21.2 {
/*import net.minecraft.client.multiplayer.TagCollector;

import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagNetworkSerialization;

import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TagCollector.class)
*///?}
public class TagCollectorMixin {
    //? if <1.21.2 {
    /*@Shadow
    private Map<ResourceKey<? extends Registry<?>>, TagNetworkSerialization.NetworkPayload> tags;

    @Inject(method = "applyTags", at = @At("HEAD"), remap = false, cancellable = true)
    private void applyTags(RegistryAccess arg, Predicate<ResourceKey<? extends Registry<?>>> predicate, CallbackInfo ci) {
        tags.forEach((arg2, arg3) -> {
            if (predicate.test(arg2)) {
                // VANILLA FIX: Vanilla would throw an exception if the registry was missing,
                // but we want to be more lenient and just skip it instead
                // They would use `arg3.applyToRegistry(arg.registryOrThrow(arg2));`
                Optional<Registry<Object>> registry = arg.registry(arg2);
                registry.ifPresent(r -> arg3.applyToRegistry(r));
            }

        });
        ci.cancel();
    }
    *///?}
}
