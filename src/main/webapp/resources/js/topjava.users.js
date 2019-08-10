// $(document).ready(function () {
$(function () {

    $(".activity").click(function () {
        checkbox = $(this).find(":checkbox");
        activityUpdate($(this).parents('tr').attr("id"), checkbox);
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


function activityUpdate(userId, checkbox) {
    active = checkbox.is(":checked");
    $.ajax({
        type: "POST",
        url: context.ajaxUrl+"activity",
        data: {userId, active},
        dataType: "json",
        success: function () {
            $("#"+userId).attr("userActive", active);
            state = (active == true) ? "Enabled" : "Disabled";
            successNoty("User has been " + state);
        },
        error: function () {
            if (active) checkbox.prop('checked', false);
            else checkbox.prop('checked', true);
            failNoty("Error to update user state");
        }
    });
}