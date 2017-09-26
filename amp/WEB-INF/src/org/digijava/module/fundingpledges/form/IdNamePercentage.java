// Generated by delombok at Mon Mar 24 00:10:06 EET 2014
package org.digijava.module.fundingpledges.form;

import org.dgfoundation.amp.algo.AmpCollections;

/**
 * a value with an id, a name and a percentage
 * @author Dolghier Constantin
 */
public class IdNamePercentage implements Comparable<IdNamePercentage>, UniquelyIdentifiable {
    public final Long id;
    public final String name;
    public final Long rootId;
    public final String rootName;
    public final String hierarchicalName;
    
    public Float percentage;
    private long uniqueId = PledgeForm.uniqueIds.getAndIncrement();
    
    public IdNamePercentage(Long id, String name, Long rootId, String rootName, String hierarchicalName) {
        this.id = id;
        this.name = name;
        this.rootId = rootId;
        this.rootName = rootName;
        this.hierarchicalName = hierarchicalName;
    }
    
    public IdNamePercentage(Long id, String name, String hierarchicalName) {
        this(id, name, id, name, hierarchicalName);
    }
    
    @Override
    public int compareTo(IdNamePercentage oth) {
        Integer a = AmpCollections.nullCompare(this.id, oth.id);
        if (a != null) return a;
        a = AmpCollections.nullCompare(this.name, oth.name);
        return a == null ? 0 : a.intValue();
    }
    
    /**
     * returns either {@link #percentage} or null, in case {@link #percentage} = 0
     * @return
     */
    public Float getPercentageOrNull(){
        return (this.percentage == null || Math.abs(this.percentage) < 0.01) ? null : this.percentage;
    }
    
    public String getPercentageDisplayed(){
        return getPercentageOrNull() == null ? "(n/a) " : String.format("%.2f", this.percentage);
    }
    
    public IdNamePercentage setPercentageChained(Float perc) {
        setPercentage(perc);
        return this;
    }
    
    @java.lang.SuppressWarnings("all")
    public Long getId() {
        return this.id;
    }
    
    @java.lang.SuppressWarnings("all")
    public String getName() {
        return this.name;
    }
    
    @java.lang.SuppressWarnings("all")
    public Long getRootId() {
        return this.rootId;
    }
    
    @java.lang.SuppressWarnings("all")
    public String getRootName() {
        return this.rootName;
    }
    
    @java.lang.SuppressWarnings("all")
    public String getHierarchicalName() {
        return this.hierarchicalName;
    }
    
    @java.lang.SuppressWarnings("all")
    public Float getPercentage() {
        return this.percentage;
    }
    
    @java.lang.SuppressWarnings("all")
    public long getUniqueId() {
        return this.uniqueId;
    }
    
    @java.lang.SuppressWarnings("all")
    public void setPercentage(final Float percentage) {
        this.percentage = percentage == null ? 0.0f : percentage;
    }
    
    @java.lang.SuppressWarnings("all")
    public void setUniqueId(final long uniqueId) {
        this.uniqueId = uniqueId;
    }
    
    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    public boolean equals(final java.lang.Object o) {
        if (o == this) return true;
        if (!(o instanceof IdNamePercentage)) return false;
        final IdNamePercentage other = (IdNamePercentage)o;
        if (!other.canEqual((java.lang.Object)this)) return false;
        final java.lang.Object this$id = this.getId();
        final java.lang.Object other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
        final java.lang.Object this$name = this.getName();
        final java.lang.Object other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false;
        final java.lang.Object this$rootId = this.getRootId();
        final java.lang.Object other$rootId = other.getRootId();
        if (this$rootId == null ? other$rootId != null : !this$rootId.equals(other$rootId)) return false;
        final java.lang.Object this$rootName = this.getRootName();
        final java.lang.Object other$rootName = other.getRootName();
        if (this$rootName == null ? other$rootName != null : !this$rootName.equals(other$rootName)) return false;
        final java.lang.Object this$hierarchicalName = this.getHierarchicalName();
        final java.lang.Object other$hierarchicalName = other.getHierarchicalName();
        if (this$hierarchicalName == null ? other$hierarchicalName != null : !this$hierarchicalName.equals(other$hierarchicalName)) return false;
        final java.lang.Object this$percentage = this.getPercentage();
        final java.lang.Object other$percentage = other.getPercentage();
        if (this$percentage == null ? other$percentage != null : !this$percentage.equals(other$percentage)) return false;
        if (this.getUniqueId() != other.getUniqueId()) return false;
        return true;
    }
    
    @java.lang.SuppressWarnings("all")
    public boolean canEqual(final java.lang.Object other) {
        return other instanceof IdNamePercentage;
    }
    
    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final java.lang.Object $id = this.getId();
        result = result * PRIME + ($id == null ? 0 : $id.hashCode());
        final java.lang.Object $name = this.getName();
        result = result * PRIME + ($name == null ? 0 : $name.hashCode());
        final java.lang.Object $rootId = this.getRootId();
        result = result * PRIME + ($rootId == null ? 0 : $rootId.hashCode());
        final java.lang.Object $rootName = this.getRootName();
        result = result * PRIME + ($rootName == null ? 0 : $rootName.hashCode());
        final java.lang.Object $hierarchicalName = this.getHierarchicalName();
        result = result * PRIME + ($hierarchicalName == null ? 0 : $hierarchicalName.hashCode());
        final java.lang.Object $percentage = this.getPercentage();
        result = result * PRIME + ($percentage == null ? 0 : $percentage.hashCode());
        final long $uniqueId = this.getUniqueId();
        result = result * PRIME + (int)($uniqueId >>> 32 ^ $uniqueId);
        return result;
    }
    
    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    public java.lang.String toString() {
        return "IdNamePercentage(id=" + this.getId() + ", name=" + this.getName() + ", rootId=" + this.getRootId() + ", rootName=" + this.getRootName() + ", hierarchicalName=" + this.getHierarchicalName() + ", percentage=" + this.getPercentage() + ", uniqueId=" + this.getUniqueId() + ")";
    }
}
