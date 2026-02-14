package me.ajh123.be_quiet_negotiator.mixins;

import com.google.common.collect.ImmutableSet;
import me.ajh123.be_quiet_negotiator.BeQuietNegotiator;
import me.ajh123.be_quiet_negotiator.ClientConfig;
import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.ClientCommonPacketListener;
import net.minecraft.network.protocol.configuration.ClientConfigurationPacketListener;
import net.minecraft.network.protocol.configuration.ServerConfigurationPacketListener;
//? if <1.21.11 {
import net.minecraft.resources.ResourceLocation;
//?}
//? if >=1.21.11 {
/*import net.minecraft.resources.Identifier;
*///?}
import net.neoforged.fml.config.ConfigTracker;
//? if <1.21.2 {
/*import net.neoforged.neoforge.network.configuration.CheckFeatureFlags;
*///?}
import net.neoforged.neoforge.network.filters.NetworkFilters;
import net.neoforged.neoforge.network.payload.MinecraftRegisterPayload;
import net.neoforged.neoforge.network.payload.ModdedNetworkQueryComponent;
import net.neoforged.neoforge.network.registration.ChannelAttributes;
import net.neoforged.neoforge.network.registration.NetworkPayloadSetup;
import net.neoforged.neoforge.network.registration.NetworkRegistry;
import net.neoforged.neoforge.network.registration.PayloadRegistration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;

@Mixin(NetworkRegistry.class)
public class NetworkRegistryMixin {
    @Inject(
            method = "initializeNeoForgeConnection(Lnet/minecraft/network/protocol/configuration/ServerConfigurationPacketListener;Ljava/util/Map;)V",
            at = @At("TAIL"),
            remap = false
    )
    private static void initializeNeoForgeConnection(ServerConfigurationPacketListener listener, Map<ConnectionProtocol, Set<ModdedNetworkQueryComponent>> clientChannels, CallbackInfo ci) {
        BeQuietNegotiator.isConnectedToVanillaServer = false;
    }

    //? if <1.21.7 {
    /*@Inject(
            method = "initializeOtherConnection(Lnet/minecraft/network/protocol/configuration/ClientConfigurationPacketListener;)V",
            at = @At("HEAD"),
            remap = false,
            cancellable = true
    )
    private static void initializeOtherConnection(ClientConfigurationPacketListener listener, CallbackInfo ci) {
        BeQuietNegotiator.isConnectedToVanillaServer = true;
        // <Default NeoForge implementation>

        // Because we are in vanilla land, no matter what we are not able to support any custom channels.
        ChannelAttributes.setPayloadSetup(listener.getConnection(), NetworkPayloadSetup.empty());
        ChannelAttributes.setConnectionType(listener.getConnection(), listener.getConnectionType());

        // Use reflection to unlock the private PAYLOAD_REGISTRATIONS static member from NetworkRegistry
        Map<ConnectionProtocol, Map<ResourceLocation, PayloadRegistration<?>>> PAYLOAD_REGISTRATIONS = getConnectionProtocolMap();

        // </Default NeoForge implementation>

        // We are on vanilla, skip payload and extended enums negotiation as these checks would fail anyway.

        if (ClientConfig.bypassNegotiationErrors()) {
            // <Default NeoForge implementation>
            // We are on the client, connected to a vanilla server, make sure we don't have any modded feature flags
            // or bypass custom feature flags if configured to do so.
            //? if <1.21.2 {
            /^if (!ClientConfig.bypassCustomFeatureFlags() && !CheckFeatureFlags.handleVanillaServerConnection(listener)) {
                return;
            }
            ^///?}
            //? if >=1.21.2 {
            if (!ClientConfig.bypassCustomFeatureFlags()) {
                return; }
            //?}

            // We are on the client, connected to a vanilla server, We have to load the default configs.
            ConfigTracker.INSTANCE.loadDefaultServerConfigs();

            NetworkFilters.injectIfNecessary(listener.getConnection());

            ImmutableSet.Builder<ResourceLocation> nowListeningOn = ImmutableSet.builder();
            nowListeningOn.addAll(NetworkRegistry.getInitialListeningChannels(listener.flow()));
            PAYLOAD_REGISTRATIONS.get(ConnectionProtocol.CONFIGURATION).entrySet().stream()
                    .filter(registration -> registration.getValue().matchesFlow(listener.flow()))
                    .filter(registration -> registration.getValue().optional())
                    .forEach(registration -> nowListeningOn.add(registration.getKey()));
            listener.send(new MinecraftRegisterPayload(nowListeningOn.build()));
            // </Default NeoForge implementation>

            ci.cancel();
        }
        // Only propagate to super if we are not connected to a vanilla server - done automatically by not returning early.
    }
    *///?}

//? if <1.21.11 {
    @SuppressWarnings("unchecked")
    private static Map<ConnectionProtocol, Map<ResourceLocation, PayloadRegistration<?>>> getConnectionProtocolMap() {
        Field payloadRegistrationsField = null;
        try {
            payloadRegistrationsField = NetworkRegistry.class.getDeclaredField("PAYLOAD_REGISTRATIONS");
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        payloadRegistrationsField.setAccessible(true);

        Map<ConnectionProtocol, Map<ResourceLocation, PayloadRegistration<?>>> PAYLOAD_REGISTRATIONS = null;
        try {
            PAYLOAD_REGISTRATIONS = (Map<ConnectionProtocol, Map<ResourceLocation, PayloadRegistration<?>>>) payloadRegistrationsField.get(null);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return PAYLOAD_REGISTRATIONS;
    }
    //?}
    //? if >=1.21.11 {
    /*@SuppressWarnings("unchecked")
    private static Map<ConnectionProtocol, Map<Identifier, PayloadRegistration<?>>> getConnectionProtocolMap() {
        Field payloadRegistrationsField = null;
        try {
            payloadRegistrationsField = NetworkRegistry.class.getDeclaredField("PAYLOAD_REGISTRATIONS");
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        payloadRegistrationsField.setAccessible(true);

        Map<ConnectionProtocol, Map<Identifier, PayloadRegistration<?>>> PAYLOAD_REGISTRATIONS = null;
        try {
            PAYLOAD_REGISTRATIONS = (Map<ConnectionProtocol, Map<Identifier, PayloadRegistration<?>>>) payloadRegistrationsField.get(null);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return PAYLOAD_REGISTRATIONS;
    }
    *///?}

    @Inject(
            method = "checkPacket(Lnet/minecraft/network/protocol/Packet;Lnet/minecraft/network/protocol/common/ClientCommonPacketListener;)V",
            at = @At("HEAD"),
            remap = false,
            cancellable = true
    )
    private static void checkPacket(Packet<?> packet, ClientCommonPacketListener listener, CallbackInfo ci) {
        // If we are connected to a vanilla server, we don't need to check the packet.
        if (ClientConfig.bypassNegotiationErrors() && BeQuietNegotiator.isConnectedToVanillaServer) {
            ci.cancel();
        }
    }
}
