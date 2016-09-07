String.prototype.format = function () {
    var args = arguments;
    return this.replace(/\{(\d+)\}/g, function (s, i) {
        return args[i];
    });
};

var APP_ADDRS = "trace/getTraces";
var APP_TRACE = "trace/getTrace/";
var APP_SERVICE = "service/getServiceByOffset/";
var APP_SEARCHSERVICE = "service/searchService";

var Net = {
    getSearchData: function (data, callback) {
        $.ajax({
                   url: APP_ADDRS,
                   type: 'post',
                   contentType: "application/json",
                   data: data,
                   success: callback
               });
    }
}