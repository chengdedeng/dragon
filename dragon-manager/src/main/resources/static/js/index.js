// 时间转化
Date.prototype.Format = function (fmt) { //author: meizz
	var o = {
		"M+": this.getMonth() + 1, //月份
		"d+": this.getDate(), //日
		"h+": this.getHours(), //小时
		"m+": this.getMinutes(), //分
		"s+": this.getSeconds(), //秒
		"q+": Math.floor((this.getMonth() + 3) / 3), //季度
		"S": this.getMilliseconds() //毫秒
	};
	if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
	for (var k in o)
		if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
	return fmt;
};

// 将日期转化为时间戳
function datetime_to_unix(datetime) {
	var tmp_datetime = datetime.replace(/:/g,'-');
	console.log("first tmp_datetime: " + tmp_datetime);
	tmp_datetime = tmp_datetime.replace(/ /g,'-');
	console.log("second tmp_datetime: " + tmp_datetime);
	var arr = tmp_datetime.split("-");
	var now = new Date(Date.UTC(arr[0],arr[1]-1,arr[2],arr[3]-8,arr[4],arr[5]));
	return parseInt(now.getTime());
}

// 点击服务名，加载弹出层
var loadService = {
	// 起始加载页数
	startService: 1,
	// 每页加载条数
	limitService: 10,
	init: function(container, limitService) {
		var _this = this;
		_this.startService = 1;
		_this.limitService = limitService;
		$(container).bind("click", function() {
			// 如果已经加载过服务名，直接显示
			if ($("#pop-service").html() != null) {
				$("#pop-service").show().parents(".popup-cover").show();
				return;
			}
			// 加载不变的信息
			var addHtml = '';
			addHtml += '<div id="pop-service" class="popup-box">';
			addHtml += '<p class="popup-title">';
			addHtml += '<span>服务名</span>';
			addHtml += '<span class="popup-close"></span>';
			addHtml += '</p>';
			addHtml += '<div class="popup-body clearFix">';
			addHtml += '<ul class="popup-form">';
			addHtml += '</ul>';
			addHtml += '<div class="nav-pagination clearFix">';
			addHtml += '<div class="btn-pagination pre-page disabled"><span class="glyphicon glyphicon-triangle-left"></span></div>';
			addHtml += '<div class="btn-pagination next-page disabled"><span class="glyphicon glyphicon-triangle-right"></span></div>';
			addHtml += '</div>';
			addHtml += '</div>';
			addHtml += '</div>';
			$(".popup-cover").append(addHtml);
			_this.getData(_this.startService, _this.limitService);
			// 绑定上一页按钮
			$(".pre-page").bind("click", function() {
				_this.preService(this);
			});
			// 绑定下一页按钮
			$(".next-page").bind("click", function() {
				_this.nextService(this);
			});
			// 绑定关闭按钮
			$("#pop-service").find(".popup-close").bind("click", function() {
				_this.closePop($(this));
			});
			// 选中服务名，关闭弹出层并在页面中显示服务名
			$(".popup-form").on("click", "li", function() {
				$(this).addClass("active").siblings().removeClass("active");
				// 在页面中的服务名输入框中显示该服务名称
				$(container).val($(this).html()).attr("data-service", $(this).attr("data-id"));
				// 关闭弹出层
				_this.closePop($(this));
			})
		});
	},
	// 加载数据
	getData: function(startService, limitService) {
		var _this = this;
		console.log("startService: " + startService, ", limitService: " + limitService);
		$.ajax({
			url: APP_SERVICE + startService + "/" + limitService,
			type: "GET",
			contentType: "application/json;charset=UTF-8",
			success: function(data) {
				// 异常信息
				if (data.code != 200) {
					alert("异常");
					return;
				}
				// 如果返回数据长度为零
				if (data.value.length == 0) {
					if (_this.startService > 1) {_this.startService--;}
					// “下一页”按钮不可用
					$("#pop-service").find(".next-page").addClass("disabled");
					alert("没有数据了");
					return;
				}
				var dataLength = data.value.length;
				var popHeight = 130 + 46 * dataLength;
				var addHtml = '';
				data.value.map(function(value, index) {
					addHtml += '<li data-id="' + value.serviceId + '">' + value.serviceName + '</li>';
				});
				// 判断是否有前一页
				if (startService == 1) {
					$("#pop-service").find(".pre-page").addClass("disabled");
				} else {
					$("#pop-service").find(".pre-page").removeClass("disabled");
				}
				// 判断是否有后一页
				if (data.value.length < limitService) {
					$("#pop-service").find(".next-page").addClass("disabled");
				} else {
					$("#pop-service").find(".next-page").removeClass("disabled");
				}
				$("#pop-service").find(".popup-form").html(addHtml).parents("#pop-service").css({"margin-top": -popHeight / 2, "height": popHeight}).show().parents(".popup-cover").show();
			}
		});
	},
	// 关闭弹出层
	closePop: function(closeObj) {
		$(closeObj).parents(".popup-box").hide().parents(".popup-cover").hide();
	},
	// 点击“上一页”按钮
	preService: function(preBtn) {
		if ($(preBtn).hasClass("disabled")) {return;}
		this.getData(--this.startService, this.limitService);
	},
	// 点击“下一页”按钮
	nextService: function(nextBtn) {
		if ($(nextBtn).hasClass("disabled")) {return;}
		this.getData(++this.startService, this.limitService);
	}
};

