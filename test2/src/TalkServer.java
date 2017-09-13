import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
//import java.util.Scanner;
public class TalkServer{
	//@SuppressWarnings("null")
	public static void main(String args[]) throws IOException {
		ServerSocket serverSocket=null;
		boolean listening=true;
		
		try{
			//����һ��ServerSocket�ڶ˿�4700�����ͻ�����
			serverSocket=new ServerSocket(4700); 			
		}catch(IOException e) {
			System.out.println("Could not listen on port:4700.");
			//������ӡ������Ϣ
			System.exit(-1); //�˳�
		}
		List <Socket> sockets = new ArrayList<Socket>();
		while(listening){ //ѭ������
		  //�������ͻ����󣬸��ݵõ���Socket����Ϳͻ��������������̣߳�������֮
			Socket s=serverSocket.accept();
			sockets.add(s);
			System.out.println("client"+s.getPort()+"������!");
				if(sockets.size()>1)
				{
					new ServerThread(sockets.get(0),sockets.get(1)).start();
					new ServerThread(sockets.get(1),sockets.get(0)).start();
					System.out.println("Now two clients connect!");
				}
		}
		serverSocket.close(); //�ر�ServerSocket
	}
}
