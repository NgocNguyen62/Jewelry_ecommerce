
    var backgroundColors = [
        'rgba(255, 99, 132, 0.2)',
        'rgba(255, 159, 64, 0.2)',
        'rgba(255, 205, 86, 0.2)',
        'rgba(75, 192, 192, 0.2)',
        'rgba(54, 162, 235, 0.2)',
        'rgba(153, 102, 255, 0.2)',
        'rgba(201, 203, 207, 0.2)'
    ];

    var borderColors = [
        'rgb(255, 99, 132)',
        'rgb(255, 159, 64)',
        'rgb(255, 205, 86)',
        'rgb(75, 192, 192)',
        'rgb(54, 162, 235)',
        'rgb(153, 102, 255)',
        'rgb(201, 203, 207)'
    ];
    var ctx1 = document.getElementById("Pie-chart").getContext('2d');
    var PieChart = new Chart(ctx1, {
        type : 'pie',
        data : {

            labels : label,
            datasets : [ {
                // label : "Chart-1",
                backgroundColor: backgroundColors,
                borderColor: borderColors,
                borderWidth: 1,
                hoverOffset: 4,
                data : data,
            }, ]
        },
    });
    var ctx2 = document.getElementById("top-sales").getContext('2d');
    var TopSales = new Chart(ctx2, {
        type: 'bar',
        data : {
            labels : labelTopSale,
            datasets : [ {
                // label : "Chart-1",
                backgroundColor: backgroundColors,
                borderColor: borderColors,
                borderWidth: 1,
                hoverOffset: 4,
                data : dataTopSale,
            }, ]
        },
    });


