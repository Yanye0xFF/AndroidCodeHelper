package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import bean.Pointi;
import bean.TableItem;
import bean.WindowPosition;
import utils.Resources;
import utils.Tools;
import view.BaseInfoJPanel;
import view.ParamsJPanel;
import view.ParamsJPanel.AddActionListener;
import view.QButton;

public class RecyclerAdapterDialog extends JDialog{
	
	private static final long serialVersionUID = 1L;
	private static final String dialogName="RecyclerView适配器创建";
	private static final String dialogName2="ListView适配器创建";
	private int dialogWidth=380;
	private int dialogheight=490;
	
	private RecycleViewListener listener=null;

	private static final String[] TABLE_HEAD = new String[]{"数据类型","变量类型","名称","资源ID"}; 
	
	private static final String ROW_VAR="TYPE_VAR";
	private static final String ROW_VIEW="TYPE_VIEW";
	private int ROW_SELECTED=0;
	
	
	
	private BaseInfoJPanel baseJPanel;
	private ParamsJPanel paramsJPanel;
	
	private DefaultTableModel mtablemodel;
	/**
	 * @param params 0:adaptername,1:viewHolderName,2:layoutFileName
	 * */
	public RecyclerAdapterDialog(Frame frame,WindowPosition parent,String[] params) {
		super(frame,ModalityType.APPLICATION_MODAL);
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
		
		Font font =new Font("宋体", Font.PLAIN, 14);
		Color colorData=new Color(255,250,250);
		
		baseJPanel = new BaseInfoJPanel();
		baseJPanel.setBounds(0, 0, 374, 90);
		baseJPanel.build();
		contentPane.add(baseJPanel);
		
		paramsJPanel = new ParamsJPanel();
		paramsJPanel.setBounds(0, 90, 374, 135);
		paramsJPanel.build();
		contentPane.add(paramsJPanel);
		
		//信息显示区
		JPanel dataPanel = new JPanel();
		dataPanel.setBounds(0, 224, 374, 237);
		dataPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
		dataPanel.setLayout(null);
		dataPanel.setBackground(colorData);
		contentPane.add(dataPanel);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 5, 354, 180);
		dataPanel.add(scrollPane);
		
		JTable table = new JTable();
		scrollPane.setViewportView(table);
		
		QButton btnQuery = new QButton("生成代码",Resources.COLOR_INDIAN_RED);
		btnQuery.setBounds(10, 195, 354, 30);
		btnQuery.setFont(font);
		dataPanel.add(btnQuery);

		JPopupMenu popupMenu = new JPopupMenu();
		JMenuItem menuItem=new JMenuItem("删除");
		popupMenu.add(menuItem); 
		
		mtablemodel = setTableModel(table);
		
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
		
		//set dafault edittext params
		if(params!=null && params.length>0) {
			if(Tools.strOK(params[1])) {
				setTitle(dialogName2);
				baseJPanel.setDefaultDatas(null,params[1],null);
			}
		}else {
			setTitle(dialogName);
		}
		
		paramsJPanel.setAddActionListener(new AddActionListener() {
			@Override
			public void onAddAction(Integer paramsType, String varType, String varName, String resId) {
				Object[] obj = null;
				if(paramsType==ParamsJPanel.TYPE_VAR) {
					obj = new Object[]{ROW_VAR,varType,varName,null};
				}else if(paramsType==ParamsJPanel.TYPE_VIEW) {
					obj = new Object[]{ROW_VIEW,varType,varName,resId};
				}
				if(obj!=null) {
					mtablemodel.addRow(obj);
				}
			}
		});
		
		
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(arg0.getButton()==MouseEvent.BUTTON3) {
					ROW_SELECTED = table.rowAtPoint(arg0.getPoint());
					 if(ROW_SELECTED>=0) {
						 //获取鼠标弹窗位置并弹窗
						 table.setRowSelectionInterval(ROW_SELECTED,ROW_SELECTED);
						 popupMenu.show(arg0.getComponent(),arg0.getX(), arg0.getY());
					 }
				}
			}
		});
		
		btnQuery.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//process data
				List<String> datas=baseJPanel.getInputDatas();
				List<Boolean> selections=baseJPanel.getSelectDatas();
				List<TableItem> items=new ArrayList<TableItem>(16);
				int rowcount=0;
				//读取基本信息区数据
				if(datas.size()>=3 && selections.size()>=2) {
					//读取列表参数信息
					rowcount=table.getRowCount();
					if(table!=null && rowcount>0) {
						for(int i=0;i<rowcount;++i) {
							//根据getValueAt(i,0)的数据类型判断是变量信息还是组件信息
							String tmp=table.getValueAt(i,0).toString();
							if(tmp!=null && tmp.equals(ROW_VAR)) {
								//变量数据类型
								items.add(new TableItem(ParamsJPanel.TYPE_VAR,table.getValueAt(i,1).toString(),
										table.getValueAt(i,2).toString(),null));
							}else if(tmp!=null && tmp.equals(ROW_VIEW)) {
								//视图数据类型
								items.add(new TableItem(ParamsJPanel.TYPE_VIEW,table.getValueAt(i,1).toString(),
										table.getValueAt(i,2).toString(),table.getValueAt(i,3).toString()));
							}
						}
						if(listener!=null && items.size()>0) {
							//信息回调函数
							listener.OnDataResult(datas.get(0),datas.get(1),datas.get(2),selections.get(0),items);
						}
					}
				}
			}
		});
		
		//列表鼠标右击弹窗
		ActionListener menuaction =new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JMenuItem item=(JMenuItem)e.getSource();
				if(item==menuItem) {
					mtablemodel.removeRow(ROW_SELECTED);
				}
			}
		};
		menuItem.addActionListener(menuaction);
	}
	
	/**
	 * 设置表头信息
	 * */
	private DefaultTableModel setTableModel(JTable table) {
		DefaultTableModel tmodel=(DefaultTableModel)table.getModel();;
		tmodel.setColumnIdentifiers(TABLE_HEAD);
		return tmodel;
	}
	
	/**
	 * RecycleViewListener回调接口
	 * */
	public interface RecycleViewListener{
		void OnDataResult(String adapterName,String viewHolderName,String layoutFileName,boolean listenerState,List<TableItem> tableDatas);
	}
	
	/**
	 * 设置回调监听
	 * @param listener 回调监听接口
	 * */
	public void setRecycleViewListener(RecycleViewListener listener) {
		this.listener=listener;
	}
	
	public void setLocation(WindowPosition parent) {
		Pointi point=new Pointi();
		point.setX((parent.getW()/2+parent.getX()));
		point.setY((parent.getH()/2+parent.getY()));
		setBounds(point.getX()-dialogWidth/2,point.getY()-dialogheight/2,dialogWidth,dialogheight);
	}
	
	public void initDatasets() {
		baseJPanel.setDefaultStates(false, true);
		baseJPanel.setDefaultDatas("","","");
		
		paramsJPanel.initInputField();
		
		int count = mtablemodel.getRowCount();
		for(int i = 0; i < count; ++i) {
			mtablemodel.removeRow(0);
		}
	}
	
}
