<template>
  <div class="app-container">
    <div class="formBox">
      <el-form :inline="true" class="demo-form-inline topsearch">
        <el-form-item>
          <el-select v-model="listQuery.type1" placeholder="争议类型" clearable filterable size="small">
            <el-option label="争议类型1" :value="1"></el-option>
            <el-option label="争议类型2" :value="2"></el-option>
            <el-option label="争议类型3" :value="3"></el-option>
            <el-option label="争议类型4" :value="4"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-input v-model="listQuery.title" size="small" placeholder="申请人" :maxlength="50" clearable/>
        </el-form-item>
        <el-form-item>
          <el-input v-model="listQuery.title" size="small" placeholder="被申请人" :maxlength="50" clearable/>
        </el-form-item>
        <el-form-item>
          <el-select v-model="listQuery.type2" placeholder="受理状态" clearable filterable size="small">
            <el-option label="未受理" :value="1"></el-option>
            <el-option label="已受理" :value="2"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-date-picker v-model="listQuery.time" value-format="yyyy-MM-dd HH:mm:ss" type="datetime" size="small"
                          placeholder="申请时间"/>
        </el-form-item>
        <el-form-item>
          <el-button size="small" type="primary" icon="el-icon-search" @click="search">查询</el-button>
        </el-form-item>
        <el-form-item>
          <el-button size="small" type="default" icon="el-icon-refresh" @click="refresh">重置</el-button>
        </el-form-item>
      </el-form>
      <div class="rightBox"><el-button size="small" type="success" icon="el-icon-plus" @click="handleEdit">预立案登记</el-button></div>
    </div>


    <el-table
      v-loading="listLoading"
      :data="list"
      element-loading-text="Loading"
      stripe
      border
      fit
      height="100%"
      class="table-container mar"
      highlight-current-row
    >
      <el-table-column
        fixed
        label="序号"
        width="80"
        type="index"
        align="center"
      />

      <el-table-column
        label="案件编号"
        align="center"
      >
        <template slot-scope="scope">
          {{ scope.row.title }}
        </template>
      </el-table-column>
      <el-table-column
        label="争议类型"
        align="center"
      >
        <template slot-scope="scope">
          <span>{{ scope.row.id }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="申请人"
        align="center"
      >
        <template slot-scope="scope">
          {{ scope.row.adminName }}
        </template>
      </el-table-column>
      <el-table-column
        label="案由"
        align="center"
      >
        <template slot-scope="scope">
          {{ scope.row.adminName }}
        </template>
      </el-table-column>
      <el-table-column
        label="申请时间"
        align="center"
      >
        <template slot-scope="scope">
          {{ scope.row.addtime }}
        </template>
      </el-table-column>
      <el-table-column
        label="受理状态"
        align="center"
      >
        <template slot-scope="scope">
          <el-tag type="success" size="mini">已受理</el-tag>
        </template>
      </el-table-column>
      <el-table-column
        label="登记机构"
        align="center"
      >
        <template slot-scope="scope">
          <span>{{ scope.row.id }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="受理人"
        align="center"
      >
        <template slot-scope="scope">
          <span>{{ scope.row.adminName }}</span>
        </template>
      </el-table-column>
      <el-table-column
        fixed="right"
        label="操作"
        width="250"
        align="center"
      >
        <template slot-scope="scope">
          <el-button
            type="success"
            size="mini"
            @click="handleEdit(scope.row)"
          >
            编辑
          </el-button>
          <el-button
            type="danger"
            size="mini"
            @click="del(scope.row)"
          >
            删除
          </el-button>
          <el-button
            type="primary"
            size="mini"
            @click="look(scope.row)"
          >
            查看详情
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="listQuery.pageNo"
      :limit.sync="listQuery.pageSize"
      @pagination="fetchData"
    />
    <edit ref="edit" @fetch-data="fetchData"></edit>
    <views ref="views" @fetch-data="fetchData"></views>
  </div>
</template>

<script>
import Pagination from '@/components/Pagination'
import Edit from './components/edit'
import Views from './components/view'
// import { getList,add,edit,remove } from "@/api/manyidu";
// import { getRenList } from "@/api/common";
const _temp = {
  id: '',
  content: '',
  title: ''
}

export default {
  components: {
    Pagination,Edit,Views
  },
  data() {
    return {
      rules: {
        title: [
          {required: true, message: '请输入标题名称', trigger: 'blur'}
        ]
      },
      total: 0,
      list: [{id: 1, title: '111', adminName: 'admin', addtime: '2024-5-18'}],
      listLoading: false,
      listQuery: {
        pageNo: 1,
        pageSize: 20,
        title: '',
        type1: 1,
        type2: '',
        startTime: '',
        endTime: '',
        time: []
      },
      temp: Object.assign({}, _temp),
      dialogVisible: false,
      dialogType: 'create',
      loading: false
    }
  },
  created() {
    // this.fetchData();
    //  this.getren();
  },
  methods: {
    handleEdit(row) {
      if (row) {
        this.$refs["edit"].showEdit(row);
      } else {
        this.$refs["edit"].showEdit();
      }
    },
    look(scope) {
      this.$refs["views"].showEdit(scope);
    },
    search() {
      this.listQuery.pageNo = 1
      // this.fetchData();
    },
    refresh() {
      this.listQuery = {
        pageNo: 1,
        pageSize: 20,
        title: '',
        schoolId: '',
        adminId: '',
        adminName: '',
        startTime: '',
        endTime: '',
        time: []
      }
      // this.fetchData();
    },
    fetchData() {
      this.listLoading = true
      this.listQuery.startTime = this.listQuery.time[0]
      this.listQuery.endTime = this.listQuery.time[1]
      // getList(this.listQuery).then((response) => {
      //   this.list = response.data
      //   this.total = response.count
      //   this.listLoading = false
      // })
    },
    resetTemp() {
      this.temp = Object.assign({}, _temp)
    },
    add() {
      this.resetTemp()
      this.dialogVisible = true
      this.dialogType = 'create'
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    edit(scope) {
      this.resetTemp()
      this.dialogVisible = true
      this.dialogType = 'modify'
      this.temp = JSON.parse(JSON.stringify(scope.row))
      console.log(scope.row)
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },

    del(scope) {
      this.$confirm('您是否确认删除该项？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        remove({id: scope.row.id}).then((response) => {
          if (response.code == 0) {
            this.list.splice(scope.$index, 1)
            this.$message({
              message: '删除成功',
              type: 'success'
            })
          }
        })
      })
    },
    submit() {
      this.$refs.dataForm.validate((valid) => {
        if (valid) {
          if (this.loading) {
            return
          }
          this.loading = true
          if (this.dialogType == 'create') {
            add(this.temp).then((response) => {
              if (response.code == 0) {
                this.$message({
                  message: '新增成功',
                  type: 'success'
                })
                this.dialogVisible = false
                this.loading = false
                this.fetchData()
              }
            })
          } else {
            edit(this.temp).then((response) => {
              if (response.code == 0) {
                this.$message({
                  message: '编辑成功',
                  type: 'success'
                })
                this.dialogVisible = false
                this.loading = false
                this.fetchData()
              }
            })
          }
        } else {
          console.log('error submit!!')
          return false
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.formBox {
  width: 100%;
  background: linear-gradient( 180deg, #F7FAFF 0%, #FFFFFF 25%);
  box-shadow: 0px 0px 12px 0px #E6EDF1;
  border-radius: 16px 16px 16px 16px;
  border: 1px solid rgba(0,0,0,0.06);
}
</style>
