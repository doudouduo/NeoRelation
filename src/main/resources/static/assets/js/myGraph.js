function search() {
    var entity1 = $("#entity1").val();
    var entity2 = $("#entity2").val();
    var jump = $("#jump").val();
    $.ajax({
        type: "get",
        url: '/search?entity1=' + entity1 + '&entity2=' + entity2 + '&jump=' + jump,
        data: {},
        success: function (data) {
            var graph = JSON.parse(data);

            var dom = document.getElementById("container");
            var myChart = echarts.init(dom);
            var option = null;

            if(graph.edges.length === 0 && graph.nodes.length === 0) {
                option = {};
                myChart.setOption(option, true);
                alert("该实体不存在！");
                return
            }

            var categories = [];
            // categories[0] = { name: 'found' };
            // categories[1] = { name: 'prefix' };
            // categories[2] = { name: 'suffix' };
            categories[0] = { name: '0' };
            categories[1] = { name: '1' };
            categories[2] = { name: '2' };
            categories[3] = { name: '3' };
            categories[4] = { name: '4' };
            categories[5] = { name: '5及以上' };

            graph.nodes.forEach(function (node) {
                if(graph.edges.length === 0) {
                    node.value = 0;
                }
                else {
                    node.value = node.level;
                }
                if(Math.abs(node.value) < 5) {
                    node.symbolSize = 60-10*(Math.abs(node.value)-1);
                }
                // if(node.value > 0) {
                //     node.category = 1;
                // }
                // else if(node.value < 0) {
                //     node.category = 2;
                // }
                // else {
                //     node.category = 0;
                //     // node.itemStyle = { color: '#c62828'};
                // }
                if(node.value < 5)
                    node.category = Math.abs(node.value);
                else
                    node.category = 5;
            });

            if(graph.edges.length === 0) {
                option = {
                    title: {
                        text: '未找到该实体，您要找的是不是以下实体？',
                        top: '3%',
                        left: '3%'
                    },
                    tooltip: {
                        show: true,
                        formatter: function(param) {
                            return param.data.name;
                        }
                    },
                    toolbox: {
                        show : true,
                        feature : {
                            dataView: {show: true, readOnly: false},
                            restore : {show: true},
                            saveAsImage : {show: true}
                        }
                    },
                    animationEasingUpdate: "quinticInOut",
                    animationDurationUpdate: 100,
                    series : [
                        {
                            name: '实体关系图',
                            type: 'graph',
                            layout: 'force',
                            draggable: true,
                            data: graph.nodes,
                            categories: categories,
                            roam: true,
                            label: {
                                normal: {
                                    show: true,
                                    position: 'inside',
                                    formatter: function(param) {
                                        return param.data.name;
                                    }
                                }
                            },
                            force: {
                                edgeLength: [200, 300],
                                repulsion: 200
                            }
                        }
                    ]
                };
            }
            else {
                option = {
                    title: {
                        text: '实体关系图',
                        top: '3%',
                        left: '3%'
                    },
                    tooltip: {
                        show: true,
                        formatter: function(param) {
                            if (param.dataType === 'edge') {
                                return param.data.source + ' > ' + param.data.target + ': ' + param.data.type;
                            }
                            return param.data.name + '  ' + param.data.value;
                        }
                    },
                    toolbox: {
                        show : true,
                        feature : {
                            dataView: {show: true, readOnly: false},
                            restore : {show: true},
                            saveAsImage : {show: true}
                        }
                    },
                    legend: [{
                        // selectedMode: 'single',
                        data: categories.map(function (a) {
                            return a.name;
                        })
                    }],
                    animationEasingUpdate: "quinticInOut",
                    animationDurationUpdate: 100,
                    series : [
                        {
                            name: '实体关系图',
                            type: 'graph',
                            layout: 'force',
                            draggable: true,
                            symbolSize: 20,
                            data: graph.nodes,
                            links: graph.edges,
                            categories: categories,
                            roam: true,
                            focusNodeAdjacency: true,
                            lineStyle: {
                                normal: {
                                    opacity: 0.8
                                }
                            },
                            label: {
                                normal: {
                                    show: true,
                                    position: 'inside',
                                    formatter: function(param) {
                                        return param.data.name;
                                    }
                                }
                            },
                            edgeLabel: {
                                normal: {
                                    show: true,
                                    textStyle: {
                                        fontSize: 14
                                    },
                                    capacity: 0.8,
                                    formatter: function(param) {
                                        return param.data.type;
                                    }
                                }
                            },
                            edgeSymbol: ['none', 'arrow'],
                            force: {
                                edgeLength: [200, 300],
                                repulsion: 200
                            }
                        }
                    ]
                };
            }

            myChart.setOption(option);
            if (option && typeof option === "object") {
                myChart.setOption(option, true);
            }
            // myChart.on('click', function (handler){
            //     console.dir(handler);
            //     // console.dir(context);
            //     //获取节点点击的数组序号
            //     var arrayIndex = handler.dataIndex;
            //     //获取数据
            //     var urlParam = graph.nodes[arrayIndex].name;
            //     console.log(urlParam);
            // });
        }
    });
}
