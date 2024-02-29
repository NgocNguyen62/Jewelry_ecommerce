$().ready(function (){
    $("#cate-form").validate({
        onfocusout: false,
        onkeyup: false,
        onclick: false,
        rules: {
            "categoryName":{
                required: true,
            }
        },
        messages: {
            "categoryName": {
                required: "Nhập tên phân loại",
            }
        },
    })
})