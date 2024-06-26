package io.github.rohit_ash;

import io.github.rohit_ash.bean.Graph;
import io.github.rohit_ash.bean.TextArrayWritable;
import io.github.rohit_ash.mapper.vf2.VF2Mapper;
import io.github.rohit_ash.reducer.IdentityReducer;
import io.github.rohit_ash.util.ArgsParser;
import io.github.rohit_ash.util.SourceGraphFileInputFormat;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class VF2Driver {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException, URISyntaxException {
        Configuration conf = new Configuration();

        ArgsParser parser = ArgsParser.parse(args);

        Path outputPath = new Path(parser.getOutputFolderPath());
        FileSystem fs = FileSystem.get(conf);
        if (fs.exists(outputPath)) {
            fs.delete(outputPath, true);
            System.out.println("Output file exists, but it has deleted.");
        }

        Job job = Job.getInstance(conf, "VF2");
        job.addCacheFile(new URI(parser.getTargetGraphFilePath()));
        job.setJarByClass(VF2Driver.class);
        job.setInputFormatClass(SourceGraphFileInputFormat.class);
        FileInputFormat.setInputPaths(job, parser.getSourceGraphFilePath());

        job.setMapperClass(VF2Mapper.class);
        job.setMapOutputKeyClass(Graph.class);
        job.setMapOutputValueClass(MapWritable.class);

        job.setNumReduceTasks(1);

        job.setReducerClass(IdentityReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(TextArrayWritable.class);

        FileOutputFormat.setOutputPath(job, outputPath);

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
