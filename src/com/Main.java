package com;

import java.awt.AWTException;
import java.awt.AlphaComposite;
import java.awt.CheckboxMenuItem;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;
import javax.swing.JOptionPane;

public class Main {
	public static void main(String args[]) {
		final String cod = "OG005397133BR";
		TrayIcon trayIcon = null;
		if (SystemTray.isSupported()) {
		    // get the SystemTray instance
		    SystemTray tray = SystemTray.getSystemTray();
		    // load an image
		    URL url = System.class.getResource("/img/down2.png");

		    //Use it to get the image
		    Image image = Toolkit.getDefaultToolkit().getImage(url);
		    ActionListener listener2 = new ActionListener() {
		        public void actionPerformed(ActionEvent e) {
		        	Object[] passos2 = getEntrega(cod);
		        		java.awt.Toolkit.getDefaultToolkit().beep();
						JOptionPane.showMessageDialog(null,"Ultima Alteração: \n\n" + ((String) passos2[0]).replaceAll("]","\n").replaceAll(":",": "));
		        }
		    };
		    
		    
		    
		    
		    ActionListener listener = new ActionListener() {
		        public void actionPerformed(ActionEvent e) {
		            System.exit(0);
		        }
		    };
		    
		    ActionListener a1 = new ActionListener() {
		        public void actionPerformed(ActionEvent e) {
		           Timer.value = (60 * 1)*1000;
		           
		        }
		    };
		    ActionListener a5 = new ActionListener() {
		        public void actionPerformed(ActionEvent e) {
		           Timer.value = (60 * 5)*1000;
		           
		        }
		    };
		    ActionListener a10 = new ActionListener() {
		        public void actionPerformed(ActionEvent e) {
		           Timer.value = (60 * 10)*1000;
		           
		        }
		    };
		    ActionListener a30 = new ActionListener() {
		        public void actionPerformed(ActionEvent e) {
		           Timer.value = (60 * 30)*1000;
		           
		        }
		    };
		    ActionListener a60 = new ActionListener() {
		        public void actionPerformed(ActionEvent e) {
		           Timer.value = (60 * 60)*1000;
		           
		        }
		    };
		    // create a popup menu
		    PopupMenu popup = new PopupMenu();
		    
		    MenuItem defaultItem5 = new MenuItem(cod);
		    //defaultItem5.setEnabled(false);
		    popup.add(defaultItem5);
		    
		    popup.addSeparator();
		    
		    MenuItem defaultItem2 = new MenuItem("Status");
		    defaultItem2.addActionListener(listener2);
		    popup.add(defaultItem2);
		    
		    PopupMenu popup2 = new PopupMenu("Refresh");
		    
		    CheckboxMenuItem a1min = new CheckboxMenuItem("1 minuto");
		    a1min.addActionListener(a1);
		    popup2.add(a1min);
		    
		    CheckboxMenuItem a5min = new CheckboxMenuItem("5 minutos");
		    a5min.addActionListener(a5);
		    popup2.add(a5min);
		    
		    CheckboxMenuItem a10min = new CheckboxMenuItem("10 minutos");
		    a10min.setState(true);
		    a10min.addActionListener(a10);
		    popup2.add(a10min);
		    
		    CheckboxMenuItem a30min = new CheckboxMenuItem("30 minutos");
		    a30min.addActionListener(a30);
		    popup2.add(a30min);
		    
		    CheckboxMenuItem a60min = new CheckboxMenuItem("1 hora");
		    a60min.addActionListener(a60);
		    popup2.add(a60min);
		    
		    popup.add(popup2);
		    
		    popup.addSeparator();
		    
		    // create menu item for the default action
		    MenuItem defaultItem = new MenuItem("Exit");
		    defaultItem.addActionListener(listener);
		    popup.add(defaultItem);
		    /// ... add other items
		    // construct a TrayIcon
		    trayIcon = new TrayIcon(image, "jCorreios", popup);
		    //trayIcon.displayMessage("aaa", "aaaaa", TrayIcon.MessageType.INFO);
		    // set the TrayIcon properties
		    trayIcon.addActionListener(listener2);
		    // ...
		    // add the tray image
		    try {
		        tray.add(trayIcon);
		    } catch (AWTException e) {
		        System.err.println(e);
		    }
		    // ...
		} else {
		}
		new Thread() {
			public void run() {
				int number = 0;
				
				//primeira rodada
				Object[] passos = getEntrega(cod);
				number = passos.length;
				java.awt.Toolkit.getDefaultToolkit().beep();
				JOptionPane.showMessageDialog(null,cod+"\nIniciado! "+number+" linhas encontradas.");
				
				
				while (true) {
					try {
						Thread.sleep(Timer.value);
						System.out.println("a> "+Timer.value);
						Object[] passos2 = getEntrega(cod);
						if(number != passos2.length){
							java.awt.Toolkit.getDefaultToolkit().beep();
							JOptionPane.showMessageDialog(null,cod+"\nNova Alteração! \n\n" + ((String) passos2[0]).replaceAll("]","\n").replaceAll(":",": "));
						}else {
						}
						number = passos2.length;
					}catch(Exception e) {
						
					}
				}
			}
		}.start();
		
	}
	
