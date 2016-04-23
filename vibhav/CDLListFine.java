//Vibhav Gupta vibhavgu@buffalo.edu

package vibhav;

import vibhav.CDLCoarse.Cursor;
import vibhav.CDLList.Element;

public class CDLListFine<T> extends CDLList<T>{
	
	FineGrain<T> fine= new FineGrain<T>();
	
	public CDLListFine(T v) {
		super(v);
		
		// TODO Auto-generated constructor stub
	}
	
	public CDLListFine<T>.Cursor reader(Element from){
		CDLListFine<T>.Cursor cursor = new Cursor(from);
		return cursor;
	}
	
	public class Cursor extends CDLList<T>.Cursor{
		
		public Cursor(CDLList<T>.Element from) {
			super(from);
			// TODO Auto-generated constructor stub
		}

		public void traverse(Element ele){
			try{
				System.out.println(ele.value);
				Element t;
				//synchronized(lock){
					t=ele.next;
				//}
				
				while(t!=ele){
					System.out.println(t.value);
					//synchronized(lock){
						t=t.next;
					//}
				}
			}
			catch(Exception e){}
		}
	}
	
	public class Writer extends CDLList<T>.Writer{
		
		public Writer(Cursor curr)
		{
			super(curr);
		}

		
		// Add before the current element.
		public boolean insertBefore(T val){//�
			try{
			assert(getCursor().current().previous!=null);
			if(fine.lock(getCursor().current().previous)){
				
				assert(getCursor().current()!=null);
				if(getCursor().current().previous==getCursor().current()){
					super.insertBefore(val);
					fine.unLock(getCursor().current().previous);
					
					return true;
				}
				
				else{
				    
					if(fine.lock(getCursor().current())){
						
						super.insertBefore(val);
				     
						fine.unLock(getCursor().current());
						fine.unLock(getCursor().current().previous.previous);
					}
					
					else{
						fine.unLock(getCursor().current().previous);
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
				//continue;
			}
			return false;
		}
			
			
			
	
		// Add after the current element.					
		
		public boolean insertAfter(T val) {//�
			try{
			assert(getCursor().current()!=null);
			if(fine.lock(getCursor().current())){
				
				assert(getCursor().current().next!=null);
				if(getCursor().current()==getCursor().current().next){
					super.insertAfter(val);
					fine.unLock(getCursor().current());
					
					return true;
				}
				
				else{
				    
					if(fine.lock(getCursor().current().next)){
						
						super.insertAfter(val);
				     
						fine.unLock(getCursor().current().next.next);
						fine.unLock(getCursor().current());
					}
					
					else{
						fine.unLock(getCursor().current());
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
			//continue;
		}
		return false;
	}
	
	
	}
}