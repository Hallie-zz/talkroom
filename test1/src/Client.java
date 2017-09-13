import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
//import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class Client extends JFrame{
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//JPanel 是一般轻量级容器，
	//可以向JPanel容器中放入JPanel，JTextfiled，JButton等；
	private JPanel p= new JPanel();
	//显示一个文本输入框,可以在里面输入字符
	private JTextField jtf1 = new JTextField();
    private JTextArea jta1 = new JTextArea();
    private DataOutputStream os ;
    private DataInputStream is;
    private JButton button  = new JButton("发送");
	private Socket client;
    public Client(){
    	//设置客户端界面大小，按钮，标题栏等
    		p.setLayout(new BorderLayout());  
    		JPanel p2 = new JPanel();
    		p2.setLayout(new BorderLayout());  
    		p2.add(jtf1,BorderLayout.CENTER);
    		p2.add(button,BorderLayout.EAST);
    		p.add(p2,BorderLayout.SOUTH);
    		p.add(jta1,BorderLayout.CENTER);
    		this.setTitle("多人聊天室");
    		this.add(p);
    		this.setSize(500,300);
    		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    		this.setVisible(true);
    		button.addActionListener(new buttonListener());
   
    		try {
    			////向本机的4700端口发出客户请求
    			client = new Socket("localhost",4700);
    			//由Socket对象得到输出流
    			os = new DataOutputStream(client.getOutputStream());
    			//由Socket对象得到输入流
    			is = new DataInputStream(client.getInputStream());
    			while(true){
    				//从输入流中读取服务端发来的消息
    				//如果有消息传来则打印
    				String msg =is.readUTF();
    				if(msg!=null)
    				//Appends the given text to the end of the document. 
    				//Does nothing if the model is null or the string is null or empty.
    					jta1.append(msg+"\n");
    			}
    		} catch (UnknownHostException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    }
     private class buttonListener implements ActionListener{

    	 @Override
    	 public void actionPerformed(ActionEvent e) {
    		// String str = jtf1.getText().toString();
    		 //获取文本内容
    		 String str = jtf1.getText();
    		 try {
    			 os.writeUTF(str);
    			 os.flush();
    		 } catch (UnknownHostException e1) {
    			 // TODO Auto-generated catch block
    			 e1.printStackTrace();
    		 } catch (IOException e1) {
    			 // TODO Auto-generated catch block
    			 e1.printStackTrace();
    		 }
    	 }
     
     }
     public static void main(String[] args) {
    	 new Client();

     }

	}
