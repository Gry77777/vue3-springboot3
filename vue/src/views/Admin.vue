<template>

  <div>
    <div class="card" style="margin-bottom: 5px" >
      <el-input v-model="data.name" style="width: 240px;margin-right:10px" placeholder="请输入查询的内容" prefix-icon="Search"></el-input>
      <el-button type="primary" @click="load">查询</el-button>
      <el-button type="warning" @click="reset">重置</el-button>
    </div>
    <div class="card" style="margin-bottom: 5px">
      <el-button type="primary" @click="handleAdd">新增</el-button>
      <el-button type="danger" @click="deleteBatch">批量删除</el-button>
<!--      <el-button type="info">导入</el-button>-->
<!--      <el-button type="success">导出</el-button>-->
    </div>

    <div class="card" style="margin-bottom: 5px">
      <el-table :data="data.tableData" stripe @selection-change="handleSelectionChange">
        <el-table-column type="selection"  width="50px"/>
        <el-table-column label="账号" prop="username"></el-table-column>
        <el-table-column label="头像">
          <template #default="scope">
            <img v-if="scope.row.avatar" :src="scope.row.avatar" style="width: 50px;height: 50px;display: block;border-radius: 50%" alt="">
          </template>
        </el-table-column>
        <el-table-column label="名称" prop="name"></el-table-column>
        <el-table-column label="操作" width="120px">
          <template #default="scope">
            <el-button  :icon="Edit" circle type="primary" @click="handleUpdate(scope.row)"></el-button>
            <el-button  :icon="Delete" circle type="danger" @click="deleteById(scope.row.id)"></el-button>
          </template>
        </el-table-column>
<!--        <el-table-column label="部门" prop="departmentName"></el-table-column>-->
      </el-table>
      <div class="card" style="margin-bottom: 5px">
        <el-pagination
            @current-change="load"
            @size-change="load"
            v-model:current-page="data.pageNum"
            v-model:page-size="data.pageSize"
            :page-sizes="[5, 10, 15, 20]"
            layout="total, sizes, prev, pager, next, jumper"
            :total="data.total"
        />

      </div>
    </div>


    <el-dialog title="管理员信息" v-model="data.formVisible" width="500" destroy-on-close>
      <el-form ref="formRef" :rules="data.rules" :model="data.form" label-width="80px" style="padding-right: 50px;padding-top:20px">
        <el-form-item label="账号" prop="username">
          <el-input :disabled="data.form.id" v-model="data.form.username" autocomplete="off" />
        </el-form-item>

        <el-form-item label="头像" prop="username">
          <el-upload
              action="http://localhost:8080/files/upload"
              list-type="picture"
              :on-success="handleAvatarSuccess"
          >
            <el-button type="primary">上传头像</el-button>
          </el-upload>
        </el-form-item>

        <el-form-item label="名称" prop="name">
          <el-input v-model="data.form.name" autocomplete="off" />
        </el-form-item>



      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="data.formVisible = false">取消</el-button>
          <el-button type="primary" @click="save">
           保存
          </el-button>
        </div>
      </template>
    </el-dialog>

  </div>

</template>


<script setup>
import {reactive,ref} from "vue";
import {Search,Edit,Delete} from "@element-plus/icons-vue"
import request from "@/utils/request.js";
import {ElMessage, ElMessageBox} from "element-plus";

const data = reactive({
  name: null,
  tableData:[],
  pageNum:1,
  pageSize:10,
  total:0,
  formVisible:false,
  rules:{
    username: [
      { required: true, message: '请输入账号', trigger: 'blur' },
      { min: 1, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
    ],
    name: [
      { required: true, message: '请输入名称', trigger: 'blur' },
      { min: 1, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
    ],
  },
  form:[
      {
        username:'',
        name:'',
        sex:'',
        no:'',
        age:'',
        description:''
      }
  ],
  ids:[]

})
const formRef = ref(null)

const load=()=>{
  request.get('/admin/selectPage',{
    params:{
      pageNum:data.pageNum,
      pageSize:data.pageSize,
      name:data.name
    }
  }).then(res=>{
    console.log(res.data)
    data.tableData=res.data.list
    data.total=res.data.total

  })
}

const handleAvatarSuccess = (res) => {
  console.log(res.data)
  data.form.avatar = res.data
}

load()
const reset=()=>{
  data.name=null
  load()
}
const handleAdd=()=>{
  data.formVisible=true
  data.form={}
}
const save=()=>{ //需要在一个方法内写新增和编辑的功能
  formRef.value.validate(valid=>{
    if(valid){
      data.form.id ? update() : add()//判断是否有id
    }
  })
}

const add=()=>{
  request.post('/admin/add',data.form).then(res=>{
    if(res.code==='200'){
      ElMessage.success('操作成功')
      load() //刷新列表
      data.formVisible=false
    }else{
      ElMessage.error(res.msg)
    }
  })
}

const update=()=>{
  request.put('/admin/update',data.form).then(res=>{
    if(res.code==='200'){
      ElMessage.success('操作成功')
      load() //刷新列表
      data.formVisible=false
    }else{
      ElMessage.error(res.msg)
    }
  })
}
// 编辑
const handleUpdate=(row)=>{
  data.form=JSON.parse(JSON.stringify(row))//深拷贝
  data.formVisible=true
}

// 删除
const deleteById=(id)=>{
  ElMessageBox.confirm('此操作将永久删除该文件, 是否继续?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    // 确认删除
    request.delete('/admin/delete/'+id).then(res=>{
      if(res.code==='200'){
        ElMessage.success('操作成功')
        load() //刷新列表
      }else{
        ElMessage.error(res.msg)
      }
    })
  }).catch(() => {
    // 取消删除
    ElMessage({
      type: 'info',
      message: '已取消删除'
    });
  });
}

const handleSelectionChange = (rows) => { // 返回所有选中的行对象数组
  data.ids=rows.map(row=>row.id)
  console.log(data.ids)
}

const deleteBatch = () => {
  if (data.ids.length === 0) {
    ElMessage.warning('请选择数据');
    return;
  }

  // 弹出确认框
  ElMessageBox.confirm(
      '删除数据后无法恢复，您确认删除吗？',
      '确认',
      {
        type: 'warning',
        confirmButtonText: '确定',
        cancelButtonText: '取消'
      }
  ).then(() => {
    // 确认删除后，执行删除操作
    request.delete('/admin/deleteBatch', { data: data.ids })
        .then(res => {
          if (res.code === '200') {
            ElMessage.success('操作成功');
            load(); // 刷新列表
          } else {
            ElMessage.error(res.msg);
          }
        })
        .catch(() => {
          ElMessage({
            type: 'info',
            message: '已取消删除'
          });
        });
  }).catch(() => {
    // 取消删除
    ElMessage({
      type: 'info',
      message: '已取消删除'
    });
  });
}

</script>
