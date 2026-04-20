package baritone.api.fakeplayer;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

import java.util.UUID;

public record FakePlayerSpawnPayload(
        int entityId,
        UUID uuid,
        int entityType,
        String name,
        double x, double y, double z,
        byte yaw, byte pitch, byte headYaw
) implements CustomPayload {

    public static final CustomPayload.Id<FakePlayerSpawnPayload> ID =
            new CustomPayload.Id<>(Identifier.of("automatone", "fake_player_spawn"));

    public static final PacketCodec<RegistryByteBuf, FakePlayerSpawnPayload> CODEC =
            PacketCodec.of(
                    (payload, buf) -> {
                        buf.writeVarInt(payload.entityId);
                        buf.writeUuid(payload.uuid);
                        buf.writeVarInt(payload.entityType);
                        buf.writeString(payload.name);
                        buf.writeDouble(payload.x);
                        buf.writeDouble(payload.y);
                        buf.writeDouble(payload.z);
                        buf.writeByte(payload.yaw);
                        buf.writeByte(payload.pitch);
                        buf.writeByte(payload.headYaw);
                    },
                    buf -> new FakePlayerSpawnPayload(
                            buf.readVarInt(),
                            buf.readUuid(),
                            buf.readVarInt(),
                            buf.readString(),
                            buf.readDouble(),
                            buf.readDouble(),
                            buf.readDouble(),
                            buf.readByte(),
                            buf.readByte(),
                            buf.readByte()
                    )
            );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}