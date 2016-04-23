package vibhav;

import java.util.concurrent.atomic.AtomicInteger;

public class CDLListRW {
	private AtomicInteger atomic=new AtomicInteger(1);
	Object obj=new Object();
       
        CDLListRW lockRead() throws InterruptedException{
        	int val;
        	while(true){
        		while(atomic.get()<1){
        			wait();
				}
        	
        		synchronized(obj){	
        			val=atomic.get();
        			if(val>=1){
        				atomic.compareAndSet(val, val+1);	
        				break;
        			}
        		}
        	
        	}
		
			//System.out.println("read");
			return this;
	    
        }
       
        
        void unlockRead(){ 	
            while(true){   	
        	
            	synchronized(obj){
        			if(atomic.get()<-1){
        				atomic.compareAndSet(atomic.get(), atomic.get()+1);
        				notifyAll();
        				break;
        			}
            
        			else if(atomic.get()>1){
						atomic.compareAndSet(atomic.get(), atomic.get()-1);
						notifyAll();
						break;
					}
				}
            }
        }
       
        
        CDLListRW lockWrite() throws InterruptedException{
        	
        	while(true){
        		
        		while(atomic.get()!=-1){
					atomic.set(Math.abs(atomic.get())*(-1));
					wait();
        		}
        		
        		synchronized(obj){
        			if(atomic.get()==-1){
        				atomic.compareAndSet(-1, 0);
        				break;
        			}
        		}				
        		
        	}
        	
			return this;
        }
   
        
        void unlockWrite() throws InterruptedException{
        	while(true){
        		
        		synchronized(obj){
        			if(atomic.get()==0){
        				atomic.compareAndSet(0, 1);
        				notifyAll();
        				break;
        			}
        		}
			
        	}
        } 
        
        
 

}