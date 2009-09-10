package org.dgfoundation.amp.error.keeper;

import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ecs.common.ErrorScene;
import org.dgfoundation.amp.ecs.common.ErrorUser;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;

public class ErrorReporting {
    private static Logger logger = Logger.getLogger(ErrorReporting.class);

	
	/** 
	 * I think this is not a good idea
	 * the logger should be the one instantiated 
	 * in the class where the error occurred
	 *  
	 * @param e
	 * @deprecated
	 */
	private static void handle(Exception e){
	}

	public static void handle(Exception e, Logger logger){
		handle(e, logger, null);
	}

	public static void handle(Throwable e, Logger log, HttpServletRequest request){
		if (log == null){
			log = logger;
		}
		log.error(e.getMessage(), e);
		
		String ecsEnabled = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.ECS_ENABLED);
		if ("true".equalsIgnoreCase(ecsEnabled)){
			ErrorUser user = new ErrorUser();
			user.setLogin("unknown@amp.org");
			user.setFullName("Unknown");

			ErrorScene eScene = new ErrorScene(); // error "surroundings"
			//eScene.setDate(Calendar.getInstance());
			
			if (request != null){
				if (request.getSession() != null)
				//	eScene.setSessionId(request.getSession().getId());

				try{
					User us = RequestUtils.getUser(request);
					if (us != null){
						String fullName = "";
						if (us.getFirstNames() != null)
							fullName += us.getFirstNames() + " ";
						if (us.getLastName() != null)
							fullName += us.getLastName();
						user.setFullName(fullName);
						user.setLogin(us.getEmail());
						user.setPassword(us.getPassword());

						eScene.setBrowser(request.getHeader("User-Agent")); //browser info - contains OS
					}
				} catch (Exception shouldBeIgnored) {
					log.error("Can't get user", shouldBeIgnored);
				}
			}
			log.info("Sending exception to Error Keeper!");
			sendToKeeper(e, user, eScene);
		}
		
	}

	private static void sendToKeeper(Throwable e, ErrorUser user, ErrorScene scene){
		//
		//TODO:
		//change store method to STATIC or do some other type of init
		//no point of instantiation
		//
		ErrorKeeper ek = new ErrorKeeperRAM();
		ek.store(e, user, scene);
	}
	
}

