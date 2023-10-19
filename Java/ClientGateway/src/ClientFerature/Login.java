/*
 * Login.java
 * 
 * Created on Jun 8, 2007, 10:00:11 AM
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ClientFerature;

/**
 *
 * @author montenegro
 */
import java.awt.*;
import java.awt.event.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class Login extends Dialog implements ActionListener {
 
	private static final long serialVersionUID = 1L;
	public boolean id = false;
	Button ok,can;
	JTextField username;
	JPasswordField password;

 
/*********************************************
 * 
 * @param frame
 */
public Login(Frame frame){
     
  super(frame, "Client Authentication", true);
 
  
  Dimension d = new Dimension();
  d.width=100;
  d.height=100;
  setSize(d);
          
  setLayout(null);
  username = new JTextField(15);           
  password = new JPasswordField(15);
  Label User=new Label("User:           ");
  Label Password=new Label("Password : ");
  
  User.setBounds(15,45, 80, 10);
  username.setBounds(100,40, 150, 20);
  Password.setBounds(15,75, 80, 10);
  password.setBounds(100,70, 150, 20);
  
  add(User); add(username);
  add(Password);  add(password);
  
  ok = new Button("      OK       ");
  can=  new Button("    Cancel    ");

  ok.setBounds(30, 110, 100, 30);     
  can.setBounds(150, 110, 100, 30);
     
  add(ok);  ok.addActionListener(this); 
  add(can); can.addActionListener(this);

  
  createFrame();
  
  pack();
    int width=280;
    int height=150;
    setSize(width, height);
    setResizable(false);
  
    setVisible(true);
  }

/*********************************************
 * 
 */

    	

 
 /*********************************************
  * 
  */
 
 void createFrame() {
        Dimension d = getToolkit().getScreenSize();
        setLocation(d.width/4,d.height/3);
  }

 
 /*********************************************
  * 
  * @param ae
  */
 
 public void actionPerformed(ActionEvent ae){
  if(ae.getSource() == ok) {
    id = true;
    setVisible(false);
    }
  else if(ae.getSource() == can) {
    id = false;
    setVisible(false);
    }
  }

 /*********************************************
  * 
  * 
  * @return
  */
 
 
 public boolean cancelClick (){
     return (id==false);
 }
 
 /*********************************************
  * 
  * 
  * @return
  */
 
 
 public String getPassword() { 
     String Spassword = new String(password.getPassword());
     
     if (Spassword.length()!=0) Spassword=codifPwd(Spassword);
     
     return Spassword;
 }

 
 /*********************************************
  * 
  * 
  * @return
  */
 
 public String getUserName() { 
     return username.getText();
 }

 /*********************************************
  * 
  * 
  * 
  * @param bytes
  * @return
  */
    private static String stringHexa(byte[] bytes) {
       StringBuilder s = new StringBuilder();
       for (int i = 0; i < bytes.length; i++) {
           int parteAlta = ((bytes[i] >> 4) & 0xf) << 4;
           int parteBaixa = bytes[i] & 0xf;
           if (parteAlta == 0) s.append('0');
           s.append(Integer.toHexString(parteAlta | parteBaixa));
       }
       return s.toString();
    }

  
    
    /*********************************************
     * 
     * 
     * 
     * 
     * @param pwd
     * @return
     */
  private String codifPwd(String pwd){
  
        
              try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                md.update(pwd.getBytes());
                return stringHexa (md.digest());
              } catch (NoSuchAlgorithmException e) {
                return null;
              }

  }
 

}
