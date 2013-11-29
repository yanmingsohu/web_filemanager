/* CatfoOD 2013 yanming-sohu@sohu.com */

package jym.file.trans;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jym.file.SecurityManager;
import jym.sim.util.ResourceLoader;
import jym.sim.util.Tools;


public class Download extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final Map<UUID, File> filePool = new HashMap<UUID, File>();

	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		SecurityManager.check();
		
		resp.setCharacterEncoding("UTF-8");
		String id = req.getParameter("id");
		
		if (!Tools.isNull(id)) {
			UUID uid = UUID.fromString(id);
			File file = filePool.remove(uid);

			if (file != null) {
				resp.reset();
				resp.setContentType("application/octet-stream; charset=utf-8");
				resp.setContentLength((int) file.length());
				resp.setHeader("Content-Disposition", 
						"filename=" + URLEncoder.encode(file.getName(), "UTF-8"));
				
				FileInputStream in = new FileInputStream(file);
				try {
					OutputStream out = resp.getOutputStream();
					ResourceLoader.writeOut(in, out);
				} finally {
					in.close();
				}
			}
		}
	}

	public static UUID putFile(File file) {
		UUID u = UUID.randomUUID();
		filePool.put(u, file);
		return u;
	}
}
