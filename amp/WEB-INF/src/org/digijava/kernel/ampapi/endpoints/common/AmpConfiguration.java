package org.digijava.kernel.ampapi.endpoints.common;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.common.net.HttpHeaders;
import com.sun.jersey.core.header.ContentDisposition;
import org.digijava.kernel.ampapi.endpoints.errors.ApiError;
import org.digijava.kernel.ampapi.endpoints.errors.ApiRuntimeException;
import org.digijava.kernel.ampapi.endpoints.errors.ErrorReportingEndpoint;
import org.digijava.kernel.ampapi.endpoints.filetype.MimeUtil;
import org.digijava.kernel.ampapi.endpoints.security.AuthRule;
import org.digijava.kernel.ampapi.endpoints.settings.SettingsUtils;
import org.digijava.kernel.ampapi.endpoints.util.ApiMethod;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.kernel.ampapi.filters.AmpOfflineModeHolder;
import org.digijava.module.aim.dbentity.AmpOfflineRelease;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.services.AmpOfflineService;
import org.digijava.kernel.util.SpringUtil;
import org.digijava.kernel.services.AmpVersionService;
import org.digijava.module.aim.dbentity.AmpGlobalSettings;
import org.digijava.module.aim.dbentity.AmpOfflineCompatibleVersionRange;
import org.digijava.module.aim.util.FeaturesUtil;

/**
 * This class should have all end point related to the configuration of amp
 * @author Diego Dimunzio
 * 
 */

@Path("amp")
public class AmpConfiguration implements ErrorReportingEndpoint {

	private AmpVersionService ampVersionService = SpringUtil.getBean(AmpVersionService.class);

	private AmpOfflineService ampOfflineService = SpringUtil.getBean(AmpOfflineService.class);

	/**
	 * Provides available settings and their possible values.
	 * <br>
	 * These settings should be supported by almost all API calls.<br>
	 * Note: most API endpoints will also need to accept a 'settings' object on queries.<br>
	 * <br>
	 * Example of 'settings' json object provided as POST input in API endpoints: 
	 * <pre>
	 * settings : { "1": "CAD", 
	 *              "2" : "6", 
	 *              "yearRange": {"yearFrom":"2005", "yearTo":"2020"}
	 *            }
	 * </pre>
	 * </br>
	 * <h3>Sample Output:</h3><pre>
     * [
     *   ...,
     *   {
     *    "id": "use-icons-for-sectors-in-project-list",
     *    "multi": false,
     *    "name": "Use icons for Sectors in Project List",
     *    "defaultId": "true",
     *    "options": [
     *      {
     *        "id": "true",
     *        "name": "true",
     *        "value": "true"
     *      }
     *    ]
     *   },
     *   {
     *    "id": "project-sites",
     *    "multi": false,
     *    "name": "Project Sites",
     *    "defaultId": "true",
     *    "options": [
     *    {
     *      "id": "true",
     *      "name": "true",
     *      "value": "true"
     *    }
     *   ]
     *  },
     *  ...
     * ]</pre>
	 * 
	 * @return a list of setting options
	 * @see SettingOptions
	 */
	@GET
	@Path("/settings")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@ApiMethod(ui = false, id = "Settings")
	public JsonBean getSettings() {
		return SettingsUtils.getGeneralSettings();
	}

