/* CatfoOD 2013 yanming-sohu@sohu.com */

package jym.file;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import jym.sim.validator.VerifyMessage.Msg;


public abstract class FaceBase implements Serializable {

	private static final long serialVersionUID = 1L;

	
	public static MessageSender getSender() {
		return new MessageSender();
	}
	
	public static class MessageSender {
		FacesContext fc = FacesContext.getCurrentInstance();
		String id = null;
		
		MessageSender() {
			fc = FacesContext.getCurrentInstance();
			if (fc == null) {
				throw new NullPointerException();
			}
		}
		
		public void addMessage(String msg) {
			fc.addMessage(id, new FacesMessage(msg));
		}
		
		public void addMessage(Msg msg) {
			addMessage(msg.getMessage());
		}
		
		public void addMessage(Throwable t) {
			addMessage(t.getMessage());
		}
		
		public void setClientId(String id) {
			this.id = id;
		}
	}
}
