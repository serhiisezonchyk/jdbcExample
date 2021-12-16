import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class GUI extends JFrame{
    private JTextField textField1;
    private JTable table1;
    private JButton button1;
    private JTextArea textArea1;
    private JScrollPane scrollPane1;
    private JPanel panelMain;
    public GUI(SimpleConection sc){
        Container content = getContentPane();
        setTitle("JAVA L2 TEST DB CONNECTIONS");
        setContentPane(content);
        setDefaultCloseOperation(
            JFrame.EXIT_ON_CLOSE);

        pack();
        setSize(900,650);
        Font ftext = new Font("Serif",Font.PLAIN,20);
        textArea1.setForeground(Color.red);
        textArea1.setFont(ftext);
        JScrollPane jScrollPane = new JScrollPane(textArea1);
        jScrollPane.setPreferredSize(new Dimension(-1,300));
        add(jScrollPane,BorderLayout.NORTH);
        Font ftable = new Font("Serif",Font.PLAIN,18);
        table1.setFont(ftable);
        JScrollPane jScrollPaneList = new JScrollPane(table1);
        jScrollPaneList.setPreferredSize(new Dimension(-1,300));
        add(jScrollPaneList,BorderLayout.CENTER);
        button1.setText("Submit");
        button1.setPreferredSize(new Dimension(-1,50));
        add(button1,BorderLayout.SOUTH);
        setContentPane(content);
        setVisible(true);

        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String sql = textArea1.getText();
                try {
                    TableModel tm = sc.returnRequest(sql);
                    table1.setModel(tm);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
    public static void main(String[] args) throws Exception {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());//Дизайн системи
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException e1) {
            e1.printStackTrace();
        }
        SimpleConection sc = new SimpleConection();
        new GUI(sc);
        if(sc.conn!=null){
            sc.close();
            System.out.println("Exit");
        }
    }
}
