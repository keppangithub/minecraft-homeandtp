package com.homeandtp.plugin.model;

import java.util.UUID;

//**  */
public class Home { 
    
    private final UUID playerUuid;

    private final String homeName;

    private final String worldName;
    private final double x;
    private final double y;
    private final double z;
    private final float yaw;
    private final float pitch;

    public Home(UUID playerUuid, String homeName, String worldName, double x, double y, double z, float yaw, float pitch) {
        this.playerUuid = playerUuid;
        this.homeName = homeName;
        this.worldName = worldName;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public UUID getPlayerUuid() {
        return playerUuid;
    }
    public String getHomeName() {
        return homeName;
    }
    public String getWorldName() {
        return worldName;
    }
    public double getX() {
        return x;
    }   
    public double getY() {
        return y;
    }
    public double getZ() {
        return z;
    }
    public float getYaw() {
        return yaw;
    }
    public float getPitch() {
        return pitch;
    }
}
