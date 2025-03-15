<template>

  <div>
    <div class="card" style="margin-bottom: 5px">
      <el-input v-model="data.title" style="width: 240px;margin-right:10px" placeholder="请输入标题"
                prefix-icon="Search"></el-input>
      <el-button type="primary" @click="load">查询</el-button>
      <el-button type="warning" @click="reset">重置</el-button>
    </div>
    <div class="card" style="margin-bottom: 5px">
      <el-button type="primary" @click="handleAdd">新增</el-button>
      <el-button type="danger" @click="deleteBatch">批量删除</el-button>
    </div>

    <div class="card" style="margin-bottom: 5px">
      <el-table :data="data.tableData" stripe @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="50px"/>
        <el-table-column label="标题" prop="title"></el-table-column>
        <el-table-column label="封面">
          <template #default="scope">
            <el-image v-if="scope.row.img" :src="scope.row.img" :preview-src-list=[scope.row.img] preview-teleported
                      style="width: 160px;height: 100px;display: block;"/>
          </template>
        </el-table-column>
        <el-table-column label="简介" prop="description" show-overflow-tooltip></el-table-column>
        <el-table-column label="内容" prop="content" show-overflow-tooltip>
          <template #default="scope">
            <el-button type="primary" @click="view(scope.row.content)">查看内容</el-button>
          </template>
        </el-table-column>
        <el-table-column label="发布时间" prop="time" width="120px"></el-table-column>
        <el-table-column label="操作" width="120px">
          <template #default="scope">
            <el-button :icon="Edit" circle type="primary" @click="handleUpdate(scope.row)"></el-button>
            <el-button :icon="Delete" circle type="danger" @click="deleteById(scope.row.id)"></el-button>
          </template>
        </el-table-column>
        <!--        <el-table-column label="部门" prop="departmentName"></el-table-column>-->
        <!--      </el-table>-->
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
      </el-table>
    </div>


    <el-dialog title="文章信息" v-model="data.formVisible" width="800" destroy-on-close>
      <el-form ref="formRef" :model="data.form" label-width="80px" style="padding-right: 50px;padding-top:20px">
        <el-form-item label="标题" prop="title">
          <el-input v-model="data.form.title" autocomplete="off"/>
        </el-form-item>

        <el-form-item label="封面">
          <el-upload
              action="http://localhost:8080/files/upload"
              list-type="picture"
              :on-success="handleImgSuccess"
          >
            <el-button type="primary">上传封面</el-button>
          </el-upload>
        </el-form-item>

        <el-form-item label="简介" prop="description">
          <el-input type="textarea" :rows="3" v-model="data.form.description" autocomplete="off"/>
        </el-form-item>

        <el-form-item label="内容">

          <div style="border: 1px solid #ccc;width: 100%">
            <Toolbar
                style="border-bottom: 1px solid #ccc"
                :editor="editorRef"
                :mode="mode"
            />
            <Editor
                style="height: 500px;overflow-y: hidden;"
                v-model="data.form.content"
                :mode="mode"
                :defaultConfig="editorConfig"
                @onCreated="handleCreated"
            />
          </div>

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

    <el-dialog title="查看内容" v-model="data.viewVisible" width="50%" :close-on-click-modal="false" destroy-on-close>
      <div class="editor-content-view" style="padding: 20px" v-html="data.content">

      </div>
      <template >
        <span class="dialog-footer">
        <el-button @click="data.viewVisible = false">关闭</el-button>
        </span>
      </template>
    </el-dialog>
  </div>

</template>


<script setup>
import request from "@/utils/request.js";
import router from "@/router/index.js";
import {Search, Calendar, Edit,Delete} from '@element-plus/icons-vue'//导入图标
import '@wangeditor/editor/dist/css/style.css';
import {onBeforeUnmount, ref, shallowRef, reactive} from "vue";
import {Editor, Toolbar} from '@wangeditor/editor-for-vue';
import {ElMessage, ElMessageBox} from "element-plus";
import '@/assets/view.css'

const data = reactive({
  title: null,
  tableData: [],
  pageNum: 1,
  pageSize: 10,
  total: 0,
  formVisible: false,
  form: [
    {
      username: '',
      name: '',
      sex: '',
      no: '',
      age: '',
      description: ''
    }
  ],
  ids: [],
  content: '',
  viewVisible: false,

})
const view = (content) => {
  data.content = content
  data.viewVisible = true
}
const baseUrl = 'http://localhost:8080';
const editorRef = shallowRef();
const mode = 'default';
const editorConfig = {MENU_CONF: {}};
editorConfig.MENU_CONF['uploadImage'] = {
  server: baseUrl + '/files/wang/upload',
  fieldName: 'file',
  onSuccess: (file, res) => {
    console.log('上传成功', res);
  },
  onFailed: (file, res) => {
    console.error('上传失败', res);
  }
}
onBeforeUnmount(() => {
  const editor = editorRef.value;
  if (editor == null) {
    return;
  }
  editor.destroy();
})
const handleCreated = (editor) => {
  editorRef.value = editor;
}


const load = () => {
  request.get('/article/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      name: data.name
    }
  }).then(res => {
    console.log(res.data)
    data.tableData = res.data.list
    data.total = res.data.total

  })
}

const handleImgSuccess = (res) => {
  console.log(res.data)
  data.form.img = res.data
}

load()
const reset = () => {
  data.name = null
  load()
}
const handleAdd = () => {
  data.formVisible = true
  data.form = {}
}
const save = () => { //需要在一个方法内写新增和编辑的功能
data.form.id ? update() : add()
}

const add = () => {
  request.post('/article/add', data.form).then(res => {
    if (res.code === '200') {
      ElMessage.success('操作成功')
      load() //刷新列表
      data.formVisible = false
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const update = () => {
  request.put('/article/update', data.form).then(res => {
    if (res.code === '200') {
      ElMessage.success('操作成功')
      load() //刷新列表
      data.formVisible = false
    } else {
      ElMessage.error(res.msg)
    }
  })
}
// 编辑
const handleUpdate = (row) => {
  data.form = JSON.parse(JSON.stringify(row))//深拷贝
  data.formVisible = true
}

// 删除
const deleteById = (id) => {
  ElMessageBox.confirm('此操作将永久删除该文件, 是否继续?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    // 确认删除
    request.delete('/article/delete/' + id).then(res => {
      if (res.code === '200') {
        ElMessage.success('操作成功')
        load() //刷新列表
      } else {
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
  data.ids = rows.map(row => row.id)
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
    request.delete('/article/deleteBatch', {data: data.ids})
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
