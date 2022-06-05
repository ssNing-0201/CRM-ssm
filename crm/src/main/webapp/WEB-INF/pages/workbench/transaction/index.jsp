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
<link href="jquery/bs_pagination-master/css/jquery.bs_pagination.min.css" type="text/css" rel="stylesheet">

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>
<script type="text/javascript" src="jquery/bs_pagination-master/js/jquery.bs_pagination.min.js"></script>
<script type="text/javascript" src="jquery/bs_pagination-master/localization/en.js"></script>

<script type="text/javascript">

	$(function(){
		tranForPage(1,10);
		//查询按钮添加事件
		$("#queryBtn").click(function () {
			tranForPage(1,$("#page").bs_pagination("getOption","rowsPerPage"));
			$("#queryForm")[0].reset();
		})
		//全选功能
		$("#checkAll").click(function () {
			$("#tbody input[type='checkbox']").prop("checked",this.checked)
		})
		$("#tbody").on("click",$("#tbody input[type='checkbox']"),function () {
			$("#checkAll").prop("checked",$("#tbody input[type='checkbox']").size()==$("#tbody input[type='checkbox']:checked").size())
		})
		$("#editBtn").click(function () {
			var id = $("#tbody input[type='checkbox']:checked");
			if (id.size()==null){
				alert("必须选择一条记录!");
				return;
			}
			if (id.size()>1){
				alert("一次只能修改一条记录!");
				return;
			}
			id = id.val();
			window.location.href="workbench/transaction/toEdit.do?id="+id;
		})

	});
/*---------------------------------------------------------------------------------------------------------*/
//分页函数
	function tranForPage(pageNo, pageSize) {
		//收集参数
		var owner = $.trim($("#query-owner").val());
		var name = $.trim($("#query-name").val());
		var customerName = $.trim($("#query-customerName").val());
		var stage = $.trim($("#query-stage").val());
		var transactionType = $.trim($("#query-transactionType").val());
		var source = $.trim($("#query-source").val());
		var contactsName = $.trim($("#query-contactsName").val());
		//表单验证

		//发送请求
		$.ajax({
			url:"workbench/transaction/queryTranForPage.do",
			data:{
				owner:owner,
				name:name,
				customerName:customerName,
				stage:stage,
				transactionType:transactionType,
				source:source,
				contactsName:contactsName,
				pageNo:pageNo,
				pageSize:pageSize
			},
			type:"post",
			dataType:"json",
			success:function (data) {
				var html = "";
				$.each(data.tranList,function (index, obj) {
					html +='<tr>';
					html +='<td><input id="'+obj.id+'" type="checkbox" value="'+obj.id+'" /></td>';
					html +='<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'workbench/transaction/detailTran.do?tranId='+obj.id+'\';">'+obj.name+'</a></td>';
					html +='<td>'+obj.customerId+'</td>';
					html +='<td>'+obj.stage+'</td>';
					html +='<td>'+obj.type+'</td>';
					html +='<td>'+obj.owner+'</td>';
					html +='<td>'+obj.source+'</td>';
					html +='<td>'+obj.contactsId+'</td>';
					html +='</tr>';
				})
				$("#checkAll").prop("checked",false);
				$("#tbody").html(html);

				//计算总页数
				var totalPages = data.totalRows%pageSize==0?data.totalRows/pageSize:parseInt(data.totalRows/pageSize)+1;
				//分页插件
				$("#page").bs_pagination({
					currentPage: pageNo, // 页码
					rowsPerPage: pageSize, // 每页显示的记录条数
					maxRowsPerPage: 10, // 每页最多显示的记录条数
					totalPages: totalPages, // 总页数
					totalRows: data.totalRows, // 总记录条数

					visiblePageLinks: 20, // 显示几个卡片

					showGoToPage:true,//是否显示跳转到某页
					showRowsPerPage:true,//是否显示每页显示条数
					showRowsInfo:true,//是否显示记录信息
					/*showRowsDefaultInfo: true,*/

					onChangePage : function(event, data){
						tranForPage(data.currentPage , data.rowsPerPage);
					}
				})
			}
		})
	}

