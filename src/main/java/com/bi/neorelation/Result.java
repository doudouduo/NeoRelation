package com.bi.neorelation;

import org.neo4j.driver.v1.types.Node;

import java.util.Map;
import java.util.Set;

public class Result {
    private Set<Relation> edge;
    private Set<String>node;
    private Map<String,Integer> level;

    public Set<Relation> getEdge() {
        return edge;
    }

    public void setEdge(Set<Relation> edge) {
        this.edge = edge;
    }

    public Set<String> getNode() {
        return node;
    }

    public void setNode(Set<String> node) {
        this.node = node;
    }

    public Map<String, Integer> getLevel() {
        return level;
    }

    public void setLevel(Map<String, Integer> level) {
        this.level = level;
    }

    public Result(Set<String>node, Set<Relation>edge){
        this.node=node;
        this.edge=edge;
    }

    public  Result(Set<String>node,Set<Relation>edge,Map<String,Integer>level){
        this.node=node;
        this.edge=edge;
        this.level=level;
    }
}
