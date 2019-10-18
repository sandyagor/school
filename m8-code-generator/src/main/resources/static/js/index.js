$(function () {
	// iframe自适应
	$(window).on('resize', function() {
		var $content = $('.content');
		$content.height($(this).height() - 120);
		$content.find('iframe').each(function() {
			$(this).height($content.height());
		});
	}).resize();
	
});
var vm = new Vue({
	el:'#rrapp',
	data(){
		return {
			main:"main.html",
	        navTitle:"代码自动生成",
	        dsName:"dev_m8cmp_microservice",
	        dsOptions:[],
		}
	},
	created:function(){  
		this.getDataSourceInfo();
    }, 
    methods: {
        donate: function () {
            layer.open({
                type: 2,
                title: false,
                area: ['806px', '467px'],
                closeBtn: 1,
                shadeClose: false,
                content: ['http://cdn.renren.io/donate.jpg', 'no']
            });
        },
        selectVal: function(ele) {
        	this.chooseDS(this.dsName);
        	var fim = document.getElementById("childframe").contentWindow.vm.query();
            this.selected = ele.target.value;
        },
        chooseDS: function(dsName) {
        	$.ajax({
				type: "GET",
			    url: "sys/generator/dselect?dataSourceName=" + dsName,
                contentType: "application/json",
			    data: {},
			    success: function(r){
			    	if(r.code === 0){
			    		
					} else {
						alert(r.msg);
					}
				}
			});
    	},
    	getDataSourceInfo:function(){
	        let _self = this;
			$.ajax({
				type: "GET",
			    url: "sys/generator/dataSourceInfo",
		        contentType: "application/json",
			    data: {},
			    success: function(r){
			    	if(r.code === 0) {
			    		_self.dsOptions = r.dsOptions;
					} else {
						alert(r.msg);
					}
				}
			});
    	},
    }
    
});

// 路由
var router = new Router();
var menus = ["main.html","tables.html","generator.html"];
routerList(router, menus);
router.start();

function routerList(router, menus){
	for(var index in menus){
		router.add('#'+menus[index], function() {
			var url = window.location.hash;

			// 替换iframe的url
			vm.main = url.replace('#', '');

			// 导航菜单展开
			$(".treeview-menu li").removeClass("active");
			$("a[href='"+url+"']").parents("li").addClass("active");

			vm.navTitle = $("a[href='"+url+"']").text();
		});
	}
}
