// $(document).ready(function () {
$(function () {

    $(".activity").click(function () {
        checkboxState = $(this).find(":checkbox").is(":checked")
        activityUpdate($(this).parents('tr').attr("id"), checkboxState);
        if (checkboxState == true)
            $(this).parents('tr').attr("userActive", "true");
        else
            $(this).parents('tr').attr("userActive", "false");
    });

    makeEditable({
            ajaxUrl: "ajax/admin/users/",
            datatableApi: $("#datatable").DataTable({
                "paging": false,
                "info": true,
                "columns": [
                    {
                        "data": "name"
                    },
                    {
                        "data": "email"
                    },
                    {
                        "data": "roles"
                    },
                    {
                        "data": "enabled"
                    },
                    {
                        "data": "registered"
                    },
                    {
                        "defaultContent": "Edit",
                        "orderable": false
                    },
                    {
                        "defaultContent": "Delete",
                        "orderable": false
                    }
                ],
                "order": [
                    [
                        0,
                        "asc"
                    ]
                ]
            })
        }
    );
});


function activityUpdate(userId, state) {
    if (state == true) active = 1;
    else active = 0;

    $.ajax({
        type: "POST",
        url: context.ajaxUrl+"activity",
        data: {userId, active},
        dataType: "json"
    }).done(function () {
        successNoty("User has been updated");
    });
}