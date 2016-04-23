//Vibhav Gupta vibhavgu@buffalo.edu

package vibhav;

import vibhav.CDLList.Cursor;
import vibhav.CDLList.Element;


public class CDLCoarse<T> extends CDLList<T> {

	Object lock=new Object();
	
	public CDLCoarse(T v)
	{
		super(v);
	}
	
	
	public Cursor reader(Element from) {//�
		Cursor c=new Cursor(from);
		return c;
	}
	
	
	public class Cursor extends CDLList<T>.Cursor{
		

		public Cursor(Element c)
		{
			super(c);
		}
		
		// Return the current element.
		public Element current() {//�
			
			synchronized(lock){
				return current;
			}

		}
		
		
		// Move to the previous element.
		public void previous() {//�
			synchronized(lock){
				current= current.previous;
			}
			
		}
		
		// Move to the next element
		public void next() {//�
			synchronized(lock){
			assert(current!=null);
			current= current.next;
			}
		}
		
		public void traverse(){
			try{
				synchronized(lock){
					Element t=current;
				
					System.out.println(current.value);
					next();
					while(current!=t){
						System.out.println(current.value);
						
						next();
					}
				}	
			}
			catch(Exception e){}
		}
		
		// Returns a writer at the current element
		public Writer writer() {//�


			Writer w=new Writer(this);
			return w;
		}
	}
	
	
	
	
	
	public class Writer extends CDLList<T>.Writer{
		
		public Writer(Cursor curr)
		{
			super(curr);
		}

		
		// Add before the current element.
		public boolean insertBefore(T val) {//�

			synchronized(lock){
			
				try {
				
				Element elem=new Element(val);

				Element currentElement = getCursor().current();
				Element previousElement = currentElement.previous;

				elem.previous = previousElement;
				elem.next = currentElement;

				currentElement.previous = elem;
				previousElement.next = elem;

				//System.out.println("success before "+val);
			}

			catch(Exception e) {
				//e.printStackTrace();
				return false;
			}
			//lock.unlock();
			return true;
			
			}

		}
		
		
		
		// Add after the current element.

		public boolean insertAfter(T val) {//�
			
			synchronized(lock){
			
			try{

			Element elem=new Element(val);
			
			Element currentElement = getCursor().current();
			if(currentElement==null)
				System.out.println("null pointer");
			Element nextElement = currentElement.next;

			elem.previous = currentElement;
			elem.next = nextElement;

			currentElement.next = elem;
			nextElement.previous = elem;

			//System.out.println("success after "+val);
			
			}

		catch(Exception e) {
			//e.printStackTrace();
			return false;
		}
		//lock.unlock();
		return true;
		
			}
			
		}
		
	}
	
	
	
}
