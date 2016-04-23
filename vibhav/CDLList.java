package vibhav;

public class CDLList<T> {
    
     Element head;
    
    public CDLList(T v) {
        head = new Element(v);
    }
    
    public class Element {
         T value;
         boolean deleted;
         Element next;
         Element previous;
         CDLListRW rwlock;
         boolean iflock;
         int count;
         
        public Element(T v) {
            this.value = v;
            this.deleted = true;
            this.next = null;
            this.previous = null;
            rwlock = new CDLListRW();
            this.iflock=false;
            this.count=0;
        }
        
        public T value() {
            return this.value;
        }
        
        public Element getPrevious() {
            return previous;
        }
        public Element getNext() {
            return next;
        }
        public boolean getValid() {
            return deleted;
        }
        public void setNext(Element e) {
            this.next = e;
        }
        public void setPrevious(Element e) {
            this.previous = e;
        }
        public void setValid(boolean b) {
            this.deleted = b;
        }
        
    }
    
    public Element head() {
        return head;
    }
    
    public Cursor reader(Element from) {
        return new Cursor(from);
    }
    
    public class Cursor {
        
         Element current;
        
        public Cursor(Element from) {
            current = from;
        }
        public Element current() {
            return current;
        }
        public void previous() {
            current = current.previous;
        }
        public void next() {
            current = current.next;
        }
        public Writer writer() {
            return new Writer(this);
        }
        public void traverse(){
        	System.out.println(current.value);
        	next();
        }
    }
    
    public class Writer {
        
         Cursor cursor;
        
        public Writer(Cursor c) {
            cursor = c;
        }
        public Writer(Element e) {
        	this(reader(e));
        }
        
        public Cursor getCursor(){
        	return cursor;
        }
        
        public boolean delete() {
            if(cursor.current.deleted) {
                // INSERT CODE HERE!
                return true;
            }
            return false;
        }
        
        public boolean insertBefore(T val) {
            if(cursor.current.deleted) {
                Element newElement = new Element(val);
                newElement.next = cursor.current;
                newElement.previous = cursor.current.previous;
                cursor.current.previous.next = newElement;
                cursor.current.previous = newElement;
                return true;
            }
            return false;
        }
        public boolean insertAfter(T val) {
            if(cursor.current.deleted) {
                Element newElement = new Element(val);
                newElement.previous = cursor.current;
                newElement.next = cursor.current.next;
                cursor.current.next.previous = newElement;
                cursor.current.next = newElement;
                return true;
            }
            return false;
        }
    }
}