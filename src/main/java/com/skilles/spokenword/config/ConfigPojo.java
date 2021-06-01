package com.skilles.spokenword.config;

import com.skilles.spokenword.SpokenWord;
import io.github.fablabsmc.fablabs.api.fiber.v1.annotation.Setting;

import java.util.Arrays;
import java.util.List;

/**
 * Contains values for config fields
 */
public class ConfigPojo {
    @Setting.Group
    public static GeneralGroup generalGroup = new GeneralGroup();

    public static class GeneralGroup {
        @Setting(comment = "Global enable")
        public boolean globalEnable = true;
        @Setting(comment = "Auto respawn")
        public boolean respawn = false;
    }
    @Setting.Group
    public static ModeGroup modeGroup = new ModeGroup();

    public static class ModeGroup {
        @Setting(comment = "Sends when another player joins")
        public boolean playerjoin = true;
        @Setting(comment = "Sends when you join a server")
        public boolean onJoin = false;
        @Setting(comment = "Sends when you die")
        public boolean death = true;
        @Setting(comment = "Sends when you break a block")
        public boolean onBreak = false;
        @Setting(comment = "Sends when a mob you have named or tamed is killed")
        public boolean onOwnedDeath = true;
        @Setting(comment = "Sends when an entity dies")
        public boolean onEntityDeath = false;
    }
    @Setting.Group
    public static DeathGroup deathGroup = new DeathGroup();

    public static class DeathGroup {
        @Setting(comment = "placeholder")
        public boolean pvp = true;
        @Setting(comment = "placeholder")
        public boolean pve = false;
        @Setting(comment = "Only entities in this list will trigger when they die")
        public List<String> entityDeathList = Arrays.asList(SpokenWord.EntityTypes.VILLAGER.name());
        @Setting(comment = "Only entities in this list will trigger when they kill you")
        public List<String> entityKillList = Arrays.asList(SpokenWord.EntityTypes.ANY.name());
    }
    @Setting.Group
    public static ChatGroup chatGroup = new ChatGroup();

    public static class ChatGroup {
        @Setting(comment = "Sends when someone sends a certain message")
        public boolean onChat = false;
        public boolean onMessage = true;
        @Setting(comment = "Criteria for 'On Chat' mode")
        public List<String> chatCriteria = Arrays.asList("%p");
        @Setting(comment = "Criteria for 'On Message' mode")
        public List<String> messageCriteria = Arrays.asList("%p");
    }
    @Setting.Group
    public static ListGroup listGroup = new ListGroup();

    public static class ListGroup {
        @Setting(comment = "List for 'PvP' mode")
        public List<String> pvpList = Arrays.asList("%p has killed me");
        @Setting(comment = "List for 'PvE' mode")
        public List<String> pveList = Arrays.asList("%e has killed me");
        @Setting(comment = "placeholder")
        public List<String> ownDeathList = Arrays.asList("%e has died. RIP");
        @Setting(comment = "placeholder")
        public List<String> entityDiedList = Arrays.asList("%e has died");
        @Setting(comment = "Lists for 'Join' toggle")
        public List<String> playerJoinList = Arrays.asList("Welcome %p!");
        @Setting(comment = "Lists for 'On Join' toggle")
        public List<String> onJoinList = Arrays.asList("I, %p, have joined the server");
        @Setting(comment = "Lists for 'On Break' toggle")
        public List<String> breakList = Arrays.asList("I have broken %b");
        @Setting(comment = "Lists for 'On Chat' toggle")
        public List<String> chatList = Arrays.asList("Oi!");
        @Setting(comment = "Lists for 'On Chat' toggle")
        public List<String> messageList = Arrays.asList("/tell %p what's up");
    }
}
