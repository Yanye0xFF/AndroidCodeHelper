package view;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JTextPane;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BoxView;

import javax.swing.text.ComponentView;
import javax.swing.text.Element;
import javax.swing.text.IconView;
import javax.swing.text.LabelView;
import javax.swing.text.ParagraphView;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;

public class QEditText extends JTextPane{

	private static final long serialVersionUID = 1L;
	
	public KeyListener listener=null;
	
	private class WarpEditorKit extends StyledEditorKit {
		private static final long serialVersionUID = 1L;
		private ViewFactory defaultFactory = new WarpColumnFactory();
		@Override
		public ViewFactory getViewFactory() {
			return defaultFactory;
		}
	}
	
	private class WarpColumnFactory implements ViewFactory {
		public View create(Element elem) {
			String kind = elem.getName();
			if (kind != null) {
				if (kind.equals(AbstractDocument.ContentElementName)) {
					return new WarpLabelView(elem);
				} else if (kind.equals(AbstractDocument.ParagraphElementName)) {
					return new ParagraphView(elem);
				} else if (kind.equals(AbstractDocument.SectionElementName)) {
					return new BoxView(elem, View.Y_AXIS);
				} else if (kind.equals(StyleConstants.ComponentElementName)) {
					return new ComponentView(elem);
				} else if (kind.equals(StyleConstants.IconElementName)) {
					return new IconView(elem);
				}
			}
			return new LabelView(elem);
		}
	}
	
	private class WarpLabelView extends LabelView {
		public WarpLabelView(Element elem) {
			super(elem);
		}
		@Override
		public float getMinimumSpan(int axis) {
			switch (axis) {
				case View.X_AXIS:
					return 0;
				case View.Y_AXIS:
					return super.getMinimumSpan(axis);
				default:
					throw new IllegalArgumentException("Invalid axis: " + axis);
			}
		}
	}

	public QEditText() {
		super();
		this.setEditorKit(new WarpEditorKit());
		listener=new KeyListener() {
			@Override
			public void keyTyped(KeyEvent event) {
				event.consume();
			}
			@Override
			public void keyReleased(KeyEvent event) {
				event.consume();
			}
			@Override
			public void keyPressed(KeyEvent event) {
				event.consume();
			}
		};
	}
	
	public void  interceptKeyBoardInput(boolean isopen) {
		if(isopen) {
			//拦截信息显示区域键盘输入事件
	    	//让消息区只显示光标，不能输入
			addKeyListener(listener);
		}else {
			this.removeKeyListener(listener);
		}
	}
	
	public void clear() {
		this.setText("");
	}
}