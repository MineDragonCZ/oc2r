/* SPDX-License-Identifier: MIT */

package li.cil.oc2r.common.bus.device.rpc.block;

import li.cil.oc2r.api.bus.device.Device;
import li.cil.oc2r.api.bus.device.object.ObjectDevice;
import li.cil.oc2r.api.bus.device.provider.BlockDeviceQuery;
import li.cil.oc2r.api.util.Invalidatable;
import li.cil.oc2r.common.bus.device.provider.util.AbstractBlockEntityCapabilityDeviceProvider;
import li.cil.oc2r.common.bus.device.rpc.EnergyStorageDevice;
import li.cil.oc2r.common.capabilities.Capabilities;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.energy.IEnergyStorage;

public final class EnergyStorageBlockDeviceProvider extends AbstractBlockEntityCapabilityDeviceProvider<IEnergyStorage, BlockEntity> {
    public EnergyStorageBlockDeviceProvider() {
        super(Capabilities::energyStorage);
    }

    ///////////////////////////////////////////////////////////////////

    @Override
    protected Invalidatable<Device> getBlockDevice(final BlockDeviceQuery query, final IEnergyStorage value) {
        return Invalidatable.of(new ObjectDevice(new EnergyStorageDevice(value)));
    }
}
