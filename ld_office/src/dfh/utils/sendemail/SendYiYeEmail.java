package dfh.utils.sendemail;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import dfh.action.office.AjaxQueryAction;
import dfh.action.vo.UserStatusVO;
import dfh.model.notdb.ReturnBean;
import dfh.utils.AESUtil;
import dfh.utils.Constants;
import dfh.utils.DateUtil;
import dfh.utils.Page;
import dfh.utils.PagenationUtil;
import dfh.utils.StringUtil;
import _43._0._168._192.mailwebservice.mailwebservice_asmx.ArrayOfContactInfoDTO;
import _43._0._168._192.mailwebservice.mailwebservice_asmx.ArrayOfEaseyeMessageReceiveDTO;
import _43._0._168._192.mailwebservice.mailwebservice_asmx.ContactFilter;
import _43._0._168._192.mailwebservice.mailwebservice_asmx.EaseyeDetailedResultReturnDTO;
import _43._0._168._192.mailwebservice.mailwebservice_asmx.EaseyeGroupDTO;
import _43._0._168._192.mailwebservice.mailwebservice_asmx.EaseyeMessageReceiveDTO;
import _43._0._168._192.mailwebservice.mailwebservice_asmx.EaseyeMessageTemplateDTO;
import _43._0._168._192.mailwebservice.mailwebservice_asmx.EaseyeSendOptionDTO;
import _43._0._168._192.mailwebservice.mailwebservice_asmx.EaseyeUserAccountDTO;
import _43._0._168._192.mailwebservice.mailwebservice_asmx.GetContactListDocument;
import _43._0._168._192.mailwebservice.mailwebservice_asmx.GetContactListResponseDocument;
import _43._0._168._192.mailwebservice.mailwebservice_asmx.ImportContactByGroupDetailedDocument;
import _43._0._168._192.mailwebservice.mailwebservice_asmx.ImportContactByGroupDetailedResponseDocument;
import _43._0._168._192.mailwebservice.mailwebservice_asmx.MailWebServiceStub;
import _43._0._168._192.mailwebservice.mailwebservice_asmx.SendMailListRequestByGroupDocument;
import _43._0._168._192.mailwebservice.mailwebservice_asmx.UpdateTemplateDocument;
import _43._0._168._192.mailwebservice.mailwebservice_asmx.UpdateTemplateResponseDocument;
import _43._0._168._192.mailwebservice.mailwebservice_asmx.GetContactListDocument.GetContactList;
import _43._0._168._192.mailwebservice.mailwebservice_asmx.ImportContactByGroupDetailedDocument.ImportContactByGroupDetailed;
import _43._0._168._192.mailwebservice.mailwebservice_asmx.SendMailListRequestByGroupDocument.SendMailListRequestByGroup;
import _43._0._168._192.mailwebservice.mailwebservice_asmx.UpdateTemplateDocument.UpdateTemplate;
import _43._0._168._192.mailwebservice.mailwebservice_asmx.SendMailListRequestByGroupResponseDocument;

public class SendYiYeEmail {
	private static Logger log = Logger.getLogger(SendYiYeEmail.class);

	public static String YIYE_EMAIL="2728414021@qq.com";
	public static String YIYE_PASSWORD="admin5285705";   // easeye Password
	public static String YIYE_EXTERNALEMAIL="635757605@qq.com";      // easeye ID
	public static String YIYE_EXTERNALPASSWORD="admin5285705";   // easeye Password
	public static String SUBSCRIPTIONEMAIL="ivan88";             //订阅 easeye ID
	public static String SUBSCRIPTIONPASSWORD="aa888888";         //订阅  easeye Password
	public static String TEMPLATENAME="ld";                  //模板= 当前产品名字  (easeye 后台 查询对应)
	public static String GROUPNAME="ld";                     //联系组= 当前产品名字  (easeye 后台 查询对应) 统一
	public static String SENDEREMAIL="";
//	public static String SENDEREMAIL="mk@mk.yahu53.com";//主账号  //mk@mk.youle338.com 子账号
	public static String SENDEREMAILNAME="龙都娱乐";
	public static Integer SUCCESSCODE=200; 
	public static Integer EXCEPTION=500; 
	private static String subscriptionGroup="龙都娱乐";            //订阅  联系组 名称 

	

