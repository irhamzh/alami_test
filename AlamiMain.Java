import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class AlamiMain {
    public static void main(String args[]) {
        //file paths
        String beforeEodFilePath = "C:\\Users\\Pegipegi\\Documents\\random\\alami_test\\before_eod.csv";
        String afterEodFilePath = "C:\\Users\\Pegipegi\\Documents\\random\\alami_test\\after_eod.csv";

        //assign the data into variables to be used later
        List<String[]> beforeEod = getAllBeforeData(beforeEodFilePath);
        List<String[]> afterEod = getAllAfterData(afterEodFilePath);

        //call main function End Of Day
        endOfDay(beforeEod, afterEod);
    }

    public static void endOfDay(List<String[]> beforeEod, List<String[]> afterEod){

        //makeshift multithreading using thread pools
        //for this test, i use ArrayBlockingQueue with max capacity of 8
        BlockingQueue<AlamiWorker> queue = new ArrayBlockingQueue<AlamiWorker>(8);

        for (int i = 1; i<=17; i++){
            try{
                AlamiWorker worker = new AlamiWorker(i, beforeEod, afterEod);
                queue.put(worker);
                worker.start();
            }
            catch(Exception e){
                System.out.println(e);
            }

            //when thread is equal to the max capacity, check for the started threads to kill if it's done
            if (queue.size() == 8){
                while(queue.iterator().hasNext()){
                    if (!queue.iterator().next().isAlive()){
                        queue.remove();
                    }
                }
            }
        }
    }

    //query all the data from before_eod to a List of String Array format
    //so we just call it once and then process it with the workers
    public static List<String[]> getAllBeforeData(String fileDir){
        BufferedReader reader = null;
        String line = "";

        List<String[]> finData = new ArrayList<String[]>();

        try{
            reader = new BufferedReader(new FileReader(fileDir));
            reader.readLine();
            while((line = reader.readLine()) != null) {
                String[] row = line.split(";");
                finData.add(row);
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            try {
                reader.close();
            }catch (IOException e) {
                e.printStackTrace();
            }
        }

        return finData;
    }

    //query all the data from after_eod to a List of String Array format
    public static List<String[]> getAllAfterData(String fileDir){
        // BufferedReader reader = null;
        String line = "";

        List<String[]> finData = new ArrayList<String[]>();

        try{
            FileReader fr = new FileReader(fileDir);
            BufferedReader reader = new BufferedReader(fr);
            reader.readLine();
            while((line = reader.readLine()) != null) {
                String[] row = line.split(";");

                //for last column (thread 2a)
                row = Arrays.copyOf(row, row.length + 1);
                row[row.length - 1] = "";

                finData.add(row);
            }

            fr.close();
            reader.close();
        }catch(Exception e){
            e.printStackTrace();
        }

        return finData;
    }    
}