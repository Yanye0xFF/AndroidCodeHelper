package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import utils.Tools;

public class BaseInfoJPanel extends JPanel{

	private static final long serialVersionUID = 1L;
	
	Color colorBase=new Color(248,248,255);
	Font textFont =new Font("宋体", Font.PLAIN, 14);
	
	private JLabel lbAdapter=null;
	private JLabel lbView=null;
	private JLabel lbLayout=null;
	
	private JCheckBox cbResId=null;
	
	private JTextField tfAdapter=null;
	private JTextField tfView=null;
	private JTextField tfLayout=null;
	
	private JRadioButton rdItem=null;
	
	private InputKeyListener inputKeyListener;
	
	public BaseInfoJPanel() {
    	this.setBorder(new EmptyBorder(0, 0, 0, 0));
    	this.setLayout(null);
    	this.setBackground(colorBase);
    	this.setMinimumSize(new Dimension(374, 90));
    	this.setPreferredSize(new Dimension(374, 90));
    	inputKeyListener=new InputKeyListener();
	}
	
	/**
	 * 设置界面背景颜色
	 * @param Color
	 * */
	public void setPanelBackground(Color color) {
		
		if(color==null) {
			return;
		}
		
		this.setBackground(color);
		this.rdItem.setBackground(color);
		this.cbResId.setBackground(color);
	}
	
	/**
	 * 设置界面文字的字体
	 * @param Font 字体信息
	 * */
	public void setTextFont(Font font) {
		
		if(font==null) {
			return;
		}
		
		lbAdapter.setFont(font);
		lbView.setFont(font);
		lbLayout.setFont(font);
		
		cbResId.setFont(font);
		
		rdItem.setFont(font);
		
		tfView.setFont(font);
		tfAdapter.setFont(font);
		tfLayout.setFont(font);
	}
	
	public void build() {
		
		lbAdapter = new JLabel("适配器类名");
		lbAdapter.setBounds(24, 10, 70, 16);
		lbAdapter.setFont(textFont);
		this.add(lbAdapter);
		
		tfAdapter = new JTextField();
		tfAdapter.setBounds(115, 8, 145, 22);
		tfAdapter.setFont(textFont);
		tfAdapter.setColumns(10);
		this.add(tfAdapter);
		
		rdItem = new JRadioButton("点击监听");
		rdItem.setBounds(266, 7, 92, 22);
		rdItem.setFont(textFont);
		rdItem.setBackground(colorBase);
		this.add(rdItem);
		
		lbView = new JLabel("ViewHolder类名");
		lbView.setFont(textFont);
		lbView.setBounds(10, 36, 100, 16);
		this.add(lbView);
		
		tfView = new JTextField();
		tfView.setColumns(10);
		tfView.setFont(textFont);
		tfView.setBounds(115, 34, 145, 22);
		this.add(tfView);
		
		lbLayout = new JLabel("布局文件ID");
		lbLayout.setFont(textFont);
		lbLayout.setBounds(22, 62, 75, 16);
		this.add(lbLayout);
		
		tfLayout = new JTextField();
		tfLayout.setFont(textFont);
		tfLayout.setColumns(10);
		tfLayout.setBounds(115, 60, 145, 22);
		this.add(tfLayout);
		
		cbResId = new JCheckBox("附加R.*");
		cbResId.setBounds(266, 59, 92, 23);
		cbResId.setSelected(true);
		cbResId.setFont(textFont);
		cbResId.setBackground(colorBase);
		this.add(cbResId);
		
		//适配器类名编辑框键盘监听事件
		tfAdapter.addKeyListener(inputKeyListener);
		//viewHolder类名编辑框键盘监听事件
		tfView.addKeyListener(inputKeyListener);
	}
	
	
	private class InputKeyListener implements KeyListener{
		@Override
		public void keyPressed(KeyEvent arg0) {
			if(arg0.getKeyCode()==KeyEvent.VK_ENTER) {
				String tmp;
				if(arg0.getSource().equals(tfView)) {
					tmp=tfView.getText().toString();
					if(Tools.strOK(tmp)) {
						tfLayout.requestFocus();
					}
				}else if(arg0.getSource().equals(tfAdapter)) {
					tmp=tfAdapter.getText().toString();
					if(Tools.strOK(tmp)) {
						tfView.requestFocus();
					}
				}
				tmp=null;
			}
		}
		@Override
		public void keyReleased(KeyEvent arg0) {
		}
		@Override
		public void keyTyped(KeyEvent arg0) {
		}
	}
	
	/**
	 * 获取信息编辑区输入的数据,List大小为3
	 * 0 适配器类名
	 * 1 viewHolder类名
	 * 2  布局文件名称
	 * @return result 数据集合
	 * */
	public List<String> getInputDatas(){
		List<String> result=new ArrayList<String>(3);
		result.add(tfAdapter.getText());
		result.add(tfView.getText());
		//判断是否勾选自动添加R.layout.前缀
		if(cbResId.isSelected()) {
			result.add("R.layout."+tfLayout.getText());
		}else {
			result.add(tfLayout.getText());
		}
		return result;
	}
	
	/**
	 * 返回监听器/自动附加R.id选中状态 ,List大小为2
	 * 0 项目监听器状态
	 * 1 自动添加R.id状态
	 * @return result 状态集合
	 * */
	public List<Boolean> getSelectDatas() {
		List<Boolean> result=new ArrayList<Boolean>(2);
		result.add(rdItem.isSelected());
		result.add(cbResId.isSelected());
		return result;
	}
	
	/**
	 * 设置基本信息输入区域的默认数据
	 * @param adapterName 适配器类名
	 * @param viewHolderName viewholder类名
	 * @param layoutName 布局文件名
	 * */
	public void setDefaultDatas(String adapterName,String viewHolderName,String layoutName) {
		
		if(adapterName != null) {
			tfAdapter.setText(adapterName);
		}
		
		if(viewHolderName != null) {
			tfView.setText(viewHolderName);
		}
		
		if(layoutName != null) {
			tfLayout.setText(layoutName);
		}
	}
	
	/**
	 * 设置基本信息输入区监听器/自动附加R.id功能选中状态
	 * @param itemListener 添加项目监听器状态
	 * @param resPerfix 布局文件自动添加R.id状态
	 * */
	public void setDefaultStates(boolean itemListener,boolean resPerfix) {
		rdItem.setSelected(itemListener);
		cbResId.setSelected(resPerfix);
	}
	
}
