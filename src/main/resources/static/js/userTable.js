var userTable = $('table#userTable').DataTable({
    ajax: '/api/users',
    serverSide: true,
    dom: 'Bfrtip',
    buttons: [
        {
            extend: 'excel',
            text:'<i class="fa fa-file-excel"></i> Excel',
            title: 'Tài khoản',
            "exportOptions": {
                columns: [1,2, 3,4,5]
            },
            className: 'btn btn-success'
        },
        {
            text: 'Tạo mới',
            className: 'btn btn-success',
            action: function (e, dt, node, config) {
                window.location.href = '/user/add';
            }
        }
    ],
    select:{
        style: 'multi'
    },
    columns: [
        {
            data: 'id',
            render: function (data){
                return '<input type="checkbox" name="userSelected">'
            },
            orderable: false
        },
        {
            data: 'id',
            render: function(data, type, row, meta) {
                if (type === 'display') {
                    return meta.row + 1; // Số thứ tự bắt đầu từ 1
                }
                return "";
            },
            orderable: false
        },
        {
            data: 'email'
        },
        {
            data: 'phone'
        },
        {
            data: 'role'
        },
        {
            data: 'userStatus',
            render: function (data){
                return data === 0?"Không khóa":"Khóa"
            }
        },
        {
            data: 'id',
            render: function (data, type){
                if (type === 'display') {
                    var deleteButton = '<a data-toggle="modal" data-target="#deleteModal-' + data + '" class="btn btn-danger btn-sm">Xóa</a>';

                    // Mã HTML cho modal
                    var modal = '<div class="modal fade" id="deleteModal-' + data + '" tabindex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle" aria-hidden="true">' +
                        '<div class="modal-dialog modal-dialog-centered" role="document">' +
                        '<div class="modal-content">' +
                        '<div class="modal-header">' +
                        '<button type="button" class="close" data-dismiss="modal" aria-label="Close">' +
                        '<span aria-hidden="true">&times;</span>' +
                        '</button>' +
                        '</div>' +
                        '<div class="modal-body">' +
                        'Xác nhận xóa người dùng có id=' + data + ' ?' +
                        '</div>' +
                        '<div class="modal-footer">' +
                        '<a type="button" class="btn btn-secondary" data-dismiss="modal">Đóng</a>' +
                        '<a href="/user/delete?id=' + data + '" type="button" class="btn btn-danger">Xóa</a>' +
                        '</div>' +
                        '</div>' +
                        '</div>' +
                        '</div>';
                    return '<a class="btn btn-primary btn-sm" href="/user/edit?id=' + data + '">Sửa</a>'  + deleteButton + modal;
                }
                return null;
            },
            orderable: false,
            searchable: false
        }
    ]
});
$('select#roleSelector').change(function (){
    var filter = '';
    $('select#roleSelector option:selected').each(function (){
        filter += $(this).text() + "+";
    });
    filter = filter.substring(0, filter.length-1);
    userTable.column(4).search(filter).draw();
});
$('select#statusSelector').change(function (){
    var filter = '';
    // $('select#statusSelector option:selected').each(function (){
    //     filter += $(this).text() + "+";
    // });
    // filter = filter.substring(0, filter.length-1);
    var filter = $('select#statusSelector').val();
    userTable.column(5).search(filter).draw();
});
userTable.on('change', 'input[type="checkbox"]', function() {
    var isChecked = $(this).prop('checked');
    var $row = $(this).closest('tr');

    if (isChecked) {
        $row.addClass('selected');
        userTable.row($row).select();
    } else {
        $row.removeClass('selected');
        userTable.row($row).deselect();
    }
});
userTable.on('click', 'tbody tr', function (e) {
    e.currentTarget.classList.toggle('selected');
});