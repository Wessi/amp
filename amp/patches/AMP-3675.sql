delete from DG_MODULE_INSTANCE where module_name in ('syndication', 'news', 'forum', 'cms', 'dm', 'highlights', 'poll', 'demo');
drop table if exists DG_PUBLICATION_FEED;
drop table if exists DG_COLLECTOR_FEED_ITEM;
drop table if exists DG_COLLECTOR_FEED;
drop table if exists DG_NEWS_ITEM;
drop table if exists DG_NEWS_SETTINGS;
drop table if exists DG_NEWS;
drop table if exists DG_FORUM_PRIVATE_MESSAGE;
drop table if exists DG_FORUM_USER_SETTINGS;
drop table if exists DG_FORUM_SUBSECTION;
drop table if exists DG_FORUM_SECTION;
drop table if exists DG_FORUM_POST;
drop table if exists DG_FORUM_THREAD;
drop table if exists DG_FORUM_ASSET;
drop table if exists DG_FORUM;
drop table if exists DG_CMS_CATEGORY_PARENT_MAP;
drop table if exists DG_CMS_CATEGORY_RELATED_MAP;
drop table if exists DG_CMS_ITEM_CATEGORY_MAP;
drop table if exists DG_CMS_CATEGORY;
drop table if exists DG_CMS;
drop table if exists DG_SITE_DM_CONFIG;
drop table if exists DG_HIGHLIGHT_LINK;
drop table if exists DG_HIGHLIGHT;
drop table if exists poll_option;
drop table if exists poll_voter;
drop table if exists poll;
