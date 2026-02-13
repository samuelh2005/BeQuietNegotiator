package me.ajh123.be_quiet_negotiator;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

//? if <1.21.7 {
/*@EventBusSubscriber(modid = BeQuietNegotiator.MODID, bus = EventBusSubscriber.Bus.MOD)
*///?}
//? if >=1.21.7 {
@EventBusSubscriber(modid = BeQuietNegotiator.MODID)
//?}
public class ClientConfig
{
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.ConfigValue<Boolean> BYPASS_NEGOTIATION_ERRORS = BUILDER
            .comment("If true, client will bypass negotiation errors when connecting to a vanilla server.")
            .define("bypassNegotiationErrors", true);

    private static final ModConfigSpec.ConfigValue<Boolean> BYPASS_CUSTOM_FEATURE_FLAGS = BUILDER
            .comment("If true, client will bypass custom feature flags when connecting to a vanilla server.")
            .define("bypassCustomFeatureFlags", true);

    private static final ModConfigSpec.ConfigValue<Boolean> IGNORE_PACKET_DECODING_ERRORS = BUILDER
            .comment("If true, client will ignore packet decoding errors when connecting to a vanilla server.")
            .define("ignorePacketDecodingErrors", true);

    private static final ModConfigSpec.ConfigValue<Boolean> IGNORE_PACKET_HANDLER_ERRORS = BUILDER
            .comment("If true, client will ignore packet handler errors when connecting to a vanilla server.")
            .define("ignorePacketHandlerErrors", true);

    static final ModConfigSpec SPEC = BUILDER.build();

    private static boolean bypass_negotiation_errors;

    private static boolean bypassCustomFeatureFlags;

    private static boolean ignorePacketDecodingErrors;

    private static boolean ignorePacketHandlerErrors;


    @SubscribeEvent
    static void onLoad(final ModConfigEvent event){
        // Ensure the event is for the correct configuration type
        if (event.getConfig().getType() != ModConfig.Type.CLIENT) {
            return;
        }

        // Load the boolean value
        bypass_negotiation_errors = BYPASS_NEGOTIATION_ERRORS.get();
        bypassCustomFeatureFlags = BYPASS_CUSTOM_FEATURE_FLAGS.get();
        ignorePacketDecodingErrors = IGNORE_PACKET_DECODING_ERRORS.get();
        ignorePacketHandlerErrors = IGNORE_PACKET_HANDLER_ERRORS.get();
    }

    public static boolean bypassNegotiationErrors() {
        return bypass_negotiation_errors;
    }

    public static boolean bypassCustomFeatureFlags() {
        return bypassCustomFeatureFlags;
    }

    public static boolean ignorePacketDecodingErrors() {
        return ignorePacketDecodingErrors;
    }

    public static boolean ignorePacketHandlerErrors() {
        return ignorePacketHandlerErrors;
    }
}