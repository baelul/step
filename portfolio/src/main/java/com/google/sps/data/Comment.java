package com.google.sps.data;

/** Creates a Comment object */
public final class Comment {
    private final long id;
    private final String body;
    private final long time;

    public Comment(long id, String body, long time) {
      this.id = id;
      this.body = body;
      this.time = time;
    }
}