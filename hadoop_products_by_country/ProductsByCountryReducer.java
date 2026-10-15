import java.io.IOException;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Reducer;

public class ProductsByCountryReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
    public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
        int totalQuantity = 0;
        for(IntWritable v : values){
            totalQuantity += v.get();
        }
        context.write(key, new IntWritable(totalQuantity));
    }
}
