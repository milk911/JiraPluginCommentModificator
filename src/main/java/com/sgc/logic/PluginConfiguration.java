package com.sgc.logic;

import com.atlassian.sal.api.pluginsettings.PluginSettings;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public final class PluginConfiguration {

    private static PluginSettings pluginSettings;
    private static final String ALIASES = "aliases";

    private PluginConfiguration() { }

    public static void initPluginConfiguration(PluginSettingsFactory pluginSettingsFactory) {
        pluginSettings = pluginSettingsFactory.createGlobalSettings();

        if (pluginSettings.get(ALIASES) == null) {
            List<CommentAlias> aliases = new ArrayList<>();
            aliases.add(new CommentAlias("@developers-example", "[~Ivan], [~Alex], [~Julia]", 1));
            pluginSettings.put(ALIASES, arrayListToJson(aliases));
        }
    }

    public static String delAlias(String idString) {
        synchronized (pluginSettings) {
            List<CommentAlias>aliases = jsonToArrayList((String)pluginSettings.get(ALIASES));
            int id = Integer.parseInt(idString);
            for (int i = 0; i < aliases.size(); i++) {
                if (aliases.get(i).getId() == id) {
                    aliases.remove(i);
                    pluginSettings.put(ALIASES, arrayListToJson(aliases));
                    return "ok";
                }
            }
        }
        return "error";
    }

    public static String addAlias(CommentAlias newAlias) {
        if (newAlias == null)
            return "Error by creating alias. Check params.";

        synchronized (pluginSettings) {
            List<CommentAlias> aliases = jsonToArrayList((String)pluginSettings.get(ALIASES));
            for (int i = 0; i < aliases.size() ; i++) {
                if (aliases.get(i).getAlias().equals(newAlias.getAlias())) {
                    return "Error. Alias '" + newAlias.getAlias() + "' already exists!";
                }
            }

            int maxId = 0;
            for (int i = 0; i < aliases.size() ; i++) {
                if (aliases.get(i).getId() > maxId) {
                    maxId = aliases.get(i).getId();
                }
            }
            newAlias.setId(++maxId);
            aliases.add(newAlias);

            pluginSettings.put(ALIASES, arrayListToJson(aliases));
        }

        return String.valueOf(newAlias.getId());
    }

    public static List<CommentAlias> getAliases() {

        synchronized (pluginSettings) {
             return jsonToArrayList((String)pluginSettings.get(ALIASES));
        }
    }

    public static String arrayListToJson(List list) {
        Gson gsonBuilder = new GsonBuilder().create();
        return gsonBuilder.toJson(list);
    }

    public static List jsonToArrayList(String gson) {
        Type listType = new TypeToken<ArrayList<CommentAlias>>(){}.getType();
        return new Gson().fromJson(gson, listType);
    }
}
