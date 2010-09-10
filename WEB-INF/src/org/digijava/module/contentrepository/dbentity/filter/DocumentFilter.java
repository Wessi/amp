package org.digijava.module.contentrepository.dbentity.filter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.contentrepository.helper.DocumentData;
import org.digijava.module.contentrepository.jcrentity.Label;

/**
 * @author Alex Gartner
 *
 */
public class DocumentFilter {
	
	public final static String SOURCE_PRIVATE_DOCUMENTS	= "private_documents";
	public final static String SOURCE_TEAM_DOCUMENTS	= "team_documents";
	public final static String SOURCE_SHARED_DOCUMENTS	= "shared_documents";
	public final static String SOURCE_PUBLIC_DOCUMENTS	= "public_documents";
	
	private String name;
	private String source;
	
	private String baseUsername;
	private Long baseTeamId;
	
	private AmpTeamMember user;
		
	private List<String> filterLabelsUUID;
	private List<Label> filterLabels;
	
	private List<Long> filterDocTypeIds;
	private List<String> filterFileType;
	
	private List<Long> filterTeamIds;
	private List<String> filterOwners;
	
	
	
	public DocumentFilter(String source, List<String> filterLabelsUUID, List<Long> filterDocTypeIds,
			List<String> filterFileType, List<Long> filterTeamIds,
			List<String> filterOwners, String baseUsername, Long baseTeamId) {
		
		this.source = source;
		
		this.baseUsername		= baseUsername;
		this.baseTeamId			= baseTeamId;
		
		if ( filterLabelsUUID != null && filterLabelsUUID.size() > 0 )
			this.filterLabelsUUID = filterLabelsUUID;
		
		if ( filterDocTypeIds != null && filterDocTypeIds.size() > 0 )
			this.filterDocTypeIds = filterDocTypeIds;
		
		if ( filterFileType != null && filterFileType.size() > 0 )
			this.filterFileType = filterFileType;
		
		if ( filterTeamIds != null && filterTeamIds.size() > 0 )
			this.filterTeamIds = filterTeamIds;
		
		if ( filterOwners != null && filterOwners.size() > 0 )
			this.filterOwners = filterOwners;
		
	}
	
	public Collection<DocumentData> applyFilter(Collection<DocumentData> col) {
		ArrayList<DocumentData> retCol	= new ArrayList<DocumentData>();
		if ( col != null ) {
			for ( DocumentData dd: col ) {
				boolean pass	= true;
				if ( this.filterDocTypeIds != null && !this.filterDocTypeIds.contains( dd.getCmDocTypeId() ) ) 
					pass	= false;
				else if ( this.filterFileType != null && !this.filterFileType.contains( dd.getContentType() ) ) 
					pass	= false;
				else if ( this.filterTeamIds != null && !this.filterTeamIds.contains( dd.getCreatorTeamId() ) )
					pass	= false;
				else if ( this.filterOwners != null && !this.filterOwners.contains( dd.getCreatorEmail() ) )
					pass	= false;
				else if ( this.filterLabelsUUID != null ) {
					if ( dd.getLabels() != null ) {
						ArrayList<String> ddLabelsUUID	= new ArrayList<String>();	
						for ( Label l: dd.getLabels() ) 
							ddLabelsUUID.add( l.getUuid() );
						for ( String uuid: this.filterLabelsUUID ) 
							if ( !ddLabelsUUID.contains(uuid) ) {
								pass	= false;
								break;
							}
					}
					else 
						pass	= false;
						
				}
				
				if (pass)
					retCol.add(dd);
			}
		}
		
		return retCol;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	
	public AmpTeamMember getUser() {
		return user;
	}
	public void setUser(AmpTeamMember user) {
		this.user = user;
	}
	public List<String> getFilterLabelsUUID() {
		return filterLabelsUUID;
	}
	public void setFilterLabelsUUID(List<String> filterLabelsUUID) {
		this.filterLabelsUUID = filterLabelsUUID;
	}
	public List<Label> getFilterLabels() {
		return filterLabels;
	}
	public void setFilterLabels(List<Label> filterLabels) {
		this.filterLabels = filterLabels;
	}
	public List<Long> getFilterDocType() {
		return filterDocTypeIds;
	}
	public void setFilterDocType(List<Long> filterDocTypeIds) {
		this.filterDocTypeIds = filterDocTypeIds;
	}
	public List<String> getFilterFileType() {
		return filterFileType;
	}
	public void setFilterFileType(List<String> filterFileType) {
		this.filterFileType = filterFileType;
	}
	public List<Long> getFilterTeamIds() {
		return filterTeamIds;
	}
	public void setFilterTeamIds(List<Long> filterTeamIds) {
		this.filterTeamIds = filterTeamIds;
	}

	public List<String> getFilterOwners() {
		return filterOwners;
	}

	public void setFilterOwners(List<String> filterOwners) {
		this.filterOwners = filterOwners;
	}

	public String getBaseUsername() {
		if ( this.user != null ) {
			return this.user.getUser().getEmail();
		}
		return baseUsername;
	}

	public void setBaseUsername(String baseUsername) {
		this.baseUsername = baseUsername;
	}

	public Long getBaseTeamId() {
		if ( this.user != null ) {
			return this.user.getAmpTeam().getAmpTeamId();
		}
		return baseTeamId;
	}

	public void setBaseTeamId(Long baseTeamId) {
		this.baseTeamId = baseTeamId;
	}

	
	
}
