package vibhav;

public class NormalThread extends Thread {
    
    CDLList<String> cdl;
    int id;
    CDLList<String>.Cursor cursor;
    public NormalThread(CDLList<String> cdl, int id) {
        this.id = id;
        this.cdl = cdl;
        cursor = cdl.reader(cdl.head());
    }

    @Override
    public void run() {

        int offset = id* 2;
        for(int i = 0; i < offset; i++) {
            cursor.next();
        }
        
        
        	
        	
        		cursor.writer().insertBefore("(IB - " + id+")");
        		System.out.println("insert before finished");
        		cursor.writer().insertAfter("(IA - " + id+")");
        		System.out.println("insert after finished");
        		/*{
        			cursor.next();
        			break;
        		}*/
        		//else
        			//System.out.println("lock failed on element:"+cursor.current().val);
        	
        	//cursor.writer().insertAfter("(IA - " + id+")");
        
    }

}