	public static ReturnBean sendYiYeMail(String[] email,String subject,String content,String type,String userLogin,String recordFunction){
		try {
			 
			  Integer errormsg=0;
			  String date=new DateUtil().fmtyyyyMMddHHmmss(new Date());
			  
		      MailWebServiceStub mailWebServiceStub = new MailWebServiceStub();
		    
	          EaseyeUserAccountDTO easeyeUserAccountDTO = EaseyeUserAccountDTO.Factory.newInstance();   //yiye 登入账号密码
	          
	          if (StringUtil.equals("INTERNAL", type)) { //已注册 用户
	        	  easeyeUserAccountDTO.setEmail(YIYE_EMAIL);
		  	      easeyeUserAccountDTO.setPassword(YIYE_PASSWORD);	
		  	      SENDEREMAIL="mk@mk.yahu53.com";
			   }else {
				  easeyeUserAccountDTO.setEmail(YIYE_EXTERNALEMAIL);
		  	      easeyeUserAccountDTO.setPassword(YIYE_EXTERNALPASSWORD);	
		  	      SENDEREMAIL="mk@mk.youle338.com";
		       }
	  	     String combineStr="_"+userLogin+"_"+recordFunction+"_"+date;
	  	     
	  	      updateTemplate(mailWebServiceStub, easeyeUserAccountDTO,subject, content,combineStr);           //更新 或者添加 模板 = subject and message
	  	         
	  	      ReturnBean rbean2= importContactByGroupDetailed(mailWebServiceStub, easeyeUserAccountDTO, email,combineStr);  //添加 联系组 
	  	      
	  	      
               if (rbean2.getSuccess().intValue()!=SUCCESSCODE.intValue()) 
            	   return new ReturnBean("用户导入发生错误,可能存在空的Email 或者 被排除的邮件地址 。 错误信息:"+rbean2.getSuccess().intValue());
			    
                  errormsg= sendMailListRequestByGroup(mailWebServiceStub,easeyeUserAccountDTO,combineStr);                   //发送邮件
					         
	  		  if (errormsg==SUCCESSCODE.intValue()) {
	  			    log.info("sendMailListSuccess success");
	  	    	   return new ReturnBean(null,errormsg, "发送成功   ----检测排除邮件"+rbean2.getMsg1());
	  		  }else 
	  			 return new ReturnBean("发生错误:"+errormsg);
				
	  		} catch (Exception e) {
	  			e.printStackTrace();
	  			return new ReturnBean("发送错误:联系技术");
	  		}
	   }
	
         // 订阅邮件群发
		public static ReturnBean sendYiYeMailSubscription(String[] email,String subject,String content,String userLogin,String recordFunction){
				try {
					 
					  Integer errormsg=0;
					  String date=new DateUtil().fmtyyyyMMddHHmmss(new Date());
					  
				      MailWebServiceStub mailWebServiceStub = new MailWebServiceStub();
				    
			          EaseyeUserAccountDTO easeyeUserAccountDTO = EaseyeUserAccountDTO.Factory.newInstance();   //yiye 登入账号密码
			          
			       
			        	  easeyeUserAccountDTO.setEmail(SUBSCRIPTIONEMAIL);
				  	      easeyeUserAccountDTO.setPassword(SUBSCRIPTIONPASSWORD);	
				  	      SENDEREMAIL="mk@mk.yofa3.com";
			  	     
				  	    date="_"+userLogin+"_"+recordFunction+"_"+date;
				  	    
				  	    
			  	        updateTemplate(mailWebServiceStub, easeyeUserAccountDTO,subject, content,date);           //更新 或者添加 模板 = subject and message
			  	         
			  	        ReturnBean rbean2= importContactByGroupSubscriptionDetailed(mailWebServiceStub, easeyeUserAccountDTO, email,date);  //添加 联系组 
			  	      
			  	      
		               if (rbean2.getSuccess().intValue()!=SUCCESSCODE.intValue()) 
		            	   return new ReturnBean("用户导入发生错误,可能存在空的Email 或者 被排除的邮件地址 。 错误信息:"+rbean2.getSuccess().intValue());
					    
		                  errormsg= sendMailListRequestByGroup(mailWebServiceStub,easeyeUserAccountDTO,date);                   //发送邮件
							         
			  		  if (errormsg==SUCCESSCODE.intValue()) {
			  			    log.info("sendMailListSuccess success");
			  	    	   return new ReturnBean(null,errormsg, "发送成功   ----检测排除邮件"+rbean2.getMsg1());
			  		  }else 
			  			 return new ReturnBean("发生错误:"+errormsg);
						
			  		} catch (Exception e) {
			  			e.printStackTrace();
			  			return new ReturnBean("发送错误:联系技术");
			  		}
			   }
		
