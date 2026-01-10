<template>
  <el-drawer :visible.sync="dialogFormVisible" :size="variables.drawWidth" @close="close">
    <span slot="title" class="drawer-title"><b>{{ title }}</b></span>

    <div class="drawer-content">
      <div class="view-title">
        <h4>案件基本信息</h4>
      </div>
      <el-form :inline="true" :model="formInline" :rules="rules" ref="formInline">

        <el-form-item label="案件编号" prop="name">
          <el-input v-model="formInline.name" placeholder="请输入案件编号"></el-input>
        </el-form-item>

        <el-form-item label="争议类别" prop="groupDesc">
          <el-select v-model="formInline.groupDesc" placeholder="争议类别" clearable filterable size="small">
            <el-option label="争议类别1" :value="1"></el-option>
            <el-option label="争议类别2" :value="2"></el-option>
          </el-select>
        </el-form-item>

      </el-form>

      <div class="view-title">
        <h4>申请人信息</h4>
        <div class="title-rt">
          <el-button type="primary" size="mini" icon="el-icon-plus" @click="handelMember">新建</el-button>
        </div>
      </div>
      <el-table v-loading="listLoading" ref="list" :data="list" :element-loading-text="elementLoadingText"
                height="calc(100vh - 740px)"
                border>
        <el-table-column label="序号" width="70" align="center">
          <template slot-scope="scope">
            {{ scope.$index + 1 }}
          </template>
        </el-table-column>
        <el-table-column show-overflow-tooltip prop="realName" label="组员名称" align="center"
                         width="120"></el-table-column>
        <!--        <el-table-column show-overflow-tooltip prop="deptName" label="员工编号" align="center"-->
        <!--          width="120"></el-table-column>-->
        <el-table-column show-overflow-tooltip prop="deptName" label="所属单位" min-width="160"
                         align="center"></el-table-column>
        <el-table-column show-overflow-tooltip prop="roleNamesStr" label="角色" min-width="160"
                         align="center"></el-table-column>
        <el-table-column show-overflow-tooltip prop="phone" label="手机号" align="center"
                         width="120"></el-table-column>
        <el-table-column show-overflow-tooltip label="组角色" align="center" width="160">
          <template slot-scope="scope">
            <el-select v-show="scope.row.isOK" v-model="scope.row.roleId" placeholder="请选择组角色"
                       style="width: 100%;" @change="changeRole(scope.row,scope.$index)">
              <el-option v-for="item in roleList" :key="item.dictValue" :label="item.dictLabel"
                         :value="item.dictValue"></el-option>
            </el-select>
            <!--            <span v-show="!scope.row.isOK" v-if="scope.row.roleId === '1'" style="color: red">{{ scope.row.infoD }}</span>-->
            <!--            <span v-show="!scope.row.isOK" v-else >{{ scope.row.infoD }}</span>-->

            <el-tag v-show="!scope.row.isOK" v-if="scope.row.roleId === '1'" type="danger">{{
                scope.row.infoD
              }}
            </el-tag>
            <el-tag v-show="!scope.row.isOK" v-else type="success">{{ scope.row.infoD }}</el-tag>
          </template>

        </el-table-column>
        <el-table-column show-overflow-tooltip label="操作" width="140" align="center">
          <template slot-scope="scope">
            <el-button type="primary" v-if="!scope.row.isOK" @click="editVersion(scope.row,scope.$index)" plain
                       size="mini">编辑
            </el-button>
            <el-button type="success" v-else @click="saveVersion(scope.row,scope.$index)" plain size="mini">保存
            </el-button>
            <el-button type="warning" plain size="mini" @click="delVersion(scope.row,scope.$index)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="view-title mt10">
        <h4>被申请人信息</h4>
        <div class="title-rt">
          <el-button type="primary" size="mini" icon="el-icon-plus" @click="handelMember">新建</el-button>
        </div>
      </div>
      <el-table v-loading="listLoading" ref="list" :data="list" :element-loading-text="elementLoadingText"
                height="calc(100vh - 740px)"
                border>
        <el-table-column label="序号" width="70" align="center">
          <template slot-scope="scope">
            {{ scope.$index + 1 }}
          </template>
        </el-table-column>
        <el-table-column show-overflow-tooltip prop="realName" label="组员名称" align="center"
                         width="120"></el-table-column>
        <!--        <el-table-column show-overflow-tooltip prop="deptName" label="员工编号" align="center"-->
        <!--          width="120"></el-table-column>-->
        <el-table-column show-overflow-tooltip prop="deptName" label="所属单位" min-width="160"
                         align="center"></el-table-column>
        <el-table-column show-overflow-tooltip prop="roleNamesStr" label="角色" min-width="160"
                         align="center"></el-table-column>
        <el-table-column show-overflow-tooltip prop="phone" label="手机号" align="center"
                         width="120"></el-table-column>
        <el-table-column show-overflow-tooltip label="组角色" align="center" width="160">
          <template slot-scope="scope">
            <el-select v-show="scope.row.isOK" v-model="scope.row.roleId" placeholder="请选择组角色"
                       style="width: 100%;" @change="changeRole(scope.row,scope.$index)">
              <el-option v-for="item in roleList" :key="item.dictValue" :label="item.dictLabel"
                         :value="item.dictValue"></el-option>
            </el-select>
            <!--            <span v-show="!scope.row.isOK" v-if="scope.row.roleId === '1'" style="color: red">{{ scope.row.infoD }}</span>-->
            <!--            <span v-show="!scope.row.isOK" v-else >{{ scope.row.infoD }}</span>-->

            <el-tag v-show="!scope.row.isOK" v-if="scope.row.roleId === '1'" type="danger">{{
                scope.row.infoD
              }}
            </el-tag>
            <el-tag v-show="!scope.row.isOK" v-else type="success">{{ scope.row.infoD }}</el-tag>
          </template>

        </el-table-column>
        <el-table-column show-overflow-tooltip label="操作" width="140" align="center">
          <template slot-scope="scope">
            <el-button type="primary" v-if="!scope.row.isOK" @click="editVersion(scope.row,scope.$index)" plain
                       size="mini">编辑
            </el-button>
            <el-button type="success" v-else @click="saveVersion(scope.row,scope.$index)" plain size="mini">保存
            </el-button>
            <el-button type="warning" plain size="mini" @click="delVersion(scope.row,scope.$index)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <div class="footer-btn">
      <el-button @click="close" size="small">暂存</el-button>
      <el-button type="primary" size="small" :disabled="disabled" :loading="disabled" @click="save('formInline')">保存
      </el-button>
    </div>

    <member ref="member" @fetch-data="" @choosePeople="handleCustomEvent" :skill-group-list="this.list"></member>
  </el-drawer>
