import java.io.IOException;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Mapper;

public class ProductsByCountryMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] fields = value.toString().split(",");

        // Ignorer l'en-tête
        if(fields[1].equals("StockCode") || fields.length < 10) return;

        try {
            String country = fields[7]; // Country
            int quantity = Integer.parseInt(fields[3]);

            context.write(new Text(country), new IntWritable(quantity));
        } catch(Exception e) {}
    }
}
