// $(document).ready(function () {
$(function () {

     makeEditable({
            ajaxUrl: "ajax/meals/",
            datatableApi: $("#datatable").DataTable({
                "paging": false,
                "info": true,
                "columns": [
                    {
                        "data": "dateTime"
                    },
                    {
                        "data": "description"
                    },
                    {
                        "data": "calories"
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
                        "desc"
                    ]
                ]
            })
        }
    );
});

function filter() {
    filterForm = $('#filterForm');
    $.ajax({
        type: "POST",
        url: context.ajaxUrl+"filter",
        data: filterForm.serialize(),
        dataType: "json"
    }).done(function (data, status, jqXHR) {
        context.datatableApi.clear().rows.add(data).draw();
        successNoty("Filtered");
    });
}

function clearFilter() {
    filterForm = $('#filterForm');
    filterForm.find(":input").val("");
    updateTable();
}

