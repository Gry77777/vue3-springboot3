<script setup>

</script>

<template>
<div>
  <el-button type="primary" @click="router.back()">返回主页</el-button>
</div>
  <div>
    <router-link to="/manager/home">
      home
    </router-link>
  </div>
  <div style="background-color: red;font-size: 20px;font-weight: bold;">
    hello!!!!!!!!
  </div>

  <div>
    {{a}}
    {{b}}
    {{data.name}}
    {{data.age}}
  </div>
  <div>
    <input v-model="data.name">
  </div>
  <div>
    <select style="width: 100px;">
      <option v-for="item in data.fruit">{{item}}</option>
    </select>
  </div>
  <div v-for="item in data.shuzi" >
    {{item}}
  </div>
  <div>
    <button @click="click('你好')">{{message}}</button>
  </div>

  <div :style="data.box">

  </div>

  <div>
    <el-row class="mb-4">
      <el-button @click="click('nihao')":style="{color:'red'}">默认{{message}}</el-button>
      <el-button type="primary" loading>Primary</el-button>
      <el-button type="success">Success</el-button>
      <el-button type="info">Info</el-button>
      <el-button type="warning">Warning</el-button>
      <el-button type="danger">Danger</el-button>
      <el-button>中文</el-button>
    </el-row>
  </div>
  <div style="margin:30px">


<span>
<el-icon :size="20">
   <el-icon><View /></el-icon>100
  </el-icon>
</span>
    <el-button type="primary" icon="Delete" circle></el-button>
  </div>
</template>
<script setup>
import {ref,reactive,onMounted} from 'vue';
import { Delete,View } from '@element-plus/icons-vue'
import router from "@/router/index.js";

const a=ref(2);
const b=ref("nihao")
const data=reactive({
  id:router.currentRoute.value.query.id,
  name:router.currentRoute.value.query.name,
  age:18,
  fruit:["apple","banana","orange"],
  shuzi:[1,2,3,4,5,6,7,8,9,10],
  box:{
    height:'100px',
    width:'200px',
    backgroundColor:'red'
  }
})

onMounted(()=>{
  console.log('nihao')
})
const click=(message)=>{
  alert(message)
}
// alert("获取到的参数是"+data.id)
// alert("获取到的名字是"+data.name)
</script>

