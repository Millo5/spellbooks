package com.example.androidproject.Util.Response;

import java.util.Arrays;


/// Gson generated
public class SpellData {
    private String index;
    private String name;
    private String[] desc;
    private String[] higher_level;

    private String range;
    private String[] components;
    private String material;

    private boolean ritual;
    private String duration;
    private boolean concentration;
    private String casting_time;

    private int level;


    public String getDescription() {
        StringBuilder sb = new StringBuilder();

        if (level > 0) sb.append("Level: ").append(level).append("\n\n");

        sb.append("Duration: ").append(duration);
        if (concentration) sb.append(" (CONCENTRATION)");
        if (ritual) sb.append(" (RITUAL)");

        sb.append("\nCasting Time: ").append(casting_time).append("\n");
        sb.append("Range: ").append(range).append("\n\n");

        if (components != null) {
            sb.append("Components: ");
            for (String component : components) {
                sb.append(component).append(", ");
            }
            sb.delete(sb.length() - 2, sb.length());
            sb.append("\n");
        }

        if (material != null) sb.append("Material: ").append(material).append("\n");

        sb.append("\n");
        if (desc != null && desc.length > 0) Arrays.stream(desc).forEach(s -> sb.append(s).append("\n"));
        if (higher_level != null && higher_level.length > 0) {
            sb.append("\nAt Higher Levels:\n");
            Arrays.stream(higher_level).forEach(s -> sb.append(s).append("\n"));
        }

        String desc = sb.toString();

        // Remove markdown
        desc = desc.replaceAll("\\*\\*(.*?)\\*\\*", "$1")
                .replaceAll("\\*(.*?)\\*", "$1");

        return desc;
    }
}