	//模板更新 标题和内容
	
	public static Integer updateTemplate(MailWebServiceStub mailWebServiceStub,EaseyeUserAccountDTO easeyeUserAccountDTO,String subject ,String content,String date){
	
		  UpdateTemplateResponseDocument updateTemplateResponseDocument;
		  EaseyeMessageTemplateDTO easeyeMessageTemplateDTO=EaseyeMessageTemplateDTO.Factory.newInstance();
		try {
			  easeyeMessageTemplateDTO=EaseyeMessageTemplateDTO.Factory.newInstance();
	          easeyeMessageTemplateDTO.setTemplateName(TEMPLATENAME+date);
	          easeyeMessageTemplateDTO.setSubject(subject);
	          easeyeMessageTemplateDTO.setBody(content);
	          easeyeMessageTemplateDTO.setTrackLink("1");
	          easeyeMessageTemplateDTO.setIsBodyHtml("1");
	          easeyeMessageTemplateDTO.setHasSysBody(true);

	          
	          UpdateTemplate updateTemplate=UpdateTemplate.Factory.newInstance();
	          updateTemplate.setEaseyeMessageTemplateDTO(easeyeMessageTemplateDTO);
	          updateTemplate.setEaseyeUserAccountDTO(easeyeUserAccountDTO);
	          UpdateTemplateDocument updateTemplateDocument=UpdateTemplateDocument.Factory.newInstance();
	        
	          updateTemplateDocument.setUpdateTemplate(updateTemplate);
	  		  updateTemplateResponseDocument=mailWebServiceStub.updateTemplate(updateTemplateDocument);
			
		} catch (Exception e) {
			return 500;
		}
		
		return updateTemplateResponseDocument.getUpdateTemplateResponse().getUpdateTemplateResult().getErrorCode();
	}
	
	
	/**
	 *  导入用户组 Subscription 
	 * 有解密过程
	 * 
	 * @param mailWebServiceStub
	 * @param easeyeUserAccountDTO
	 * @param mails
	 * @param date
	 * @return
	 */

