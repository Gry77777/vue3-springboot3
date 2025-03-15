<template>



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