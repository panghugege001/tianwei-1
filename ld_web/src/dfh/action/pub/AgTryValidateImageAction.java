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

public class AgTryValidateImageAction extends SubActionSupport {

	private static final long serialVersionUID = 0xf3f51ffe591730b8L;
	private ByteArrayInputStream inputStream;
	private Integer width;
	private Integer height;
	private Integer wordLength;
	private String r;

	public void setR(String r) {
		this.r = r;
	}

	public AgTryValidateImageAction() {
		width = Integer.valueOf(85);
		height = Integer.valueOf(20);
		wordLength = Integer.valueOf(4);
	}

	@Override
	public String execute() throws Exception {
		
		getHttpSession().setAttribute(Constants.SESSION_AG_TRY_RANDID,null);
		
		BufferedImage image = new BufferedImage(width.intValue(), height.intValue(), 1);
		Graphics g = image.getGraphics();
		Random random = new Random(System.currentTimeMillis());
		g.setColor(getRandColor(200, 250));
		g.fillRect(0, 0, width.intValue(), height.intValue());
		g.setFont(new Font("Times New Roman", 0, 18));
		g.setColor(getRandColor(160, 200));
		for (int i = 0; i < 155; i++) {
			int x = random.nextInt(width.intValue());
			int y = random.nextInt(height.intValue());
			int xl = random.nextInt(12);
			int yl = random.nextInt(12);
			g.drawLine(x, y, x + xl, y + yl);
		}

		String sRand = "";
		for (int i = 0; i < wordLength.intValue(); i++) {
			String rand = (new StringBuilder(String.valueOf(random.nextInt(10)))).toString();
			sRand = (new StringBuilder(String.valueOf(sRand))).append(rand).toString();
			g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
			g.drawString(rand, 13 * i + 4, 16);
		}
//		System.out.println((new StringBuilder("validate code:")).append(sRand).toString());
		getHttpSession().setAttribute(Constants.SESSION_AG_TRY_RANDID, sRand);
		g.dispose();
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		ImageOutputStream imageOut = ImageIO.createImageOutputStream(output);
		ImageIO.write(image, "JPEG", imageOut);
		imageOut.close();
		ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
		setInputStream(input);
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
