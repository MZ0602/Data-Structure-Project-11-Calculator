
import java.util.EmptyStackException;

public class SeqStack<T> implements Stack<T> {
    private int top = -1;
    private int capacity = 10;
    private T[] array;

    private int size;

    public SeqStack(int capacity){
        array = (T[]) new Object[capacity];
    }

    public SeqStack(){
        array = (T[]) new Object[this.capacity];
    }

    public int size(){
        return size;
    }

    public boolean isEmpty(){
        return this.top==-1;
    }

    public void push(T data){
        if (array.length == size)
            ensureCapacity(size*2+1);
        top = top + 1;
        array[top]=data;
        size++;
    }

    public T peek(){
        if (isEmpty())
            new EmptyStackException();
        return array[top];
    }

    public T pop(){
        if (isEmpty())
            new EmptyStackException();
        size--;
        return array[top--];
    }

    public void ensureCapacity(int capacity){
        if (capacity < size) return;
        T[] old = array;
        array = (T[]) new Object[capacity];

        for (int i=0;i<size;i++)
            array[i]=old[i];
    }

    public static void main(String[] args){
        SeqStack<String> s=new SeqStack<>();
        s.push("A");
        s.push("B");
        s.push("C");
        System.out.println("size->"+s.size());
        int l=s.size();//size 在减少,必须先记录
        for (int i=0;i<l;i++){
            System.out.println("s.pop->"+s.pop());
        }

        System.out.println("s.peek->"+s.peek());
    }
}
/*
import java.util.EmptyStackException;

public class SeqStack<T> implements Stack<T> {


    private int top=-1;


    private int capacity=10;


    private T[] array;

    private int size;

    public SeqStack(int capacity){
        array = (T[]) new Object[capacity];
    }

    public SeqStack(){
        array= (T[]) new Object[this.capacity];
    }

    public  int size(){
        return size;
    }



    public boolean isEmpty() {
        return this.top==-1;
    }



    public void push(T data) {
        //判断容量是否充足
        if(array.length==size)
            ensureCapacity(size*2+1);//扩容

        //从栈顶添加元素
        array[++top]=data;

        size++;
    }



    public T peek() {
        if(isEmpty())
            new EmptyStackException();
        return array[top];
    }



    public T pop() {
        if(isEmpty())
            new EmptyStackException();
        size--;
        return array[top--];
    }


    public void ensureCapacity(int capacity) {
        //如果需要拓展的容量比现在数组的容量还小,则无需扩容
        if (capacity<size)
            return;

        T[] old = array;
        array = (T[]) new Object[capacity];
        //复制元素
        for (int i=0; i<size ; i++)
            array[i]=old[i];
    }

    public static void main(String[] args){
        SeqStack<String> s=new SeqStack<>();
        s.push("A");
        s.push("B");
        s.push("C");
        System.out.println("size->"+s.size());
        int l=s.size();//size 在减少,必须先记录
        for (int i=0;i<l;i++){
            System.out.println("s.pop->"+s.pop());
        }

        System.out.println("s.peek->"+s.peek());
    }
}
*/