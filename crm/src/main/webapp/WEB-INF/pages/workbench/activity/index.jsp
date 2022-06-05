<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
String basePath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
%>
<html>
<head>
	<base href="<%=basePath%>">
<meta charset="UTF-8">
	<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>

<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />
<link href="jquery/bs_pagination-master/css/jquery.bs_pagination.min.css" type="text/css" rel="stylesheet">

<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>
<script type="text/javascript" src="jquery/bs_pagination-master/js/jquery.bs_pagination.min.js"></script>
<script type="text/javascript" src="jquery/bs_pagination-master/localization/en.js"></script>

<script type="text/javascript">

	$(function(){
		queryActivityByConditionForPage(1,10);
		//为创建市场订单时间，使用日历控件
		//在日历标签后添加readonly（只读）不能输入，只能选择日期
		$(".time").datetimepicker({
			minView: "month",
			language:  'zh-CN',
			format: 'yyyy-mm-dd',
			autoclose: true,
			todayBtn: true,
			clearBtn:true,
			pickerPosition: "bottom-left"
		})
		//给创建按钮添加单击事件（打开模态窗口）
		$("#createActivityBtn").click(function (){
			//弹出创建市场活动的模态窗口
			$("#createActivityModal").modal("show");
		});
		//给保存按钮添加单击事件
		$("#insertSaveBtn").click(function (){
			//收集参数
			var owner = $("#create-marketActivityOwner").val();
			var name = $.trim($("#create-marketActivityName").val());
			var startData =$.trim($("#create-startData").val());
			var endData = $.trim($("#create-endData").val());
			var cost = $.trim($("#create-cost").val());
			var description = $.trim($("#create-description").val());
			//发请求前先做表单验证
			if (owner==""){
				alert("所有者不能为空!");
				return;
			}
			if (name==""){
				alert("名称不能为空!");
				return;
			}
			if (startData!=""&&endData!=""){
				if (endData<startData){
					alert("结束日期不能比开始日期小!")
					return;
				}
			}
			/*
			* 正则表达式用以验证
			* 用来定义字符串的匹配模式，用来判断指定的具体字符串是否符合字符串的匹配模式
			*/
			var regExp = /^(([1-9]\d*)|0)$/;
			if (!regExp.test(cost)){
				alert("成本只能是非负整数!");
				return;
			}
			//数据判定合法后发送请求
			$.ajax({
				url:"workbench/activity/saveCreateActivity.do",
				data:{
					"owner":owner,
					"name":name,
					"startDate":startData,
					"endDate":endData,
					"cost":cost,
					"description":description
				},
				type:"post",
				dataType:"json",
				success:function (data){
					if (data.code=="1"){
						//登陆成功后关闭模态窗口
						$("#createActivityModal").modal("hide");
						//重置表单
						$("#creayeActivityFrom").get(0).reset()
						//刷新市场活动列表，显示第一页数据
						queryActivityByConditionForPage(1,$("#page").bs_pagination("getOption","rowsPerPage"));
					}else {
						alert(data.message);
						$("#createActivityModal").modal("show");//这行代码可以不写
					}
				}
			})
		});

		//给查询按钮添加单击事件
		$("#queryActivityBtn").click(function (){
			queryActivityByConditionForPage(1,$("#page").bs_pagination("getOption","rowsPerPage"));
		});

		//给全选按钮添加单击事件
		$("#checkAll").click(function () {
			//如果全选是选中状态列表中所有都选中
			/*if (this.checked){
				$("#tBody input[type='checkbox']").prop("checked",true);
			}else {
				$("#tBody input[type='checkbox']").prop("checked",false);
			}*/
			$("#tBody input[type='checkbox']").prop("checked",this.checked);
		})
		//给每个选项添加单击事件
		$("#tBody").on("click",$("#tBody input[type='checkbox']"),function () {
			$("#checkAll").prop("checked",$("#tBody input[type='checkbox']").size()==$("#tBody input[type='checkbox']:checked").size())
		});

		//给删除按钮添加单击事件
		$("#deleteActivityBtn").click(function () {
			//收集参数,获取列表中被选中的chechBox
			var id = $("#tBody input[type='checkbox']:checked");
			if (id.size()==0){
				alert("请选择要删除的市场活动!")
				return
			}
			var ids = "";
			$.each(id,function () {
				ids+="id="+this.value+"&";
			})
			ids=ids.substring(0,ids.length-1);
			//发送删除请求
			$.ajax({
				url:"workbench.activity/deleteActivityByIds.do",
				data:ids,
				type:"post",
				dataType:"json",
				success:function (data){
					if (data.code==1){
						queryActivityByConditionForPage(1,$("#page").bs_pagination("getOption","rowsPerPage"))
					}else {
						alert(data.message);
					}
				}
			})
		})

		//给修改按钮添加单击事件
		$("#editActivityBtn").click(function (){

			//获取被选中的checkbox
			var checkedId = $("#tBody input[type='checkbox']:checked");
			if (checkedId.size()==0){
				alert("请选择要修改的市场活动!");
				return;
			}
			if (checkedId.size()>1){
				alert("只能选中一条修改!");
				return;
			}
			var id = checkedId.val();
			//发送请求
			$.ajax({
				url:"workbench.activity/queryActivityById.do",
				data:{"id":id},
				type:"post",
				dataType:"json",
				success:function (data){
					//铺数据
					$("#edit-id").val(data.id)
					$("#edit-marketActivityOwner").val(data.owner);
					$("#edit-marketActivityName").val(data.name);
					$("#edit-startDate").val(data.startDate);
					$("#edit-endDate").val(data.endDate);
					$("#edit-cost").val(data.cost);
					$("#edit-description").val(data.description);
					//打开模态窗口
					$("#editActivityModal").modal("show");
				}
			})
		})
		$("#changeActivity").click(function () {
			//获取数据
			var id = $("#edit-id").val()
			var owner = $.trim($("#edit-marketActivityOwner").val());
			var name = $.trim($("#edit-marketActivityName").val());
			var startDate = $.trim($("#edit-startDate").val());
			var endDate = $.trim($("#edit-endDate").val());
			var cost = $.trim($("#edit-cost").val());
			var description = $.trim($("#edit-description").val());

			if (owner==""){
				alert("所有者不能为空!");
				return;
			}
			if (name==""){
				alert("名称不能为空!");
				return;
			}
			if (startDate!=""&&endDate!=""){
				if (endDate<startDate){
					alert("结束日期不能比开始日期小!")
					return;
				}
			}
			var regExp = /^(([1-9]\d*)|0)$/;
			if (!regExp.test(cost)){
				alert("成本只能是非负整数!");
				return;
			}
			//验证通过发送请求
			$.ajax({
				url:"workbench/activity/updateActivityById.do",
				data:{
					id:id,
					owner:owner,
					name:name,
					startDate:startDate,
					endDate:endDate,
					cost:cost,
					description:description
				},
				type:"post",
				dataType:"json",
				success:function (data) {
					if (data.code==1){
						//更新成功
						queryActivityByConditionForPage($("#page").bs_pagination("getOption","currentPage"),$("#page").bs_pagination("getOption","rowsPerPage"));
						//关闭模态窗口
						$("#editActivityModal").modal("hide");
						//成功后重置表单
						$("#editActivityForm").get(0).reset();
					}else {
						alert(data.message);
						$("#editActivityModal").modal("show");
					}
				}
			});
		})

		$("#exportActivityAllBtn").click(function () {
			window.location.href="workbench.activity/exportAllActivity.do";
		});

		$("#exportActivityXzBtn").click(function (){
			//收集数据
			var id = $("#tBody input[type='checkbox']:checked");
			if (id.size()==0){
				alert("请选择需要导出的数据!");
				return;
			}
			var ids="";
			$.each(id,function () {
				ids+="id="+this.value+"&";
			})
			ids = ids.substring(0,ids.length-1);

			window.location.href="workbench.activity/exportAllActivity.do?"+ids;
		});

		$("#importActivityBtn").click(function () {
			//第一步表单验证
			var activityFileName = $("#activityFile").val();//拿到文件名
			if ('xls'!=activityFileName.substr(activityFileName.lastIndexOf(".")+1).toLocaleLowerCase()){
				alert("只支持xls文件!");
				return;
			}
			var activityFile = $("#activityFile")[0].files[0];//拿到文件本身
			if (activityFile.size>5*1024*1024){
				alert("文件大小不能超过5MB!");
				return;
			}
			//FormData	对象，是ajax提供的接口，这个类可以模拟键值对向后台提交参数
			//FormData	优势，不仅能提交文本字符串数据，还能提交二进制数据(其它提交方式做不到)。
			var formData = new FormData();
			formData.append("activityFile",activityFile);
			//验证通过，发送请求
			$.ajax({
				url:"workbench/activity/importActivity.do",
				data:formData,
				processData:false,//默认情况下取值是true，设置ajax向后台提交参数之前是否将参数统一转换成字符串
				contentType:false,//设置ajax向后台提交参数之前，是否把所有参数按照urlencoded编码，默认是true
				type:"post",
				dataType:"json",
				success:function (data){
					if (data.code==1){
						alert("成功导入"+data.retData+"条记录");
						$("#importActivityModal").modal("hide");
						queryActivityByConditionForPage(1,$("#page").bs_pagination("getOption","rowsPerPage"));
					}else {
						alert(data.message);
						$("#importActivityModal").modal("show");
					}
				}
			})
		})

	});


	//------------------------------------------------------------------------------------------------------

	function queryActivityByConditionForPage(pageNo,pageSize) {
		//当市场活动页面加载完成后，查询所有数据第一页以及数据总条数
		//收集参数
		var name = $("#query-name").val();
		var owner = $("#query-owner").val();
		var startDate = $("#query-startDate").val();
		var endDate = $("#query-endDate").val();
		var pageNo = pageNo;
		var pageSize = pageSize;
		//发送请求
		$.ajax({
			url:"workbench/activity/queryActivityByConditionForPage.do",
			data: {"name":name, "owner":owner, "startDate":startDate, "endDate":endDate, "pageNo":pageNo, "pageSize":pageSize},
			type:"post",
			dataType: "json",
			success:function (data){
				//显示总条数
				$("#totalRowsB").text(data.totalRows)
				//显示市场活动列表
				var htmlStr ="";
				$.each(data.activityList,function (index,obj){
					htmlStr+='<tr class="active">';
					htmlStr+='<td><input type="checkbox" value="'+obj.id+'" /></td>';
					htmlStr+='<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'workbench/activity/detailActivity.do?id='+obj.id+'\';">'+obj.name+'</a></td>';
					htmlStr+='<td>'+obj.owner+'</td>';
					htmlStr+='<td>'+obj.startDate+'</td>';
					htmlStr+='<td>'+obj.endDate+'</td>';
					htmlStr+='</tr>';
				});
				$("#checkAll").prop("checked",false);
				$("#tBody").html(htmlStr);

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
						queryActivityByConditionForPage(data.currentPage , data.rowsPerPage);
					}
				})
			}
		})
	}
	
