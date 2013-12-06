/* CatfoOD 2013 yanming-sohu@sohu.com */

package jym.file;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import jym.sim.util.Tools;


public class Test {

	public static void main(String[] args) {
		SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH);
		
		Tools.pl(format.format(new Date()));
	}

}
