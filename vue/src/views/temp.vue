<template>

  <div style="margin: 50px">
    <router-link to="/test">
      跳转
    </router-link>

  </div>

  <div>
    <el-button type="primary" @click="router.push('/manager/test')">无参跳转到新页面</el-button>
  </div>

  <div style="margin: 50px">
    <el-button type="primary" @click="router.push('/manager/test?id=2&name=小古')">传递参数id和名字</el-button>
  </div>
  <div style="margin: 50px">
    <el-button type="primary" @click="router.push({path: '/manager/test',query:{id:2,name:'小古'}})">传递参数id和名字</el-button>
    <!--    第二种传递参数-->
  </div>
  <div>
    <el-input v-model="data.input" style="width: 240px" placeholder="请输入内容" :prefix-icon="Search" />
    <el-input  style="width: 240px" placeholder="请输入内容" :suffix-icon="Calendar" />
  </div>
  <el-select
      multiple
      v-model="data.value"
      placeholder="Select"
      size="large"
      style="width: 240px"
  >
    <el-option
        v-for="item in data.options"
        :key="item.value"
        :label="item.label"
        :value="item.value"
    />
  </el-select>{{data.value}}

  <el-input
      v-model="data.des"
      style="width: 240px"
      :rows="2"
      type="textarea"
      placeholder="Please input"
  />

  <div>

    <el-radio-group v-model="data.sex">
      <!-- works when >=2.6.0, recommended ✔️ not work when <2.6.0 ❌ -->
      <el-radio value="男">男</el-radio>
      <!-- works when <2.6.0, deprecated act as value when >=3.0.0 -->
      <el-radio value="女">女</el-radio>
    </el-radio-group> {{data.sex}}


    <el-radio-group style="margin-left: 100px" v-model="data.tag" size="large">
      <el-radio-button label="New York" value="1" />
      <el-radio-button label="Washington" value="2" />
      <el-radio-button label="Los Angeles" value="3" />
      <el-radio-button label="Chicago" value="4" />
    </el-radio-group>
    <!--    v-model的值和value的值对应上即可-->
  </div>

  <div>
    <el-image
        style="width: 100px; height: 100px"
        :src="data.url"
        :zoom-rate="1.2"
        :max-scale="7"
        :min-scale="0.2"
        :preview-src-list="data.tupian"
        show-progress
        :initial-index="4"
        fit="cover"
    />
  </div>

  <div>
    <el-carousel indicator-position="outside">
      <el-carousel-item v-for="item in data.tupian" :key="item">
        <el-image
            style="width: 100%; height: 100%"
            :src="item"
            fit="cover"
        />
      </el-carousel-item>
    </el-carousel>
  </div>

  <div>
    <el-date-picker
        v-model="data.date"
        type="datetime"
        placeholder="请选择你的日期"
        format="YYYY/MM/DD"
        value-format="YYYY/MM/DD"
    />{{data.date}}

    <el-date-picker
        v-model="data.date1"
        type="datetimerange"
        start-placeholder="开始"
        end-placeholder="结束"
        format="YYYY-MM-DD HH:mm:ss"
        date-format="YYYY/MM/DD ddd"
        time-format="A hh:mm:ss"
    />{{data.date1}}

  </div>

  <div>

    <el-table :data="data.tableData" stripe style="width: 100%">
      <el-table-column prop="date" label="Date" width="180"/>
      <el-table-column prop="name" label="Name" width="180"/>
      <el-table-column prop="address" label="Address"/>
      <el-table-column prop="content" label="内容">
        <template #default="scope">
          <div v-html="scope.row.content"></div>
        </template>
      </el-table-column>
      <el-table-column label="操作">
        <template #default="scope">
          <el-button type="primary"  @click="editContent(scope.row)">
            编辑富文本
          </el-button>
          <el-button type="primary" circle @click="edit(scope.row)">
            <el-icon>
              <Edit/>
            </el-icon>
          </el-button>
          <el-button type="danger" circle @click="del(scope.row.id)">
            <el-icon>
              <Delete/>
            </el-icon>
          </el-button>
        </template>
      </el-table-column>

    </el-table>


    <el-pagination
        v-model:current-page="data.currentPage"
        v-model:page-size="data.pageSize"
        :page-sizes="[5, 10, 15, 20]"
        :background="background"
        layout="total, sizes, prev, pager, next, jumper"
        :total="data.tableData.length"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"

    />
  </div>

  <el-dialog v-model="data.DialogVisible" title="编辑行对象" width="500" center>
    <div>
      <p>日期{{data.row.date}}</p>
      <p>地址{{data.row.address}}</p>
      <p>姓名{{data.row.name}}</p>
    </div>
  </el-dialog>

  <el-dialog v-model="data.formContentVisible" title="编辑行对象" width="800" center>
    <div style="padding: 20px">
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
    </div>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="data.formVisible = false">取消</el-button>
        <el-button type="primary" @click="saveContent">
          保存
        </el-button>
      </div>
    </template>
  </el-dialog>