// 点击表格，加载弹出层
var loadTree = {
	width: 1200,
	height: 550,
	init: function(serviceId, traceId) {
		var _this = this;
		// 如果已经加载过，显示弹出层
		if ($("#pop-tree").html() != null) {
			$("#pop-tree").show().parents(".popup-cover").show();
		} else {
			// 加载不变的信息
			var addHtml = '';
			addHtml += '<div id="pop-tree" class="popup-box">';
			addHtml += '<p class="popup-title">';
			addHtml += '<span></span>';
			addHtml += '<span class="popup-close"></span>';
			addHtml += '</p>';
			addHtml += '<div class="popup-body">';
			addHtml += '<div id="canvas">';
			addHtml += '</div>';
			addHtml += '</div>';
			addHtml += '</div>';
			$(".popup-cover").append(addHtml).show();
		}
		_this.getData(serviceId, traceId);
		// 关闭当前弹出框并清空数据
		$("#pop-tree").find(".popup-close").bind("click", function() {
			_this.closePop();
		});
	},
	// 关闭弹出层
	closePop: function() {
		$("#canvas").html('').parents("#pop-tree").hide().parents(".popup-cover").hide();
	},
	// 加载数据
	getData: function(serviceId, traceId) {
		$("#pop-tree").find(".popup-title span").eq(0).html("TraceId: " + traceId);
		// $("#canvas").html('');
		var svg = d3.select("#canvas").append("svg").attr("width", this.width).attr("height", this.height).append("g").attr("transform", "translate(100,20)");
		// 定义集群图
		var tree = d3.layout.tree().size([this.width - 200, this.height - 60]);
		// 获取json数据
		d3.json(APP_TRACE + serviceId + "/" + traceId, function(error, data) {
			var root = data.value.nodeVo;
			// 转换json数据
			// nodes中有各节点的子节点(children)、深度(depth)、位置(x, y)、名称(name)——json中的属性
			var nodes = tree.nodes(root);
			// links中有连线两端(source, target)的节点信息
			var links = tree.links(nodes);

			// 定义集群图连线方式. diagonal是一个对角线生成器; projection是一个点变换器
			var diagonal = d3.svg.diagonal().projection(function(d) {
				return [d.x, d.y];
			});

			// 绘制连线
			var link = svg.selectAll(".link").data(links).enter().append("path").attr("class", function(d) {
				if (d.target.exceptionValue) return "tree-link tree-link-error";
				else return "tree-link tree-link-success";
			}).attr("d", diagonal);

			// 连线上信息显示
			var linkText = svg.selectAll(".tree-link-text").data(links).enter().append("text").attr("class", "tree-link-text").attr("transform", function(d) {
				var x = (d.source.x + d.target.x) / 2 - d.target.networkTime.length * 3.5;
				var y = (d.source.y + d.target.y) / 2 - 5;
				return "translate(" + x + "," + y + ")";
			}).text(function(d) {
				return d.target.networkTime;
			});

			// 绘制节点上的信息, 错误节点点击显示错误信息
			var node = svg.selectAll(".node").data(nodes).enter().append("g").attr("class", "node").attr("transform", function(d) {
				return "translate(" + d.x + "," + d.y + ")";
			}).on("click", function(d) {
				loadDetail.init(d);
			});
			// 节点上的圆
			node.append("circle").attr("class", function(d) {
				if (d.exceptionValue) return "tree-circle tree-circle-error";
				else return "tree-circle tree-circle-success";
			}).attr("r", 6);
			// 节点上的文字
			node.append("text").attr("class", "tree-text").attr("dx", function(d) {
				return d.children ? d.appName.length * 4 : -d.appName.length * 4;
			}).attr("dy", 20).style("text-anchor", function(d) {
				return d.children ? "end" : "start";
			}).text(function(d) {
				return d.appName;
			});
		})
	}
};

