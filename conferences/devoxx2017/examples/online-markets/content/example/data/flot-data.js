//Flot Multiple Axes Line Chart
$(function () {
  var oilprices = [];
  var exchangerates = [];
  var oilpricesApiCall = $.get("http://localhost:8092/prices/api/oilprices.json");

  oilpricesApiCall.done(function (data) {
    oilprices = data;
    var exchangeApiCall = $.get("http://localhost:8092/prices/api/exchangerates.json");
    exchangeApiCall.done(function (rates) {
      exchangerates = rates;
      doPlot("right");
    });
  });

  function euroFormatter(v, axis) {
    return v.toFixed(axis.tickDecimals) + "€";
  }

  function doPlot(position) {
    $.plot($("#flot-line-chart-multi"), [{
      data: oilprices,
      label: "Oil price ($)"
    }, {
      data: exchangerates,
      label: "USD/EUR exchange rate",
      yaxis: 2
    }], {
      xaxes: [{
        mode: 'time'
      }],
      yaxes: [{
        min: 0
      }, {
        // align if we are to the right
        alignTicksWithAxis: position == "right" ? 1 : null,
        position: position,
        tickFormatter: euroFormatter
      }],
      legend: {
        position: 'sw'
      },
      grid: {
        hoverable: true //IMPORTANT! this is needed for tooltip to work
      },
      tooltip: true,
      tooltipOpts: {
        content: "%s for %x was %y",
        xDateFormat: "%y-%0m-%0d",

        onHover: function (flotItem, $tooltipEl) {
          // console.log(flotItem, $tooltipEl);
        }
      }

    });
  }

  $("button").click(function () {
    doPlot($(this).text());
  });
});