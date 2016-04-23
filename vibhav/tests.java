//INCLUDE PACKAGENAME HERE
package vibhav;

import java.util.List;
import java.util.ArrayList;

import org.junit.Test;

public class tests {

	@Test
    public void test1(){
		
//        USE THIS FOR COARSELIST AND FINELIST WITH MODIFICATIONS
        CDLCoarseRW<String> list = new CDLCoarseRW<String>("hi");
        //CDLCoarseNew<String>.Element head = list.head();
        CDLCoarseRW<String>.Cursor c = list.reader(list.head());
        
        for(int i = 74; i >= 65; i--) {
            char val = (char) i;
            c.writer().insertAfter("" + val);
        }
        
        List<Thread> threadList = new ArrayList<Thread>();
        
        //Normal Thread
        for (int i = 0; i < 5; i++) {
            NormalThread nt = new NormalThread(list, i);
            threadList.add(nt);
        }
            
       //Random Thread
       RandomThread rt = new RandomThread(list);
       threadList.add(rt);
	
       
        try {
            for(Thread t : threadList){
            	//System.out.println("new thread started");
            	t.start();
            }
            for (Thread t : threadList) {
            	t.join();
            }
        } 
        catch(InterruptedException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        
//    YOU MAY WANT TO INCLUDE A PRINT METHOD TO VIEW ALL THE ELEMENTS
//        list.print();
        c.traverse();
        System.out.println("");
        System.out.println("Success");
    }  
    
    
    
    public static void main(String arg[]){
    	
    	tests t=new tests();
    	t.test1();
    }
    
}



