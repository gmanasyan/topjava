const mealAjaxUrl = "ajax/profile/meals/";

$(function () {

    initDatePicker();
    i18nInit("meal");

    makeEditable({
        ajaxUrl: mealAjaxUrl,
        datatableApi: $("#datatable").DataTable({
            "ajax": {
                "url": mealAjaxUrl,
                "dataSrc": ""
            },
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "dateTime",
                    "render": function (data, type, row) {
                        if (type === "display") {
                            return data.replace("T", " ");
                        }
                        return data;
                    }
                },
                {
                    "data": "description"
                },
                {
                    "data": "calories"
                },
                {
                    "defaultContent": "Edit",
                    "orderable": false,
                    "render": renderEditBtn
                },
                {
                    "defaultContent": "Delete",
                    "orderable": false,
                    "render": renderDeleteBtn
                }
            ],
            "order": [
                [
                    0,
                    "desc"
                ]
            ],
            "createdRow": function (row, data, dataIndex) {
                $(row).attr("data-mealExcess", data.excess);
            }
        }),
        updateTable: updateFilteredTable
    });
});

function initDatePicker() {
    $('.datepicker').datetimepicker({
        timepicker:false,
        format:'Y-m-d'
    });

    $('.timepicker').datetimepicker({
        datepicker:false,
        format:'H:i'
    });

    $('.datetimepicker').datetimepicker({
        format:'Y-m-d H:i'
    });

    $('#startDate.datepicker').change(function () {
        minDate = $(this).val();
        minEndDate(minDate);
    });
}

function minEndDate(date) {
    $('#endDate.datepicker').datetimepicker({
        minDate:date
    });
}

function updateFilteredTable() {
    $.ajax({
        type: "GET",
        url: "ajax/profile/meals/filter",
        data: $("#filter").serialize()
    }).done(updateTableByData);
}

function clearFilter() {
    $("#filter")[0].reset();
    $.get("ajax/profile/meals/", updateTableByData);
}

