import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Calculator {
    String expression="";
    String result="";
    String num="";
//    String signal = "";
    SeqStack<String> OPTR = new SeqStack<>();  //运算符栈
    SeqStack<String> OPND = new SeqStack<>();  //操作数栈

    //运算优先级表
    int[][] priority = {{1,1,-1,-1,-1,1,1},
                        {1,1,-1,-1,-1,1,1},
                        {1,1,1,1,-1,1,1},
                        {1,1,1,1,-1,1,1},
                        {-1,-1,-1,-1,-1,0,-2},
                        {1,1,1,1,-2,1,1},
                        {-1,-1,-1,-1,-1,-2,0}};
    //声明UI组件并初始化
    JFrame frame = new JFrame("Calculator");
    JTextField expression_TextField = new JTextField(expression,20);
    JTextField result_TextField = new JTextField(result,20);
    JButton button_C = new JButton("C");
    JButton button_CE = new JButton("CE");
    JButton button_dengyu = new JButton("=");
    JButton button_backspace = new JButton("Backspace");
    JButton button_zuokuohao = new JButton("(");
    JButton button_youkuohao = new JButton(")");
    JButton button0 = new JButton("0");
    JButton button1 = new JButton("1");
    JButton button2 = new JButton("2");
    JButton button3 = new JButton("3");
    JButton button4 = new JButton("4");
    JButton button5 = new JButton("5");
    JButton button6 = new JButton("6");
    JButton button7 = new JButton("7");
    JButton button8 = new JButton("8");
    JButton button9 = new JButton("9");
    JButton button_dian = new JButton(".");
    JButton button_jia = new JButton("+");
    JButton button_jian = new JButton("-");
    JButton button_cheng = new JButton("×");
    JButton button_chu = new JButton("÷");

    public Calculator(){
        //栈初始化
//        char t = '0';
//        OPND.push(t+"");
        OPTR.push("#");
        //为按钮设置键盘控制
        button0.setMnemonic(KeyEvent.VK_0);
        button1.setMnemonic(KeyEvent.VK_1);
        button2.setMnemonic(KeyEvent.VK_2);
        button3.setMnemonic(KeyEvent.VK_3);
        button4.setMnemonic(KeyEvent.VK_4);
        button5.setMnemonic(KeyEvent.VK_5);
        button6.setMnemonic(KeyEvent.VK_6);
        button7.setMnemonic(KeyEvent.VK_7);
        button8.setMnemonic(KeyEvent.VK_8);
        button9.setMnemonic(KeyEvent.VK_9);

        //设置文本框为右对齐，使输入和结果都靠右显示
        result_TextField.setHorizontalAlignment(JTextField.RIGHT);
        expression_TextField.setHorizontalAlignment(JTextField.RIGHT);
        //将UI组件添加至容器内
        //三级组件
        JPanel pan_1 = new JPanel();
        pan_1.setLayout(new GridLayout(1,4,5,5));
        pan_1.add(button_dian); pan_1.add(button0); pan_1.add(button_dengyu); pan_1.add(button_jia);

        JPanel pan_2 = new JPanel();
        pan_2.setLayout(new GridLayout(1,4,5,5));
        pan_2.add(button1); pan_2.add(button2); pan_2.add(button3); pan_2.add(button_jian);

        JPanel pan_3 = new JPanel();
        pan_3.setLayout(new GridLayout(1,4,5,5));
        pan_3.add(button4); pan_3.add(button5); pan_3.add(button6); pan_3.add(button_cheng);

        JPanel pan_4 = new JPanel();
        pan_4.setLayout(new GridLayout(1,4,5,5));
        pan_4.add(button7); pan_4.add(button8); pan_4.add(button9); pan_4.add(button_chu);

        JPanel pan_5 = new JPanel();
        pan_5.setLayout(new GridLayout(1,5,5,5));
        pan_5.add(button_zuokuohao); pan_5.add(button_youkuohao); pan_5.add(button_CE);
        pan_5.add(button_C); pan_5.add(button_backspace);

        //二级组件
        JPanel pan_center = new JPanel();
        pan_center.setLayout(new GridLayout(5,1));
        pan_center.add(pan_5); pan_center.add(pan_4); pan_center.add(pan_3);
        pan_center.add(pan_2); pan_center.add(pan_1);

        JPanel pan_north = new JPanel();
        pan_north.setLayout(new BorderLayout());
        pan_north.add(result_TextField,BorderLayout.SOUTH);
        pan_north.add(expression_TextField,BorderLayout.NORTH);
        //一级组件
        frame.setLocation(300,200);
        frame.setResizable(false);
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(pan_north,BorderLayout.NORTH);
        frame.getContentPane().add(pan_center,BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);

        //事件处理程序
        //数字键
        class Listener_num implements ActionListener{
            public void actionPerformed(ActionEvent e){
                String ss = ((JButton) e.getSource()).getText();
//            if (num.length() == 1 && num == "0") num="";        //初始状态0删除
                //判断出现.0情况
                if (isIntegerOrDouble(num) == 0){
                    int len = num.length();
                    String cmp = num.substring(len-2);
                    if (cmp.equals(".0"))
                        num = num.substring(0,len-1);
                }
                num = num + ss;
                result_TextField.setText(num);
            }
        }
        //运算符键
        class Listener_signal implements ActionListener{
            public void actionPerformed(ActionEvent e){
                String ss = ((JButton) e.getSource()).getText();
                String a;       //两个操作数
                String b;

                //遇到操作符判断是不是有数字，有的话就把以前的数字入栈，并恢复为初始状态
                if (ss.equals("(")){
                    expression = expression + ss;
                    expression_TextField.setText(expression);
                }else
                    if (!num.equals("")){
                        OPND.push(num);
                        expression = expression + num + ss;
                        expression_TextField.setText(expression);
                    }
                num = "";

//            signal = ss;
                while (!ss.equals("")){
                    String theta;
                    switch (precede(OPTR.peek(),ss)){
                        case -1:            //栈顶元素优先级低
                            OPTR.push(ss);
                            ss = "";
                            break;
                        case 0:             //括号抵消
                            OPTR.pop();
                            ss = "";
                            break;
                        case 1:             //栈顶元素优先级高
                            theta = OPTR.pop();
                            a = OPND.pop(); b = OPND.pop();
                            String res = cal(a,b,theta);
                            OPND.push(res);
                            result_TextField.setText(res);
                            break;
                        case -2:
                            result_TextField.setText("1:Grammar error");
                            ss = "";
                            //错误处理
                            break;
                    }
                }
            }
        }
        //小数点
        class Listener_doc implements ActionListener{
            public void actionPerformed(ActionEvent e){
                if (num.equals("")) {
                    num = "0.0";
                    expression = num;
                    expression_TextField.setText(expression);
                }
                else{
                    int is = isIntegerOrDouble(num);
                    if (is == 1){
                        num = num + ".0";
                        expression = expression + ".";
                        expression_TextField.setText(expression);
                    }
                    else
                    if (is != 0) result_TextField.setText("Number error");
                }
                result_TextField.setText(num.substring(0,num.length()-1));
            }
        }
        //等于号
        class Listener_dy implements ActionListener{
            public void actionPerformed(ActionEvent e){
                expression = "";
                expression_TextField.setText(expression);
                if (!num.equals("")){
                    OPND.push(num);
                    num = "";
//                    result_TextField.setText("2:Grammar error");
                    //错误处理
                }

                while (!(OPND.isEmpty() && OPTR.peek().equals("#"))){
                    //运算无法进行下去
                    if ((!OPND.isEmpty() && OPTR.peek().equals("#")) || (OPND.isEmpty() && !OPTR.peek().equals("#"))){
                        result_TextField.setText("3:Grammar error");
                        break;
                    }
                    String ope = OPTR.pop();
                    String n1 = OPND.pop();
                    String n2 = OPND.pop();
                    String ans = cal(n1,n2,ope);
                    if (OPND.isEmpty() && OPTR.peek().equals("#")){
                        num = "";
                        result_TextField.setText(ans);
                        break;
                    }else{
                        OPND.push(ans);
//                       OPND.push(ans);
                    }
                }
            }
        }

        class Listener_C implements ActionListener{
            public void actionPerformed(ActionEvent e){
                //显示清空
                num = "";
                result = "";
                result_TextField.setText(result);
                expression = "";
                expression_TextField.setText(expression);
                //栈清空
                while (!OPND.isEmpty()) OPND.pop();
                while (!OPTR.peek().equals("#")) OPTR.pop();
            }
        }

        class Listener_CE implements ActionListener{
            public void actionPerformed(ActionEvent e){
                num = "";
                result_TextField.setText(num);
            }
        }

        class Listener_backspace implements ActionListener{
            public void actionPerformed(ActionEvent e){
                if (!num.equals("")){
                    num = num.substring(0,num.length()-1);
                    result_TextField.setText(num);
                }
            }
        }
        //注册监听器，即绑定事件响应逻辑到各个UI组件上
        //监听等于键
        Listener_dy jt_dy = new Listener_dy();
        //监听运算符键
        Listener_signal jt_signal = new Listener_signal();
        //监听数字键
        Listener_num jt_num = new Listener_num();
        //监听小数点键
        Listener_doc jt_doc = new Listener_doc();
        //监听C键
        Listener_C jt_C = new Listener_C();
        //监听CE键
        Listener_CE jt_CE = new Listener_CE();
        //监听backspace键
        Listener_backspace jt_backspace = new Listener_backspace();

        //绑定监听器
        button7.addActionListener(jt_num);
        button8.addActionListener(jt_num);
        button9.addActionListener(jt_num);
        button_chu.addActionListener(jt_signal);
        button4.addActionListener(jt_num);
        button5.addActionListener(jt_num);
        button6.addActionListener(jt_num);
        button_cheng.addActionListener(jt_signal);
        button1.addActionListener(jt_num);
        button2.addActionListener(jt_num);
        button3.addActionListener(jt_num);
        button_jian.addActionListener(jt_signal);
        button_dian.addActionListener(jt_doc);
        button0.addActionListener(jt_num);
        button_dengyu.addActionListener(jt_dy);
        button_jia.addActionListener(jt_signal);
        button_C.addActionListener(jt_C);
        button_CE.addActionListener(jt_CE);
        button_backspace.addActionListener(jt_backspace);
        button_zuokuohao.addActionListener(jt_signal);
        button_youkuohao.addActionListener(jt_signal);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    //判断整数(1)or小数(0)错误(-1)
    public static int isIntegerOrDouble(String a){
        Matcher mer = Pattern.compile("^[+-]?[0-9]+$").matcher(a);
        if (mer.find() == true) return 1;
        else{
            mer = Pattern.compile("^(-?\\d+)(\\.\\d+)?$").matcher(a);
            if (mer.find() == true) return 0;
            else
                return -1;
        }

    }
    //返回优先级
    public int precede(String a,String b){
        int x=-1,y=-1;
        switch (a){
            case "+":
                x = 0;
                break;
            case "-":
                x = 1;
                break;
            case "×":
                x = 2;
                break;
            case "÷":
                x = 3;
                break;
            case "(":
                x = 4;
                break;
            case ")":
                x = 5;
                break;
            case "#":
                x = 6;
                break;
        }
        switch (b){
            case "+":
                y = 0;
                break;
            case "-":
                y = 1;
                break;
            case "×":
                y = 2;
                break;
            case "÷":
                y = 3;
                break;
            case "(":
                y = 4;
                break;
            case ")":
                y = 5;
                break;
            case "#":
                y = 6;
                break;
        }
        if (x == -1 && y == -1) return -3;
        else
            return priority[x][y];
    }
    //计算逻辑
    public String cal(String x,String y,String signal){
        double a = Double.valueOf(x.toString());
        double b = Double.valueOf(y.toString());
        double res = 0;

        boolean flag = false;
        if (signal.equals("")){
            result_TextField.setText("Please input operator");
        }else{
            if (signal.equals("+")){
                res = a + b;
                flag = true;
            }
            if (signal.equals("-")){
                res = a - b;
                flag = true;
            }
            if (signal.equals("×")){
                res = a * b;
                flag = true;
            }
            if (signal.equals("÷")){
                if (b == 0){
                    result_TextField.setText("The divisor cannot be 0");
                }else{
                    res = a / b;
                    flag = true;
                }
            }
            if (flag){
                return Double.toString(res);
            }
            else
                return "";
        }
        return "";
    }
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetallookAndFell");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Calculator calu = new Calculator();
    }
}