	/**
	 * Check if AMP Offline App is compatible with AMP.
	 * <p>This method will return:
	 * <ul>
	 * <li>if AMP Offline App is compatible with AMP
	 * <li>AMP version
	 * <li>whenever AMP Offline is enabled or not
	 * <li>latest AMP Offline release
	 * </p>
	 * </ul>
	 * <p>AMP Offline version is read from User-Agent header. Header must have the following form:
	 * AMPOffline/{version} ({os}; {arch}). Example: AMPOffline/1.0.0 (windows; 32).</p>
	 */
	@GET
	@Path("/amp-offline-version-check")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@ApiMethod(ui = false, id = "version-check")
	public VersionCheckResponse ampOfflineVersionCheck() {

		AmpOfflineRelease clientRelease = detectClientRelease();

		VersionCheckResponse response = new VersionCheckResponse();
		response.setAmpOfflineCompatible(isAmpOfflineCompatible(clientRelease));
		response.setAmpOfflineEnabled(true);
		response.setAmpVersion(ampVersionService.getVersionInfo().getAmpVersion());
		response.setLatestAmpOffline(ampOfflineService.findLastRelease(clientRelease));

		return response;
	}

	private AmpOfflineRelease detectClientRelease() {
		AmpOfflineRelease release = null;
		if (AmpOfflineModeHolder.isAmpOfflineMode()) {
			try {
				String userAgent = TLSUtils.getRequest().getHeader("User-Agent");
				release = AmpOfflineRelease.fromUserAgent(userAgent);
			} catch (IllegalArgumentException e) {
				JsonBean error = ApiError.toError(AmpConfigurationErrors.INVALID_INPUT.withDetails(e.getMessage()));
				throw new ApiRuntimeException(Response.Status.BAD_REQUEST, error);
			}
		}
		return release;
	}

	private boolean isAmpOfflineCompatible(AmpOfflineRelease release) {
		return release != null && ampVersionService.isAmpOfflineCompatible(release.getVersion());
	}

	/**
	 * List latest AMP Offline releases for each OS/Arch.
	 * <h3>Sample Output:</h3>
	 * <pre>
	 * [
	 *   {
	 *     "id": 13,
	 *     "version": "1.0.1",
	 *     "os": "windows",
	 *     "arch": "32",
	 *     "critical": false,
	 *     "date": "2017-07-24"
	 *   },
	 *   {
	 *     "id": 14,
	 *     "version": "1.0.1",
	 *     "os": "windows",
	 *     "arch": "64",
	 *     "critical": false,
	 *     "date": "2017-07-24"
	 *   }
	 * ]
	 * </pre>
	 * @return latest AMP Offline releases
	 */
	@GET
	@Path("/amp-offline-release")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public List<AmpOfflineRelease> getAmpOfflineReleases() {
		return ampOfflineService.getLatestCompatibleReleases();
	}

	/**
	 * Returns the AMP Offline release binary.
	 *
	 * @param id of the binary
	 * @return the binary
	 */
	@GET
	@Path("/amp-offline-release/{id}")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response getAmpOfflineReleaseFile(@PathParam("id") Long id) {
		File file = ampOfflineService.getReleaseFile(id);

		ContentDisposition contentDisposition = ContentDisposition.type("attachment")
				.fileName(file.getName())
				.size(file.length())
				.build();

		String mimeType = MimeUtil.detectMimeType(file, MediaType.APPLICATION_OCTET_STREAM);

		return Response.ok(file, mimeType)
				.header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
				.build();
	}

	/**
	 * Returns all AMP Global Settings.
	 * <p>Response is a map containing all global settings where key is setting name and value is setting value.
	 * <h3>Sample Output:</h3>
	 * <pre>
	 * {
	 *   "Base Currency": "USD",
	 *   "Default Date Format": "dd/MM/yyyy",
	 *   "Resource List Sort Column": "date_DESC",
	 *   "ECS Enabled": "true"
	 * }
	 * </pre>
	 */
	@GET
	@Path("global-settings")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@ApiMethod(ui=false, id = "global-settings", authTypes = AuthRule.AUTHENTICATED)
	public Map<String, String> getGlobalSettings() {
		return FeaturesUtil.getGlobalSettings().stream()
				.filter(s -> s.getGlobalSettingsValue() != null)
				.collect(Collectors.toMap(
						AmpGlobalSettings::getGlobalSettingsName,
						AmpGlobalSettings::getGlobalSettingsValue));
	}

