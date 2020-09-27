package dfh.service.interfaces;

public interface NotifyService extends UniversalService {

	String sendEmail(String email, String title, String msg);

	String sendSms(String phoneNo, String msg);

}