</script>
</head>
<body>

	
	
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>交易列表</h3>
			</div>
		</div>
	</div>
	
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
	
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">
		
			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" id="queryForm" role="form" style="position: relative;top: 8%; left: 5px;">
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" id="query-owner" type="text">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input class="form-control" id="query-name" type="text">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">客户名称</div>
				      <input class="form-control" id="query-customerName" type="text">
				    </div>
				  </div>
				  
				  <br>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">阶段</div>
					  <select class="form-control" id="quert-stage">
					  	<option></option>
					  	<C:forEach items="${stageList}" var="s">
							<option value="${s.id}">${s.value}</option>
						</C:forEach>
					  </select>
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">类型</div>
					  <select class="form-control" id="query-transactionType">
					  	<option></option>
					  	<c:forEach items="${transactionTypeList}" var="t">
							<option value="${t.id}">${t.value}</option>
						</c:forEach>
					  </select>
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">来源</div>
				      <select class="form-control" id="query-source">
						  <option></option>
						  <c:forEach items="${sourceList}" var="s">
							  <option value="${s.id}">${s.value}</option>
						  </c:forEach>
						</select>
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">联系人名称</div>
				      <input class="form-control" id="query-contactsName" type="text">
				    </div>
				  </div>
				  
				  <button type="button" id="queryBtn" class="btn btn-default">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 10px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button type="button" class="btn btn-primary" onclick="window.location.href='workbench/transaction/toSave.do';"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" id="editBtn"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				
				
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input id="checkAll" type="checkbox" /></td>
							<td>名称</td>
							<td>客户名称</td>
							<td>阶段</td>
							<td>类型</td>
							<td>所有者</td>
							<td>来源</td>
							<td>联系人名称</td>
						</tr>
					</thead>
					<tbody id="tbody">
						<%--<tr>
							<td><input type="checkbox" /></td>
							<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.jsp';">动力节点-交易01</a></td>
							<td>动力节点</td>
							<td>谈判/复审</td>
							<td>新业务</td>
							<td>zhangsan</td>
							<td>广告</td>
							<td>李四</td>
						</tr>
                        <tr class="active">
                            <td><input type="checkbox" /></td>
                            <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.jsp';">动力节点-交易01</a></td>
                            <td>动力节点</td>
                            <td>谈判/复审</td>
                            <td>新业务</td>
                            <td>zhangsan</td>
                            <td>广告</td>
                            <td>李四</td>
                        </tr>--%>
					</tbody>
				</table>
				<div id="page"/>
			</div>
			
			<%--<div style="height: 50px; position: relative;top: 20px;">
				<div>
					<button type="button" class="btn btn-default" style="cursor: default;">共<b>50</b>条记录</button>
				</div>
				<div class="btn-group" style="position: relative;top: -34px; left: 110px;">
					<button type="button" class="btn btn-default" style="cursor: default;">显示</button>
					<div class="btn-group">
						<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
							10
							<span class="caret"></span>
						</button>
						<ul class="dropdown-menu" role="menu">
							<li><a href="#">20</a></li>
							<li><a href="#">30</a></li>
						</ul>
					</div>
					<button type="button" class="btn btn-default" style="cursor: default;">条/页</button>
				</div>
				<div style="position: relative;top: -88px; left: 285px;">
					<nav>
						<ul class="pagination">
							<li class="disabled"><a href="#">首页</a></li>
							<li class="disabled"><a href="#">上一页</a></li>
							<li class="active"><a href="#">1</a></li>
							<li><a href="#">2</a></li>
							<li><a href="#">3</a></li>
							<li><a href="#">4</a></li>
							<li><a href="#">5</a></li>
							<li><a href="#">下一页</a></li>
							<li class="disabled"><a href="#">末页</a></li>
						</ul>
					</nav>
				</div>
			</div>--%>
			
		</div>
		
	</div>
</body>
</html>