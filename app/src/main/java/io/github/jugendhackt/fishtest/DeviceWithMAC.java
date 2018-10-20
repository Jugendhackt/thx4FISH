package io.github.jugendhackt.fishtest;

import com.stealthcopter.networktools.subnet.Device;

/**
 * Erstellt von Julian am 20.10.2018 um 17:46.
 */
public class DeviceWithMAC {
    protected Device device;
    protected String MAC;

    public DeviceWithMAC(Device device, String MAC) {
        this.device = device;
        this.MAC = MAC;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public String getMAC() {
        return MAC;
    }

    public void setMAC(String MAC) {
        this.MAC = MAC;
    }

    @Override
    public String toString(){
        return "IP: " + this.device.ip + " MAC: " + this.MAC;
    }
}