</script>
</head>
<body>

	<!-- 创建市场活动的模态窗口 -->
	<div class="modal fade" id="createActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form id="creayeActivityFrom" class="form-horizontal" role="form">
					
						<div class="form-group">
							<label for="create-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-marketActivityOwner">
								  <c:forEach items="${userList}" var="user">
									  <option value="${user.id}">${user.name}</option>
								  </c:forEach>
								</select>
							</div>
                            <label for="create-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-marketActivityName">
                            </div>
						</div>
						
						<div class="form-group">
							<label for="create-startDate" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-startDate" readonly>
							</div>
							<label for="create-endDate" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-endDate" readonly>
							</div>
						</div>
                        <div class="form-group">

                            <label for="create-cost" class="col-sm-2 control-label">成本</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-cost">
                            </div>
                        </div>
						<div class="form-group">
							<label for="create-description" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-description"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="insertSaveBtn">保存</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 修改市场活动的模态窗口 -->
	<div class="modal fade" id="editActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form class="form-horizontal" id="editActivityForm" role="form">
					
						<div class="form-group">
							<input type="hidden" id="edit-id" >
							<label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-marketActivityOwner">
									<c:forEach items="${userList}" var="user">
										<option value="${user.id}">${user.name}</option>
									</c:forEach>
								</select>
							</div>
                            <label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-marketActivityName" value="发传单">
                            </div>
						</div>

						<div class="form-group">
							<label for="edit-startDate" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="edit-startDate" readonly>
							</div>
							<label for="edit-endDate" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="edit-endDate" readonly>
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-cost" class="col-sm-2 control-label">成本</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-cost" >
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-description" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="edit-description">市场活动Marketing，是指品牌主办或参与的展览会议与公关市场活动，包括自行主办的各类研讨会、客户交流会、演示会、新产品发布会、体验会、答谢会、年会和出席参加并布展或演讲的展览会、研讨会、行业交流会、颁奖典礼等</textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="changeActivity">更新</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 导入市场活动的模态窗口 -->
    <div class="modal fade" id="importActivityModal" role="dialog">
        <div class="modal-dialog" role="document" style="width: 85%;">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">
                        <span aria-hidden="true">×</span>
                    </button>
                    <h4 class="modal-title" id="myModalLabel">导入市场活动</h4>
                </div>
                <div class="modal-body" style="height: 350px;">
                    <div style="position: relative;top: 20px; left: 50px;">
                        请选择要上传的文件：<small style="color: gray;">[仅支持.xls]</small>
                    </div>
                    <div style="position: relative;top: 40px; left: 50px;">
                        <input type="file" id="activityFile">
                    </div>
                    <div style="position: relative; width: 400px; height: 320px; left: 45% ; top: -40px;" >
                        <h3>重要提示</h3>
                        <ul>
                            <li>操作仅针对Excel，仅支持后缀名为XLS的文件。</li>
                            <li>给定文件的第一行将视为字段名。</li>
                            <li>请确认您的文件大小不超过5MB。</li>
                            <li>日期值以文本形式保存，必须符合yyyy-MM-dd格式。</li>
                            <li>日期时间以文本形式保存，必须符合yyyy-MM-dd HH:mm:ss的格式。</li>
                            <li>默认情况下，字符编码是UTF-8 (统一码)，请确保您导入的文件使用的是正确的字符编码方式。</li>
                            <li>建议您在导入真实数据之前用测试文件测试文件导入功能。</li>
                        </ul>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button id="importActivityBtn" type="button" class="btn btn-primary">导入</button>
                </div>
            </div>
        </div>
    </div>
	
	
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>市场活动列表</h3>
			</div>
		</div>
	</div>
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">
		
			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input class="form-control" type="text" id="query-name">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text" id="query-owner">
				    </div>
				  </div>


				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">开始日期</div>
					  <input class="form-control" type="text" id="query-startDate" />
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">结束日期</div>
					  <input class="form-control" type="text" id="query-endDate">
				    </div>
				  </div>
				  
				  <button type="button" class="btn btn-default" id="queryActivityBtn">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button type="button" class="btn btn-primary" id="createActivityBtn" data-target="#createActivityModal"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" id="editActivityBtn" data-target="#editActivityModal"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger" id="deleteActivityBtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				<div class="btn-group" style="position: relative; top: 18%;">
                    <button type="button" class="btn btn-default" id="importBtn" data-toggle="modal" data-target="#importActivityModal" ><span class="glyphicon glyphicon-import"></span> 上传列表数据（导入）</button>
                    <button id="exportActivityAllBtn" type="button" class="btn btn-default"><span class="glyphicon glyphicon-export"></span> 下载列表数据（批量导出）</button>
                    <button id="exportActivityXzBtn" type="button" class="btn btn-default"><span class="glyphicon glyphicon-export"></span> 下载列表数据（选择导出）</button>
                </div>
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="checkAll" /></td>
							<td>名称</td>
                            <td>所有者</td>
							<td>开始日期</td>
							<td>结束日期</td>
						</tr>
					</thead>
					<tbody id="tBody">
						<%--<tr class="active">
							<td><input type="checkbox" /></td>
							<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.jsp';">发传单</a></td>
                            <td>zhangsan</td>
							<td>2020-10-10</td>
							<td>2020-10-20</td>
						</tr>
                        <tr class="active">
                            <td><input type="checkbox" /></td>
                            <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.jsp';">发传单</a></td>
                            <td>zhangsan</td>
                            <td>2020-10-10</td>
                            <td>2020-10-20</td>
                        </tr>--%>
					</tbody>
				</table>
				<div id="page"/>
			</div>
			
			<div style="height: 50px; position: relative;top: 30px;">
				<%--<div>
					<button type="button" class="btn btn-default" style="cursor: default;">共<b id="totalRowsB">50</b>条记录</button>
				</div>--%>
				<%--<div class="btn-group" style="position: relative;top: -34px; left: 110px;">--%>
					<%--<button type="button" class="btn btn-default" style="cursor: default;">显示</button>--%>
					<%--<div class="btn-group" id="page">--%>
						<%--<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
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
				<div style="position: relative;top: -88px; left: 285px;" id="page">--%>
					<%--<nav>
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
					</nav>--%>
				</div>
			</div>
			
		</div>
		
	</div>
</body>
</html>