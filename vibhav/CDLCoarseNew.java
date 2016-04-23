//Vibhav Gupta vibhavgu@buffalo.edu

package vibhav;

import vibhav.CDLList.Element;

public class CDLCoarseNew<T> extends CDLList<T>{
	//create a RW lock object
	private ReadWriteLock<T> lock = new ReadWriteLock<T>();
	
	public CDLCoarseNew(T v){
		super(v);
		
	}
	
	
	public CDLCoarseNew<T>.Cursor reader(Element from){
		CDLCoarseNew<T>.Cursor cursor = new Cursor(from);
		return cursor;
	}
	
	
	
	public class Cursor extends CDLList<T>.Cursor{
	
		
		public Cursor(Element e){
			super(e);
			
		}
		
		public void previous(){
			try{
				lock.lockRead();
				super.previous();
				lock.unlockRead();
			}
			
			catch(InterruptedException e){
				lock.unlockRead();
			}
		}
		
		public void next(){
			try{
				lock.lockRead();
				super.next();
				lock.unlockRead();
			}
			
			catch(InterruptedException e){
				lock.unlockRead();
			}
		}
		
		public void traverse(){
			try{
				lock.lockRead();
				Element t=current;
				System.out.println(current.value);
				
				next();
				while(current!=t){
					System.out.println(current.value);
					next();
				}
				lock.unlockRead();
			}
			catch(Exception e){
				lock.unlockRead();
			}
		}
		
		public Writer writer(){
			Writer w=new Writer(this);
			return w;
		}
		
		
	}
	
	public class Writer extends CDLList<T>.Writer{
		
		public Writer(Cursor c){super(c);}
		
		public boolean insertBefore(T val){
			try{
				lock.lockWrite();
				super.insertBefore(val);
		
				lock.unlockWrite();
				return true;
			}
			
			catch(InterruptedException e){
				lock.unlockWrite();
				return false;
			}
		}
		
		public boolean insertAfter(T val){
			try{
				lock.lockWrite();
				super.insertAfter(val);
	
				lock.unlockWrite();
				return true;
			}
			
			catch(InterruptedException e){
				lock.unlockWrite();
				return false;
			}
		}
	}
	
	
	
	
	
}