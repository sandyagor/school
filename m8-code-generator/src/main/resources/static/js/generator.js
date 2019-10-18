$(function () {
    $("#jqGrid").jqGrid({
        url: 'sys/generator/catalogList',// sys/generator/list
        datatype: "json",
        postData:{"dataSourceName": ''},
        colModel: [			
			{ label: '目录编码', name: 'catalogCode', width: 100, key: true },
			{ label: '名称', name: 'catalogName', width: 70},
			{ label: '目录类型', name: 'catalogTypeName', width: 50},
			{ label: '备注', name: 'catalogNote', width: 100 }
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
			catalogName: null,
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
                postData:{'catalogName': vm.q.catalogName,"dataSourceName": ds},
                page:1 
            }).trigger("reloadGrid");
		},
		generator: function() {
			var catalogCodes = getSelectedRows();
			var packageName = vm.q.packageName;
			var selected = vm.q.selected;
			var moduleName = vm.q.moduleName;
			var dataSourceName = parent.$("select").find("option:selected").val();
			if(catalogCodes == null){
				return ;
			}
			location.href = "sys/generator/catalogCode?catalogCode=" + JSON.stringify(catalogCodes)+"&packageName="+packageName+"&selected="+selected+"&moduleName="+moduleName+"&dataSourceName="+dataSourceName;;
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

