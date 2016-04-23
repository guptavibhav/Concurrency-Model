package vibhav;

import vibhav.CDLList.Element;

public class CDLListFineRW<T> extends CDLList<T>{

    protected Element tail; //internal use only
    
   /**
    *Createsaoneelementlistcontainingvaluev.
    *
    *@paramv
    */
    CDLListFineRW(T v){
		super(v);
		tail = new Element(v);
		head.value = v;
		tail.value = v;
		tail.deleted = false;
		head.deleted = false;
		head.previous = head.next = tail;
		tail.previous = tail.next = head;
    }

  
   /**
    *@returnheadofthecircularlist
    */
   public CDLList<T>.Element head() {
       return head;
   }
  

   /**
    *Returnacursorforsomeelementinthelist
    *@paramfromanelementinthelist
    *@returnnewcursor
    */
   public Cursor reader(CDLList<T>.Element from) {
       return new Cursor(from);
   }

   /**
    *Permitstoreadthelist.        *@param<TT>
    */
   public class Cursor extends CDLList<T>.Cursor{

       public Cursor(CDLList<T>.Element c) {
		   super(c);
       }
       public CDLList<T>.Element current() {
           synchronized(current){
               if(current.deleted)
                   return head;
               else
                   return current;
           }
       }
       public void previous() {
	   CDLListRW lock = null;
           if(current == head){
	       
			   try {
			       lock = tail.rwlock.lockRead();
			       if(lock != null) {
					 current = tail.previous;
			       }
			   } catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			   }
			   finally {
			       if(lock!=null) lock.unlockRead();
			   }
	       
           }
			else{
			    
				try {
				    lock = current.rwlock.lockRead();
					if(current.deleted)
						current=head;
					if(lock !=null)
					 current = current.previous;
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				finally {
				    if(lock!=null) lock.unlockRead();
				}
			    
			}
       }

       public void next() {
    	   CDLListRW lock = null;
           try{
        	   lock = current.rwlock.lockRead();
			   
        	   if(current.deleted){
				    current = head;
			   }
			   if(current.next == tail)
				    current = head;
			   else {
				   current = current.next;
			   }
		   } catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		   }
           finally {
        	   if(lock!=null) lock.unlockRead();
           }
       }
       
       public void traverse(){
    	   CDLListRW lock = null;
           try{
        	   lock = current.rwlock.lockRead();
        	   Element t=current;
			   System.out.println(current.value);
        	   
        	   if(current.deleted){
				    current = head;
			   }
			   if(current.next == tail)
				    current = head;
			   //lock.unlockRead();
			   
			   while(current.next!=null & current.next!=t){
				   //lock = current.rwlock.lockRead();
				   current = current.next;
				   System.out.println(current.value);
				   //lock.unlockRead();
			   }
			   
		   } catch (InterruptedException e) {
			// TODO Auto-generated catch block
			   //e.printStackTrace();
		   }
           finally {
        	   if(lock!=null) lock.unlockRead();
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
    		   e.printStackTrace();
    	   }
    	   return false;
       }
       
       public CDLList<T>.Element delete(Writer w) throws deleteException{
           if(cursor.current.deleted || cursor.current == head || cursor.current == tail){
               throw new deleteException();
           }
           while (true){
	       CDLListRW lock1=null;
	       CDLListRW lock2=null;
	       CDLListRW lock3=null;
	       try {
	    	   temp1 = cursor.current.previous;
	       lock1= cursor.current.previous.rwlock.lockWrite();
	       lock2= cursor.current.rwlock.lockWrite();
	       lock3= cursor.current.next.rwlock.lockWrite();
	       	if(temp1 == cursor.current.previous) {
                               if(cursor.current.deleted)
                                   throw new deleteException();
                               cursor.current.previous.next = cursor.current.next;
			                   cursor.current.next.previous = cursor.current.previous;
                               cursor.current.deleted = true;
                               return cursor.current;
	       	} } catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	       finally {
	    	   try {
		   if(lock1!=null) lock1.unlockWrite();
		   if(lock2!=null) lock2.unlockWrite();
		   if(lock3!=null) lock3.unlockWrite();
	    	   } catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	       }
                               return cursor.current;
	   }
       }

       public boolean insertBefore(T val) {
	       CDLList.Element temp = new CDLList.Element(val);
	       CDLList.Element temp1 = null;
	       temp.deleted = false;
	       temp.value = val;
	       CDLListRW lock1 = null;
	       CDLListRW lock2 = null;
	       int count = 0;
           if(cursor.current == head){
	           while(true){
		       try {
		    	   System.out.println(count++);
		    temp1 = tail.previous;
			   lock1 = tail.previous.rwlock.lockWrite();
			   lock2 = tail.rwlock.lockWrite();
			   if(temp1 == tail.previous) {
							   temp.previous = (CDLList.Element) tail.previous;
							   temp.next = (CDLList.Element) tail;
							   tail.previous.next = temp;
							   tail.previous = temp;
							   return true;
			   } } catch (InterruptedException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
		       finally {
		    	   try {
			   if(lock1 != null) lock1.unlockWrite();
			   if(lock2 != null) lock2.unlockWrite();
		    	   } catch (InterruptedException e) {
						// TODO Auto-generated catch block
						//e.printStackTrace();
					}
		       }
							   
						  
					   
				   }
	   }
       else{
               while(true){
		   try {
			   System.out.println(count++);
			   temp1 = cursor.current.previous;
		       lock1 = cursor.current.previous.rwlock.lockWrite();
		       lock2 = cursor.current.rwlock.lockWrite();
                      if(temp1 == cursor.current.previous) {
                           if(cursor.current.deleted)
                               return false;                           
                               temp.previous = (CDLList.Element) cursor.current.previous;
                               temp.next = (CDLList.Element) cursor.current;
                               cursor.current.previous.next = temp;
                               cursor.current.previous = temp;
                               return true;
                      }} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		  		       finally {
		  		    	   try {
			   if(lock1 != null) lock1.unlockWrite();
			   if(lock2 != null) lock2.unlockWrite();
		  		    	 } catch (InterruptedException e) {
		  					// TODO Auto-generated catch block
		  					//e.printStackTrace();
		  				}
		       }

	       }
       }
       }

       public boolean insertAfter(T val) {
		   CDLList.Element temp = new CDLList.Element(val);
		   temp.deleted = false;
		   temp.value = val;
	       CDLListRW lock1 = null;
	       CDLListRW lock2 = null;
	       try {
		       lock1 = cursor.current.rwlock.lockWrite();
		       lock2 = cursor.current.next.rwlock.lockWrite();
				   if(cursor.current.deleted)
					   return false;
				   temp.previous = (CDLList.Element) cursor.current;
				   temp.next = (CDLList.Element) cursor.current.next;
				   cursor.current.next.previous = temp;
				   cursor.current.next = temp;
			   
	       } catch (InterruptedException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
	       finally {
	    	   try {
			   if(lock1 != null) lock1.unlockWrite();
			   if(lock2 != null) lock2.unlockWrite();
	    	   } catch (InterruptedException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
	       }
	   
           return true;
       }
   }
} 