</template>
<script setup>
import request from "@/utils/request.js";
import router  from "@/router/index.js";
import {reactive}  from "vue";
import {Search,Calendar,Edit} from '@element-plus/icons-vue'//导入图标
import '@wangeditor/editor/dist/css/style.css';
import {onBeforeUnmount ,ref,shallowRef} from "vue";
import {Editor,Toolbar} from '@wangeditor/editor-for-vue';

const data=reactive({
  input:null,
  value:null,
  options:[
    {
      value: '选项1',
      label: '黄金糕'
    },
    {
      value: '选项2',
      label: '双皮奶'
    },
    {
      value: '选项3',
      label: '蚵仔煎'
    },
    {
      value: '选项4',
      label: '龙须面'
    },
    {
      value: '选项5',
      label: '北京烤鸭'
    }
  ],
  des:'Setting the autosize prop for a textarea type of Input makes the height to automatically adjust based on the content. An options object can be provided to autosize to specify the minimum and maximum number of lines the textarea can automatically adjust.',
  sex:'',
  tag:'1',
  url:'https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg',
  tupian:[
    'https://fuss10.elemecdn.com/a/3f/3302e58f9a181d2509f3dc0fa68b0jpeg.jpeg',
    'https://fuss10.elemecdn.com/1/34/19aa98b1fcb2781c4fba33d850549jpeg.jpeg',
    'https://fuss10.elemecdn.com/0/6f/e35ff375812e6b0020b6b4e8f9583jpeg.jpeg',
    'https://fuss10.elemecdn.com/9/bb/e27858e973f5d7d3904835f46abbdjpeg.jpeg',
    'https://fuss10.elemecdn.com/d/e6/c4d93a3805b3ce3f323f7974e6f78jpeg.jpeg',
    'https://fuss10.elemecdn.com/3/28/bbf893f792f03a54408b3b7a7ebf0jpeg.jpeg',
    'https://fuss10.elemecdn.com/2/11/6535bcfb26e4c79b48ddde44f4b6fjpeg.jpeg',
  ],
  date:null,
  date1:null,
  tableData:[
    {id:1,
      date: '2016-05-02',
      name: '王小虎',
      address: '上海市普陀区金沙江路 1518 弄'
      ,content:'我是内容1'
    },
    {id:2,
      date: '2016-05-04',
      name: '王小虎',
      address: '上海市普陀区金沙江路 1517 弄'
      ,content:'我是内容2'
    },
    {id:3,
      date: '2016-05-01',
      name:'王小虎',
      address: '上海市普陀区金沙江路 1519 弄'
    }
  ],
  employeeList:[],
  currentPage: 1,
  pageSize:5,
  DialogVisible:false,
  row:null,
  form:{},
  formContentVisible:false
})

request.get('/employee/selectAll').then(res=>{
  console.log(res.data)
  data.employeeList=res.data
})
const del=(id)=>{
  alert("删除的id"+id)
}
const editContent = (row)=>{
  data.formContentVisible=true
  data.form=row
}

const saveContent=()=>{
  data.formContentVisible=false
}

const edit=(row)=>{
  data.row=row
  data.DialogVisible=true
}



const baseUrl = 'http://localhost:8080/';
const editorRef = shallowRef();
const mode = 'default';
const editorConfig = {MENU_CONF:{}};
editorConfig.MENU_CONF['uploadImage'] = {
  server: baseUrl + '/files/wang/upload',
  fieldName: 'file',
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

</script>