<template>

  <div class="card" style="width: 50%;padding:40px 40px">
    <el-form ref="formRef" :rules="data.rules" :model="data.form" label-width="100px" style="padding-right: 50px;padding-top:20px">
      <el-form-item label="原密码" prop="password">
        <el-input show-password  v-model="data.form.password" placeholder="请输入原密码" autocomplete="off" />
      </el-form-item>
      <el-form-item label="新密码" prop="newPassword">
        <el-input show-password  v-model="data.form.newPassword" placeholder="请输入新密码" autocomplete="off" />
      </el-form-item>
      <el-form-item label="确认新密码" prop="confirmPassword" required>
        <el-input show-password  v-model="data.form.confirmPassword" placeholder="请确认密码" autocomplete="off" />
      </el-form-item>
      <div style="text-align: center">
        <el-button style="margin-top: 20px;" type="primary" @click="updatePassword">立即修改</el-button>
      </div>
    </el-form>
  </div>
</template>

<script setup>
import {reactive, ref} from 'vue'
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";

const validatePass = (rule, value, callback) => {
  if (!value) {
    callback(new Error('请再次输入密码'));
  } else {
    if (value!==data.form.newPassword) {
     callback(new Error('两次密码不一致'));
    }
    callback();
  }
};
const formRef =ref()
const data=reactive({
  form:{},
  user:JSON.parse(localStorage.getItem('xm-pro-user')),
  rules: {
    password: [
      { required: true, message: '请输入原密码', trigger: 'blur' },
    ],
    newPassword: [
      { required: true, message: '请输入新密码', trigger: 'blur' },
    ],
    confirmPassword: [
      { validator: validatePass, trigger: 'blur' },
    ]
  }
})

const updatePassword = () => {
  data.form.id = data.user.id;
  data.form.role = data.user.role;
  formRef.value.validate(valid => {
    console.log(valid);  // 打印验证结果
    if (valid) {
      request.put('/updatePassword', data.form).then(res => {
        if (res.code === '200') {
          ElMessage.success('修改成功');
          localStorage.removeItem('xm-pro-user');
          setTimeout(() => {
            location.href = '/login';
          }, 500);
        } else {
          ElMessage.error(res.msg);
        }
      });
    }
  });
};

</script>
