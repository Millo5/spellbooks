package com.example.androidproject.Util.Response;


/// Gson generated
public class SpellListResponse {

    private int count;
    private SpellListElement[] results;

    public SpellListElement[] getResults() {
        return results;
    }

    public static class SpellListElement {
        private String index;
        private String name;
        private int level;
        private String url;

        public String getIndex() {
            return index;
        }

        public String getName() {
            return name;
        }
    }

}
