import java.io.*;
import java.net.*;
import java.util.Date;  
import java.text.SimpleDateFormat;
import java.util.Random;
public class ServerThread extends Thread{
	Socket me=null;
	Socket others=null;
	byte[] inputByte = null;  
    int length = 0;  
    DataInputStream is1 = null;  
    FileOutputStream fos = null;  
    String filePath = "D:/temp/"+GetDate.getDate()+"SJ"+new Random().nextInt(10000)+".txt"; 
	public ServerThread(Socket me,Socket others) { //构造函数
	     this.me=me; 
	     this.others=others; 
	}
	public void run() { //线程主体
	    try{
			String line1;
			//由Socket对象得到输入流，并构造相应的BufferedReader对象
			DataOutputStream os2=new DataOutputStream(others.getOutputStream());
			
			try {  
				//文件接收：
                is1 = new DataInputStream(me.getInputStream());  
                File f = new File("D:/temp");  
                if(!f.exists()){  
                    f.mkdir();    
                }  
                /*   
                 * 文件存储位置   
                 */  
                fos = new FileOutputStream(new File(filePath));      
                inputByte = new byte[1024];     
                System.out.println("开始接收Client"+me.getPort()+"的文件...");    
                while ((length = is1.read(inputByte, 0, inputByte.length)) > 0) {  
                    fos.write(inputByte, 0, length);  
                    fos.flush();  
                System.out.println("完成接收Client"+me.getPort()+"的文件："+filePath);   
                
                
                //消息的转接发送工作：
                    line1=is1.readUTF();
        			while(true){//如果该字符串为 "bye"，则停止循环
        			   System.out.println("服务端已收到客户端"+me.getPort()+"的消息:"+line1);
         			   os2.writeUTF(line1);
         			   os2.flush();//刷新输出流，使Client2马上收到该字符串
         			   if(line1.equals("bye")){
         				   System.out.println("Client"+me.getPort()+
         						   "已下线其和Clinet"+others.getPort()+"的链接断开");
         				   break;}
         			   line1=is1.readUTF();
         			}//继续循环
        			break;
                }  
				} 
                finally {
                	fos.close();
        			os2.close(); //关闭Socket输出流
        			is1.close(); //关闭Socket输入流
        			me.close();
        			others.close();
            }  
		}catch(Exception e){
			System.out.println("Error:"+e);//出错，打印出错信息
		}
	}
}
/** 
 * 时间工具类 
 * @author admin_Hzw 
 * 
 */  
 class GetDate {  
    /** 
     * 时间格式到毫秒 
     */  
    private static SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");  
    public static String getDate(){  
        return df.format(new Date());  
    }    
}  
