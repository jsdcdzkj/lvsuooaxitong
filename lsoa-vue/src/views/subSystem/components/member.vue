<template>
  <el-dialog
      title="人员信息"
      append-to-body
      :visible.sync="dialogFormVisible"
      top="10vh"
      width="1000px"
  @close="close">
      <el-row :gutter="0">
        <el-col :span="24">
          <el-form :inline="true"  class="demo-form-inline" :model="queryParams" @submit.native.prevent ref="queryParams"  size="small ">
            <el-form-item>
<!--              <ebs-tree-select :options="treeData" v-model="queryParams.unitId" :accordion="true" @node-click="nodeClick" placeholder="选择所属单位" />-->
            </el-form-item>
            <el-form-item>
              <el-select v-model="queryParams.roleId" placeholder="角色名称" clearable>
                <el-option v-for="item in roleList" :key="item.id" :label="item.roleName" :value="item.id"></el-option>
              </el-select>
            </el-form-item>

            <el-form-item>
              <el-input v-model.trim="queryParams.realName" clearable placeholder="用户名称" @keyup.enter.native="queryData"/>
            </el-form-item>

            <el-form-item style="margin-left: 12px;">
              <el-button type="primary" icon="el-icon-search" @click="queryData" >查询</el-button>
              <el-button type="primary" icon="el-icon-refresh" plain  @click="reset" >重置</el-button>
            </el-form-item>
          </el-form>
        </el-col>
      </el-row>

      <div class="relative">
          <el-table
          v-loading="listLoading"
          :data="list"
          :element-loading-text="elementLoadingText"
          height="calc(100vh - 10vh - 400px)"
          border
          ref="multipleTable"
          row-key="id"
          tooltip-effect="dark"
          @selection-change="handleSelectionChange">
            <el-table-column
            align="center"
              type="selection"
            :reserve-selection="true"
              width="55">
            </el-table-column>
            <el-table-column  label="序号" width="50" align="center">
              <template slot-scope="scope">
                {{(queryParams.pageNo-1) * queryParams.pageSize+scope.$index+1}}
              </template>
            </el-table-column>
            <el-table-column show-overflow-tooltip prop="realName" label="用户名称" width="100" align="center"></el-table-column>
            <el-table-column show-overflow-tooltip prop="deptName" label="所属单位" min-width="200" align="center"></el-table-column>
            <el-table-column show-overflow-tooltip prop="roleNamesStr" label="角色" min-width="200" align="center"></el-table-column>
            <el-table-column show-overflow-tooltip prop="phone" label="手机号" width="120" align="center"></el-table-column>
          </el-table>
          <el-pagination
            background
            :current-page="queryParams.pageNo"
            :page-size="queryParams.pageSize"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange" @current-change="pageCurrentChange"
            :total="total"
          ></el-pagination>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="close">取 消</el-button>
        <el-button type="primary" @click="save" :disabled="disabled" :loading="disabled">确 定</el-button>
      </span>
    </el-dialog>
</template>

<script>

// import {getUserPage,orgTreeList,getRoleList} from "@/api/teamGroup";
// import EbsTreeSelect from "@/components/BoTreeSelect/index.vue";

export default {
  name: 'add',
  components: {},
  props:{
    skillGroupList:{  // 接收父组件传递过来的值
      type: Array
    }
  },
  data() {
    return {
      loading: false,
      disabled: false,
      title: '',
      total: 20,
      listLoading:false,
      elementLoadingText: '正在加载...',
      dialogFormVisible: false,
      multipleSelection: [],
      queryParams: {
        pageNo: 1,
        pageSize: 10,
        unitId: '',
        roleId: '',
        realName: '',
      },
      options: [

      ],
      list: [
        {
          id: 1,
          infoA: '张三',
          infoB: '34234234',
          infoC: '男',
          infoD: '组长',
        },
        {
          id: 2,
          infoA: '张四',
          infoB: '34234234',
          infoC: '男',
          infoD: '组长',
        }
      ],
      treeData: [],
      roleList: [],
      skillGroupList: [],
      chooseIdList: [],


    }
  },
  created() {

  },
  methods: {
    showEdit() {
      this.title = '新增'
      this.form = {}
      this.dialogFormVisible = true
      // this.processingData();
      // this.fetchData()
      // this.getTreeData()
      // this.getRoleList()
    },
    processingData() {
      var tempList = this.skillGroupList
      for (var i =0 ; i< tempList.length ; i++){
        this.chooseIdList.push(tempList[i].id);
      }
    },

    nodeClick(node){
      this.queryParams.unitId = node.data.id
    },
    close() {
      this.chooseIdList= [];
      this.dialogFormVisible = false
    },
    // 切换页面显示个数操作
    handleSizeChange(val) {
      this.queryParams.pageNo = 1;
      this.queryParams.pageSize = val;
      this.fetchData();
    },
    // 切换页面页码
    pageCurrentChange(val){
      this.queryParams.pageNo = val;
      this.fetchData();
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    save() {
      // this.$message({
      //   message: '保存成功',
      //   type: 'success'
      // });
      // var resultList = [];
      // for (var i = 0 ; i < this.multipleSelection.length ; i++){
      //   var count  = -1;
      //   for (var j = 0 ; j < this.skillGroupList.length ; j++){
      //     if (this.multipleSelection[i].id === this.skillGroupList[j].id){
      //       count = j
      //     }
      //   }
      //   if (count === -1){
      //     resultList.push(this.multipleSelection[i])
      //   }else {
      //     resultList.push(this.skillGroupList[j])
      //   }
      // }
      this.chooseIdList = [];
      this.$emit('choosePeople', this.multipleSelection );
      this.dialogFormVisible = false
    },
    async fetchData() {
      this.listLoading = true
      const { data, totalCount } = await getUserPage(this.queryParams)
      this.list = data.records
      this.total = data.total

      this.$nextTick(()=>{
        this.list.forEach(row => {
          if(this.chooseIdList.indexOf(row.id) >= 0){
            this.$refs.multipleTable.toggleRowSelection(row,true);
          }else {
            this.$refs.multipleTable.toggleRowSelection(row,false);

          }
        })
      })

      setTimeout(() => {
        this.listLoading = false
      }, 300)
    },
    // 单位数据
    getTreeData() {
      orgTreeList().then((res) => {
        if (res.code === 0) {
          this.treeData = res.data;
        }
      })
    },
    async getRoleList() {
      const {data} = await getRoleList()
      this.roleList = data
    },
    queryData() {
      this.queryParams.pageNo = 1
      this.fetchData()
    },
    reset() {
      this.queryParams = {
        pageNo: 1,
        pageSize: 10,
        realName: '',
        loginName: '',
        roleId: '',
        deptId: '',
        unitId: '',
      }
      this.fetchData()
    },

  },
}
</script>
