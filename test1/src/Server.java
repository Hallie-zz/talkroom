import java.awt.BorderLayout;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JFrame;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;


public class Server extends JFrame {
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static JTextArea jta = new JTextArea();
	private ServerSocket server;
	private static List<Socket> list = new ArrayList<Socket>();
    private ExecutorService exec;//线程池：启动、调度、管理线程的大量API
    public Server(){
    	//设置聊天室窗口按钮及大小：
    	setLayout(new BorderLayout());
    	add(new JScrollPane(jta),BorderLayout.CENTER);
    	setTitle("聊天室服务器");
    	setSize(500,300);
    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	setVisible(true);
    	try{
    		server = new ServerSocket(4700);
    		//public static ExecutorService newCachedThreadPool()
    		//创建一个可根据需要创建新线程的线程池，但是在以前构造的线程可用时将重用它们
    		exec = Executors.newCachedThreadPool();
 
    		jta.append("服务器已经启动\n");
    		Socket client = null;
    		while(true){
    			//在死循环里不停调用accept()方法，阻塞，直到接收到一个客户端继续向下运行
    			client =server.accept();
    			//接收到一个客户后，将其返回的socket套接字存储到列表里
    			list.add(client);
    			//ExecutorService继承自Executor接口
    			//Executor接口只有一个方法execute，相当于启动线程
    			exec.execute(new Task(client));
    		}
    	}catch(Exception e){}
    }
    static class Task implements Runnable{
		private Socket client;
        private DataInputStream is;
        private DataOutputStream os;
        String msg;
        public Task(Socket client) throws IOException{
        	this.client = client;
        	//从socket套接字中构造输入流
        	is= new DataInputStream(client.getInputStream());
        }
    public void run() {
       	try{//当服务器从与客户相连的输入流中读取到消息，则开始循环
        	while((msg=is.readUTF())!=null){
        		msg = "["+client.getPort()+"]说："+msg;
        		jta.append(msg+"\n");
        		sendMessage();
        	}
       	}catch(Exception e){
       	}
       }
     public void sendMessage() throws IOException{
    	 //for循环列表中的每一个客户
        	for(Socket client:list){
        		os= new DataOutputStream(client.getOutputStream());
        		os.writeUTF(msg);
        	}
        }
    }
    public static void main(String[] args){
    	new Server();
    }

}
