
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="C" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String basePath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
%>
<html>
<head>
    <base href="<%=basePath%>">
<meta charset="UTF-8">
<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="jquery/echarts/echarts.min.js"></script>
<script type="text/javascript">
    $(function () {
        //初始化图表实例
        var myChart = echarts.init(document.getElementById('main'));
        $.ajax({
            url:"",
            type: "post",
            dataType:"json",
            success:function (data) {

                myChart.setOption({
                    title: {
                        text: '交易统计图表',
                        subtext:'交易表中各个阶段的数量'
                    },
                    tooltip: {
                        trigger: 'item',
                        formatter: '{a} <br/>{b} : {c}'
                    },
                    toolbox: {
                        feature: {
                            dataView: { readOnly: false },
                            restore: {},
                            saveAsImage: {}
                        }
                    },
                    legend: {
                        data: ['Show', 'Click', 'Visit', 'Inquiry', 'Order']
                    },
                    series: [
                        {
                            name: 'Expected',
                            type: 'funnel',
                            left: '10%',
                            width: '80%',
                            label: {
                                formatter: '{b}Expected'
                            },
                            labelLine: {
                                show: false
                            },
                            itemStyle: {
                                opacity: 0.7
                            },
                            emphasis: {
                                label: {
                                    position: 'inside',
                                    formatter: '{b}Expected: {c}'
                                }
                            },
                            data: [
                                { value: 60, name: 'Visit' },
                                { value: 40, name: 'Inquiry' },
                                { value: 20, name: 'Order' },
                                { value: 80, name: 'Click' },
                                { value: 100, name: 'Show' }
                            ]
                        }
                    ]
                });

            }
        });




    });
</script>

    <title>Title</title>
</head>
<body>
    <div id="main" style="width: 600px;height:400px;"></div>
</body>
</html>