	/**
	 * Returns all compatible AMP Offline version ranges.
	 * <h3>Sample Output:</h3>
	 * <pre>
	 * [
	 *     {
	 *         "id": 1,
	 *         "from-version": "1.0.0",
	 *         "to-version": "2.0.0"
	 *     }
	 * ]
	 * </pre>
	 */
	@GET
	@Path("compatible-version-range")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@ApiMethod(id = "getCompatibleVersionRanges", ui = false, authTypes = AuthRule.IN_ADMIN)
	public List<AmpOfflineCompatibleVersionRange> getCompatibleVersionRanges() {
		return ampVersionService.getCompatibleVersionRanges();
	}

	/**
	 * Create a new version range to denote AMP Offline compatibility.
	 * <h3>Sample Input:</h3>
	 * <pre>
	 * {
	 *     "from-version": "1.0.0",
	 *     "to-version": "2.0.0"
	 * }
	 * </pre>
	 * <h3>Sample Output:</h3>
	 * <pre>
	 * {
	 *     "id": 3,
	 *     "from-version": "1.0.0",
	 *     "to-version": "2.0.0"
	 * }
	 * </pre>
	 */
	@PUT
	@Path("compatible-version-range")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@ApiMethod(id = "addCompatibleVersionRange", ui = false, authTypes = AuthRule.IN_ADMIN)
	public AmpOfflineCompatibleVersionRange addCompatibleVersionRange(AmpOfflineCompatibleVersionRange versionRange) {
		try {
			return ampVersionService.addCompatibleVersionRange(versionRange);
		} catch (IllegalArgumentException e) {
			JsonBean error = ApiError.toError(AmpConfigurationErrors.INVALID_INPUT.withDetails(e.getMessage()));
			throw new ApiRuntimeException(Response.Status.BAD_REQUEST, error);
		}
	}

	/**
	 * Update an existing version range that denotes AMP Offline compatibility.
	 * <h3>Sample Input:</h3>
	 * <pre>
	 * {
	 *     "from-version": "1.1.0",
	 *     "to-version": "2.0.0"
	 * }
	 * </pre>
	 * <h3>Sample Output:</h3>
	 * <pre>
	 * {
	 *     "id": 1,
	 *     "from-version": "1.1.0",
	 *     "to-version": "2.0.0"
	 * }
	 * </pre>
	 */
	@POST
	@Path("compatible-version-range/{id}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@ApiMethod(id = "updateCompatibleVersionRange", ui = false, authTypes = AuthRule.IN_ADMIN)
	public AmpOfflineCompatibleVersionRange updateCompatibleVersionRange(@PathParam("id") Long id,
			AmpOfflineCompatibleVersionRange versionRange) {
		try {
			versionRange.setId(id);
			return ampVersionService.updateCompatibleVersionRange(versionRange);
		} catch (IllegalArgumentException e) {
			JsonBean error = ApiError.toError(AmpConfigurationErrors.INVALID_INPUT.withDetails(e.getMessage()));
			throw new ApiRuntimeException(Response.Status.BAD_REQUEST, error);
		}
	}

	/**
	 * Delete an existing version range that denotes AMP Offline compatibility.
	 * <h3>Sample Output:</h3>
	 * <pre>
	 * {
	 *     "id": 3,
	 *     "from-version": "1.0.0",
	 *     "to-version": "2.0.0"
	 * }
	 * </pre>
	 */
	@DELETE
	@Path("compatible-version-range/{id}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@ApiMethod(id = "deleteCompatibleVersionRange", ui = false, authTypes = AuthRule.IN_ADMIN)
	public AmpOfflineCompatibleVersionRange deleteCompatibleVersionRange(@PathParam("id") Long id) {
		return ampVersionService.deleteCompatibleVersionRange(id);
	}

	@Override
	public Class getErrorsClass() {
		return AmpConfigurationErrors.class;
	}
}
