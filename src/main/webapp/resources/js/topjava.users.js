// $(document).ready(function () {
$(function () {

    $(".activity").click(function () {
        checkboxState = $(this).find(":checkbox").is(":checked")
        activityUpdate($(this).parents('tr').attr("id"), checkboxState);
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


function activityUpdate(userId, active) {
    $.ajax({
        type: "POST",
        url: context.ajaxUrl+"activity",
        data: {userId, active},
        dataType: "json",
        success: function () {
            $("#"+userId).attr("userActive", active);
            state = (active == true) ? "Enabled" : "Disabled"
            successNoty("User has been " + state);
        },
        error: function () {
            failNoty("Error to update user state");
        }
    });
}