</template>

<script>
import variables from '@/styles/variables.scss'
import Member from './member.vue'
// import {detailTeam, editTeam, getroleDicts} from "@/api/teamGroup";
export default {
  name: 'add',
  components: {Member},
  data() {
    return {
      loading: false,
      disabled: false,
      title: '',
      listLoading: false,
      elementLoadingText: '正在加载...',
      dialogFormVisible: false,
      formInline: {
        id: '',
        name: '',
        groupDesc: '',
        list: [],
      },
      rules: {
        name: [
          {required: true, message: '请输入班组名称', trigger: 'blur'},
        ],
        groupDesc: [
          {required: true, message: '请输入班组说明', trigger: 'blur'},
        ],
      },
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
      roleList: [],

    }
  },
  computed: {
    variables() {
      return variables
    },
  },
  created() {
  },
  methods: {
    showEdit(row) {
      if (row) {
        this.title = '编辑'
        this.form = Object.assign({}, row)
        this.formInline.id = row.id;
        // this.getInfo();
      } else {
        this.title = '新增'
        this.form = {}
      }
      // this.getRoleList();
      this.dialogFormVisible = true
    },
    close() {
      this.list = [];
      this.formInline = {};
      this.disabled = false;
      this.dialogFormVisible = false
    },
    changeRole(row, index) {

      var id = row.roleId;
      for (var i = 0; i < this.roleList.length; i++) {
        if (this.roleList[i].dictValue === id) {
          this.$set(this.list, index, {
            isOK: true,
            id: row.id,
            realName: row.realName,
            deptName: row.deptName,
            roleNamesStr: row.roleNamesStr,
            phone: row.phone,
            roleId: id,
            infoD: this.roleList[i].dictLabel
          });
        }
      }

    },
    handelMember() {
      // this.list = [];
      this.$refs['member'].showEdit()
    },
    editVersion(row, index) {
      this.$set(this.list, index, {
        isOK: true,
        id: row.id,
        realName: row.realName,
        deptName: row.deptName,
        roleNamesStr: row.roleNamesStr,
        phone: row.phone,
        roleId: row.roleId,
        infoD: row.infoD
      });
    },
    saveVersion(row, index) {
      this.$set(this.list, index, {
        isOK: false,
        id: row.id,
        realName: row.realName,
        deptName: row.deptName,
        roleNamesStr: row.roleNamesStr,
        phone: row.phone,
        roleId: row.roleId,
        infoD: row.infoD
      });
    },
    save(formName) {


      var count = -1;
      var num = -1;
      var tempList = [];
      for (var i = 0; i < this.list.length; i++) {
        if (this.list[i].roleId === null || this.list[i].roleId === '' || this.list[i].roleId === undefined) {
          count++;
        }
        if (this.list[i].roleId === '1') {
          num++;
        }
        var z = {};
        z.userId = this.list[i].id;
        z.teamRole = this.list[i].roleId;
        tempList.push(z);
      }
      if (count !== -1) {
        this.$message({
          type: 'error',
          message: '请选择班组成员角色!'
        });
        return;
      }
      if (num === -1) {
        this.$message({
          type: 'error',
          message: '请选择组长!'
        });
        return;
      }
      if (num > 0) {
        this.$message({
          type: 'error',
          message: '有且只能有一位组长!'
        });
        return;
      }


      var x = {};
      x.id = this.formInline.id
      x.name = this.formInline.name
      x.groupDesc = this.formInline.groupDesc;
      x.list = tempList;

      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.disabled = true;

          editTeam(x).then((res) => {
            if (res.code === 0) {
              setTimeout(() => {
                this.disabled = false;
                // this.dialogFormVisible = false;
                this.$message({
                  type: 'success',
                  message: '操作成功!'
                });
              }, 1000);
              this.$emit("fetch-data");
              this.close();
            } else {
              this.disabled = false;
              this.$message({
                type: 'error',
                message: res.msg
              });
            }

          })


        } else {
          return false;
        }
      });
    },

    //删除
    delVersion(row, index) {
      this.$confirm('此操作将删除该信息, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.list.splice(index, 1)
        this.$message({
          type: 'success',
          message: '删除成功!'
        });
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    handleCustomEvent(data) {
      // debugger;
      var delIds = [];
      //删除取消勾选的数据
      for (var j = 0; j < this.list.length; j++) {
        var count = -1;
        for (var i = 0; i < data.length; i++) {
          if (this.list[j].id === data[i].id) {
            count++;
          }
        }
        if (count === -1) {
          delIds.push(j);
        }

      }

      delIds.sort(this.compare);
      for (var i = 0; i < delIds.length; i++) {
        this.list.splice(delIds[i], 1);
      }


      //添加
      for (var i = 0; i < data.length; i++) {
        var count = -1;
        for (var j = 0; j < this.list.length; j++) {
          if (this.list[j].id === data[i].id) {
            count++;
          }
        }
        if (count === -1) {
          data[i].isOK = false;
          // data[i].infoD = '';
          data[i].roleId = this.roleList[this.roleList.length - 1].dictValue;
          data[i].infoD = this.roleList[this.roleList.length - 1].dictLabel;
          this.list.push(data[i]);
        }
      }
    },
    getRoleList() {
      getroleDicts().then((res) => {
        if (res.code === 0) {
          this.roleList = res.data
        }
      })
    },
    //排序
    compare(val1, val2) {
      return val2 - val1;
    },

    //详情
    getInfo() {
      var x = {};
      x.id = this.formInline.id;
      detailTeam(x).then((res) => {
        if (res.code === 0) {
          this.formInline = res.data;

          var listValue = res.data.list;
          var tempList = [];

          for (var i = 0; i < listValue.length; i++) {
            listValue[i].isOK = false;
            listValue[i].roleId = listValue[i].teamRole;
            listValue[i].infoD = listValue[i].teamRoleName;
            listValue[i].id = listValue[i].userId;
            tempList.push(listValue[i]);
          }


          this.list = tempList

        }
      })
    }


  },
}
</script>
<style scoped lang="scss">
::v-deep {
  .el-drawer__header {
    padding: 10px !important;
    margin-bottom: 14px;
    border-bottom: 1px solid #d1d9e1;
  }
}
</style>
