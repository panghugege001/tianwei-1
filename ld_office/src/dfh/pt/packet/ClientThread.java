package dfh.pt.packet;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.hibernate.LockMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import dfh.model.Proposal;
import dfh.model.PtProfit;
import dfh.model.Users;
import dfh.model.enums.ProposalType;
import dfh.service.interfaces.IGetdateService;
import dfh.utils.DateUtil;
import dfh.utils.NTUtils;

public class ClientThread extends Thread {
	private static DataOutputStream socketOut;
	private static ByteArrayOutputStream commandBuffer;
	private static ApplicationContext springCtx;
	private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public ClientThread(ApplicationContext springCtx) {
		ClientThread.springCtx = springCtx;
	}

	public void run() {
		try {
			commandBuffer = new ByteArrayOutputStream();
			String _url = "api.totemcasino.biz";
			Socket socket = new Socket(_url, 6778);
			System.out.println("connected");
			DataInputStream socketIn = new DataInputStream(socket.getInputStream());
			socketOut = new DataOutputStream(socket.getOutputStream());
			Packet p = new Packet(Command.LOGIN);
			p.add("283r4h2reh2f2828rher");
			p.add("4B9DECC8107D75CF5786188491E9886F");
			send(p.build());
			int byteIn = socketIn.read();
			boolean needListen = true;

			List<Byte> bytes = new ArrayList<Byte>();
			while (needListen && byteIn != -1) {
				bytes.add((byte) byteIn);
				while (socketIn.available() > 0) {
					byteIn = socketIn.read();
					if (byteIn == -1) {
						break;
					}

					bytes.add((byte) byteIn);
				}

				if (bytes.size() > 0) {
					onMessageReceived(bytes);
					bytes.clear();
				}
				byteIn = socketIn.read();

			}
			System.out.println("closed");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ClientThread clientThread = new ClientThread(springCtx);
			clientThread.start();
		}
	}

	private static void send(byte[] b) {
		try {
			socketOut.write(b);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void onMessageReceived(List<Byte> bytes) {
		try {
			for (Byte b : bytes) {
				if (b == (byte) 254) {
					byte[] _b = commandBuffer.toByteArray();
					commandBuffer.reset();
					Packet p = new Packet(_b);
					System.out.println("code = " + p.code);
					if (p.code == 4) {
						System.out.println("code = " + p.code);
						// 用户ID
						Integer userId = Integer.parseInt(p.list.get(0));
						// 类型
						Integer type = Integer.parseInt(p.list.get(3));
						// 投注额
						Double betCredit = 0.00;
						if (type == 0) {
							betCredit = Double.parseDouble(p.list.get(6)) * Double.parseDouble(p.list.get(7)) / 100.0;
						}
						// 赔付
						Double payout = Double.parseDouble(p.list.get(2)) / 100.0;
						// 输赢
						Double amount = betCredit - payout;
						// 游戏时间
						Date dateTime = format.parse(p.list.get(4));
						// 游戏时间加7小时
						Calendar s = Calendar.getInstance();
						s.setTime(dateTime);
						s.add(Calendar.HOUR_OF_DAY, 7);
						dateTime = s.getTime();

						// 获取开始时间
						Date startTime = null;
						Date endTime = null;

						SimpleDateFormat sdfHHH = new SimpleDateFormat("HH");
						Integer hh = Integer.parseInt(sdfHHH.format(dateTime));
						if (hh < 12) {
							Calendar cals = Calendar.getInstance();
							cals.setTime(dateTime);
							cals.set(Calendar.HOUR_OF_DAY, 12);
							cals.set(Calendar.MINUTE, 0);
							cals.set(Calendar.SECOND, 0);
							endTime = cals.getTime();

							cals.add(Calendar.DAY_OF_MONTH, -1); // 减1天
							cals.set(Calendar.HOUR_OF_DAY, 12);
							cals.set(Calendar.MINUTE, 0);
							cals.set(Calendar.SECOND, 0);
							startTime = cals.getTime();
						} else {
							Calendar cals = Calendar.getInstance();
							cals.setTime(dateTime);
							cals.set(Calendar.HOUR_OF_DAY, 12);
							cals.set(Calendar.MINUTE, 0);
							cals.set(Calendar.SECOND, 0);
							startTime = cals.getTime();

							cals.add(Calendar.DAY_OF_MONTH, 1); // 加1天
							cals.set(Calendar.HOUR_OF_DAY, 12);
							cals.set(Calendar.MINUTE, 0);
							cals.set(Calendar.SECOND, 0);
							endTime = cals.getTime();
						}
						System.out.println("**********" + userId + "***********" + betCredit + "**********" + payout + "*****************" + amount + "*********");
						
					} 
				}else {
					commandBuffer.write(b);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}