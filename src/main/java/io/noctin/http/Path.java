package io.noctin.http;

import java.util.regex.Pattern;

public final class Path {

    public static final Pattern PATH_PARAM = Pattern.compile("(?<PARAM>(?<!\\})\\{[a-zA-Z0-9]+\\}(?!\\{))");

    private final String raw;
    private final Pattern pattern;

    public Path(String path) {
        this.raw = path;
        this.pattern = build(path);
    }

    private Pattern build(String path){



        return null;
    }


}
