package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class DelayNoticeDialog {
	
    private int secends = 0;  
    private JLabel label = new JLabel();   
    private JDialog dialog = null;  
    
    public void showDialog(Frame father, String message, String btnText,int sec) {  

        secends = sec;  
        label.setText(message); 
        label.setForeground(Color.RED);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setFont(new Font("宋体", Font.PLAIN, 16));
        label.setBounds(10, 24, 164, 42);
        ScheduledExecutorService schedule = Executors.newSingleThreadScheduledExecutor();  
        
    	JButton btnOk = new JButton(btnText);
    	btnOk.setBounds(42, 88, 93, 23);
        dialog = new JDialog(father, true);  
        dialog.setTitle(secends+"秒后关闭");  
        dialog.setLayout(null);  
        dialog.add(btnOk);
        dialog.add(label);
        
        btnOk.addActionListener(new ActionListener() {     
            @Override  
            public void actionPerformed(ActionEvent e) {  
                DelayNoticeDialog.this.dialog.dispose();  
            }  
        });  
        
        schedule.scheduleAtFixedRate(new Runnable() {  
            @Override  
            public void run() {
                DelayNoticeDialog.this.secends--;  
                if(DelayNoticeDialog.this.secends == 0) {  
                    DelayNoticeDialog.this.dialog.dispose();  
                }else {  
                    dialog.setTitle(secends+"秒后关闭");  
                }  
            }  
        }, 1, 1, TimeUnit.SECONDS);  
        dialog.pack();  
        dialog.setSize(new Dimension(195,160));  
        dialog.setLocationRelativeTo(father);  
        dialog.setVisible(true);  
    }

}