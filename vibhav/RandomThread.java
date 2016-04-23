
package vibhav;
public class RandomThread extends Thread {

    CDLList<String> cdl;
    CDLList<String>.Cursor cursor;
    public RandomThread(CDLList<String> cdl) {
        this.cdl = cdl;
    }
    
    public void run() {
        cursor = cdl.reader(cdl.head());
        for(int i = 0;i < 10;i++) {
            double temp = java.lang.Math.random();
            int rand = (int)(temp*10)%4;


            switch(rand) {
            case 0:
                cursor.next();// Go to the next 
                System.out.println("cursor on next node");
                break;
            case 1:
                cursor.previous();
                System.out.println("cursor on previous node");
                break;    
            case 2:
                cursor.writer().insertBefore("Random-Before");
                System.out.println("random inserted before");
                break;
            case 3:
                cursor.writer().insertAfter("Random-After");
                System.out.println("random inserted after");
                break;
            default:
                break;
            }
            yield();
        }
    }
}
