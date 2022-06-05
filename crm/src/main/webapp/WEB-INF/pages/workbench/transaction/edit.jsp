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
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>

<script type="text/javascript">
	$(function () {
		//添加日历插件
		$(".time").datetimepicker({
			minView: "month",
			language:  'zh-CN',
			format: 'yyyy-mm-dd',
			autoclose: true,
			todayBtn: true,
			clearBtn:true,
			pickerPosition: "bottom-left"
		})

		$("#create-customerName").val("${tran.customerId}");
		$("#create-stage").val("${tran.stage}");
		$("#create-owner").val("${tran.owner}");
		$("#create-nextContactTime").val("${tran.nextContactTime}");
		$("#create-name").val("${tran.name}");
		$("#create-money").val("${tran.money}");
		$("#create-expectedDate").val("${tran.expectedDate}");
		$("#create-contactSummary").val("${tran.contactSummary}");
		$("#create-contactsName").val("${tran.contactsId}");
		$("#create-activityName").val("${tran.activityId}");
		$("#create-description").val("${tran.description}");
		$("#create-source").val("${tran.source}");
		$("#create-type").val("${tran.type}");



		$("#findActivityBtn").click(function () {
			$("#create-activity").text("");
			$("#findMarketActivity").modal("show");
			$("#queryActivity").text("");
			$("#queryActivity").keyup();
		})
		$("#queryActivity").keyup(function () {
			var activityName = $.trim($("#queryActivity").val());
			$.ajax({
				url:"workbench/transaction/queryActivityForCreateTran.do",
				data:{activityName:activityName},
				type:"post",
				dataType:"json",
				success:function (data) {
					var html = "";
					$.each(data,function (index, obj) {
						html +='<tr>';
						html +='<td><input value="'+obj.id+'" type="radio" activityName="'+obj.name+'" name="activity"/></td>';
						html +='<td id="activity_name">'+obj.name+'</td>';
						html +='<td>'+obj.startDate+'</td>';
						html +='<td>'+obj.endDate+'</td>';
						html +='<td>'+obj.owner+'</td>';
						html +='</tr>';
					})
					$("#activityTbody").html(html);
				}
			})
		});
		$("#activityTbody").on("click","input[type='radio']",function () {
			//获取参数
			var activityId = this.value;
			var activityName = $("#activityTbody input[type='radio']:checked").attr("activityName");
			$("#create-activityId").val(activityId);
			$("#create-activity").val(activityName);
			$("#findMarketActivity").modal("hide");
		} )
		$("#findContactsBtn").click(function () {
			$("#create-contactsName").text("");
			$("#findContacts").modal("show");
			$("#queryContacts").text("");
			$("#queryContacts").keyup();
		})
		$("#queryContacts").keyup(function () {
			var contactsName = $.trim($("#queryContacts").val());
			//发送请求
			$.ajax({
				url: "workbench/transaction/queryContactsForCreateTran.do",
				data: {contactsName:contactsName},
				type: "post",
				dataType: "json",
				success:function (data) {
					var html = "";
					$.each(data,function (index, obj) {
						html += '<tr>';
						html += '<td><input value="'+obj.id+'" contactsName="'+obj.fullName+obj.appellation+'" type="radio" name="activity"/></td>';
						html += '<td>'+obj.fullName+obj.appellation+'</td>';
						html += '<td>'+obj.email+'</td>';
						html += '<td>'+obj.mphone+'</td>';
						html += '</tr>';
					})
					$("#contactsTbody").html(html);
				}
			})
		})
		$("#contactsTbody").on("click","input[type='radio']",function () {
			var id = this.value;
			var name=$(this).attr("contactsName");
			$("#create-contactsId").val(id);
			$("#create-contactsName").val(name);
			$("#findContacts").modal("hide");
		})
		//改变阶段自动获取可能性
		$("#create-stage").change(function () {
			//获取参数
			var stage = $("#create-stage").find("option:selected").text();
			if (stage==""){
				$("#create-possibility").val("");
				return;
			}
			//发送请求
			$.ajax({
				url:"workbench/transaction/getpossibilityByStage.do",
				data:{stage:stage},
				type:"post",
				dataType:"json",
				success:function (data) {
					$("#create-possibility").val(data+"%");
				}
			})
		})
		$("#create-customerName").typeahead({
			source:function (jquery, process) {
				$.ajax({
					url:"workbench/transaction/queryAllCustomerName.do",
					data:{customerName:jquery},
					type:"post",
					dataType:"json",
					success:function (data) {
						process(data);
					}
				});
			}
		})

		$("#saveTranBtn").click(function () {
			//收集参数
			var customerName = $.trim($("#create-customerName").val());
			var stage = $.trim($("#create-stage").val());
			var owner = $.trim($("#create-owner").val());
			var nextContactTime = $.trim($("#create-nextContactTime").val());
			var name = $.trim($("#create-name").val());
			var money = $.trim($("#create-money").val());
			var expectedDate = $.trim($("#create-expectedDate").val());
			var contactSummary = $.trim($("#create-contactSummary").val());
			var contactsId = $.trim($("#create-contactsId").val());
			var activityId = $.trim($("#create-activityId").val());
			var description = $.trim($("#create-description").val());
			var source = $.trim($("#create-source").val());
			var type = $.trim($("#create-type").val());
			var id = "${tran.id}";
			//表单验证
			//发送请求
			$.ajax({
				url:"workbench/transaction/saveEditTranById.do",
				data:{
					id:id,
					customerName:customerName,
					stage:stage,
					owner:owner,
					nextContactTime:nextContactTime,
					name:name,
					money:money,
					expectedDate:expectedDate,
					contactSummary:contactSummary,
					contactsId:contactsId,
					activityId:activityId,
					description:description,
					source:source,
					type:type
				},
				type:"post",
				dataType:"json",
				success:function (data) {
					if (data.code=="1"){
						$("#saveForm")[0].reset();
						window.location.href="workbench/transaction/index.do";
					}else {
						alert(data.message);
					}
				}
			})
		})


	});
