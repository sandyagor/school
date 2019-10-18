$(function () {
    $("#jqGrid").jqGrid({
        url: 'sys/generator/list',
        postData:{"dataSourceName": ''},
        datatype: "json",
        colModel: [			
			{ label: '表名', name: 'tableName', width: 100, key: true },
			{ label: 'Engine', name: 'engine', width: 70},
			{ label: '表备注', name: 'tableComment', width: 100 },
			{ label: '创建时间', name: 'createTime', width: 100 }
        ],
		viewrecords: true,
        height: 385,
        rowNum: 50,
		rowList : [10,30,50,100,200,500],
        rownumbers: true, 
        rownumWidth: 25, 
        autowidth:true,
        multiselect: true,
        pager: "#jqGridPager",
        jsonReader : {
            root: "page.list",
            page: "page.currPage",
            total: "page.totalPage",
            records: "page.totalCount"
        },
        prmNames : {
            page:"page", 
            rows:"limit", 
            order: "order"
        },
        gridComplete:function(){
        	//隐藏grid底部滚动条
        	$("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
        }
    });
});

var vm = new Vue({
	el:'#rrapp',
	data:{
		q:{
			tableName: null,
			packageName:'',
			selected:'0',
			moduleName:'',
			bool:false
		}
	},
	methods: {
		query: function () {
			let ds = parent.$("select").find("option:selected").val();
			if(!ds){
				ds= '';
			}
			$("#jqGrid").jqGrid('setGridParam',{ 
                postData:{'tableName': vm.q.tableName, "dataSourceName": ds},
                page:1
            }).trigger("reloadGrid");
		},
		generator: function() {
			var tableNames = getSelectedRows();
			var packageName = vm.q.packageName;
			var selected = vm.q.selected;
			var moduleName = vm.q.moduleName;
			var dataSourceName = parent.$("select").find("option:selected").val();
			if(tableNames == null){
				return ;
			}
			location.href = "sys/generator/code?tables=" + JSON.stringify(tableNames)+"&packageName="+packageName+"&selected="+selected+"&moduleName="+moduleName+"&dataSourceName="+dataSourceName;
		},
		inOne:function(){
			vm.q.bool=true;
		},
		notOne:function(){
			vm.q.bool=false;
			vm.q.moduleName='';
		}
	}
});

