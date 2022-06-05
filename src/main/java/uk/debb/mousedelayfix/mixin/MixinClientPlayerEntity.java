package uk.debb.mousedelayfix.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ClientPlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ClientPlayerEntity.class)
public abstract class MixinClientPlayerEntity extends Entity {
    public MixinClientPlayerEntity(World world) {
        super(world);
    }

    @Override
    public Vec3d getRotationVector(float vector) {
        return this.getRotationVector(this.pitch, this.yaw);
    }
}
