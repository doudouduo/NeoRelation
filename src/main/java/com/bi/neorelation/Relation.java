package com.bi.neorelation;

public class Relation {
    private String source;
    private String target;
    private String type;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Relation(String source,String target,String type){
        this.source=source;
        this.target=target;
        this.type=type;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (this == obj)
            return true;
        if (obj instanceof Relation) {
            Relation relation = (Relation) obj;

            // 比较每个属性的值 一致时才返回true
            if (relation.source.equals(this.source) && relation.target.equals(this.target)&& relation.type.equals(this.type))
                return true;
        }
        return false;
    }

    /**
     * 重写hashcode 方法，返回的hashCode不一样才再去比较每个属性的值
     */
    @Override
    public int hashCode() {
        return source.hashCode() * type.hashCode()*target.hashCode();
    }
}