// 点击节点，展示详细信息
var loadDetail = {
	init: function(data) {
		var _this = this;
		// 如果已经加载过，显示弹出层
		if ($("#pop-detail").html() != null) {
			$("#pop-detail").show().siblings("#pop-tree").hide();
		} else {
			// 加载不变的信息
			var addHtml = '';
			addHtml += '<div id="pop-detail" class="popup-box">';
			addHtml += '<p class="popup-title">';
			addHtml += '<span></span>';
			addHtml += '<span class="popup-close"></span>';
			addHtml += '</p>';
			addHtml += '<div class="popup-body">';
			addHtml += '<div class="form-horizontal popup-form-horizontal">';
			addHtml += '</div>';
			addHtml += '</div>';
			addHtml += '</div>';
			$(".popup-cover").append(addHtml);
		}
		_this.getData(data);
		// 关闭当前弹出框并清空数据, 显示pop-tree
		$("#pop-detail").find(".popup-close").bind("click", function() {
			_this.closePop();
		});
	},
	// 关闭弹出层
	closePop: function() {
		$("#pop-detail").find(".form-horizontal").html('').parents("#pop-detail").hide();
		$("#pop-tree").show();
	},
	getData: function(data) {
		var addHtml = '';
		addHtml += this.formGroup("Address", data.address);
		addHtml += this.formGroup("InvokeTime", data.invokeTime);
		if (data.exceptionValue) {addHtml += this.formGroup("ExceptionValue", data.exceptionValue);}
		var $thisPop = $("#pop-detail");
		$thisPop.find(".popup-title span").eq(0).html("AppName: " + data.appName);
		$thisPop.find(".form-horizontal").html(addHtml);
		var popHeight = $thisPop.find(".popup-body").height() + 80;
		$thisPop.css({"margin-top": -popHeight / 2}).siblings("#pop-tree").hide();
	},
	formGroup: function(name, content) {
		var addHtml = '';
		addHtml += '<div class="form-group">';
		addHtml += '<label class="col-sm-2 control-label">' + name + '</label>';
		addHtml += '<div class="col-sm-10">';
		if (name == "ExceptionValue") {
			var newContent = content.replace("\\n", "<br /><br />");
			addHtml += '<p class="form-control-static">' + newContent + '</p>';
		} else {
			addHtml += '<p class="form-control-static">' + content + '</p>';
		}
		addHtml += '</div>';
		addHtml += '</div>';
		return addHtml;
	}
};

