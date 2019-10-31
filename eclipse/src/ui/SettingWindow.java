package ui;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.RoundRectangle2D;

import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.InputMap;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JRootPane;
import javax.swing.JWindow;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;

import bean.Pointi;
import bean.SettingItem;
import bean.WindowPosition;
import utils.ConfigUtils;
import view.QButton;

public class SettingWindow extends JWindow{
	private static final long serialVersionUID = 1L;
	private int dialogWidth=300;
	private int dialogheight=90;
	private volatile int originX=0,originY=0;
	private ButtonActionListener buttonActionListener;
	private JCheckBox cbExpandAll;
	private JRadioButton rdUtf8,rdGbk;
	private QButton btnCancel,btnSave;
	
	public SettingWindow(WindowPosition parent) {
		Pointi point=new Pointi();
		point.setX((parent.getW()/2+parent.getX()));
		point.setY((parent.getH()/2+parent.getY()));
		setSize(dialogWidth,dialogheight);
		setLocation(point.getX()-dialogWidth/2,point.getY()-dialogheight/2);
		setAlwaysOnTop(true);
		setShape(new RoundRectangle2D.Double(0, 0, 300, 90, 10, 10));
		
        buttonActionListener=new ButtonActionListener();
        
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(null);
		setContentPane(contentPane);
		
		JLabel label = new JLabel("功能列表:");
		label.setBounds(10, 10, 60, 16);
		contentPane.add(label);
		
		cbExpandAll = new JCheckBox("展开所有项目");
		cbExpandAll.setBounds(76, 10, 150, 16);
		contentPane.add(cbExpandAll);
		cbExpandAll.setSelected(true);
		
		JLabel lbEncode = new JLabel("存储编码:");
		lbEncode.setBounds(10, 36, 60, 16);
		contentPane.add(lbEncode);
		
		rdUtf8 = new JRadioButton("UTF-8");
		rdUtf8.setBounds(76, 36, 60, 16);
		rdUtf8.setSelected(true);
		contentPane.add(rdUtf8);
		
		rdGbk = new JRadioButton("GBK");
		rdGbk.setBounds(138, 36, 54, 16);
		rdGbk.setSelected(false);
		contentPane.add(rdGbk);
		
		ButtonGroup group =new ButtonGroup();
		group.add(rdUtf8);
		group.add(rdGbk);
		
		btnCancel = new QButton("返回",Color.BLUE);
		btnCancel.setBounds(230, 60, 60, 23);
		contentPane.add(btnCancel);
		
		btnSave = new QButton("保存",Color.RED);
		btnSave.setBounds(160, 60, 60, 23);
		contentPane.add(btnSave);
		 
		btnCancel.addActionListener(buttonActionListener);
		btnSave.addActionListener(buttonActionListener);
		
		loadSettings();
		
		JRootPane rp= this.getRootPane(); 
		KeyStroke stroke = KeyStroke.getKeyStroke("ESCAPE");
		InputMap inputMap = rp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		inputMap.put(stroke, "ESCAPE");
		rp.getActionMap().put("ESCAPE", new AbstractAction() {
			private static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent e) {
					dispose();
		    	}
		});
		
	
		contentPane.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent arg0) {
				super.mouseMoved(arg0);
				Point point = getLocation();
				setLocation( point.x + (arg0.getX() - originX), point.y + (arg0.getY() - originY));
				point=null;
			}
		});
	
		contentPane.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				super.mousePressed(e);
				originX=e.getX();
				originY=e.getY();
			}
		});
	}
	
	private class ButtonActionListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource().equals(btnCancel)) {
				dispose();
			}else {
				saveSettings();
				dispose();
			}
		}
	}
	
	private void loadSettings() {
		ConfigUtils config=new ConfigUtils();
		SettingItem item=config.readConfig();
		if(item!=null) {
			cbExpandAll.setSelected(item.getExpandAll());
			//读取编码信息 
			if(item.getEncodeType().equals("utf-8")) {
				rdUtf8.setSelected(true);
			}else {
				rdGbk.setSelected(true);
			}
		}
		item=null;
		config=null;
	}
	
	private void saveSettings() {
		SettingItem item = new SettingItem();
		if(cbExpandAll.isSelected()) {
			item.setExpandAll(true);
		}else {
			item.setExpandAll(false);
		}
		
		if(rdUtf8.isSelected()) {
			item.setEncodeType("utf-8");
		}else if(rdGbk.isSelected()) {
			item.setEncodeType("gbk"); 
		}
		if(item!=null) {
			new ConfigUtils().updateConfig(item);
		}
	}
	
	public void setLocation(WindowPosition parent) {
		Pointi point=new Pointi();
		point.setX((parent.getW()/2+parent.getX()));
		point.setY((parent.getH()/2+parent.getY()));
		setLocation(point.getX()-dialogWidth/2,point.getY()-dialogheight/2);
		point=null;
	}
}
