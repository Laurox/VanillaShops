package de.laurox.mc;

import de.laurox.mc.files.FileManager;

import java.util.Map;

public class MessageParser {

    private static String prefix = null;

    public static String send(String key) {
        return send(key, null);
    }

    public static String send(String key, Map<String, String> variables) {
        if(variables == null || variables.size() == 0) {
            return fetchTemplateString(key);
        }

        String templateString = fetchTemplateString(key);

        for(Map.Entry<String, String> entry : variables.entrySet()) {
            templateString = templateString.replace(entry.getKey(), entry.getValue());
        }

        return templateString;
    }

    private static String fetchTemplateString(String key) {
        // fetch String from config
        String templateString = FileManager.getLanguageConfig().getString(key);

        // replace & to real color codes (§)
        templateString = templateString.replaceAll("&", "§");

        // load prefix
        if(prefix == null) {
            prefix = FileManager.getLanguageConfig().getString("General.prefix");
            assert prefix != null;
            prefix = prefix.replaceAll("&", "§");
        }

        return prefix + templateString;
    }
}
