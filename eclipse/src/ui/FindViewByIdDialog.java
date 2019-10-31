package ui;

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
import bean.ViewAndId;
import bean.WindowPosition;
import utils.Resources;
import utils.Tools;
import view.QButton;

public class FindViewByIdDialog extends JDialog{
	
	private static final long serialVersionUID = 1L;
	private int dialogWidth=395;
	private int dialogheight=378;
	private int rowSelected=0;
	private FindViewListener mListener=null;

	
	private static final String dialogName="生成findViewById";
	
	private final String widgetType="控件类型";
	private final String widgetName="控件名称";
	private final String widgetResId="资源ID";
	private final String widgetAdd="添加";
	private final String widgetCreat="生成代码";
	private final String pop_remove="移除该项";
	
	private final String[] TableHead = new String[]{"类型","名称","资源Id"};
	public static final String[] lvViewTypes= new String[] {"TextView","ImageView","EditText","LinearLayout","RelativeLayout","Button","ImageButton","CheckBox","Spinner"};
	public static final String[] lvViewParent=new String[] {"default","getActivity()","itemView","view"};
	
	
	private JTextField tfName;
	private JTextField tfResId;
	private DefaultTableModel mModel;
	
	public FindViewByIdDialog(Frame frame,WindowPosition parent) {
		
		super(frame,dialogName,ModalityType.APPLICATION_MODAL);
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
		
		Font lbFont=new Font("宋体", Font.PLAIN, 14);
		
		JLabel lbType = new JLabel(widgetType);
		lbType.setFont(lbFont);
		lbType.setBounds(10, 10, 65, 16);
		contentPane.add(lbType);
		
		JLabel lbName = new JLabel(widgetName);
		lbName.setFont(lbFont);
		lbName.setBounds(10, 80, 65, 16);
		contentPane.add(lbName);
		
		JLabel lbResId = new JLabel(widgetResId);
		lbResId.setBounds(10, 45, 54, 16);
		lbResId.setFont(lbFont);
		contentPane.add(lbResId);
		
		JComboBox<String> cbType = new JComboBox<String>();
		cbType.setBounds(70, 6, 95, 25);
		cbType.setEditable(true);
		contentPane.add(cbType);
		
		tfName = new JTextField();
		tfName.setBounds(70, 76, 150, 25);
		contentPane.add(tfName);
		tfName.setColumns(10);
		
		tfResId = new JTextField();
		tfResId.setColumns(10);
		tfResId.setBounds(70, 41, 150, 25);
		contentPane.add(tfResId);
		
		JCheckBox cbAuto = new JCheckBox("附加 R.id.");
		cbAuto.setBounds(235, 42, 100, 23);
		cbAuto.setFont(lbFont);
		cbAuto.setSelected(true);
		contentPane.add(cbAuto);
		
		QButton btnAdd = new QButton(widgetAdd,Resources.COLOR_GREEN_DARKER);
		btnAdd.setBounds(237, 77, 132, 23);
		contentPane.add(btnAdd);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 110, 359, 182);
		contentPane.add(scrollPane);
		
		JTable table = new JTable();
		table.setFont(lbFont);
		scrollPane.setViewportView(table);
		
		QButton btnCreat = new QButton(widgetCreat,Resources.COLOR_INDIAN_RED);
		btnCreat.setBounds(10, 302, 362, 30);
		contentPane.add(btnCreat);
		
		JLabel lbPrefix = new JLabel("父级视图");
		lbPrefix.setFont(lbFont);
		lbPrefix.setBounds(175, 10, 65, 16);
		contentPane.add(lbPrefix);
		
		JComboBox<String> cbParent = new JComboBox<String>();
		cbParent.setBounds(237, 6, 132, 25);
		cbParent.setEditable(true);
		contentPane.add(cbParent);
		
		addType(cbType);
		addViewParent(cbParent);
		
		mModel = setTableModel(table);
		
		JPopupMenu popupMenu = new JPopupMenu();
		JMenuItem menuItem=new JMenuItem(pop_remove);
		popupMenu.add(menuItem); 
		
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
		
		SwingUtilities.invokeLater(new  Runnable(){
			public void run() {
				tfResId.requestFocus();
			}
		});
		
		btnAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String type=cbType.getEditor().getItem().toString();
				String varname=tfName.getText().toString();
				String varres=tfResId.getText().toString();
				if(Tools.strOK(type) && Tools.strOK(varname) && Tools.strOK(varres) ) {
					Object[] obj;
					obj = cbAuto.isSelected() ? new Object[]{type,varname,"R.id."+varres} : new Object[]{type,varname,varres};
					mModel.addRow(obj);
					tfName.setText("");
					tfResId.setText("");
					tfResId.requestFocus();
				}
			}
		});
		
		tfName.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_ENTER) {
					String type=cbType.getEditor().getItem().toString();
					String varname=tfName.getText().toString();
					String varres=tfResId.getText().toString();
					if(Tools.strOK(varname) && Tools.strOK(varres)) {
						Object[] obj = cbAuto.isSelected() ? new Object[]{type,varname,"R.id."+varres} : new Object[]{type,varname,varres};
						mModel.addRow(obj);
						tfName.setText("");
						tfResId.setText("");
						tfResId.requestFocus();
						return;
					}
					if(Tools.strOK(varname)) {
						tfResId.requestFocus();
					}else {
						tfName.requestFocus();
					}
				}
			}
		});
	
		tfResId.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_ENTER) {
					String type=cbType.getEditor().getItem().toString();
					String varname=tfName.getText().toString();
					String varres=tfResId.getText().toString();
					if(Tools.strOK(varname) && Tools.strOK(varres)) {
						Object[] obj = cbAuto.isSelected() ? new Object[]{type,varname,"R.id."+varres} : new Object[]{type,varname,varres};
						mModel.addRow(obj);
						tfName.setText("");
						tfResId.setText("");
						return;
					}
					if(Tools.strOK(varname)) {
						tfResId.requestFocus();
					}else {
						tfName.requestFocus();
					}
				}
			}
		});
		
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
		
		btnCreat.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(mListener!=null) {
					int rowcount=table.getRowCount();
					List<ViewAndId> items=null;
					String viewParent=cbParent.getEditor().getItem().toString();
					if(rowcount>0) {
						items=new ArrayList<ViewAndId>(16);
						for(int i=0;i<rowcount;++i) {
							ViewAndId viewid=new ViewAndId(table.getValueAt(i,0).toString(),table.getValueAt(i,1).toString(),
									table.getValueAt(i,2).toString());
							items.add(viewid);
						}
						if("default".equals(viewParent)) {
							mListener.onViewIdSetted(null,items,true);
						}else {
							mListener.onViewIdSetted(viewParent,items,true);
						}
					}
				}
			}
		});
		
		ActionListener menuaction =new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JMenuItem item=(JMenuItem)e.getSource();
				if(item==menuItem) {
					mModel.removeRow(rowSelected);
				}
			}
		};
		
		menuItem.addActionListener(menuaction);
	}	
	
	public interface FindViewListener{
		void onViewIdSetted(String parentView,List<ViewAndId> items,boolean varannounce);
	}
	
	public void setDialogResultListener(FindViewListener listener) {
		mListener=listener;
	}

	private void addType(JComboBox<String> comboBox) {
		
		int len=lvViewTypes.length;
		for(int i=0;i<len;i++) {
			comboBox.addItem(lvViewTypes[i]);
		}
	}
	
	private void addViewParent( JComboBox<String> comboBox) {
		
		int len=lvViewParent.length;
		for(int i=0;i<len;i++) {
			comboBox.addItem(lvViewParent[i]);
		}
	}
	
	private DefaultTableModel setTableModel(JTable table) {
		DefaultTableModel tmodel=(DefaultTableModel)table.getModel();;
		tmodel.setColumnIdentifiers(TableHead);
		return tmodel;
	}
	
	public void setLocation(WindowPosition parent) {
		Pointi point=new Pointi();
		point.setX((parent.getW()/2+parent.getX()));
		point.setY((parent.getH()/2+parent.getY()));
		setBounds(point.getX()-dialogWidth/2,point.getY()-dialogheight/2,dialogWidth,dialogheight);
	}
	
	public void initDatasets() {
		tfName.setText("");
		tfResId.setText("");
		
		int count = mModel.getRowCount();
		for(int i = 0; i < count; ++i) {
			mModel.removeRow(0);
		}
	}
}
