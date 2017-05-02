/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bot;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 *
 * @author renardn
 */
public class Config {

    private String token;
    private String ownerId;
    private String serverId;
    private String textChannelId;
    private String voiceChannelId;
    private String prefix;
    private String commanderRole;

    public Config() {
    }

    public boolean readFromConfigFile(String path) {
        boolean ok = false;
        List<String> lines;
        try {
            lines = Files.readAllLines(Paths.get(path));
            for (String line : lines) {
                if ((!line.startsWith("//")) && (!line.isEmpty())) {
                    String[] parts = line.split("=", 2);
                    String key = parts[0];
                    String option = parts[1];
                    switch (key) {
                        case "token":
                            token = option;
                            break;
                        case "ownerId":
                            ownerId = option;
                            break;
                        case "serverId":
                            serverId = option;
                            break;
                        case "textChannelId":
                            textChannelId = option;
                            break;
                        case "voiceChannelId":
                            voiceChannelId = option;
                            break;
                        case "prefix":
                            prefix = option;
                            break;
                        case "commanderRole":
                            commanderRole = option;
                            break;
                    }
                    ok = true;
                } else {
                }
            }
        } catch (IOException e) {
            System.err.println("Config file not found in: " + path+"\n Or config file is incorect check the example file ;)");
        }
        return ok;
    }

    public String getToken() {
        return token;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public String getServerId() {
        return serverId;
    }

    public String getTextChannelId() {
        return textChannelId;
    }

    public String getVoiceChannelId() {
        return voiceChannelId;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getCommanderRole() {
        return commanderRole;
    }
    
}
