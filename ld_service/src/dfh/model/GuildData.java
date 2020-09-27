package dfh.model;

import java.util.List;

/**
 * Created by Zii on 2017/9/14.
 */
public class GuildData {
    private List<Guild> guildList;

    private String selfGuild;

    public List<Guild> getGuildList() {
        return guildList;
    }

    public void setGuildList(List<Guild> guildList) {
        this.guildList = guildList;
    }

    public String getSelfGuild() {
        return selfGuild;
    }

    public void setSelfGuild(String selfGuild) {
        this.selfGuild = selfGuild;
    }
}
