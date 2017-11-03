<%@page pageEncoding="UTF-8"%>
<div class="modal fade" id="exampleModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="exampleModalLabel">切换用户  (SwitchUserFilter)</h4>
            </div>
            <div class="modal-body">
                <form>
                    <div class="form-group">
                        <label for="switch-user-input" class="control-label">请输入要切换用户的邮箱:</label>
                        <input type="text" class="form-control" id="switch-user-input">
                    </div>
                </form>
                <div class="alert alert-info alert-dismissible" hidden role="alert" id="processing-alert">
                    <strong>用户切换中。。。</strong>
                </div>
                <div class="alert alert-danger alert-dismissible" hidden role="alert" id="error-alert">
                    <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <strong>Warning!</strong> 用户名错误
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">撤销</button>
                <button type="button" class="btn btn-primary" id="switch-user-btn">切换用户</button>
            </div>
        </div>
    </div>
</div>
<script>
    $(function(){
        $("#switch-user-btn").on("click",function(){
            $.ajax({
                type:"GET",
                url: '${pageContext.request.contextPath}/switch_user?username=' + $('#switch-user-input').val(),
                beforeSend:function(){$("#processing-alert").show();$("#error-alert").hide();},
                success:function(data){
                    $("#exampleModal").modal('hide');
                    window.location.href = "${pageContext.request.contextPath}/";
                }   ,
                error: function(){
                    $("#error-alert").show();$("#processing-alert").hide();
                }
            });

        });
    });
</script>
