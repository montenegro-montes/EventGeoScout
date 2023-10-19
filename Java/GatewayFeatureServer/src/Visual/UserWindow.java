/*
 * CircuitWindow.java
 * 
 * Created on 03-dic-2007, 23:45:54
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package Visual;


import java.awt.Button;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import Server.*;

/**
 *
 * @author Monte
 */

/**
 *
 * @author Monte
 */

public class UserWindow extends JDialog implements ActionListener{

  
    
    JTable tableV;
    Vector rows;
  
    GeoDatabase database;
/*************************************************************
 * GENERAL CONSTRUCTOR
 * 
 * 
 * 
 * ***********************************************************/
    public UserWindow(JFrame frame,GeoDatabase databaseP){//(Mediator med,String title,int x, int y) {
    
              super(frame, "User Administration", true);
           
              database= databaseP;
              
              setLocation(10, 20);
               
              setLayout (null);
              setResizable(false);
              setPreferredSize(new Dimension(400,500));
              
              Hashtable users=database.getUsers();
        
              
              ///////////////////////////////////////////
              JButton NewUser = new JButton("New User");
              NewUser.setBounds(70, 425, 100, 25);    
              NewUser.addActionListener (this);
              add(NewUser);
              ///////////////////////////////////////////
              
              ///////////////////////////////////////////
              JButton DelUser = new JButton("Del. User");
              DelUser.setBounds(220, 425, 100, 25);    
              DelUser.addActionListener (this);
              add(DelUser);
              ///////////////////////////////////////////
              
                
                NotEditableTableModel bulModel=new NotEditableTableModel();
                String [] columnNames = {"Login Name","Password"};
                rows=new Vector();  Vector columns=new Vector(); 
                for (int j=0;j<columnNames.length;j++) columns.addElement((String) columnNames[j]);
                bulModel.setDataVector(rows, columns);
        
              tableV= new JTable(bulModel);

              final JDialog window=this;
                tableV.addMouseListener(new MouseAdapter (){
                    public void mouseClicked (MouseEvent e){
                        if (e.getClickCount()==2){
                            JTable target = (JTable) e.getSource();
                            int row = target.getSelectedRow();
  
                            Vector selectedRow = (Vector) rows.elementAt(row);
                            String ID= (String) selectedRow.elementAt(0);
                            
                            UserModif x= new UserModif(window,ID);
                             if (x.id){
                                String PWD=x.getPassword();
                                if (PWD.length()==0)    JOptionPane.showMessageDialog(null,"Please, Introduce a valid password.","Error Adding User",JOptionPane.WARNING_MESSAGE); 
                                else modifyDataTable(row,ID,PWD);
                                
                            }
                        }
                    }
                });


                tableV.getTableHeader().setReorderingAllowed(false);

                
              JLabel TableV = new JLabel ("Users:");
              
              JScrollPane scrollTableV = new JScrollPane(tableV);
              scrollTableV.setBounds(10, 15, 370, 400);
              add (scrollTableV); 
              
              
              //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	      pack();
              loadDataTable(users);
   }
    
      
  public void showWindow () {
  
    setVisible(true);
  }
  
    
    /**********************************************
   * actionPerformed:  Treat events
   * 
   * ********************************************/
  
  public void actionPerformed (ActionEvent event) {

    String cmd = event.getActionCommand ();
            
    
    if ( cmd.equals ("New User")) {
        UserModif x= new UserModif(this,null);
        if (x.id){
            String ID=x.getUserName();
            if (ID.length()==0)      JOptionPane.showMessageDialog(null,"Please, Introduce a valid user.","Error Adding User",JOptionPane.WARNING_MESSAGE); 
            else{
                    if (isInTable(ID)) JOptionPane.showMessageDialog(null,"User is already included in database.","Error Adding User",JOptionPane.WARNING_MESSAGE);
                    else {
                        String PWD=x.getPassword();
                        if (PWD.length()==0)      JOptionPane.showMessageDialog(null,"Please, Introduce a valid password.","Error Adding User",JOptionPane.WARNING_MESSAGE); 
                        else                insertNewDataTable(ID,PWD);    
                    }
            }
        }
    }
    else 
    if ( cmd.equals ("Del. User")) {
        int selected=tableV.getSelectedRow();
        int NumItems=rows.size();
        
        if (selected==-1 | selected>=NumItems ) JOptionPane.showMessageDialog(null,"Please, select first the user to delete.","Error Delete User",JOptionPane.WARNING_MESSAGE);
        else {    
                Vector selectedRow = (Vector) rows.elementAt(selected);
                String id= (String) selectedRow.elementAt(0);
                deleteDataTable(selected,id);
        }
    }
      
  }
  
  