</script>
</head>
<body>

	<!-- 查找市场活动 -->	
	<div class="modal fade" id="findMarketActivity" role="dialog">
		<div class="modal-dialog" role="document" style="width: 80%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title">查找市场活动</h4>
				</div>
				<div class="modal-body">
					<div class="btn-group" style="position: relative; top: 18%; left: 8px;">
						<form class="form-inline" role="form">
						  <div class="form-group has-feedback">
						    <input type="text" class="form-control" style="width: 300px;" placeholder="请输入市场活动名称，支持模糊查询">
						    <span class="glyphicon glyphicon-search form-control-feedback"></span>
						  </div>
						</form>
					</div>
					<table id="activityTable3" class="table table-hover" style="width: 900px; position: relative;top: 10px;">
						<thead>
							<tr style="color: #B3B3B3;">
								<td></td>
								<td>名称</td>
								<td>开始日期</td>
								<td>结束日期</td>
								<td>所有者</td>
							</tr>
						</thead>
						<tbody id="activityTbody">
							<%--<tr>
								<td><input type="radio" name="activity"/></td>
								<td>发传单</td>
								<td>2020-10-10</td>
								<td>2020-10-20</td>
								<td>zhangsan</td>
							</tr>
							<tr>
								<td><input type="radio" name="activity"/></td>
								<td>发传单</td>
								<td>2020-10-10</td>
								<td>2020-10-20</td>
								<td>zhangsan</td>
							</tr>--%>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>

	<!-- 查找联系人 -->	
	<div class="modal fade" id="findContacts" role="dialog">
		<div class="modal-dialog" role="document" style="width: 80%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title">查找联系人</h4>
				</div>
				<div class="modal-body">
					<div class="btn-group" style="position: relative; top: 18%; left: 8px;">
						<form class="form-inline" role="form">
						  <div class="form-group has-feedback">
						    <input type="text" class="form-control" style="width: 300px;" placeholder="请输入联系人名称，支持模糊查询">
						    <span class="glyphicon glyphicon-search form-control-feedback"></span>
						  </div>
						</form>
					</div>
					<table id="activityTable" class="table table-hover" style="width: 900px; position: relative;top: 10px;">
						<thead>
							<tr style="color: #B3B3B3;">
								<td></td>
								<td>名称</td>
								<td>邮箱</td>
								<td>手机</td>
							</tr>
						</thead>
						<tbody id="contactsTbody">
							<%--<tr>
								<td><input type="radio" name="activity"/></td>
								<td>李四</td>
								<td>lisi@bjpowernode.com</td>
								<td>12345678901</td>
							</tr>
							<tr>
								<td><input type="radio" name="activity"/></td>
								<td>李四</td>
								<td>lisi@bjpowernode.com</td>
								<td>12345678901</td>
							</tr>--%>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
	
	
	<div style="position:  relative; left: 30px;">
		<h3>修改交易</h3>
	  	<div style="position: relative; top: -40px; left: 70%;">
			<button id="saveTranBtn" type="button" class="btn btn-primary">保存</button>
			<button type="button" class="btn btn-default">取消</button>
		</div>
		<hr style="position: relative; top: -40px;">
	</div>
	<form class="form-horizontal" role="form" style="position: relative; top: -30px;">
		<div class="form-group">
			<label for="create-owner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
				<select class="form-control" id="create-owner">
					<c:forEach items="${userList}" var="u">
						<option value="${u.id}">${u.name}</option>
					</c:forEach>
				</select>
			</div>
			<label for="create-money" class="col-sm-2 control-label">金额</label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="create-money">
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-name" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="create-name">
			</div>
			<label for="create-expectedDate" class="col-sm-2 control-label">预计成交日期<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control time" id="create-expectedDate" readonly>
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-customerName" class="col-sm-2 control-label">客户名称<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="create-customerName" placeholder="支持自动补全，输入客户不存在则新建">
			</div>
			<label for="create-stage" class="col-sm-2 control-label">阶段<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
			  <select class="form-control" id="create-stage">
			  	<option></option>
				  <c:forEach items="${stageList}" var="s">
					  <option value="${s.id}">${s.value}</option>
				  </c:forEach>
			  </select>
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-type" class="col-sm-2 control-label">类型</label>
			<div class="col-sm-10" style="width: 300px;">
				<select class="form-control" id="create-type">
				  <option></option>
					<c:forEach items="${transactionTypeList}" var="t">
						<option value="${t.id}">${t.value}</option>
					</c:forEach>
				</select>
			</div>
			<label for="create-possibility" class="col-sm-2 control-label">可能性</label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="create-possibility" readonly>
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-source" class="col-sm-2 control-label">来源</label>
			<div class="col-sm-10" style="width: 300px;">
				<select class="form-control" id="create-source">
				  <option></option>
					<c:forEach items="${sourceList}" var="s">
						<option value="${s.id}">${s.value}</option>
					</c:forEach>
				</select>
			</div>
			<label for="create-activityName" class="col-sm-2 control-label">市场活动源&nbsp;&nbsp;<a href="javascript:void(0);" data-toggle="modal" data-target="#findMarketActivity"><span class="glyphicon glyphicon-search"></span></a></label>
			<div id="findActivityBtn" class="col-sm-10" style="width: 300px;">
				<input type="hidden" id="create-activityId">
				<input type="text" class="form-control" id="create-activityName" readonly>
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-contactsName" class="col-sm-2 control-label">联系人名称&nbsp;&nbsp;<a href="javascript:void(0);" data-toggle="modal" data-target="#findContacts"><span class="glyphicon glyphicon-search"></span></a></label>
			<div  id="findContactsBtn" class="col-sm-10" style="width: 300px;">
				<input type="hidden" id="create-contactsId">
				<input type="text" class="form-control" id="create-contactsName" readonly>
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-description" class="col-sm-2 control-label">描述</label>
			<div class="col-sm-10" style="width: 70%;">
				<textarea class="form-control" rows="3" id="create-description"></textarea>
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-contactSummary" class="col-sm-2 control-label">联系纪要</label>
			<div class="col-sm-10" style="width: 70%;">
				<textarea class="form-control" rows="3" id="create-contactSummary"></textarea>
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-nextContactTime" class="col-sm-2 control-label">下次联系时间</label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control time" id="create-nextContactTime" readonly>
			</div>
		</div>
		
	</form>
</body>
</html>