//Vibhav Gupta vibhavgu@buffalo.edu

package vibhav;

import java.util.concurrent.atomic.*;

class FineGrain<T>{

	public FineGrain(){}
	
	
	//lock for fine grain locking
	public boolean lock(CDLList<T>.Element e){
		synchronized(e){
			if(e.iflock){return false;}
			else{e.iflock=true; return true;}
		}
	}
	
	
	//release lock
 	public  void unLock(CDLList<T>.Element e){
		synchronized(e){
			assert(e.iflock==true);
			e.iflock=false;
		
		}
	
 	}

}






class ReadWriteLock<T>{
	
	//the number of readers
	private int readers;
	
	//indicates if Write Lock is previously acquired by a thread 
	private boolean WriteLocked;
	
	
	public ReadWriteLock(){
		readers=0;
		WriteLocked	= false;
	}
	
	//read lock for coarse grain lock
	public synchronized void lockRead() throws InterruptedException{
		while(WriteLocked){
			wait();
		}
		readers++;
	}
	
	// read lock for fine grain lock
	//checks on iflock value of element
	//if true, returns false, else increments read count by 1 and returns true
	public boolean lockRead(CDLList<T>.Element e){
		synchronized(e){
			
			if(e.iflock){
				return false;
			}
			
			else{
				e.count++; 
				return true;
			}
		}
	}
	
	
	//release read lock for Coarse Grain locking.
	//no of readers reduce by 1.
	//there may still be some reader threads accessing the element, 
	//so no writer thread can acquire the lock.
	//if no readers left, others are notified
	public synchronized void unlockRead(){
		readers--;
		if(readers == 0){
			notifyAll();
		}
	}
	
	
	//release read lock (fine grain locking)
	//value of count decrements by 1
	public void unlockRead(CDLList<T>.Element e){
		synchronized(e){
			e.count--;
		        
		}
	}
	
	
	//if there is already any reader(s) or a writer gained lock, than need to wait
	//else writer acquires write lock
	public synchronized void lockWrite()throws InterruptedException{
		while(readers>0 || WriteLocked){
			wait();
			
		}
		WriteLocked = true;
	}
	
	
	//write lock for fine grained locking
	//if already has a read lock or write lock, returns false
	//else acquires write lock
	public boolean lockWrite(CDLList<T>.Element e){
		synchronized(e){
			if(e.count>0||e.iflock){
				return false;
			}
			
			else{
				e.iflock = true;
				return true;
			}
		}
	}
	
	
	//writer unlock action will signal all blocking threads to wake up
	public synchronized void unlockWrite(){
		assert(WriteLocked==true);
		WriteLocked = false;
		notifyAll();
	}
	
	
	//release write lock from element (fine grain locking)
	public void unlockWrite(CDLList<T>.Element e){
		synchronized(e){
			assert(e.iflock==true);
			e.iflock = false;
		}
		
	}

}



