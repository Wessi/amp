/*
 *   SiteCache.java
 * 	 @Author Mikheil Kapanadze mikheil@digijava.org
 * 	 Created: Sep 9, 2003
 * 	 CVS-ID: $Id: SiteCache.java,v 1.1 2005-07-06 12:00:13 rahul Exp $
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
package org.digijava.kernel.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.digijava.kernel.Constants;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.request.SiteDomain;
import net.sf.hibernate.Session;
import net.sf.swarmcache.ObjectCache;

public class SiteCache implements Runnable {

    private static Logger logger = I18NHelper.getKernelLogger(SiteCache.class);
    private static final String appScopeKey = SiteCache.class.getName();

    public class CachedSite {

        public CachedSite(Site site) {
            this.site = site;
            this.instances = new ArrayList();
            this.userLanguages = new ArrayList(site.getUserLanguages());
            this.translationLanguages = new ArrayList(site.getTranslationLanguages());
            this.setDefaultLanguage(site.getDefaultLanguage());
            this.sendAlertsToAdmin = site.getSendAlertsToAdmin();

            instances.addAll(site.getModuleInstances());
            Collections.sort(instances, moduleInstanceComparator);
        }

        private Site site;
        private Site rootSite;
        private List instances;
        private Collection userLanguages;
        private Collection translationLanguages;
        private Locale defaultLanguage;
        private Boolean sendAlertsToAdmin;

        public Site getSite() {
            return site;
        }

        public Site getRootSite() {
            return rootSite;
        }

        public void setRootSite(Site rootSite) {
            this.rootSite = rootSite;
        }

        public List getInstances() {
            return instances;
        }

        public Collection getUserLanguages() {
            return userLanguages;
        }

        public void setUserLanguages(Collection userLanguages) {
            this.userLanguages = new ArrayList(userLanguages);
        }

        public Collection getTranslationLanguages() {
            return translationLanguages;
        }

        public void setTranslationLanguages(Collection translationLanguages) {
            this.translationLanguages = new ArrayList(translationLanguages);
        }

        public Locale getDefaultLanguage() {
            return defaultLanguage;
        }

        public void setDefaultLanguage(Locale defaultLanguage) {
            this.defaultLanguage = defaultLanguage;
        }

        public Boolean getSendAlertsToAdmin() {
            return sendAlertsToAdmin;
        }

        public void setSendAlertsToAdmin(Boolean sendAlertsToAdmin) {
            this.sendAlertsToAdmin = sendAlertsToAdmin;
        }

    }

    private static final Comparator reverseStringComparator = new Comparator() {
        public int compare(Object o1,
                           Object o2) {
            String s1 = (String) o1;
            String s2 = (String) o2;

            return -s1.compareTo(s2);
        }
    };

    public static final Comparator moduleInstanceComparator = new Comparator() {
        public int compare(Object o1,
                           Object o2) {
            ModuleInstance i1 = (ModuleInstance) o1;
            ModuleInstance i2 = (ModuleInstance) o2;

            int result;
            result = i1.getModuleName().compareTo(i2.getModuleName());
            if (result == 0) {
                result = i1.getInstanceName().compareTo(i2.getInstanceName());
            }
            return result;
        }
    };

    private java.util.List sharedInstances;
    private HashMap sites;
    private HashMap sitesByStringId;
    private HashMap siteDomains;
    private static SiteCache currentInstance;
    private volatile Long cacheVersion;
    private ObjectCache appScopeCache;

    public static SiteCache getInstance() {
        return currentInstance;
    }

    private SiteCache() {
        this.appScopeCache = DigiCacheManager.getInstance().getCache(Constants.APP_SCOPE_REGION);
        cacheVersion = new Long(1);
        try {
            load(false);
        }
        catch (DgException ex) {
            throw new RuntimeException("Unable to initialize site cache");
        }
    }

    private void handleVersioning() {
        Long versionFromCache = (Long) appScopeCache.get(appScopeKey);
        if (versionFromCache == null) {
            versionFromCache = new Long(0);
        }

        if (!cacheVersion.equals(versionFromCache)) {
            //Fire reload
            Thread loader = new Thread(this, "Site cache loader thread");
            loader.setDaemon(true);
            loader.start();
        }
    }



    public void load() throws DgException {
        load(false);
    }

    public void load(boolean silent) throws DgException {
        logger.debug("Reloading SiteCache. silent=" + silent);
        HashMap siteCache = new HashMap();
        HashMap sitesByStringIdCache = new HashMap();
        HashMap siteDomainCache = new HashMap();
        List newSharedInstances = null;

        Session session = null;
        try {
            session = PersistenceManager.getSession();

            newSharedInstances = new ArrayList(session.find(" from mi in class " +
                ModuleInstance.class.getName() +
                " where mi.site is null "));

            Collections.sort(newSharedInstances, moduleInstanceComparator);

            Iterator iter = session.iterate(" from " + SiteDomain.class.getName());
            while (iter.hasNext()) {
                SiteDomain siteDomain = (SiteDomain) iter.next();

                String path = siteDomain.getSitePath() == null ? "" :
                    siteDomain.getSitePath().trim();
                SortedMap siteDomainPathes = (SortedMap) siteDomainCache.get(
                    siteDomain.getSiteDomain().trim());
                if (siteDomainPathes == null) {
                    siteDomainPathes = new TreeMap(reverseStringComparator);
                    siteDomainCache.put(siteDomain.getSiteDomain().trim(),
                                        siteDomainPathes);
                }
                siteDomainPathes.put(path, siteDomain);

                Site site = siteDomain.getSite();
                if( site != null ) {
                    if (!siteCache.containsKey(site.getId())) {
                        siteCache.put(site.getId(), new CachedSite(site));
                        sitesByStringIdCache.put(site.getSiteId(), new CachedSite(site));
                    }
                } else {
                    if (logger.isDebugEnabled()) {
                        Object[] params = {siteDomain.getSiteDomain()};
                        logger.l7dlog(Level.ERROR, "SiteCache.noSiteForDomain", params, null);
                    }
                }
            }
        }
        catch (Exception ex) {
            logger.debug("load() failed ",ex);
            throw new DgException("load() failed ",ex);
        }
        finally {
            if (session != null) {
                try {
                    PersistenceManager.releaseSession(session);
                }
                catch (Exception ex) {
                    logger.warn("releaseSession() failed ",ex);
                }
            }
        }

        Iterator iter = siteCache.values().iterator();
        while (iter.hasNext()) {
            CachedSite cachedSite = (CachedSite) iter.next();
            synchronizePreferences(siteCache, cachedSite);
        }

        synchronized (this) {
            sites = siteCache;
            sitesByStringId = sitesByStringIdCache;
            siteDomains = siteDomainCache;
            sharedInstances = newSharedInstances;

            boolean putBack = false;
            Long versionFromCache = (Long) appScopeCache.get(appScopeKey);
            if (versionFromCache == null) {
                versionFromCache = new Long(0);
                putBack = true;
            }

            if (cacheVersion.compareTo(versionFromCache) < 0) {
                cacheVersion = versionFromCache;
            } else {
                if (cacheVersion.compareTo(versionFromCache) > 0) {
                    putBack = true;
                }
            }

            if (!silent) {
                cacheVersion = new Long(cacheVersion.longValue() + 1);
                putBack = true;
            }

            if (putBack) {
                logger.debug("Putting new version " + cacheVersion +
                             " of SiteCache in the application scope");
                appScopeCache.put(appScopeKey, cacheVersion);
            }


            if (logger.isDebugEnabled()) {
                Object[] params = {
                    toXml()};
                logger.l7dlog(Level.DEBUG, "SiteCache.currentCache", params, null);
            }
        }
    }

    private void synchronizePreferences(HashMap sites, CachedSite cachedSite) {
        Site site = cachedSite.getSite();

        if (site.getParentId() == null) {
            // If root site does not have languages assigned, assume English
            // as default, user and translation languages
            if (site.getDefaultLanguage() == null) {
                Locale language = new Locale();
                language.setCode(java.util.Locale.ENGLISH.getLanguage());
                language.setName(java.util.Locale.ENGLISH.getDisplayName());

                cachedSite.setDefaultLanguage(language);
                //site.setDefaultLanguage(language);
                HashSet languages = new HashSet();
                languages.add(language);
                //site.setUserLanguages(languages);
                //site.setTranslationLanguages(languages);
                cachedSite.setUserLanguages(languages);
                cachedSite.setTranslationLanguages(languages);
            }
            cachedSite.setRootSite(site);
            if (site.getSendAlertsToAdmin() == null) {
                cachedSite.setSendAlertsToAdmin(Boolean.FALSE);
            }
        }
        else {
            CachedSite cachedParentSite = (CachedSite) sites.get(site.
                getParentId());
            Site parentSite = cachedParentSite.getSite();
            synchronizePreferences(sites, cachedParentSite);
            // If site does not have language preferences assigned, then
            // import them from root site
            if (site.getDefaultLanguage() == null) {
                cachedSite.setUserLanguages(cachedParentSite.getUserLanguages());
                cachedSite.setTranslationLanguages(cachedParentSite.getTranslationLanguages());
                cachedSite.setDefaultLanguage(cachedParentSite.getDefaultLanguage());
            } else {
                cachedSite.setUserLanguages(site.getUserLanguages());
                cachedSite.setTranslationLanguages(site.getTranslationLanguages());
                cachedSite.setDefaultLanguage(site.getDefaultLanguage());
            }
            cachedSite.setRootSite(cachedParentSite.getRootSite());

            if (site.getSendAlertsToAdmin() == null) {
                cachedSite.setSendAlertsToAdmin(cachedParentSite.getSendAlertsToAdmin());
            }
        }
    }

    public Site getSite(Long siteId) {
        CachedSite cachedSite = (CachedSite) getSites().get(siteId);
        return cachedSite.getSite();
    }

    public Site getSite(Site site) {
        CachedSite cachedSite = (CachedSite) getSites().get(site.getId());
        return cachedSite.getSite();
    }

    public Site getSite(String siteId) {
        CachedSite cachedSite = (CachedSite) getSitesByStringId().get(siteId);
        return cachedSite.getSite();
    }


    public Site getRootSite(Site site) {
        CachedSite cachedSite = (CachedSite) getSites().get(site.getId());
        return cachedSite.getRootSite();
    }

    public Boolean getSendAlertsToAdmin(Site site) {
        CachedSite cachedSite = (CachedSite) getSites().get(site.getId());
        return cachedSite.getSendAlertsToAdmin();
    }

    public Site getParentSite(Site site) {
        CachedSite cachedSite = (CachedSite) getSites().get(site.getId());
        Long parentId = cachedSite.getSite().getParentId();
        if (parentId != null) {
            Site parent = ((CachedSite)getSites().get(parentId)).getSite();
            if (parent == null) {
                logger.warn("Site #" + parentId + " was not found");
            }
            return parent;
        } else {
            return null;
        }
    }

    public Collection getUserLanguages(Site site) {
        CachedSite cachedSite = (CachedSite) getSites().get(site.getId());
        if (cachedSite != null) {
            return cachedSite.getUserLanguages();
        } else {
            logger.warn("Site #" + site.getId() + " was not found");
            return null;
        }
    }

    public Collection getTranslationLanguages(Site site) {
        CachedSite cachedSite = (CachedSite) getSites().get(site.getId());
        if (cachedSite != null) {
            return cachedSite.getTranslationLanguages();
        } else {
            logger.warn("Site #" + site.getId() + " was not found");
            return null;
        }
    }

    public Locale getDefaultLanguage(Site site) {
        CachedSite cachedSite = (CachedSite) getSites().get(site.getId());
        if (cachedSite != null) {
            return cachedSite.getDefaultLanguage();
        }
        else {
            logger.warn("Site #" + site.getId() + " was not found");
            return null;
        }
    }


    public List getInstances(Site site) {
        CachedSite cachedSite = (CachedSite) getSites().get(site.getId());
        return cachedSite.getInstances();
    }

    public SiteDomain getSiteDomain(String domain, String path) {
        SortedMap siteDomainPathes = (SortedMap) getSiteDomains().get(domain.
            trim());
        if (siteDomainPathes == null) {
            return null;
        }

        if ( (path == null) || (path.trim().length() == 0)) {
            return (SiteDomain) siteDomainPathes.get("");
        }

        SiteDomain siteDomain = null;
        Iterator iter = siteDomainPathes.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry item = (Map.Entry) iter.next();
            if (path.startsWith(item.getKey() + "/")) {
                siteDomain = (SiteDomain) item.getValue();
                break;
            }
        }

        return siteDomain;
    }

    private synchronized HashMap getSiteDomains() {
        handleVersioning();
        return siteDomains;
    }

    private synchronized HashMap getSites() {
        handleVersioning();
        return sites;
    }

    private synchronized HashMap getSitesByStringId() {
        handleVersioning();
        return sitesByStringId;
    }

    public synchronized List getSharedInstances() {
        handleVersioning();
        return sharedInstances;
    }

    static {
        synchronized (SiteCache.class) {
            currentInstance = new SiteCache();
        }
    }

    public String toXml() {
        HashMap siteCache = null;

        synchronized (this) {
            siteCache = sites;
        }
        StringBuffer buff = new StringBuffer();
        final String newLn = "\n";
        buff.append("<site-cache>").append(newLn);
        Iterator iter = siteCache.values().iterator();
        while (iter.hasNext()) {
            CachedSite cachedSite = (CachedSite) iter.next();
            buff.append("    ").append("<site id=\"").append(cachedSite.getSite().
                getId()).append("\" ");
            buff.append("root=\"");
            if (cachedSite.getRootSite() == null) {
                buff.append("NULL");
            }
            else {
                buff.append(cachedSite.getRootSite().getId());
            }
            buff.append("\">");
            buff.append(cachedSite.getSite().getSiteId());
            buff.append("</site>").append(newLn);
        }
        buff.append("</site-cache>");
        return buff.toString();
    }

    public void run() {
        try {
            load(true);
        }
        catch (DgException ex) {
            logger.error("Unabme to refresh SiteCache", ex);
            throw new RuntimeException("Unabme to refresh SiteCache", ex);
        }
    }
}