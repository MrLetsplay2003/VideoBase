package me.mrletsplay.videobase.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.List;

import me.mrletsplay.mrcore.misc.StringUtils;

public class ThumbnailUtil {

	public static final int
		THUMBNAIL_WIDTH = 720,
		THUMBNAIL_HEIGHT = 480,
		COLLECTION_THUMBNAIL_WIDTH = 480,
		COLLECTION_THUMBNAIL_HEIGHT = 720;

	public static final double
		THUMBNAIL_RATIO = (double) THUMBNAIL_WIDTH / THUMBNAIL_HEIGHT,
		COLLECTION_THUMBNAIL_RATIO = (double) COLLECTION_THUMBNAIL_WIDTH / COLLECTION_THUMBNAIL_HEIGHT;

	public static BufferedImage generateThumbnail(String episode, String text, BufferedImage background) {
		BufferedImage thumb = new BufferedImage(THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT, BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D g2d = thumb.createGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		float ratio = (float) background.getWidth() / background.getHeight();
		if(ratio < THUMBNAIL_WIDTH / THUMBNAIL_HEIGHT) {
			int newW = THUMBNAIL_WIDTH;
			int newH = (int) ((double) background.getHeight() / background.getWidth() * newW);

			int overhang = newH - THUMBNAIL_HEIGHT;

			g2d.drawImage(background, 0, -overhang / 2, newW, newH, null);
		}

		g2d.setColor(new Color(0f, 0f, 0f, 0.5f));
		g2d.fillRect(0, 0, THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT);

		g2d.setColor(Color.WHITE);
		g2d.setFont(g2d.getFont().deriveFont(Font.BOLD, 200f));

		String draw = episode;
		Rectangle2D r = g2d.getFontMetrics().getStringBounds(draw, g2d);
		g2d.drawString(draw, (int) (THUMBNAIL_WIDTH / 2 - r.getCenterX()), (int) ((text != null ? THUMBNAIL_HEIGHT / 2 - 150 : THUMBNAIL_HEIGHT / 2) - r.getCenterY()));

		if(text != null) {
			List<String> lines = StringUtils.wrapString(text, 15);

			g2d.setColor(Color.WHITE);
			g2d.setFont(g2d.getFont().deriveFont(Font.BOLD, 60f));
			for(int i = 0; i < lines.size(); i++) {
				String draw2 = lines.get(i);
				Rectangle2D r2 = g2d.getFontMetrics().getStringBounds(draw2, g2d);
				g2d.drawString(draw2, (int) (THUMBNAIL_WIDTH / 2 - r2.getCenterX()), THUMBNAIL_HEIGHT / 2 + 150 + i * 65);
			}
		}

		return thumb;
	}

	private static BufferedImage rescaleThumbnail(BufferedImage thumbnail, int targetWidth, int targetHeight) {
		double ratio = (double) thumbnail.getWidth() / thumbnail.getHeight();

		int width, height, edgeX, edgeY;
		if(ratio > COLLECTION_THUMBNAIL_RATIO) {
			width = targetWidth;
			height = (int) (1 / ratio * width);
			edgeY = targetHeight - height;
			edgeX = 0;
		}else {
			height = targetHeight;
			width = (int) (ratio * height);
			edgeX = targetWidth - width;
			edgeY = 0;
		}

		BufferedImage image = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D g2d = image.createGraphics();
		g2d.drawImage(thumbnail, edgeX / 2, edgeY / 2, width, height, null);
		return image;
	}

	public static BufferedImage rescaleVideoThumbnail(BufferedImage thumbnail) {
		return rescaleThumbnail(thumbnail, THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT);
	}

	public static BufferedImage rescaleCollectionThumbnail(BufferedImage thumbnail) {
		return rescaleThumbnail(thumbnail, COLLECTION_THUMBNAIL_WIDTH, COLLECTION_THUMBNAIL_HEIGHT);
	}

}
