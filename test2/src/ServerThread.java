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
	public ServerThread(Socket me,Socket others) { //���캯��
	     this.me=me; 
	     this.others=others; 
	}
	public void run() { //�߳�����
	    try{
			String line1;
			//��Socket����õ�����������������Ӧ��BufferedReader����
			DataOutputStream os2=new DataOutputStream(others.getOutputStream());
			
			try {  
				//�ļ����գ�
                is1 = new DataInputStream(me.getInputStream());  
                File f = new File("D:/temp");  
                if(!f.exists()){  
                    f.mkdir();    
                }  
                /*   
                 * �ļ��洢λ��   
                 */  
                fos = new FileOutputStream(new File(filePath));      
                inputByte = new byte[1024];     
                System.out.println("��ʼ����Client"+me.getPort()+"���ļ�...");    
                while ((length = is1.read(inputByte, 0, inputByte.length)) > 0) {  
                    fos.write(inputByte, 0, length);  
                    fos.flush();  
                System.out.println("��ɽ���Client"+me.getPort()+"���ļ���"+filePath);   
                
                
                //��Ϣ��ת�ӷ��͹�����
                    line1=is1.readUTF();
        			while(true){//������ַ���Ϊ "bye"����ֹͣѭ��
        			   System.out.println("��������յ��ͻ���"+me.getPort()+"����Ϣ:"+line1);
         			   os2.writeUTF(line1);
         			   os2.flush();//ˢ���������ʹClient2�����յ����ַ���
         			   if(line1.equals("bye")){
         				   System.out.println("Client"+me.getPort()+
         						   "���������Clinet"+others.getPort()+"�����ӶϿ�");
         				   break;}
         			   line1=is1.readUTF();
         			}//����ѭ��
        			break;
                }  
				} 
                finally {
                	fos.close();
        			os2.close(); //�ر�Socket�����
        			is1.close(); //�ر�Socket������
        			me.close();
        			others.close();
            }  
		}catch(Exception e){
			System.out.println("Error:"+e);//������ӡ������Ϣ
		}
	}
}
/** 
 * ʱ�乤���� 
 * @author admin_Hzw 
 * 
 */  
 class GetDate {  
    /** 
     * ʱ���ʽ������ 
     */  
    private static SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");  
    public static String getDate(){  
        return df.format(new Date());  
    }    
}  
