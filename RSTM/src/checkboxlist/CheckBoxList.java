package checkboxlist;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

/**
 * CheckBoxListΪ�Զ��帴ѡ��List���ṩ�˻�ȡѡ��ѡ��ϵķ���
 * @author always
 *
 */
public class CheckBoxList extends JList<Object> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7896533700038454365L;
	
	private CheckBoxItem cbItemSet[];
	private ListModel<Object> cblistmodel;
	
	public CheckBoxList(String Itemset[]){
		Init(Itemset);
	}
	
	private void Init(String Itemset[]){
		this.cblistmodel = new DefaultListModel<Object>();
		this.cbItemSet=new CheckBoxItem[Itemset.length];
		for(int i=0;i<Itemset.length;i++){
			cbItemSet[i]=new CheckBoxItem(Itemset[i],i);
			((DefaultListModel<Object>) cblistmodel).addElement(cbItemSet[i]);
		}
		this.setModel(this.cblistmodel);
		CheckBoxListCellRenderer renderer = new CheckBoxListCellRenderer();
		this.setCellRenderer(renderer);
		this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		CheckBoxListener lst = new CheckBoxListener(this);
		this.addMouseListener(lst);
		this.addKeyListener(lst);
	}
	
	/**
	 * getSelectedItemsIndex()���ڻ�ȡCheckBoxListѡ��ѡ����±꼯��
	 * @return Integer[] ѡ��ѡ���±�����
	 */
	public Integer[] getSelectedItemsIndex(){
		ArrayList<Integer> selectedIndex=new ArrayList<Integer>();
		Integer res[];
		for(int i=0;i<this.cbItemSet.length;i++){
			if(cbItemSet[i].isSelected())
				selectedIndex.add(i);
		}
		res=new Integer[selectedIndex.size()];
		selectedIndex.toArray(res);
		return res;
	}
	
}

/**
 * CheckBoxListCellRendererΪ�Զ���ListCell��Ⱦ����̳���JCheckBox
 * @author always
 *
 */
class CheckBoxListCellRenderer extends JCheckBox implements ListCellRenderer<Object> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4201629995031003632L;
	
	protected static Border cb_noFocusBorder = new EmptyBorder(1, 1, 1, 1);
	
	public CheckBoxListCellRenderer() {
		super();
		setOpaque(true);
		setBorder(cb_noFocusBorder);
	}
	
	public Component getListCellRendererComponent(JList<?> list, Object value,
	int index, boolean isSelected, boolean cellHasFocus) {
		//�ı�����
		setText(value.toString());
		//ǰ�󱳾�����
		setBackground(isSelected ? list.getSelectionBackground() : list
		.getBackground());
		setForeground(isSelected ? list.getSelectionForeground() : list
		.getForeground());
		//�Ƿ�ѡ������
		CheckBoxItem item = (CheckBoxItem) value;
		setSelected(item.isSelected());
		//���弰�߿�����
		setFont(list.getFont());
		setBorder((cellHasFocus) ? UIManager
		.getBorder("List.focusCellHighlightBorder") : cb_noFocusBorder);
		return this;
	}
}

class CheckBoxListener implements MouseListener, KeyListener {
	protected JList<?> cb_list;
	
	public CheckBoxListener(CheckBoxList cbList) {
		cb_list=cbList;
	}
	
	@Override
	public void keyPressed(KeyEvent arg0) {
		if (arg0.getKeyChar() == ' ')
			doCheck();
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getX() < 20)
			doCheck();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
	}
	
	/**
	 * doCheck()Ϊ�Զ���ѡ���������
	 */
	protected void doCheck() {
		int index = cb_list.getSelectedIndex();
		if (index < 0)return;
		CheckBoxItem data = (CheckBoxItem) cb_list.getModel().getElementAt(index);
		data.invertSelected();
		cb_list.repaint();
	}
	
}

/**
 * CheckBoxItemΪCheckBoxListѡ����
 * @author always
 *
 */
class CheckBoxItem {
	protected String cbItem_name;
	protected int cbItem_index;
	protected boolean cbItem_selected;
	
	public CheckBoxItem(String name, int index) {
		cbItem_name = name;
		cbItem_index = index;
		cbItem_selected = false;
	}
	
	public String getName() {
		return cbItem_name;
	}
	
	public int getIndex() {
		return cbItem_index;
	}
	
	public void setSelected(boolean selected) {
		cbItem_selected = selected;
	}
	
	public void invertSelected() {
		cbItem_selected = !cbItem_selected;
	}
	
	public boolean isSelected() {
		return cbItem_selected;
	}
	
	public String toString() {
		return cbItem_name;
	}
}