package dfh.action.pub;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;

import dfh.action.SubActionSupport;
import dfh.utils.Constants;

public class MobileValidateImageAction extends SubActionSupport {

	private static final long serialVersionUID = 0xf3f51ffe591730b8L;
	private ByteArrayInputStream inputStream;
	private Integer width;
	private Integer height;
	private Integer wordLength;

//	private static String charsLong = "23456789abcdefghjklmnpqrstuvwxyzABCDEFGHIJKLMNPQRSTUVWXYZ";
	private static String charsLong = "0123456789";
	private static String chars = charsLong;
	private static String[] fontNames = { "Times New Roman", "Arial", "Book antiqua","" };

	public MobileValidateImageAction() {
	}

	@Override
	public String execute() throws Exception {
		try {
			getHttpSession().setAttribute(Constants.SESSION_RANDID, null);
			getResponse().setHeader("Pragma", "No-cache");
			getResponse().setHeader("Cache-Control", "no-cache");
			getResponse().setDateHeader("Expires", 0);
			BufferedImage image = new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);

			Graphics g = image.getGraphics();
			Random random = new Random();

			g.setColor(getRandColor(200, 250));
			g.fillRect(0, 0, width, height);
			g.setColor(getRandColor(160, 200));
			for (int i = 0; i < width; i++) {
				int x = random.nextInt(width);
				int y = random.nextInt(height);
				int xl = random.nextInt(width);
				int yl = random.nextInt(width);
				g.drawLine(x, y, x + xl, y + yl);
			}

			StringBuilder sRand = new StringBuilder();
			int fontSize = (int) (height*0.8);
			int left = (int) ((width*0.8)/wordLength);
			int marginLeft = (int) (width*0.1);
			int top = height-fontSize;
			int marginTop = top*2;

			for (int i = 0; i < wordLength; i++) {
				char rand = chars.charAt(random.nextInt(chars.length()));
				sRand.append(rand);
				
				int x = marginLeft+(left * i)+((random.nextInt(left))-left/3);
				int y = fontSize+random.nextInt(marginTop)-top;
				g.setFont(new Font(fontNames[random.nextInt(3)], Font.ITALIC,fontSize));
				g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
				g.drawString(String.valueOf(rand), x,y);
			}
			g.dispose();
			
			getHttpSession().setAttribute(Constants.SESSION_RANDID, sRand.toString());
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			ImageOutputStream imageOut = ImageIO.createImageOutputStream(output);
			ImageIO.write(image, "JPEG", imageOut);
			imageOut.close();
			ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
			setInputStream(input);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}

	public Integer getHeight() {
		return height;
	}

	public ByteArrayInputStream getInputStream() {
		return inputStream;
	}

	private Color getRandColor(int fc, int bc) {
		Random random = new Random();
		if (fc > 255)
			fc = 255;
		if (bc > 255)
			bc = 255;
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}

	public Integer getWidth() {
		return width;
	}

	public Integer getWordLength() {
		return wordLength;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public void setInputStream(ByteArrayInputStream inputStream) {
		this.inputStream = inputStream;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public void setWordLength(Integer wordLength) {
		this.wordLength = wordLength;
	}
}
