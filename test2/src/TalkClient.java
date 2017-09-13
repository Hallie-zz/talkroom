import java.io.*;
import java.net.*;

public class TalkClient{
	public static void main(String[] args) throws IOException { 
        Socket socket = null;  
		try{
			//向本机的4700端口发出客户请求
			socket=new Socket("127.0.0.1",4700); 
			//Socket socket=new Socket("127.0.0.1",4700); 
			//由系统标准输入设备构造BufferedReader对象
			BufferedReader sin=new BufferedReader(new InputStreamReader(System.in));
			//由Socket对象得到输出流，并构造PrintWriter对象
			//DataOutputStream os= new DataOutputStream(socket.getOutputStream());
			DataOutputStream os= new DataOutputStream(socket.getOutputStream());
			//由Socket对象得到输入流，并构造相应的BufferedReader对象
			DataInputStream is = new DataInputStream(socket.getInputStream());
			
			//System.out.println("If you want to send a file(.txt),please input the path.");
			//String path=sin.readLine();
			//if(!path.equals("NO")){
			//	os.writeUTF("YES");//向服务端发送YES申请发送文件
			//	os.flush();
				File file = new File( "D:/test/测试.txt"); //要传输的文件路径  D:/test/测试.txt
				FileInputStream fis = new FileInputStream(file); 
				new SendFile(socket,fis,os,file).start();
			//}
			
			new SendThread(socket,sin,os).start();
			new ReciveThread(socket,is).start();
		}catch(Exception e) {
			System.out.println("Error"+e); //出错，则打印出错信息
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
//              File file = new File("D:/test/测试.txt"); //要传输的文件路径  
    			long l = file.length();
//              fis = new FileInputStream(file);        
    			sendBytes = new byte[1024];    
    			while ((length = fis.read(sendBytes, 0, sendBytes.length)) > 0) {  
                  sumL += length;    
                  System.out.println("已传输："+((sumL/l)*100)+"%");  
                  os.write(sendBytes, 0, length);  
                  os.flush();   
              }   
              //虽然数据类型不同，但JAVA会自动转换成相同数据类型后在做比较  
              if(sumL==l){  
                  bool = true;  
              }  
          }catch (Exception e) {  
              System.out.println("客户端文件传输异常");  
              bool = false;  
              e.printStackTrace();    
          } finally{    }  
          System.out.println(bool?"文件传输成功":"文件传输失败");  
    	}
    //}
    
}
class SendThread extends Thread{//发送消息
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
					//将从系统标准输入读入的字符串输出到Server
					os.writeUTF(readline);
					os.flush();//刷新输出流，使Server马上收到该字符串
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
class ReciveThread extends Thread{//接收消息
	Socket socket=null;
	DataInputStream is=null;
	public ReciveThread(Socket s,DataInputStream is){
		socket=s;
		this.is=is;
	}
	public void run(){
		while(true){
			//从Server读入一字符串，并打印到标准输出上
			try{
				if(is.available()>0){
				System.out.println("Others:"+is.readUTF());
				}
			}catch(Exception e){
				System.out.println("Error:"+e);//出错，打印出错信息
			}
		}
	 }
}
