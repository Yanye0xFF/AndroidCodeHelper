package ui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import bean.FuncItem;
import bean.SettingItem;
import bean.TableItem;
import bean.Variable;
import bean.ViewAndId;
import bean.WindowPosition;

import core.BeanCreater;
import core.FindViewCreater;
import core.ListAdapterCreater;
import core.RecycleAdapterCreater;
import core.XML2IdParser;
import thread.XML2IdThread;
import utils.ConfigUtils;
import utils.Resources;
import utils.Tools;
import view.EditCellRenderer;
import view.ParamsJPanel;
import view.Toast;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.SwingUtilities;

public class MainFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	private static final String mainTitle="Android开发助手";
	private int winX=600;
	private int winY=300;
	private int winWidth=800;
	private int winHeight=600;
	private int codeFontSize=16;
	
	private ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	private JScrollPane treeScrollPane;
	private JScrollPane editScrollPane;
	private JScrollPane codeScrollPane;
	private JTextArea codeArea;
	private JTree funcTree;
	private JList<FuncItem> editList;
	private WindowMouseAdapter windowMouseAdapter;
	private JPanel settingPanel;
	private JLabel lbSetTitle;
	private static Frame windowFrame;
	private Toast toast =null;
	private SettingWindow settingWindow=null;
	private BeanCreaterDialog beanCreaterDialog=null;
	private FindViewByIdDialog findViewByIdDialog=null;
	private RecyclerAdapterDialog recycleAdapterDialog=null;
	private RecyclerAdapterDialog listviewAdapterDialog=null;
	public MainFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle(mainTitle);
		setIconImage(new ImageIcon(System.getProperty("user.dir")+"\\resource\\app_icon.png").getImage());
		setBounds(winX, winY, winWidth, winHeight);
		setPreferredSize(new Dimension(1024, 768));
		setMinimumSize(new Dimension(winWidth,winHeight));
		setLocation(600, 300);
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);
		
		windowFrame=getFrames()[0];
		toast = new Toast(windowFrame, new WindowPosition(winX, winY, winWidth, winHeight),"", Toast.MIDDLE, Toast.msg);
		windowMouseAdapter=new WindowMouseAdapter();
		winX=getX();
		winY=getY();
		
		settingPanel = new JPanel();
		settingPanel.setBackground(new Color(240,255,240));
		settingPanel.setBounds(10, 8, 172, 41);
		settingPanel.setLayout(null);
		contentPane.add(settingPanel);
		
		JLabel lbSetIcon = new JLabel();
		lbSetIcon.setBounds(10, 5, 32, 32);
		lbSetIcon.setIcon(new ImageIcon(System.getProperty("user.dir")+"\\resource\\set.png"));
		settingPanel.add(lbSetIcon);
		
		lbSetTitle = new JLabel("Settings");
		lbSetTitle.setFont(new Font("consolas", Font.BOLD, 18));
		lbSetTitle.setForeground(new Color(46,139,87));
		lbSetTitle.setBounds(52, 7, 110, 32);
		settingPanel.add(lbSetTitle);
		
		treeScrollPane = new JScrollPane();
		treeScrollPane.setBounds(10, 60, 172, 491);
		contentPane.add(treeScrollPane);
		
		codeScrollPane = new JScrollPane();
		codeScrollPane.setBounds(192, 60, 582, 491);
		contentPane.add(codeScrollPane);
		
		codeArea = new JTextArea();
		codeArea.setFont(new Font("宋体", Font.PLAIN, codeFontSize));
		codeScrollPane.setViewportView(codeArea);
		
		editScrollPane = new JScrollPane();
		editScrollPane.setBounds(192, 10, 582, 40);
		contentPane.add(editScrollPane);
		
		DefaultListModel<FuncItem> editModel = new DefaultListModel<>();
		editModel.addElement(new FuncItem("存储",100,"save"));
		editModel.addElement(new FuncItem("复制",100,"copy"));
		editModel.addElement(new FuncItem("粘贴",100,"past"));
		editModel.addElement(new FuncItem("清除",100,"delete"));
		editModel.addElement(new FuncItem("字体-",100,"text_small"));
		editModel.addElement(new FuncItem("字体+",100,"text_large"));
		
		editList = new JList<FuncItem>(editModel);
		editList.setFixedCellHeight(37);
		editList.setFixedCellWidth(80);
		editList.setDragEnabled(false);

		editList.setCellRenderer(new EditCellRenderer());
		editList.setVisibleRowCount(1);
		editList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		editScrollPane.setViewportView(editList);

		DefaultMutableTreeNode funcMain = new DefaultMutableTreeNode("FunctionTree");
		DefaultMutableTreeNode nodeBean = new DefaultMutableTreeNode("Model");
		nodeBean.add(new DefaultMutableTreeNode(new FuncItem("JavaBean",100,null)));
		
		DefaultMutableTreeNode nodeAdapter = new DefaultMutableTreeNode("Adapter");
		nodeAdapter.add(new DefaultMutableTreeNode(new FuncItem("ListViewAdapter",200,null)));
		nodeAdapter.add(new DefaultMutableTreeNode(new FuncItem("RecycleViewAdapter",201,null)));
		
		DefaultMutableTreeNode nodeActivity = new DefaultMutableTreeNode("Activity");
		nodeActivity.add(new DefaultMutableTreeNode(new FuncItem("ActivityTemplate",300,null)));
		nodeActivity.add(new DefaultMutableTreeNode(new FuncItem("FragementTemplate",301,null)));
		
		DefaultMutableTreeNode nodeView = new DefaultMutableTreeNode("ViewAndId");
		nodeView.add(new DefaultMutableTreeNode(new FuncItem("findViewById",400,null)));
		nodeView.add(new DefaultMutableTreeNode(new FuncItem("parseXMLtoID",401,null)));
		
		funcMain.add(nodeBean);
		funcMain.add(nodeAdapter);
		funcMain.add(nodeActivity);
		funcMain.add(nodeView);
		
		funcTree = new JTree(funcMain);
		treeScrollPane.setViewportView(funcTree);
		
		ConfigUtils config=new ConfigUtils();
		SettingItem item=config.readConfig();
		//读取功能展开状态 
		if(item.getExpandAll()) {
			expandAll(funcTree, new TreePath(funcMain), true);
		}
		item=null;
		config=null;
		
		funcTree.addMouseListener(windowMouseAdapter);
		editList.addMouseListener(windowMouseAdapter);
		settingPanel.addMouseListener(windowMouseAdapter);
		
		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				winX=getX();
				winY=getY();
			}
		});
		
		windowFrame.addComponentListener(new ComponentAdapter(){
			@Override public void componentResized(ComponentEvent e){
				onWindowResize(windowFrame);
			}
		});
		windowFrame.addWindowStateListener(new WindowStateListener () {
			public void windowStateChanged(WindowEvent state) {
				int newState=state.getNewState();
				switch (newState) {
					case Frame.NORMAL:
						onWindowResize(windowFrame);
						break;
					case Frame.MAXIMIZED_BOTH:
						onWindowResize(windowFrame);
						break;
					default:
						break;
				}
			}
		});
	}
	
	private class WindowMouseAdapter extends MouseAdapter{
		@Override
		public void mouseClicked(MouseEvent e) {
			if(e.getSource().equals(funcTree)) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) funcTree.getLastSelectedPathComponent();
				if(node!=null) {
					Object object = node.getUserObject();
					if (node.isLeaf()) {
						FuncItem item = (FuncItem) object;
						onFuncSelected(item.getId());
					}
				}
			}else if(e.getSource().equals(editList)) {
				onTabClicked(editList.getSelectedIndex());
			}else if(e.getSource().equals(settingPanel)) {
				if(settingWindow==null) {
					settingWindow=new SettingWindow(new WindowPosition(winX, winY, winWidth, winHeight));
				}
				if(settingWindow.isVisible()) {
					settingWindow.dispose();
				}else {
					settingWindow.setLocation(new WindowPosition(winX, winY, winWidth, winHeight));
					settingWindow.setVisible(true);
				}
			}
		}
		@Override
		public void mouseEntered(MouseEvent e) {
			if(e.getSource().equals(settingPanel)) {
				settingPanel.setBackground(new Color(154,205,50));
				lbSetTitle.setForeground(Color.WHITE);
				setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			}
		}
		@Override
		public void mouseExited(MouseEvent e) {
			if(e.getSource().equals(settingPanel)) {
				settingPanel.setBackground(new Color(240,255,240));
				lbSetTitle.setForeground(new Color(46,139,87));
				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
		}
	}
	
	private void onWindowResize(Frame frame){
		Dimension dim=frame.getSize();
		double x= dim.getWidth();
		double y =dim.getHeight();
		winWidth=(int)x;
		winHeight=(int)y;
		//改变窗体大小时更新窗口左上角坐标
		winX=frame.getX();
		winY=frame.getY();
		codeScrollPane.setSize(new java.awt.Dimension((int)(x-218),(int)(y-109)));
		editScrollPane.setSize(new java.awt.Dimension((int)(x-218),41));
		treeScrollPane.setSize(new java.awt.Dimension(172,(int)(y-109)));
		dim=null;
	}

	private void onTabClicked(int tabPos) {
		switch (tabPos) {
			case 0:
				if(!codeArea.getText().isEmpty()) {
					String fileName=parserFileName(codeArea.getText());
					if(fileName==null) {
						toast.setLoaction(new WindowPosition(winX, winY, winWidth, winHeight));
						toast.setText("不被支持的类型");
						toast.display();
					}else{
						showFileSaverDialog(fileName,codeArea.getText());
					}
				}else {
					toast.setLoaction(new WindowPosition(winX, winY, winWidth, winHeight));
					toast.setText("请先创建代码");
					toast.display();
				}
				break;
			case 1:
				String mCode=codeArea.getText().toString();
				if(Tools.strOK(mCode)) {
					Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
					StringSelection selection = new StringSelection(mCode);
					clipboard.setContents(selection, null);
					toast.setLoaction(new WindowPosition(winX, winY, winWidth, winHeight));
					toast.setText("代码复制成功");
					toast.display();
				}else {
					toast.setLoaction(new WindowPosition(winX, winY, winWidth, winHeight));
					toast.setText("请先创建代码");
					toast.display();
				}
				mCode=null;
				break;
			case 2:
				Clipboard sysClip = Toolkit.getDefaultToolkit().getSystemClipboard();
				Transferable clipTf = sysClip.getContents(null);
				if (clipTf != null) {
		            // 检查内容是否是文本类型
		            if (clipTf.isDataFlavorSupported(DataFlavor.stringFlavor)) {
	                	try {
							codeArea.setText((String)clipTf.getTransferData(DataFlavor.stringFlavor));
						} catch (UnsupportedFlavorException | IOException e) {
							e.printStackTrace();
						}
		            }
		        }
				clipTf=null;
				sysClip=null;
				break;
			case 3:
				if(!codeArea.getText().isEmpty()) {
					codeArea.setText("");
					toast.setLoaction(new WindowPosition(winX, winY, winWidth, winHeight));
					toast.setText("内容已清空");
					toast.display();
				}
				break;
			case 4:
				if(Tools.strOK(codeArea.getText())) {
					if(codeFontSize>12) {
						codeFontSize--;
						codeArea.setFont(new Font("宋体", Font.PLAIN, codeFontSize));
					}else {
						toast.setLoaction(new WindowPosition(winX, winY, winWidth, winHeight));
						toast.setText("字体不能再小了");
						toast.display();
					}
				}
				break;
			case 5:
				if(Tools.strOK(codeArea.getText())) {
					
					if(codeFontSize<32) {
						codeFontSize++;
						codeArea.setFont(new Font("宋体", Font.PLAIN, codeFontSize));
					}else {
						toast.setLoaction(new WindowPosition(winX, winY, winWidth, winHeight));
						toast.setText("字体已经最大了");
						toast.display();
					}
				}
				break;
			default:
				break;
		}
	}
	
	private void onFuncSelected(int funcId) {
		switch (funcId) {
			case 100:
				//创建/编辑JavaBean对话框
				List<Variable> variables=paraseBeanCode();
				if(beanCreaterDialog==null) {
					beanCreaterDialog=new BeanCreaterDialog(windowFrame,new WindowPosition(winX, winY, winWidth, winHeight),variables);
					beanCreaterDialog.setDialogResultListener(new BeanCreaterDialog.DialogResultListener() {
						@Override
						public void onDialogResulted(String pack,String name,List<Variable> items,List<Boolean> options) {
							cachedThreadPool.execute(new BeanThread(pack,name,items,options));
							beanCreaterDialog.dispose();
						}
					});
					beanCreaterDialog.setVisible(true);
				}else {	
					beanCreaterDialog.setLocation(new WindowPosition(winX, winY, winWidth, winHeight));
					beanCreaterDialog.setData(variables);
					beanCreaterDialog.setVisible(true);
				}
				break;
			case 200:
				//ListViewView适配器对话框
				if(listviewAdapterDialog==null) {
					listviewAdapterDialog=new RecyclerAdapterDialog(windowFrame,new WindowPosition(winX, winY, winWidth, winHeight),
							new String[] {null,"ViewHolder",null});
					listviewAdapterDialog.setRecycleViewListener(new RecyclerAdapterDialog.RecycleViewListener() {
						@Override
						public void OnDataResult(String adapterName,String viewHolderName,String layoutFileName,boolean listenerState,List<TableItem> tableDatas) {
							//启动代码创建线程
							cachedThreadPool.execute(new ListViewThread(adapterName, viewHolderName, layoutFileName, tableDatas));
							listviewAdapterDialog.dispose();
						}
					});
				}
				listviewAdapterDialog.initDatasets();
				listviewAdapterDialog.setLocation(new WindowPosition(winX, winY, winWidth, winHeight));
				listviewAdapterDialog.setVisible(true);
				break;
			case 201:
				//RecycleView适配器对话框
				if(recycleAdapterDialog==null) {
					recycleAdapterDialog=new RecyclerAdapterDialog(windowFrame,new WindowPosition(winX, winY, winWidth, winHeight),null);
					recycleAdapterDialog.setRecycleViewListener(new RecyclerAdapterDialog.RecycleViewListener() {
						@Override
						public void OnDataResult(String adapterName,String viewHolderName,String layoutFileName,boolean listenerState,List<TableItem> tableDatas) {
							//启动代码创建线程
							cachedThreadPool.execute(new RecycleViewThread(adapterName, viewHolderName, layoutFileName, listenerState, tableDatas));
							recycleAdapterDialog.dispose();
						}
					});
				}
				recycleAdapterDialog.setLocation(new WindowPosition(winX, winY, winWidth, winHeight));
				recycleAdapterDialog.initDatasets();
				recycleAdapterDialog.setVisible(true);
				break;
			case 300:
				cachedThreadPool.execute(new LoadTemplateThread(Resources.getTemplateResource("MainActivity.java")));
				break;
			case 301:
				cachedThreadPool.execute(new LoadTemplateThread(Resources.getTemplateResource("MainFragement.java")));
				break;
			case 400:
				if(findViewByIdDialog==null) {
					findViewByIdDialog=new FindViewByIdDialog(windowFrame,new WindowPosition(winX, winY, winWidth, winHeight));
					findViewByIdDialog.setDialogResultListener(new FindViewByIdDialog.FindViewListener() {
						@Override
						public void onViewIdSetted(String viewParent,List<ViewAndId> items, boolean varannounce) {
							cachedThreadPool.execute(new ViewIdThread(viewParent,items,varannounce));
							findViewByIdDialog.dispose();
						}
					});
				}
				findViewByIdDialog.setLocation(new WindowPosition(winX, winY, winWidth, winHeight));
				findViewByIdDialog.initDatasets();
				findViewByIdDialog.setVisible(true);
				break;
			case 401:
				XML2IdParser parser = new XML2IdParser(codeArea.getText());
				boolean state = parser.start();
				if(state) {
					//结果集合为空提示
					if(parser.getResults().isEmpty()) {
						showToast("XML文件内容无效！");
						return;
					}
					cachedThreadPool.execute(new XML2IdThread(parser.getResults(), codeArea));
				}else {
					showToast("解析失败，请检查XML文件！");
				}
				break;
			default:
				break;
		}
	}

	/**
	 * 弹出提示信息
	 * */
	private void showToast(String msg) {
		toast.setLoaction(new WindowPosition(winX, winY, winWidth, winHeight));
		toast.setText(msg);
		toast.display();
	}
	
	/**
	 * 展开所有功能项目
	 * */
	private void expandAll(JTree tree, TreePath parent, boolean expand) {
	     TreeNode node = (TreeNode) parent.getLastPathComponent();
	         if (node.getChildCount() > 0) {
	              for (Enumeration<?> e = node.children(); e.hasMoreElements();) {
	                    TreeNode n = (TreeNode) e.nextElement();
	                    TreePath path = parent.pathByAddingChild(n);
	                     expandAll(tree, path, expand);
	                }
	          }
	      if (expand) {
	         tree.expandPath(parent);
	      } else {
	          tree.collapsePath(parent);
	     }
	} 
	
	//解析编辑框内的数据,提取出可创建javabean的内容
	//规则javabean私有变量以private开头;号结尾,且在一行
	private List<Variable> paraseBeanCode() {
		BufferedReader buffere=null;
		String editArea = null;
		String line=null;
		List<Variable> variables = new ArrayList<Variable>(16);
		editArea=codeArea.getText().toString();
		//对codeArea代码区进行判空
		if(!Tools.strOK(editArea)) {
			return null;
		}
		try {
			buffere = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(editArea.getBytes()),"utf-8"));
			while ((line = buffere.readLine()) != null) {
				//去掉字符串首尾的空格
				line=line.trim();
				if(line.startsWith("private") && line.endsWith(";")) {
					//如果私有变量声明赋值,将其从等号开始(包括等号)赋值信息去除
					if(line.contains("=")) {
						line=line.substring(0, line.indexOf("="));
					}
					//未赋值的语句去除末尾的分号
					if(line.contains(";")) {
						line=line.replace(";", "");
					}
					//使用语法中的空格分断文本
					String[] array=line.split(" ");
					if(array!=null && array.length==3) {
						variables.add(new Variable(array[1],array[2]));
					}
				}
				line=null;
			}
			buffere.close();
			buffere=null;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
            try{
                if(buffere!=null){
                	buffere.close();
                	buffere=null;
                }
            }catch(IOException e){
                e.printStackTrace();
            }
        }
		return variables;
	}
	
	private String parserFileName(String datas) {
		BufferedReader buffer = null;
		String fileName=null;
		try {
			buffer = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(datas.getBytes()),"utf-8"));
			String cache,tmp;
			while((cache=buffer.readLine())!=null) {
				if(cache.startsWith("public class ")) {
					tmp = cache.substring("public class ".length(), cache.length());
					fileName = tmp.substring(0,tmp.indexOf(" "));
					tmp=null;
					cache=null;
					break;
				}
				cache=null;
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fileName;
	}
	
	/**
	 * 选择保存文件目录弹窗
	 * */
	private JFileChooser fileChooser;
	private FileNameExtensionFilter fileFilter;
	private JLabel fileDialogTitle;
	private void showFileSaverDialog(String fileName,String datas) {
		ConfigUtils config=new ConfigUtils();
		SettingItem item=config.readConfig();
		if(fileName==null || datas==null) {
			return;
		}
		if(fileChooser==null) {
			fileChooser=new JFileChooser();
			fileDialogTitle=new JLabel();
			fileFilter =new FileNameExtensionFilter("java源码(*.java)", "java");
			fileChooser.setFileFilter(fileFilter);
		}
		if(fileName.endsWith(".java")) {
			fileChooser.setSelectedFile(new File(fileName));
		}else {
			fileChooser.setSelectedFile(new File(fileName+".java"));
		}
		int option =fileChooser.showDialog(fileDialogTitle, "存储文件");
		File save=fileChooser.getSelectedFile();
		if(option==JFileChooser.APPROVE_OPTION) {
			String fname = fileChooser.getName(save);
			if(!fname.endsWith(".java")){
				save=null;
				save=new File(fileChooser.getCurrentDirectory(),fname+".java");
			}
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(save);
				fos.write(datas.getBytes(item.getEncodeType()));
				fos.flush();
				fos.close();
				fos=null;
			} catch (UnsupportedEncodingException | FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}finally {
				item=null;
				config=null;
				if(fos!=null ) {
					try {
						fos.flush();
						fos.close();
						fos=null;
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	private class LoadTemplateThread extends Thread{
		private String filePath;
		public LoadTemplateThread(String filePath) {
			this.filePath=filePath;
		}
		@Override
		public void run() {
			BufferedReader buffer = null;
			char[] cache=new char[32];
			StringBuffer content=new StringBuffer(2048);
			try {
				buffer = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filePath)),"utf-8"));
				while(true) {
					if(buffer.read(cache, 0, 32)!=-1) {
						content.append(cache, 0, 32);
					}else {
						break;
					}
				}
				buffer.close();
				buffer=null;
				SwingUtilities.invokeLater(new  Runnable(){
					public void run() {
						codeArea.setText("");
						codeArea.append(content.toString());	
						content.delete(0, content.length());
						codeArea.setCaretPosition(0);
					}
				});
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}finally {
				if(buffer!=null) {
					try {
						buffer.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	//创建RecycleView代码线程
	private class RecycleViewThread extends Thread {
		private String adapterName,viewHolderName,layoutFileName;
		private boolean listenerState;
		private List<TableItem> tableDatas;
		public RecycleViewThread(String adapterName,String viewHolderName,String layoutFileName,boolean listenerState,List<TableItem> tableDatas) {
			this.adapterName=adapterName;
			this.viewHolderName=viewHolderName;
			this.viewHolderName=viewHolderName;
			this.layoutFileName=layoutFileName;
			this.listenerState=listenerState;
			this.tableDatas=tableDatas;
		}
		
		@Override
		public void run() {
			RecycleAdapterCreater creater=new RecycleAdapterCreater(adapterName,viewHolderName,layoutFileName,listenerState);
			List<Variable> variables=new ArrayList<Variable>(16);
			List<ViewAndId> viewAndIds=new ArrayList<ViewAndId>(16);
			int len=tableDatas.size();
			for(int i=0;i<len;++i) {
				TableItem item=tableDatas.get(i);
				if(item.getParamsType()==ParamsJPanel.TYPE_VAR) {
					variables.add(new Variable(item.getVarType(),item.getVarName()));
				}else if(item.getParamsType()==ParamsJPanel.TYPE_VIEW) {
					viewAndIds.add(new ViewAndId(item.getVarType(),item.getVarName(),item.getResId()));
				}
			}
			SwingUtilities.invokeLater(new  Runnable(){
				public void run() {
					codeArea.setText("");
					codeArea.append(creater.creat(variables, viewAndIds));
					codeArea.setCaretPosition(0);
					viewAndIds.clear();
					variables.clear();
					tableDatas.clear();
				}
			});
		}
	}
	
	//创建ListViewThread代码线程
	private class ListViewThread extends Thread {
		private String adapterName,viewHolderName,layoutFileName;
		private List<TableItem> tableDatas;
		public ListViewThread(String adapterName,String viewHolderName,String layoutFileName,List<TableItem> tableDatas) {
			this.adapterName=adapterName;
			this.viewHolderName=viewHolderName;
			this.viewHolderName=viewHolderName;
			this.layoutFileName=layoutFileName;
			this.tableDatas=tableDatas;
		}
		@Override
		public void run() {
			ListAdapterCreater creater=new ListAdapterCreater(adapterName,viewHolderName,layoutFileName);
			List<Variable> variables=new ArrayList<Variable>(16);
			List<ViewAndId> viewAndIds=new ArrayList<ViewAndId>(16);
			int len=tableDatas.size();
			for(int i=0;i<len;++i) {
				TableItem item=tableDatas.get(i);
				if(item.getParamsType()==ParamsJPanel.TYPE_VAR) {
					variables.add(new Variable(item.getVarType(),item.getVarName()));
				}else if(item.getParamsType()==ParamsJPanel.TYPE_VIEW) {
					viewAndIds.add(new ViewAndId(item.getVarType(),item.getVarName(),item.getResId()));
				}
			}
			SwingUtilities.invokeLater(new  Runnable(){
				public void run() {
					codeArea.setText("");
					codeArea.append(creater.creat(variables, viewAndIds));
					codeArea.setCaretPosition(0);
					viewAndIds.clear();
					variables.clear();
					tableDatas.clear();
				}
			});
		}
	}
	
	//JavaBean代码创建线程
	private class BeanThread extends Thread{
		private String mPack,mName;
		private List<Variable> mItems;
		private List<Boolean> mOptions;
		private BeanCreater creater=null;
		public BeanThread(String pack, String name, List<Variable> items, List<Boolean> options) {
			this.mPack=pack;
			this.mName=name;
			this.mItems=items;
			this.mOptions=options;
		}
		@Override
		public void interrupt() {
			super.interrupt();
		}
		@Override
		public void run() {
			super.run();
			creater=new BeanCreater(mPack, mName, mItems, mOptions);
			SwingUtilities.invokeLater(new  Runnable(){
				public void run() {
					codeArea.setText("");
					codeArea.append(creater.creat(mItems));
					codeArea.setCaretPosition(0);
					creater=null;
				}
			});
		}
		@Override
		public synchronized void start() {
			super.start();
		}
	}
	
	//创建findViewById代码线程
	private class ViewIdThread extends Thread{
		private List<ViewAndId> mItems;
		private boolean mAnnounce;
		private String veiwParent=null;
		private FindViewCreater creater =null;
		public ViewIdThread(String veiwParent,List<ViewAndId> items, boolean varannounce) {
			this.veiwParent=veiwParent;
			this.mItems=items;
			this.mAnnounce=varannounce;
		}
		@Override
		public boolean isInterrupted() {
			return super.isInterrupted();
		}

		@Override
		public void run() {
			super.run();
			if(mAnnounce) {
				creater=new FindViewCreater(veiwParent,true);
			}else {
				creater=new FindViewCreater();
			}
			SwingUtilities.invokeLater(new  Runnable(){
				public void run() {
					codeArea.setText("");
					codeArea.append(creater.creat(mItems));
					codeArea.setCaretPosition(0);
				}
			});
		}
		@Override
		public synchronized void start() {
			super.start();
		}
	}
}
