package com.homeandtp.plugin.service;
import com.homeandtp.plugin.model.Home;
import java.util.Optional;

public class HomeResult {
    private final boolean success;
    private final String message;
    private final Home home;

    private HomeResult(boolean success, String message, Home home) {
        this.success = success;
        this.message = message;
        this.home = home;
    }
    public static HomeResult success(String message, Home home) {
        return new HomeResult(true, message, home);
    }
    public static HomeResult failure(String message) {
        return new HomeResult(false, message, null);
    }
    public static HomeResult failure(String message, Home home) {
        return new HomeResult(false, message, home);
    }

    public boolean isSuccess() {
        return success;
    }
    public String getMessage() {
        return message;
    }
    public Optional<Home> getHome() {
        return Optional.ofNullable(home);
    }

}
