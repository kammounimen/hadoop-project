import java.io.IOException;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Reducer;

public class SalesByCountryReducer extends Reducer<Text, DoubleWritable, Text, DoubleWritable> {
    public void reduce(Text key, Iterable<DoubleWritable> values, Context context) throws IOException, InterruptedException {
        double totalSales = 0;
        for(DoubleWritable v : values){
            totalSales += v.get();
        }
        context.write(key, new DoubleWritable(totalSales));
    }
}