  ///////////////////////////////////////////
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

  
  private String codifPwd(String pwd){
  
        
              try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                md.update(pwd.getBytes());
                return stringHexa (md.digest());
              } catch (NoSuchAlgorithmException e) {
                return null;
              }

  }
  ///////////////////////////////////////////

  public void loadDataTable(Hashtable USers){
    String ID;
    String PWD;
      
        
    Iterator it=USers.keySet().iterator();
    
        while (it.hasNext()){
            
              ID=(String)it.next();
              PWD=(String)USers.get(ID);
              insertDataTable(ID,PWD);
        }
  }
  ///////////////////////////////////////////

    public boolean isInTable(String ID){
        
      
       int tableSize=rows.size();
          
       for (int i=0;i<tableSize;i++){
              Vector dataE;
              dataE=(Vector)rows.get(i);
              String IDd=(String)dataE.get(0);
              if (IDd.contentEquals(ID)) return true;
       }
        return false;
  }

///////////////////////////////////////////
    
  public void modifyDataTable(int row,String ID,String PWD){
        
      String PwdC=codifPwd(PWD);
      Vector dataE;
      dataE=(Vector)rows.get(row);
      dataE.set(1, PwdC);
      
       rows.set(row, dataE);
            
        tableV.addNotify();
        tableV.repaint();
       
        database.updatePwd(ID, PwdC); 
  }
  
    
  public void deleteDataTable(int row, String Id){
        
        rows.remove(row);
            
        tableV.addNotify();
        tableV.repaint();
        database.deleteUser(Id);
        
  
  }
  
  public void insertDataTable(String ID,String PWD){
      
        
        Vector dataE=new Vector();
            
        dataE.addElement(ID);  
        dataE.addElement(PWD);
        rows.add(dataE);
            
        tableV.addNotify();
        tableV.repaint();
        
  }

  
    public void insertNewDataTable(String ID,String PWD){
      
        String PwdC=codifPwd(PWD);
        insertDataTable( ID, PwdC);
        database.insertUser(ID, PwdC);
  }
    
  
  
      private class NotEditableTableModel extends DefaultTableModel{
      public boolean isCellEditable(int row, int column){
          return false;
      }
    }

      
private class UserModif extends Dialog implements ActionListener {
 boolean id = false;
 Button ok,can;
 JLabel username;
 JPasswordField password;
 JTextField username2;
 

 UserModif(JDialog dialog, String user){
     
   
  super(dialog, "New User ", true);
 
  if (user!=null) this.setTitle("Change Password of User "+user);
  
  Dimension d = new Dimension();
  d.width=100;
  d.height=100;
  setSize(d);
          
  setLayout(null);//new FlowLayout(FlowLayout.CENTER));
  username = new JLabel(user);
  username2 = new JTextField(15);
  password = new JPasswordField(15);
  Label User=new Label("User:           ");
  Label Password=new Label("Password : ");
  
  User.setBounds(15,45, 135, 10);
  
  if (user!=null) {
    username.setBounds(100,40, 150, 20);
    add(username);
  }
  else {
    username2.setBounds(100,40, 150, 20);
    add(username2);  
  }
          
  
  Password.setBounds(15,75, 80, 10);
  password.setBounds(100,70, 150, 20);
  
  add(User);
  add(Password);
  add(password);
  
  addOKCancelPanel();
  createFrame();
  
  pack();
    int width=280;
    int height=150;
    setSize(width, height);
    setResizable(false);
  
  setVisible(true);
  }
      
 
 void addOKCancelPanel() {
  Panel p = new Panel();
  p.setLayout(new FlowLayout());
  createButtons( p );
  add( p );
  p.setBounds(50,100, 190, 40);
  }

 void createButtons(Panel p) {
      p.add(ok = new Button("      OK       "));
      ok.addActionListener(this); 
      p.add(can = new Button("    Cancel    "));
      can.addActionListener(this);
  }

 void createFrame() {
  Dimension d = getToolkit().getScreenSize();
  setLocation(d.width/4,d.height/3);
  }

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

  public String getPassword() { 
     String Spassword = new String(password.getPassword());
     return Spassword;
 }
 
  public String getUserName() { 
      
      return username2.getText(); 
  }

}
}
  


