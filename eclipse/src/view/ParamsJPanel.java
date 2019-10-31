package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import utils.Resources;
import utils.Tools;

public class ParamsJPanel extends JPanel{

	private static final long serialVersionUID = 1L;
	public static final int TYPE_VAR=0;
	public static final int TYPE_VIEW=1;
	public static final String[] lvVarTypes= new String[] {"Context","List<String>","List<Integer>","List<Object>",
			"List<Map<String,String>>","List<Map<String,Object>>"};
	public static final String[] lvViewTypes= new String[] {"TextView","ImageView","EditText","LinearLayout",
			"RelativeLayout","Button","ImageButton","CheckBox","Spinner"};

	private Font fontEdit =new Font("SimSun", Font.PLAIN, 13);
	private Color colorFunc=new Color(255,255,240);
	private Font font =new Font("宋体", Font.PLAIN, 14);
	private Color colorEdit=new Color(245,255,250);
	private int itemType=0;
	
	private AddActionListener addActionListener;
	private ButtonActionListener buttonActionListener;
	private InputKeyListener inputKeyListener;
	
	public ParamsJPanel() {
		this.setBorder(new EmptyBorder(0, 0, 0, 0));
    	this.setLayout(null);
    	this.setMinimumSize(new Dimension(374, 135));
    	this.setPreferredSize(new Dimension(374, 135));
    	buttonActionListener=new ButtonActionListener();
    	inputKeyListener=new InputKeyListener();
	}
	
	private JPanel funcPanel = null;
	private JPanel editPanel=null;
	private ButtonGroup btnGroup= null;
	private JRadioButton rdVar,rdView;
	private QButton btnAdd;
	private JComboBox<String> combType;
	private JTextField tfName,tfComp;
	private JCheckBox cbId;

	public void build() {
		
		btnGroup= new ButtonGroup();
		
		funcPanel = new JPanel();
		funcPanel.setBounds(0, 0, 374, 25);
		funcPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
		funcPanel.setLayout(null);
		funcPanel.setBackground(colorFunc);
		this.add(funcPanel);
		
		rdVar = new JRadioButton("构造参数");
		rdVar.setBounds(20, 0, 85, 25);
		rdVar.setBackground(colorFunc);
		rdVar.setSelected(true);
		rdVar.setFont(font);
		
		rdView = new JRadioButton("组件信息");
		rdView.setBounds(210, 0, 110, 25);
		rdView.setBackground(colorFunc);
		rdView.setFont(font);
		
		funcPanel.add(rdVar);
		funcPanel.add(rdView);
		
		btnGroup.add(rdVar);
		btnGroup.add(rdView);
		
		editPanel=new JPanel();
		editPanel.setBounds(0, 25, 374, 110);
		editPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
		editPanel.setLayout(null);
		editPanel.setBackground(colorEdit);
		this.add(editPanel);
		
		JLabel lbType = new JLabel("类型");
		lbType.setBounds(10, 5, 46, 15);
		lbType.setFont(fontEdit);
		editPanel.add(lbType);
		
		combType = new JComboBox<String>();
		combType.setBounds(10, 25, 161, 22);
		combType.setEditable(true);
		editPanel.add(combType);
		
		JLabel lbName = new JLabel("名称");
		lbName.setFont(fontEdit);
		lbName.setBounds(197, 5, 46, 15);
		editPanel.add(lbName);
		
		tfName = new JTextField();
		tfName.setFont(font);
		tfName.setColumns(10);
		tfName.setBounds(197, 24, 161, 22);
		editPanel.add(tfName);
		
		JLabel lbComp = new JLabel("布局组件ID");
		lbComp.setFont(fontEdit);
		lbComp.setBounds(10, 55, 84, 15);
		editPanel.add(lbComp);
		
		tfComp = new JTextField();
		tfComp.setFont(font);
		tfComp.setColumns(10);
		tfComp.setBounds(10, 75, 161, 22);
		editPanel.add(tfComp);
		
		cbId = new JCheckBox("附加R.id.*");
		cbId.setSelected(true);
		cbId.setFont(fontEdit);
		cbId.setBackground(colorEdit);
		cbId.setBounds(80, 51, 108, 23);
		editPanel.add(cbId);
		
		btnAdd = new QButton("添加",Resources.COLOR_GREEN_DARKER);
		btnAdd.setBounds(197, 75, 161, 23);
		btnAdd.setFont(fontEdit);
		editPanel.add(btnAdd);
		
		//初始化itemType为TYPE_VAR
		//并更新combobox数据
		itemType=TYPE_VAR;
		updateTypeBox(combType,itemType);
		
		//构造参数点击事件
		rdVar.addActionListener(buttonActionListener);
		//组件信息点击事件
		rdView.addActionListener(buttonActionListener);
		//添加按钮点击事件
		btnAdd.addActionListener(buttonActionListener);
		
		//变量名称编辑框键盘监听事件
		tfName.addKeyListener(inputKeyListener);
		//视图资源id编辑框键盘监听事件
		tfComp.addKeyListener(inputKeyListener);
	}
	
