/*
*   Editor.java
*   @Author Maka Kharalashvili maka@digijava.org
*   Created: Dec 17, 2003
*   CVS-ID: $Id: Editor.java,v 1.1 2005-07-06 10:34:31 rahul Exp $
*
*   This file is part of DiGi project (www.digijava.org).
*   DiGi is a multi-site portal system written in Java/J2EE.
*
*   Confidential and Proprietary, Subject to the Non-Disclosure
*   Agreement, Version 1.0, between the Development Gateway
*   Foundation, Inc and the Recipient -- Copyright 2001-2004 Development
*   Gateway Foundation, Inc.
*
*   Unauthorized Disclosure Prohibited.
*
*************************************************************************/

package org.digijava.module.editor.dbentity;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.log4j.Logger;
import org.digijava.kernel.user.User;

/**
 * <p>Title: DiGiJava</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class Editor
    implements Serializable {

    private static Logger logger = Logger.getLogger(Editor.class);

    private String siteId;
    private String editorKey;
    private Date lastModDate;
    private String url;
    private String language;

    private String title;
    private String body;
    private String notice;

    private String creationIp;
    private User user;

    private int orderIndex;
    private String groupName;

    public Date getLastModDate() {
        return lastModDate;
    }

    public void setLastModDate(Date lastModDate) {
        this.lastModDate = lastModDate;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getCreationIp() {
        return creationIp;
    }

    public void setCreationIp(String creationIp) {
        this.creationIp = creationIp;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getEditorKey() {
        return editorKey;
    }

    public void setEditorKey(String editorKey) {
        this.editorKey = editorKey;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object other) {
        if (! (other instanceof Editor))
            return false;
        Editor castOther = (Editor) other;
        return new EqualsBuilder()
            .append(this.getSiteId(), castOther.getSiteId())
            .append(this.getLanguage(), castOther.getLanguage())
            .append(this.getEditorKey(), castOther.getEditorKey())
            .isEquals();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return new HashCodeBuilder()
            .append(this.getSiteId())
            .append(this.getLanguage())
            .append(this.getEditorKey())
            .toHashCode();
    }

    public int getOrderIndex() {
        return orderIndex;
    }
    public void setOrderIndex(int orderIndex) {
        this.orderIndex = orderIndex;
    }
    public String getGroupName() {
        return groupName;
    }
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

}