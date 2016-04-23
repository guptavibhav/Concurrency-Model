//Vibhav Gupta vibhavgu@buffalo.edu

package vibhav;

import vibhav.CDLList.Element;

public class CDLListFineNew<T> extends CDLList<T> {
	
	private ReadWriteLock<T> lock = new ReadWriteLock<T>();
	
	public CDLListFineNew(T v) {
		super(v);
	}
	
	
	
	
	public CDLListFineNew<T>.Cursor reader(Element from){
		CDLListFineNew<T>.Cursor cursor = new Cursor(from);
		return cursor;
	
	}
	
	
	
	public class Cursor extends CDLList<T>.Cursor{
	
		
		public Cursor(Element e){
			super(e);
			
		}
		
		public void previous(){
			try{
				if(lock.lockRead(current)){
					if(lock.lockRead(current.previous)){
						super.previous();
						lock.unlockRead(current.previous);
					}
					lock.unlockRead(current);
				}
			}
			
			catch(Exception e){
				e.printStackTrace();
			}
		}
		
		public void next(){
			try{
				if(lock.lockRead(current)){
					if(lock.lockRead(current.next)){
						super.next();
						lock.unlockRead(current.next);
					}
					lock.unlockRead(current);
				}
			}
			
			catch(Exception e){
				e.printStackTrace();
			}
		}
		
		public void traverse(Element ele){
			//try{
				System.out.println(ele.value);
				Element t;
				//lock.lockRead();
				t=ele.next;
				//lock.unlockRead();
				
				while(t!=ele){
					System.out.println(t.value);
					//lock.lockRead();
					t=t.next;
					//lock.unlockRead();
					
				}
			//}
			//catch(Exception e){}
		}
		
		
		public Writer writer(){
			Writer w=new Writer(this);
			return w;
			
		}
		
		
	}
	
	
	
	
	public class Writer extends CDLList<T>.Writer{

		public Writer(Cursor c) {
			super(c);
		}
		
		
		public boolean insertBefore(T val){
			
		try{
			assert(getCursor().current()!=null);
			if(lock.lockWrite(getCursor().current())){
				
				assert(getCursor().current()!=null);
				if(getCursor().current()==getCursor().current().previous){
					
					super.insertBefore(val);
					lock.unlockWrite(getCursor().current());
					
					return true;
				}
				
				else{
				    
					if(lock.lockWrite(getCursor().current().previous)){
							
						super.insertBefore(val);
				     
						lock.unlockWrite(getCursor().current());
						lock.unlockWrite(getCursor().current().previous.previous);  
					}
					
					else{
						lock.unlockWrite(getCursor().current());
						return false;
					}
					
					return true;
				}
			
			}
				
			else{
				     
				return false;	    
			}
			
		}
		
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
			
		}
		
		
		public boolean insertAfter(T val){
			
		try{
			assert(getCursor().current()!=null);
			if(lock.lockWrite(getCursor().current())){
				
				assert(getCursor().current().next!=null);
				if(getCursor().current()==getCursor().current().next){
					
					super.insertAfter(val);
					lock.unlockWrite(getCursor().current());
					
					return true;
				}
				
				else{
				    
					if(lock.lockWrite(getCursor().current().next)){
						
						super.insertAfter(val);
				     
						lock.unlockWrite(getCursor().current().next.next);
						lock.unlockWrite(getCursor().current());             
					}
					
					else{
						lock.unlockWrite(getCursor().current());
						return false;
					}
					
					return true;
				}
			
			}
				
			else{
				     
				return false;	    
			}
			
		}
		
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	
	}
		
	}


}

