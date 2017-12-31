public interface Stack<T>{
    //判断栈是否为空
    boolean isEmpty();
    //data入栈
    void push(T data);
    //返回栈顶元素
    T peek();
    //出栈并返回该元素
    T pop();
}
