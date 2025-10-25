package com.homeandtp.plugin.data;

import java.util.UUID;
import java.util.Optional;
import com.homeandtp.plugin.model.Home;
import java.util.List;

public interface HomeRepository {
    void  saveHome(Home home);
    boolean deleteHome(UUID playerUuid, String homeName);
    Optional<Home> getHome(UUID playerUuid, String homeName);
    List<Home> listHomes(UUID playerUuid); 
}



