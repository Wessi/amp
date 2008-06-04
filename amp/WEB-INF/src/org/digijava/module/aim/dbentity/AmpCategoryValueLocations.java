package org.digijava.module.aim.dbentity;

/**
 *
 * @author medea
 */
public class AmpCategoryValueLocations {

    private Long id;
    private String name;
    private AmpCategoryValue parentCategoryValue;
    private AmpCategoryValueLocations parentLocation;
    private String description;
    private String gsLat;
    private String gsLong;
    private String geoCode;
    private String code;
    private String iso3;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGeoCode() {
        return geoCode;
    }

    public void setGeoCode(String geoCode) {
        this.geoCode = geoCode;
    }

    public String getGsLat() {
        return gsLat;
    }

    public void setGsLat(String gsLat) {
        this.gsLat = gsLat;
    }

    public String getGsLong() {
        return gsLong;
    }

    public void setGsLong(String gsLong) {
        this.gsLong = gsLong;
    }

    public String getIso() {
        return iso;
    }

    public void setIso(String iso) {
        this.iso = iso;
    }

    public String getIso3() {
        return iso3;
    }

    public void setIso3(String iso3) {
        this.iso3 = iso3;
    }
    private String iso;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AmpCategoryValue getParentCategoryValue() {
        return parentCategoryValue;
    }

    public void setParentCategoryValue(AmpCategoryValue parentCategoryValue) {
        this.parentCategoryValue = parentCategoryValue;
    }

    public AmpCategoryValueLocations getParentLocation() {
        return parentLocation;
    }

    public void setParentLocation(AmpCategoryValueLocations parentLocation) {
        this.parentLocation = parentLocation;
    }
}
