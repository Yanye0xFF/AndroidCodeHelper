package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.border.EmptyBorder;

import bean.FuncItem;

public class EditCellRenderer extends JPanel implements ListCellRenderer<FuncItem> {
	
	private static final long serialVersionUID = 1L;
	private JLabel lbName = new JLabel();
	private Font font=null;
	private Color selColor;
	private Color norColor;
	JPanel contentPane = new JPanel();
	public EditCellRenderer() {
		selColor=new Color(255,160,122);
		norColor=new Color(230,230,250);
		font=new Font("宋体",Font.PLAIN,14);
		
		setLayout(null);
		contentPane.setBounds(0, 0, 80, 37);
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		contentPane.setLayout(null);
		contentPane.add(lbName);
		
		add(contentPane);
	}

    @Override
    public Component getListCellRendererComponent(JList<? extends FuncItem> list,FuncItem item,int index,boolean isSelected,boolean cellHasFocus) {
        
    	if(item.getName().length()<3) {
        	lbName.setBounds(15, 10, 60, 16);
        }else {
        	lbName.setBounds(5, 10, 75, 16);
        }
    	
    	lbName.setText(item.getName());
        lbName.setFont(font);
        lbName.setIcon(new ImageIcon(System.getProperty("user.dir")+"\\resource\\"+item.getIconPath()+".png"));
        if(isSelected) {
        	lbName.setForeground(Color.WHITE);
        	contentPane.setBackground(selColor);
        }else {
        	lbName.setForeground(Color.DARK_GRAY);
        	contentPane.setBackground(norColor);
        }  
        return this;
    }
}
