function dataToChart(chart) {
    var colors = [
        "#00ffff", "#000000", "#0000ff", "#a52a2a", "#00ffff", "#00008b", "#008b8b",
        "#006400", "#bdb76b", "#8b008b", "#556b2f", "#ff8c00", "#9932cc", "#8b0000",
        "#e9967a", "#9400d3", "#ff00ff", "#ffd700", "#008000", "#4b0082", "#f0e68c",
        "#90ee90", "#d3d3d3", "#ffb6c1", "#00ff00", "#ff00ff", "#800000", "#000080",
        "#808000", "#ffa500", "#ffc0cb", "#800080", "#800080", "#ff0000", "#c0c0c0",
        "#ffff00"
    ];
    chartData = [];

    $.each(chart, function (productName, datePriceArr) {
        var arr = [];
        var color = colors[Math.floor(Math.random()*colors.length)];
        colors = jQuery.grep(colors, function(value) {
            return value != color;
        });
        chartData.push({
            label: productName,
            fill: false,
            backgroundColor: color,
            borderColor: color,
            data: arr
        });

        $.each(datePriceArr, function (date, price) {
            arr.push({x: new Date(date), y: price});
        });
    });

    appendChart(chartData);
}

function appendChart(chartData) {
    var ctx = document.getElementById("graph").getContext('2d');
    $(ctx).empty();

    var myChart = new Chart(ctx, {
        type: 'line',
        // labels: unique(dateArr.reverse()),
        data: {
            // labels: unique(dateLabels).sort(),
            datasets: chartData
        },
        options: {
            elements: {
                line: {
                    tension: 0
                }
            },
            scales: {
                xAxes: [{
                    type: 'time',
                    time: {
                        unit: 'month',
                        displayFormats: {

                        }
                    }
                }],
                yAxes: [{
                    ticks: {
                        beginAtZero:true
                    }
                }]
            }
        }
    });
}

function unique(array){
    return array.filter(function(el, index, arr) {
        return index === arr.indexOf(el);
    });
}