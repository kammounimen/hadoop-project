import java.io.IOException;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Mapper;

public class TotalRevenueMapper extends Mapper<LongWritable, Text, Text, DoubleWritable> {
    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

        String[] fields = value.toString().split(",");

        // Ignorer l'en-tête
        if(fields[1].equals("StockCode") || fields.length < 10) return;

        try {
            int quantity = Integer.parseInt(fields[3]);
            double price = Double.parseDouble(fields[5]);
            double discount = fields[8].isEmpty() ? 0 : Double.parseDouble(fields[8]);

            double total = quantity * price * (1 - discount);

            context.write(new Text("Total"), new DoubleWritable(total));

        } catch(Exception e) {}
    }
}
