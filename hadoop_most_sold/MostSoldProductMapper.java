import java.io.IOException;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Mapper;

public class MostSoldProductMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] fields = value.toString().split(",");

        // Ignorer l'en-tête
        if(fields[1].equals("StockCode") || fields.length < 10) return;

        try {
            String stock = fields[1];
            int quantity = Integer.parseInt(fields[3]);

            context.write(new Text(stock), new IntWritable(quantity));
        } catch(Exception e) {}
    }
} 
