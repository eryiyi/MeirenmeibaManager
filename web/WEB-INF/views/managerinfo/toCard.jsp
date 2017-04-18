<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="um" uri="/unimanager-tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" trimDirectiveWhitespaces="true" %>

<div class="row">
    <div id="breadcrumb" class="col-xs-12">
        <a href="#" class="show-sidebar">
            <i class="fa fa-bars"></i>
        </a>
        <ol class="breadcrumb pull-left">
            <li><a href="javascript:void(0)" onclick="toPage('mainPage','')">主页</a></li>
            <li><a href="javascript:void (0);">店铺管理</a></li>
            <li><a href="javascript:void (0);">店铺编辑</a></li>
        </ol>

    </div>
</div>

<div class="row">
    <div class="col-xs-12 col-sm-12">
        <div class="box">
            <div class="box-header">
                <div class="box-name">
                    <i class="fa fa-search"></i>
                    <span>店铺编辑</span>
                </div>
                <div class="box-icons">
                    <a class="collapse-link">
                        <i class="fa fa-chevron-up"></i>
                    </a>
                    <a class="expand-link">
                        <i class="fa fa-expand"></i>
                    </a>
                    <a class="close-link">
                        <i class="fa fa-times"></i>
                    </a>
                </div>
                <div class="no-move"></div>
            </div>
            <div class="box-content">
                <h4 class="page-header">店铺编辑</h4>

                <form class="form-horizontal" role="form">

                    <div class="form-group">
                        <label class="col-sm-2 control-label">*店铺名称*</label>

                        <div class="col-sm-4">
                            <input type="text" id="company_name" placeholder="店铺名称" class="form-control" readonly="true"
                                   value="${info.company_name}" data-toggle="tooltip" data-placement="bottom"
                                   title="Tooltip for name">
                        </div>
                    </div>

                    <input type="hidden" id="info_id" value="${info.id}">
                    <input type="hidden" id="emp_id" value="${info.emp_id}">

                    <div class="form-group">
                        <label class="col-sm-2 control-label">*入驻时间*</label>

                        <div class="col-sm-4">
                            <input type="text" id="dateline" placeholder="入驻时间" class="form-control" readonly="true"
                                   value="${info.dateline}" data-toggle="tooltip" data-placement="bottom"
                                   title="Tooltip for name">
                        </div>
                    </div>

                        <div class="form-group">
                            <label class="col-sm-2 control-label">是否定向卡商家</label>
                            <div class="col-sm-4">
                                <select class="form-control" id="is_card">
                                    <option value="">--请选择--</option>
                                    <option value="0" ${info.is_card=='0'?'selected':''}>否</option>
                                    <option value="1" ${info.is_card=='1'?'selected':''}>是</option>
                                </select>
                            </div>
                        </div>

                    <div class="form-group">
                        <label class="col-sm-2 control-label">店铺商品类别</label>

                        <div class="col-sm-4">
                            <select class="form-control" id="mm_emp_provinceId1" onchange="selectClass()">
                                <option value="">--选择店铺商品类别--</option>
                                <c:forEach items="${listClassbig}" var="e" varStatus="st">
                                    <option value="${e.lx_class_id}">${e.lx_class_name}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">小类别</label>

                        <div class="col-sm-4">
                            <select class="form-control" id="lx_class_id">
                                <option value="">--选择小类别--</option>
                                <c:forEach items="${listClassSmall}" var="e" varStatus="st">
                                    <option value="${e.lx_class_id}" ${info.lx_class_id==e.lx_class_id?'selected':''}>${e.lx_class_name}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>

                    <c:if test="${info.is_card == '1'}">
                        <div class="form-group">
                            <label class="col-sm-2 control-label">免费消费二维码</label>
                            <div class="col-sm-4">
                                <img src="http://qr.topscan.com/api.php?text=${GET_GET_DXK_GOODS_URN}" style="width: 300px;height: 300px;">
                            </div>
                        </div>
                    </c:if>

                    <div class="form-group">
                        <div class="col-sm-9 col-sm-offset-3">
                            <button type="button" class="btn btn-primary" onclick="saveManagerInfo()">确定</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>

</div>
<script type="text/javascript">
    function saveManagerInfo() {
        var infoId = $("#info_id").val();
        var emp_id= $("#emp_id").val();

        var is_card= $("#is_card").val();
        if(is_card.replace(/\s/g, '') == ''){
            alert("请选择是否定向卡商家");
            return;
        }

        var lx_class_id = $("#lx_class_id").val();

        if (lx_class_id.replace(/\s/g, '') == '') {
            alert("请选择店铺分类！");
            return;
        }

        $.ajax({
            cache: true,
            type: "POST",
            url: "/managerinfo/updateCard.do",
            data: {
                "id": infoId,
                "is_card": is_card,
                "lx_class_id": lx_class_id,
                "emp_id": emp_id
            },

            async: false,
            success: function (_data) {
                var data = $.parseJSON(_data);
                if (data.success) {
                    alert("编辑成功");
                    window.location.href = "#module=/managerinfo/list"+ "&_t="+ new Date().getTime()+"&page=1";
                } else {
                    var _case = {1: "编辑失败", 2: "编辑失败"};
                    alert(_case[data.code])
                }
            }
        });
    };

</script>

<script type="text/javascript">

    function selectClass() {
        var province = $("#mm_emp_provinceId1").val();
        $.ajax({
            cache: true,
            type: "POST",
            url: "/getAllLxClass.do",
            data: {
                "f_lx_class_id": province
            },
            async: false,
            contentType: "application/x-www-form-urlencoded; charset=utf-8",
            success: function (_data) {
                var data = $.parseJSON(_data);
                if (data.success) {
                    <%--var citys = ${listCitysAll};--%>
                    var citys = data.data;
                    var ret = "<option value=''>" + '请选择分类' + "</option>";
                    for (var i = citys.length - 1; i >= 0; i--) {
                        if (citys[i].f_lx_class_id == province) {
                            ret += "<option value='" + citys[i].lx_class_id + "'>" + citys[i].lx_class_name + "</option>";
                        }
                    }
                    $("#lx_class_id").html(ret);
                } else {
                    var _case = {1: "获取数据失败"};
                    alert(_case[data.code])
                }
            }
        });
    }
    ;
</script>