	 public static ReturnBean importContactByGroupSubscriptionDetailed(MailWebServiceStub mailWebServiceStub,EaseyeUserAccountDTO easeyeUserAccountDTO,String [] mails,String date){
		   String excludeMail="";
		   String passMail=null;
		 
		     ImportContactByGroupDetailedResponseDocument importContactByGroupDetailedResponseDocument ;
		     ImportContactByGroupDetailed importContactByGroupDetailed=ImportContactByGroupDetailed.Factory.newInstance();
		     
	    	try {
                
	    	
	  	        EaseyeGroupDTO easeyeGroupDTO=EaseyeGroupDTO.Factory.newInstance();
	  	        easeyeGroupDTO.setGroupName(GROUPNAME+date);
	  	        easeyeGroupDTO.setImportOption(1);
	  	        
	  	      	Pattern pattern = Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");

	  	        for (int i = 0; i < mails.length; i++) {
	  	        	String email=mails[i];
	  	      	if(StringUtils.isNotBlank(email) && !email.contains("@"))
	  	             	email= AESUtil.aesDecrypt(email, AESUtil.KEY);
	  	      	  else 
	  	      	        email=mails[i];

				      Matcher matcher = pattern.matcher(email);
				       if (true == matcher.matches()) {
							if (StringUtil.isEmpty(passMail)) 
								passMail=email;
						    else 
				    			passMail=passMail+","+email;
				           }
		     	         }
	  	        
	  	        
	  	         String [] userMail=StringUtil.split(passMail, ",");

	  	         log.info("SetYiyeMailSubscriptionSuccess:"+userMail.length);
	  	         
	  	         EaseyeMessageReceiveDTO [] messageReceiveList =new EaseyeMessageReceiveDTO[userMail.length];

	  	        for (int i = 0; i < userMail.length; i++) {
						messageReceiveList[i] = EaseyeMessageReceiveDTO.Factory.newInstance();
						messageReceiveList[i].setTo(userMail[i]);
		    	}
	  	        
	  	        importContactByGroupDetailed.setImportName(GROUPNAME+date);
	  	        importContactByGroupDetailed.setEaseyeUserAccountDTO(easeyeUserAccountDTO);
	  	        importContactByGroupDetailed.setEaseyeGroupDTO(easeyeGroupDTO);
	  	        
	  	        ArrayOfEaseyeMessageReceiveDTO arrayOfEaseyeMessageReceiveDTO=ArrayOfEaseyeMessageReceiveDTO.Factory.newInstance();
	  	        arrayOfEaseyeMessageReceiveDTO.setEaseyeMessageReceiveDTOArray(messageReceiveList);
	  	        		
	  	        importContactByGroupDetailed.setEaseyeMessageReceiveDTOArray1(arrayOfEaseyeMessageReceiveDTO);
	  	        
	  	        ImportContactByGroupDetailedDocument importContactByGroupDetailedDocument=ImportContactByGroupDetailedDocument.Factory.newInstance();
	  	        importContactByGroupDetailedDocument.setImportContactByGroupDetailed(importContactByGroupDetailed);
	            
	  	        importContactByGroupDetailedResponseDocument=mailWebServiceStub.importContactByGroupDetailed(importContactByGroupDetailedDocument);

			 } catch (Exception e) {
	              return new ReturnBean(null,EXCEPTION);
	         }    	
	    	
	    	return new ReturnBean(null,importContactByGroupDetailedResponseDocument.getImportContactByGroupDetailedResponse().getImportContactByGroupDetailedResult().getErrorCode(), excludeMail);
	    }
	 
	
	  //导入用户组
	 public static ReturnBean importContactByGroupDetailed(MailWebServiceStub mailWebServiceStub,EaseyeUserAccountDTO easeyeUserAccountDTO,String [] mails,String date){
		   String excludeMail="";
		   String passMail=null;
		 
		     ImportContactByGroupDetailedResponseDocument importContactByGroupDetailedResponseDocument ;
		     ImportContactByGroupDetailed importContactByGroupDetailed=ImportContactByGroupDetailed.Factory.newInstance();
		     
	    	try {

	  	        EaseyeGroupDTO easeyeGroupDTO=EaseyeGroupDTO.Factory.newInstance();
	  	        easeyeGroupDTO.setGroupName(GROUPNAME+date);
	  	        easeyeGroupDTO.setImportOption(1);
	  	        
	  	      	Pattern pattern = Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");

	  	        for (int i = 0; i < mails.length; i++) {
				Matcher matcher = pattern.matcher(mails[i]);
				       if (true == matcher.matches()) {
							if (StringUtil.isEmpty(passMail)) 
								passMail=mails[i];
						    else 
				    			passMail=passMail+","+mails[i];
				           }
		     	         }
	  	        
	  	        
	  	         String [] userMail=StringUtil.split(passMail, ",");

	  	         log.info("SetYiyeMailSuccess:"+userMail.length);
	  	         
	  	         EaseyeMessageReceiveDTO [] messageReceiveList =new EaseyeMessageReceiveDTO[userMail.length];

	  	        for (int i = 0; i < userMail.length; i++) {
						messageReceiveList[i] = EaseyeMessageReceiveDTO.Factory.newInstance();
						messageReceiveList[i].setTo(userMail[i]);
		    	}
	  	        
	  	        importContactByGroupDetailed.setImportName(GROUPNAME+date);
	  	        importContactByGroupDetailed.setEaseyeUserAccountDTO(easeyeUserAccountDTO);
	  	        importContactByGroupDetailed.setEaseyeGroupDTO(easeyeGroupDTO);
	  	        
	  	        ArrayOfEaseyeMessageReceiveDTO arrayOfEaseyeMessageReceiveDTO=ArrayOfEaseyeMessageReceiveDTO.Factory.newInstance();
	  	        arrayOfEaseyeMessageReceiveDTO.setEaseyeMessageReceiveDTOArray(messageReceiveList);
	  	        		
	  	        importContactByGroupDetailed.setEaseyeMessageReceiveDTOArray1(arrayOfEaseyeMessageReceiveDTO);
	  	        
	  	        ImportContactByGroupDetailedDocument importContactByGroupDetailedDocument=ImportContactByGroupDetailedDocument.Factory.newInstance();
	  	        importContactByGroupDetailedDocument.setImportContactByGroupDetailed(importContactByGroupDetailed);
	            
	  	        importContactByGroupDetailedResponseDocument=mailWebServiceStub.importContactByGroupDetailed(importContactByGroupDetailedDocument);

			 } catch (Exception e) {
	              return new ReturnBean(null,EXCEPTION);
	         }    	
	    	
	    	return new ReturnBean(null,importContactByGroupDetailedResponseDocument.getImportContactByGroupDetailedResponse().getImportContactByGroupDetailedResult().getErrorCode(), excludeMail);
	    }
	
	 
	//发送邮件
		public static Integer sendMailListRequestByGroup(MailWebServiceStub mailWebServiceStub,EaseyeUserAccountDTO easeyeUserAccountDTO,String date){
			
			   SendMailListRequestByGroupResponseDocument sendMailListRequestByGroupResponseDocument;
			   SendMailListRequestByGroup sendMailListRequestByGroup=SendMailListRequestByGroup.Factory.newInstance();
			   
			 try {
		
			   EaseyeMessageTemplateDTO easeyeMessageTemplateDTO=EaseyeMessageTemplateDTO.Factory.newInstance();

		       easeyeMessageTemplateDTO=EaseyeMessageTemplateDTO.Factory.newInstance();
		       easeyeMessageTemplateDTO.setTemplateName(TEMPLATENAME+date);
			   
			   
	  	      EaseyeGroupDTO easeyeGroupDTO=EaseyeGroupDTO.Factory.newInstance();
	  	      easeyeGroupDTO.setGroupName(GROUPNAME+date);
	  	        
	  	      EaseyeSendOptionDTO easeyeSendOptionDTO=EaseyeSendOptionDTO.Factory.newInstance();
	  	      easeyeSendOptionDTO.setSenderEmail(SENDEREMAIL);
	  	      easeyeSendOptionDTO.setSenderName(SENDEREMAILNAME);
	          easeyeSendOptionDTO.setIsActive(true);
	          easeyeSendOptionDTO.setTrackOpen("1");
	          easeyeSendOptionDTO.setMailListName(GROUPNAME+date);

				
	          SendMailListRequestByGroupDocument sendMailListRequestByGroupDocument=SendMailListRequestByGroupDocument.Factory.newInstance();
	          sendMailListRequestByGroup.setEaseyeGroupDTO(easeyeGroupDTO);
	          sendMailListRequestByGroup.setEaseyeMessageTemplateDTO(easeyeMessageTemplateDTO);
	          sendMailListRequestByGroup.setEaseyeUserAccountDTO(easeyeUserAccountDTO);
	          sendMailListRequestByGroup.setEaseyeSendOptionDTO(easeyeSendOptionDTO);
	          sendMailListRequestByGroupDocument.setSendMailListRequestByGroup(sendMailListRequestByGroup);
	        
	      
			  sendMailListRequestByGroupResponseDocument = mailWebServiceStub.sendMailListRequestByGroup(sendMailListRequestByGroupDocument);
			} catch (RemoteException e) {
			     return EXCEPTION;
			}
	  
			return sendMailListRequestByGroupResponseDocument.getSendMailListRequestByGroupResponse().getSendMailListRequestByGroupResult().getErrorCode();
		}
		
		
       public static ReturnBean GetContactListMail (Date start,Date end,Integer pageIndex,Integer size){
			
			try {
				
				  MailWebServiceStub mailWebServiceStub = new MailWebServiceStub();
				   List<UserStatusVO>list=new ArrayList<UserStatusVO>();
				   DateUtil dateUtil=new DateUtil();
        	       Date date=new Date();
        	       Calendar calStart = Calendar.getInstance();
        	       Calendar calEnd = Calendar.getInstance();
        	       calStart.setTime(start);
        	       calEnd.setTime(end);
        	       
		          EaseyeUserAccountDTO easeyeUserAccountDTO = EaseyeUserAccountDTO.Factory.newInstance();   //yiye 登入账号密码
		          easeyeUserAccountDTO.setEmail(SUBSCRIPTIONEMAIL);
		  	      easeyeUserAccountDTO.setPassword(SUBSCRIPTIONPASSWORD);	
	
		          ContactFilter contactFilter=ContactFilter.Factory.newInstance();
		          contactFilter.setGroupName(subscriptionGroup);
		          contactFilter.setBeginTime(calStart);
		          contactFilter.setEndTime(calEnd);
		          contactFilter.setSortDir("desc");
		          
		          GetContactList getContactList=GetContactList.Factory.newInstance();
		   
		          getContactList.setContactFilter(contactFilter);
		          getContactList.setEaseyeUserAccountDTO(easeyeUserAccountDTO);
		          getContactList.setPageSize(size);
		          getContactList.setPageIndex(pageIndex);

		          
		          GetContactListDocument getContactListDocument=GetContactListDocument.Factory.newInstance();
		          getContactListDocument.setGetContactList(getContactList);
		          GetContactListResponseDocument responseDocument=mailWebServiceStub.getContactList(getContactListDocument);
		           ArrayOfContactInfoDTO arrayOfContactInfoDTO= responseDocument.getGetContactListResponse().getGetContactListResult().getContactInfoDTO();
		           
		           if (responseDocument.getGetContactListResponse().getGetContactListResult().getErrorCode()==SUCCESSCODE.intValue()) {

				          for (int i = 0; i < arrayOfContactInfoDTO.getContactInfoDTOArray().length; i++) {
		 
				        	      UserStatusVO userStatusVO=new UserStatusVO();
				        	      Calendar calendar=arrayOfContactInfoDTO.getContactInfoDTOArray(i).getStatusChangeTime();
		                          String email=arrayOfContactInfoDTO.getContactInfoDTOArray(i).getEmail();
				        	      date.setTime(calendar.getTimeInMillis());
				        	      userStatusVO.setEmail(email.replace(email.substring(0, 5), "*****"));
				          	      userStatusVO.setRemark(dateUtil.fmtDateForBetRecods(date));
				          	      userStatusVO.setLoginname(AESUtil.aesEncrypt(email, AESUtil.KEY));
				          	      list.add(userStatusVO);
						}
				          
				          Page page=new Page();
				          Integer totalResults=responseDocument.getGetContactListResponse().getGetContactListResult().getTotalCount();
				          page.setTotalRecords(totalResults);
				          page.setPageContents(list);
						  page.setNumberOfRecordsShown(totalResults);
				          int pages = PagenationUtil.computeTotalPages(totalResults, size).intValue();
				          page.setTotalPages(totalResults);
				          if (pageIndex.intValue() > pages)
								pageIndex = Page.PAGE_BEGIN_INDEX;
							page.setPageNumber(pageIndex);
				          return new ReturnBean(null, page);
				   }else 
					   
					 return new ReturnBean("获取组联系人报错:"+responseDocument.getGetContactListResponse().getGetContactListResult().getErrorCode());

			} catch (Exception e) {
				return new ReturnBean("系统报错");
			}
			
		}
	
}
