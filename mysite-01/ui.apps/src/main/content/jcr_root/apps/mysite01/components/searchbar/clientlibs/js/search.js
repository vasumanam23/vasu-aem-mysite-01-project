$(function(){
    $('#fulltext-search').submit(function(event){
        var formE1 = $(this);
        var submitButton = $('input[type=submit]'),formE1);

        $.ajax({
            type:'GET',
            url:formE1.prop('action'),
            accept: { javascript:'application/javascript'},
            data:{'keyword':$('#keyword').val()},
            beforeSend:function(){
                submitButton.prop('disabled','disabled');
            }
            }).done(function (data){
                 submitButton.prop('disabled',false);

        });
    });

});
