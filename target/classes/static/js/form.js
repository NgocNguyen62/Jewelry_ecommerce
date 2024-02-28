$().ready(function (){
    $("#user-form").validate({
        onfocusout: false,
        onkeyup: false,
        onclick: false,
        rules: {
            "email":{
                required: true,
                email: true,
            },
            "password":{
                required: true,
                validatePassword: true,
            },
            "re-password":{
                required: true,
                equalTo: "#password"
            },
            "firstName":{
                required: true,
            },
            "lastName":{
                required: true,
            },
            "phone":{
                required: true,
                validatePhone: true,
            },
            "address":{
                required: true
            }
        },
        messages: {
            "email": {
                required: "Nhập email",
                email: "Email phải đúng định dạng @domain.com"
            },
            "password": {
                required: "Nhập mật khẩu",
                minlength: "Mật khẩu ít nhất 8 kí tự",
            },
            "re-password":{
                required: "Nhập lại mật khẩu",
                equalTo: "Mật khẩu nhập lại không khớp"
            },
            "firstName":{
                required: "Nhập vào họ"
            },
            "lastName":{
                required:"Nhập vào tên"
            },
            "phone":{
                required: "Nhập số điện thoại"
            },
            "address":{
                required: "Nhập địa chỉ"
            }
        },
    })
    $.validator.addMethod("validatePassword", function (value, element) {
        return this.optional(element) || /^(?=.*[!@#$%^&*]).{8,}$/i.test(value);
    }, "Hãy nhập password ít nhất 8 ký tự và ít nhất một kí tự đặc biệt");
    $.validator.addMethod("validatePhone", function (value, element){
        return this.optional(element) || /^(0\d{9})$/.test(value);
    }, "Số điện thoại gồm 10 chữ số bắt đầu bằng 0");
})
