package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.InputMap;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import bean.Pointi;
import bean.Variable;
import bean.WindowPosition;
import utils.Resources;
import utils.Tools;
import view.QButton;


public class BeanCreaterDialog extends JDialog{
	
	private static final long serialVersionUID = 1L;
	private static final int dialogWidth=310;
	private static final int dialogheight=460;
	
	private static final String[] beanTableHead = new String[]{"类型","名称"};
	private static final String[] beanTypes= new String[] {"String","int","boolean","double","float","Object","long","short","byte","char"};
	
	private JTextField tfPackage,tfClass,tfVar;
	private JComboBox<String> cbType;
	private QButton btnAdd,btnQuery;
	private JTable table;
	private JCheckBox cbserial,cbEquals,cbCopy;
	
	private int rowSelected=0;
	
	private DialogResultListener dialogListener=null;
	private InputKeyListener inputKeyListener=null;
	private ButtonActionListener buttonListener=null;
	
	private DefaultTableModel mtablemodel=null;
	
	private List<Variable> varCache = null;
	
	public BeanCreaterDialog(Frame frame,WindowPosition parent,List<Variable> variables) {
		super(frame,"创建JavaBean",ModalityType.APPLICATION_MODAL);
		
		if(variables!=null && variables.size()>0) {
			setTitle("编辑JavaBean");
		}
		
		Pointi point=new Pointi();
		point.setX((parent.getW()/2+parent.getX()));
		point.setY((parent.getH()/2+parent.getY()));
		setBounds(point.getX()-dialogWidth/2,point.getY()-dialogheight/2,dialogWidth,dialogheight);
        setResizable(false);
        setAlwaysOnTop(true);
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(null);
		setContentPane(contentPane);

		Font font=new Font("宋体",Font.PLAIN,14);
		varCache=new ArrayList<Variable>(16);
		inputKeyListener=new InputKeyListener();
		buttonListener=new ButtonActionListener();
		
		JLabel lbPackage = new JLabel("包名(#可空):");
		lbPackage.setFont(font);
		lbPackage.setBounds(10, 11, 90, 16);
		contentPane.add(lbPackage);
		
		tfPackage = new JTextField();
		tfPackage.setFont(font);
		tfPackage.setColumns(10);
		tfPackage.setBounds(99, 8, 195, 21);
		contentPane.add(tfPackage);
		
		JLabel lbClass = new JLabel("类名(*必填):");
		lbClass.setForeground(Color.RED);
		lbClass.setFont(font);
		lbClass.setBounds(10, 43, 90, 16);
		contentPane.add(lbClass);
		
		tfClass = new JTextField();
		tfClass.setFont(font);
		tfClass.setBounds(99, 40, 195, 21);
		contentPane.add(tfClass);
		tfClass.setColumns(10);
		
		JLabel lbType = new JLabel("类型");
		lbType.setFont(font);
		lbType.setBounds(10, 71, 69, 15);
		contentPane.add(lbType);
		
		JLabel lbName = new JLabel("名称");
		lbName.setFont(font);
		lbName.setBounds(140, 71, 69, 15);
		contentPane.add(lbName);
		
		cbType = new JComboBox<String>();
		cbType.setBounds(10, 95, 120, 21);
		cbType.setEditable(true);
		cbType.setFont(font);
		contentPane.add(cbType);
		
		tfVar = new JTextField();
		tfVar.setFont(font);
		tfVar.setBounds(140, 94, 92, 21);
		contentPane.add(tfVar);
		tfVar.setColumns(10);
		
		btnAdd = new QButton("添加",Resources.COLOR_GREEN_DARKER);
		btnAdd.setBounds(236, 71, 58, 45);
		contentPane.add(btnAdd);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 125, 284, 215);
		contentPane.add(scrollPane);
		
		table = new JTable();
		table.setFont(font);
		scrollPane.setViewportView(table);
		
		cbserial = new JCheckBox("serialize");
		cbserial.setForeground(Color.BLUE);
		cbserial.setBounds(9, 346, 80, 23);
		cbserial.setSelected(false);
		contentPane.add(cbserial);
		
		cbEquals = new JCheckBox("toJSON");
		cbEquals.setSelected(false);
		cbEquals.setForeground(Color.BLUE);
		cbEquals.setBounds(95, 346, 70, 23);
		contentPane.add(cbEquals);
		
		cbCopy = new JCheckBox("copyFrom");
		cbCopy.setSelected(false);
		cbCopy.setForeground(Color.BLUE);
		cbCopy.setBounds(170, 346, 85, 23);
		contentPane.add(cbCopy);
	
		btnQuery = new QButton("生成代码",Resources.COLOR_INDIAN_RED);
		btnQuery.setBounds(10, 375, 284, 44);
		contentPane.add(btnQuery);
		
		JPopupMenu popupMenu = new JPopupMenu();
		JMenuItem menuItem=new JMenuItem("移除该项");
		popupMenu.add(menuItem); 
		
		mtablemodel=(DefaultTableModel)table.getModel();
		mtablemodel.setColumnIdentifiers(beanTableHead);
		
		//variables存在时在自动添加表项
		setData(variables);
		