	private class ButtonActionListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource().equals(rdVar)) {
				if(itemType!=TYPE_VAR) {
					itemType=TYPE_VAR;
					combType.removeAllItems();
					updateTypeBox(combType,itemType);
					initInputField();
				}
			}else if(e.getSource().equals(rdView)) {
				if(itemType!=TYPE_VIEW) {
					itemType=TYPE_VIEW;
					combType.removeAllItems();
					updateTypeBox(combType,itemType);
					initInputField();
				}
			}else if(e.getSource().equals(btnAdd)) {
				if(addActionListener!=null) {
					 String varType=combType.getEditor().getItem().toString();
					 String varName=tfName.getText().toString();
					 if(itemType==TYPE_VAR && Tools.strOK(varName)) {
						addActionListener.onAddAction(itemType,varType,varName,null);
						tfName.setText("");
					 }else if(itemType==TYPE_VIEW) {
						 String resId=tfComp.getText().toString();
						 if(Tools.strOK(varName) && Tools.strOK(resId)) {
							resId = cbId.isSelected() ? "R.id."+tfComp.getText().toString() : tfComp.getText().toString();
							addActionListener.onAddAction(itemType,varType,varName,resId);
							initInputField();
						 }
					 }
				}
			}
		}
	} 
	
	private class InputKeyListener implements KeyListener{
		@Override
		public void keyPressed(KeyEvent event) {
			if(event.getKeyCode()==KeyEvent.VK_ENTER) {
				String varType=null,varName=null,resId=null;
				if(event.getSource().equals(tfName)) {
					 //当前信息输入类型为TYPE_VAR时直接添加进列表
					 if(itemType==TYPE_VAR) {
						 varName=tfName.getText().toString();
						 varType=combType.getEditor().getItem().toString();
						 if(Tools.strOK(varName)) {
							 addActionListener.onAddAction(itemType,varType,varName,resId);
							 tfName.setText("");
						 }
						//当前信息输入类型为TYPE_VIEW时需要进行判断
					 }else if(itemType==TYPE_VIEW) {
						 varType=combType.getEditor().getItem().toString();
						 varName=tfName.getText().toString();
						 resId=tfComp.getText().toString();
						 //varName和resId都有数据时直接加入列表
						 if(Tools.strOK(varName) && Tools.strOK(resId)) {
							 resId = cbId.isSelected() ? "R.id."+tfComp.getText() : tfComp.getText();
							 if(addActionListener!=null) {
								 addActionListener.onAddAction(itemType,varType,varName,resId);
								 initInputField();
							 }
							 return;
						 }
						 if(Tools.strOK(varName)) {
							 tfComp.requestFocus();
						 }else {
							 tfName.requestFocus();
						 }
					 }
				}else if(event.getSource().equals(tfComp)) {
					 if(itemType==TYPE_VAR) {
						 tfName.requestFocus();
						 return;
					 }else if(itemType==TYPE_VIEW) {
						 varType=combType.getEditor().getItem().toString();
						 resId=tfComp.getText().toString();
						 varName=tfName.getText().toString();
						 if(Tools.strOK(varName) && Tools.strOK(resId)) {
							 resId = cbId.isSelected() ? "R.id."+tfComp.getText() : tfComp.getText();
							 if(addActionListener!=null) {
								 addActionListener.onAddAction(itemType,varType,varName,resId);
							 }
							 //光标在r.id输入框时将光标移至tfName
							 tfName.requestFocus();
							 initInputField();
							 return;
						 }
						 if(Tools.strOK(varName)) {
							 tfComp.requestFocus();
						 }else {
							 tfName.requestFocus();
						 }
					 }
				}
			}
		}
		@Override
		public void keyReleased(KeyEvent event) {
		}
		@Override
		public void keyTyped(KeyEvent event) {
		}
	}
	
	/**
	 * 添加变量/视图组件接口
	 * @category onAddAction 信息添加接口函数
	 * */
	public interface AddActionListener {
		/**
		 * 添加接口函数
		 * @params paramsType 返回数据类型 TYPE_VAR,TYPE_VIEW
		 * @params varType 变量/视图类型
		 * @params varName 变量/视图名称
		 * @params resId 视图资源id,仅在TYPE_VIEW时有效,TYPE_VAR时传入null即可
		 * */
		void onAddAction(Integer paramsType,String varType,String varName,String resId);
	}
	
	/**
	 * 设置变量/视图添加事件监听器
	 * @param listener 变量/视图事件监听器接口
	 * */
	public void setAddActionListener(AddActionListener listener) {
		this.addActionListener=listener;
	}
	
	/**
	 * 更新combobox内数据列表
	 * @param comboBox 被更新的combobox
	 * @param type 类型 ,分为TYPE_VAR,TYPE_VIEW
	 * */
	private void updateTypeBox(JComboBox<String> comboBox,int type) {
		int len;
		if(type==TYPE_VAR) {
			len=lvVarTypes.length;
			for(int i=0;i<len;i++) {
				comboBox.addItem(lvVarTypes[i]);
			}
		}else if(type==TYPE_VIEW){
			len=lvViewTypes.length;
			for(int i=0;i<len;i++) {
				comboBox.addItem(lvViewTypes[i]);
			}
		}
	}
	
	public void initInputField() {
		tfName.setText("");
		tfComp.setText("");
	}
}
