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
            <li><a href="javascript:void(0)">会员充值</a></li>
            <li><a href="javascript:void(0)">零钱扣除</a></li>
        </ol>

    </div>
</div>

<div class="row">
    <div class="col-xs-12 col-sm-12">
        <div class="box">
            <div class="box-header">
                <div class="box-name">
                    <i class="fa fa-search"></i>
                    <span>零钱扣除</span>
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
                <h4 class="page-header">零钱扣除</h4>
                <form class="form-horizontal" role="form">
                    <input type="hidden" id="emp_id" name="emp_id" value="${memberVO.empId}">
                    <div class="form-group">
                        <label class="col-sm-2 control-label">用户昵称</label>
                        <div class="col-sm-4">
                            <input type="text" id="emp_name" value="${memberVO.empName}" class="form-control"
                                   placeholder="用户昵称" data-toggle="tooltip" data-placement="bottom" readonly="true"
                                   title="Tooltip for name">
                        </div>
                    </div>

                <div class="form-group">
                    <label class="col-sm-2 control-label">手机号</label>
                    <div class="col-sm-4">
                        <input type="text" id="emp_mobile" value="${memberVO.empMobile}" class="form-control"
                               placeholder="手机号" data-toggle="tooltip" data-placement="bottom" readonly="true"
                               title="Tooltip for name">
                    </div>
                </div>

                    <div class="form-group">
                        <label class="col-sm-2 control-label">剩余金额</label>
                        <div class="col-sm-4">
                            <input type="text" id="package_money" value="${memberVO.package_money}" class="form-control"
                                   placeholder="剩余金额" data-toggle="tooltip" data-placement="bottom" readonly="true"
                                   title="Tooltip for name">
                        </div>
                    </div>

                <div class="form-group">
                    <label class="col-sm-2 control-label">扣除金额</label>
                    <div class="col-sm-4">
                        <input type="text" id="lx_consumption_count" class="form-control"
                               placeholder="扣除金额" data-toggle="tooltip" data-placement="bottom"
                               title="Tooltip for name">
                    </div>
                </div>

                    <div class="form-group">
                        <div class="col-sm-9 col-sm-offset-3">
                            <button type="button" class="btn btn-primary" onclick="chongzhi()">确定
                            </button>
                            <button type="button" class="btn btn-primary" onclick="javascript :history.back(-1)">返回
                            </button>
                        </div>
                    </div>
                </form>
            </div>

        </div>
    </div>
</div>

<script type="text/javascript">
    var kaiguan=1;

    function chongzhi(){
        if(kaiguan == 1){
            var emp_id = $("#emp_id").val();
            var lx_consumption_count = $("#lx_consumption_count").val();
            var package_money = $("#package_money").val();

            var regInt = /^([0-9]\d*)$/;
            if (lx_consumption_count.replace(/\s/g, '') == '') {
                alert("扣除金额不能为空");
                return;
            } else {
                if (!regInt.test(lx_consumption_count)) {
                    alert("扣除金额必须是正整数");
                    return;
                }
            }

            kaiguan = 0;
            $.ajax({
                cache: true,
                type: "POST",
                url: "/lxConsumptionController/chongzhiLqDelete.do",
                data: {
                    "emp_id": emp_id,
                    "lx_consumption_type": '4',
                    "package_money": package_money,
                    "lx_consumption_count": lx_consumption_count
                },
                async: false,
                success: function (_data) {
                    var data = $.parseJSON(_data);
                    if (data.success) {
                        alert("扣除成功");
                        history.go(-1);
                    } else {
                        var _case = {1: "扣除金额为空或0", 2:"会员不存在，请检查会员!", 3:"扣除失败", 4:"剩余金额为空！", 5:"剩余金额少于要扣除的金额！"};
                        alert(_case[data.code])
                    }
                }
            });
        }

    }

</script>
