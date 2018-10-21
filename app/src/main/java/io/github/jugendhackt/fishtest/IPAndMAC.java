package io.github.jugendhackt.fishtest;

/**
 * Erstellt von Julian am 21.10.2018 um 07:25.
 */
public class IPAndMAC {
    protected String IP;
    protected String MAC;

    public IPAndMAC(String IP, String MAC) {
        this.IP = IP;
        this.MAC = MAC;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public String getMAC() {
        return MAC;
    }

    public void setMAC(String MAC) {
        this.MAC = MAC;
    }
}
