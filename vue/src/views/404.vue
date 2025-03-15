<template>
  <div>
<img src="@/assets/404.png">
  </div>
<div style="text-align: center">
  <el-button type="primary" @click="goHome">返回首页</el-button>
</div>
</template>
<script setup>
import router from "@/router/index.js";
const goHome = () => {
  router.push("/manager/home");
};

</script>