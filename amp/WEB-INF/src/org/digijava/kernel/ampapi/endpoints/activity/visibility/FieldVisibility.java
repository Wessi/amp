package org.digijava.kernel.ampapi.endpoints.activity.visibility;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.dgfoundation.amp.visibility.AmpTreeVisibility;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.module.aim.annotations.interchange.Interchangeable;
import org.digijava.module.aim.dbentity.AmpModulesVisibility;
import org.digijava.module.aim.dbentity.AmpTemplatesVisibility;
import org.digijava.module.aim.util.FeaturesUtil;

public class FieldVisibility {

	private final static Map<String, Boolean> visibilityMap = new HashMap<String, Boolean>();
	private static Date lastTreeVisibilityUpdate;

	/**
	 * Checks whether a Field is visible or not according to the Feature
	 * Manager. It checks the @Interchangeable annotation for its fmPath. If the
	 * the Module is enabled on the Feature Manager or there is no defined
	 * Feature Manager path for the Field the it returns true Otherwise it
	 * returns false
	 * 
	 * @param field
	 *            , the field to determine its visibility
	 * @return boolean, returns true if the field is visible, false otherwise.
	 */
	public static boolean isVisible(Field field) {
		HttpSession session = TLSUtils.getRequest().getSession();

		ServletContext ampContext = session.getServletContext();
		AmpTreeVisibility ampTreeVisibility = FeaturesUtil.getAmpTreeVisibility(ampContext, session);
		checkTreeVisibilityUpdate(session);

		boolean isVisible = false;
		Interchangeable annotation = field.getAnnotation(Interchangeable.class);
		if (annotation == null) {
			return isVisible;
		}
		String path = annotation.fmPath();
		if (path.equals("")) {
			isVisible = true;
		} else {
			String ancestorVerifiedPath = getLastVerifiedPath(path);
			if (ancestorVerifiedPath.equals("")) {
				ancestorVerifiedPath = getChildFMPath(path, ancestorVerifiedPath);
			}
			isVisible = isVisibleInFM(ampTreeVisibility, path,ancestorVerifiedPath);
		}
		return isVisible;
	}

	/**
	 * Checks if there was any update under Feature Manager tree visibility.
	 * If that the case all FM paths need to be rechecked again.
	 * 
	 * @param session, HttpSession containing the tree last modification date.
	 */
	private static void checkTreeVisibilityUpdate(HttpSession session) {
		Date lastUpdate = (Date) session.getAttribute("ampTreeVisibilityModificationDate");
		if (lastTreeVisibilityUpdate!=null && lastUpdate.after(lastTreeVisibilityUpdate)) {
			visibilityMap.clear();
		}
		lastTreeVisibilityUpdate = lastUpdate;

	}

	/**
	 * Checks if a FM Path is visible. In order to be visible, the FM path and all its ancestors need to be checked.
	 * 
	 * @param ampTreeVisibility, the AmpTreeVisibility that contains all modules visibility
	 * @param fmPath, the path to check for its visibility
	 * @param lastVerifiedPath, the last FM verified path. This is needed in order to avoid checking all FM path ancestors 
	 * multiple times
	 * @return, whether a FM path is visible or not
	 */
	private static boolean isVisibleInFM(AmpTreeVisibility ampTreeVisibility, String fmPath,String lastVerifiedPath) {
		boolean isVisible = false;
		AmpModulesVisibility modulesVisibility = ampTreeVisibility.getModuleByNameFromRoot(lastVerifiedPath);
		if (modulesVisibility != null) {
			isVisible = modulesVisibility.isVisibleTemplateObj((AmpTemplatesVisibility) ampTreeVisibility.getRoot());
			visibilityMap.put(lastVerifiedPath, isVisible);
		}
		if (!fmPath.equals(lastVerifiedPath) && isVisible) {
			lastVerifiedPath = getChildFMPath(fmPath, lastVerifiedPath);
			isVisible = isVisibleInFM(ampTreeVisibility, fmPath, lastVerifiedPath);
		}
		return isVisible;
	}

	/**
	 * Starting from the last FM verified Path, it returns its next child for the FM path we are searching for.
	 * If lastVerifiedPath is '/Activity Form', and we are searching for the visibility of 
	 * fmPath = '/Activity Form/Identification/Description' then the next child will be
	 * '/Activity Form/Identification'
	 * @param fmPath, the FM path to check if it is visible or not.
	 * @param lastVerifiedPath, the last FM ancestor path that was already verified for its visibility
	 * @return a String with the child FM path 
	 */
	private static String getChildFMPath(String fmPath, String lastVerifiedPath) {
		String pathDifference ;
		if (lastVerifiedPath.equals("")) {
			pathDifference = fmPath;
		}
		else {
			pathDifference = fmPath.substring(lastVerifiedPath.length(),fmPath.length());
		}
		int secondIndex = StringUtils.ordinalIndexOf(pathDifference, "/", 2);
		if (secondIndex == -1) {
			secondIndex = pathDifference.length();
		}
		lastVerifiedPath = lastVerifiedPath + pathDifference.substring(0, secondIndex);
		return lastVerifiedPath;
	}

	/**
	 * Given a FM Path like '/Activity Form/Identification/Description' it checks which was the last verified FM path.
	 * Starting from fmPath parameter and going up until the last verified is found. That is, if '/Activity Form/Identification/Description'
	 * was not already verified, it checks if '/Activity Form/Identification' was and after that it checks for the root 
	 *'/Activity Form'. Checking is stopped when a fm path that was already verified is found.
	 * 
	 * 
	 * @param fmPath, the path to check if it was already verified
	 * @return the last verified FM path.
	 */
	private static String getLastVerifiedPath(String fmPath) {
		String fmPathToCheck = fmPath;
		boolean alreadyVerified = false;
		do {
			alreadyVerified = isInMap(fmPathToCheck);
			fmPath = fmPathToCheck;
			fmPathToCheck = fmPathToCheck.substring(0, fmPathToCheck.lastIndexOf("/"));
			if (fmPathToCheck.equals("")) {
				alreadyVerified = true;
			}

		} while (!alreadyVerified);
		return fmPath;
	}

	/**
	 * Verifies if a FM Path was already checked.
	 * 
	 * @param fmPath, the path under Feature Manager
	 * @return true if it was already verified, false otherwise
	 */
	private static boolean isInMap(String fmPath) {
		return visibilityMap.containsKey(fmPath);
	}
}
