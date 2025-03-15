<template>
  <div class="login-container">
    <div class="login-box">
      <div style="padding:50px 30px;background:white;margin-right:100px;border-radius: 10px;box-shadow: 0 0 10px ">
      <el-form ref="formRef"  :rules="data.rules" :model="data.form" style="width: 300px">
        <div style="margin-bottom: 30px;font-size: 24px;text-align: center;color: #6b6bff;font-weight: bold">欢 迎 注 册</div>
        <el-form-item prop="username">
          <el-input size="large" v-model="data.form.username" placeholder="请输入账号" :prefix-icon="User"></el-input>
        </el-form-item>
        <el-form-item prop="no">
          <el-input size="large" v-model="data.form.no" placeholder="工号" :prefix-icon="User"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input size="large" show-password v-model="data.form.password" placeholder="请输入密码" :prefix-icon="Lock"></el-input>
        </el-form-item>
        <el-form-item prop="confirmPassword">
          <el-input size="large" show-password v-model="data.form.confirmPassword" placeholder="确认密码" :prefix-icon="Lock"></el-input>
        </el-form-item>
        <div style="margin-bottom: 20px">
          <el-button type="primary" style="width: 100%;margin-bottom: 10px" @click="register">注册</el-button>
        </div>
        <div style="text-align:right;">已有账号？请<a style="color: #1256ff;text-decoration: none" href="/login">登录</a></div>
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

const validatePass = (rule, value, callback) => {
  if (!value) {
    callback(new Error('请确认密码'))
  } else if (value !== data.form.password) {
    callback(new Error("两次密码不一致"))
  } else {
    callback()
  }
}

const data = reactive({
  form: {
    username: '',
    password: '',
    confirmPassword: '',
    no:''
  },
  rules: {
    username: [
      { required: true, message: '请输入账号', trigger: 'blur' },
      { min: 3, max: 18, message: '长度在 3 到 10 个字符', trigger: 'blur' }
    ],
    password: [
      { required: true, message: '请输入密码', trigger: 'blur' },
      { min: 3, max: 18, message: '长度在 3 到 10 个字符', trigger: 'blur' }
    ],
    confirmPassword: [
      {
       validator:validatePass,
       trigger: 'blur'
      }
    ],
    no:[
      { required: true, message: '请输入工号', trigger: 'blur' },
      { min: 3, max: 20, message: '长度在 3 到 10 个字符', trigger: 'blur' }
    ]
  }
})
const formRef = ref()


const register=()=>{
  formRef.value.validate((valid) => {
    if (valid) {
      request.post('/register', data.form).then(res => {
        if (res.code === '200') {
          ElMessage.success('注册成功')
          setTimeout(() => {
            location.href = '/login'
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
  background-position: 0 -50px;

}

.login-box {
  width: 80%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: flex-start;
  right: 0;
  position: absolute;
}
</style>