		SwingUtilities.invokeLater(new  Runnable(){
			public void run() {
				tfClass.requestFocus();
			}
		});
		
		btnAdd.addActionListener(buttonListener);
		btnQuery.addActionListener(buttonListener);
		
		tfPackage.addKeyListener(inputKeyListener);
		tfClass.addKeyListener(inputKeyListener);
		tfVar.addKeyListener(inputKeyListener);
		
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(arg0.getButton()==MouseEvent.BUTTON3) {
					rowSelected = table.rowAtPoint(arg0.getPoint());
					 if(rowSelected>=0)
						 table.setRowSelectionInterval(rowSelected,rowSelected);
					 popupMenu.show(arg0.getComponent(),arg0.getX(), arg0.getY());
				}
			}
		});
	
		ActionListener menuaction =new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JMenuItem item=(JMenuItem)e.getSource();
				if(item==menuItem) {
					mtablemodel.removeRow(rowSelected);
					varCache.remove(rowSelected);
				}
			}
		};
		menuItem.addActionListener(menuaction);
		
		updateTypeBox(cbType);
		
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
	}
	
	private class InputKeyListener extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode()!=KeyEvent.VK_ENTER) {
				return;
			}
			JTextField obj=(JTextField)e.getSource();
			if(obj==null) {
				return;
			}
			String temp=null;
			if(obj.equals(tfPackage)) {
				temp=tfPackage.getText().toString();
				if(Tools.strOK(temp)) {
					tfClass.requestFocus();
				}
			}else if(obj.equals(tfClass)) {
				temp=tfClass.getText().toString();
				if(Tools.strOK(temp)) {
					tfVar.requestFocus();
				}
			}else if(obj.equals(tfVar)) {
				temp=cbType.getEditor().getItem().toString();
				String varname=tfVar.getText().toString();
				if(Tools.strOK(temp) && Tools.strOK(varname)) {
					mtablemodel.addRow(new Object[]{temp,varname});
					tfVar.setText("");
				}
				varname=null;
			}
			temp=null;
			obj=null;
		}
	}
	
	private class ButtonActionListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			QButton button=(QButton)e.getSource();
			if(button==null) {
				return;
			}
			if(button.equals(btnAdd)) {
				String type = cbType.getEditor().getItem().toString();
				String varname = tfVar.getText().toString();
				
				if(Tools.strOK(type) && Tools.strOK(varname)) {
					
					varCache.add(new Variable(type, varname));
					
					Object[] obj = new Object[]{type,varname};
					mtablemodel.addRow(obj);
					tfVar.setText("");
				}
			}else if(button.equals(btnQuery)) {
				if(dialogListener!=null) {
					String name=tfClass.getText().toString();
					int rowcount=table.getRowCount();
					List<Variable> items=null;
					if(Tools.strOK(name) && rowcount>0) {
						items=new ArrayList<Variable>(32);
						for(int i=0;i<rowcount;++i) {
							Variable var=new Variable(table.getValueAt(i,0).toString(),table.getValueAt(i,1).toString());
							items.add(var);
						}
						dialogListener.onDialogResulted(tfPackage.getText().toString(), name, 
								items,getOptions());
					}
					name=null;
				}
			}
		}
	}
	
	public interface DialogResultListener{
		void onDialogResulted(String pack,String name,List<Variable> items,List<Boolean> options);
	}
	
	public void setDialogResultListener(DialogResultListener listener) {
		this.dialogListener=listener;
	}
	
	private void updateTypeBox(final JComboBox<String> comboBox) {
		int len=beanTypes.length;
		for(int i=0;i<len;i++) {
			comboBox.addItem(beanTypes[i]);
		}
	}
	
	public void setLocation(WindowPosition parent) {
		Pointi point=new Pointi();
		point.setX((parent.getW()/2+parent.getX()));
		point.setY((parent.getH()/2+parent.getY()));
		setBounds(point.getX()-dialogWidth/2,point.getY()-dialogheight/2,dialogWidth,dialogheight);
	}
	
	public void setData(List<Variable> variables) {
		//variables存在时在自动添加表项
		if(variables != null && variables.size() > 0) {
			//获取items大小
			int len=variables.size();
			for(int i=0;i<len;i++) {
				Variable var=variables.get(i);
				if(!isItemInCache(var)) {
					varCache.add(var);
					Object[] obj = new Object[]{var.getType(),var.getName()};
					mtablemodel.addRow(obj);
				}
				var=null;
			}
		}else {
			varCache.clear();
			int count = mtablemodel.getRowCount();
			for(int i=0;i<count;++i) {
				mtablemodel.removeRow(0);
			}
		}
	}
	
	private boolean isItemInCache(Variable var) {
		int size = varCache.size();
		for(int i = 0; i < size; ++i) {
			if(varCache.get(i).equals(var)) {
				return true;
			}
		}
		return false;
	}
	
	private List<Boolean> getOptions(){
		List<Boolean> options = new ArrayList<Boolean>(3);
		options.add(cbserial.isSelected());
		options.add(cbEquals.isSelected());
		options.add(cbCopy.isSelected());
		return options;
	}
}
