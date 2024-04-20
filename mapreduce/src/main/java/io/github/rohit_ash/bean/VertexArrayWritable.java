package io.github.rohit_ash.bean;

import org.apache.hadoop.io.ArrayWritable;

public class VertexArrayWritable extends ArrayWritable {
    public VertexArrayWritable() {
        super(Vertex.class);
    }
}
