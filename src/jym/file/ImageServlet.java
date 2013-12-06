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
        Util.donotCache(resp);
        getRandcode(req, resp);
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
	private int lineSize = 160;
	private int stringNum = 6;

	
	private Font getFont() {
		return new Font("Fixedsys", Font.CENTER_BASELINE, 18);
	}

	public String getRandcode(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		
		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_BGR);
		Graphics g = image.getGraphics();
		g.fillRect(0, 0, width, height);
		g.setFont(new Font("Times New Roman", Font.ROMAN_BASELINE, 18));
		
		drowLine(g);
		
		char[] buff = new char[stringNum];
		drowString(g, buff);
		
		String randomString = new String(buff).toLowerCase();
		HttpSession session = request.getSession();
		session.setAttribute(RANDOMCODEKEY, randomString);
		g.dispose();
		
		ImageIO.write(image, "JPEG", response.getOutputStream());
		return randomString;
	}

	/*
	 * 绘制字符串
	 */
	private void drowString(Graphics g, char[] randomString) {
		for (int i = 1; i <= stringNum; i++) {
			randomString[i-1] = randString.charAt( 
					random.nextInt(randString.length()) );
			
			g.setFont(getFont());
			g.setColor(new Color(random.nextInt(101), 
					random.nextInt(111), random.nextInt(121)));
			g.translate(random.nextInt(3), random.nextInt(3));
			g.drawChars(randomString, i-1, 1, 13 * i, 16);
		}
	}

	/*
	 * 绘制干扰线
	 */
	private void drowLine(Graphics g) {
		for (int i = 0; i < lineSize; i++) {
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			int xl = random.nextInt(13);
			int yl = random.nextInt(15);
			
			g.setColor(new Color(random.nextInt(150)+100, 
					random.nextInt(55)+130, random.nextInt(200)+50));
			g.drawLine(x, y, x + xl,   y + yl  );
			g.drawLine(x, y, x + xl+1, y + yl  );
			g.drawLine(x, y, x + xl,   y + yl+1);
			g.drawLine(x, y, x + xl+i, y + yl+1);
		}
	}

}