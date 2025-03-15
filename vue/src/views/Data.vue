<template>
  <div style="display: flex;grid-gap:10px;margin-bottom: 10px">
    <div class="card" style="padding:20px;width: 50%;height: 400px;"id="bar"></div>
    <div class="card" style="padding:20px;width: 50%;height: 400px;"id="line"></div>
  </div>

  <div style="display: flex;grid-gap: 10px">
    <div class="card" style="padding:20px;width: 50%;height: 400px;"id="pie"></div>
    <div class="card" style="padding:20px;width: 50%;height: 400px;"id="pie1"></div>
  </div>
</template>

<script setup>
import { reactive, onMounted } from "vue";
import * as echarts from 'echarts';
import request from "@/utils/request.js";


const barOption = {
  title: {
    text: '各部门员工数量'
  },
  tooltip: {},
  legend: {
    data: ['人数']
  },
  xAxis: {
    data: []
  },
  yAxis: {},
  series: [
    {
      name: '人数',
      type: 'bar',
      data: [],
      itemStyle: {
        normal:{
          color: function(params) {
            //自定义颜色
            const colorList = ['#FF0000', '#FFA500', '#FFFF00', '#008000', '#0000FF', '#4B0082', '#EE82EE'];
            return colorList[params.dataIndex];
          }
        }
      }
    }
  ]
}

const lineOption = {
  title: {
    text: '近七天发布文章的数量'
  },
  tooltip: {},
  legend: {
   trigger: 'item',
  },
  xAxis: {
    data: []
  },
  yAxis: {},
  series: [
    {
      name: '发布文章的数量',
      type: 'line',
      data: [],
      smooth: true,
    }
  ]
}

const pieOption = {
  title: {
    text: '各部门员工数量比例',
    left: 'center'
  },
  tooltip: {
    trigger: 'item'
  },
  legend: {
    orient: 'vertical',
    left: 'left'
  },
  series: [
    {
      name: '员工数量',
      type: 'pie',
      radius: '50%',
      data: [],
      center: ['50%', '50%'],
      label: {
        formatter: '{b}: {d}%'
      },
      emphasis: {
        itemStyle: {
          shadowBlur: 10,
          shadowOffsetX: 0,
          shadowColor: 'rgba(0, 0, 0, 0.5)'
        }
      }
    }
  ]
}


onMounted(() => {
  const barChart = echarts.init(document.getElementById('bar'));
  request.get('/barData').then(res=>{
    barOption.xAxis.data=res.data.department
    barOption.series[0].data=res.data.count
    barChart.setOption(barOption);
  })

  const lineChart = echarts.init(document.getElementById('line'));
  request.get('/lineData').then(res=>{
    lineOption.xAxis.data=res.data.date
    lineOption.series[0].data=res.data.count
    lineChart.setOption(lineOption);
  })

  const pieChart = echarts.init(document.getElementById('pie'));
  request.get('/pieData').then(res=>{
    pieOption.series[0].data=res.data
    pieChart.setOption(pieOption);
  })

})
</script>

<style scoped>
</style>
