<template>
  <div>
    <!-- 顶部开始 -->
  <div style="height: 60px;background-color:#6b6bff;display: flex;align-items:center;">
  <div style="width: 200px;align-content: center;display: flex;padding-left: 15px">
    <img src="@/assets/logo.png" style="width: 40px">
    <span style="color:white;font-size: 20px;padding-top: 7px">后台管理系统</span>
  </div>
  <div style="flex:1;"></div>
  <div style="width:fit-content;display:flex;align-items:center;padding-right:10px">
    <img :src="data.user.avatar || 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'"style="width:40px;height:40px;border-radius: 50%">
    <span style="color:white;margin-left: 5px;font-size: 12px">{{data.user.name}}</span>
  </div>
  </div>

    <!-- 导航 -->
  <div style="display: flex;">
    <!--左侧导航侧边栏开始-->
    <div style="width: 200px;border-right:1px solid #ccc; height:calc(100vh - 60px)">
    <el-menu router :default-active="router.currentRoute.value.path" style="border:0">
      <el-menu-item index="/manager/home">
        <el-icon><House/></el-icon>系统首页
      </el-menu-item>
      <el-menu-item index="/manager/data">
        <el-icon><Reading/></el-icon>数据统计
      </el-menu-item>
      <el-menu-item index="/manager/article">
        <el-icon><DataAnalysis/></el-icon>文章管理
      </el-menu-item>
      <el-menu-item index="/manager/department">
        <el-icon><OfficeBuilding/></el-icon>部门信息
      </el-menu-item>
      <div v-if="data.user.role==='ADMIN'">
      <el-sub-menu index="1">
        <template #title>
          <el-icon><User /></el-icon>
          <span>用户管理</span>
        </template>
        <el-menu-item index="/manager/admin">管理员信息</el-menu-item>
        <el-menu-item index="/manager/employee">员工信息</el-menu-item>
      </el-sub-menu>
      </div>
      <el-menu-item index="/manager/person" >
        <el-icon><UserFilled/></el-icon>个人信息
      </el-menu-item>

      <el-menu-item index="/manager/password" >
        <el-icon><Lock/></el-icon>修改密码
      </el-menu-item>

      <el-menu-item @click="logout" >
        <el-icon><SwitchButton/></el-icon>退出登录
      </el-menu-item>

    </el-menu>
    </div>
    <!--左侧导航侧边栏结束-->
    <!--右侧开始-->
    <div style="flex:1;width:0;background-color: #f2f6ff;padding:10px ">
    <RouterView @updateUser="updateUser" />
    </div>

    <!--右侧结束-->
  </div>
  <!-- 底部结束-->

  </div>
</template>


<script setup>
import router from "@/router/index.js";
import {reactive} from "vue";
import {Lock, OfficeBuilding, Reading } from "@element-plus/icons-vue";

const data=reactive({
    user:JSON.parse(
        localStorage.getItem("xm-pro-user"))
})

const logout = () => {
  localStorage.removeItem("xm-pro-user")//删除本地存储
  location.href = "/login"//跳转到登录页
}

const updateUser=()=>{
  data.user = JSON.parse(localStorage.getItem("xm-pro-user"));
}
</script>

<style scoped>
.el-menu-item.is-active {
  background-color: rgb(231, 238, 255) !important;
}
.el-sub-menu.is-active > .el-sub-menu__title {
  background-color: transparent !important;
}
</style>