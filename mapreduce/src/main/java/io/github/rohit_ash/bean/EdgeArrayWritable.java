package io.github.rohit_ash.bean;

import org.apache.hadoop.io.ArrayWritable;

public class EdgeArrayWritable extends ArrayWritable {
    public EdgeArrayWritable() {
        super(Edge.class);
    }
}
