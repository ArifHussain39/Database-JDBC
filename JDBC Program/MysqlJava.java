
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

class MysqlJava implements ActionListener {

  JFrame j = new JFrame("Database Connection");
  JLabel title = new JLabel("MySQL Database");
  JLabel querylbl = new JLabel("Write Query");
  JLabel urllbl = new JLabel("Database Url");
  JLabel userlbl = new JLabel("Username");
  JLabel passwordlbl = new JLabel("Password");
  JLabel errorlbl = new JLabel("");
  JButton run = new JButton("Run");
  JButton connect = new JButton("Connect");
  JTextField field = new JTextField();
  JTextField urlfield = new JTextField("jdbc:mysql://HostName:Port/Database");
  JTextField userfield = new JTextField();
  JPasswordField passwordfield = new JPasswordField();
  JTable table;
  DefaultTableModel model;

  public static void main(String args[]) {
    new MysqlJava();
  }

  MysqlJava() {

    j.setLayout(null);
    j.getContentPane().setBackground(Color.black);

    title.setFont(new Font("", Font.BOLD, 30));
    title.setForeground(Color.blue);
    title.setBounds(600, 0, 300, 100);

    urllbl.setFont(new Font("", Font.PLAIN, 20));
    urllbl.setForeground(Color.white);
    urllbl.setBounds(50, 130, 150, 20);
    urlfield.setFont(new Font("", Font.PLAIN, 20));
    urlfield.setBounds(200, 120, 300, 40);

    userlbl.setFont(new Font("", Font.PLAIN, 20));
    userlbl.setForeground(Color.white);
    userlbl.setBounds(50, 190, 150, 20);
    userfield.setFont(new Font("", Font.PLAIN, 20));
    userfield.setBounds(200, 180, 300, 40);

    passwordlbl.setFont(new Font("", Font.PLAIN, 20));
    passwordlbl.setForeground(Color.white);
    passwordlbl.setBounds(50, 250, 150, 20);
    passwordfield.setFont(new Font("", Font.PLAIN, 20));
    passwordfield.setBounds(200, 240, 300, 40);

    connect.setBounds(300, 300, 100, 30);
    connect.setBackground(Color.blue);
    connect.setForeground(Color.WHITE);
    connect.setFocusPainted(false);
    connect.setFont(new Font("Arial", Font.BOLD, 16));
    connect.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.WHITE, 2), BorderFactory.createEmptyBorder(5, 15, 5, 15)));
	

    querylbl.setFont(new Font("", Font.PLAIN, 20));
    querylbl.setForeground(Color.white);
    querylbl.setBounds(50, 370, 150, 20);
    field.setFont(new Font("", Font.PLAIN, 20));
    field.setBounds(200, 360, 300, 40);

    run.setBounds(300, 420, 100, 30);
    run.setBackground(Color.blue);
    run.setForeground(Color.WHITE);
    run.setFocusPainted(false);
    run.setFont(new Font("Arial", Font.BOLD, 16));
    run.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.WHITE, 2), BorderFactory.createEmptyBorder(5, 15, 5, 15)));
	
    errorlbl.setFont(new Font("", Font.PLAIN, 15));
    errorlbl.setForeground(Color.WHITE);
    errorlbl.setBounds(20, 650, 1400, 20);

    model = new DefaultTableModel();
    table = new JTable(model);

    JScrollPane scrollPane = new JScrollPane(table);
    scrollPane.setBounds(550, 120, 750, 500);

    j.add(title);
    j.add(urllbl);
    j.add(urlfield);
    j.add(userlbl);
    j.add(userfield);
    j.add(passwordlbl);
    j.add(passwordfield);
    j.add(connect);
    j.add(querylbl);
    j.add(field);
    j.add(run);
    j.add(errorlbl);
    j.add(scrollPane);


    connect.addActionListener(this);
    run.addActionListener(this);
    run.setEnabled(false);

    j.setSize(1500, 800);
    j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    j.setVisible(true);

  }

  public void actionPerformed(ActionEvent e) {

    Connection connection;
    Statement statement;
    ResultSet resultSet;
    ResultSetMetaData rsmd;
    int columnsNumber;

    if (e.getSource() == connect) {
      try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        //connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hr", "root", "12345");
        connection = DriverManager.getConnection(urlfield.getText(), userfield.getText(), passwordfield.getText());
        errorlbl.setText("Connection created");
        run.setEnabled(true);
      } catch (Exception exception) {
	errorlbl.setText("Error: Connection not created  =  "+exception.getMessage());
      }

    }

    if (e.getSource() == run) {
      try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection(urlfield.getText(), userfield.getText(), passwordfield.getText());
        statement = connection.createStatement();

        String query = field.getText();
        int indexOfSpace = query.indexOf(' ');
        query = query.substring(0, indexOfSpace);

        if (query.equals("insert") || query.equals("INSERT") || query.equals("Insert")) {
          statement.executeUpdate(field.getText());
          statement.close();
          connection.close();
        } else if (query.equals("create") || query.equals("CREATE") || query.equals("Create")) {
          statement.executeUpdate(field.getText());
          statement.close();
          connection.close();
        }

	else {
          resultSet = statement.executeQuery(field.getText());
          rsmd = resultSet.getMetaData();

          columnsNumber = rsmd.getColumnCount();
          model.setRowCount(0);
          model.setColumnCount(0);

          //set colomns names in ScrollPane
          for (int i = 1; i <= columnsNumber; i++) {
            String colName = rsmd.getColumnName(i);
            model.addColumn(colName);
          }

          //Add rows in table
          while (resultSet.next()) {
            Object[] row = new Object[columnsNumber];
            for (int i = 1; i <= columnsNumber; i++) {
              row[i - 1] = resultSet.getObject(i);

            }
            model.addRow(row);
    
          }
          resultSet.close();
          statement.close();
          connection.close();
        }

      } catch (Exception ee) {
	errorlbl.setText("Error: "+ee.getMessage());
      }
    }
  }
}