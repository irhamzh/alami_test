import java.util.*;
import java.io.*;

public class AlamiWorker extends Thread{
    private int threadNum;
    private List<String[]> beforeEod;
    private List<String[]> afterEod;

    public AlamiWorker(int threadNum,List<String[]> beforeEod, List<String[]> afterEod){
        this.threadNum = threadNum;
        this.beforeEod = beforeEod;
        this.afterEod = afterEod;
    }
    
    //override the main run() function of Thread
    @Override
    public void run(){

        //dividing task for threads
        //thread 5,6,7,8 for problem 1
        if (threadNum >= 5 && threadNum <= 8){
            averageBalanceProblem(beforeEod, afterEod, threadNum);
        } 
        
        else if (threadNum <= 4){

            //thread 1 and 2 for problem 2b
            if (threadNum <= 2){
                addBalanceProblem(beforeEod, afterEod, threadNum);
            }

            //thread 3 and 4 for problem 2a
            else{
                freeTransferProblem(beforeEod, afterEod, threadNum);
            }
        } 

        //thread 9-16 (8 threads) for problem 3
        else if (threadNum > 8 && threadNum < 17){
            firstOneHundredProblem(beforeEod, afterEod, threadNum);
        } 

        //last remaining thread (thread 17) to write the final processed data (afterEod) into after_eod csv file
        else if (threadNum == 17){
            writeToAfterEodFile();
        }
    }

    public void averageBalanceProblem(List<String[]> dataBeforeEod, List<String[]> dataAfterEod, int threadNum){
        for (int i = threadNum-5; i < dataBeforeEod.size(); i+= 4){
            String[] row = dataBeforeEod.get(i);
            int averageBalanced = (Integer.parseInt(row[3]) + Integer.parseInt(row[4])) / 2;

            //update data in after_eod
            dataAfterEod.get(i)[7] = averageBalanced + "";
            dataAfterEod.get(i)[8] = threadNum + "";
        }
    } 

    public void freeTransferProblem(List<String[]> dataBeforeEod, List<String[]> dataAfterEod, int threadNum){
        for (int i = threadNum-3; i < dataBeforeEod.size(); i+=2){
            int balance = Integer.parseInt(dataAfterEod.get(i)[3]);
            int freeTransfer = balance > 100 && balance < 150 ? 5 : Integer.parseInt(dataBeforeEod.get(i)[6]);
            
            //update data in after_eod
            dataAfterEod.get(i)[9] = freeTransfer + "";
            dataAfterEod.get(i)[10] = threadNum + "";
        }
    }

    public void addBalanceProblem(List<String[]> dataBeforeEod, List<String[]> dataAfterEod, int threadNum){
        for (int i = threadNum-1; i < dataBeforeEod.size(); i+=2){
            String[] row = dataBeforeEod.get(i);
            int balance = Integer.parseInt(row[3]);
            balance = balance >= 150 ? balance + 25 : balance;

            //update data in after_eod
            dataAfterEod.get(i)[3] = balance + "";
            dataAfterEod.get(i)[4] = threadNum + "";
        }
    }

    public void firstOneHundredProblem(List<String[]> dataBeforeEod, List<String[]> dataAfterEod, int threadNum){
        for (int i = threadNum-9; i < dataBeforeEod.size()/2; i+=8){
            String[] row = dataBeforeEod.get(i);
            int balance = Integer.parseInt(row[3]);
            balance += 10;

            //update data in after_eod
            dataAfterEod.get(i)[3] = balance + "";
            dataAfterEod.get(i)[5] = threadNum + "";
        }
    }

    //final function to write the processed data into the after_eod file, to prevent overlap
    public void writeToAfterEodFile(){
        ArrayList<String> lines = new ArrayList<String>();
        String afterEodFile = "C:\\Users\\Pegipegi\\Documents\\random\\alami_test\\after_eod.csv";
        
        try {
            FileReader fr = new FileReader(afterEodFile);
            BufferedReader br = new BufferedReader(fr);
            lines.add(br.readLine());
            fr.close();
            br.close();

            for (String[] row : afterEod){
                String line = "";
                for (String r : row){
                    line += r + ";";
                }
                lines.add(line);
            }

            FileWriter fw = new FileWriter(afterEodFile);
            BufferedWriter out = new BufferedWriter(fw);
            for(String row : lines)
                 out.write(row+ "\n");
            out.flush();
            out.close();

        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}
