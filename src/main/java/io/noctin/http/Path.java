package io.noctin.http;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Path {

    public static final String MAINPATTERN_GROUP = "PARAM";
    public static final Pattern PATH_PARAM = Pattern.compile("(?<PARAM>(?<!\\})\\{(?![0-9])[a-zA-Z0-9]+\\}(?!\\{))");
    // TODO: Generify, end or start by /, lookaheads etc. "/{id}
    public static final String BUILT_RAW = "(?<%s>[^\\/]+)";

    private final String raw;
    private final LinkedList<String> groups = new LinkedList<>();
    private final Pattern pattern;


    public Path(String path) {
        this.raw = path;
        this.pattern = build(path);
    }

    // This is a bit messy
    private Pattern build(String raw){

        Matcher matcher = PATH_PARAM.matcher(raw);

        String rawPattern = this.raw;

        // We need an offset, to match the correct start and end,
        // In function of what subPattern we append
        int offset = 0;
        while (matcher.find()){
            String group = matcher.group(MAINPATTERN_GROUP);
            int rawGroupLength = group.length();
            group = group.substring(1, group.length() - 1);
            groups.add(group);

            String builtSubPattern = String.format(BUILT_RAW, group);

            int start = matcher.start(MAINPATTERN_GROUP) + offset;
            int end = matcher.end(MAINPATTERN_GROUP) + offset;

            String first = rawPattern.substring(0, start);
            String last = rawPattern.substring(end, rawPattern.length());

            // We insert the built subPattern
            rawPattern = first + builtSubPattern + last;

            // We add the number of added chars to always match with the first raw
            offset += builtSubPattern.length() - rawGroupLength;
        }

        // We escape the brackets {} to prevent exceptions with regex quantifiers
        rawPattern = sanitizePattern(rawPattern);

        // The generated pattern must match from the beginning
        char beginToken = '^';

        return Pattern.compile(beginToken + rawPattern);
    }

    private String sanitizePattern(String pattern){
        pattern = pattern.replace("{", "\\{");
        return pattern.replace("}", "\\}");
    }

    public boolean matches(String path){
        return this.pattern.matcher(path).matches();
    }

    public LinkedHashMap<String, String> parse(String path){
        LinkedHashMap<String, String> map = new LinkedHashMap<>();

        Matcher matcher = this.pattern.matcher(path);

        if (matcher.matches()){
            for (String group : groups) {
                map.put(group, matcher.group(group));
            }
        }

        return map;
    }

    public String getRaw() {
        return raw;
    }

    public LinkedList<String> getGroups() {
        return new LinkedList<>(this.groups);
    }

    public Pattern getPattern() {
        return pattern;
    }
}
