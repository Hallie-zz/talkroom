import java.io.*;
import java.net.*;

public class TalkClient{
	public static void main(String[] args) throws IOException { 
        Socket socket = null;  
		try{
			//�򱾻���4700�˿ڷ����ͻ�����
			socket=new Socket("127.0.0.1",4700); 
			//Socket socket=new Socket("127.0.0.1",4700); 
			//��ϵͳ��׼�����豸����BufferedReader����
			BufferedReader sin=new BufferedReader(new InputStreamReader(System.in));
			//��Socket����õ��������������PrintWriter����
			//DataOutputStream os= new DataOutputStream(socket.getOutputStream());
			DataOutputStream os= new DataOutputStream(socket.getOutputStream());
			//��Socket����õ�����������������Ӧ��BufferedReader����
			DataInputStream is = new DataInputStream(socket.getInputStream());
			
			//System.out.println("If you want to send a file(.txt),please input the path.");
			//String path=sin.readLine();
			//if(!path.equals("NO")){
			//	os.writeUTF("YES");//�����˷���YES���뷢���ļ�
			//	os.flush();
				File file = new File( "D:/test/����.txt"); //Ҫ������ļ�·��  D:/test/����.txt
				FileInputStream fis = new FileInputStream(file); 
				new SendFile(socket,fis,os,file).start();
			//}
			
			new SendThread(socket,sin,os).start();
			new ReciveThread(socket,is).start();
		}catch(Exception e) {
			System.out.println("Error"+e); //�������ӡ������Ϣ
	    }
	}
}

class SendFile extends Thread{
	int length = 0;  
    double sumL = 0 ;  
    byte[] sendBytes = null;  
    Socket socket = null;  
    DataOutputStream os = null;  
    FileInputStream fis = null;  
    boolean bool = false;  
    File file=null;
    public SendFile(Socket s,FileInputStream fis,DataOutputStream os,File file){
    	socket=s;
    	this.os=os;
    	this.fis=fis;
    	this.file=file;
    }
    public void run(){
    	//while(true){
    		try {  
//              File file = new File("D:/test/����.txt"); //Ҫ������ļ�·��  
    			long l = file.length();
//              fis = new FileInputStream(file);        
    			sendBytes = new byte[1024];    
    			while ((length = fis.read(sendBytes, 0, sendBytes.length)) > 0) {  
                  sumL += length;    
                  System.out.println("�Ѵ��䣺"+((sumL/l)*100)+"%");  
                  os.write(sendBytes, 0, length);  
                  os.flush();   
              }   
              //��Ȼ�������Ͳ�ͬ����JAVA���Զ�ת������ͬ�������ͺ������Ƚ�  
              if(sumL==l){  
                  bool = true;  
              }  
          }catch (Exception e) {  
              System.out.println("�ͻ����ļ������쳣");  
              bool = false;  
              e.printStackTrace();    
          } finally{    }  
          System.out.println(bool?"�ļ�����ɹ�":"�ļ�����ʧ��");  
    	}
    //}
    
}
class SendThread extends Thread{//������Ϣ
	Socket socket=null;
	DataOutputStream os=null;
	String readline=null;
	BufferedReader sin=null;
	public SendThread(Socket s,BufferedReader sin,DataOutputStream os) throws IOException{
		socket=s;
		this.os=os;
		this.sin=sin;
	}
	public void run(){
		while(true){
			try {
				if(sin.ready()){
					readline=sin.readLine();
					System.out.println("Me:"+readline);
					//����ϵͳ��׼���������ַ��������Server
					os.writeUTF(readline);
					os.flush();//ˢ���������ʹServer�����յ����ַ���
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
class ReciveThread extends Thread{//������Ϣ
	Socket socket=null;
	DataInputStream is=null;
	public ReciveThread(Socket s,DataInputStream is){
		socket=s;
		this.is=is;
	}
	public void run(){
		while(true){
			//��Server����һ�ַ���������ӡ����׼�����
			try{
				if(is.available()>0){
				System.out.println("Others:"+is.readUTF());
				}
			}catch(Exception e){
				System.out.println("Error:"+e);//������ӡ������Ϣ
			}
		}
	 }
}
