/**
 * This file is part of DiGi project (www.digijava.org).
 * DiGi is a multi-site portal system written in Java/J2EE.
 *
 * Copyright (C) 2002-2007 Development Gateway Foundation, Inc.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 */

package org.digijava.module.translation.taglib;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.security.auth.Subject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.log4j.Logger;
import org.digijava.kernel.Constants;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.request.SiteDomain;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.SiteUtils;
import org.digijava.module.translation.security.TranslateSecurityManager;
import org.digijava.kernel.util.DigiConfigManager;

/**
 * Custom tag that retrieves internationalized message
 * based on the Key provided. This tag uses methods in DgUtil to determine
 * the language in which the message must be displayed.
 * If the key does not have a translation in the requested language then
 * the message in site default language is shown, else English
 * language message is shown.
 * This Tag also appends an Edit,Translate or CreateTag to the message
 * if a Translator has logged in.
 */
public class TrnTag
    extends BodyTagSupport {

    private static final Logger logger = Logger.getLogger(TrnTag.class);

    private static final int NUMBER_OF_PARAMETERS = 5;

    public static final String LOCAL_TRANSLATION = "local";
    public static final String GROUP_TRANSLATION = "group";
    public static final String GLOBAL_TRANSLATION = "global";

    private String key = null;
    private Boolean linkAlwaysVisible = null;
    private String type = null;
    private String siteId = null;
    private String locale = null;

    private String[] args;

    private boolean groupTranslator = false;
    private boolean localTranslator = false;
    private boolean showLinks = false;
    private HttpServletRequest request = null;
    private String backUrl = null;
    private String bodyText;

    private int max;

    public TrnTag() {
        args = new String[NUMBER_OF_PARAMETERS];
        linkAlwaysVisible = new Boolean(false);
        max = 0;
    }

    /**
     * Gets the translation key
     * @return
     */
    public String getKey() {
        boolean caseSensitiveKeys = DigiConfigManager.getConfig().isCaseSensitiveTranslatioKeys();
        if (!caseSensitiveKeys) {
            return (this.key == null ? this.key : this.key.toLowerCase());
        } else {
            return this.key;
        }
    }

    /**
     * @param key
     */
    public void setKey(String key) {
        this.key = key;
    }

    public Boolean getLinkAlwaysVisible() {
        return linkAlwaysVisible;
    }

    public void setLinkAlwaysVisible(Boolean linkAlwaysVisible) {
        this.linkAlwaysVisible = linkAlwaysVisible;
    }

    /**
     * Gets the type of a message
     * Only Group and Local are allowed
     * @return
     */
    public String getType() {

        //Check if the type is null
        if (this.type == null) {
            this.type = GLOBAL_TRANSLATION;
        }

        return (this.type);
    }

    /**
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     *
     * @return
     */
    public String getSiteId() {
        return siteId;
    }

    /**
     *
     * @param siteId
     */
    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    /**
     * First argument used for formatting
     */
    public String getArg1() {
        return (this.args[0]);
    }

    /**
     * First argument used for formatting
     * @param String
     */
    public void setArg1(String arg1) {
        this.args[0] = arg1;
    }

    /**
     * Second argument used for formatting args
     */
    public String getArg2() {
        return (this.args[1]);
    }

    /**
     * Second argument used for formatting
     * @param String
     */
    public void setArg2(String arg2) {
        this.args[1] = arg2;
    }

    /**
     * Third argument used for formatting
     */
    public String getArg3() {
        return (this.args[2]);
    }

    /**
     * Third argument used for formatting
     *
     * @param String
     */
    public void setArg3(String arg3) {
        this.args[2] = arg3;
    }

    /**
     * Fourth argument used for formatting
     */
    public String getArg4() {
        return (this.args[3]);
    }

    /**
     * Third argument used for formatting
     *
     * @param String
     */
    public void setArg4(String arg4) {
        this.args[3] = arg4;
    }

    /**
     * Third argument used for formatting
     */
    public String getArg5() {
        return (this.args[4]);
    }

    /**
     * Third argument used for formatting
     *
     * @param String
     */
    public void setArg5(String arg5) {
        this.args[4] = arg5;
    }

    /**
     * maximal length of visible translation
     * @param max
     */
    public void setMax(int max) {
        this.max = max;
    }

    public int getMax() {
        return max;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public void setBodyText(String bodyText) {
        this.bodyText = bodyText;
    }

    public String getLocale() {
        return locale;
    }

    public String getBodyText() {
        return bodyText;
    }

    /**
     * Process the start of this tag.
     * @return int
     */
    public int doStartTag() {
        return EVAL_BODY_BUFFERED;
    }


    public int doEndTag() {

        request = (HttpServletRequest) pageContext.getRequest();

        //Compute back url...
        backUrl = "";
        showLinks = TranslatorWorker.isTranslationMode(request);
        if (!showLinks) {
            showLinks = linkAlwaysVisible.booleanValue();
        }

        //Determine Locale
        String langCode = getLocale();
        if (langCode == null) {
            Locale currLocale = RequestUtils.getNavigationLanguage(request);
            if (currLocale == null) {
                SiteDomain dom = RequestUtils.getSiteDomain(request);
                logger.warn("Navigation language is null for SiteDomain #" + dom.getSiteDomainId());
                langCode = "en";
            } else {
                langCode = currLocale.getCode();
            }
        }
        Site site = null;
        if (siteId != null) {
            try {
                site = SiteUtils.getSite(siteId);
            }
            catch (DgException ex) {
                logger.error("Error in trn tag", ex);
                writeData("<b>ERROR:</b> " + ex.getMessage());
                return EVAL_PAGE;
            }
        }
        else {
            site = RequestUtils.getSite(request);
        }
        // determine User permissions
        if (showLinks) {
            backUrl = RequestUtils.getRelativeSourceURL(request);


            Site rootSite = DgUtil.getRootSite(site);
            Subject subject = RequestUtils.getSubject(request);

            groupTranslator = TranslateSecurityManager.checkPermission(subject,
                rootSite.getId().longValue(), langCode);
            localTranslator = TranslateSecurityManager.checkPermission(subject,
                site.getId().longValue(), langCode);
        }

        int actualType = getActualTypeCode();
        logger.debug("tag type is: " + getType() + " actual type is: " +
                     actualType);

        try {
            translate(getKey(), site, langCode, actualType);
        }
        catch (WorkerException ex1) {
            logger.error("Error in trn tag", ex1);
            writeData("<b>ERROR:</b> " + ex1.getMessage());
        }
        // Skip the tag and continue processing this page
        return EVAL_PAGE;
    }

    private void translate(String key, Site site, String langCode, int trnType) throws WorkerException {
        HashSet checked = new HashSet();
        String value = TranslatorWorker.getInstance(key).
            translateFromTree(key, site.getId().longValue(),
                              langCode, null, trnType);
        if (value != null) {
            writeData(value, getMessageEdit());
            return;
        }
        checked.add(langCode);

        Locale defLang = SiteUtils.getDefaultLanguages(site);
        if (!defLang.getCode().equals(langCode)) {
            value = TranslatorWorker.getInstance(key).
                translateFromTree(key, site.getId().longValue(),
                                  defLang.getCode(), null, trnType);
            if (value != null) {
                writeData(value, getMessageTranslate());
                return;
            }
            checked.add(defLang.getCode());
        }

        /** @todo implement more wise solution here */
        String defaultTrn = getBodyText();
        if (defaultTrn == null) {
            defaultTrn = getDefaultTranslation();
            logger.debug(
                "Returning message from tag body");
        }

        value = TranslatorWorker.getInstance(key).
            translateFromTree(key, site.getId().longValue(),
                              "en", defaultTrn, trnType);
        if (value != null) {
            writeData(value, getMessageTranslate());
            return;
        }
        checked.add("en");

        Set otherLangs = SiteUtils.getUserLanguages(site);
        otherLangs.removeAll(checked);
        if (otherLangs.size() != 0) {
            String[] langs = new String[otherLangs.size()];
            int i =0;
            Iterator iter = otherLangs.iterator();
            while (iter.hasNext()) {
                Locale item = (Locale) iter.next();
                langs[i] = item.getCode();
                i ++;
            }
            value = TranslatorWorker.getInstance(key).
                translateFromTree(key, site.getId().longValue(),
                                  langs, null, null, trnType);
            if (value != null) {
                writeData(value, getMessageTranslate());
                return;
            }
        }
        writeData("key:" + key + getMessageCreate());
    }

    /**
     * Returns text from tag body
     * @return text from tag body
     */
    private String getDefaultTranslation() {
        BodyContent body = getBodyContent();
        if (body != null) {
            String result = body.getString();
            if (result != null && result.trim().length() == 0) {
                return null;
            } else {
                return result;
            }
        } else {
            return null;
        }
    }

    /**
     * Formats message text, appends suffix and prints the result
     * @param message String
     * @param suffix String
     * @param body BodyContent
     */
    protected void writeData(String message, String suffix) {
        String newSuffix = suffix;
        if (suffix == null) {
            newSuffix = "";
        }
        writeData(format(message) + newSuffix);
    }

    /**
     * Writes given string to the page output
     * @param localizedMsg
     * @param body
     */
    protected void writeData(String localizedMsg) {
        try {
            JspWriter out = pageContext.getOut();
            out.print(localizedMsg);
        }
        catch (IOException ioe) {
            logger.error("IOException " + ioe, ioe);
        }

    }

    /**
     * This method would format a passed message
     * @param msg message to format
     */
    protected String format(String msg) {
        if (msg.indexOf('{') < 0) {
            //truncate msg if needed
            if (getMax() > 0) {
                if (msg.length() > getMax()) {
                    msg = msg.substring(0, getMax()) + "...";
                }
            }

            return msg;
        }
        Map translationParams = (Map) RequestUtils.getDigiContextAttribute( (
            HttpServletRequest) pageContext.getRequest(),
            Constants.TRANSLATION_PARAMETERS);

        if (translationParams != null) {
            translationParams = new HashMap(translationParams);
        }

        for (int i = 0; i < args.length; i++) {
            if (args[i] != null) {
                if (translationParams == null) {
                    translationParams = new HashMap();
                }
                translationParams.put(Integer.toString(i), args[i]);
            }
        }

        String formattedString = msg;
        if (translationParams != null) {
            formattedString = DgUtil.fillPattern(formattedString,
                                                 translationParams);
        }

        //truncate if needed
        if (getMax() > 0) {
            if (formattedString.length() > getMax()) {
                formattedString = formattedString.substring(0, getMax()) +
                    "...";
            }
        }

        return formattedString;
    }

    /**
     * Returns Edit string depending on the tag type and user priveleges
     * @return
     */
    private String showLinks(char operType) {
        String translatorTag = "";

        if (showLinks) {
            if (this.getKey().trim().startsWith("cn") ||
                this.getKey().trim().startsWith("ln")) {
                return translatorTag;
            }

            String actualType = getActualEditType();

            if (actualType.equals(LOCAL_TRANSLATION)) {
                if (localTranslator) {
                    translatorTag = getTranslationLink(LOCAL_TRANSLATION, operType);
                }
            }
            else if (actualType.equals(GROUP_TRANSLATION)) {
                if (localTranslator) {
                    translatorTag = getTranslationLink(LOCAL_TRANSLATION, operType);
                }
                if (groupTranslator) {
                    translatorTag += getTranslationLink(GROUP_TRANSLATION, operType);
                }

            }
        }
        return translatorTag;
    }
    /**
     * Returns Edit string depending on the tag type and user priveleges
     * @return
     */
    private String getMessageEdit() {
        return showLinks('E');
    }

    /**
     * Returns Create string depending on the tag type and user previledges
     *
     * @return
     */
    private String getMessageCreate() {
        return showLinks('C');
    }

    /**
     * Returns translate string depending on the tag type and user previledges
     *
     * @return
     */
    private String getMessageTranslate() {
        return showLinks('T');
    }

    /**
     * Returns Href string depending on the context path retrieved
     *
     * @return
     */
    private String getHref() {
        String returnPath = request.getContextPath();
        if (RequestUtils.getSiteDomain(request).getSitePath() != null) {
            returnPath = returnPath +
                RequestUtils.getSiteDomain(request).getSitePath();
        }
        return returnPath;
    }

    private String getTranslationLink(String trnType, char operType) {
        String suffix = "L";
        if (!trnType.equals(LOCAL_TRANSLATION)) {
            suffix = "";
        }
        String siteParam = "";
        if (siteId != null) {
            siteParam = "&siteId=" + siteId;
        }
        return "<a href=\"" + getHref() +
            "/translation/showTranslate.do?key=" + getKey() + siteParam +
            "&back_url=" + backUrl + "&type=" + trnType + "\">&lt;" + operType +
            suffix + "&gt;</a>";

    }

    /**
     * Release any acquired resources.
     */
    public void release() {

        super.release();

    }

    private String getActualEditType() {
        if (siteId != null) {
            return LOCAL_TRANSLATION;
        }
        else if (LOCAL_TRANSLATION.equals(type)) {
            return LOCAL_TRANSLATION;

        }
        return GROUP_TRANSLATION;
    }

    private int getActualTypeCode() {
        if (siteId != null) {
            return TranslatorWorker.TRNTYPE_LOCAL;
        }
        else if (LOCAL_TRANSLATION.equals(type)) {
            return TranslatorWorker.TRNTYPE_LOCAL;

        }
        else if (GROUP_TRANSLATION.equals(type)) {
            return TranslatorWorker.TRNTYPE_GROUP;
        }
        else {
            return TranslatorWorker.TRNTYPE_GLOBAL;
        }

}

}
