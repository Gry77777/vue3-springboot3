<template>
  <div class="login-container">
    <div class="login-box">
      <div style="padding:40px 30px;background:white;margin-right:100px;border-radius: 10px;box-shadow: 0 0 10px ">
      <el-form ref="formRef"  :rules="data.rules" :model="data.form" style="width: 300px">
        <div style="margin-bottom: 30px;font-size: 24px;text-align: center;color: #5959ff;font-weight: bold">欢迎登录后台管理系统</div>
        <el-form-item prop="username">
          <el-input size="large" v-model="data.form.username" placeholder="请输入账号" :prefix-icon="User"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input size="large" show-password v-model="data.form.password" placeholder="请输入密码" :prefix-icon="Lock"></el-input>
        </el-form-item>

        <el-form-item prop="role">
          <el-select v-model="data.form.role" style="width: 100%" size="large">
         <el-option value="ADMIN" label="管理员"></el-option>
         <el-option value="EMP" label="员工"></el-option>
          </el-select>
        </el-form-item>

        <div style="margin-bottom: 20px">
          <el-button type="primary" style="width: 100%;margin-bottom: 10px" @click="login">登录</el-button>
        </div>
        <div style="text-align:right;">还没有账号？请<a style="color: #1256ff;text-decoration: none" href="/register">注册</a></div>
      </el-form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive ,ref} from 'vue'
import { User,Lock } from '@element-plus/icons-vue'
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";

const data = reactive({
  form: {
    username: '',
    password: '',
    role:'ADMIN'

  },
  rules: {
    username: [
      { required: true, message: '请输入账号', trigger: 'blur' },
      { min: 3, max: 15, message: '长度在 3 到 10 个字符', trigger: 'blur' }
    ],
    password: [
      { required: true, message: '请输入密码', trigger: 'blur' },
      { min: 3, max: 20, message: '长度在 3 到 10 个字符', trigger: 'blur' }
    ]
  }
})
const formRef = ref()

const login=()=>{
  formRef.value.validate((valid) => {
    if (valid) {
      request.post('/login', data.form).then(res => {
        if (res.code === '200') {
          localStorage.setItem('xm-pro-user', JSON.stringify(res.data)) //将用户信息保存到本地,json对象转化为json字符串存储
          ElMessage.success('登录成功')
          setTimeout(() => {
            location.href = '/manager/home'
          },500)

        } else {
          ElMessage.error(res.msg)
        }
      })
    }
  })
}
</script>

<style scoped>
.login-container {
  height: 100vh;
  overflow: hidden;
  background-image: url('@/assets/bg1.png');
  background-size: cover;

}

.login-box {
  width: 75%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: flex-start;
  right: 0;
  position: absolute;
}
</style>