	static BufferedImage createResizedCopy(Image originalImage, 
            int scaledWidth, int scaledHeight, 
            boolean preserveAlpha)
    {
        System.out.println("resizing...");
        int imageType = preserveAlpha ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
        BufferedImage scaledBI = new BufferedImage(scaledWidth, scaledHeight, imageType);
        Graphics2D g = scaledBI.createGraphics();
        if (preserveAlpha) {
            g.setComposite(AlphaComposite.Src);
        }
        g.drawImage(originalImage, 0, 0, scaledWidth, scaledHeight, null); 
        g.dispose();
        return scaledBI;
    }
	
	public static Object[] getEntrega(String cod) {
		try {
			
			String url = "https://www2.correios.com.br/sistemas/rastreamento/resultado_semcontent.cfm?";
			URL obj = new URL(url);
			HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

			con.setRequestMethod("POST");
			con.setRequestProperty("User-Agent", "Mozilla/5.0");
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

			String urlParameters = "Objetos="+cod;
			
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();

			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			String a = response.toString();
			
			if(a.contains("Não é possível exibir")) {
				return new String[]{"Código não encontrado!"};
				
			}else {
				String b = a.split("<table class=\"listEvent sro\">")[1];
				String c = b.split("</table>")[0].replaceAll("\\s+", " ");
				
				String infos[] = c.split("<tr> ");
				int n=infos.length-1;
				String[] infosb=new String[n];
				System.arraycopy(infos,1,infosb,0,n);
				ArrayList<String> informacoes = new ArrayList<String>();
				for (int i = 0; i < infosb.length; i++) {
					String temp = "Data:";
					
					
					infosb[i].replace(" </tr>", "");
					
					String data = infosb[i].replaceAll("<td class=\"sroDtEvent\" valign=\"top\"> ", "").split(" <br />")[0];
					
					temp += data + "]Hora:";
					
					String hora = infosb[i].split(" <br /> ")[1].split(" <br /> ")[0];
					
					temp += hora + "]Local:";
					
					String r = infosb[i].split(" <br /> ")[2];
					
					if(r.contains("<label style=\"text-transform:")) {
						String local = r.replaceAll("<label style=\"text-transform:capitalize;\">", "").split("</label>")[0].replace("&nbsp;/&nbsp;", " / ");
						temp += local + "]Mensagem:";
						
					}else {
						String local = r.split("<br /> ")[0];
						temp += local + "]Mensagem:";
					}
					
					if(r.contains("strong")) {
						String msg = r.split("<strong>")[1].split("</strong>")[0];
						temp += msg;
					}else {
						String msg = r.split("<td class=\"sroLbEvent\"> ")[1].replaceAll("<br /> ", ". ");
						temp += msg;
					}
					
					informacoes.add(temp);
					
				}
				return informacoes.toArray();
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
