/* CatfoOD 2013 yanming-sohu@sohu.com */

package jym.file;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class ImageServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("image/jpeg");
        resp.setHeader("Pragma", "No-cache");
        resp.setHeader("Cache-Control", "no-cache");
        resp.setDateHeader("Expire", 0);
        
        try {
            getRandcode(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
    
    /** 验证码总是小写 */
	public static final String RANDOMCODEKEY = "RANDOMVALIDATECODEKEY";
	private Random random = new Random();
	private String randString = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

	private int width = 120;
	private int height = 26;
	private int lineSize = 60;
	private int stringNum = 6;

	/*
	 * 获得字体
	 */
	private Font getFont() {
		return new Font("Fixedsys", Font.CENTER_BASELINE, 18);
	}

	/*
	 * 获得颜色
	 */
	private Color getRandColor(int fc, int bc) {
		if (fc > 255)
			fc = 255;
		if (bc > 255)
			bc = 255;
		int r = fc + random.nextInt(bc - fc - 16);
		int g = fc + random.nextInt(bc - fc - 14);
		int b = fc + random.nextInt(bc - fc - 18);
		return new Color(r, g, b);
	}

	/**
	 * 生成随机图片
	 */
	public String getRandcode(HttpServletRequest request,
			HttpServletResponse response) {
		
		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_BGR);
		Graphics g = image.getGraphics();
		g.fillRect(0, 0, width, height);
		g.setFont(new Font("Times New Roman", Font.ROMAN_BASELINE, 18));
		g.setColor(getRandColor(110, 133));
		
		// 绘制干扰线
		for (int i = 0; i <= lineSize; i++) {
			drowLine(g);
		}
		
		// 绘制随机字符
		StringBuilder buff = new StringBuilder();
		for (int i = 1; i <= stringNum; i++) {
			drowString(g, buff, i);
		}
		
		String randomString = buff.toString().toLowerCase();
		HttpSession session = request.getSession();
		session.setAttribute(RANDOMCODEKEY, randomString);
		g.dispose();
		
		try {
			ImageIO.write(image, "JPEG", response.getOutputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return randomString;
	}

	/*
	 * 绘制字符串
	 */
	private void drowString(Graphics g, StringBuilder randomString, int i) {
		g.setFont(getFont());
		g.setColor(new Color(random.nextInt(101), random.nextInt(111), random.nextInt(121)));
		
		String rand = getRandomString(random.nextInt(randString.length()));
		randomString.append(rand);
		
		g.translate(random.nextInt(3), random.nextInt(3));
		g.drawString(rand, 13 * i, 16);
	}

	/*
	 * 绘制干扰线
	 */
	private void drowLine(Graphics g) {
		int x = random.nextInt(width);
		int y = random.nextInt(height);
		int xl = random.nextInt(13);
		int yl = random.nextInt(15);
		g.drawLine(x, y, x + xl, y + yl);
	}

	/*
	 * 获取随机的字符
	 */
	public String getRandomString(int num) {
		return String.valueOf(randString.charAt(num));
	}
}