// 点击搜索，展示列表
var loadPage = {
	// preBtn: “上一页”按钮
	preBtn: null,
	// nextBtn: “下一页”按钮
	nextBtn: null,
	// datas: 记录所有符合条件的数据
	datas: [],
	// nowPage: 当前页数
	nowPage: 1,
	// totalPage: 总页数
	totalPage: null,
	// limitList: 每页显示条数
	limitList: null,
	// totalList: 数据总条数
	totalList: null,
	init: function(searchBtn, preBtn, nextBtn, limitList) {
		var _this = this;
		_this.preBtn = preBtn;
		_this.nextBtn = nextBtn;
		_this.limitList = limitList;

		// 点击“搜索”按钮
		$(searchBtn).bind("click", function() {
			// 获取serviceId
			var serviceId = $("#service_name").attr("data-service");
			// 获取traceTime
			var traceTime = $("#start_time").val();
			// 将traceTime转化成时间戳格式
			traceTime = datetime_to_unix(traceTime);
			// 防止服务名或开始时间为空
			if (serviceId == "" || !traceTime) {
				return;
			}
			_this.loadData(parseInt(serviceId), traceTime);
		});
		// 点击“上一页”按钮
		$(preBtn).bind("click", function() {
			if ($(this).hasClass("disabled")) {return;}
			_this.getData(--_this.nowPage);
		});
		// 点击“下一页”按钮
		$(nextBtn).bind("click", function() {
			if ($(this).hasClass("disabled")) {return;}
			_this.getData(++_this.nowPage);
		});
		// 点击“跳转”按钮
		$(".btn-turn").bind("click", function() {
			var turnPage = $("#turnPage").val().trim();
			// 防止输入有误
			if (!checkNum(turnPage)) {
				alert("请输入正确的跳转页码");
				return;
			}
			turnPage = parseInt(turnPage);
			// 防止超出页码范围
			if (turnPage < 1 || turnPage > _this.totalPage) {
				alert("页数超出范围");
				return;
			}
			// 防止重复查询
			if (turnPage == _this.nowPage) {
				return;
			}
			_this.nowPage = turnPage;
			_this.getData(_this.nowPage);
		});
	},
	// 查询所有符合条件的记录
	loadData: function(serviceId, traceTime) {
		var _this = this;

		// 首先清空表格数据
		document.getElementById('content').innerHTML = "";
		
		// 定义POST请求数据
		var data = {
			"serviceId": serviceId,
			"traceTime": traceTime
		};
		// 获取请求数据
		Net.getSearchData(JSON.stringify(data), function(result) {
			// 异常信息
			if (result.code != 200) {
				alert("异常");
				return;
			}

			var value = result.value;
			// 如果返回数据长度为零
			if (value.length == 0) {
				alert("没有数据了");
				return;
			}

			// 重新格式化返回数据的时间
			for (var i = 0; i < value.length; i++) {
				value[i].traceTime = new Date(value[i].traceTime).Format('yyyy-MM-dd hh:mm:ss');
				value[i].nodeVo = JSON.stringify(value[i].nodeVo);
			}

			_this.datas = value;
			// 计算总数据条数
			_this.totalList = value.length;
			_this.nowPage = 1;
			// 计算总页数
			_this.totalPage = Math.ceil(_this.totalList / _this.limitList);

			// 页面部分信息显示
			$("#totalPage").html(_this.totalPage);
			$("#totalList").html(_this.totalList);
			$(".footer-data").show();

			// 显示数据
			_this.getData(_this.nowPage);
		});
	},
	// 显示当页数据
	getData: function(nowPage) {
		// 存放当页显示的数据
		var list = { value: [] };

		var _this = this;
		_this.nowPage = nowPage;

		// 判断是否有前一页
		if (_this.nowPage <= 1) {
			_this.nowPage = 1;
			$(_this.preBtn).addClass("disabled");
		} else {
			$(_this.preBtn).removeClass("disabled");
		}
		// 判断是否有后一页
		if (_this.nowPage >= _this.totalPage) {
			_this.nowPage = _this.totalPage;
			$(_this.nextBtn).addClass("disabled");
		} else {
			$(_this.nextBtn).removeClass("disabled");
		}

		// 页面上显示当前页码
		$("#list_now_num").html(_this.nowPage);

		// 赋值当页数据
		for (var i = (_this.nowPage - 1) * _this.limitList; i < _this.nowPage * _this.limitList; i++) {
			// 防止没有数据
			if (!_this.datas[i]) {break;}
			list.value.push(_this.datas[i]);
		}
		var addHtml = template('test', list);
		document.getElementById('content').innerHTML = addHtml;
	}
};

// 检验输入的是否都是数字
function checkNum(num) {
	return /^[1-9][0-9]*$/.test(num);
}

$(function() {
	// limitService: 每页显示服务名条数, limitList: 每页显示数据条数
	var limitService = 5, limitList = 20;
	
	// 初始化选择服务名, 参数一: 触发元素, 参数二: 每页加载条数
	loadService.init($("#service_name"), limitService);

	// 初始化查询列表, 参数一: 搜索按钮, 参数二: “上一页”按钮, 参数三: “下一页”按钮, 参数四: 每页加载条数
	loadPage.init($("#search"), $("#list_pre_btn"), $("#list_next_btn"), limitList);
	

	//日期插件初始化
	$('.form_datetime').datetimepicker({
		format:'yyyy-mm-dd hh:ii:ss',
		autoclose: 1,
		todayBtn:  1
	});

	// 点击表格弹出d3树
	$(".page-container").on("click", ".data_info", function() {
		var id = $(this).find("td").eq(0).html();
		loadTree.init($('.popup-form').find('.active').attr('data-id'), id);
	});
});