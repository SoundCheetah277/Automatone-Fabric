package baritone.api.fakeplayer;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

import com.mojang.authlib.GameProfile;

public record FakePlayerProfileUpdatePayload(
        int entityId,
        GameProfile displayProfile
) implements CustomPayload {

    public static final CustomPayload.Id<FakePlayerProfileUpdatePayload> ID =
            new CustomPayload.Id<>(Identifier.of("automatone", "fake_player_profile"));

    public static final PacketCodec<RegistryByteBuf, FakePlayerProfileUpdatePayload> CODEC =
            PacketCodec.of(
                    (payload, buf) -> {
                        buf.writeVarInt(payload.entityId);
                        writeProfile(new PacketByteBuf(buf), payload.displayProfile);
                    },
                    buf -> new FakePlayerProfileUpdatePayload(
                            buf.readVarInt(),
                            readProfile(new PacketByteBuf(buf))
                    )
            );

    private static void writeProfile(PacketByteBuf buf, GameProfile profile) {
        buf.writeBoolean(profile != null);
        if (profile != null) {
            buf.writeUuid(profile.getId());
            buf.writeString(profile.getName());
        }
    }

    private static GameProfile readProfile(PacketByteBuf buf) {
        boolean present = buf.readBoolean();
        return present ? new GameProfile(buf.readUuid(), buf.readString()) : null;
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}