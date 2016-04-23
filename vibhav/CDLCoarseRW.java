package vibhav;

import vibhav.CDLList.Element;

public class CDLCoarseRW<T> extends CDLList<T>{

    protected Element tail; //internal use only
    private CDLListRW lock;

    CDLCoarseRW(T v){
		super(v);
		tail = new Element(v);
		lock = new CDLListRW();
		head.value = v;
		tail.value = v;
		tail.deleted = false;
		head.deleted = false;
		head.previous = head.next = tail;
		tail.previous = tail.next = head;
    }

   public CDLList<T>.Element head() {
       return head;
   }
  
   public Cursor reader(CDLList<T>.Element from) {
       return new Cursor(from);
   }

   public class Cursor extends CDLList<T>.Cursor{

       public Cursor(CDLList<T>.Element c) {
		   super(c);
       }
       
       public CDLList<T>.Element current() {
           try {
	       lock.lockRead();
               if(current.deleted)
                   return head;
               else
                   return current;
           } catch (InterruptedException e) {
			// TODO Auto-generated catch block
        	   e.printStackTrace();
           }
           finally {
        	   lock.unlockRead();
           }
           return null;
       }
       
       public void previous() {
	   try {
	       	lock.lockRead();
	       	if(current == head){ 	      		 
			     current = tail.previous;		     
			}
			else{				
				if(current.deleted)
					current=head;
				else
					current = current.previous;				    
			}
	   } catch (InterruptedException e) {
		// TODO Auto-generated catch block
		   //e.printStackTrace();
	   }
	   finally {
	       lock.unlockRead();
	   }
       }

       public void next() {
           try {
        	   lock.lockRead();
			   if(current.deleted){
				    current = head;
			   }
			   if(current.next == tail) {
				   current = head;
			   }				   
			   else {
				   current = current.next;
			   }
		   } catch (InterruptedException e) {
			// TODO Auto-generated catch block
			   //e.printStackTrace();
		   }
           finally {
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
			}
			catch(Exception e){}
			finally{
				lock.unlockRead();
			}
		}

       public Writer writer() {
           return new Writer(current);
       }
   }

   public class Writer extends CDLList<T>.Writer{
       volatile CDLList<T>.Element temp1;

       public Writer(CDLList<T>.Element c) {
		   super(c);
       }

       public boolean delete() {
    	   try {
	    	   CDLList<T>.Element ele=delete(this);
	    	   if(ele!=null) {
	    		   return true;
	    	   }
    	   }
    	   catch(Exception e) {
    		   //e.printStackTrace();
    	   }
    	   return false;
       }
       
       public CDLList<T>.Element delete(Writer w) throws deleteException{
           if(cursor.current.deleted || cursor.current == head || cursor.current == tail){
               throw new deleteException();
           }
           while (true){
               temp1 = cursor.current.previous;
               try {
            	   lock.lockWrite();
                       if(temp1 == cursor.current.previous){
                           
                               if(cursor.current.deleted)
                                   throw new deleteException();
                               cursor.current.previous.next = cursor.current.next;
                               cursor.current.next.previous = cursor.current.previous;                         
                               cursor.current.deleted = true;
                               return cursor.current;                                                  
                   }
               } catch (InterruptedException e) {
				// TODO Auto-generated catch block
            	   e.printStackTrace();
               }
               finally {
            	   try {
            		   lock.unlockWrite();
            	   } catch (InterruptedException e) {
			// TODO Auto-generated catch block
            		   e.printStackTrace();
            	   }
               }
           }
       }

       public boolean insertBefore(T val) {
	       CDLList.Element temp = new CDLList.Element(val);
	       temp.deleted = false;
	       temp.value = val;
	       
	       try {
	    	   lock.lockWrite();
	    	   if(cursor.current == head){
	    		   while(true){
	    			   temp = cursor.current.previous;

	    			   if(temp1 == cursor.current.previous){
	    				   temp.previous = (CDLList.Element) tail.previous;
	    				   temp.next = (CDLList.Element) tail;
						   tail.previous.next = temp;
						   tail.previous = temp;
						   return true;						   
					   }				   
	    		   }
	    	   }
	    	   else{
	    		   while(true){
	    			   temp1 = cursor.current.previous;
	    			   if(cursor.current.deleted)
	    				   return false;
                       if(temp1 == cursor.current.previous){
                    	   temp.previous = (CDLList.Element) cursor.current.previous;
                           temp.next = (CDLList.Element) cursor.current;
                           cursor.current.previous.next = temp;
                           cursor.current.previous = temp;
                           return true;                           
                       }
                   }
               }
           } 
	       catch (InterruptedException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
	       }
	       finally {
	    	   try {
	    		   lock.unlockWrite();
	    	   } 
	    	   catch (InterruptedException e) {
						// TODO Auto-generated catch block
						//e.printStackTrace();
	    	   }
	       }
	       return false;				  
       }

       public boolean insertAfter(T val) {
		   CDLList.Element temp = new CDLList.Element(val);
		   temp.deleted = false;
		   temp.value = val;
		   
		   try {
			   lock.lockWrite();
			   if(cursor.current.deleted)
				   return false;
			   
			   temp.previous = (CDLList.Element) cursor.current;
			   temp.next = (CDLList.Element) cursor.current.next;
			   cursor.current.next.previous = temp;
			   cursor.current.next = temp;			   
		   } 
		   catch (InterruptedException e) {
			// TODO Auto-generated catch block
			   //e.printStackTrace();
		   }
		   finally {
		       try {
		    	   lock.unlockWrite();
		       } 
		       catch (InterruptedException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
		       }
		   }
           return true;
       }
   }
}