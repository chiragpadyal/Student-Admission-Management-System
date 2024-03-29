package Frames;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import sql.conn;

public class BatchForm  extends JFrame{
    static int StudentID = 0;
    static String PaymentID;
    public BatchForm(int ID){
        //Constructor
        StudentID = ID;


        setTitle("Branch Form");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                if (JOptionPane.showConfirmDialog(null, 
                    "Form is not saved, still want to exit to Student Panel?", "Alert!", 
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
                        new StudentPortal(StudentID);
                        dispose();
                }
            }
        });
        setSize(1280, 750);
        setLocationRelativeTo(null);
        setResizable(false);

        //plane
        JPanel pane = new JPanel();
        add(pane);
        pane.setLayout(null);
        pane.setBackground(Color.white);
        pane.setSize(1280, 720);
        
        //bgPlane
        JPanel bgPlane = new JPanel();
        bgPlane.setBackground(Color.decode("#EDE3E3"));
        bgPlane.setBounds(36,95,1208,590);

        //obj
        JLabel title = new JLabel("A P Shah Institude Of Technoogy", JLabel.CENTER);
        JLabel subtitle = new JLabel("Branch Form");
        JLabel branch = new JLabel("Branch:");
        JLabel AcadYear = new JLabel("Academic Year:");
        JLabel Course = new JLabel("Course:");
        JLabel PayId = new JLabel("Payment Receipt Photo:");
        JButton PIField = new JButton("No File"); // file upload
        JLabel header1 = new JLabel("Admission Fees Should Be Paid on Following Bank Details", JLabel.CENTER);
        JTextArea BKDetail = new JTextArea("Bank Name.: xxxxxxxxxx \nBank Acct No.: xxxxxxxxxx\nIFSE:xxxxxxxxxxxxxxx\nAcct Holder Name: AP Shah") ;
        JLabel alertText = new JLabel("Alert! Enter all Field Correctly!",JLabel.CENTER);
        
        PIField.addActionListener(e -> {
            JFileChooserOpener();
            PIField.setText("Selected");
        });

        String branchArr[] = { "IT", "Computer Science", "Electronics", "Civil", "Mechanical" };
        String year[] = { "FE", "SE", "TE", "BE"};
        String couStr[] = { "BTech" };
        JComboBox<String> branchField = new JComboBox<>(branchArr);
        JComboBox<String> AYField = new JComboBox<>(year);
        JComboBox<String>  CouField = new JComboBox<>(couStr);
        //JTextField PIField = new JTextField();

        JButton subBut = new JButton("Submit");
        JPanel mainBg = new JPanel();
        JPanel secBg = new JPanel();
        //add
        pane.add(title);
        pane.add(subtitle);
        pane.add(branch);
        pane.add(AcadYear);
        pane.add(Course);
        pane.add(PayId);
        pane.add(header1);
        pane.add(BKDetail);
        pane.add(alertText);
        pane.add(subBut);
        pane.add(branchField);
        pane.add(AYField);
        pane.add(CouField);
        pane.add(PIField);


        pane.add(secBg);
        pane.add(mainBg);


        //style
        alertText.setVisible(false);

        alertText.setFont(new Font("Serif", Font.PLAIN, 15));
        alertText.setForeground(Color.red);
        title.setFont(new Font("Serif", Font.PLAIN, 30));
        title.setOpaque(true);
        title.setBackground(Color.lightGray);

        secBg.setBackground(Color.lightGray);
        mainBg.setBackground(Color.decode("#EDE3E3"));

        BKDetail.setEditable(false);
        BKDetail.setFont(new Font("Serif", Font.PLAIN, 20));

        // addsField.setBorder(BorderFactory.createEtchedBorder());
        
        //setBounds
        title.setBounds(0,0,1280,40);
        subtitle.setBounds(45,55,239,37);
        branch.setBounds(58,119,239,37);
        AcadYear.setBounds(58,190,239,37);
        Course.setBounds(58,261,239,37);
        PayId.setBounds(58,334,239,37);
        header1.setBounds(714,138,489,46);
        BKDetail.setBounds(802,201,313,195);
        alertText.setBounds(410,522,488,46);
        subBut.setBounds(533,579,232,49);
        branchField.setBounds(297,119,249,39);
        CouField.setBounds(297,190,249,39);
        AYField.setBounds(297,261,249,39);
        PIField.setBounds(297,334,249,39);
        secBg.setBounds(714,119,489,365);
        mainBg.setBounds(36,92,1208,592);

        //Butoon Press
        subBut.addActionListener(e ->
        {

            String CourseText = CouField.getSelectedItem().toString();
            String paymentIDLink = "/Storage/" + StudentID + "/" + StudentID + "-" +"receipt.png";
            String Branch = branchField.getSelectedItem().toString();
            String Year = AYField.getSelectedItem().toString();
            if (PaymentID.isEmpty()) {
                alertText.setVisible(true);
            }else{
            storeData(CourseText, paymentIDLink, Branch, Year);
            fileCopy(System.getProperty("user.dir") + paymentIDLink, PaymentID);
            new StudentPortal(ID);
            setVisible(false);
            };
        });

    }


    private void JFileChooserOpener(){//Select File to Copy and Create Dir
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        jfc.setDialogTitle("Select an image");
        jfc.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG and GIF images", "png", "gif");
        jfc.addChoosableFileFilter(filter);
        int returnValue = jfc.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            System.out.println(jfc.getSelectedFile().getPath());
        };
        String dir = System.getProperty("user.dir") +  "/Storage/"+ StudentID + "/";
        try {
            Files.createDirectories(Paths.get(dir));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        PaymentID = jfc.getSelectedFile().getPath();
        
    }

    private void fileCopy(String currentPath,String targetLocation  ){//Copy FIle to Storage/StudentID
        File currentFile  = new File(currentPath);
        File targetFile  = new File(targetLocation);  
        FileOutputStream fileOutputStream = null; 
        FileInputStream fileInputStream = null;
        try {
         fileOutputStream = new FileOutputStream(currentFile);
         fileInputStream = new FileInputStream(targetFile);
         byte[] buffer = new byte[4096];
         int read;
         while ((read = fileInputStream.read(buffer)) != -1) {
          fileOutputStream.write(buffer, 0, read);
         }
        } catch(IOException e) {
         try {
          e.printStackTrace();
          if (fileInputStream != null) {
           fileInputStream.close();     
          }
          if (fileOutputStream != null) {
           fileOutputStream.flush(); 
           fileOutputStream.close();
          }
         }
         catch (IOException e1) {
          e1.printStackTrace();
         }
        }
    }

    private void storeData(String Course, String paymentID, String Branch, String Year)//Store Data To Mysql
    {
     try{
        String insertSQL ="insert into BranchData (id, Course, paymentID, Branch, Year) values(?,?,?,?,?)" ;
        Class.forName("com.mysql.cj.jdbc.Driver");  
        Connection con=DriverManager.getConnection(conn.Conn, conn.Conn_user, conn.Conn_pw);  
        PreparedStatement Pstatement=con.prepareStatement(insertSQL);
        //code
        Pstatement.setInt(1,StudentID);
        Pstatement.setString(2,Course);
        Pstatement.setString(3,paymentID);
        Pstatement.setString(4,Branch);
        Pstatement.setString(5,Year);
        Pstatement.executeUpdate();
        JOptionPane.showMessageDialog(null,"Data Registered Successfully");
        Pstatement.close();
        con.close();

    }catch(Exception e){ System.out.println(e);}        
    }

}
