package com.github.vortexellauncher.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import com.github.vortexellauncher.pack.Modpack;

public class ModpackCellRenderer extends JPanel implements ListCellRenderer {
	private static final long serialVersionUID = 1L;
	 
	private Image baseImage;
	private Rectangle grBounds = new Rectangle();
	private Rectangle imgBounds = new Rectangle();
	private String packName;
	private boolean isAddNew = false;
	private JList listRef;
	
	public ModpackCellRenderer() {
		setDoubleBuffered(true);
	}
	
	@Override
	public void paint(Graphics g1) {
		Graphics2D g = (Graphics2D)g1;
		g.getClipBounds(grBounds);
		g.setColor(getBackground());
		g.clearRect(0, 0, grBounds.width, grBounds.height);
		if (isAddNew) {
			g.setColor(Color.BLACK);
			int fontHeight = g.getFontMetrics().getHeight();
			drawStringHCentered(g, grBounds.getCenterX(), grBounds.getCenterY() - (fontHeight * 1.5), "Add New");
			drawStringHCentered(g, grBounds.getCenterX(), grBounds.getCenterY() + (fontHeight / 2), "Modpack");
		} else {
			Image scaledImg = rescaleImage(baseImage, listRef.getFixedCellHeight());
			imgBounds.setBounds(0, 0, scaledImg.getWidth(null), scaledImg.getHeight(null));
			centerRect(grBounds, imgBounds);
			imgBounds.y += grBounds.height - imgBounds.height;
			g.drawImage(scaledImg, imgBounds.x, imgBounds.y, null);
			drawStringHCentered(g, grBounds.getCenterX(), 2, packName);
		}
	}
	
	public static int drawStringHCentered(Graphics g, double x, double y, String str) {
		FontMetrics fm = g.getFontMetrics();
		int drawOffset = fm.stringWidth(str) / 2;
		g.drawString(str, (int)(x - drawOffset), (int)y);
		return fm.getAscent() + fm.getMaxDescent() + fm.getLeading() + fm.getLeading();
	}

	@Override
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		listRef = list;
		if (value != null) {
			isAddNew = false;
			Modpack pack = (Modpack)value;
			baseImage = Res.defaultModpackIcon.getImage();
			packName = pack.name;
		} else {
			isAddNew = true;
		}
		return this;
	}
	
	private static Image rescaleImage(Image base, int nheight) {
		if (nheight <= 0)
			nheight = 1;
		return base.getScaledInstance(-1, nheight, Image.SCALE_DEFAULT);
	}
	
	private static void centerRect(Rectangle a, Rectangle b) {
		double diffX = a.getCenterX() - b.getCenterX();
		double diffY = a.getCenterY() - b.getCenterY();
		b.add(diffX, diffY);
	}

}
