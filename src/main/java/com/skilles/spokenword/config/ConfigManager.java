package com.skilles.spokenword.config;

import net.minecraft.util.Identifier;

import java.util.Collections;
import java.util.List;

import static com.skilles.spokenword.config.ConfigPojo.*;
import static com.skilles.spokenword.util.Util.*;

public class ConfigManager {
    
    public static boolean globalEnabled() {
        return generalGroup.globalEnable;
    }
    public static boolean autoRespawn() {
        return generalGroup.respawn;
    }
    public static List<String> getList(Identifier id) {
        if (id.equals(PVP_LIST)) {
            return listConfig().pvpList;
        } else if (id.equals(PVE_LIST)) {
            return listConfig().pveList;
        } else if (id.equals(PLAYER_JOIN_LIST)) {
            return listConfig().playerJoinList;
        } else if (id.equals(ON_JOIN_LIST)) {
            return listConfig().onJoinList;
        } else if(id.equals(BREAK_LIST)) {
            return listConfig().breakList;
        } else if(id.equals(OWNED_DEATH_LIST)) {
            return listConfig().ownDeathList;
        } else if(id.equals(ENTITY_DEATH_LIST)) {
            return listConfig().entityDiedList;
        } else if(id.equals(CHAT_LIST)) {
            return listConfig().chatList;
        } else if(id.equals(MESSAGE_LIST)) {
            return listConfig().messageList;
        }
        return Collections.singletonList("UNKNOWN LIST");
    }
    public static List<String> getCriteria(Identifier id) {
        if(id.equals(CHAT_LIST)) {
            return chatConfig().chatCriteria;
        } else if(id.equals(MESSAGE_LIST)) {
            return chatConfig().messageCriteria;
        }
        return Collections.emptyList();
    }
    public static GeneralGroup generalConfig() {
        return generalGroup;
    }
    public static DeathGroup deathConfig() {
        return deathGroup;
    }
    public static ChatGroup chatConfig() {
        return chatGroup;
    }
    public static ModeGroup modeConfig() {
        return modeGroup;
    }
    public static ListGroup listConfig() { return listGroup; }
}