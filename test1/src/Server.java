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
    private ExecutorService exec;//�̳߳أ����������ȡ������̵߳Ĵ���API
    public Server(){
    	//���������Ҵ��ڰ�ť����С��
    	setLayout(new BorderLayout());
    	add(new JScrollPane(jta),BorderLayout.CENTER);
    	setTitle("�����ҷ�����");
    	setSize(500,300);
    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	setVisible(true);
    	try{
    		server = new ServerSocket(4700);
    		//public static ExecutorService newCachedThreadPool()
    		//����һ���ɸ�����Ҫ�������̵߳��̳߳أ���������ǰ������߳̿���ʱ����������
    		exec = Executors.newCachedThreadPool();
 
    		jta.append("�������Ѿ�����\n");
    		Socket client = null;
    		while(true){
    			//����ѭ���ﲻͣ����accept()������������ֱ�����յ�һ���ͻ��˼�����������
    			client =server.accept();
    			//���յ�һ���ͻ��󣬽��䷵�ص�socket�׽��ִ洢���б���
    			list.add(client);
    			//ExecutorService�̳���Executor�ӿ�
    			//Executor�ӿ�ֻ��һ������execute���൱�������߳�
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
        	//��socket�׽����й���������
        	is= new DataInputStream(client.getInputStream());
        }
    public void run() {
       	try{//������������ͻ��������������ж�ȡ����Ϣ����ʼѭ��
        	while((msg=is.readUTF())!=null){
        		msg = "["+client.getPort()+"]˵��"+msg;
        		jta.append(msg+"\n");
        		sendMessage();
        	}
       	}catch(Exception e){
       	}
       }
     public void sendMessage() throws IOException{
    	 //forѭ���б��е�ÿһ���ͻ�
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
