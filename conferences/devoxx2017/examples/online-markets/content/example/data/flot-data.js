//Flot Multiple Axes Line Chart
$(function () {
  var oilprices = {};
  var exchangerates = {};

  function refreshPlot() {
    var oilpricesApiCall = $.get(
        "http://localhost:8092/prices/api/oilprices");
    oilpricesApiCall.done(function (data) {
      oilprices = data;
      var exchangeApiCall = $.get(
          "http://localhost:8092/prices/api/exchangerates");
      exchangeApiCall.done(function (rates) {
        exchangerates = rates;
        doPlot("right");
      });
    });
  }

  function euroFormatter(v, axis) {
    console.log("Axis: ", axis);
    return v.toFixed(axis.tickDecimals) + "€";
  }

  function doPlot(position) {
    $.plot($("#flot-line-chart-multi"), [{
      data: oilprices.rates,
      label: "Oil price ($)"
    }, {
      data: exchangerates.rates,
      label: "USD/EUR exchange rate",
      yaxis: 2
    }], {
      xaxes: [{
        mode: 'time',
        timeformat: "%h:%M:%S"
      }],
      yaxes: [{
        min: 0
      }, {
        // align if we are to the right
        alignTicksWithAxis: position === "right" ? 1 : null,
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
        content: "%s at %x was %y",
        xDateFormat: "%h:%M:%S",

        onHover: function (flotItem, $tooltipEl) {
          // console.log(flotItem, $tooltipEl);
        }
      }

    });
  }

  refreshPlot();
  setInterval(refreshPlot, 5000);

  // $("button").click(function () {
  //   doPlot($(this).text());
  // });
});