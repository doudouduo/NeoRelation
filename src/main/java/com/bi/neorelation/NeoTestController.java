package com.bi.neorelation;

import jdk.internal.util.xml.XMLStreamException;
import org.neo4j.shell.util.json.JSONArray;
import org.neo4j.shell.util.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


@Controller
public class NeoTestController {
    @Autowired
    private Search search;

    @GetMapping(value="/test_Neo4j")
    public void work()throws Exception{
        System.out.println(search.SingleNodePrefixSearch("李明",2));
        System.out.println(search.SingleNodeSuffixSearch("李芳",2));
        System.out.println(search.DoubleNodeSearch("李明","张三",2));
    }

    @RequestMapping(value = "/relation", method = {RequestMethod.GET, RequestMethod.POST})
    public String index(){
        return "graph";
    }

    @RequestMapping(value = "/search", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String search(@RequestParam("entity1")String entity1, @RequestParam("entity2")String entity2, @RequestParam("jump")String jump) throws Exception{
//        System.out.println(entity1);
//        System.out.println(entity2);
//        System.out.println(jump);
        System.out.println("开始查询");
        Map<String,Integer> level=new HashMap<>();
        Map<String,Integer> lev=new HashMap<>();
        if(jump.equals(""))
            jump = "5";
        int n_step = Integer.parseInt(jump);

        String s;
        Result result;
        Set<String>node=new HashSet<>();
        Set<Relation>edge=new HashSet<>();
        long start_time=System.nanoTime();
        if(entity2.equals("")) {
//            level.put(entity1,0);
            for (int i=1;i<=n_step;++i) {
                result = search.SingleNodePrefixSearch(entity1, i);
                node.addAll(result.getNode());
                edge.addAll(result.getEdge());
                lev=result.getLevel();
                for (String name:lev.keySet()) {
                    if ((level.containsKey(name)&&Math.abs(level.get(name))>Math.abs(lev.get(name)))||!level.containsKey(name)){
                        level.put(name,lev.get(name));
                    }
                }

                result = search.SingleNodeSuffixSearch(entity1, i);
                node.addAll(result.getNode());
                edge.addAll(result.getEdge());
                lev=result.getLevel();
                for (String name:lev.keySet()) {
                    if ((level.containsKey(name)&&Math.abs(level.get(name))>Math.abs(lev.get(name)))||!level.containsKey(name)){
                        level.put(name,lev.get(name));
                    }
                }

                if (node.size()==0){
                    node=search.NodeSearch(entity1);
                }
            }
        }
        else {
            level.put(entity1,0);
            level.put(entity2,0);
            for (int i=1;i<=n_step;++i) {
                result = search.DoubleNodeSearch(entity1, entity2, i);
                node.addAll(result.getNode());
                edge.addAll(result.getEdge());
                lev=result.getLevel();
                for (String name:lev.keySet()) {
                    if ((level.containsKey(name)&&Math.abs(level.get(name))>Math.abs(lev.get(name)))||!level.containsKey(name)){
                        level.put(name,lev.get(name));
                    }
                }

                result = search.DoubleNodeSearch(entity2, entity1, i);
                node.addAll(result.getNode());
                edge.addAll(result.getEdge());
                lev=result.getLevel();
                for (String name:lev.keySet()) {
                    if ((level.containsKey(name)&&Math.abs(level.get(name))>Math.abs(lev.get(name)))||!level.containsKey(name)){
                        level.put(name,lev.get(name));
                    }
                }
            }

            if (node.size()==0){
                node=search.NodeSearch(entity1);
                node.addAll(search.NodeSearch(entity2));
            }
        }
        long end_time=System.nanoTime();
        System.out.println("查询时间："+Double.toString((end_time-start_time)/1e6)+"ms");


        JSONObject Node, Edge, Result;
        JSONArray Nodes, Edges;
        Nodes = new JSONArray();
        Edges = new JSONArray();
        Result = new JSONObject();
        for (String name : node) {
            Node = new JSONObject();
            Node.put("name", name);
            Node.put("level",level.get(name));
            Nodes.put(Node);
        }
        for (Relation relation:edge){
                Edge = new JSONObject();
                Edge.put("source", relation.getSource());
                Edge.put("target", relation.getTarget());
                Edge.put("type", relation.getType());
                Edges.put(Edge);
        }
        Result.put("nodes", Nodes);
        Result.put("edges", Edges);
        s=Result.toString();
        System.out.println(Result.toString());
        return s;
    }

}
