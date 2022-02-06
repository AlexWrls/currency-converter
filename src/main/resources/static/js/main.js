// подсчитат статистику при старте
window.onload = function (event) {
    event.preventDefault();
    statistics_submit();
}
// кнопки
$(document).ready(function () {
    $("#convert").submit(function (event) {
        event.preventDefault();
        convert_submit();
    });
    $("#statistics").submit(function (event) {
        event.preventDefault();
        statistics_submit();
    });
    $("#chart").submit(function (event) {
        event.preventDefault();
        chart_submit();
    });
});

// запрос на график
function chart_submit() {
    $('#myChart')[0].remove()
    $('<canvas>', {
        id: 'myChart'
    }).appendTo('#rootChart')

    let data = {};
    data["charCode"] = $('#charCode').val()
    $('#btn-chart').prop('disabled', true)
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/chart",
        data: JSON.stringify(data),
        dataType: 'json',
        timeout: 600000,
        success: function (data) {
            console.log("SUCCESS : ", data);
            drawChart(data.cursDate, data.values)
            $('#btn-chart').prop('disabled', false)
        },
        error: function (e) {
            $('<p>Error: ' + e.responseText + '</p>').appendTo('#chart')
            console.log("ERROR: ", e);
            $('#btn-chart').prop('disabled', false)
        }
    });
}

// нарисовать график по данным
function drawChart(labelsChart, dataChart) {
    const data = {
        labels: labelsChart,
        datasets: [{
            label: '₽',
            backgroundColor: 'rgb(255, 99, 132)',
            borderColor: 'rgb(255, 99, 132)',
            data: dataChart,
        }]
    };
    const config = {
        type: 'line',
        data: data,
        options: {}
    };
    const myChart = new Chart(
        $('#myChart')[0],
        config
    );
}

// статистика
function statistics_submit() {
    let data = {};
    data["after"] = $('#after').val()
    data["before"] = $('#before').val()
    $('#btn-statistics').prop('disabled', true)
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/statistics",
        data: JSON.stringify(data),
        dataType: 'json',
        timeout: 600000,
        success: function (data) {
            console.log("SUCCESS : ", data);
            $('#after').val(data.after)
            $('#before').val(data.before)
            objStatistic = data.statistics
            filter()
            $('#btn-statistics').prop('disabled', false)
        },
        error: function (e) {
            $('<p>Error: ' + e.responseText + '</p>').appendTo('#statistics')
            console.log("ERROR: ", e);
            $('#btn-statistics').prop('disabled', false)
        }
    });
}

// конвертирование
function convert_submit() {
    let data = {};
    data["convertTo"] = $('#convertTo').val();
    data["convertFrom"] = $('#convertFrom').val();
    data["valueTo"] = $('#valTo').val();
    $('#btn-convert').prop('disabled', true);
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/calculate",
        data: JSON.stringify(data),
        dataType: 'json',
        timeout: 600000,
        success: function (data) {
            console.log("SUCCESS : ", data);
            $('#valFrom').val(data.valueFrom)
            $('#btn-convert').prop('disabled', false);
            statistics_submit()
        },
        error: function (e) {
            $('<p>Error: ' + e.responseText + '</p>').appendTo('#convert')
            console.log("ERROR: ", e);
            $('#btn-convert').prop('disabled', false);
        }
    });
}


let objStatistic = []

// формирование фильтра статистики
function filter() {
    let filterTo = $('#filterConvertTo')[0];
    let filterFrom = $('#filterConvertFrom')[0];
    let listing_table = $('#listingTable')[0];
    listing_table.innerHTML = "";
    for (let i = 0; i < objStatistic.length; i++) {
        if ((filterFrom.value === objStatistic[i].convertFrom || filterFrom.value === '...') && (filterTo.value === objStatistic[i].convertTo || filterTo.value === '...')) {
            listing_table.innerHTML += "<tr><td>" + objStatistic[i].convertTo + "</td><td>" + objStatistic[i].convertFrom + "</td><td>" + objStatistic[i].count + "</td></tr>";
        }
    }
}

//сортировка по возрастанию
function sort() {
    objStatistic.sort(function (a, b) {
        if (a.count > b.count) {
            return 1;
        }
        if (a.count < b.count) {
            return -1;
        }
        return 0;
    });
    filter();
}

//сортировка по убыванию
function reverseSort() {
    objStatistic.sort(function (a, b) {
        if (a.count > b.count) {
            return -1;
        }
        if (a.count < b.count) {
            return 1;
        }
        return 0;
    });
    filter();
}
