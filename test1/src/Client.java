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
	//JPanel ��һ��������������
	//������JPanel�����з���JPanel��JTextfiled��JButton�ȣ�
	private JPanel p= new JPanel();
	//��ʾһ���ı������,���������������ַ�
	private JTextField jtf1 = new JTextField();
    private JTextArea jta1 = new JTextArea();
    private DataOutputStream os ;
    private DataInputStream is;
    private JButton button  = new JButton("����");
	private Socket client;
    public Client(){
    	//���ÿͻ��˽����С����ť����������
    		p.setLayout(new BorderLayout());  
    		JPanel p2 = new JPanel();
    		p2.setLayout(new BorderLayout());  
    		p2.add(jtf1,BorderLayout.CENTER);
    		p2.add(button,BorderLayout.EAST);
    		p.add(p2,BorderLayout.SOUTH);
    		p.add(jta1,BorderLayout.CENTER);
    		this.setTitle("����������");
    		this.add(p);
    		this.setSize(500,300);
    		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    		this.setVisible(true);
    		button.addActionListener(new buttonListener());
   
    		try {
    			////�򱾻���4700�˿ڷ����ͻ�����
    			client = new Socket("localhost",4700);
    			//��Socket����õ������
    			os = new DataOutputStream(client.getOutputStream());
    			//��Socket����õ�������
    			is = new DataInputStream(client.getInputStream());
    			while(true){
    				//���������ж�ȡ����˷�������Ϣ
    				//�������Ϣ�������ӡ
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
    		 //��ȡ�ı�����
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
